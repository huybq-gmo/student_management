package com.huybq.student_management.user.dto;

import lombok.Builder;

import java.util.Date;

@Builder
public record LoginDto(String token, Date loginAt,Date expiresAt, Integer userId) {
}
