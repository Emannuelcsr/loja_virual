package jdev.mentoria.lojavirtual.repository;

import java.util.List;

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

}
