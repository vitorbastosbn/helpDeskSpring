package com.helpdesk.api.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.helpdesk.api.entity.Usuario;

public interface UsuarioRepository extends MongoRepository<Usuario, String>{
	
	Usuario findByEmail(String email);

}
