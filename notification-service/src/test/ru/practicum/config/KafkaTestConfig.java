package ru.practicum.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.TopicBuilder;

@TestConfiguration
public class KafkaTestConfig {

    @Bean
    public NewTopic userEventsTopic() {
        return TopicBuilder.name("user-events-topic")
                .partitions(1)
                .replicas(1)
                .build();
    }
}
