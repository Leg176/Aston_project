package ru.practicum.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.utility.DockerImageName;
import ru.practicum.config.KafkaTestConfig;

import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

@Import(KafkaTestConfig.class)
@SpringBootTest
@Testcontainers
public class NotificationIntegrationTest {

    @Container
    static KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("apache/kafka:3.7.0"));

    @DynamicPropertySource
    static void kafkaProps(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
    }

    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;

    @SpyBean
    MailService mailService;

    @Test
    void shouldProcessKafkaEventAndSendEmail() {

        kafkaTemplate.send("user-events-topic", "test@mail.com", "CREATE");

        verify(mailService, timeout(5000).times(1)).process("test@mail.com", "CREATE");
    }
}