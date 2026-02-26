package jdev.mentoria.lojavirtual.model.dto;

public class ItemVendaDTO {
	
	
	private Double quantidade;
	
	private ProdutoDTO produto;

	public Double getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Double quantidade) {
		this.quantidade = quantidade;
	}

	public ProdutoDTO getProduto() {
		return produto;
	}

	public void setProduto(ProdutoDTO produto) {
		this.produto = produto;
	}
	
	

}
