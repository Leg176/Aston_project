package ru.practicum.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import ru.practicum.constans.NotificationConstants;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FallbackControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void usersFallback_shouldReturnMessage() {
        webTestClient.get()
                .uri("/fallback/users")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo(NotificationConstants.USER_FALLBACK);
    }

    @Test
    void mailFallback_shouldReturnMessage() {
        webTestClient.get()
                .uri("/fallback/mail")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo(NotificationConstants.MAIL_FALLBACK);
    }
}
