package com.helpdesk.api.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.helpdesk.api.entity.Usuario;

@Repository("usuarioRepository")
public interface UsuarioRepository extends MongoRepository<Usuario, String>{
	
	Usuario findByEmail(String email);

}
