package jdev.mentoria.lojavirtual.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "acess_token_api")
@SequenceGenerator(name = "acess_token_api", sequenceName = "seq_acess_token_api", allocationSize = 1, initialValue = 1)
public class AcessTokenJunoAPI implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_acess_token_api")
	private Long id;

	@Column(columnDefinition = "text")
	private String acess_token; 
	
	private String token_type; 
	
	private String expires_in; 

	private String scope; 

	private String user_name; 

	private String jti; 
	
	@Column(updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataCadastro = Calendar.getInstance().getTime();

	private String token_acesso;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAcess_token() {
		return acess_token;
	}

	public void setAcess_token(String acess_token) {
		this.acess_token = acess_token;
	}

	public String getToken_type() {
		return token_type;
	}

	public void setToken_type(String token_type) {
		this.token_type = token_type;
	}

	public String getExpires_in() {
		return expires_in;
	}

	public void setExpires_in(String expires_in) {
		this.expires_in = expires_in;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getJti() {
		return jti;
	}

	public void setJti(String jti) {
		this.jti = jti;
	}

	public Date getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public String getToken_acesso() {
		return token_acesso;
	}

	public void setToken_acesso(String token_acesso) {
		this.token_acesso = token_acesso;
	}
	
	
	
	/**
	 * Verifica se o token de acesso já ultrapassou o tempo limite de uso.
	 *
	 * <p>Este método calcula a diferença de tempo entre a data atual do sistema
	 * e a data em que o token foi criado ({@link #dataCadastro}). Essa diferença
	 * é convertida para minutos para facilitar a comparação.</p>
	 *
	 * <p>Embora o token tenha validade aproximada de 1 hora, foi definida uma
	 * margem de segurança de 50 minutos. Isso evita que o sistema tente utilizar
	 * um token que esteja prestes a expirar durante uma requisição externa,
	 * reduzindo falhas de autenticação em integrações com APIs.</p>
	 *
	 * <p>Fluxo do cálculo:</p>
	 * <ul>
	 *   <li>Obtém a data e hora atual do sistema.</li>
	 *   <li>Calcula a diferença em milissegundos entre a data atual e {@link #dataCadastro}.</li>
	 *   <li>Converte essa diferença para minutos.</li>
	 *   <li>Se o tempo for maior que 50 minutos, o token é considerado expirado.</li>
	 * </ul>
	 *
	 * @return {@code true} se o token já ultrapassou o tempo limite definido
	 *         (mais de 50 minutos desde a criação), ou {@code false} caso ainda
	 *         esteja dentro do período válido.
	 */
	public boolean expirado() {

	    Date dataAtual = Calendar.getInstance().getTime();

	    Long tempo = dataAtual.getTime() - this.dataCadastro.getTime();

	    Long minutos = (tempo / 1000) / 60;

	    if (minutos.intValue() > 50) {
	        return true;
	    }

	    return false;
	}
	
}
