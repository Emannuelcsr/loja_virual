package jdev.mentoria.lojavirtual.model.dto;

import java.io.Serializable;

import jakarta.validation.constraints.NotEmpty;

public class ObjetoRequisicaoRelatorioProdCompraNotaFiscalDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String nomeProduto ="";

	@NotEmpty(message = "Informe a data incial")
	private String dataInicial;

	@NotEmpty(message = "Informe a data final")
	private String dataFinal;

	private String codNota ="";

	private String codProduto ="";
	
	private String valorVenda ="";
	
	private String quantidadeComprada ="";
	
	private String codigoFornecedor="";
	
	private String nomeFornecedor="";
	
	private String dataCompra="";

	public String getNomeProduto() {
		return nomeProduto;
	}

	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}

	public String getDataInicial() {
		return dataInicial;
	}

	public void setDataInicial(String dataInicial) {
		this.dataInicial = dataInicial;
	}

	public String getDataFinal() {
		return dataFinal;
	}

	public void setDataFinal(String dataFinal) {
		this.dataFinal = dataFinal;
	}

	public String getCodNota() {
		return codNota;
	}

	public void setCodNota(String codNota) {
		this.codNota = codNota;
	}

	public String getCodProduto() {
		return codProduto;
	}

	public void setCodProduto(String codProduto) {
		this.codProduto = codProduto;
	}

	public String getValorVenda() {
		return valorVenda;
	}

	public void setValorVenda(String valorVenda) {
		this.valorVenda = valorVenda;
	}

	public String getQuantidadeComprada() {
		return quantidadeComprada;
	}

	public void setQuantidadeComprada(String quantidadeComprada) {
		this.quantidadeComprada = quantidadeComprada;
	}

	public String getCodigoFornecedor() {
		return codigoFornecedor;
	}

	public void setCodigoFornecedor(String codigoFornecedor) {
		this.codigoFornecedor = codigoFornecedor;
	}

	public String getNomeFornecedor() {
		return nomeFornecedor;
	}

	public void setNomeFornecedor(String nomeFornecedor) {
		this.nomeFornecedor = nomeFornecedor;
	}

	public String getDataCompra() {
		return dataCompra;
	}

	public void setDataCompra(String dataCompra) {
		this.dataCompra = dataCompra;
	}

	
	
	
	
}
