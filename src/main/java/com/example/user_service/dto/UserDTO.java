package com.example.user_service.dto;

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
}
