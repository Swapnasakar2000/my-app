package com.example.user_service.service;

import com.example.user_service.dto.UserDTO;
import com.example.user_service.entity.User;
import com.example.user_service.exception.ResourceNotFoundException;
import com.example.user_service.repository.UserRepository;
import com.example.user_service.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Void createUser(List<User> userList) {
//        User user = mapToEntity(userDTO);
       userRepository.saveAll(userList);
        return null;
    }

    @Override
    public void deleteAllUsers() {
        userRepository.deleteAll();
    }

    // 🔁 Mapping Methods
    private UserDTO mapToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setPrimaryId(user.getId());
        dto.setNameOfTheStudent(user.getName());
        dto.setPrimaryEmail(user.getEmail());
        return dto;
    }

    private User mapToEntity(UserDTO dto) {
        User user = new User();
        user.setName(dto.getNameOfTheStudent());
        user.setEmail(dto.getPrimaryEmail());
        return user;
    }
}
