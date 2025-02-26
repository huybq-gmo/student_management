package com.huybq.student_management.auth.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordService {
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public String hashPassword(String password) {
        return passwordEncoder.encode(password);
    }
    public boolean verifyPassword(String rawPassword, String hashPassword){
        return passwordEncoder.matches(rawPassword,hashPassword);
    }
}
