package jdev.mentoria.lojavirtual.model.dto;

import java.math.BigDecimal;

public class ProdutoDTO {

	   private Long id;
	   private String nome;
	   private BigDecimal valorVenda;
	   public Long getId() {
		   return id;
	   }
	   public void setId(Long id) {
		   this.id = id;
	   }
	   public String getNome() {
		   return nome;
	   }
	   public void setNome(String nome) {
		   this.nome = nome;
	   }
	   public BigDecimal getValorVenda() {
		   return valorVenda;
	   }
	   public void setValorVenda(BigDecimal valorVenda) {
		   this.valorVenda = valorVenda;
	   }
	   
	   
	
}
