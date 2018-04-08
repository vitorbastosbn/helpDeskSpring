package com.helpdesk.api.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.helpdesk.api.entity.Usuario;
import com.helpdesk.api.repository.UsuarioRepository;
import com.helpdesk.api.service.UsuarioService;

@Service
public class UsuarioServiceImpl implements UsuarioService {
	
	@Autowired
	private UsuarioRepository usuarioRepository;

	@Override
	public Usuario findByEmail(String email) {
		return this.usuarioRepository.findByEmail(email);
	}

	@Override
	public Usuario createOrUpdate(Usuario usuario) {
		return this.usuarioRepository.save(usuario);
	}

	@Override
	public Usuario findById(String id) {
		return this.usuarioRepository.findOne(id);
	}

	@Override
	public void delete(String id) {
		this.usuarioRepository.delete(id);
	}

	@Override
	public Page<Usuario> findAll(int page, int registroPorPagina) {
		Pageable pages = new PageRequest(page, registroPorPagina);
		return this.usuarioRepository.findAll(pages);
	}

}
