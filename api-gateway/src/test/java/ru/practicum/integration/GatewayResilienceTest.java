package ru.practicum.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import ru.practicum.constans.NotificationConstants;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GatewayResilienceTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void shouldReturnFallback_whenServiceDown() {

        webTestClient.get()
                .uri("/api/v1/users")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .value(body ->
                        body.contains(NotificationConstants.USER_FALLBACK));
    }
}
