package jdev.mentoria.lojavirtual;

import java.util.Calendar;
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
		
		String descricaoa = "testeMockTransacional" + Calendar.getInstance().getTimeInMillis();
		
		
		// Cria o MockMvc, que é quem vai simular a requisição HTTP
		MockMvc mockMvc = builder.build();

		// Cria o objeto que será enviado para a API (como se fosse o corpo da
		// requisição)
		Acesso acesso = new Acesso();

		
		
		
		// Define o dado que queremos testar enviando para o endpoint
		acesso.setDescricao(descricaoa);

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

		assertEquals(descricaoa, objetoRetorno.getDescricao());

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
	 * @throws ExcepetionLojaVirtual 
	 */
	@Test
	void testCadastroAcesso() throws ExcepetionLojaVirtual {

	    Long id1 = null;
	    Long id2 = null;

	    try {
	        // ===================== CADASTRO DE UM ACESSO =====================

	        Acesso acesso = new Acesso();

	        // descrição única pra não bater em registro antigo
	        String descricao1 = "teste_" + System.currentTimeMillis();
	        acesso.setDescricao(descricao1);

	        acesso = acessoController.salvarAcesso(acesso).getBody();

	        assertEquals(true, acesso.getId() > 0);
	        id1 = acesso.getId();

	        // ===================== BUSCA POR ID =====================

	        Acesso acesso2 = acessoRepository.findById(id1).orElse(null);

	        assertEquals(true, acesso2 != null);
	        assertEquals(id1, acesso2.getId());
	        assertEquals(descricao1, acesso2.getDescricao());

	        // ===================== EXCLUSÃO DO REGISTRO =====================

	        acessoRepository.deleteById(id1);
	        acessoRepository.flush();

	        Acesso acesso3 = acessoRepository.findById(id1).orElse(null);
	        assertEquals(true, acesso3 == null);

	        // ===================== TESTE DE BUSCA CUSTOMIZADA =====================

	        acesso = new Acesso();
	        String descricao2 = "ROLE_ALUNO_" + System.currentTimeMillis();
	        acesso.setDescricao(descricao2);

	        acesso = acessoController.salvarAcesso(acesso).getBody();
	        id2 = acesso.getId();

	        List<Acesso> acessos = acessoRepository.buscarAcessoDesc("ALUNO");

	        // em vez de size==1 (frágil), garante que tem PELO MENOS um
	        assertEquals(true, acessos.size() >= 1);

	    } finally {
	        // limpeza defensiva: não suja o banco mesmo se der erro no meio
	        if (id2 != null && acessoRepository.existsById(id2)) {
	            acessoRepository.deleteById(id2);
	        }
	        if (id1 != null && acessoRepository.existsById(id1)) {
	            acessoRepository.deleteById(id1);
	        }
	        acessoRepository.flush();
	    }
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
