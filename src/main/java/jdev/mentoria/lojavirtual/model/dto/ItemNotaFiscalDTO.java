package jdev.mentoria.lojavirtual.model.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ItemNotaFiscalDTO implements Serializable {


	/** Número do item na nota */
	@JsonProperty("numero_item")
	private Integer numeroItem;

	/** Código interno do produto */
	@JsonProperty("codigo_produto")
	private String codigoProduto;

	/** Descrição do produto */
	@JsonProperty("descricao")
	private String descricao;

	/** CFOP (definido pelo contador) */
	@JsonProperty("cfop")
	private Integer cfop;

	/** Unidade (ex: un, kg) */
	@JsonProperty("unidade_comercial")
	private String unidadeComercial;

	/** Quantidade */
	@JsonProperty("quantidade_comercial")
	private BigDecimal quantidadeComercial;

	/** Valor unitário */
	@JsonProperty("valor_unitario_comercial")
	private BigDecimal valorUnitarioComercial;

	/** Valor unitário tributável */
	@JsonProperty("valor_unitario_tributavel")
	private BigDecimal valorUnitarioTributavel;

	/** Unidade tributável */
	@JsonProperty("unidade_tributavel")
	private String unidadeTributavel;

	/** Código NCM (produto) */
	@JsonProperty("codigo_ncm")
	private String codigoNcm;

	/** Quantidade tributável */
	@JsonProperty("quantidade_tributavel")
	private BigDecimal quantidadeTributavel;

	/** Valor total do item */
	@JsonProperty("valor_bruto")
	private BigDecimal valorBruto;

	/** ICMS */
	@JsonProperty("icms_situacao_tributaria")
	private String icmsSituacaoTributaria;

	@JsonProperty("icms_origem")
	private String icmsOrigem;

	/** PIS */
	@JsonProperty("pis_situacao_tributaria")
	private String pisSituacaoTributaria;

	/** COFINS */
	@JsonProperty("cofins_situacao_tributaria")
	private String cofinsSituacaoTributaria;

	public Integer getNumeroItem() {
		return numeroItem;
	}

	public void setNumeroItem(Integer numeroItem) {
		this.numeroItem = numeroItem;
	}

	public String getCodigoProduto() {
		return codigoProduto;
	}

	public void setCodigoProduto(String codigoProduto) {
		this.codigoProduto = codigoProduto;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Integer getCfop() {
		return cfop;
	}

	public void setCfop(Integer cfop) {
		this.cfop = cfop;
	}

	public String getUnidadeComercial() {
		return unidadeComercial;
	}

	public void setUnidadeComercial(String unidadeComercial) {
		this.unidadeComercial = unidadeComercial;
	}

	public BigDecimal getQuantidadeComercial() {
		return quantidadeComercial;
	}

	public void setQuantidadeComercial(BigDecimal quantidadeComercial) {
		this.quantidadeComercial = quantidadeComercial;
	}

	public BigDecimal getValorUnitarioComercial() {
		return valorUnitarioComercial;
	}

	public void setValorUnitarioComercial(BigDecimal valorUnitarioComercial) {
		this.valorUnitarioComercial = valorUnitarioComercial;
	}

	public BigDecimal getValorUnitarioTributavel() {
		return valorUnitarioTributavel;
	}

	public void setValorUnitarioTributavel(BigDecimal valorUnitarioTributavel) {
		this.valorUnitarioTributavel = valorUnitarioTributavel;
	}

	public String getUnidadeTributavel() {
		return unidadeTributavel;
	}

	public void setUnidadeTributavel(String unidadeTributavel) {
		this.unidadeTributavel = unidadeTributavel;
	}

	public String getCodigoNcm() {
		return codigoNcm;
	}

	public void setCodigoNcm(String codigoNcm) {
		this.codigoNcm = codigoNcm;
	}

	public BigDecimal getQuantidadeTributavel() {
		return quantidadeTributavel;
	}

	public void setQuantidadeTributavel(BigDecimal quantidadeTributavel) {
		this.quantidadeTributavel = quantidadeTributavel;
	}

	public BigDecimal getValorBruto() {
		return valorBruto;
	}

	public void setValorBruto(BigDecimal valorBruto) {
		this.valorBruto = valorBruto;
	}

	public String getIcmsSituacaoTributaria() {
		return icmsSituacaoTributaria;
	}

	public void setIcmsSituacaoTributaria(String icmsSituacaoTributaria) {
		this.icmsSituacaoTributaria = icmsSituacaoTributaria;
	}

	public String getIcmsOrigem() {
		return icmsOrigem;
	}

	public void setIcmsOrigem(String icmsOrigem) {
		this.icmsOrigem = icmsOrigem;
	}

	public String getPisSituacaoTributaria() {
		return pisSituacaoTributaria;
	}

	public void setPisSituacaoTributaria(String pisSituacaoTributaria) {
		this.pisSituacaoTributaria = pisSituacaoTributaria;
	}

	public String getCofinsSituacaoTributaria() {
		return cofinsSituacaoTributaria;
	}

	public void setCofinsSituacaoTributaria(String cofinsSituacaoTributaria) {
		this.cofinsSituacaoTributaria = cofinsSituacaoTributaria;
	}

}