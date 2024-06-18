package com.thiagobfb.totvstestebackend.security.resource;

import com.thiagobfb.totvstestebackend.security.dto.UserSS;
import com.thiagobfb.totvstestebackend.security.service.UserService;
import com.thiagobfb.totvstestebackend.security.utils.JWTUtil;
import com.thiagobfb.totvstestebackend.usuario.enums.Profile;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

public class AuthResourceTest {

    @InjectMocks
    private AuthResource authResource;

    @Mock
    private JWTUtil jwtUtil;

    @Mock
    private UserService userService;

    @Mock
    private HttpServletResponse response;

    @BeforeEach
    public void setUp() {
        openMocks(this);
    }

    @Test
    public void testRefreshToken() {
        Set<Profile> profiles = new HashSet<>();
        profiles.add(Profile.ADMIN);
        UserSS userSS = new UserSS(1L, "testUser", "password", profiles);
        String token = "mockToken";

        mockStatic(UserService.class);
        when(UserService.authenticated()).thenReturn(userSS);

        when(jwtUtil.generateToken(userSS.getUsername())).thenReturn(token);

        ResponseEntity<Void> responseEntity = authResource.refreshToken(response);

        verify(response).addHeader("Authorization", "Bearer " + token);
        verify(response).addHeader("access-control-expose-headers", "Authorization");

        assertEquals(ResponseEntity.noContent().build(), responseEntity);
    }
}
