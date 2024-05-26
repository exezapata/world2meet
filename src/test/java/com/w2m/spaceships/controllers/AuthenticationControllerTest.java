package com.w2m.spaceships.controllers;

import com.w2m.spaceships.dtos.UserDto;
import com.w2m.spaceships.exceptions.AuthenticationException;
import com.w2m.spaceships.exceptions.ResourceNotFoundException;
import com.w2m.spaceships.models.User;
import com.w2m.spaceships.repositories.UserRepository;
import com.w2m.spaceships.services.JwtService;
import com.w2m.spaceships.services.UserDetailsServiceImpl;
import com.w2m.spaceships.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@WebMvcTest(AuthenticationController.class)
@ActiveProfiles("test")
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private AuthenticationController authenticationController;
    @MockBean
    private UserService userService;
    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;
    @MockBean
    private AuthenticationManager authenticationManager;

    private UserDto userDto;

    private User user;

    @BeforeEach
    void setUp() {
        userDto = new UserDto();
        userDto.setUsername("username");
        userDto.setPassword("password");

        user = new User();
        user.setUsername("username");
        user.setPassword("password");
    }

    @Test
    void testRegisterUser_ValidUser_ReturnsCreated() {

        when(userService.addUser(userDto)).thenReturn(user);

        ResponseEntity<String> response = authenticationController.registerUser(userDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("User registered successfully", response.getBody());
    }

    @Test
    void testRegisterUser_UserService_Throws_ResourceNotFoundException() {

        UserDto existingUser = new UserDto();
        existingUser.setUsername("existingUser");

        when(userService.addUser(existingUser)).thenThrow(new ResourceNotFoundException("Username already exists: " + existingUser.getUsername()));

        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> {
            authenticationController.registerUser(existingUser);
        });

        assertEquals("Username already exists: " + existingUser.getUsername(), thrown.getMessage());
    }

    @Test
    void testCreateAuthenticationToken_ValidCredentials_ReturnsToken() throws Exception {

        Authentication authentication = mock(Authentication.class);

        when(authenticationManager.authenticate(any())).thenReturn(authentication);

        when(jwtService.generateToken(userDto.getUsername())).thenReturn("test_token");

        ResponseEntity<?> response = authenticationController.createAuthenticationToken(userDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("test_token", response.getBody());
    }

    @Test
    void testCreateAuthenticationToken_InvalidCredentials_ReturnsUnauthorized() throws Exception {

        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("Invalid credentials"));

        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> {
            authenticationController.createAuthenticationToken(userDto);
        });

        assertEquals("Incorrect username or password", thrown.getMessage());
    }
}