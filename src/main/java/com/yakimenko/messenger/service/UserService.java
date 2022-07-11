package com.yakimenko.messenger.service;

import com.yakimenko.messenger.entity.User;
import com.yakimenko.messenger.payload.request.SignupRequest;
import com.yakimenko.messenger.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserService {
    public static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    //метод для создания нового пользователя
    public User createUser(SignupRequest userIn){
        User user = User.builder()
                .name(userIn.getName())
                .password(passwordEncoder.encode(userIn.getPassword())) //кодируем пароль
                .build();

        try {
            LOG.info("Saving User {}", userIn.getName());
            return userRepository.save(user);
        } catch (Exception exception) {
            LOG.error("Error during registration {}", exception.getMessage());
            throw new RuntimeException("The user " + user.getName() + " already exist. Please check credentials");
        }
    }
}
