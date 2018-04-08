package com.helpdesk.api.security.jwt;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class JwtUsuario implements UserDetails {

	private static final long serialVersionUID = 1L;

	private final String id;
	private final String usuario;
	private final String senha;
	private final Collection<? extends GrantedAuthority> autorizacoes;

	public JwtUsuario(String id, String usuario, String senha, Collection<? extends GrantedAuthority> autorizacoes) {
		super();
		this.id = id;
		this.usuario = usuario;
		this.senha = senha;
		this.autorizacoes = autorizacoes;
	}

	@JsonIgnore
	public String getId() {
		return id;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return autorizacoes;
	}

	@JsonIgnore
	@Override
	public String getPassword() {
		return senha;
	}

	@JsonIgnore
	@Override
	public String getUsername() {
		return usuario;
	}

	@JsonIgnore
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@JsonIgnore
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@JsonIgnore
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
