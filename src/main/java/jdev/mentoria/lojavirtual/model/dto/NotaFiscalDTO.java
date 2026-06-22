package jdev.mentoria.lojavirtual.model.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NotaFiscalDTO implements Serializable	 {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	/** Natureza da operação (ex: Venda, Remessa, Devolução) */
    @JsonProperty("natureza_operacao")
    private String naturezaOperacao;

    /** Data de emissão da nota (formato: yyyy-MM-dd ou ISO) */
    @JsonProperty("data_emissao")
    private String dataEmissao;

    /** Data de entrada/saída */
    @JsonProperty("data_entrada_saida")
    private String dataEntradaSaida;

    /** Tipo do documento (1 = NFe normal) */
    @JsonProperty("tipo_documento")
    private Integer tipoDocumento;

    /** Finalidade da emissão (1 = normal) */
    @JsonProperty("finalidade_emissao")
    private Integer finalidadeEmissao;

    /** CNPJ do emitente */
    @JsonProperty("cnpj_emitente")
    private String cnpjEmitente;

    /** CPF do emitente (caso não seja CNPJ) */
    @JsonProperty("cpf_emitente")
    private String cpfEmitente;

    /** Razão social da empresa */
    @JsonProperty("nome_emitente")
    private String nomeEmitente;

    /** Nome fantasia */
    @JsonProperty("nome_fantasia_emitente")
    private String nomeFantasiaEmitente;

    /** Endereço do emitente */
    @JsonProperty("logradouro_emitente")
    private String logradouroEmitente;

    @JsonProperty("numero_emitente")
    private Integer numeroEmitente;

    @JsonProperty("bairro_emitente")
    private String bairroEmitente;

    @JsonProperty("municipio_emitente")
    private String municipioEmitente;

    @JsonProperty("uf_emitente")
    private String ufEmitente;

    @JsonProperty("cep_emitente")
    private String cepEmitente;

    /** Inscrição estadual */
    @JsonProperty("inscricao_estadual_emitente")
    private String inscricaoEstadualEmitente;

    /** Dados do cliente */
    @JsonProperty("nome_destinatario")
    private String nomeDestinatario;

    @JsonProperty("cpf_destinatario")
    private String cpfDestinatario;

    @JsonProperty("inscricao_estadual_destinatario")
    private String inscricaoEstadualDestinatario;

    @JsonProperty("telefone_destinatario")
    private String telefoneDestinatario;

    @JsonProperty("logradouro_destinatario")
    private String logradouroDestinatario;

    @JsonProperty("numero_destinatario")
    private Integer numeroDestinatario;

    @JsonProperty("bairro_destinatario")
    private String bairroDestinatario;

    @JsonProperty("municipio_destinatario")
    private String municipioDestinatario;

    @JsonProperty("uf_destinatario")
    private String ufDestinatario;

    @JsonProperty("pais_destinatario")
    private String paisDestinatario;

    @JsonProperty("cep_destinatario")
    private String cepDestinatario;

    /** Valores da nota */
    @JsonProperty("valor_frete")
    private BigDecimal valorFrete;

    @JsonProperty("valor_seguro")
    private BigDecimal valorSeguro;

    @JsonProperty("valor_total")
    private BigDecimal valorTotal;

    @JsonProperty("valor_produtos")
    private BigDecimal valorProdutos;

    /** Modalidade de frete */
    @JsonProperty("modalidade_frete")
    private Integer modalidadeFrete;

    /** Lista de produtos */
    @JsonProperty("items")
    private List<ItemNotaFiscalDTO> items;

	public String getNaturezaOperacao() {
		return naturezaOperacao;
	}

	public void setNaturezaOperacao(String naturezaOperacao) {
		this.naturezaOperacao = naturezaOperacao;
	}

	public String getDataEmissao() {
		return dataEmissao;
	}

	public void setDataEmissao(String dataEmissao) {
		this.dataEmissao = dataEmissao;
	}

	public String getDataEntradaSaida() {
		return dataEntradaSaida;
	}

	public void setDataEntradaSaida(String dataEntradaSaida) {
		this.dataEntradaSaida = dataEntradaSaida;
	}

	public Integer getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(Integer tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public Integer getFinalidadeEmissao() {
		return finalidadeEmissao;
	}

	public void setFinalidadeEmissao(Integer finalidadeEmissao) {
		this.finalidadeEmissao = finalidadeEmissao;
	}

	public String getCnpjEmitente() {
		return cnpjEmitente;
	}

	public void setCnpjEmitente(String cnpjEmitente) {
		this.cnpjEmitente = cnpjEmitente;
	}

	public String getCpfEmitente() {
		return cpfEmitente;
	}

	public void setCpfEmitente(String cpfEmitente) {
		this.cpfEmitente = cpfEmitente;
	}

	public String getNomeEmitente() {
		return nomeEmitente;
	}

	public void setNomeEmitente(String nomeEmitente) {
		this.nomeEmitente = nomeEmitente;
	}

	public String getNomeFantasiaEmitente() {
		return nomeFantasiaEmitente;
	}

	public void setNomeFantasiaEmitente(String nomeFantasiaEmitente) {
		this.nomeFantasiaEmitente = nomeFantasiaEmitente;
	}

	public String getLogradouroEmitente() {
		return logradouroEmitente;
	}

	public void setLogradouroEmitente(String logradouroEmitente) {
		this.logradouroEmitente = logradouroEmitente;
	}

	public Integer getNumeroEmitente() {
		return numeroEmitente;
	}

	public void setNumeroEmitente(Integer numeroEmitente) {
		this.numeroEmitente = numeroEmitente;
	}

	public String getBairroEmitente() {
		return bairroEmitente;
	}

	public void setBairroEmitente(String bairroEmitente) {
		this.bairroEmitente = bairroEmitente;
	}

	public String getMunicipioEmitente() {
		return municipioEmitente;
	}

	public void setMunicipioEmitente(String municipioEmitente) {
		this.municipioEmitente = municipioEmitente;
	}

	public String getUfEmitente() {
		return ufEmitente;
	}

	public void setUfEmitente(String ufEmitente) {
		this.ufEmitente = ufEmitente;
	}

	public String getCepEmitente() {
		return cepEmitente;
	}

	public void setCepEmitente(String cepEmitente) {
		this.cepEmitente = cepEmitente;
	}

	public String getInscricaoEstadualEmitente() {
		return inscricaoEstadualEmitente;
	}

	public void setInscricaoEstadualEmitente(String inscricaoEstadualEmitente) {
		this.inscricaoEstadualEmitente = inscricaoEstadualEmitente;
	}

	public String getNomeDestinatario() {
		return nomeDestinatario;
	}

	public void setNomeDestinatario(String nomeDestinatario) {
		this.nomeDestinatario = nomeDestinatario;
	}

	public String getCpfDestinatario() {
		return cpfDestinatario;
	}

	public void setCpfDestinatario(String cpfDestinatario) {
		this.cpfDestinatario = cpfDestinatario;
	}

	public String getInscricaoEstadualDestinatario() {
		return inscricaoEstadualDestinatario;
	}

	public void setInscricaoEstadualDestinatario(String inscricaoEstadualDestinatario) {
		this.inscricaoEstadualDestinatario = inscricaoEstadualDestinatario;
	}

	public String getTelefoneDestinatario() {
		return telefoneDestinatario;
	}

	public void setTelefoneDestinatario(String telefoneDestinatario) {
		this.telefoneDestinatario = telefoneDestinatario;
	}

	public String getLogradouroDestinatario() {
		return logradouroDestinatario;
	}

	public void setLogradouroDestinatario(String logradouroDestinatario) {
		this.logradouroDestinatario = logradouroDestinatario;
	}

	public Integer getNumeroDestinatario() {
		return numeroDestinatario;
	}

	public void setNumeroDestinatario(Integer numeroDestinatario) {
		this.numeroDestinatario = numeroDestinatario;
	}

	public String getBairroDestinatario() {
		return bairroDestinatario;
	}

	public void setBairroDestinatario(String bairroDestinatario) {
		this.bairroDestinatario = bairroDestinatario;
	}

	public String getMunicipioDestinatario() {
		return municipioDestinatario;
	}

	public void setMunicipioDestinatario(String municipioDestinatario) {
		this.municipioDestinatario = municipioDestinatario;
	}

	public String getUfDestinatario() {
		return ufDestinatario;
	}

	public void setUfDestinatario(String ufDestinatario) {
		this.ufDestinatario = ufDestinatario;
	}

	public String getPaisDestinatario() {
		return paisDestinatario;
	}

	public void setPaisDestinatario(String paisDestinatario) {
		this.paisDestinatario = paisDestinatario;
	}

	public String getCepDestinatario() {
		return cepDestinatario;
	}

	public void setCepDestinatario(String cepDestinatario) {
		this.cepDestinatario = cepDestinatario;
	}

	public BigDecimal getValorFrete() {
		return valorFrete;
	}

	public void setValorFrete(BigDecimal valorFrete) {
		this.valorFrete = valorFrete;
	}

	public BigDecimal getValorSeguro() {
		return valorSeguro;
	}

	public void setValorSeguro(BigDecimal valorSeguro) {
		this.valorSeguro = valorSeguro;
	}

	public BigDecimal getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}

	public BigDecimal getValorProdutos() {
		return valorProdutos;
	}

	public void setValorProdutos(BigDecimal valorProdutos) {
		this.valorProdutos = valorProdutos;
	}

	public Integer getModalidadeFrete() {
		return modalidadeFrete;
	}

	public void setModalidadeFrete(Integer modalidadeFrete) {
		this.modalidadeFrete = modalidadeFrete;
	}

	public List<ItemNotaFiscalDTO> getItems() {
		return items;
	}

	public void setItems(List<ItemNotaFiscalDTO> items) {
		this.items = items;
	}

}