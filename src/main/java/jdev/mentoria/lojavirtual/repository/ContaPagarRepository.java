package jdev.mentoria.lojavirtual.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import jdev.mentoria.lojavirtual.model.ContaPagar;

@Repository
public interface ContaPagarRepository extends JpaRepository<ContaPagar, Long> {

	@Query(value = "Select a from ContaPagar a where upper(trim(a.descricao)) like %?1%")
	List<ContaPagar> buscaContaDesc(String desc);
	
	
	@Query(value = "Select a from ContaPagar a where a.pessoa.id = ?1")
	List<ContaPagar> buscaContaPorPessoa(Long idPessoa);
	
	
	@Query(value = "Select a from ContaPagar a where a.pessoa_fornecedor.id = ?1")
	List<ContaPagar> buscaContaPorFornecedor(Long idFornecedor);
	
	
	@Query(value = "Select a from ContaPagar a where a.empresa.id = ?1")
	List<ContaPagar> buscaContaPorEmpresas(Long idEmpresa);
	
}
