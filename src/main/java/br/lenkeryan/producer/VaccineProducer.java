package br.lenkeryan.producer;

import br.lenkeryan.config.KafkaTopicConfig;
import br.lenkeryan.model.Coordinate;
import br.lenkeryan.model.TemperatureInfo;
import br.lenkeryan.model.TemperatureProducerInfo;
import br.lenkeryan.model.Vaccine;
import br.lenkeryan.utils.Constants;
import br.lenkeryan.utils.CustomJsonReader;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VaccineProducer {
    static TemperatureProducerInfo producerInfo;
    static CustomJsonReader jsonReader = new CustomJsonReader();
    static Logger logger = Logger.getLogger("VaccineLogger");
    static Boolean temperatureOutOfBounds = true;
    static Double sleepingTime = 10.0;
    static Gson json = new Gson();

    public static void main(String[] args) {
        String filename = args[0];
        producerInfo = jsonReader.readTemperatureProducerJsonInfo(filename);
        temperatureOutOfBounds = Boolean.valueOf(args[1]);
        if (temperatureOutOfBounds) {
            logger.info("[VaccineProducer] Produzindo vacinas fora do limite de temperatura");
        } else {
            logger.info("[VaccineProducer] Produzindo vacinas dentro do limite de temperatura");
        }
        run();
    }

    public static void run() {
        KafkaProducer<String, String> producer = createProducer();

        while(true) {
            if(producerInfo == null) { return; }

            TemperatureInfo temperature = getTemperatureInfo();
            String data = json.toJson(temperature);
            ProducerRecord<String, String> record = new ProducerRecord<>(producerInfo.getHospital(), producerInfo.getId(), data);

            producer.send(record, (metadata, exception) -> {
                if (exception == null) {
                    logger.info("Producer: " + producerInfo.getId());
                    logger.info("Metadados recebidos \n " +
                            "Topic " + metadata.topic() + "\n " +
                            "Partition: " + metadata.partition() + "\n" +
                            "Offset: " + metadata.offset() + "\n" +
                            "Timestamp: " + metadata.timestamp());
                } else {
                    logger.log(Level.WARNING, exception.getLocalizedMessage());
                }
                try {
                    Thread.sleep((long) (sleepingTime * 1000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
    }


    private static KafkaProducer<String, String> createProducer() {
        Properties prop = new Properties();
        prop.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, Constants.BOOTSTRAP_SERVER);
        prop.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        prop.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        prop.setProperty(ProducerConfig.BATCH_SIZE_CONFIG, String.valueOf(16384));
        String topicName = producerInfo.getHospital();
        KafkaTopicConfig.CreateNewTopic(topicName);
        return new KafkaProducer<>(prop);
    }



    private static TemperatureInfo getTemperatureInfo() {
        Random random = new Random();
        Double latitude = random.nextDouble(-0.001, 0.001) + producerInfo.getInitialCoordinate().getLat();
        Double longitude = random.nextDouble(-0.001, 0.001) + producerInfo.getInitialCoordinate().getLon();
        Coordinate coord = new Coordinate(latitude, longitude);
        Double temp;
        if (temperatureOutOfBounds) {
            Vaccine vaccine = producerInfo.getVaccines().get(0);
                    temp = random.nextDouble(vaccine.getMaxTemperature(), vaccine.getMaxTemperature() + 10);
        } else {
            Vaccine vaccine = producerInfo.getVaccines().get(0);
                    temp = random.nextDouble(vaccine.getMinTemperature() , vaccine.getMaxTemperature());
        }
        return new TemperatureInfo(temp, producerInfo, coord);
    }

}
