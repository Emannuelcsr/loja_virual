package jdev.mentoria.lojavirtual;

import java.awt.MediaTracker;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import jdev.mentoria.lojavirtual.controller.AcessoController;
import jdev.mentoria.lojavirtual.model.Acesso;
import jdev.mentoria.lojavirtual.repository.AcessoRepository;
import junit.framework.TestCase;
import tools.jackson.core.JacksonException;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

/**
 * Classe de testes responsável por validar o fluxo completo de cadastro, busca
 * e exclusão da entidade {@link Acesso}.
 *
 * <p>
 * Esse teste sobe o contexto completo do Spring Boot, permitindo testar a
 * aplicação de forma integrada, utilizando banco de dados, repositórios e
 * controllers reais.
 * </p>
 *
 * <p>
 * O objetivo aqui não é testar métodos isolados, mas sim garantir que o fluxo
 * completo do sistema esteja funcionando corretamente do início ao fim.
 * </p>
 */

@Profile("test")
@SpringBootTest(classes = LojaVirualApplication.class)
class LojaVirualApplicationTests extends TestCase {

	/**
	 * Controller responsável por salvar acessos. É injetado pelo Spring durante a
	 * execução do teste.
	 */
	@Autowired
	private AcessoController acessoController;

	/**
	 * Repositório responsável por acessar os dados da tabela de acessos.
	 */
	@Autowired
	private AcessoRepository acessoRepository;

	// Injeta o contexto web completo da aplicação dentro do teste.
	// Esse contexto contém todos os componentes registrados pelo Spring:
	// Controllers, Services, Repositories, conversores de JSON, configurações etc.
	// Ele representa a aplicação já "montada", mas rodando apenas na memória do
	// teste,
	// sem subir servidor externo (Tomcat/Jetty).
	@Autowired
	private WebApplicationContext webApplicationContext;

	@Transactional
	@Test
	public void testRestApiCadastroAcesso() throws JacksonException, Exception {

		// Monta o ambiente da aplicação (Controllers, configs, etc.) dentro do teste
		DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(this.webApplicationContext);

		// Cria o MockMvc, que é quem vai simular a requisição HTTP
		MockMvc mockMvc = builder.build();

		// Cria o objeto que será enviado para a API (como se fosse o corpo da
		// requisição)
		Acesso acesso = new Acesso();

		// Define o dado que queremos testar enviando para o endpoint
		acesso.setDescricao("testeMockTransacional");

		// Objeto responsável por converter Java ↔ JSON
		ObjectMapper objectMapper = new ObjectMapper();

		// Aqui acontece a simulação da chamada HTTP
		ResultActions retornoApi = mockMvc.perform(

				// Simula um POST para o endpoint /salvarAcesso
				MockMvcRequestBuilders.post("/salvarAcesso")

						// Converte o objeto Java em JSON e coloca no corpo da requisição
						.content(objectMapper.writeValueAsString(acesso))

						// Diz que esperamos receber JSON como resposta
						.accept(MediaType.APPLICATION_JSON)

						// Diz que estamos enviando JSON
						.contentType(MediaType.APPLICATION_JSON));

		System.out.println("Retorno da api " + retornoApi.andReturn().getResponse().getContentAsString());

		// -----------------------CONVERTE O RETORNO DA API PARA UM OBJETO DE ACESSo

		Acesso objetoRetorno = objectMapper.readValue(retornoApi.andReturn().getResponse().getContentAsString(),
				Acesso.class);

		assertEquals(acesso.getDescricao(), objetoRetorno.getDescricao());

	}

	@Test
	public void testRestApiDeleteAcesso() throws JacksonException, Exception {

		// Monta o ambiente da aplicação (Controllers, configs, etc.) dentro do teste
		DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(this.webApplicationContext);

		// Cria o MockMvc, que é quem vai simular a requisição HTTP
		MockMvc mockMvc = builder.build();

		// Cria o objeto que será enviado para a API (como se fosse o corpo da
		// requisição)
		Acesso acesso = new Acesso();

		// Define o dado que queremos testar enviando para o endpoint
		acesso.setDescricao("testeMockDeleteTransacional");

		acesso = acessoRepository.save(acesso);

		// Objeto responsável por converter Java ↔ JSON
		ObjectMapper objectMapper = new ObjectMapper();

		// Aqui acontece a simulação da chamada HTTP
		ResultActions retornoApi = mockMvc.perform(

				// Simula um POST para o endpoint /deleteAcesso
				MockMvcRequestBuilders.post("/deleteAcesso")

						// Converte o objeto Java em JSON e coloca no corpo da requisição
						.content(objectMapper.writeValueAsString(acesso))

						// Diz que esperamos receber JSON como resposta
						.accept(MediaType.APPLICATION_JSON)

						// Diz que estamos enviando JSON
						.contentType(MediaType.APPLICATION_JSON));

		System.out.println("Retorno da api " + retornoApi.andReturn().getResponse().getContentAsString());
		System.out.println("Status de retorno " + retornoApi.andReturn().getResponse().getStatus());

		assertEquals("Cadastro Removido", retornoApi.andReturn().getResponse().getContentAsString());
		assertEquals(200, retornoApi.andReturn().getResponse().getStatus());
	}

