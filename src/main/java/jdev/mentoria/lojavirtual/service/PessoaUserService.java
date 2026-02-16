package jdev.mentoria.lojavirtual.service;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import jdev.mentoria.lojavirtual.model.PessoaFisica;
import jdev.mentoria.lojavirtual.model.PessoaJuridica;
import jdev.mentoria.lojavirtual.model.Usuario;
import jdev.mentoria.lojavirtual.repository.PessoaFisicaRepository;
import jdev.mentoria.lojavirtual.repository.PessoaRepository;
import jdev.mentoria.lojavirtual.repository.UsuarioRepository;

/**
 * Serviço responsável por salvar {@link PessoaJuridica} e garantir que ela
 * tenha um usuário de acesso criado no sistema (quando necessário).
 *
 * <p>
 * Essa classe fica na camada de serviço, ou seja, ela concentra regras de
 * negócio. O controller normalmente chama este serviço para executar a lógica
 * completa de cadastro.
 * </p>
 *
 * <p>
 * Em termos simples: quando uma empresa (PessoaJuridica) é cadastrada, este
 * serviço: salva a empresa + ajusta os endereços + cria o usuário de login (se
 * ainda não existir) + define senha criptografada + adiciona o acesso padrão +
 * envia e-mail com login e senha.
 * </p>
 */
@Service
public class PessoaUserService {

	
	
	
	
	/**
	 * Repositório responsável pelas operações de banco relacionadas a
	 * {@link Usuario}.
	 */
	@Autowired
	private UsuarioRepository usuarioRepository;

	
	
	
	
	
	/**
	 * 
	 * Repositório responsável pelas operações de banco relacionadas a
	 * {@link PessoaJuridica}.
	 */
	@Autowired
	private PessoaRepository pessoaRepository;

	@Autowired
	private PessoaFisicaRepository pessoaFisicaRepository;
	
	
	
