package jdev.mentoria.lojavirtual.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import jdev.mentoria.lojavirtual.model.CupomDesconto;

@Repository
public interface CupomDescontoRepository extends JpaRepository<CupomDesconto, Long> {

	@Query(value = "Select c from CupomDesconto c where c.empresa.id = ?1")
	public List<CupomDesconto> cupomDescontoPorEmpresa(Long idEmpresa);

	
	@Query(value = "Select a from CupomDesconto a where upper(trim(a.codDescricao)) like %?1%")
	List<CupomDesconto> buscaCupomDesc(String desc);
	
	@Query(value = "Select a from CupomDesconto a where a.empresa.id = ?1")
	public List<CupomDesconto> findbyPage(Long idEmpresa, Pageable pageable);
	
	@Query(nativeQuery = true, value = "SELECT CAST(CEIL(COUNT(1) / 5.0) AS INTEGER) FROM cupom_desconto WHERE empresa_id = ?1")
	public Integer quantidadePagina(Long idEmpresa);
}
