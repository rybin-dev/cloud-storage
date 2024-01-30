package com.rybindev.cloudstorage.mapper;

import com.rybindev.cloudstorage.dto.CreateUserRequest;
import com.rybindev.cloudstorage.entiry.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserMapper implements Mapper<CreateUserRequest, User> {
    private final PasswordEncoder passwordEncoder;

    public User map(CreateUserRequest dto) {
        User user = new User();
        user.setName(dto.getUsername());
        Optional.of(dto.getPassword())
                .map(passwordEncoder::encode)
                .ifPresent(user::setPassword);

        return user;
    }
}
