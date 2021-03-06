package br.lenkeryan.consumer;

import br.lenkeryan.topology.NotificationTopology;
import br.lenkeryan.topology.VaccineTopology;
import br.lenkeryan.utils.Constants;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class NotificationsStreamsConsumer {

    @Bean
    public Properties notificationsKafkaStreamsProps() {
        Properties properties = new Properties();
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG, "vaccine-prime-notifications");
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, Constants.BOOTSTRAP_SERVER);
        properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        properties.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, "0");
        return properties;
    }

    @Bean
    public static KafkaStreams notificationsKafkaStreams(@Qualifier("notificationsKafkaStreamsProps") Properties props) {
        var topology = NotificationTopology.buildTopology();
        var kafkaStreams = new KafkaStreams(topology, props);

        kafkaStreams.start();

        Runtime.getRuntime().addShutdownHook(new Thread(kafkaStreams::close));

        return kafkaStreams;
    }
}
