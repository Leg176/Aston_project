package ru.practicum.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "spring.main.web-application-type=reactive",
                "eureka.client.enabled=false",
                "spring.cloud.gateway.routes[0].id=users",
                "spring.cloud.gateway.routes[0].uri=http://localhost:8081",
                "spring.cloud.gateway.routes[0].predicates[0]=Path=/api/v1/users/**",
                "spring.cloud.gateway.routes[1].id=mail",
                "spring.cloud.gateway.routes[1].uri=http://localhost:8082",
                "spring.cloud.gateway.routes[1].predicates[0]=Path=/mail/**"
        }
)
@AutoConfigureWebTestClient
class GatewayRoutingTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void shouldRouteUsersRequest() {
        webTestClient.get()
                .uri("/api/v1/users")
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    void shouldRouteMailRequest() {
        webTestClient.get()
                .uri("/mail")
                .exchange()
                .expectStatus().is5xxServerError();
    }
}