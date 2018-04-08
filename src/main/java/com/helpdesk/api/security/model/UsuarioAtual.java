package com.helpdesk.api.security.model;

import com.helpdesk.api.entity.Usuario;

public class UsuarioAtual {

	private String token;
	private Usuario usuario;

	public UsuarioAtual(String token, Usuario usuario) {
		super();
		this.token = token;
		this.usuario = usuario;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

}
