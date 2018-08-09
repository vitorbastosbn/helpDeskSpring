package com.helpdesk.api.enums;

public enum StatusEnum {
	NOVO, DESIGNADO, RESOLVIDO, APROVADO, REPROVADO, FECHADO;

	public static StatusEnum getStatus(String status) {
		switch (status) {
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
		case "":
			return NOVO;
		default:
			return NOVO;
		}
	}
}
