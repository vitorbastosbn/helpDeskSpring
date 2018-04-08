package com.helpdesk.api.security.jwt;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenUtil implements Serializable {

	private static final long serialVersionUID = 1L;

	static final String CHAVE_USUARIO = "sub";
	static final String CHAVE_CRIADA = "created";
	static final String CHAVE_EXPIRADA = "exp";

	@Value("${jwt.secreto}")
	private String secreto;

	@Value("${jwt.expiracao}")
	private Long expiracao;

	public String getUsuarioFromToken(String token) {
		try {
			final Claims claims = getClaimsFromToken(token);
			return claims.getSubject();
		} catch (Exception e) {
			return null;
		}
	}

	public Date getDataExpiracaoFromToken(String token) {
		try {
			final Claims claims = getClaimsFromToken(token);
			return claims.getExpiration();
		} catch (Exception e) {
			return null;
		}
	}

	private Claims getClaimsFromToken(String token) {
		Claims claims;
		try {
			claims = Jwts.parser().setSigningKey(secreto).parseClaimsJws(token).getBody();
		} catch (Exception e) {
			claims = null;
		}

		return claims;
	}

	private Boolean isTokenExpired(String token) {
		final Date expiration = getDataExpiracaoFromToken(token);
		return expiration.before(new Date());
	}

	public String gerarToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();

		claims.put(CHAVE_USUARIO, userDetails.getUsername());

		final Date createdDate = new Date();
		claims.put(CHAVE_CRIADA, createdDate);

		return facaGerarToken(claims);
	}

	private String facaGerarToken(Map<String, Object> claims) {
		final Date dataCriacao = (Date) claims.get(CHAVE_CRIADA);
		final Date dataExpiracao = new Date(dataCriacao.getTime() + expiracao + 1000);
		return Jwts.builder().setClaims(claims).setExpiration(dataExpiracao).signWith(SignatureAlgorithm.HS512, secreto)
				.compact();
	}
	
	public Boolean isTokenPodeSerAtualizado(String token) {
		return (!isTokenExpired(token));
	}
	
	public Boolean validadeToken(String token, UserDetails userDetails) {
		JwtUsuario usuario = (JwtUsuario) userDetails;
		final String username = getUsuarioFromToken(token);
		return (username.equals(usuario.getUsername()) && !isTokenExpired(token));
	}
	
	public String atualizaToken(String token) {
		try {
			final Claims claims = getClaimsFromToken(token);
			claims.put(CHAVE_CRIADA, new Date());
			return facaGerarToken(claims);
		} catch (Exception e) {
			return null;
		}
	}
}
