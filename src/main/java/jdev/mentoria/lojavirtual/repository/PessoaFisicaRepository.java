package jdev.mentoria.lojavirtual.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import jdev.mentoria.lojavirtual.model.PessoaFisica;
import jdev.mentoria.lojavirtual.model.PessoaJuridica;

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
	
	
	@Query(nativeQuery = true, value = "SELECT CAST(CEIL(COUNT(1) / 5.0) AS INTEGER) FROM pessoa_fisica WHERE empresa_id = ?1")
	public Integer quantidadePaginaPF(Long idEmpresa);
		
	@Query("Select a from PessoaFisica a where upper(trim(a.nome)) like %?1% and a.empresa.id = ?2")
	List<PessoaFisica> buscarPessoaFisica(String nome, String empresa);
	
	
	@Query("Select a from PessoaFisica a where a.empresa.id = ?1")
	List<PessoaFisica> findAll(Long codEmp);
	
	
	@Query(value = "Select a from PessoaFisica a where a.empresa.id = ?1")
	public List<PessoaFisica> findbyPage(Long idEmpresa, Pageable pageable);
	
	@Modifying
	@Transactional
	@Query(
		    nativeQuery = true,
		    value = "delete from usuarios_acessos where usuario_id in (select id from usuario where pessoa_id = ?1)"
		)
		void deletaAcessoUserByPessoa(Long idPessoa);
	
	@Transactional
	@Modifying
	@Query(value = "delete from usuario where pessoa_id = ?1",nativeQuery = true)
	void deleteByPF(Long idEmpresa);

	
}
