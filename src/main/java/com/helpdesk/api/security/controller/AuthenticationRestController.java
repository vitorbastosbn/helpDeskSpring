package com.helpdesk.api.security.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.helpdesk.api.entity.Usuario;
import com.helpdesk.api.security.jwt.JwtAuthenticationRequest;
import com.helpdesk.api.security.jwt.JwtTokenUtil;
import com.helpdesk.api.security.model.UsuarioAtual;
import com.helpdesk.api.service.UsuarioService;

import ch.qos.logback.core.net.SyslogOutputStream;

@RestController
@CrossOrigin(origins = "*")
public class AuthenticationRestController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private UsuarioService usuarioService;

	@PostMapping(value = "/api/auth")
	public ResponseEntity<?> criarAutenticacaoToken(@RequestBody JwtAuthenticationRequest authenticationRequest)
			throws AuthenticationException {
		
		final Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(),
						authenticationRequest.getSenha()));
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getEmail());
		final String token = jwtTokenUtil.gerarToken(userDetails);
		final Usuario usuario = usuarioService.findByEmail(authenticationRequest.getEmail());
		usuario.setSenha(null);
		return ResponseEntity.ok(new UsuarioAtual(token, usuario));
	}
	
	@PostMapping(value="/api/refresh")
	public ResponseEntity<?> atualizaEConsegueAutenticacaoToken(HttpServletRequest requisicao) {
		String token = requisicao.getHeader("Authorization");
		String username = jwtTokenUtil.getUsuarioFromToken(token);
		final Usuario usuario = usuarioService.findByEmail(username);
		
		if (jwtTokenUtil.isTokenPodeSerAtualizado(token)) {
			String tokenAtualizado = jwtTokenUtil.atualizaToken(token);
			return ResponseEntity.ok(new UsuarioAtual(tokenAtualizado, usuario));
		} else {
			return ResponseEntity.badRequest().body(null);
		}
	}

}
