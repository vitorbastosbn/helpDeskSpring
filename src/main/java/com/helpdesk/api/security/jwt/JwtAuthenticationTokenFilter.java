package com.helpdesk.api.security.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest requisicao, HttpServletResponse resposta,
			FilterChain chain) throws ServletException, IOException {

		String authToken = requisicao.getHeader("Authorization");
		String usuario = jwtTokenUtil.getUsuarioFromToken(authToken);

		if (usuario != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails = this.userDetailsService.loadUserByUsername(usuario);

			if (jwtTokenUtil.validadeToken(authToken, userDetails)) {
				UsernamePasswordAuthenticationToken autenticacao = new UsernamePasswordAuthenticationToken(userDetails,
						null, userDetails.getAuthorities());
				autenticacao.setDetails(new WebAuthenticationDetailsSource().buildDetails(requisicao));
				logger.info("usuário autenticado " + usuario + ", contexto de configuração de segurança");
				SecurityContextHolder.getContext().setAuthentication(autenticacao);

			}

		}
		chain.doFilter(requisicao, resposta);

	}

}
