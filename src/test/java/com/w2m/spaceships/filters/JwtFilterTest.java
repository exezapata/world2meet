package com.w2m.spaceships.filters;

import com.w2m.spaceships.services.JwtService;
import com.w2m.spaceships.services.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class JwtFilterTest {

    @InjectMocks
    private JwtFilter jwtFilter;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;



    @Test
    void testDoFilter_ValidToken() throws ServletException, IOException {

        String validToken = "validToken";
        String username = "username";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + validToken);

        when(jwtService.extractUsername(validToken)).thenReturn(username);

        UserDetails userDetails = mock(UserDetails.class);

        when(userDetailsServiceImpl.loadUserByUsername(username)).thenReturn(userDetails);

        when(jwtService.validateToken(validToken, userDetails)).thenReturn(true);

        jwtFilter.doFilterInternal(request, response, filterChain);

        verify(jwtService).extractUsername(validToken);
        verify(userDetailsServiceImpl).loadUserByUsername(username);
        verify(jwtService).validateToken(validToken, userDetails);
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_InvalidToken() throws ServletException, IOException {

        String invalidToken = "invalidToken";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + invalidToken);

        when(jwtService.extractUsername(invalidToken)).thenReturn(null);

        jwtFilter.doFilterInternal(request, response, filterChain);

        verify(jwtService).extractUsername(invalidToken);
        verify(filterChain).doFilter(request, response);
    }

}