package jdev.mentoria.lojavirtual.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import jdev.mentoria.lojavirtual.model.VendaCompraLojaVirtual;

@Repository
public interface Vd_cp_Loja_virtual_Repository extends JpaRepository<VendaCompraLojaVirtual, Long> {

	@Modifying
	@Transactional
	@Query(nativeQuery = true, value = """
			WITH del_status AS (
			  DELETE FROM status_rastreio WHERE venda_compra_loja_virtual_id = ?1 RETURNING 1
			),
			del_itens AS (
			  DELETE FROM item_venda_loja WHERE venda_compra_loja_virtual_id = ?1 RETURNING 1
			),
			del_nf AS (
			  DELETE FROM nota_fiscal_venda WHERE venda_compra_loja_virtual_id = ?1 RETURNING 1
			)
			DELETE FROM venda_compra_loja_virtual WHERE id = ?1;
			""")
	void exclusaoTotalVendaBanco(Long idVenda);

	@Query("Select a from VendaCompraLojaVirtual a where a.id = ?1 and a.excluido = false")
	VendaCompraLojaVirtual findByIdExclusao(Long id);

	@Modifying
	@Transactional
	@Query("update VendaCompraLojaVirtual v set v.excluido = true where v.id = :id")
	void exclusaoLogicaVendaBanco(@Param("id") Long idVenda);

	@Modifying
	@Transactional
	@Query("update VendaCompraLojaVirtual v set v.excluido = false where v.id = :id")
	void ativaVendaBanco(@Param("id") Long idVenda);

	
	
	
	/**
	 * Retorna todas as vendas que contenham o produto informado, considerando
	 * apenas vendas não excluídas (excluido = false).
	 * 
	 *“Em quais vendas esse produto apareceu?”
	
	 *
	 * @param idProduto ID do produto a ser filtrado
	 * @return Lista de VendaCompraLojaVirtual relacionadas ao produto
	 */
	@Query("select distinct i.vendaCompraLojaVirtual " + "from ItemVendaLoja i "
			+ "where i.vendaCompraLojaVirtual.excluido = false " + "and i.produto.id = ?1")
	List<VendaCompraLojaVirtual> vendaPorProduto(Long idProduto);

	
	
	@Query("select distinct i.vendaCompraLojaVirtual " + "from ItemVendaLoja i "
			+ "where i.vendaCompraLojaVirtual.excluido = false " + "and upper(trim(i.produto.nome)) like %?1%")
	List<VendaCompraLojaVirtual> vendaPorNomeProduto(String nomeProduto);

	
	@Query("select distinct i.vendaCompraLojaVirtual " + "from ItemVendaLoja i "
			+ "where i.vendaCompraLojaVirtual.excluido = false " + "and upper(trim(i.vendaCompraLojaVirtual.pessoa.nome)) like %?1%")
	List<VendaCompraLojaVirtual> vendaPorNomeCliente(String cliente);

	@Query("select distinct i.vendaCompraLojaVirtual " + "from ItemVendaLoja i "
			+ "where i.vendaCompraLojaVirtual.excluido = false " + "and upper(trim(i.vendaCompraLojaVirtual.enderecoCobranca.ruaLogra)) like %?1%")
	List<VendaCompraLojaVirtual> vendaPorEndCobranca(String rua);

	@Query("select distinct i.vendaCompraLojaVirtual " + "from ItemVendaLoja i "
			+ "where i.vendaCompraLojaVirtual.excluido = false " + "and upper(trim(i.vendaCompraLojaVirtual.enderecoEntrega.ruaLogra)) like %?1%")
	List<VendaCompraLojaVirtual> vendaPorEndEntrega(String trim);

}
