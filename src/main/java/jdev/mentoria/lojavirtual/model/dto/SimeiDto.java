package jdev.mentoria.lojavirtual.model.dto;

import java.io.Serializable;

public class SimeiDto implements Serializable {

	private static final long serialVersionUID = -113563836256003259L;

	private Boolean optante;

	private String dataOpcao;

	private String dataExclusao;

	private String ultimaAtualizacao;

	public Boolean getOptante() {
		return optante;
	}

	public void setOptante(Boolean optante) {
		this.optante = optante;
	}

	public String getDataOpcao() {
		return dataOpcao;
	}

	public void setDataOpcao(String dataOpcao) {
		this.dataOpcao = dataOpcao;
	}

	public String getDataExclusao() {
		return dataExclusao;
	}

	public void setDataExclusao(String dataExclusao) {
		this.dataExclusao = dataExclusao;
	}

	public String getUltimaAtualizacao() {
		return ultimaAtualizacao;
	}

	public void setUltimaAtualizacao(String ultimaAtualizacao) {
		this.ultimaAtualizacao = ultimaAtualizacao;
	}

	
	
	
}
