package jdev.mentoria.lojavirtual.model.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jdev.mentoria.lojavirtual.model.Endereco;
import jdev.mentoria.lojavirtual.model.Pessoa;
import jdev.mentoria.lojavirtual.model.PessoaJuridica;

public class VendaCompraLojaVirtualDTO {

	private Long id;
	
	private BigDecimal valorTotal;

	private Pessoa pessoa;

	private Endereco cobranca;

	private Endereco entrega;
	
	private Long empresa;
	
	
	
	private BigDecimal valorDesconto;

	private BigDecimal valorFrete;

	private List<ItemVendaDTO> itemVendaDTOs = new ArrayList<ItemVendaDTO>();
	
	


	public BigDecimal getValorDesconto() {
		return valorDesconto;
	}

	public void setValorDesconto(BigDecimal valorDesconto) {
		this.valorDesconto = valorDesconto;
	}

	public Endereco getCobranca() {
		return cobranca;
	}

	public void setCobranca(Endereco cobranca) {
		this.cobranca = cobranca;
	}

	public Endereco getEntrega() {
		return entrega;
	}

	public void setEntrega(Endereco entrega) {
		this.entrega = entrega;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public BigDecimal getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}

	public BigDecimal getValorFrete() {
		return valorFrete;
	}

	public void setValorFrete(BigDecimal valorFrete) {
		this.valorFrete = valorFrete;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<ItemVendaDTO> getItemVendaDTOs() {
		return itemVendaDTOs;
	}

	public void setItemVendaDTOs(List<ItemVendaDTO> itemVendaDTOs) {
		this.itemVendaDTOs = itemVendaDTOs;
	}

	public Long getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Long empresa) {
		this.empresa = empresa;
	}
	
		

}
