package br.lenkeryan.config;

import br.lenkeryan.utils.Constants;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic managersTopic() {
        return TopicBuilder.name(Constants.MANAGERS_TOPIC).build();
    }
}
