package com.w2m.spaceships.services;

import com.w2m.spaceships.models.User;
import com.w2m.spaceships.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
class UserDetailsServiceImplTest {

    private UserDetailsServiceImpl userDetailsService;
    @MockBean
    private UserRepository userRepository;


    @BeforeEach
    void setUp() {
        userDetailsService = new UserDetailsServiceImpl(userRepository);
    }

    @Test
    public void testLoadUserByUsername_ExistingUser_ReturnsUserDetails() {

        String username = "user";
        User user = new User();
        user.setUsername(username);
        user.setPassword("password");

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        assertEquals(username, userDetails.getUsername());
    }

    @Test
    public void testLoadUserByUsername_NonExistingUser_ThrowsException() {

        String username = "nonexistinguser";

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        UsernameNotFoundException exception = org.junit.jupiter.api.Assertions.assertThrows(
                UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername(username)
        );

        assertEquals("User not found: " + username, exception.getMessage());
    }

}