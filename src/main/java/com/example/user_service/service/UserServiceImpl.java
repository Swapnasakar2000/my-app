package com.example.user_service.service;

import com.example.user_service.dto.UserDTO;
import com.example.user_service.entity.User;
import com.example.user_service.exception.ResourceNotFoundException;
import com.example.user_service.repository.UserRepository;
import com.example.user_service.service.UserService;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl implements UserService {

    private static final String USER_SERVICE = "userService";

    @PersistenceContext
    private EntityManager entityManager;

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "usersAll")
    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @CacheEvict(value = "usersAll", allEntries = true)
    public void createUser(List<User> userList) {
//        User user = mapToEntity(userDTO);
        userRepository.saveAll(userList);
    }

    @Override
    @CacheEvict(value = {"usersAll", "users"}, allEntries = true)
    public void deleteAllUsers() {
        userRepository.deleteAll();
    }

    @Transactional(readOnly = true)
    @Override
    @Cacheable(value = "users", key = "#id")
    @CircuitBreaker(name = USER_SERVICE,fallbackMethod = "fetchUserFallback")
    @Retry(name = USER_SERVICE)
    @RateLimiter(name = USER_SERVICE)
    public UserDTO fetchIndividualUser(Integer id) throws ResourceNotFoundException {
        //jpql way which direct connects to entity table not database table
        String query = "SELECT u FROM User u WHERE u.id = :id";

        User user = entityManager
                .createQuery(query, User.class)
                .setParameter("id", id)
                .getResultList().stream().findFirst().orElseThrow(() -> new ResourceNotFoundException("User is empty"));

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
//        throw new RuntimeException("Simulated failure");

    }

    // Fallback method for circuit breaker
    public UserDTO fetchUserFallback(Integer id, Throwable ex) {
        if (ex instanceof CallNotPermittedException) {
            System.out.println("Circuit breaker is OPEN!");
            // Optional: throw custom exception or return a special response
        }
        return new UserDTO(-1, "Fallback User", "fallback@example.com");
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
