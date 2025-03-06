package com.huybq.student_management.auth;

import com.huybq.student_management.user.controller.AuthController;
import com.huybq.student_management.user.dto.LoginDto;
import com.huybq.student_management.user.dto.UserDto;
import com.huybq.student_management.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthController authController;

    @Test
    public void loginSuccess() {
        // Arrange
        UserDto loginRequest = UserDto.builder()
                .username("testuser")
                .password("password123")
                .build();

        LoginDto expectedResponse = LoginDto.builder()
                .token("jwt.token.here")
                .loginAt(new Date(System.currentTimeMillis()))
                .expiresAt(new Date(System.currentTimeMillis() + 3600000))
                .userId(1)
                .build();

        when(userService.login(any(UserDto.class))).thenReturn(expectedResponse);

        // Act
        ResponseEntity<LoginDto> response = authController.login(loginRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(expectedResponse.token(), response.getBody().token());
        assertEquals(expectedResponse.userId(), response.getBody().userId());
        verify(userService).login(any(UserDto.class));
    }

    @Test
    public void loginFailed() {
        // Arrange
        UserDto loginRequest = UserDto.builder()
                .username("nonexistent")
                .password("wrongpass")
                .build();

        when(userService.login(any(UserDto.class)))
                .thenThrow(new IllegalArgumentException("Username or password is incorrect"));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> authController.login(loginRequest));
        verify(userService).login(any(UserDto.class));
    }

    @Test
    public void registerSuccess() {
        // Arrange
        UserDto userDto = UserDto.builder()
                .username("testuser")
                .password("password123")
                .build();

        when(userService.register(any(UserDto.class))).thenReturn("Register succeeded");

        // Act
        ResponseEntity<String> response = authController.register(userDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Register succeeded", response.getBody());
        verify(userService).register(any(UserDto.class));
    }

    @Test
    public void getTokenSuccess() {
        // Arrange
        UserDto userDto = UserDto.builder()
                .username("validuser")
                .password("validpassword")
                .build();

        LoginDto loginDto = LoginDto.builder()
                .token("validToken")
                .loginAt(new Date(System.currentTimeMillis()))
                .expiresAt(new Date(System.currentTimeMillis() + 3600000))
                .userId(1)
                .build();

        when(userService.login(any(UserDto.class))).thenReturn(loginDto);

        // Act
        ResponseEntity<LoginDto> response = authController.login(userDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("validToken", response.getBody().token());
        verify(userService).login(any(UserDto.class));
    }
}
