package jdev.mentoria.lojavirtual.model.dto;

import java.io.Serializable;

public class NotaFiscalRetornoEnvioDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String cnpj_emitente;
	private String ref;
	private String status;
	private String status_sefaz;
	private String mensagem_sefaz;
	private String chave_nfe;
	private String numero;
	private String serie;
	private String caminho_xml_nota_fiscal;
	private String caminho_danfe;

	// Getters e Setters

	public String getCnpj_emitente() {
		return cnpj_emitente;
	}

	public void setCnpj_emitente(String cnpj_emitente) {
		this.cnpj_emitente = cnpj_emitente;
	}

	public String getRef() {
		return ref;
	}

	public void setRef(String ref) {
		this.ref = ref;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatus_sefaz() {
		return status_sefaz;
	}

	public void setStatus_sefaz(String status_sefaz) {
		this.status_sefaz = status_sefaz;
	}

	public String getMensagem_sefaz() {
		return mensagem_sefaz;
	}

	public void setMensagem_sefaz(String mensagem_sefaz) {
		this.mensagem_sefaz = mensagem_sefaz;
	}

	public String getChave_nfe() {
		return chave_nfe;
	}

	public void setChave_nfe(String chave_nfe) {
		this.chave_nfe = chave_nfe;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getSerie() {
		return serie;
	}

	public void setSerie(String serie) {
		this.serie = serie;
	}

	public String getCaminho_xml_nota_fiscal() {
		return caminho_xml_nota_fiscal;
	}

	public void setCaminho_xml_nota_fiscal(String caminho_xml_nota_fiscal) {
		this.caminho_xml_nota_fiscal = caminho_xml_nota_fiscal;
	}

	public String getCaminho_danfe() {
		return caminho_danfe;
	}

	public void setCaminho_danfe(String caminho_danfe) {
		this.caminho_danfe = caminho_danfe;
	}

}
