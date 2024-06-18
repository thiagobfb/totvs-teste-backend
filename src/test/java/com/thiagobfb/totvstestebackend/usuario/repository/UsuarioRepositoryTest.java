package com.thiagobfb.totvstestebackend.usuario.repository;

import com.thiagobfb.totvstestebackend.usuario.enums.Profile;
import com.thiagobfb.totvstestebackend.usuario.model.Usuario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    public void testSaveUsuarioWithProfiles() {
        Usuario user = new Usuario();
        user.setUsername("testuser");
        user.setPassword("password");

        user.addProfile(Profile.ADMIN);

        usuarioRepository.save(user);

        Usuario foundUser = usuarioRepository.findById(user.getId()).orElse(null);
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getProfiles()).containsExactlyInAnyOrder(Profile.CLIENTE,  Profile.ADMIN);
    }
}
