package jdev.mentoria.lojavirtual.controller;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import jdev.mentoria.lojavirtual.ExcepetionLojaVirtual;
import jdev.mentoria.lojavirtual.enums.TipoPessoa;
import jdev.mentoria.lojavirtual.model.Endereco;
import jdev.mentoria.lojavirtual.model.PessoaFisica;
import jdev.mentoria.lojavirtual.model.PessoaJuridica;
import jdev.mentoria.lojavirtual.model.Usuario;
import jdev.mentoria.lojavirtual.model.dto.CepDto;
import jdev.mentoria.lojavirtual.model.dto.ConsultaCnpjDto;
import jdev.mentoria.lojavirtual.repository.EnderecoRepository;
import jdev.mentoria.lojavirtual.repository.PessoaFisicaRepository;
import jdev.mentoria.lojavirtual.repository.PessoaRepository;
import jdev.mentoria.lojavirtual.repository.UsuarioRepository;
import jdev.mentoria.lojavirtual.service.EmailMarketingService;
import jdev.mentoria.lojavirtual.service.PessoaUserService;
import jdev.mentoria.lojavirtual.service.SendEmailService;
import jdev.mentoria.lojavirtual.service.ServiceContagemApi;
import jdev.mentoria.lojavirtual.util.ValidadorCNPJ;
import jdev.mentoria.lojavirtual.util.ValidadorCPF;

/**
 * Controller responsável por expor endpoints relacionados ao cadastro de Pessoa
 * Jurídica (empresa).
 *
 * <p>
 * Essa classe recebe requisições HTTP, valida regras básicas e delega a lógica
 * principal para a camada de serviço.
 * </p>
 *
 * <p>
 * Em termos simples: ela é a porta de entrada para cadastrar empresas na loja
 * virtual.
 * </p>
 */
@RestController
public class PessoaController {

    private final CupomDescontoController cupomDescontoController;

	private final ContaPagarController contaPagarController;

	private final EmailMarketingService emailMarketingService;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private ServiceContagemApi serviceContagemApi;

	@Autowired
	private SendEmailService emailService;

	/**
	 * Repositório responsável por consultas diretas no banco relacionadas à
	 * entidade PessoaJuridica.
	 */
	@Autowired
	private PessoaRepository pessoaRepository;

	/**
	 * Serviço que contém a regra de negócio para salvar Pessoa Jurídica e criar
	 * usuário automaticamente.
	 */
	@Autowired
	private PessoaUserService pessoaUserService;

	@Autowired
	private EnderecoRepository enderecoRepository;

	@Autowired
	private PessoaFisicaRepository pessoaFisicaRepository;

	PessoaController(UsuarioRepository usuarioRepository, EmailMarketingService emailMarketingService,
			ContaPagarController contaPagarController, CupomDescontoController cupomDescontoController) {
		this.usuarioRepository = usuarioRepository;
		this.emailMarketingService = emailMarketingService;
		this.contaPagarController = contaPagarController;
		this.cupomDescontoController = cupomDescontoController;
	}

	@GetMapping(value = "/consultaPFporNome/{nome}")
	public ResponseEntity<List<PessoaFisica>> consultaPFporNome(@PathVariable("nome") String nome) {

		List<PessoaFisica> pessoaFisicas = pessoaFisicaRepository.pesquisaPorNomePF(nome.trim().toUpperCase());

		serviceContagemApi.atualizaAcessoEndPointPF();

		return new ResponseEntity<List<PessoaFisica>>(pessoaFisicas, HttpStatus.OK);

	}

	@GetMapping(value = "/consultaPFporCpf/{cpf}")
	public ResponseEntity<List<PessoaFisica>> consultaPFporCpf(@PathVariable("cpf") String cpf) {

		List<PessoaFisica> pessoaFisicas = pessoaFisicaRepository.pesquisaPorCpfPF(cpf);

		return new ResponseEntity<List<PessoaFisica>>(pessoaFisicas, HttpStatus.OK);

	}

