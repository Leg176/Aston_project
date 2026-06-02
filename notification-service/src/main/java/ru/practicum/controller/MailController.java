package ru.practicum.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.service.MailService;

@RestController
@RequestMapping("/mail")
public class MailController {

    private final MailService mailService;
    private static final Logger log = LoggerFactory.getLogger(MailController.class);

    public MailController(MailService mailService) {
        this.mailService = mailService;
    }

    @PostMapping("/send")
    public void send(@RequestParam String email, @RequestParam String operation) {
        log.info("Вызов удаления/создания аккаунта.");
        mailService.process(email, operation);
    }
}
