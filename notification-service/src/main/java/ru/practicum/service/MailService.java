package ru.practicum.service;

public interface MailService {
    void process(String email, String operation);

    void sendCreatedMessage(String email);

    void sendDeletedMessage(String email);
}
