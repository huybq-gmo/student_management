package com.huybq.student_management.user.mapper;

import com.huybq.student_management.user.dto.UserDto;
import com.huybq.student_management.user.model.User;
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
