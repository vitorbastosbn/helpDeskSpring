package com.helpdesk.api.dto;

import java.io.Serializable;

public class Sumario implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer montanteNovo;
	private Integer montanteResolvido;
	private Integer montanteAprovado;
	private Integer montanteReprovado;
	private Integer montanteDesignado;
	private Integer montanteFechado;

	public Integer getMontanteNovo() {
		return montanteNovo;
	}

	public void setMontanteNovo(Integer montanteNovo) {
		this.montanteNovo = montanteNovo;
	}

	public Integer getMontanteResolvido() {
		return montanteResolvido;
	}

	public void setMontanteResolvido(Integer montanteResolvido) {
		this.montanteResolvido = montanteResolvido;
	}

	public Integer getMontanteAprovado() {
		return montanteAprovado;
	}

	public void setMontanteAprovado(Integer montanteAprovado) {
		this.montanteAprovado = montanteAprovado;
	}

	public Integer getMontanteReprovado() {
		return montanteReprovado;
	}

	public void setMontanteReprovado(Integer montanteReprovado) {
		this.montanteReprovado = montanteReprovado;
	}

	public Integer getMontanteDesignado() {
		return montanteDesignado;
	}

	public void setMontanteDesignado(Integer montanteDesignado) {
		this.montanteDesignado = montanteDesignado;
	}

	public Integer getMontanteFechado() {
		return montanteFechado;
	}

	public void setMontanteFechado(Integer montanteFechado) {
		this.montanteFechado = montanteFechado;
	}

}
