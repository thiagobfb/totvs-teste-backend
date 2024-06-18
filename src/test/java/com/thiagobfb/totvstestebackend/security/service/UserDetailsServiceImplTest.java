package com.thiagobfb.totvstestebackend.security.service;

import com.thiagobfb.totvstestebackend.security.dto.UserSS;
import com.thiagobfb.totvstestebackend.usuario.enums.Profile;
import com.thiagobfb.totvstestebackend.usuario.model.Usuario;
import com.thiagobfb.totvstestebackend.usuario.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserDetailsServiceImplTest {

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private UsuarioRepository repository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testLoadUserByUsername_UserFound() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUsername( "testuser");
        usuario.setPassword("pwd");
        usuario.addProfile(Profile.ADMIN);

        when(repository.findByUsername(usuario.getUsername())).thenReturn(usuario);

        UserSS userSS = (UserSS) userDetailsService.loadUserByUsername(usuario.getUsername());

        assertNotNull(userSS);
        assertEquals(usuario.getId(), userSS.getId());
        assertEquals(usuario.getUsername(), userSS.getUsername());
        assertEquals(usuario.getPassword(), userSS.getPassword());

        verify(repository, times(1)).findByUsername(usuario.getUsername());
    }

    @Test
    public void testLoadUserByUsername_UserNotFound() {
        String username = "notfound";
        when(repository.findByUsername(username)).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername(username);
        });

        verify(repository, times(1)).findByUsername(username);
    }

    @Test
    public void testLoadUserByUsername_UsernameNull() {
        when(repository.findByUsername(null)).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername(null);
        });

        verify(repository, times(1)).findByUsername(null);
    }
}