	@GetMapping(value = "/consultaPJporNome/{nome}")
	public ResponseEntity<List<PessoaJuridica>> consultaPJporNome(@PathVariable("nome") String nome) {

		List<PessoaJuridica> pessoaJuridicas = pessoaRepository.pesquisaPorNomePJ(nome.trim().toUpperCase());

		return new ResponseEntity<List<PessoaJuridica>>(pessoaJuridicas, HttpStatus.OK);

	}

	@GetMapping(value = "/consultaPJporCnpj/{cnpj}")
	public ResponseEntity<List<PessoaJuridica>> consultaPJporCnpj(@PathVariable("cnpj") String cnpj) {

		List<PessoaJuridica> pessoaJuridicas = pessoaRepository.existeCnpjCadastradoList(cnpj);

		return new ResponseEntity<List<PessoaJuridica>>(pessoaJuridicas, HttpStatus.OK);

	}

	/**
	 * Endpoint responsável por salvar uma Pessoa Jurídica.
	 *
	 * <p>
	 * Fluxo executado:
	 * </p>
	 * <p>
	 * - valida se o objeto não é nulo<br>
	 * - valida se já existe CNPJ cadastrado (em caso de novo registro)<br>
	 * - delega a lógica de salvamento para o service<br>
	 * - retorna a empresa salva
	 * </p>
	 *
	 * @param pessoaJuridica Objeto recebido no corpo da requisição (JSON).
	 * @return ResponseEntity contendo a Pessoa Jurídica salva.
	 *
	 * @throws ExcepetionLojaVirtual caso alguma regra de negócio seja violada.
	 */
	@PostMapping(value = "/salvarPJ")
	public ResponseEntity<PessoaJuridica> salvarPJ(@RequestBody @Valid PessoaJuridica pessoaJuridica)
			throws ExcepetionLojaVirtual {

		// ------------------------------------------------------------
		// 1) Validação básica: objeto não pode ser nulo
		// ------------------------------------------------------------
		if (pessoaJuridica == null) {
			throw new ExcepetionLojaVirtual("Pessoa juridica não pode ser NULL");
		}

		if (pessoaJuridica.getTipoPessoa() == null) {

			throw new ExcepetionLojaVirtual("Informe o tipo: Juridico ou Fornecedor");
		}

		// ------------------------------------------------------------
		// 2) Validação de CNPJ duplicado (apenas para novo cadastro)
		// ------------------------------------------------------------
		if (pessoaJuridica.getId() == null && pessoaRepository.existeCnpjCadastrado(pessoaJuridica.getCnpj()) != null) {

			throw new ExcepetionLojaVirtual("Ja existe CNPJ cadastrado com o numero: " + pessoaJuridica.getCnpj());
		}

		if (pessoaJuridica.getId() == null
				&& pessoaRepository.existeInscriEstadualCadastrado(pessoaJuridica.getInscEstadual()) != null) {

			throw new ExcepetionLojaVirtual(
					"Ja existe Inscrição estadual cadastrado com o numero: " + pessoaJuridica.getInscEstadual());
		}

		if (!ValidadorCNPJ.validar(pessoaJuridica.getCnpj())) {
			throw new ExcepetionLojaVirtual("CNPJ : " + pessoaJuridica.getCnpj() + "não válido");
		}

		if (pessoaJuridica.getId() == null) {

			for (int p = 0; p < pessoaJuridica.getEnderecos().size(); p++) {

				String cep = pessoaJuridica.getEnderecos().get(p).getCep();
				
				CepDto cepDto = pessoaUserService.consultaCep(pessoaJuridica.getEnderecos().get(p).getCep());
				
				if(cepDto == null || (cepDto !=null && cepDto.getCep() == null)) {
					
					throw new ExcepetionLojaVirtual("CEP " +cep+" esta inválido.");
				}
				
				pessoaJuridica.getEnderecos().get(p).setBairro(cepDto.getBairro());
				pessoaJuridica.getEnderecos().get(p).setCidade(cepDto.getLocalidade());
				pessoaJuridica.getEnderecos().get(p).setComplemtento(cepDto.getComplemento());
				pessoaJuridica.getEnderecos().get(p).setRuaLogra(cepDto.getLogradouro());
				pessoaJuridica.getEnderecos().get(p).setUf(cepDto.getUf());

			}

		} else {
			for (int p = 0; p < pessoaJuridica.getEnderecos().size(); p++) {

				
				Long cepId =pessoaJuridica.getEnderecos().get(p).getId();
				
				if(cepId !=null) {
				
				Endereco enderecoTemp = enderecoRepository.findById(pessoaJuridica.getEnderecos().get(p).getId()).get();

					if (!enderecoTemp.getCep().equals(pessoaJuridica.getEnderecos().get(p).getCep())) {
	
						CepDto cepDto = pessoaUserService.consultaCep(pessoaJuridica.getEnderecos().get(p).getCep());
	
						pessoaJuridica.getEnderecos().get(p).setBairro(cepDto.getBairro());
						pessoaJuridica.getEnderecos().get(p).setCidade(cepDto.getLocalidade());
						pessoaJuridica.getEnderecos().get(p).setComplemtento(cepDto.getComplemento());
						pessoaJuridica.getEnderecos().get(p).setRuaLogra(cepDto.getLogradouro());
						pessoaJuridica.getEnderecos().get(p).setUf(cepDto.getUf());

					}

				}else {
					
					CepDto cepDto = pessoaUserService.consultaCep(pessoaJuridica.getEnderecos().get(p).getCep());
					
					pessoaJuridica.getEnderecos().get(p).setBairro(cepDto.getBairro());
					pessoaJuridica.getEnderecos().get(p).setCidade(cepDto.getLocalidade());
					pessoaJuridica.getEnderecos().get(p).setComplemtento(cepDto.getComplemento());
					pessoaJuridica.getEnderecos().get(p).setRuaLogra(cepDto.getLogradouro());
					pessoaJuridica.getEnderecos().get(p).setUf(cepDto.getUf());
					
				}
			}

		}

		// ------------------------------------------------------------
		// 3) Delegação da regra principal para a camada de serviço
		// ------------------------------------------------------------
		pessoaJuridica = pessoaUserService.salvarPessoaJuridica(pessoaJuridica);

		// ------------------------------------------------------------
		// 4) Retorno da resposta HTTP com status 200 (OK)
		// ------------------------------------------------------------
		return new ResponseEntity<>(pessoaJuridica, HttpStatus.OK);
	}

