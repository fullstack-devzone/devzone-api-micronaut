package com.sivalabs.devzone.users.services;

import com.sivalabs.devzone.common.exceptions.DevZoneException;
import com.sivalabs.devzone.users.entities.RoleEnum;
import com.sivalabs.devzone.users.entities.User;
import com.sivalabs.devzone.users.models.UserDTO;
import com.sivalabs.devzone.users.repositories.UserRepository;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.transaction.Transactional;
import java.util.Optional;

@Singleton
@Transactional
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Optional<UserDTO> getUserById(Long id) {
        return userRepository.findById(id).map(UserDTO::fromEntity);
    }

    @Transactional
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public UserDTO createUser(UserDTO user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new DevZoneException("Email " + user.getEmail() + " is already in use");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User userEntity = user.toEntity();
        userEntity.setRole(RoleEnum.ROLE_USER);
        return UserDTO.fromEntity(userRepository.save(userEntity));
    }
}
