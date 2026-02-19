package jdev.mentoria.lojavirtual.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jdev.mentoria.lojavirtual.model.Produto;

@Repository
public interface ProdutoRepository  extends JpaRepository<Produto, Long>  {
	
	
	@Query(value = "select count(1) > 0 from produto where upper(trim(nome)) = :nome", 
		       nativeQuery = true)
		boolean existePrdouto(@Param("nome") String nome);

	@Query("Select a from Produto a where upper(trim(a.nome)) like %?1%")
	List<Produto> buscarProdutoNome(String nome);


	
	
	@Query("Select a from Produto a where upper(trim(a.nome)) like %?1%and a.empresa.id = ?2")
	List<Produto> buscarProdutoNome(String nome,Long idEmpresa);
	
}
	