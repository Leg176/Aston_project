package ru.practicum.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GatewayRoutingTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void shouldRouteRequest() {
        webTestClient.get()
                .uri("/api/v1/users")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void shouldRouteMailService() {
        webTestClient.get()
                .uri("/mail")
                .exchange()
                .expectStatus().isOk();
    }
}
