package com.w2m.spaceships.services;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
class JwtServiceTest {

    @Autowired
    private JwtService  jwtService;
    @MockBean
    private UserDetails userDetails;

    private String token;
    private String username = "user";

    @BeforeEach
    void setUp() {
        token = jwtService.generateToken(username);

        when(userDetails.getUsername()).thenReturn(username);
    }


    @Test
    void testExtractUsername() {
        String extractedUsername = jwtService.extractUsername(token);

        assertEquals(username, extractedUsername);
    }

    @Test
    void testExtractExpiration() {
        Date expiration = jwtService.extractExpiration(token);

        assertNotNull(expiration);
    }

    @Test
    void testExtractClaim() {
        Claims claims = jwtService.extractAllClaims(token);

        String extractedUsername = jwtService.extractClaim(token, Claims::getSubject);

        assertEquals(username, extractedUsername);
    }

    @Test
    void testIsTokenExpired() {
        Boolean isExpired = jwtService.isTokenExpired(token);

        assertFalse(isExpired);
    }

    @Test
    void testValidateToken() {
        Boolean isValid = jwtService.validateToken(token, userDetails);

        assertTrue(isValid);
    }

    @Test
    void testGenerateToken() {
        String newToken = jwtService.generateToken(username);

        assertNotNull(newToken);
    }

    @Test
    void testCreateToken() {
        Map<String, Object> claims = new HashMap<>();

        String createdToken = jwtService.createToken(claims, username);

        assertNotNull(createdToken);
    }
}