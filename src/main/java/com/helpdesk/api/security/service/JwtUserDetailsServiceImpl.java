package com.helpdesk.api.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.helpdesk.api.entity.Usuario;
import com.helpdesk.api.security.jwt.JwtUsuarioFactory;
import com.helpdesk.api.service.UsuarioService;

@Service
public class JwtUserDetailsServiceImpl implements UserDetailsService {
	
	@Autowired
	private UsuarioService usuarioService;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Usuario usuario = usuarioService.findByEmail(email);
		
		if (usuario == null) {
			throw new UsernameNotFoundException(String.format("Nenhum usuário encontrado '%s'.", email));
		} else {
			return JwtUsuarioFactory.criar(usuario);
		}
	}

}
