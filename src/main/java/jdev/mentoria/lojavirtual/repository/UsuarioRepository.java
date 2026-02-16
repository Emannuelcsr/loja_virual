package jdev.mentoria.lojavirtual.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import jdev.mentoria.lojavirtual.model.Usuario;

@Repository
public interface UsuarioRepository extends CrudRepository<Usuario, Long> {

	@Query("select u from Usuario u where u.login = ?1")
	Usuario findUserByLogin(String login);

	@Query("select u from Usuario u where u.pessoa.id = ?1 or u.login = ?2")
	Usuario findUserByPessoa(Long id, String email);

	@jakarta.transaction.Transactional
	@Modifying
	@Query(nativeQuery = true, value = "insert into usuarios_acessos(usuario_id,acesso_id) values (?1,(select id from acesso where descricao = 'ROLE_USER' ))")
	void insereAcessoUserPj(Long iduser);

	@jakarta.transaction.Transactional
	@Modifying
	@Query(nativeQuery = true, value = "insert into usuarios_acessos(usuario_id,acesso_id) values (?1,(select id from acesso where descricao = ?2 ))")
	void insereAcessoUserPj(Long iduser, String acesso);

	@Query(value = "select * from usuario u where u.data_atual_senha <= (current_date - interval '90 day')", nativeQuery = true)
	List<Usuario> usuarioSenhaVencida();

}
