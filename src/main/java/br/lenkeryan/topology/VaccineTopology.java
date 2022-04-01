package br.lenkeryan.topology;

import br.lenkeryan.model.*;
import br.lenkeryan.utils.Constants;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.kafka.support.serializer.JsonSerde;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

public class VaccineTopology {
    static Logger logger = Logger.getLogger("VaccineLogger");

    public static Properties getProps() {
        Properties prop = new Properties();

        prop.setProperty(StreamsConfig.APPLICATION_ID_CONFIG, "vaccine-prime-vaccine");
        prop.setProperty(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, Constants.BOOTSTRAP_SERVER);
        prop.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        prop.setProperty(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.StringSerde.class.getName());
        prop.setProperty(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.StringSerde.class.getName());
        return prop;
    }

    public static Topology buildTopology(String topicName) {
        Serde<TemperatureInfo> TemperatureInfoSerdes = new JsonSerde<>(TemperatureInfo.class);
        Serde<Notification> notificationsSerde = new JsonSerde<>(Notification.class);
        StreamsBuilder streamsBuilder = new StreamsBuilder();

        KStream<String, TemperatureInfo> vaccineTemperatures = streamsBuilder.stream(topicName, Consumed.with(Serdes.String(), TemperatureInfoSerdes));

        vaccineTemperatures
                .peek((key, value) -> {
                    logger.info("Temperatura recebida: " + value.getValue() + "Para o hospital " + value.getProducerInfo().getHospital());
                    ProgramData.temperatureReads.add(value);
                })
                .filter((key, value) -> value.getProducerInfo() != null && value.getProducerInfo().getVaccines() != null)
                // Filtrando para pegar temperaturas que estão fora dos limites
                .filterNot((key, value) -> {
                    AtomicReference<Boolean> hasOutOfBounds = new AtomicReference<>(false);
                    value.getProducerInfo().getVaccines().stream().forEach(vaccine -> {
                        if (vaccine.getMaxTemperature() >= value.getValue() - Constants.TOLERANCE && vaccine.getMinTemperature() <= value.getValue() + Constants.TOLERANCE) {
                            hasOutOfBounds.set(true);
                        }
                    });
                    return hasOutOfBounds.get();
                })
                // Transformando informações de temperatura em notificações
                .mapValues(value -> {
                    var notification = analyseNotificationToSend(value);
                    return notification;
                })
                .filter((key, value) -> value != null)
                .peek((key, value) -> {
                    logger.info("Notification criada: " + value.getMessage());
                })
                // Jogando para o tópico de notificações
                .to(Constants.NOTIFICATIONS_TOPIC, Produced.with(Serdes.String(), notificationsSerde));

        return streamsBuilder.build();
    }


    private static Notification analyseNotificationToSend(TemperatureInfo tempInfo) {
        var knowFreezers = ProgramData.knowFreezers;
        var contains = ProgramData.returnIfFreezerExists(tempInfo.getProducerInfo().getId());
        if(!contains) {
            knowFreezers.put(tempInfo.getProducerInfo().getId(), tempInfo.getProducerInfo());
        }

        var now = Instant.now().getEpochSecond();
        TemperatureProducerInfo freezer = knowFreezers.get(tempInfo.getProducerInfo().getId());
        Boolean willNotificateWarning = false;
        Notification notification = null;

        for (int i = 0; i < freezer.getVaccines().size(); i++) {
            Vaccine vaccine = freezer.getVaccines().get(i);
            Boolean isTemperatureOutOfBounds = vaccine.checkIfTemperatureIsOutOfBounds(tempInfo.getValue());

            if(isTemperatureOutOfBounds) {
                // Temperatura fora do limite desejado
                // Fora por quanto tempo??
                if(vaccine.getLastTimeOutOfBounds().compareTo(0L) != 0) {
                    Long timeDifference = now - vaccine.getLastTimeOutOfBounds();
                    if (timeDifference >= vaccine.getMaxDuration() * 1000 * 3600) {
                        // Descarte
                        notification = new Notification(
                                NotificationType.DISCARD,
                                "Descarte a vacina " + vaccine.getBrand() + "da câmara de vacinas de id " + freezer.getId() + "do hospital " + freezer.getHospital() ,
                                false,
                                (ArrayList<ManagerInfo>) ProgramData.managers.values().stream().toList());
                        //                                this.sendNotification(notification, freezer);
                        logger.info("[VaccineConsumer] Criando notificação do tipo DISCARD de temperatura fora do limite por grande período de tempo!");
                    } else {
                        // Avisa gestor mais próximo
                        notification = sendWarnNotificationToNearestManager(tempInfo, freezer);
                        willNotificateWarning = notification != null;
                    }
                } else {
                    freezer.getVaccines().get(i).setLastTimeOutOfBounds(now);
                }
            } else {
                var isTemperatureNearOutOfBound = vaccine.checkIfTemperatureIsNearOutOfBounds(tempInfo.getValue());
                if (isTemperatureNearOutOfBound) {
                    willNotificateWarning = true;
                } else {
                    freezer.getVaccines().get(i).setLastTimeOutOfBounds(0L);
                }
            }
        }

        if (willNotificateWarning) {
            willNotificateWarning = false;
            if (notification.getNotificationType() == NotificationType.WARN) {
                logger.info("[VaccineConsumer] Criando notificação do tipo WARN de temperatura fora dos limites");
            } else {
                logger.info("[VaccineConsumer] Criando notificação do tipo CAUTION de temperatura fora dos limites");
            }
            return notification;
        }
        return null;
    }

    private static Notification sendWarnNotificationToNearestManager(TemperatureInfo info, TemperatureProducerInfo freezer) {
        var nearestManager = ProgramData.getNearestManager(info.getActualCoordinate());
        if(nearestManager == null) {
            logger.info("[VaccineConsumer] Não existe um Manager próximo conhecido! não foi possível criar uma notificação");
        } else {
            return new Notification(
                    NotificationType.WARN,
                    "Atenção " + nearestManager.getName() +  " A câmara de vacina de id " + freezer.getId() +"  do hospital " + freezer.getHospital() + " está com temperaturas fora do limite, por favor verifique",
                    nearestManager);
        }
        return null;
    }
}
