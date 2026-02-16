package jdev.mentoria.lojavirtual.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import jdev.mentoria.lojavirtual.model.PessoaFisica;
import jdev.mentoria.lojavirtual.model.PessoaJuridica;

@Repository
public interface PessoaRepository extends CrudRepository<PessoaJuridica, Long> {

	@Query("select pj from PessoaJuridica pj where pj.cnpj = ?1")
	PessoaJuridica existeCnpjCadastrado(String cnpj);

	@Query("select pj from PessoaJuridica pj where pj.inscEstadual = ?1")
	PessoaJuridica existeInscriEstadualCadastrado(String inscEstadual);

	
	@Query("select pj from PessoaFisica pj where pj.cpf = ?1")
	PessoaFisica existeCpfCadastrado(String cpf);
	
}
