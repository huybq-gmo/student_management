package com.huybq.student_management.user.service;

import com.huybq.student_management.jwt.JwtService;
import com.huybq.student_management.user.dto.LoginDto;
import com.huybq.student_management.user.dto.UserDto;
import com.huybq.student_management.user.mapper.UserMapper;
import com.huybq.student_management.user.model.User;
import com.huybq.student_management.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

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

    public LoginDto login(UserDto userDto) {
        var user = repository.findByUsername(userDto.username());
        boolean result = user.filter(value -> passwordEncoder.matches(userDto.password(), value.getPassword())).isPresent();
        if (!result) {
            throw new IllegalArgumentException("Username or password is uncorrected");
        }
        String token = jwtService.generateToken(user.get().getUserId());
        String refeshToken = jwtService.generateRefeshToken(user.get().getUserId());
        jwtService.saveUserToken(user.get(),token,refeshToken);
        return LoginDto.builder()
                .token(token)
                .loginAt(new Date(System.currentTimeMillis()))
                .expiresAt(new Date(System.currentTimeMillis()+3600000))
                .userId(user.get().getUserId())
                .build();
    }

}
