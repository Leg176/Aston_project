package ru.practicum.service.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.practicum.service.MailService;

@Service
public class OrderKafkaConsumer {

    private final MailService mailService;
    private static final Logger log = LoggerFactory.getLogger(OrderKafkaConsumer.class);

    public OrderKafkaConsumer(MailService mailService) {
        this.mailService = mailService;
    }

    @KafkaListener(topics = "${app.kafka.topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void listen(ConsumerRecord<String, String> record) {

        String email = record.key();
        String typeOperation = record.value();

        log.info("Сообщение получено из Kafka: email = {}, operation = {}", email, typeOperation);

        mailService.process(email, typeOperation);
    }
}
