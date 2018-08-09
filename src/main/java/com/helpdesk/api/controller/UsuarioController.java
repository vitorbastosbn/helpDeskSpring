package com.helpdesk.api.controller;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.helpdesk.api.entity.Usuario;
import com.helpdesk.api.response.Response;
import com.helpdesk.api.service.UsuarioService;
import com.mongodb.DuplicateKeyException;

@RestController
@RequestMapping("/api/usuario")
@CrossOrigin(origins = "*")
public class UsuarioController {

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@PostMapping
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<Response<Usuario>> salvar(@RequestBody Usuario usuario, HttpServletRequest request,
			BindingResult result) {
		Response<Usuario> response = new Response<Usuario>();
		try {
			validarCriacaoUsuario(usuario, result);
			if (result.hasErrors()) {
				result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
				return ResponseEntity.badRequest().body(response);
			}

			usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
			Usuario usuarioSalvo = (Usuario) this.usuarioService.createOrUpdate(usuario);

			response.setData(usuarioSalvo);

		} catch (DuplicateKeyException e) {
			response.getErrors().add("Email já existe");
			return ResponseEntity.badRequest().body(response);
		} catch (Exception e) {
			response.getErrors().add(e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}

		return ResponseEntity.ok(response);
	}

	private void validarCriacaoUsuario(Usuario usuario, BindingResult result) {
		if (usuario.getEmail() == null) {
			result.addError(new ObjectError("Usuario", "Email não informado"));
		}
	}

	@PutMapping
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<Response<Usuario>> atualizar(@RequestBody Usuario usuario, HttpServletRequest request,
			BindingResult result) {
		Response<Usuario> response = new Response<Usuario>();
		try {
			validarAlteracaoUsuario(usuario, result);
			if (result.hasErrors()) {
				result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
				return ResponseEntity.badRequest().body(response);
			}
			
			usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
			Usuario usuarioAlterado = (Usuario) this.usuarioService.createOrUpdate(usuario);

			response.setData(usuarioAlterado);
			
		} catch (Exception e) {
			response.getErrors().add(e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}

		return ResponseEntity.ok(response);
	}

	private void validarAlteracaoUsuario(Usuario usuario, BindingResult result) {
		if (usuario.getId() == null) {
			result.addError(new ObjectError("Usuario", "Id não informado"));
		}

		if (usuario.getEmail() == null) {
			result.addError(new ObjectError("Usuario", "Email não informado"));
		}
	}
	
	@GetMapping(value = "{id}")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<Response<Usuario>> findById(@PathVariable("id") String id) {
		Response<Usuario> response = new Response<Usuario>();
		Usuario usuario = this.usuarioService.findById(id);
		
		if (usuario == null) {
			response.getErrors().add("Registro não encontrado");
			return ResponseEntity.badRequest().body(response);
		} else {
			response.setData(usuario);
		}
		
		return ResponseEntity.ok(response);
	}
	
	@DeleteMapping(value = "{id}")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<Response<String>> apagar(@PathVariable("id") String id) {
		Response<String> response = new Response<String>();
		Usuario usuario = this.usuarioService.findById(id);
		
		if (usuario == null) {
			response.getErrors().add("Registro não encontrado");
			return ResponseEntity.badRequest().body(response);
		} else {
			this.usuarioService.delete(id);
		}
		return ResponseEntity.ok(new Response<String>());
	}
	
	@GetMapping(value = "{pagina}/{registrosPorPagina}")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<Response<Page<Usuario>>> findAll(@PathVariable int pagina, @PathVariable int registrosPorPagina) {
		Response<Page<Usuario>> response = new Response<Page<Usuario>>();
		Page<Usuario> usuarios = this.usuarioService.findAll(pagina, registrosPorPagina);
		response.setData(usuarios);
		return ResponseEntity.ok(response);
	}
}
