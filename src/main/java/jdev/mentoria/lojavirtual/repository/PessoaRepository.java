package jdev.mentoria.lojavirtual.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jdev.mentoria.lojavirtual.model.PessoaFisica;
import jdev.mentoria.lojavirtual.model.PessoaJuridica;

@Repository
public interface PessoaRepository extends CrudRepository<PessoaJuridica, Long> {

	@Query("select pj from PessoaJuridica pj where pj.cnpj = ?1")
	PessoaJuridica existeCnpjCadastrado(String cnpj);

	@Query("select pj from PessoaJuridica pj where pj.cnpj = ?1")
	List<PessoaJuridica> existeCnpjCadastradoList(String cnpj);

	@Query("select pj from PessoaJuridica pj where pj.inscEstadual = ?1")
	PessoaJuridica existeInscriEstadualCadastrado(String inscEstadual);

	@Query("select pj from PessoaJuridica pj where pj.inscEstadual = ?1")
	List<PessoaJuridica> existeInscriEstadualCadastradoList(String inscEstadual);

	@Query("select pf from PessoaFisica pf where pf.cpf = ?1")
	PessoaFisica existeCpfCadastrado(String cpf);

	@Query("select pf from PessoaFisica pf where pf.cpf = ?1")
	List<PessoaFisica> existeCpfCadastradoList(String cpf);

	@Query("""
		select pj
		from PessoaJuridica pj
		where upper(trim(pj.nome)) like concat('%', upper(trim(:nome)), '%')
	""")
	List<PessoaJuridica> pesquisaPorNomePJ(@Param("nome") String nome);
}
