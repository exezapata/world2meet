package com.w2m.spaceships.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ActiveProfiles;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
class UserMainTest {

    private User user;
    private UserMain userMain;
    private Role role;

    @BeforeEach
    public void setUp() {

        role = Role.builder().id(1L).name("ADMIN").build();

        user = new User();
        user.setUsername("username");
        user.setPassword("password");
        user.setRoles(Set.of(role));

        userMain = new UserMain(user);
    }

    @Test
    void getUsername_ReturnsCorrectUsername() {
        assertEquals("username", userMain.getUsername());
    }

    @Test
    void getPassword_ReturnsCorrectPassword() {
        assertEquals("password", userMain.getPassword());
    }

    @Test
    void getAuthorities_ReturnsSingleUserRole() {
        Set<SimpleGrantedAuthority> authorities = (Set<SimpleGrantedAuthority>) userMain.getAuthorities();

        assertEquals(1, authorities.size());
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ADMIN")));
    }

    @Test
    void isAccountNonExpired_ReturnsTrue() {
        assertTrue(userMain.isAccountNonExpired());
    }

    @Test
    void isAccountNonLocked_ReturnsTrue() {
        assertTrue(userMain.isAccountNonLocked());
    }

    @Test
    void isCredentialsNonExpired_ReturnsTrue() {
        assertTrue(userMain.isCredentialsNonExpired());
    }

    @Test
    void isEnabled_ReturnsTrue() {
        assertTrue(userMain.isEnabled());
    }
}