package jdev.mentoria.lojavirtual.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jdev.mentoria.lojavirtual.model.PessoaFisica;

@Repository
public interface PessoaFisicaRepository extends CrudRepository<PessoaFisica, Long> {

	@Query("""
		select pf
		from PessoaFisica pf
		where upper(trim(pf.nome)) like concat('%', upper(trim(:nome)), '%')
	""")
	List<PessoaFisica> pesquisaPorNomePF(@Param("nome") String nome);

	@Query("select pf from PessoaFisica pf where pf.cpf = ?1")
	List<PessoaFisica> pesquisaPorCpfPF(String cpf);
}
