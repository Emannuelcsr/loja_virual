package jdev.mentoria.lojavirtual.model.dto;

import java.io.Serializable;

public class QSADto  implements Serializable{

	private static final long serialVersionUID = 1L;

	
	private String nome;
    private String qual;

    private String paisOrigem;

    private String nomeRepLegal;

    private String qualRepLegal;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getQual() {
		return qual;
	}

	public void setQual(String qual) {
		this.qual = qual;
	}

	public String getPaisOrigem() {
		return paisOrigem;
	}

	public void setPaisOrigem(String paisOrigem) {
		this.paisOrigem = paisOrigem;
	}

	public String getNomeRepLegal() {
		return nomeRepLegal;
	}

	public void setNomeRepLegal(String nomeRepLegal) {
		this.nomeRepLegal = nomeRepLegal;
	}

	public String getQualRepLegal() {
		return qualRepLegal;
	}

	public void setQualRepLegal(String qualRepLegal) {
		this.qualRepLegal = qualRepLegal;
	}
    
    
    
	
}