	/**
	 * Serviço responsável por enviar e-mail. Aqui é usado para enviar o login e a
	 * senha gerados para a empresa cadastrada.
	 */
	@Autowired
	private SendEmailService emailService;

	
	
	
	
	
	/**
	 * Salva uma {@link PessoaJuridica} no banco e cria um usuário de acesso caso
	 * ainda não exista.
	 *
	 * <p>
	 * Esse método faz mais do que apenas “salvar a empresa”. Ele garante também
	 * que:
	 * </p>
	 * <p>
	 * - cada endereço esteja associado à pessoa/empresa corretamente<br>
	 * - a empresa seja persistida no banco<br>
	 * - se não existir usuário para essa pessoa, um novo usuário seja criado<br>
	 * - uma senha temporária seja gerada e criptografada<br>
	 * - seja atribuído um acesso padrão (via método do repositório)<br>
	 * - seja enviado um e-mail com login e senha para o usuário
	 * </p>
	 *
	 * @param pessoaJuridica Empresa (Pessoa Jurídica) que será salva.
	 * @return A mesma {@link PessoaJuridica} já persistida (com ID gerado).
	 */
	public PessoaJuridica salvarPessoaJuridica(PessoaJuridica pessoaJuridica) {

		
		
		
		
		
		// ------------------------------------------------------------
		// 1) Ajusta os endereços para apontarem corretamente para a empresa
		// ------------------------------------------------------------

		// Percorre a lista de endereços da pessoa jurídica
		for (int i = 0; i < pessoaJuridica.getEnderecos().size(); i++) {

			// Faz o endereço “apontar” para a pessoa (dono do endereço)
			pessoaJuridica.getEnderecos().get(i).setPessoa(pessoaJuridica);

			// Faz o endereço “apontar” para a empresa (empresa associada ao endereço)
			pessoaJuridica.getEnderecos().get(i).setEmpresa(pessoaJuridica);
		}

		
		
		
		
		// ------------------------------------------------------------
		// 2) Salva a Pessoa Jurídica no banco
		// ------------------------------------------------------------

		// Persiste a empresa e garante que o ID seja gerado
		pessoaJuridica = pessoaRepository.save(pessoaJuridica);
		
		
		
		
		
		
		
		

		// ------------------------------------------------------------
		// 3) Verifica se já existe usuário para essa empresa
		// ------------------------------------------------------------

		// Procura um usuário vinculado à pessoa e ao e-mail (login)
		Usuario usuarioPJ = usuarioRepository.findUserByPessoa(pessoaJuridica.getId(), pessoaJuridica.getEmail());

		// Se não existir, cria um novo usuário com senha e acessos padrão
		if (usuarioPJ == null) {

			
			
			
			
			
			// ------------------------------------------------------------
			// 4) Cria o usuário de acesso da empresa
			// ------------------------------------------------------------

			usuarioPJ = new Usuario();

			// Data em que a senha foi “atualizada” (útil para controle de expiração)
			usuarioPJ.setDataAtualSenha(Calendar.getInstance().getTime());

			// Define empresa e pessoa associadas ao usuário
			usuarioPJ.setEmpresa(pessoaJuridica);
			usuarioPJ.setPessoa(pessoaJuridica);

			// Login será o e-mail da empresa
			usuarioPJ.setLogin(pessoaJuridica.getEmail());

			
			
			
			
			
			// ------------------------------------------------------------
			// 5) Gera uma senha temporária e criptografa
			// ------------------------------------------------------------

			// Gera uma senha simples baseada no tempo atual (milissegundos)
			String senha = "" + Calendar.getInstance().getTimeInMillis();

			// Criptografa a senha com BCrypt (o ideal para senhas)
			String senhaCrypt = new BCryptPasswordEncoder().encode(senha);

			// Salva a senha criptografada no usuário
			usuarioPJ.setSenha(senhaCrypt);

			
			
			
			
			
			// ------------------------------------------------------------
			// 6) Salva o usuário e atribui o acesso padrão
			// ------------------------------------------------------------

			// Persiste o usuário no banco
			usuarioPJ = usuarioRepository.save(usuarioPJ);

			
			
			// Insere o acesso padrão para o usuário PJ (provavelmente uma ROLE específica)
			usuarioRepository.insereAcessoUserPj(usuarioPJ.getId());
			
			System.out.println("DEBUG ROLE: antes de inserir ROLE_ADMIN");
			usuarioRepository.insereAcessoUserPj(usuarioPJ.getId(), "ROLE_ADMIN");
			System.out.println("DEBUG ROLE: depois de inserir ROLE_ADMIN");
			
			
			
			// ------------------------------------------------------------
			// 7) Monta e envia e-mail com login e senha
			// ------------------------------------------------------------

			StringBuilder mensagemHtml = new StringBuilder();

			mensagemHtml.append("<div style='font-family: Arial, sans-serif; font-size: 14px; color: #333;'>");

			mensagemHtml.append("<h2 style='color: #2c3e50;'>Acesso à Loja Virtual</h2>");

			mensagemHtml.append("<p>Segue abaixo seus dados de acesso:</p>");

			mensagemHtml.append("<p>").append("<b>Login:</b> ").append(pessoaJuridica.getEmail()).append("<br/>")
					.append("<b>Senha:</b> ").append(senha).append("</p>");

			mensagemHtml.append("<p style='margin-top:20px;'>")
					.append("Recomendamos que você altere sua senha após o primeiro acesso.").append("</p>");

			mensagemHtml.append("<p>Obrigado!<br/>Equipe Loja Virtual</p>");

			mensagemHtml.append("</div>");

			try {
				// Envia o e-mail em HTML para o e-mail da empresa
				emailService.enviarEmailHtml("Acesso gerado para loja virtual", mensagemHtml.toString(),
						pessoaJuridica.getEmail());
			} catch (Exception e) {
				// Se o e-mail falhar, imprime o erro, mas não impede o cadastro
				e.printStackTrace();
			}
		}

		
		
		
		// Retorna a pessoa jurídica salva (com ou sem criação do usuário)
		return pessoaJuridica;
	}






