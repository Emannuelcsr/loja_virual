package jdev.mentoria.lojavirtual.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import jdev.mentoria.lojavirtual.model.PessoaJuridica;

@Repository
public interface PessoaRepository extends CrudRepository<PessoaJuridica, Long> {

	@Query("select pj from PessoaJuridica pj where pj.cnpj = ?1")
	PessoaJuridica existeCnpjCadastrado(String cnpj);

	

}
