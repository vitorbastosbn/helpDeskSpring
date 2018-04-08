package com.helpdesk.api.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.helpdesk.api.enums.StatusEnum;

@Document
public class AlteraStatus {

	@Id
	private String id;
	@DBRef(lazy = true)
	private Ticket ticket;
	@DBRef(lazy = true)
	private Usuario usuarioRespAlteracao;
	private Date dataAlteracaoStatus;
	private StatusEnum status;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Ticket getTicket() {
		return ticket;
	}

	public void setTicket(Ticket ticket) {
		this.ticket = ticket;
	}

	public Usuario getUsuarioRespAlteracao() {
		return usuarioRespAlteracao;
	}

	public void setUsuarioRespAlteracao(Usuario usuarioRespAlteracao) {
		this.usuarioRespAlteracao = usuarioRespAlteracao;
	}

	public Date getDataAlteracaoStatus() {
		return dataAlteracaoStatus;
	}

	public void setDataAlteracaoStatus(Date dataAlteracaoStatus) {
		this.dataAlteracaoStatus = dataAlteracaoStatus;
	}

	public StatusEnum getStatus() {
		return status;
	}

	public void setStatus(StatusEnum status) {
		this.status = status;
	}

}
