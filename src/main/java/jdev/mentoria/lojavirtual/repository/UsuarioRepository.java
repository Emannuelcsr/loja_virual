package jdev.mentoria.lojavirtual.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jdev.mentoria.lojavirtual.model.Usuario;

@Repository
public interface UsuarioRepository extends CrudRepository<Usuario, Long> {

	@Query("select u from Usuario u where u.login = ?1")
	Usuario findUserByLogin(String login);

	@Query("select u from Usuario u where u.pessoa.id = ?1 and u.login = ?2")
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

    @org.springframework.transaction.annotation.Transactional
    @Modifying(flushAutomatically = true,clearAutomatically = true)
    @Query(value = "update usuario set senha = ?1 where login = ?2", nativeQuery = true)
    void updateSenha(String senha, String login);
    
    
    @org.springframework.transaction.annotation.Transactional
    @Modifying(flushAutomatically = true,clearAutomatically = true)
    @Query(value = "update usuario set senha = ?1 where id = ?2", nativeQuery = true)
    void updateSenha2(String senha, Long idUser);
    
    @Query(value = "select u from Usuario u where u.empresa.id = ?1")
    List<Usuario> listUserByEmpresa(Long idEmpresa);
	
    
    @org.springframework.transaction.annotation.Transactional
    @Modifying(flushAutomatically = true,clearAutomatically = true)
    @Query(value = "update usuario set login = ?1 where id = ?2", nativeQuery = true)
    void updateLogin(String login, Long idUser);
    
    @Query(value = "select count(1)>0 from Usuario where senha = ?1 and id = ?2")
    boolean senhaIgual(String senha,Long idUser);
    
    
  

    @org.springframework.transaction.annotation.Transactional
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(
        value = "delete from usuarios_acessos where acesso_id = :idAcesso and usuario_id = :idUser",
        nativeQuery = true
    )
    void deleteByAcesso(@Param("idAcesso") Long idAcesso, @Param("idUser") Long idUser);


    @org.springframework.transaction.annotation.Transactional
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(
        value = "insert into usuarios_acessos(usuario_id, acesso_id) values (:idUser, :idAcesso)",
        nativeQuery = true
    )
    void addAcesso(@Param("idAcesso") Long idAcesso, @Param("idUser") Long idUser);
    
    
    
}
