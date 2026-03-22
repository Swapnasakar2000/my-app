package com.example.user_service.service;

import com.example.user_service.dto.UserDTO;
import com.example.user_service.entity.User;

import java.util.List;

public interface UserService {

    List<UserDTO> getAllUsers();

    void createUser(List<User> userEntity);

    void deleteAllUsers();

    UserDTO fetchIndividualUser(Integer id);
}
