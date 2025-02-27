package com.huybq.student_management.user.service;

import com.huybq.student_management.user.dto.UserDto;
import com.huybq.student_management.user.mapper.UserMapper;
import com.huybq.student_management.user.model.User;
import com.huybq.student_management.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public String register(UserDto userDto) {
        if (repository.existsByUsername(userDto.username())) {
            throw new IllegalArgumentException("Username already exists");
        }

        User user = mapper.toUser(userDto);
        user.setPassword(passwordEncoder.encode(userDto.password()));
        repository.save(user);
        return "Register succeeded";
    }

    public boolean login(UserDto userDto) {
        var user = repository.findByUsername(userDto.username());
        return user.filter(value -> passwordEncoder.matches(userDto.password(), value.getPassword())).isPresent();
    }

}
