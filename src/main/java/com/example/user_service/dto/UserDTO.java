package com.example.user_service.dto;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    @Schema(description = "User ID", example = "1")
    private Integer primaryId;
    @Schema(description = "User name", example = "Swapna")
    private String nameOfTheStudent;
    @Schema(description = "User email", example = "swapna@gmail.com")
    private String primaryEmail;

    public UserDTO fetchUserFallback(Integer id, Throwable ex) {
        if (ex instanceof CallNotPermittedException) {
            // Circuit breaker is OPEN
            throw new RuntimeException(
                    String.format("CircuitBreaker 'userService' is OPEN and does not permit further calls")
            );
        }

        // Normal fallback for other failures
        UserDTO fallbackUser = new UserDTO();
        fallbackUser.setPrimaryId(-1);
        fallbackUser.setNameOfTheStudent("Fallback User");
        fallbackUser.setPrimaryEmail("fallback@example.com");
        return fallbackUser;
    }
}
