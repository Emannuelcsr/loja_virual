package jdev.mentoria.lojavirtual.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import jdev.mentoria.lojavirtual.model.AvaliacaoProduto;

@Transactional
@Repository
public interface AvaliacaoProdutoRepository extends JpaRepository<AvaliacaoProduto, Long> {

	List<AvaliacaoProduto> findByProdutoId(Long produtoId);

	@Query(value = "select a from AvaliacaoProduto a where a.pessoa.id = ?1 and a.produto.id = ?2")
	List<AvaliacaoProduto> findByProdutoPessoaId(Long pessoaId,Long produtoId);
	
	List<AvaliacaoProduto> findByPessoaId(Long pessoaId);

	List<AvaliacaoProduto> findByEmpresaId(Long empresaId);

	boolean existsByPessoaIdAndEmpresaIdAndProdutoId(Long pessoaId, Long empresaId, Long produtoId);

	long countByProdutoId(Long produtoId);

	@Query("select avg(a.nota) from AvaliacaoProduto a where a.produto.id = :produtoId")
	Double mediaNotaPorProduto(@Param("produtoId") Long produtoId);

}
