package jdev.mentoria.lojavirtual.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import jdev.mentoria.lojavirtual.model.NotaItemProduto;


@Repository
@Transactional
public interface NotaItemProdutoRepository extends JpaRepository<NotaItemProduto,Long> {

	
	@Query(value = "select a from NotaItemProduto a where a.produto.id = ?1 and a.notaFiscalCompra.id = ?2")
	List<NotaItemProduto> buscaNotaItemPorProdutoNota(Long idProduto, Long idNotaFiscal);
	
	
	@Query(value = "select a from NotaItemProduto a where a.produto.id = ?1")
	List<NotaItemProduto> buscaNotaItemProduto(Long idProduto);
	
	@Query(value = "select a from NotaItemProduto a where  a.notaFiscalCompra.id = ?1")
	List<NotaItemProduto> buscaNotaItemPorNotaFiscal(Long idNotaFiscal);
	
	@Query(value = "select a from NotaItemProduto a where a.empresa.id = ?1")
	List<NotaItemProduto> buscaNotaItemPorEmpresa(Long idEmpresa);
	
}
