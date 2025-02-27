package com.huybq.student_management.user.controller;

import com.huybq.student_management.user.dto.UserDto;
import com.huybq.student_management.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "*")
@Tag(name = "Authentication", description = "Authenticating user before accessing resource")
public class AuthController {
    private final UserService userService;
    @PostMapping("/login")
    @Operation(summary = "Login", description = "Sign in user")
    @ApiResponse(responseCode = "200", description = "Succeed")
    @ApiResponse(responseCode = "404", description = "Not found")
    public ResponseEntity<Boolean>login(@Valid @RequestBody UserDto userDto){
        return ResponseEntity.ok(userService.login(userDto));
    }
    @PostMapping("/register")
    @Operation(summary = "Register", description = "Sign up user")
    @ApiResponse(responseCode = "200", description = "Succeed")
    @ApiResponse(responseCode = "404", description = "Username is existed")
    public ResponseEntity<String>register(@Valid @RequestBody UserDto userDto){
        return ResponseEntity.ok(userService.register(userDto));
    }
}
