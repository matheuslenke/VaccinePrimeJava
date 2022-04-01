package br.lenkeryan.topology;

import br.lenkeryan.model.ManagerCoordinates;
import br.lenkeryan.model.Notification;
import br.lenkeryan.model.ProgramData;
import br.lenkeryan.utils.Constants;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.springframework.kafka.support.serializer.JsonSerde;

import java.util.Properties;

public class NotificationTopology {

    public static Properties getProps() {
        Properties prop = new Properties();

        prop.setProperty(StreamsConfig.APPLICATION_ID_CONFIG, "vaccine-prime-notifications");
        prop.setProperty(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, Constants.BOOTSTRAP_SERVER);
        prop.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        prop.setProperty(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.StringSerde.class.getName());
        prop.setProperty(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.StringSerde.class.getName());
        return prop;
    }

    public static Topology buildTopology() {
        Serde<Notification> notificationsSerde = new JsonSerde<>(Notification.class);
        StreamsBuilder streamsBuilder = new StreamsBuilder();

        KStream<String, Notification> notificationsStream = streamsBuilder.stream(Constants.NOTIFICATIONS_TOPIC, Consumed.with(Serdes.String(), notificationsSerde));

        notificationsStream
                .peek((key, value) -> {
                    System.out.println(value.getMessage());
                        ProgramData.notifications.add(value);
                });

        return streamsBuilder.build();
    }

}
