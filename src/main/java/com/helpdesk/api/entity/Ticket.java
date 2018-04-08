package com.helpdesk.api.entity;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.helpdesk.api.enums.PrioridadeEnum;
import com.helpdesk.api.enums.StatusEnum;

@Document
public class Ticket {

	@Id
	private String id;
	@DBRef(lazy = true)
	private Usuario usuario;
	private Date data;
	private String titulo;
	private Long protocolo;
	private StatusEnum status;
	private PrioridadeEnum prioridade;
	@DBRef(lazy = true)
	private Usuario usuarioDesignado;
	private String descricao;
	private String imagem;
	@Transient
	private List<AlteraStatus> alteracoes;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public Long getProtocolo() {
		return protocolo;
	}

	public void setProtocolo(Long protocolo) {
		this.protocolo = protocolo;
	}

	public StatusEnum getStatus() {
		return status;
	}

	public void setStatus(StatusEnum status) {
		this.status = status;
	}

	public PrioridadeEnum getPrioridade() {
		return prioridade;
	}

	public void setPrioridade(PrioridadeEnum prioridade) {
		this.prioridade = prioridade;
	}

	public Usuario getUsuarioDesignado() {
		return usuarioDesignado;
	}

	public void setUsuarioDesignado(Usuario usuarioDesignado) {
		this.usuarioDesignado = usuarioDesignado;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getImagem() {
		return imagem;
	}

	public void setImagem(String imagem) {
		this.imagem = imagem;
	}

	public List<AlteraStatus> getAlteracoes() {
		return alteracoes;
	}

	public void setAlteracoes(List<AlteraStatus> alteracoes) {
		this.alteracoes = alteracoes;
	}

}
