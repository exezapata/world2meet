package com.w2m.spaceships.services;

import com.w2m.spaceships.exceptions.ResourceNotFoundException;
import com.w2m.spaceships.models.Role;
import com.w2m.spaceships.models.User;
import com.w2m.spaceships.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
class UserDetailsServiceImplTest {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @MockBean
    private UserRepository userRepository;

    @Test
    public void testLoadUserByUsername_ExistingUser_ReturnsUserDetails() {

        Role roleAdmin = Role.builder()
                .id(1L)
                .name("ROLE_ADMIN")
                .build();

        String username = "user";
        User user = new User();
        user.setUsername(username);
        user.setPassword("password");
        user.setRoles(Set.of(roleAdmin));


        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        assertEquals(username, userDetails.getUsername());
    }

    @Test
    public void testLoadUserByUsername_NonExistingUser_ThrowsException() {

        String username = "nonexistinguser";

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> userDetailsService.loadUserByUsername(username)
        );

        assertEquals("User not found: " + username, exception.getMessage());
    }

}