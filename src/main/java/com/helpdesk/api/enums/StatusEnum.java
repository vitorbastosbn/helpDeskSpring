package com.helpdesk.api.enums;

public enum StatusEnum {

	NOVO(1, "NOVO"), DESIGNADO(2, "DESIGNADO"), RESOLVIDO(3, "RESOLVIDO"), APROVADO(4, "APROVADO"), REPROVADO(5,
			"REPROVADO"), FECHADO(6, "FECHADO");

	private Integer id;
	private String descricao;

	StatusEnum() {
	}

	StatusEnum(Integer id, String descricao) {
		this.id = id;
		this.descricao = descricao;
	}

	public static StatusEnum getStatus(String status) {
		switch (status) {
		case "NOVO":
			return NOVO;
		case "DESIGNADO":
			return DESIGNADO;
		case "RESOLVIDO":
			return RESOLVIDO;
		case "APROVADO":
			return APROVADO;
		case "REPROVADO":
			return REPROVADO;
		case "FECHADO":
			return FECHADO;
		default:
			return NOVO;
		}
	}

	public static StatusEnum getStatus(Integer id) {
		switch (id) {
		case 1:
			return NOVO;
		case 2:
			return DESIGNADO;
		case 3:
			return RESOLVIDO;
		case 4:
			return APROVADO;
		case 5:
			return REPROVADO;
		case 6:
			return FECHADO;
		default:
			return NOVO;
		}
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

}
