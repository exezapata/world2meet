package com.w2m.spaceships.services;

import com.w2m.spaceships.models.User;
import com.w2m.spaceships.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
class UserServiceTest {

    private UserService userService;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository, passwordEncoder);
    }

    @Test
    void testAddUser_UsernameAlreadyExists() {

        User existingUser = new User();
        existingUser.setUsername("existingUser");

        when(userRepository.existsByUsername(existingUser.getUsername())).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.addUser(existingUser));
        assertEquals("Username already exists: " + existingUser.getUsername(), exception.getMessage());

        verify(userRepository, times(1)).existsByUsername(existingUser.getUsername());
        verify(userRepository, never()).save(existingUser);
    }

    @Test
    void testAddUser_Success() {

        User newUser = new User();
        newUser.setUsername("newUser");
        newUser.setPassword("password");

        when(userRepository.existsByUsername(newUser.getUsername())).thenReturn(false);

        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

        when(userRepository.save(newUser)).thenReturn(newUser);

        User savedUser = userService.addUser(newUser);

        assertNotNull(savedUser);
        assertEquals(newUser.getUsername(), savedUser.getUsername());
        assertEquals(newUser.getPassword(), savedUser.getPassword());

        verify(userRepository, times(1)).existsByUsername(newUser.getUsername());
        verify(passwordEncoder, times(1)).encode("password");
        verify(userRepository, times(1)).save(newUser);
    }

}