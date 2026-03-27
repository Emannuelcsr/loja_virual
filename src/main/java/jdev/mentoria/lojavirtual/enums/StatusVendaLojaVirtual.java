package jdev.mentoria.lojavirtual.enums;

public enum StatusVendaLojaVirtual {
	
	
	FINALIZADA("Finalizada"),
	CANCELADA("Cancelada"),
	ABANDONOU_CARRINHO("Abandonou carrinho");
	;
	private String descricao = "";
	
	
	private StatusVendaLojaVirtual(String valor) {
		
		this.descricao = valor;
	}


	public String getDescricao() {
		return descricao;
	}


	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	
	@Override
	public String toString() {
		return this.descricao;
	}
	
}