	/**
	 * Endpoint responsável por salvar uma Pessoa Jurídica.
	 *
	 * <p>
	 * Fluxo executado:
	 * </p>
	 * <p>
	 * - valida se o objeto não é nulo<br>
	 * - valida se já existe CNPJ cadastrado (em caso de novo registro)<br>
	 * - delega a lógica de salvamento para o service<br>
	 * - retorna a empresa salva
	 * </p>
	 *
	 * @param pessoaJuridica Objeto recebido no corpo da requisição (JSON).
	 * @return ResponseEntity contendo a Pessoa Jurídica salva.
	 *
	 * @throws ExcepetionLojaVirtual caso alguma regra de negócio seja violada.
	 */
	@PostMapping(value = "/salvarpf")
	public ResponseEntity<PessoaFisica> salvarPF(@RequestBody PessoaFisica pessoaFisica) throws ExcepetionLojaVirtual {

		// ------------------------------------------------------------
		// 1) Validação básica: objeto não pode ser nulo
		// ------------------------------------------------------------
		if (pessoaFisica == null) {
			throw new ExcepetionLojaVirtual("Pessoa fisica não pode ser NULL");
		}

		if (pessoaFisica.getTipoPessoa() == null) {

			pessoaFisica.setTipoPessoa(TipoPessoa.FISICA.name());
		}

		// ------------------------------------------------------------
		// 2) Validação de CPF duplicado (apenas para novo cadastro)
		// ------------------------------------------------------------
		if (pessoaFisica.getId() == null && !pessoaFisicaRepository.pesquisaPorCpfPF(pessoaFisica.getCpf()).isEmpty()) {

			throw new ExcepetionLojaVirtual("Ja existe CPF cadastrado com o numero: " + pessoaFisica.getCpf());
		}

		if (!ValidadorCPF.validar(pessoaFisica.getCpf())) {
			throw new ExcepetionLojaVirtual("CNPJ : " + pessoaFisica.getCpf() + "não válido");
		}

		
		
		
		if (pessoaFisica.getId() == null) {

			for (int p = 0; p < pessoaFisica.getEnderecos().size(); p++) {

				String cep = pessoaFisica.getEnderecos().get(p).getCep();
				
				CepDto cepDto = pessoaUserService.consultaCep(pessoaFisica.getEnderecos().get(p).getCep());
				
				if(cepDto == null || (cepDto !=null && cepDto.getCep() == null)) {
					
					throw new ExcepetionLojaVirtual("CEP " +cep+" esta inválido.");
				}
				
				pessoaFisica.getEnderecos().get(p).setBairro(cepDto.getBairro());
				pessoaFisica.getEnderecos().get(p).setCidade(cepDto.getLocalidade());
				pessoaFisica.getEnderecos().get(p).setComplemtento(cepDto.getComplemento());
				pessoaFisica.getEnderecos().get(p).setRuaLogra(cepDto.getLogradouro());
				pessoaFisica.getEnderecos().get(p).setUf(cepDto.getUf());
			}

		} else {
			for (int p = 0; p < pessoaFisica.getEnderecos().size(); p++) {

				
				Long cepId =pessoaFisica.getEnderecos().get(p).getId();
				
				if(cepId !=null) {
				
				Endereco enderecoTemp = enderecoRepository.findById(pessoaFisica.getEnderecos().get(p).getId()).get();

					if (!enderecoTemp.getCep().equals(pessoaFisica.getEnderecos().get(p).getCep())) {
	
						CepDto cepDto = pessoaUserService.consultaCep(pessoaFisica.getEnderecos().get(p).getCep());
	
						pessoaFisica.getEnderecos().get(p).setBairro(cepDto.getBairro());
						pessoaFisica.getEnderecos().get(p).setCidade(cepDto.getLocalidade());
						pessoaFisica.getEnderecos().get(p).setComplemtento(cepDto.getComplemento());
						pessoaFisica.getEnderecos().get(p).setRuaLogra(cepDto.getLogradouro());
						pessoaFisica.getEnderecos().get(p).setUf(cepDto.getUf());

					}

				}else {
					
					CepDto cepDto = pessoaUserService.consultaCep(pessoaFisica.getEnderecos().get(p).getCep());
					
					pessoaFisica.getEnderecos().get(p).setBairro(cepDto.getBairro());
					pessoaFisica.getEnderecos().get(p).setCidade(cepDto.getLocalidade());
					pessoaFisica.getEnderecos().get(p).setComplemtento(cepDto.getComplemento());
					pessoaFisica.getEnderecos().get(p).setRuaLogra(cepDto.getLogradouro());
					pessoaFisica.getEnderecos().get(p).setUf(cepDto.getUf());
					
				}
			}

		}
		
		
		// ------------------------------------------------------------
		// 3) Delegação da regra principal para a camada de serviço
		// ------------------------------------------------------------
		pessoaFisica = pessoaUserService.salvarPessoaFisica(pessoaFisica);

		// ------------------------------------------------------------
		// 4) Retorno da resposta HTTP com status 200 (OK)
		// ------------------------------------------------------------
		return new ResponseEntity<>(pessoaFisica, HttpStatus.OK);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	

	@GetMapping(value = "/consultacep/{cep}")
	public ResponseEntity<CepDto> consultaCep(@PathVariable("cep") String cep) {

		CepDto cepDto = pessoaUserService.consultaCep(cep);

		return new ResponseEntity<CepDto>(cepDto, HttpStatus.OK);
	}

	@GetMapping(value = "/consultacnpj/{cnpj}")
	public ResponseEntity<ConsultaCnpjDto> consultaCnpj(@PathVariable("cnpj") String cnpj) {

		ConsultaCnpjDto consultaCnpjDto = pessoaUserService.consultaCnpj(cnpj);

		return new ResponseEntity<ConsultaCnpjDto>(consultaCnpjDto, HttpStatus.OK);
	}

	@PostMapping(value = "/recuperarSenha")
	public ResponseEntity<String> recuperarAcesso(@RequestBody String login)
			throws UnsupportedEncodingException, MessagingException {

		System.out.println("faasf");

		Usuario usuario = usuarioRepository.findUserByLogin(login);

		if (usuario == null) {
			return new ResponseEntity<String>("Usuario não existe", HttpStatus.BAD_REQUEST);
		}

		String senha = UUID.randomUUID().toString();// gera numeros randomicos q nunca se repetem/

		senha = senha.substring(0, 6);// pega os numeros randomicos gerados e só deixa os 6 primeiros

		String senhaCriptografada = new BCryptPasswordEncoder().encode(senha);// criptografa a senha de 6 digitos

		usuarioRepository.updateSenha(senhaCriptografada, login);

		StringBuilder msgEmail = new StringBuilder();
		msgEmail.append("<html>");
		msgEmail.append("<body style='font-family: Arial, sans-serif;'>");

		msgEmail.append("<h2>Recuperação de senha</h2>");

		msgEmail.append("<p>Olá,</p>");

		msgEmail.append("<p>Recebemos uma solicitação para recuperação de senha da sua conta.</p>");

		msgEmail.append("<p>Sua nova senha é:</p>");

		msgEmail.append("<h3 style='color: #0d6efd;'>").append(senha).append("</h3>");

		msgEmail.append("<p>Recomendamos que você altere essa senha após acessar o sistema.</p>");

		msgEmail.append("<br>");

		msgEmail.append("<p>Atenciosamente,</p>");
		msgEmail.append("<p><strong>Equipe Loja Virtual</strong></p>");

		msgEmail.append("</body>");
		msgEmail.append("</html>");

		emailService.enviarEmailHtml("Nova senha", msgEmail.toString(), usuario.getPessoa().getEmail());

		return new ResponseEntity<String>("Senha enviada para seu email", HttpStatus.OK);
	}

	// Endpoint chamado pelo Angular para verificar se o usuário possui acesso.
	@GetMapping(value = "/possuiacesso/{username}/{roleBackEnd}")
	public ResponseEntity<Boolean> possuAcesso(
			// Recebe o username enviado pelo Angular pela URL.
			@PathVariable("username") String username,

			// Recebe as roles permitidas para a tela.
			// Exemplo vindo do Angular:
			// ROLE_ADMIN,ROLE_USER,ROLE_FUNCIONARIO
			@PathVariable("roleBackEnd") String roleBackEnd) {

		// Transforma a String recebida:
		// ROLE_ADMIN,ROLE_USER,ROLE_FUNCIONARIO
		//
		// Em:
		// 'ROLE_ADMIN','ROLE_USER','ROLE_FUNCIONARIO'
		//
		// Esse formato será usado dentro do IN do SQL.
		String sqlRole = "'" + roleBackEnd.replaceAll(",", "','") + "'";

		Boolean possuiAcesso = pessoaUserService.possuiAcesso(username, sqlRole);

		// Retorna para o Angular o resultado real da consulta.
		//
		// Se possuiAcesso for true, o Angular pode liberar a rota.
		// Se for false, o Angular deve bloquear a rota.
		return new ResponseEntity<Boolean>(possuiAcesso, HttpStatus.OK);
	}

	@GetMapping(value = "/quantidadeDeEmpresas/{codEmp}")
	public ResponseEntity<Long> quantidadeDeEmpresas(@PathVariable("codEmp")Long codEmp) {

		long quantidadeTotal = pessoaRepository.findAll(codEmp).size();

		return new ResponseEntity<Long>(quantidadeTotal, HttpStatus.OK);
	}
	
	
	@GetMapping(value = "/quantidadeDePessoasNaEmpresa/{codEmp}") //angular pf TOTAL
	public ResponseEntity<Long> quantidadeDePessoas(@PathVariable("codEmp")Long codEmp) {

		long quantidadeTotal = pessoaFisicaRepository.findAll(codEmp).size();

		return new ResponseEntity<Long>(quantidadeTotal, HttpStatus.OK);
	}
	
	
	

	@GetMapping(value = "/listaPorPageEmpresas/{codEmp}/{pagina}")
	public ResponseEntity<List<PessoaJuridica>> listaPorPageEmpresas(@PathVariable("codEmp") Long codEmp,
			@PathVariable("pagina") Integer pagina) {

		org.springframework.data.domain.Pageable pageable = PageRequest.of(pagina - 1, 5, Sort.by("nomeFantasia"));

		List<PessoaJuridica> lista = pessoaRepository.findbyPage(codEmp, pageable);

		return new ResponseEntity<List<PessoaJuridica>>(lista, HttpStatus.OK);
	}
	
	
	
	@GetMapping(value = "/listaPorPagePF/{codEmp}/{pagina}")
	public ResponseEntity<List<PessoaFisica>> listaPorPagePF(@PathVariable("codEmp") Long codEmp,
			@PathVariable("pagina") Integer pagina) {

		org.springframework.data.domain.Pageable pageable = PageRequest.of(pagina - 1, 5, Sort.by("nome"));

		List<PessoaFisica> lista = pessoaFisicaRepository.findbyPage(codEmp, pageable);

		return new ResponseEntity<List<PessoaFisica>>(lista, HttpStatus.OK);
	}

	
	

	
	@GetMapping(value = "/qtdadePaginaEmpresas/{codEmp}")
	public ResponseEntity<Map<String, Integer>> qtdadePaginaEmpresas(@PathVariable("codEmp") Long codEmp) {

		Integer qtdadePagina = pessoaRepository.quantidadePagina(codEmp);

		Map<String, Integer> resposta = new HashMap<>();

		resposta.put("resposta", qtdadePagina);

		return new ResponseEntity<Map<String, Integer>>(resposta, HttpStatus.OK);
	}

	
	
	@GetMapping(value = "/qtdadePaginaEmpresasPf/{codEmp}")//Angular PF 
	public ResponseEntity<Map<String, Integer>> qtdadePaginaEmpresasPf(@PathVariable("codEmp") Long codEmp) {

		Integer qtdadePagina = pessoaFisicaRepository.quantidadePaginaPF(codEmp);

		Map<String, Integer> resposta = new HashMap<>();

		resposta.put("resposta", qtdadePagina);

		return new ResponseEntity<Map<String, Integer>>(resposta, HttpStatus.OK);
	}

	
	
	@GetMapping(value = "/buscarPorPessoaJuridica/{desc}/{empresa}")
	public ResponseEntity<Map<String, Object>> buscarPorPessoaJuridica(@PathVariable("desc") String desc,
			@PathVariable("empresa") String empresa) {

		List<PessoaJuridica> empresas = pessoaRepository.buscarEmpresaNomeFantasia(desc.toUpperCase(), empresa);

		Map<String, Object> resposta = new HashMap<>();

		resposta.put("resposta", empresas);

		return new ResponseEntity<Map<String, Object>>(resposta, HttpStatus.OK);
	}
	
	
	@GetMapping(value = "/buscarPessoaFisica/{desc}/{empresa}")
	public ResponseEntity<Map<String, Object>> buscarPessoaFisica(@PathVariable("desc") String desc,
			@PathVariable("empresa") String empresa) {

		List<PessoaFisica> empresas = pessoaFisicaRepository.buscarPessoaFisica(desc.toUpperCase(), empresa);

		Map<String, Object> resposta = new HashMap<>();

		resposta.put("resposta", empresas);

		return new ResponseEntity<Map<String, Object>>(resposta, HttpStatus.OK);
	}
	

	@GetMapping(value = "/buscarPorEmpresaId/{id}")
	public ResponseEntity<Map<String, Object>> buscarPorEmpresa(@PathVariable("id") Long id) {

		PessoaJuridica empresa = pessoaRepository.buscarEmpresaId(id);

		Map<String, Object> resposta = new HashMap<>();

		if (empresa == null) {
			resposta.put("mensagem", "Empresa não encontrada");
			return new ResponseEntity<Map<String, Object>>(resposta, HttpStatus.NOT_FOUND);
		}

		resposta.put("resposta", empresa);

		return new ResponseEntity<Map<String, Object>>(resposta, HttpStatus.OK);
	}

	@ResponseBody // Ele pega o objeto Java que seu método retorna e transforma em JSON pra mandar
	// de volta pra tela.
	@PostMapping(value = "/deleteEmpresa")
	public ResponseEntity<?> deleteEmpresa(@RequestBody PessoaJuridica empresa) {// requestBody transforma JSON da tela em objeto

		pessoaRepository.deletaAcessoUserByPessoa(empresa.getId());
		pessoaRepository.deleteByPj(empresa.getId());
		pessoaRepository.deleteById(empresa.getId());
		
		

		return new ResponseEntity("Cadastro Removido", HttpStatus.OK);
	}
	
	
	@ResponseBody // Ele pega o objeto Java que seu método retorna e transforma em JSON pra mandar
	// de volta pra tela.
	@PostMapping(value = "/deletePessoaFisica")
	public ResponseEntity<?> deletePessoaFisica(@RequestBody PessoaFisica pessoafisica) {// requestBody transforma JSON da tela em objeto

		pessoaFisicaRepository.deletaAcessoUserByPessoa(pessoafisica.getId());
		pessoaFisicaRepository.deleteByPF(pessoafisica.getId());
		pessoaFisicaRepository.deleteById(pessoafisica.getId());
		
		

		return new ResponseEntity("Cadastro Removido", HttpStatus.OK);
	}
	
	
	

	

	
	@GetMapping(value = "/obterEmpresa/{id}")// na vdd é pra editar
	public ResponseEntity<PessoaJuridica> obterEmpresa(@PathVariable("id") Long id) throws ExcepetionLojaVirtual {

		PessoaJuridica acesso = pessoaRepository.findById(id).orElse(null);

		if (acesso == null) {

			throw new ExcepetionLojaVirtual("Não encontou acesso com código: " + id);
		}

		return new ResponseEntity<PessoaJuridica>(acesso, HttpStatus.OK);
	}
	
	
	@GetMapping(value = "/obterPessoaEditar/{id}")// na vdd é pra editar
	public ResponseEntity<PessoaFisica> obterPessoaEditar(@PathVariable("id") Long id) throws ExcepetionLojaVirtual {

		PessoaFisica acesso = pessoaFisicaRepository.findById(id).orElse(null);

		if (acesso == null) {

			throw new ExcepetionLojaVirtual("Não encontou pessoa fisica com código: " + id);
		}

		return new ResponseEntity<PessoaFisica>(acesso, HttpStatus.OK);
	}
	
	
	@ResponseBody // Ele pega o objeto Java que seu método retorna e transforma em JSON pra mandar
	// de volta pra tela.
	@PostMapping(value = "/deleteend")
	public ResponseEntity<String> deleteend(@RequestBody Endereco end) {// requestBody transforma JSON da tela em objeto


		pessoaRepository.deleteEndById(end.getId());
		
		

		return new ResponseEntity("Endereco Removido", HttpStatus.OK);
	}
	
	
	@GetMapping(value = "/listUserByEmpresa/{idEmpresa}")
	public ResponseEntity<List<Usuario>> listUserByEmpresa(@PathVariable("idEmpresa") Long idEmpresa){
		
		List<Usuario> usuarios = usuarioRepository.listUserByEmpresa(idEmpresa);
		
		return new ResponseEntity(usuarios, HttpStatus.OK);
	}
	
	@GetMapping(value = "/obterUsuario/{idUser}")
	public ResponseEntity<Map<String, Object>>  obterUsuario(@PathVariable("idUser") Long idUser){
		
		Usuario usuarios = usuarioRepository.findById(idUser).get();
		
		Map<String, Object> resposta = new HashMap<>();
		
		
		resposta.put("id", usuarios.getId());
		resposta.put("login", usuarios.getLogin());
		resposta.put("senha", usuarios.getSenha());
		resposta.put("acesso",usuarios.getAcessos());
		
		
		
		return new ResponseEntity<Map<String, Object>> (resposta, HttpStatus.OK);
	}
	
	
		
	@ResponseBody 
	@PostMapping(value = "/updateUser")
	public ResponseEntity<String> updateUser(@RequestBody Usuario usuario) {// requestBody transforma JSON da tela em objeto
		
		usuarioRepository.updateLogin(usuario.getLogin(),usuario.getId());
		
		boolean senhaIgual = usuarioRepository.senhaIgual(usuario.getSenha(), usuario.getId());
		
		if(senhaIgual == false) {
			
			String senhaCriptografada = new BCryptPasswordEncoder().encode(usuario.getSenha());
			
			usuarioRepository.updateSenha2(senhaCriptografada, usuario.getId());
		}

		 Map<String, String> resposta = new HashMap<>();
		    resposta.put("mensagem", "Usuario Atualizado");
		
		return new ResponseEntity(resposta, HttpStatus.OK);
	}
	
	
	

	
	
	@ResponseBody // Ele pega o objeto Java que seu método retorna e transforma em JSON pra mandar
	// de volta pra tela.
	@PostMapping(value = "/deletarUser")
	public ResponseEntity<?> deletarUser(@RequestBody Long user) {// requestBody transforma JSON da tela em objeto

		Usuario usuario =  usuarioRepository.findById(user).get();
		
		Long idPessoa = usuario.getPessoa().getId();
		
		pessoaRepository.deletaAcessoUserByPessoa(usuario.getPessoa().getId());
		pessoaRepository.deleteByPessoa(usuario.getPessoa().getId());
	
		
		 pessoaRepository.deleteById(idPessoa);

		return new ResponseEntity("User Removido", HttpStatus.OK);
	}
	
	
	
	@GetMapping(value = "/listaTodasEmpresas/{codEmp}")
	public ResponseEntity<List<PessoaJuridica>> listaTodasEmpresas(@PathVariable("codEmp") Long codEmp)
			 {

	    System.out.println("COD EMP RECEBIDO = " + codEmp);

	    List<PessoaJuridica> lista = pessoaRepository.findAll(codEmp);

	    System.out.println("TOTAL ENCONTRADO = " + lista.size());


		return new ResponseEntity<List<PessoaJuridica>>(lista, HttpStatus.OK);
	}
	
	/*
	 * ===================== EXPLICAÇÃO DIDÁTICA =====================
	 *
	 * Esse controller faz apenas o papel de “porta de entrada”. Ele não concentra
	 * regra complexa, apenas validações simples.
	 *
	 * Primeiro ele verifica se o objeto recebido é nulo. Isso evita
	 * NullPointerException e mantém o erro controlado.
	 *
	 * Depois ele valida uma regra de negócio importante: não pode existir duas
	 * empresas com o mesmo CNPJ.
	 *
	 * Repare que essa validação só acontece quando o ID é null. Isso significa que
	 * estamos tratando apenas cadastro novo, e não atualização.
	 *
	 * Se tudo estiver válido, ele chama o service, que contém a regra pesada: -
	 * salvar empresa - ajustar endereços - criar usuário - gerar senha - enviar
	 * e-mail
	 *
	 * Por fim, ele devolve HTTP 200 com o objeto salvo.
	 *
	 * Se alguma regra for violada, é lançada uma ExcepetionLojaVirtual, que será
	 * tratada pelo ControleExcecoes e transformada em uma resposta HTTP adequada
	 * (como 404 ou outra).
	 *
	 * Em resumo: o controller valida, delega e responde. A regra real está no
	 * service. ===============================================================
	 */
}
