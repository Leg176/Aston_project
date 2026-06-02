package ru.practicum.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImpl implements MailService {

    private static final Logger log = LoggerFactory.getLogger(MailServiceImpl.class);

    @Override
    public void process(String email, String operation) {

        switch (operation) {
            case "CREATE" -> sendCreatedMessage(email);
            case "DELETE" -> sendDeletedMessage(email);
            default -> throw new IllegalArgumentException("Неверный тип операции: " + operation);
        }
    }

    @Override
    public void sendCreatedMessage(String email) {
        System.out.println("Отправлено сообщение на email: " + email + ": \" Здравствуйте! Ваш аккаунт на сайте ваш сайт был успешно создан\"");
        log.info("Email sent: {} - Аккаунт создан", email);
    }

    @Override
    public void sendDeletedMessage(String email) {
        System.out.println("Отправлено сообщение на email: " + email + ": \"Здравствуйте! Ваш аккаунт был удалён\"");
        log.info("Email sent: {} - Аккаунт удалён", email);
    }
}
