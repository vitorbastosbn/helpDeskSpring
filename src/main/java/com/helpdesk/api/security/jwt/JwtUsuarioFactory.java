package com.helpdesk.api.security.jwt;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.helpdesk.api.entity.Usuario;
import com.helpdesk.api.enums.PerfilEnum;

public class JwtUsuarioFactory {
	
	private JwtUsuarioFactory() {}
	
	//Converte o usuario para o formato do spring secutiry
	public static JwtUsuario criar(Usuario usuario) {
		return new JwtUsuario(usuario.getId(), usuario.getEmail(), usuario.getSenha(), mapParaGarantirAutorizacoes(usuario.getPerfil()));
	}
	
	//Converte o perfil para o formato do spring secutiry
	private static List<GrantedAuthority> mapParaGarantirAutorizacoes(PerfilEnum perfilEnum) {
		List<GrantedAuthority> autorizacoes = new ArrayList<GrantedAuthority>();
		autorizacoes.add(new SimpleGrantedAuthority(perfilEnum.toString()));
		return autorizacoes;
	}

}
