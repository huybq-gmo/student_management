package com.huybq.student_management.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder

public record UserDto(
        @NotBlank(message = "Username is required") String username,
        @NotBlank(message = "Password is required") String password
) {}
