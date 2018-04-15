package com.helpdesk.api.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

import com.helpdesk.api.dto.Sumario;
import com.helpdesk.api.entity.AlteraStatus;
import com.helpdesk.api.entity.Ticket;
import com.helpdesk.api.entity.Usuario;
import com.helpdesk.api.enums.PerfilEnum;
import com.helpdesk.api.enums.StatusEnum;
import com.helpdesk.api.response.Response;
import com.helpdesk.api.security.jwt.JwtTokenUtil;
import com.helpdesk.api.service.TicketService;
import com.helpdesk.api.service.UsuarioService;

@RestController
@RequestMapping("/api/ticket")
@CrossOrigin(origins = "*")
public class TicketController {

	@Autowired
	private TicketService ticketService;

	@Autowired
	protected JwtTokenUtil jwtTokenUtil;

	@Autowired
	private UsuarioService usuarioService;

	@PostMapping
	@PreAuthorize("hasAnyRole('CLIENTE')")
	public ResponseEntity<Response<Ticket>> salvar(@RequestBody Ticket ticket, HttpServletRequest request,
			BindingResult result) {
		Response<Ticket> response = new Response<Ticket>();

		try {
			validarCriacaoTicket(ticket, result);
			if (result.hasErrors()) {
				result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
				return ResponseEntity.badRequest().body(response);
			}

			ticket.setStatus(StatusEnum.NOVO);
			ticket.setUsuario(usuarioFromRequest(request));
			ticket.setData(new Date());
			ticket.setProtocolo(gerarProtocolo());
			Ticket ticketSalvo = (Ticket) this.ticketService.createOrUpdate(ticket);
			response.setData(ticketSalvo);
		} catch (Exception e) {
			response.getErrors().add(e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}

		return ResponseEntity.ok(response);
	}

	private void validarCriacaoTicket(Ticket ticket, BindingResult result) {
		if (ticket.getTitulo() == null) {
			result.addError(new ObjectError("Ticket", "Título não informado"));
		}
	}

	private Usuario usuarioFromRequest(HttpServletRequest request) {
		String token = request.getHeader("Authorization");
		String email = jwtTokenUtil.getUsuarioFromToken(token);
		return usuarioService.findByEmail(email);
	}

	private Long gerarProtocolo() {
		Random random = new Random();
		return (long) random.nextInt(9999);
	}

	@PutMapping
	@PreAuthorize("hasAnyRole('CLIENTE')")
	public ResponseEntity<Response<Ticket>> alterar(@RequestBody Ticket ticket, HttpServletRequest request,
			BindingResult result) {
		Response<Ticket> response = new Response<Ticket>();
		try {
			validarAlteracaoTicket(ticket, result);
			if (result.hasErrors()) {
				result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
				return ResponseEntity.badRequest().body(response);
			}

			Ticket ticketAtual = this.ticketService.findById(ticket.getId());
			ticket.setStatus(ticketAtual.getStatus());
			ticket.setUsuario(ticketAtual.getUsuario());
			ticket.setData(ticketAtual.getData());
			ticket.setProtocolo(ticketAtual.getProtocolo());

			if (ticketAtual.getUsuarioDesignado() != null) {
				ticket.setUsuarioDesignado(ticketAtual.getUsuarioDesignado());
			}

			Ticket ticketSalvo = (Ticket) ticketService.createOrUpdate(ticket);
			response.setData(ticketSalvo);

		} catch (Exception e) {
			response.getErrors().add(e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}

		return ResponseEntity.ok(response);
	}

	private void validarAlteracaoTicket(Ticket ticket, BindingResult result) {
		if (ticket.getId() == null) {
			result.addError(new ObjectError("Ticket", "Id não informado"));
		}

		if (ticket.getTitulo() == null) {
			result.addError(new ObjectError("Ticket", "Título não informado"));
		}
	}

	@GetMapping(value = "{id}")
	@PreAuthorize("hasAnyRole('CLIENTE', 'TECNICO')")
	public ResponseEntity<Response<Ticket>> findById(@PathVariable("id") String id) {
		Response<Ticket> response = new Response<Ticket>();
		Ticket ticket = this.ticketService.findById(id);

		if (ticket == null) {
			response.getErrors().add("Registro não encontrado");
			return ResponseEntity.badRequest().body(response);
		} else {
			List<AlteraStatus> alteracoes = new ArrayList<AlteraStatus>();
			Iterable<AlteraStatus> alteracoesAtuais = this.ticketService.listaAlteraStatus(ticket.getId());

			for (Iterator<AlteraStatus> iterator = alteracoesAtuais.iterator(); iterator.hasNext();) {
				AlteraStatus alteraStatus = (AlteraStatus) iterator.next();
				alteraStatus.setTicket(null);
				alteracoes.add(alteraStatus);
			}

			ticket.setAlteracoes(alteracoes);
			response.setData(ticket);
		}

		return ResponseEntity.ok(response);
	}

	@DeleteMapping(value = "{id}")
	@PreAuthorize("hasAnyRole('CLIENTE')")
	public ResponseEntity<Response<String>> apagar(@PathVariable("id") String id) {
		Response<String> response = new Response<String>();
		Ticket ticket = this.ticketService.findById(id);

		if (ticket == null) {
			response.getErrors().add("Registro não encontrado");
			return ResponseEntity.badRequest().body(response);
		} else {
			this.ticketService.delete(id);
		}
		return ResponseEntity.ok(new Response<String>());
	}

	@GetMapping(value = "{pagina}/{registrosPorPagina}")
	@PreAuthorize("hasAnyRole('CLIENTE', 'TECNICO')")
	public ResponseEntity<Response<Page<Ticket>>> findAll(HttpServletRequest request, @PathVariable int pagina,
			@PathVariable int registrosPorPagina) {
		Response<Page<Ticket>> response = new Response<Page<Ticket>>();
		Page<Ticket> tickets = null;

		Usuario usuarioRequisitado = usuarioFromRequest(request);

		if (usuarioRequisitado.getPerfil().equals(PerfilEnum.ROLE_TECNICO)) {
			tickets = this.ticketService.listaTicket(pagina, registrosPorPagina);
		} else if (usuarioRequisitado.getPerfil().equals(PerfilEnum.ROLE_CLIENTE)) {
			tickets = this.ticketService.findByUsuarioAtual(pagina, registrosPorPagina, usuarioRequisitado.getId());
		}

		response.setData(tickets);
		return ResponseEntity.ok(response);
	}

	@GetMapping(value = "{pagina}/{registrosPorPagina}/{protocolo}/{titulo}/{status}/{prioridade}/{designado}")
	@PreAuthorize("hasAnyRole('CLIENTE', 'TECNICO')")
	public ResponseEntity<Response<Page<Ticket>>> findByParametros(HttpServletRequest request,
			@PathVariable("pagina") int pagina, @PathVariable("registrosPorPagina") int registrosPorPagina,
			@PathVariable("protocolo") Long protocolo, @PathVariable("titulo") String titulo,
			@PathVariable("status") String status, @PathVariable("prioridade") String prioridade,
			@PathVariable("designado") boolean designado) {

		titulo = titulo.equals("vazio") ? "" : titulo;
		status = status.equals("vazio") ? "" : status;
		prioridade = prioridade.equals("vazio") ? "" : prioridade;

		Response<Page<Ticket>> response = new Response<Page<Ticket>>();
		Page<Ticket> tickets = null;

		if (protocolo > 0) {
			tickets = this.ticketService.findByProtocolo(pagina, registrosPorPagina, protocolo);
		} else {
			Usuario usuarioRequisitado = usuarioFromRequest(request);

			if (usuarioRequisitado.getPerfil().equals(PerfilEnum.ROLE_TECNICO)) {
				if (designado) {
					tickets = this.ticketService.findByParameterAndUsuarioDesignado(pagina, registrosPorPagina, titulo,
							status, prioridade, usuarioRequisitado.getId());
				} else {
					tickets = this.ticketService.findByParameters(pagina, registrosPorPagina, titulo, status,
							prioridade);
				}
			} else if (usuarioRequisitado.getPerfil().equals(PerfilEnum.ROLE_CLIENTE)) {
				tickets = this.ticketService.findByParametersAndUsuarioAtual(pagina, registrosPorPagina, titulo, status,
						prioridade, usuarioRequisitado.getId());
			}
		}

		response.setData(tickets);
		return ResponseEntity.ok(response);
	}

	@PutMapping(value = "{id}/{status}")
	@PreAuthorize("hasAnyRole('CLIENTE', 'TECNICO')")
	public ResponseEntity<Response<Ticket>> alteraStatus(HttpServletRequest request, @PathVariable("id") String id,
			@PathVariable("status") String status, @RequestBody Ticket ticket, BindingResult result) {

		Response<Ticket> response = new Response<Ticket>();

		try {
			validarAlteracaoStatus(id, status, result);

			if (result.hasErrors()) {
				result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
				return ResponseEntity.badRequest().body(response);
			}

			Ticket ticketAtual = this.ticketService.findById(id);

			if (status.equals(StatusEnum.DESIGNADO.toString())) {
				ticketAtual.setUsuarioDesignado(usuarioFromRequest(request));
			}
			ticketAtual.setStatus(StatusEnum.getStatus(status));

			Ticket ticketSalvo = (Ticket) this.ticketService.createOrUpdate(ticketAtual);
			AlteraStatus alteraStatus = new AlteraStatus();
			alteraStatus.setUsuarioRespAlteracao(usuarioFromRequest(request));
			alteraStatus.setDataAlteracaoStatus(new Date());
			alteraStatus.setTicket(ticketSalvo);
			alteraStatus.setStatus(StatusEnum.getStatus(status));
			this.ticketService.createAlteraStatus(alteraStatus);
			response.setData(ticketSalvo);

		} catch (Exception e) {
			response.getErrors().add(e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}

		return ResponseEntity.ok(response);
	}

	private void validarAlteracaoStatus(String id, String status, BindingResult result) {
		if (id == null) {
			result.addError(new ObjectError("Ticket", "Id não informado"));
		}

		if (status == null) {
			result.addError(new ObjectError("Ticket", "Status não informado"));
		}
	}
	
	@GetMapping(value = "/sumario")
//	@PreAuthorize("hasAnyRole('CLIENTE', 'TECNICO')")
	public ResponseEntity<Response<Sumario>> findSumario() {
		
		Response<Sumario> response = new Response<Sumario>();
		Sumario sumario = new Sumario();
		
		int montanteNovo = 0;
		int montanteResolvido = 0;
		int montanteAprovado = 0;
		int montanteReprovado = 0;
		int montanteDesignado = 0;
		int montanteFechado = 0;
		
		
		Iterable<Ticket> tickets = this.ticketService.findAll();
		
		if(tickets != null) {
			for(Iterator<Ticket> iterator = tickets.iterator(); iterator.hasNext();) {
				Ticket ticket = (Ticket) iterator.next();
				
				if (ticket.getStatus().equals(StatusEnum.NOVO)) {
					montanteNovo++;
				}
				if (ticket.getStatus().equals(StatusEnum.RESOLVIDO)) {
					montanteResolvido++;
				}
				if (ticket.getStatus().equals(StatusEnum.APROVADO)) {
					montanteAprovado++;
				}
				if (ticket.getStatus().equals(StatusEnum.REPROVADO)) {
					montanteReprovado++;
				}
				if (ticket.getStatus().equals(StatusEnum.DESIGNADO)) {
					montanteDesignado++;
				}
				if (ticket.getStatus().equals(StatusEnum.FECHADO)) {
					montanteFechado++;
				}
			}
		}
		
		sumario.setMontanteNovo(montanteNovo);
		sumario.setMontanteAprovado(montanteAprovado);
		sumario.setMontanteDesignado(montanteDesignado);
		sumario.setMontanteFechado(montanteFechado);
		sumario.setMontanteReprovado(montanteReprovado);
		sumario.setMontanteResolvido(montanteResolvido);
		
		response.setData(sumario);
		
		return ResponseEntity.ok(response);
	}
}
