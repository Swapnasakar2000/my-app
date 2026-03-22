package com.example.user_service.service;

import com.example.user_service.dto.UserDTO;
import com.example.user_service.entity.User;
import com.example.user_service.exception.ResourceNotFoundException;
import com.example.user_service.repository.UserRepository;
import com.example.user_service.service.UserService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @PersistenceContext
    private EntityManager entityManager;

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

    @Override
    public UserDTO fetchIndividualUser(Integer id) throws ResourceNotFoundException {

        // jpql way which direct connects to entity table not database table
        String query = "SELECT u FROM User u WHERE u.id = :id";

        User user = entityManager
                .createQuery(query, User.class)
                .setParameter("id", id)
                .getResultList().stream().findFirst().orElseThrow(() ->new ResourceNotFoundException("User is empty"));

        // native query way which connects to database table instead of entity table
        /*
        String query = "SELECT id, name, email FROM users WHERE id = :id";

    // Cast is necessary
    User user = (User) entityManager
            .createNativeQuery(query, User.class)
            .setParameter("id", id)
            .getSingleResult();
            */

        return mapToDTO(user);
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
