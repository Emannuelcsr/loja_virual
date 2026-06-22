package jdev.mentoria.lojavirtual.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import jdev.mentoria.lojavirtual.model.Acesso;


@Repository
@Transactional
public interface AcessoRepository extends JpaRepository<Acesso, Long> {
	
	@Query("Select a from Acesso a where upper(trim(a.descricao)) like %?1%")
	List<Acesso> buscarAcessoDesc(String desc);

	@Query(value = "Select a from Acesso a where a.empresa.id = ?1")
	public List<Acesso> findbyPage(Long idEmpresa, Pageable pageable);
	
	@Query(
		    nativeQuery = true,
		    value = "SELECT CAST(CEIL(COUNT(1) / 5.0) AS INTEGER) FROM acesso WHERE empresa_id = ?1"
		)
		public Integer quantidadePagina(Long idEmpresa);
	
	
	@Query("Select a from Acesso a where upper(trim(a.descricao)) like %?1% and a.empresa.id = ?2")
	List<Acesso> buscarCategoriaDesc(String nomeDesc, String empresa);
	
	
	@Query("Select a from Acesso a where a.empresa.id = ?1")
	List<Acesso> findAll(Long codEmp);
	
	@Query(value = "Select a from Acesso a where a.empresa.id = ?1")
	public List<Acesso> findAcessos(Long idEmpresa);
	@Query(
		    nativeQuery = true,
		    value = "select count(1) > 0 from usuarios_acessos where usuario_id = ?1 and acesso_id = ?2"
		)
		Boolean possuiAcesso(Long idUser, Long idAcesso);
}
