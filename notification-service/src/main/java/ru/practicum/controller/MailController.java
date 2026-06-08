package ru.practicum.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.constans.ControllerApiConstants;
import ru.practicum.dto.MailRequestDto;
import ru.practicum.service.MailService;

@RestController
@RequestMapping("/mail")
@Validated
@Tag(name = ControllerApiConstants.TAG_NAME, description = ControllerApiConstants.TAG_DESCRIPTION)
public class MailController {

    private final MailService mailService;
    private static final Logger log = LoggerFactory.getLogger(MailController.class);

    public MailController(MailService mailService) {
        this.mailService = mailService;
    }

    @PostMapping("/send")
    @Operation(summary = ControllerApiConstants.PUSH_MESSAGES)
    public void send(@Valid @RequestBody MailRequestDto dto) {
        log.info("Вызов удаления/создания аккаунта.");
        mailService.process(dto.getEmail(), dto.getOperation().name());
    }
}
