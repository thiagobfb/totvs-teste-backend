package com.thiagobfb.totvstestebackend.security.service;

import com.thiagobfb.totvstestebackend.security.dto.UserSS;
import com.thiagobfb.totvstestebackend.usuario.model.Usuario;
import com.thiagobfb.totvstestebackend.usuario.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	
	private final UsuarioRepository repository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario usuario = repository.findByUsername(username);
		
		if (username == null || usuario == null) throw new UsernameNotFoundException(null);
		
		return new UserSS(usuario.getId(), usuario.getUsername(), usuario.getPassword(), usuario.getProfiles());
	}

}
