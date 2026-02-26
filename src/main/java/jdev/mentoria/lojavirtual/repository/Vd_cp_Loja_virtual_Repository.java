package jdev.mentoria.lojavirtual.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
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
}