	@Test
	public void testRestApiDeleteporId() throws JacksonException, Exception {

		// Monta o ambiente da aplicação (Controllers, configs, etc.) dentro do teste
		DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(this.webApplicationContext);

		// Cria o MockMvc, que é quem vai simular a requisição HTTP
		MockMvc mockMvc = builder.build();

		// Cria o objeto que será enviado para a API (como se fosse o corpo da
		// requisição)
		Acesso acesso = new Acesso();

		// Define o dado que queremos testar enviando para o endpoint
		acesso.setDescricao("testeMockDeleteTransacional");

		acesso = acessoRepository.save(acesso);

		// Objeto responsável por converter Java ↔ JSON
		ObjectMapper objectMapper = new ObjectMapper();

		// Aqui acontece a simulação da chamada HTTP
		ResultActions retornoApi = mockMvc.perform(

				// Simula um POST para o endpoint /deleteAcesso
				MockMvcRequestBuilders.delete("/deleteAcessoPorId/" + acesso.getId())

						// Converte o objeto Java em JSON e coloca no corpo da requisição
						.content(objectMapper.writeValueAsString(acesso))

						// Diz que esperamos receber JSON como resposta
						.accept(MediaType.APPLICATION_JSON)

						// Diz que estamos enviando JSON
						.contentType(MediaType.APPLICATION_JSON));

		System.out.println("Retorno da api " + retornoApi.andReturn().getResponse().getContentAsString());
		System.out.println("Status de retorno " + retornoApi.andReturn().getResponse().getStatus());

		assertEquals("Cadastro Removido", retornoApi.andReturn().getResponse().getContentAsString());
		assertEquals(200, retornoApi.andReturn().getResponse().getStatus());
	}

	@Test
	public void testRestApiObterAcesso() throws JacksonException, Exception {

		// Monta o ambiente da aplicação (Controllers, configs, etc.) dentro do teste
		DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(this.webApplicationContext);

		// Cria o MockMvc, que é quem vai simular a requisição HTTP
		MockMvc mockMvc = builder.build();

		// Cria o objeto que será enviado para a API (como se fosse o corpo da
		// requisição)
		Acesso acesso = new Acesso();

		// Define o dado que queremos testar enviando para o endpoint
		acesso.setDescricao("testeMockObterAcesso");

		acesso = acessoRepository.save(acesso);

		// Objeto responsável por converter Java ↔ JSON
		ObjectMapper objectMapper = new ObjectMapper();

		// Aqui acontece a simulação da chamada HTTP
		ResultActions retornoApi = mockMvc.perform(

				// Simula um POST para o endpoint /deleteAcesso
				MockMvcRequestBuilders.get("/obterAcesso/" + acesso.getId())

						// Converte o objeto Java em JSON e coloca no corpo da requisição
						.content(objectMapper.writeValueAsString(acesso))

						// Diz que esperamos receber JSON como resposta
						.accept(MediaType.APPLICATION_JSON)

						// Diz que estamos enviando JSON
						.contentType(MediaType.APPLICATION_JSON));

		assertEquals(200, retornoApi.andReturn().getResponse().getStatus());

		Acesso acessoRetorno = objectMapper.readValue(retornoApi.andReturn().getResponse().getContentAsString(),
				Acesso.class);

		assertEquals(acesso.getDescricao(), acessoRetorno.getDescricao());
	}

