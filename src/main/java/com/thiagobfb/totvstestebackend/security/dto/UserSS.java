package com.thiagobfb.totvstestebackend.security.dto;

import com.thiagobfb.totvstestebackend.usuario.enums.Profile;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class UserSS implements UserDetails {

	@Serial
	private static final long serialVersionUID = -9159887693013225654L;

	@Getter
	@Setter
	private Long id;
	private String username;
	private String password;
	private Collection<? extends GrantedAuthority> authorities;

	public UserSS(Long id, String username, String password, Set<Profile> profiles) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.authorities = profiles.stream().map(p -> new SimpleGrantedAuthority(p.getDescricao())).collect(Collectors.toList());
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	public boolean hasRole(Profile profile) {
		return getAuthorities().contains(new SimpleGrantedAuthority(profile.getDescricao()));
	}

}
