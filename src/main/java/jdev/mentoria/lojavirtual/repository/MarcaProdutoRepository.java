package jdev.mentoria.lojavirtual.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import jdev.mentoria.lojavirtual.model.CategoriaProduto;
import jdev.mentoria.lojavirtual.model.MarcaProduto;


@Repository
@Transactional
public interface MarcaProdutoRepository extends JpaRepository<MarcaProduto, Long> {
	
	@Query("Select a from MarcaProduto a where upper(trim(a.nomeDesc)) like %?1%")
	List<MarcaProduto> buscarMarcaDesc(String nomeDesc);

	
	@Query(
		    nativeQuery = true,
		    value = "SELECT CAST(CEIL(COUNT(1) / 5.0) AS INTEGER) FROM marca_produto WHERE empresa_id = ?1"
		)
		public Integer quantidadePagina(Long idEmpresa);
	
	@Query("Select a from MarcaProduto a where upper(trim(a.nomeDesc)) like %?1% and a.empresa.id = ?2")
	List<MarcaProduto> buscarMarcaDesc(String nomeDesc, String empresa);
	
	
	
	@Query(value = "Select a from MarcaProduto a where a.empresa.id = ?1")
	public List<MarcaProduto> findbyPage(Long idEmpresa, Pageable pageable);
	
	
	@Query(value = "select count(1) > 0 from marca_produto where upper(trim(nome_desc)) = :nomeCat", 
		       nativeQuery = true)
		boolean existeMarca(@Param("nomeCat") String nomeCat);
	
	
	
	@Query("Select a from MarcaProduto a where a.empresa.id = ?1")
	List<MarcaProduto> findAll(Long codEmp);
	
}
