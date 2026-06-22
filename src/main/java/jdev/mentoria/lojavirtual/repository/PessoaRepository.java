package jdev.mentoria.lojavirtual.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
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

	@Transactional
	@Modifying
	@Query(value = "delete from usuario where empresa_id = ?1",nativeQuery = true)
	void deleteByPj(Long idEmpresa);
	
	@Transactional
	@Modifying
	@Query(value = "delete from usuario where pessoa_id = ?1",nativeQuery = true)
	void deleteByPessoa(Long idEmpresa);

	@Query("""
				select pj
				from PessoaJuridica pj
				where upper(trim(pj.nome)) like concat('%', upper(trim(:nome)), '%')
			""")
	List<PessoaJuridica> pesquisaPorNomePJ(@Param("nome") String nome);

	@Query(value = "Select a from PessoaJuridica a where a.empresa.id = ?1")
	public List<PessoaJuridica> findbyPage(Long idEmpresa, Pageable pageable);

	@Query(nativeQuery = true, value = "SELECT CAST(CEIL(COUNT(1) / 5.0) AS INTEGER) FROM pessoa_juridica WHERE empresa_id = ?1")
	public Integer quantidadePagina(Long idEmpresa);

	
	
	@Query("Select a from PessoaJuridica a where upper(trim(a.nomeFantasia)) like %?1% and a.empresa.id = ?2")
	List<PessoaJuridica> buscarEmpresaNomeFantasia(String nomeFantasia, String empresa);
	
	

	
	@Query("Select a from PessoaJuridica a where a.empresa.id = ?1")
	List<PessoaJuridica> findAll(Long codEmp);
	
	@Query("Select a from PessoaJuridica a where a.id = ?1")
	PessoaJuridica buscarEmpresaId(Long id);

	@Modifying
	@Transactional
	@Query(nativeQuery = true, value = "delete from usuarios_acessos where usuario_id in (select id from usuario where pessoa_id = ?1)")
	void deletaAcessoUserByPessoa(Long idPessoa);

	@Transactional
	@Modifying
	@Query(value = "delete from endereco where id = ?1",nativeQuery = true)
	void deleteEndById(Long idEnd);
	
	
	
}
