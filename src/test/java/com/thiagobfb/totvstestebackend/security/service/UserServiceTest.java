package com.thiagobfb.totvstestebackend.security.service;

import com.thiagobfb.totvstestebackend.security.dto.UserSS;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @Mock
    private UserSS userSS;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void testAuthenticated_withAuthenticatedUser() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userSS);

        UserSS result = UserService.authenticated();

        assertNotNull(result);
        assertEquals(userSS, result);
        verify(securityContext, times(1)).getAuthentication();
        verify(authentication, times(1)).getPrincipal();
    }

    @Test
    void testAuthenticated_withoutAuthenticatedUser() {
        when(securityContext.getAuthentication()).thenReturn(null);

        UserSS result = UserService.authenticated();

        assertNull(result);
        verify(securityContext, times(1)).getAuthentication();
        verify(authentication, times(0)).getPrincipal();
    }

    @Test
    void testAuthenticated_withException() {
        when(securityContext.getAuthentication()).thenThrow(new RuntimeException("Exception"));

        UserSS result = UserService.authenticated();

        assertNull(result);
        verify(securityContext, times(1)).getAuthentication();
    }
}
