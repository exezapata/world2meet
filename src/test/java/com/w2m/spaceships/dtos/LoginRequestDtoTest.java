package com.w2m.spaceships.dtos;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
class LoginRequestDtoTest {

    private LoginRequestDto loginRequestDto;

    @BeforeEach
    void setUp() {
        loginRequestDto = new LoginRequestDto("username", "password");
    }

    @Test
    void getUsername() {
        assertEquals("username", loginRequestDto.getUsername());
    }

    @Test
    void getPassword() {
        assertEquals("password", loginRequestDto.getPassword());
    }

    @Test
    void setUsername() {
        loginRequestDto.setUsername("new_username");

        assertEquals("new_username", loginRequestDto.getUsername());
    }

    @Test
    void setPassword() {
        loginRequestDto.setPassword("new_password");

        assertEquals("new_password", loginRequestDto.getPassword());
    }

    @Test
    void testEquals() {
        LoginRequestDto loginRequestDto2 = new LoginRequestDto("username", "password");

        assertEquals(loginRequestDto, loginRequestDto2);
    }

    @Test
    void canEqual() {
        LoginRequestDto loginRequestDto2 = new LoginRequestDto("username", "password");

        assertTrue(loginRequestDto.canEqual(loginRequestDto2));
    }

    @Test
    void testHashCode() {
        LoginRequestDto loginRequestDto2 = new LoginRequestDto("username", "password");

        assertEquals(loginRequestDto.hashCode(), loginRequestDto2.hashCode());
    }

    @Test
    void testToString() {
        assertEquals("LoginRequestDto(username=username, password=password)", loginRequestDto.toString());
    }
}