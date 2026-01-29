package com.tasksoft.mark.mainservice.service;

import com.tasksoft.mark.mainservice.exception.UserNotFoundException;
import com.tasksoft.mark.mainservice.dto.UserCreateDto;
import com.tasksoft.mark.mainservice.entity.User;
import com.tasksoft.mark.mainservice.repository.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(UserCreateDto createUserDto) {
        User user = new User();
        user.setFirstName(createUserDto.firstName());
        user.setLastName(createUserDto.lastName());
        user.setUsername(createUserDto.username());
        return userRepository.save(user);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    public List<User> getUsersById(List<Long> users) {
        return userRepository.findAllById(users);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User syncUserFromJwt(Jwt jwt) {
        String userIdString = jwt.getClaimAsString("userId");
        if (userIdString == null) {
            throw new IllegalArgumentException("Token is missing 'userId' claim");
        }
        Long userId = Long.valueOf(userIdString);

        return userRepository.findById(userId).orElseGet(() -> createUserSafely(jwt, userId));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public User createUserSafely(Jwt jwt, Long userId) {
        try {
            User newUser = new User();
            newUser.setId(userId);
            newUser.setUsername(jwt.getClaimAsString("username"));
            newUser.setFirstName(jwt.getClaimAsString("firstName"));
            newUser.setLastName(jwt.getClaimAsString("lastName"));

            return userRepository.saveAndFlush(newUser);

        } catch (DataIntegrityViolationException e) {
            return userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User creation failed unexpectedly", e));
        }
    }
}
