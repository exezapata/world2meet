package com.w2m.spaceships.services;

import com.w2m.spaceships.dtos.RoleDto;
import com.w2m.spaceships.dtos.UserDto;
import com.w2m.spaceships.exceptions.ResourceNotFoundException;
import com.w2m.spaceships.models.Role;
import com.w2m.spaceships.models.User;
import com.w2m.spaceships.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
class UserServiceTest {
    @Autowired
    private UserService userService;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private ModelMapper modelMapper;

    @Test
    void testAddUser_UsernameAlreadyExists() {

        UserDto existingUser = new UserDto();
        existingUser.setUsername("existingUser");

        when(userRepository.existsByUsername(existingUser.getUsername())).thenReturn(true);

        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> {
            userService.addUser(existingUser);
        });

        assertEquals("Username already exists: " + existingUser.getUsername(), thrown.getMessage());

    }

    @Test
    void testAddUser_Success() {

        RoleDto roleAdminDto = RoleDto.builder()
                .id(1L)
                .name("ROLE_ADMIN")
                .build();

        Role roleAdmin = Role.builder()
                .id(1L)
                .name("ROLE_ADMIN")
                .build();

        UserDto newUser = new UserDto();
        newUser.setUsername("newUser");
        newUser.setPassword("password");
        newUser.setRoles(Set.of(roleAdminDto));

        User user = new User();
        user.setUsername("newUser");
        user.setPassword("encodedPassword");
        user.setRoles(Set.of(roleAdmin));

        when(userRepository.existsByUsername(newUser.getUsername())).thenReturn(false);

        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

        when(modelMapper.map(newUser, User.class)).thenReturn(user);

        when(userRepository.save(user)).thenReturn(user);

        User savedUser = userService.addUser(newUser);

        assertNotNull(savedUser);
        assertEquals(newUser.getUsername(), savedUser.getUsername());
        assertEquals(newUser.getPassword(), savedUser.getPassword());

        verify(userRepository, times(1)).existsByUsername(newUser.getUsername());
        verify(passwordEncoder, times(1)).encode("password");
        verify(modelMapper, times(1)).map(newUser, User.class);
        verify(userRepository, times(1)).save(user);
    }

}