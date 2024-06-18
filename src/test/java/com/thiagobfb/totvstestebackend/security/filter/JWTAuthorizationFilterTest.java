package com.thiagobfb.totvstestebackend.security.filter;

import com.thiagobfb.totvstestebackend.security.utils.JWTUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class JWTAuthorizationFilterTest {

    @Mock
    private JWTUtil jwtUtil;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private JWTAuthorizationFilter jwtAuthorizationFilter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testDoFilterInternal_withValidToken() throws ServletException, IOException {
        String token = "valid_token";
        String header = "Bearer " + token;

        when(request.getHeader("Authorization")).thenReturn(header);
        when(jwtUtil.tokenValido(token)).thenReturn(true);
        when(jwtUtil.getUsername(token)).thenReturn("user");
        when(userDetailsService.loadUserByUsername(anyString())).thenReturn(userDetails);

        jwtAuthorizationFilter.doFilterInternal(request, response, filterChain);

        verify(jwtUtil, times(1)).tokenValido(token);
        verify(jwtUtil, times(1)).getUsername(token);
        verify(userDetailsService, times(1)).loadUserByUsername("user");
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void testDoFilterInternal_withInvalidToken() throws ServletException, IOException {
        String token = "invalid_token";
        String header = "Bearer " + token;

        when(request.getHeader("Authorization")).thenReturn(header);
        when(jwtUtil.tokenValido(token)).thenReturn(false);

        jwtAuthorizationFilter.doFilterInternal(request, response, filterChain);

        verify(jwtUtil, times(1)).tokenValido(token);
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void testDoFilterInternal_withoutAuthorizationHeader() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtAuthorizationFilter.doFilterInternal(request, response, filterChain);

        verify(jwtUtil, times(0)).tokenValido(anyString());
        verify(filterChain, times(1)).doFilter(request, response);
    }
}