	@Test
	public void testRestApiBuscaDesc() throws JacksonException, Exception {

		// Monta o ambiente da aplicação (Controllers, configs, etc.) dentro do teste
		DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(this.webApplicationContext);

		// Cria o MockMvc, que é quem vai simular a requisição HTTP
		MockMvc mockMvc = builder.build();

		// Cria o objeto que será enviado para a API (como se fosse o corpo da
		// requisição)
		Acesso acesso = new Acesso();

		// Define o dado que queremos testar enviando para o endpoint
		acesso.setDescricao("TESTAO");

		acesso = acessoRepository.save(acesso);

		// Objeto responsável por converter Java ↔ JSON
		ObjectMapper objectMapper = new ObjectMapper();

		// Aqui acontece a simulação da chamada HTTP
		ResultActions retornoApi = mockMvc.perform(

				// Simula um POST para o endpoint /deleteAcesso
				MockMvcRequestBuilders.get("/buscarPorDescricao/TESTAO")

						// Converte o objeto Java em JSON e coloca no corpo da requisição
						.content(objectMapper.writeValueAsString(acesso))

						// Diz que esperamos receber JSON como resposta
						.accept(MediaType.APPLICATION_JSON)

						);

		assertEquals(200, retornoApi.andReturn().getResponse().getStatus());

		
		List<Acesso> listaRetorno = objectMapper.readValue(
			    retornoApi.andReturn().getResponse().getContentAsString(),
			    new TypeReference<List<Acesso>>() {}
			);

		
		assertEquals(1, listaRetorno.size());
		
		assertEquals(acesso.getDescricao(),listaRetorno.get(0).getDescricao());

		
		acessoRepository.deleteById(acesso.getId());

	}

	
	
	
	
	
	
	
	/**
	 * Testa o ciclo completo da entidade Acesso:
	 *
	 * <p>
	 * - cadastra um acesso - valida se foi salvo corretamente - busca no banco pelo
	 * ID - remove o registro - valida se a exclusão funcionou - testa busca
	 * customizada por descrição
	 * </p>
	 */
	@Test
	void testCadastroAcesso() {

		// ===================== CADASTRO DE UM ACESSO =====================

		// Cria um novo objeto Acesso
		Acesso acesso = new Acesso();

		// Define a descrição da permissão
		acesso.setDescricao("teste");

		// Salva o acesso usando o controller (fluxo real da aplicação)
		acesso = acessoController.salvarAcesso(acesso).getBody();

		// Verifica se o ID foi gerado corretamente (registro persistido)
		assertEquals(true, acesso.getId() > 0);

		// ===================== BUSCA POR ID =====================

		// Busca o mesmo acesso diretamente pelo repositório
		Acesso acesso2 = acessoRepository.findById(acesso.getId()).get();

		// Confirma que o ID retornado é o mesmo que foi salvo
		assertEquals(acesso.getId(), acesso2.getId());

		// ===================== EXCLUSÃO DO REGISTRO =====================

		// Remove o registro do banco
		acessoRepository.deleteById(acesso.getId());

		// Força a execução imediata do DELETE no banco
		acessoRepository.flush();

		// Tenta buscar novamente o registro removido
		Acesso acesso3 = acessoRepository.findById(acesso2.getId()).orElse(null);

		// Verifica que o registro não existe mais
		assertEquals(true, acesso3 == null);

		// ===================== TESTE DE BUSCA CUSTOMIZADA =====================

		// Cria um novo acesso com padrão de role
		acesso = new Acesso();
		acesso.setDescricao("ROLE_ALUNO");

		// Salva o novo acesso
		acesso = acessoController.salvarAcesso(acesso).getBody();

		// Executa a busca customizada pelo repositório
		List<Acesso> acessos = acessoRepository.buscarAcessoDesc("ALUNO".trim().toUpperCase());

		// Verifica se encontrou exatamente um registro
		assertEquals(1, acessos.size());

		// Remove o registro criado para não sujar o banco de testes
		acessoRepository.deleteById(acesso.getId());
	}

	/*
	 * ===================== EXPLICAÇÃO DIDÁTICA =====================
	 *
	 * Esse teste é um exemplo clássico de teste de integração. Ele não testa apenas
	 * um método isolado, mas sim o comportamento real da aplicação rodando com
	 * Spring Boot.
	 *
	 * Primeiro, o teste cria um objeto Acesso e salva usando o AcessoController.
	 * Isso garante que a camada de controller, service (se existir) e repositório
	 * estão funcionando juntas.
	 *
	 * Em seguida, o teste busca o mesmo registro diretamente pelo repositório para
	 * validar se o dado foi persistido corretamente no banco.
	 *
	 * Depois disso, o teste remove o registro e força o flush, garantindo que o
	 * DELETE seja executado imediatamente. A busca seguinte confirma que o dado
	 * realmente não existe mais.
	 *
	 * Na segunda parte do teste, o foco muda: aqui é validado um método de consulta
	 * customizada, que busca acessos pela descrição.
	 *
	 * Esse tipo de teste é muito importante porque garante que: - o mapeamento JPA
	 * está correto - o banco está respondendo como esperado - as regras de negócio
	 * funcionam de ponta a ponta
	 *
	 * Em resumo: esse teste garante que o CRUD de Acesso está íntegro, confiável e
	 * pronto para ser usado pelo Spring Security.
	 * ===============================================================
	 */

}
