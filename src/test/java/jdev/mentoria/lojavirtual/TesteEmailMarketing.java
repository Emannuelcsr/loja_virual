package jdev.mentoria.lojavirtual;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import jdev.mentoria.lojavirtual.model.dto.CampaignDTO;
import jdev.mentoria.lojavirtual.model.dto.CreateFromFieldEmailMarketing;
import jdev.mentoria.lojavirtual.model.dto.CreateMessageDTO;
import jdev.mentoria.lojavirtual.model.dto.FromFieldEmailMarketing;
import jdev.mentoria.lojavirtual.model.dto.LeadCampanhaEmailCadastrado;
import jdev.mentoria.lojavirtual.model.dto.LeadCampanhaEmailMarketing;
import jdev.mentoria.lojavirtual.model.dto.SendOn;
import jdev.mentoria.lojavirtual.service.EmailMarketingService;
import jdev.mentoria.lojavirtual.service.HostIgnoreClient;
import junit.framework.TestCase;

@org.springframework.test.context.ActiveProfiles("dev")
@SpringBootTest(classes = LojaVirualApplication.class)
public class TesteEmailMarketing extends TestCase {

	@Autowired
	private EmailMarketingService emailMarketingService;

	@Test
	public void testeCarregaCampanhaGetResponse() throws Exception {

		List<CampaignDTO> campaignDTOs = emailMarketingService.CarregaListaCampanhasEmailMarketing();

		for (CampaignDTO campaignDTO : campaignDTOs) {

			System.out.println(campaignDTO);
			System.out.println("----------------------");

		}
	}

	@Test
	public void testCriaLead() throws Exception {

		LeadCampanhaEmailMarketing lead = new LeadCampanhaEmailMarketing();

		// 🔹 nome
		lead.setName("John Doe");

		// 🔹 email
		lead.setEmail("frankiecorretor@gmail.com");

		LeadCampanhaEmailCadastrado emailCadastrado = new LeadCampanhaEmailCadastrado();
		emailCadastrado.setCampaignId("fmufd");

		lead.setCampaign(emailCadastrado);

		// 🔹 dia do ciclo
		lead.setDayOfCycle("42");

		// 🔹 pontuação
		lead.setScoring(8);

		// 🔹 IP
		lead.setIpAddress("1.2.3.4");

		emailMarketingService.CriaLeadApiMarketing(lead);

	}

	@Test
	public void testEnviaEmailPorApi() throws Exception {

		String retorno = emailMarketingService.EnviaEmailMarketing("fmufd", "Teste marketing",
				"<html><h1>oioioioi zzzz</h1></html>");

		System.out.println(retorno);
	}

	@Test
	public void testFromField() throws Exception {

		List<FromFieldEmailMarketing> lista = emailMarketingService.ListaRemetenteEmailMarketing();

		for (FromFieldEmailMarketing fromFieldEmailMarketing : lista) {
			
			
		    System.out.println("ID = " + fromFieldEmailMarketing.getFromFieldId());
		    System.out.println("EMAIL = " + fromFieldEmailMarketing.getEmail());
		    System.out.println("NOME = " + fromFieldEmailMarketing.getName());
			
		}
		
		
		
	}

}
