package jdev.mentoria.lojavirtual.model.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ConsultaCnpjDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<AtividadeDTO> atividade_principal = new ArrayList<AtividadeDTO>();

	private List<AtividadeDTO> atividades_secundarias = new ArrayList<AtividadeDTO>();

	private List<QSADto> qsa;

	private String logradouro;
	private String numero;
	private String complemento;
	private String municipio;
	private String bairro;
	private String uf;
	private String cep;
	private String email;
	private String telefone;

	private String dataSituacao;

	private String cnpj;

	private String ultimaAtualizacao;

	private String status;
	private String efr;

	private String motivoSituacao;

	private String situacaoEspecial;

	private String dataSituacaoEspecial;

	private String capitalSocial;

	private SimplesDto simples;
	private SimeiDto simei;

	private Map<String, Object> extra;
	private BillingDto billing;

	public List<AtividadeDTO> getAtividade_principal() {
		return atividade_principal;
	}

	public void setAtividade_principal(List<AtividadeDTO> atividade_principal) {
		this.atividade_principal = atividade_principal;
	}

	public List<AtividadeDTO> getAtividades_secundarias() {
		return atividades_secundarias;
	}

	public void setAtividades_secundarias(List<AtividadeDTO> atividades_secundarias) {
		this.atividades_secundarias = atividades_secundarias;
	}

	public List<QSADto> getQsa() {
		return qsa;
	}

	public void setQsa(List<QSADto> qsa) {
		this.qsa = qsa;
	}

	public String getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public String getMunicipio() {
		return municipio;
	}

	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getDataSituacao() {
		return dataSituacao;
	}

	public void setDataSituacao(String dataSituacao) {
		this.dataSituacao = dataSituacao;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getUltimaAtualizacao() {
		return ultimaAtualizacao;
	}

	public void setUltimaAtualizacao(String ultimaAtualizacao) {
		this.ultimaAtualizacao = ultimaAtualizacao;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getEfr() {
		return efr;
	}

	public void setEfr(String efr) {
		this.efr = efr;
	}

	public String getMotivoSituacao() {
		return motivoSituacao;
	}

	public void setMotivoSituacao(String motivoSituacao) {
		this.motivoSituacao = motivoSituacao;
	}

	public String getSituacaoEspecial() {
		return situacaoEspecial;
	}

	public void setSituacaoEspecial(String situacaoEspecial) {
		this.situacaoEspecial = situacaoEspecial;
	}

	public String getDataSituacaoEspecial() {
		return dataSituacaoEspecial;
	}

	public void setDataSituacaoEspecial(String dataSituacaoEspecial) {
		this.dataSituacaoEspecial = dataSituacaoEspecial;
	}

	public String getCapitalSocial() {
		return capitalSocial;
	}

	public void setCapitalSocial(String capitalSocial) {
		this.capitalSocial = capitalSocial;
	}

	public SimplesDto getSimples() {
		return simples;
	}

	public void setSimples(SimplesDto simples) {
		this.simples = simples;
	}

	public SimeiDto getSimei() {
		return simei;
	}

	public void setSimei(SimeiDto simei) {
		this.simei = simei;
	}

	public Map<String, Object> getExtra() {
		return extra;
	}

	public void setExtra(Map<String, Object> extra) {
		this.extra = extra;
	}

	public BillingDto getBilling() {
		return billing;
	}

	public void setBilling(BillingDto billing) {
		this.billing = billing;
	}

}
