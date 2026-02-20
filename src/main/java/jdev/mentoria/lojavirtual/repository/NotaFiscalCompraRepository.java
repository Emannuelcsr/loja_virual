package jdev.mentoria.lojavirtual.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import jdev.mentoria.lojavirtual.model.NotaFiscalCompra;

@Repository
@Transactional
public interface NotaFiscalCompraRepository extends JpaRepository<NotaFiscalCompra, Long> {

	@Query(value = "Select a from NotaFiscalCompra a where upper(trim(a.descricaoObs)) like %?1%")
	List<NotaFiscalCompra> buscarNotaFiscalPorDesc(String desc);

	@Query(value = "Select a from NotaFiscalCompra a where a.pessoa.id = ?1")
	List<NotaFiscalCompra> buscarNotaFiscalPorPessoa(Long idPessoa);

	@Query(value = "Select a from NotaFiscalCompra a where a.contaPagar.id = ?1")
	List<NotaFiscalCompra> buscarNotaFiscalPorContaPagar(Long idContaPagar);

	@Query(value = "Select a from NotaFiscalCompra a where a.empresa.id = ?1")
	List<NotaFiscalCompra> buscarNotaFiscalPorEmpresa(Long idEmpresa);

	@org.springframework.transaction.annotation.Transactional
	@Modifying(flushAutomatically = true, clearAutomatically = true)
	@Query(nativeQuery = true, value = "delete from nota_item_produto where nota_fiscal_compra_id = ?1 ")
	void deleteItemNotaFiscalCompra(Long idNotaFiscalCompra);

	
	
}
