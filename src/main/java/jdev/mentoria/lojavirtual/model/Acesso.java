package jdev.mentoria.lojavirtual.model;

import java.util.Objects;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;





/**
 * Entidade responsável por representar um nível de acesso (permissão)
 * dentro do sistema de segurança.
 *
 * <p>Essa classe é utilizada pelo Spring Security para definir
 * quais permissões um usuário possui. Cada instância de {@code Acesso}
 * representa um papel ou autoridade, como por exemplo:
 * ROLE_ADMIN, ROLE_USER, ROLE_GERENTE.</p>
 *
 * <p>Ela implementa a interface {@link GrantedAuthority},
 * o que permite que o Spring Security utilize esse objeto
 * diretamente no processo de autenticação e autorização.</p>
 */
@Entity
@Table(name = "acesso")
@SequenceGenerator(
        name = "seq_acesso",
        sequenceName = "seq_acesso",
        allocationSize = 1,
        initialValue = 1
)
public class Acesso implements GrantedAuthority {

    private static final long serialVersionUID = 2109367091287926831L;

    
    
    
    
    
    
    /**
     * Identificador único do acesso.
     *
     * <p>É gerado automaticamente pelo banco de dados
     * através de uma sequência.</p>
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_acesso")
    private Long id;

        
    
    
    
    /**
     * Descrição do acesso.
     *
     * <p>Esse campo representa o nome da autoridade
     * utilizada pelo Spring Security, como:
     * ROLE_ADMIN, ROLE_USER, etc.</p>
     *
     * <p>É esse valor que será comparado nas regras
     * de segurança.</p>
     */
    @Column(nullable = false)
    private String descricao;

    
    
    
    
    
    
    
    
    /**
     * Método exigido pela interface {@link GrantedAuthority}.
     *
     * <p>O Spring Security chama esse método automaticamente
     * para obter o nome da autoridade associada ao usuário.</p>
     *
     * <p>O valor retornado aqui é usado internamente pelo
     * mecanismo de autorização para decidir se o usuário
     * tem ou não permissão para acessar um recurso.</p>
     *
     * @return String descrição da autoridade (ex: ROLE_ADMIN)
     */
    @JsonIgnore
    @Override
    public @Nullable String getAuthority() {
        return this.descricao;
    }

    
    
    
    
    
    
    
    /**
     * Retorna o ID do acesso.
     *
     * @return Long identificador do acesso.
     */
    public Long getId() {
        return id;
    }

    
    
    
    
    
    /**
     * Define o ID do acesso.
     *
     * @param id Identificador do acesso.
     */
    public void setId(Long id) {
        this.id = id;
    }

    
    
    
    
    
    
    
    
    /**
     * Retorna a descrição do acesso.
     *
     * @return String descrição da permissão.
     */
    public String getDescricao() {
        return descricao;
    }

    
    
    
    
    
    /**
     * Define a descrição do acesso.
     *
     * @param descricao Nome da permissão (ex: ROLE_USER).
     */
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    
    
    
    /**
     * Gera o hashCode com base no ID.
     *
     * <p>Isso garante o funcionamento correto da entidade
     * em coleções como Set e Map.</p>
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    
    /**
     * Compara dois objetos Acesso.
     *
     * <p>Dois acessos são considerados iguais se
     * possuírem o mesmo ID.</p>
     *
     * @param obj Objeto a ser comparado.
     * @return true se forem iguais, false caso contrário.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Acesso other = (Acesso) obj;
        return Objects.equals(id, other.id);
    }

    
    
    
    /*
     * ===================== EXPLICAÇÃO DIDÁTICA =====================
     *
     * Essa classe representa uma permissão do sistema.
     * No contexto do Spring Security, permissões são chamadas
     * de "authorities".
     *
     * Cada registro da tabela "acesso" define um papel que pode
     * ser atribuído a um usuário, como ADMIN, USER, GERENTE etc.
     *
     * Ao implementar a interface GrantedAuthority, essa entidade
     * passa a ser reconhecida automaticamente pelo Spring Security.
     * Durante o login, o Spring chama o método getAuthority()
     * para descobrir quais permissões o usuário possui.
     *
     * O campo "descricao" é o ponto mais importante da classe.
     * É ele que representa o nome real da permissão e é usado
     * diretamente nas regras de segurança, como:
     *
     *   hasRole("ADMIN")
     *   hasAuthority("ROLE_USER")
     *
     * Os métodos equals e hashCode garantem que o Hibernate
     * consiga comparar corretamente os objetos quando eles
     * estiverem em listas, sets ou no cache.
     *
     * Em resumo:
     * essa classe é a ponte entre o banco de dados
     * e o mecanismo de segurança do Spring.
     * Sem ela, o controle de acesso do sistema não funcionaria.
     * ===============================================================
     */

}
