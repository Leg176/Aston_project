package ru.practicum.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.practicum.constans.NotificationConstants;

@RestController
@RequestMapping("/fallback")
public class FallbackController {

    @GetMapping("/users")
    public Mono<String> userFallback() {
        return Mono.just(NotificationConstants.USER_FALLBACK);
    }

    @GetMapping("/mail")
    public Mono<String> mailFallback() {
        return Mono.just(NotificationConstants.MAIL_FALLBACK);
    }
}
