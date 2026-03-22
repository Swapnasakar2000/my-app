package com.example.user_service.config;

import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.circuitbreaker.event.CircuitBreakerOnStateTransitionEvent;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.*;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("User Service API")
                        .version("1.0")
                        .description("API documentation for User Service"));
    }

    private final CircuitBreakerRegistry circuitBreakerRegistry;

    public SwaggerConfig(CircuitBreakerRegistry circuitBreakerRegistry) {
        this.circuitBreakerRegistry = circuitBreakerRegistry;
    }

    @PostConstruct
    public void registerStateChangeLogger() {
        circuitBreakerRegistry.circuitBreaker("userService")
                .getEventPublisher()
                .onStateTransition(event -> logState(event));
    }

    private void logState(CircuitBreakerOnStateTransitionEvent event) {
        System.out.println("CircuitBreaker '" + event.getCircuitBreakerName() +
                "' changed state from " + event.getStateTransition().getFromState() +
                " to " + event.getStateTransition().getToState());
    }
}
