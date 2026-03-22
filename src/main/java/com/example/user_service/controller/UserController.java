package com.example.user_service.controller;

import com.example.user_service.entity.User;
import com.example.user_service.repository.UserRepository;
import com.example.user_service.dto.UserDTO;
import com.example.user_service.response.ApiResponse;
import com.example.user_service.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Tag(name = "User APIs", description = "Operations related to users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/fetch/id")
    public ResponseEntity<Object> hello(@RequestParam Integer id) {
        try {
            UserDTO userDTO = userService.fetchIndividualUser(id);
            return ResponseEntity.ok(userDTO);
        } catch (RuntimeException ex) {
            // Handle circuit breaker open
            if (ex.getMessage().contains("CircuitBreaker")) {
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(
                        Map.of(
                                "status", "error",
                                "message", ex.getMessage(),
                                "data", null
                        )
                );
            }
            // Other exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    Map.of(
                            "status", "error",
                            "message", ex.getMessage(),
                            "data", null
                    )
            );
        }
    }

    // Create a new user
    @Operation(summary = "Create a new user")
    @PostMapping("/save")
    public ResponseEntity<Void> createUser(@RequestBody List<User> userList) {

        logger.info("Received request to save {} users", userList.size());

        userService.createUser(userList);
        return ResponseEntity.status(HttpStatus.OK).build();

    }


    // Get all users
    @Operation(summary = "Get all users")
    @GetMapping("fetch")
    public ResponseEntity<ApiResponse> getAllUsers() {

        logger.info("Fetching all users from database");

        return ResponseEntity.ok(
                new ApiResponse("success", "User fetched", userService.getAllUsers()));

//        return ResponseEntity.status(HttpStatus.OK).body(userService.getAllUsers());
        // code for java stream
       /* List<UserRemote> userRemoteList = userRepository.findAll().stream()
                .map(user -> {
                    UserRemote userRemote = new UserRemote();
                    userRemote.setPrimaryId(user.getId());
                    userRemote.setNameOfTheStudent(user.getName());
                    userRemote.setPrimaryEmail(user.getEmail());
                    return userRemote;
                })
                .collect(Collectors.toList());
                */

    }
    @Operation(summary = "Delete all users")
    @DeleteMapping("/delete/all")
    public ResponseEntity<String> deleteAllUsers() {
        logger.info("deleting all users from users table");
        userService.deleteAllUsers();
        return ResponseEntity.status(HttpStatus.OK).body("All users deleted successfully");
    }
}
