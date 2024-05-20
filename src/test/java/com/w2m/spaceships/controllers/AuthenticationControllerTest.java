package com.w2m.spaceships.controllers;

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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@WebMvcTest(AuthenticationController.class)
@ActiveProfiles("test")
@WithMockUser
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

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUsername("username");
        user.setPassword("password");
    }

    @Test
    void testRegisterUser_ValidUser_ReturnsCreated() {

        when(userService.addUser(user)).thenReturn(user);

        ResponseEntity<String> response = authenticationController.registerUser(user);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("User registered successfully", response.getBody());
    }

    @Test
    void testRegisterUser_UserServiceThrowsException_ReturnsInternalServerError() {

        when(userService.addUser(user)).thenThrow(new RuntimeException("User already exists"));

        ResponseEntity<String> response = authenticationController.registerUser(user);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().contains("Error registering user"));
    }

    @Test
    void testCreateAuthenticationToken_ValidCredentials_ReturnsToken() throws Exception {

        Authentication authentication = mock(Authentication.class);

        when(authenticationManager.authenticate(any())).thenReturn(authentication);

        when(jwtService.generateToken(user.getUsername())).thenReturn("test_token");

        ResponseEntity<?> response = authenticationController.createAuthenticationToken(user);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("test_token", response.getBody());
    }

    @Test
    void testCreateAuthenticationToken_InvalidCredentials_ReturnsUnauthorized() throws Exception {

        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("Invalid credentials"));

        ResponseEntity<?> response = authenticationController.createAuthenticationToken(user);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Incorrect username or password"));
    }
}