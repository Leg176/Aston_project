package ru.practicum.service.producer;

public interface KafkaProducerService {

    void sendUserOperation(String email, String typeOperation);
}
