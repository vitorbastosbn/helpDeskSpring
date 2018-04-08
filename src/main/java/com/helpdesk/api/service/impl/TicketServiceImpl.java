package com.helpdesk.api.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.helpdesk.api.entity.AlteraStatus;
import com.helpdesk.api.entity.Ticket;
import com.helpdesk.api.repository.AlteraStatusRepository;
import com.helpdesk.api.repository.TicketRepository;
import com.helpdesk.api.service.TicketService;

@Service
public class TicketServiceImpl implements TicketService {
	
	@Autowired
	private TicketRepository ticketRepository;
	
	@Autowired
	private AlteraStatusRepository alteraStatusRepository;

	@Override
	public Ticket createOrUpdate(Ticket ticket) {
		return this.ticketRepository.save(ticket);
	}

	@Override
	public Ticket findById(String id) {
		return this.ticketRepository.findOne(id);
	}

	@Override
	public void delete(String id) {
		this.ticketRepository.delete(id);
	}

	@Override
	public Page<Ticket> listaTicket(int pagina, int quantidadeRegistros) {
		Pageable paginas = new PageRequest(pagina, quantidadeRegistros);
		return this.ticketRepository.findAll(paginas);
	}

	@Override
	public AlteraStatus createAlteraStatus(AlteraStatus alteraStatus) {
		return this.alteraStatusRepository.save(alteraStatus);
	}

	@Override
	public Iterable<AlteraStatus> listaAlteraStatus(String ticketId) {
		return this.alteraStatusRepository.findByTicketIdOrderByDataAlteracaoStatusDesc(ticketId);
	}

	@Override
	public Page<Ticket> findByUsuarioAtual(int pagina, int quantidadeRegistros, String usuarioId) {
		Pageable paginas = new PageRequest(pagina, quantidadeRegistros);
		return this.ticketRepository.findByUsuarioIdOrderByDataDesc(paginas, usuarioId);
	}

	@Override
	public Page<Ticket> findByParameters(int pagina, int quantidadeRegistros, String titulo, String status,
			String prioridade) {
		Pageable paginas = new PageRequest(pagina, quantidadeRegistros);
		return this.ticketRepository.findByTituloIgnoreCaseContainingAndStatusAndPrioridadeOrderByDataDesc(titulo, status, prioridade, paginas);
	}

	@Override
	public Page<Ticket> findByParametersAndUsuarioAtual(int pagina, int quantidadeRegistros, String titulo,
			String status, String prioridade, String usuarioId) {
		Pageable paginas = new PageRequest(pagina, quantidadeRegistros);
		return this.ticketRepository.findByTituloIgnoreCaseContainingAndStatusAndPrioridadeAndUsuarioIdOrderByDataDesc(titulo, status, prioridade, usuarioId, paginas);
	}

	@Override
	public Page<Ticket> findByProtocolo(int pagina, int quantidadeRegistros, Long protocolo) {
		Pageable paginas = new PageRequest(pagina, quantidadeRegistros);
		return this.ticketRepository.findByProtocolo(protocolo, paginas);
	}

	@Override
	public Iterable<Ticket> findAll() {
		return this.ticketRepository.findAll();
	}

	@Override
	public Page<Ticket> findByParameterAndUsuarioDesignado(int pagina, int quantidadeRegistros, String titulo,
			String status, String prioridade, String usuarioDesignadoId) {
		Pageable paginas = new PageRequest(pagina, quantidadeRegistros);
		return this.ticketRepository.findByTituloIgnoreCaseContainingAndStatusAndPrioridadeAndUsuarioDesignadoIdOrderByDataDesc(titulo, status, prioridade, usuarioDesignadoId, paginas);
	}

	
}
