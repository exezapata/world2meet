package com.w2m.spaceships.dtos;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
class UserDtoTest {

    private UserDto userDto1;
    private UserDto userDto2;
    private Set<RoleDto> roles1;

    private Set<RoleDto> roles2;

    @BeforeEach
    void setUp() {
        roles1 = new HashSet<>();
        roles1.add(new RoleDto(1L, "ROLE_USER"));
        roles1.add(new RoleDto(2L, "ROLE_ADMIN"));

        roles2 = new HashSet<>();
        roles2.add(new RoleDto(1L, "ROLE_USER"));
        roles2.add(new RoleDto(2L, "ROLE_ADMIN"));

        userDto1 = new UserDto(1L, "user1", "password1", roles1);
        userDto2 = new UserDto(1L, "user1", "password1", roles2);
    }

    @Test
    void getId() {
        assertEquals(1L, userDto1.getId());
    }

    @Test
    void getUsername() {
        assertEquals("user1", userDto1.getUsername());
    }

    @Test
    void getPassword() {
        assertEquals("password1", userDto1.getPassword());
    }

    @Test
    void getRoles() {
        Set<RoleDto> expectedRoles = new HashSet<>();

        expectedRoles.add(new RoleDto(1L, "ROLE_USER"));
        expectedRoles.add(new RoleDto(2L, "ROLE_ADMIN"));

        assertEquals(expectedRoles, userDto1.getRoles());
    }

    @Test
    void setId() {
        userDto1.setId(2L);

        assertEquals(2L, userDto1.getId());
    }

    @Test
    void setUsername() {
        userDto1.setUsername("newUsername");

        assertEquals("newUsername", userDto1.getUsername());
    }

    @Test
    void setPassword() {
        userDto1.setPassword("newPassword");

        assertEquals("newPassword", userDto1.getPassword());
    }

    @Test
    void setRoles() {
        Set<RoleDto> newRoles = new HashSet<>();
        newRoles.add(new RoleDto(3L, "ROLE_GUEST"));
        userDto1.setRoles(newRoles);

        assertEquals(newRoles, userDto1.getRoles());
    }

    @Test
    void testEquals() {
        assertEquals(userDto1, userDto2);
    }

    @Test
    void canEqual() {
        assertTrue(userDto1.canEqual(userDto2));
    }

    @Test
    void testHashCode() {
        assertEquals(userDto1.hashCode(), userDto2.hashCode());
    }

    @Test
    void testToString() {
        assertEquals(userDto1.toString(), userDto2.toString());
    }

    @Test
    void builder() {
        UserDto userDto = UserDto.builder()
                .id(1L)
                .username("user1")
                .password("password1")
                .roles(roles1)
                .build();

        assertEquals(userDto1, userDto);
    }
}
