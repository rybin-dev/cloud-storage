package com.rybindev.cloudstorage.service;

import com.rybindev.cloudstorage.dto.AuthorizedUser;
import com.rybindev.cloudstorage.dto.CreateUserRequest;
import com.rybindev.cloudstorage.entiry.User;
import com.rybindev.cloudstorage.mapper.UserMapper;
import com.rybindev.cloudstorage.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional
    public Long register(CreateUserRequest dto) {
        return Optional.of(dto)
                .map(userMapper::map)
                .map(userRepository::saveAndFlush)
                .map(User::getId)
                .orElseThrow();

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByName(username)
                .map(user -> new AuthorizedUser(
                        user.getId(),
                        user.getName(),
                        user.getPassword(),
                        Collections.singleton(user.getRole())
                )).orElseThrow(() -> new UsernameNotFoundException("Failed to retrieve user: " + username));
    }
}
