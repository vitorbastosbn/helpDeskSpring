package com.helpdesk.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.helpdesk.api.entity.Ticket;

public interface TicketRepository extends MongoRepository<Ticket, String> {

	// Para usar paginação usa o Page com atributo pages e o atributo da pesquisa. O
	// order by
	// pode-se passar o atributo e o spring se vira
	Page<Ticket> findByUsuarioIdOrderByDataDesc(Pageable pages, String usuarioId);

	// Pesquisa por texto ignorando case sensitive o spring se vira
	Page<Ticket> findByTituloIgnoreCaseContainingAndStatusContainingAndPrioridadeContainingOrderByDataDesc(String titulo, String status,
			String prioridade, Pageable pages);

	Page<Ticket> findByTituloIgnoreCaseContainingAndStatusContainingAndPrioridadeContainingAndUsuarioIdOrderByDataDesc(String titulo,
			String status, String prioridade, String usuarioId, Pageable pages);

	Page<Ticket> findByTituloIgnoreCaseContainingAndStatusContainingAndPrioridadeContainingAndUsuarioDesignadoIdOrderByDataDesc(
			String titulo, String status, String prioridade, String usuarioDesignadoId, Pageable pages);

	Page<Ticket> findByProtocolo(Long protocolo, Pageable pages);

}