	public PessoaFisica salvarPessoaFisica(PessoaFisica pessoaFisica) {
		
				
		// ------------------------------------------------------------
		// 1) Ajusta os endereços para apontarem corretamente para a empresa
		// ------------------------------------------------------------

		// Percorre a lista de endereços da pessoa jurídica
		for (int i = 0; i < pessoaFisica.getEnderecos().size(); i++) {

			// Faz o endereço “apontar” para a pessoa (dono do endereço)
			pessoaFisica.getEnderecos().get(i).setPessoa(pessoaFisica);

			// Faz o endereço “apontar” para a empresa (empresa associada ao endereço)
			pessoaFisica.getEnderecos().get(i).setEmpresa(pessoaFisica);
		}

		
		
		
		
		// ------------------------------------------------------------
		// 2) Salva a Pessoa Jurídica no banco
		// ------------------------------------------------------------

		// Persiste a empresa e garante que o ID seja gerado
		pessoaFisica = pessoaFisicaRepository.save(pessoaFisica);
		
		
		
		
		
		
		
		

		// ------------------------------------------------------------
		// 3) Verifica se já existe usuário para essa empresa
		// ------------------------------------------------------------

		// Procura um usuário vinculado à pessoa e ao e-mail (login)
		Usuario usuarioPJ = usuarioRepository.findUserByPessoa(pessoaFisica.getId(), pessoaFisica.getEmail());

		// Se não existir, cria um novo usuário com senha e acessos padrão
		if (usuarioPJ == null) {

			
			
			
			
			
			// ------------------------------------------------------------
			// 4) Cria o usuário de acesso da empresa
			// ------------------------------------------------------------

			usuarioPJ = new Usuario();

			// Data em que a senha foi “atualizada” (útil para controle de expiração)
			usuarioPJ.setDataAtualSenha(Calendar.getInstance().getTime());

			// Define empresa e pessoa associadas ao usuário
			usuarioPJ.setEmpresa(pessoaFisica);
			usuarioPJ.setPessoa(pessoaFisica);

			// Login será o e-mail da empresa
			usuarioPJ.setLogin(pessoaFisica.getEmail());

			
			
			
			
			
			// ------------------------------------------------------------
			// 5) Gera uma senha temporária e criptografa
			// ------------------------------------------------------------

			// Gera uma senha simples baseada no tempo atual (milissegundos)
			String senha = "" + Calendar.getInstance().getTimeInMillis();

			// Criptografa a senha com BCrypt (o ideal para senhas)
			String senhaCrypt = new BCryptPasswordEncoder().encode(senha);

			// Salva a senha criptografada no usuário
			usuarioPJ.setSenha(senhaCrypt);

			
			
			
			
			
			// ------------------------------------------------------------
			// 6) Salva o usuário e atribui o acesso padrão
			// ------------------------------------------------------------

			// Persiste o usuário no banco
			usuarioPJ = usuarioRepository.save(usuarioPJ);

			
			
			// Insere o acesso padrão para o usuário PJ (provavelmente uma ROLE específica)
			usuarioRepository.insereAcessoUserPj(usuarioPJ.getId());
			
			
			
			
			// ------------------------------------------------------------
			// 7) Monta e envia e-mail com login e senha
			// ------------------------------------------------------------

			StringBuilder mensagemHtml = new StringBuilder();

			mensagemHtml.append("<div style='font-family: Arial, sans-serif; font-size: 14px; color: #333;'>");

			mensagemHtml.append("<h2 style='color: #2c3e50;'>Acesso à Loja Virtual</h2>");

			mensagemHtml.append("<p>Segue abaixo seus dados de acesso:</p>");

			mensagemHtml.append("<p>").append("<b>Login:</b> ").append(pessoaFisica.getEmail()).append("<br/>")
					.append("<b>Senha:</b> ").append(senha).append("</p>");

			mensagemHtml.append("<p style='margin-top:20px;'>")
					.append("Recomendamos que você altere sua senha após o primeiro acesso.").append("</p>");

			mensagemHtml.append("<p>Obrigado!<br/>Equipe Loja Virtual</p>");

			mensagemHtml.append("</div>");

			try {
				// Envia o e-mail em HTML para o e-mail da empresa
				emailService.enviarEmailHtml("Acesso gerado para loja virtual", mensagemHtml.toString(),
						pessoaFisica.getEmail());
			} catch (Exception e) {
				// Se o e-mail falhar, imprime o erro, mas não impede o cadastro
				e.printStackTrace();
			}
		}

		
		
		
		// Retorna a pessoa jurídica salva (com ou sem criação do usuário)
		return pessoaFisica;
	}

	
	
	
	
	
	
	
	
	
	/*
	 * ===================== EXPLICAÇÃO DIDÁTICA =====================
	 *
	 * Esse service faz o cadastro “completo” da empresa.
	 *
	 * Primeiro ele garante que os endereços estão corretos: se você não setar o
	 * “pessoa” e o “empresa” dentro do endereço, o Hibernate pode tentar salvar o
	 * endereço sem vínculo e dar erro, ou salvar de um jeito incompleto.
	 *
	 * Depois ele salva a PessoaJuridica e pega o ID gerado. Esse ID é importante
	 * porque, sem ele, você não consegue relacionar corretamente o usuário com a
	 * empresa.
	 *
	 * Aí vem a parte mais importante: criar o usuário de acesso. O método procura
	 * se já existe um usuário para essa pessoa e e-mail. Se não existir, cria: -
	 * login = e-mail da empresa - senha temporária gerada na hora - senha
	 * criptografada com BCrypt (isso é o que faz a senha ser segura) - salva no
	 * banco - coloca o acesso padrão (role) para esse usuário
	 *
	 * Por fim, ele envia um e-mail com login e senha. Repare que o sistema guarda a
	 * senha criptografada, mas manda para o usuário a senha original (não
	 * criptografada), porque só assim o usuário consegue fazer o primeiro login.
	 *
	 * Em resumo: esse método salva a empresa e “prepara o acesso dela ao sistema”
	 * criando automaticamente um usuário e liberando as permissões.
	 * ===============================================================
	 */
}
