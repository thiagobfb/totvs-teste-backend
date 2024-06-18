package com.thiagobfb.totvstestebackend.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thiagobfb.totvstestebackend.security.dto.CredenciaisDTO;
import com.thiagobfb.totvstestebackend.security.dto.UserSS;
import com.thiagobfb.totvstestebackend.security.utils.JWTUtil;
import jakarta.servlet.ReadListener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintWriter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class JWTAuthenticationFilterTest {

    @InjectMocks
    private JWTAuthenticationFilter jwtAuthenticationFilter;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JWTUtil jwtUtil;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @Mock
    private Authentication authentication;

    @Mock
    private AuthenticationException authenticationException;

    @Mock
    private UserSS userSS;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtAuthenticationFilter = new JWTAuthenticationFilter(authenticationManager, jwtUtil);
    }

    @Test
    public void testAttemptAuthentication() throws IOException {
        CredenciaisDTO creds = new CredenciaisDTO();
        creds.setUsername("testuser");
        creds.setPassword("testpass");

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(new ObjectMapper().writeValueAsBytes(creds));
        ServletInputStream servletInputStream = new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }

            @Override
            public int read() throws IOException {
                return byteArrayInputStream.read();
            }
        };

        when(request.getInputStream()).thenReturn(servletInputStream);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);

        Authentication result = jwtAuthenticationFilter.attemptAuthentication(request, response);

        assertEquals(authentication, result);
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    public void testSuccessfulAuthentication() throws IOException, ServletException {
        when(authentication.getPrincipal()).thenReturn(userSS);
        when(userSS.getUsername()).thenReturn("testuser");
        when(jwtUtil.generateToken("testuser")).thenReturn("testtoken");

        jwtAuthenticationFilter.successfulAuthentication(request, response, filterChain, authentication);

        verify(response, times(1)).addHeader("Authorization", "Bearer testtoken");
        verify(response, times(1)).addHeader("access-control-expose-headers", "Authorization");
    }

    @Test
    public void testOnAuthenticationFailure() throws Exception {
        JWTAuthenticationFilter.JWTAuthenticationFailureHandler failureHandler = jwtAuthenticationFilter.new JWTAuthenticationFailureHandler();
        PrintWriter printWriter = mock(PrintWriter.class);

        when(response.getWriter()).thenReturn(printWriter);

        failureHandler.onAuthenticationFailure(null, response, authenticationException);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(response).setContentType("application/json");
        verify(response.getWriter(), times(1)).append(anyString());
    }
}
