package com.helpdesk.api.service;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.helpdesk.api.entity.AlteraStatus;
import com.helpdesk.api.entity.Ticket;

@Component
public interface TicketService {

	Ticket createOrUpdate(Ticket ticket);
	
	Ticket findById(String id);
	
	void delete(String id);
	
	Page<Ticket> listaTicket(int pagina, int quantidadeRegistros);
	
	AlteraStatus createAlteraStatus(AlteraStatus alteraStatus);
	
	Iterable<AlteraStatus> listaAlteraStatus(String ticketId);
	
	Page<Ticket> findByUsuarioAtual(int pagina, int quantidadeRegistros, String usuarioId);
	
	Page<Ticket> findByParameters(int pagina, int quantidadeRegistros, String titulo, String status, String prioridade);
	
	Page<Ticket> findByParametersAndUsuarioAtual(int pagina, int quantidadeRegistros, String titulo, String status, String prioridade, String usuarioId);
	
	Page<Ticket> findByProtocolo(int pagina, int quantidadeRegistros, Long protocolo);
	
	Iterable<Ticket> findAll();
	
	Page<Ticket> findByParameterAndUsuarioDesignado(int pagina, int quantidadeRegistros, String titulo, String status, String prioridade, String usuarioDesignadoId);
}
