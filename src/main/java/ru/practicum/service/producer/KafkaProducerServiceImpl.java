package ru.practicum.service.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.practicum.config.KafkaConfig;

@Service
public class KafkaProducerServiceImpl implements KafkaProducerService {

    private static final Logger log = LoggerFactory.getLogger(KafkaProducerServiceImpl.class);
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final KafkaConfig kafkaConfig;

    public KafkaProducerServiceImpl(KafkaTemplate<String, String> kafkaTemplate, KafkaConfig kafkaConfig) {
        this.kafkaTemplate = kafkaTemplate;
        this.kafkaConfig = kafkaConfig;
    }

    @Override
    public void sendUserOperation(String email, String typeOperation) {
        String topic = kafkaConfig.getTopic();
        kafkaTemplate.send(topic, email, typeOperation);
        log.info("A message was sent to Kafka: topic = {}, email = {}, operation = {}", topic, email, typeOperation);
    }
}
