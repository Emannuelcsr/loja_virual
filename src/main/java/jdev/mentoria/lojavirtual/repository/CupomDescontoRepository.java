package jdev.mentoria.lojavirtual.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import jdev.mentoria.lojavirtual.model.CupomDesconto;

@Repository
public interface CupomDescontoRepository extends JpaRepository<CupomDesconto, Long> {

	@Query(value = "Select c from CupomDesconto c where c.empresa.id = ?1")
	public List<CupomDesconto> cupomDescontoPorEmpresa(Long idEmpresa);


}
