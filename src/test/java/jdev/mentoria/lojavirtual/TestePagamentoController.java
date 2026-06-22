package jdev.mentoria.lojavirtual;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import jdev.mentoria.lojavirtual.controller.PagamentoController;
import jdev.mentoria.lojavirtual.controller.RecebePagamentoWebHookApiAsaas;
import jdev.mentoria.lojavirtual.model.dto.AsaasWebhookDTO;
import junit.framework.TestCase;

@org.springframework.test.context.ActiveProfiles("dev")
@SpringBootTest(classes = LojaVirualApplication.class)
public class TestePagamentoController extends TestCase {

	@Autowired
	private PagamentoController pagamentoController;

	@Autowired
	private RecebePagamentoWebHookApiAsaas recebePagamentoWebHookApiAsaas;

	@Autowired
	private WebApplicationContext wac;

	@Test
	public void testFinalizarCompraCartaoAsaas() throws Exception {

		pagamentoController.finalizarCompraCartaoAsaas("5502099263789251", "EMANNUEL C S RUSSO", "429", "04", "2034",
				29L, "06826774933", 2, "88330338", "RUA 2970", "200", "SC", "BC");
	}

	@Test
	public void recebeNotificacaoPagamentoApiAsaas() throws Exception {

		DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(wac);

		MockMvc mockMvc = builder.build();

		ObjectMapper objectMapper = new ObjectMapper();

		objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		AsaasWebhookDTO dto = new AsaasWebhookDTO();
		
		String json = new String(Files.readAllBytes(Paths.get("E:\\workspace\\loja_virtual\\src\\test\\java\\webhook\\teste")));
		
		ResultActions retornoApi =  mockMvc.perform(MockMvcRequestBuilders.post("/requisicaoapiasaas/notificacaoapiasaas").content(json)
				.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON));
		
		System.out.println(retornoApi.andReturn().getResponse().getContentAsString());

	}

}
