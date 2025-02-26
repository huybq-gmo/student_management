package com.huybq.student_management.auth.mapper;

import com.huybq.student_management.auth.dto.UserDto;
import com.huybq.student_management.auth.model.User;
import com.huybq.student_management.auth.service.PasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserMapper {
    public User toUser(UserDto userDto) {
        if (userDto.password() == null || userDto.password().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        return User.builder()
                .username(userDto.username())
                .password(userDto.password())
                .build();
    }

}
