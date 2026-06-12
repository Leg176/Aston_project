package ru.practicum.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import ru.practicum.constans.NotificationConstants;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "spring.main.web-application-type=reactive",
                "spring.cloud.gateway.routes[0].id=main-service",
                "spring.cloud.gateway.routes[0].uri=lb://main-service",
                "spring.cloud.gateway.routes[0].predicates[0]=Path=/api/v1/users/**,/api/v1/users",
                "spring.cloud.gateway.routes[0].filters[0].name=CircuitBreaker",
                "spring.cloud.gateway.routes[0].filters[0].args.name=mainServiceCircuitBreaker",
                "spring.cloud.gateway.routes[0].filters[0].args.fallbackUri=forward:/fallback/users"
        }
)
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
