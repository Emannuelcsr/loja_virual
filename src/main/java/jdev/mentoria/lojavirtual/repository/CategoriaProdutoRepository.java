package jdev.mentoria.lojavirtual.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jdev.mentoria.lojavirtual.model.CategoriaProduto;

@Repository
public interface CategoriaProdutoRepository  extends JpaRepository<CategoriaProduto, Long>  {
	
	
	@Query(value = "select count(1) > 0 from categoria_produto where upper(trim(nome_desc)) = :nomeCat", 
		       nativeQuery = true)
		boolean existeCategoria(@Param("nomeCat") String nomeCat);

	@Query("Select a from CategoriaProduto a where upper(trim(a.nomeDesc)) like %?1%")
	List<CategoriaProduto> buscarCategoriaDesc(String nomeDesc);

	@Query("Select a from CategoriaProduto a where a.empresa.id = ?1")
	List<CategoriaProduto> findAll(Long codEmp);

	
	@Query("Select a from CategoriaProduto a where upper(trim(a.nomeDesc)) like %?1% and a.empresa.id = ?2")
	List<CategoriaProduto> buscarCategoriaDesc(String nomeDesc, String empresa);
	
	@Query(
		    nativeQuery = true,
		    value = "SELECT CAST(CEIL(COUNT(1) / 5.0) AS INTEGER) FROM categoria_produto WHERE empresa_id = ?1"
		)
		public Integer quantidadePagina(Long idEmpresa);
	
	@Query(value = "Select a from CategoriaProduto a where a.empresa.id = ?1")
	public List<CategoriaProduto> findbyPage(Long idEmpresa, Pageable pageable);
}
