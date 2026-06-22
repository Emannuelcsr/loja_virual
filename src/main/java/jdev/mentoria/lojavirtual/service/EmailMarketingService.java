package jdev.mentoria.lojavirtual.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import jdev.mentoria.lojavirtual.ApiTokenIntegracao;
import jdev.mentoria.lojavirtual.model.dto.CampaignDTO;
import jdev.mentoria.lojavirtual.model.dto.CreateMessageDTO;
import jdev.mentoria.lojavirtual.model.dto.FromFieldEmailMarketing;
import jdev.mentoria.lojavirtual.model.dto.LeadCampanhaEmailCadastrado;
import jdev.mentoria.lojavirtual.model.dto.LeadCampanhaEmailMarketing;

@Service
public class EmailMarketingService {

	public List<CampaignDTO> CarregaListaCampanhasEmailMarketing() throws Exception {

		Client client = new HostIgnoreClient(ApiTokenIntegracao.URL_EMAIL_MARKETING).hostIgnoreClient();

		String json = client.resource(ApiTokenIntegracao.URL_EMAIL_MARKETING + "campaigns")
				.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON)
				.header("X-Auth-Token", ApiTokenIntegracao.TOKEN_EMAIL_MARKETING).get(String.class);

		ObjectMapper objectMapper = new ObjectMapper();

		objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);

		List<CampaignDTO> campaignDTOs = objectMapper.readValue(json, new TypeReference<List<CampaignDTO>>() {
		});

		return campaignDTOs;
	}

	public String CriaLeadApiMarketing(LeadCampanhaEmailMarketing leadEmailMarketing) throws Exception {

		String json = new ObjectMapper().writeValueAsString(leadEmailMarketing);

		Client client = new HostIgnoreClient(ApiTokenIntegracao.URL_EMAIL_MARKETING).hostIgnoreClient();

		WebResource webResource = client.resource(ApiTokenIntegracao.URL_EMAIL_MARKETING + "contacts");

		ClientResponse clientResponse = webResource.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON)
				.header("X-Auth-Token", ApiTokenIntegracao.TOKEN_EMAIL_MARKETING).post(ClientResponse.class, json);

		String retorno = clientResponse.getEntity(String.class);
		clientResponse.close();
		return retorno;
	}

	public String EnviaEmailMarketing(String idCampanha, String assuntoEmail, String mensagemEmail) throws Exception {

		CreateMessageDTO createMessageDTO = new CreateMessageDTO();

		createMessageDTO.getSendSettings().getSelectedCampaigns().add(idCampanha);// fmufd - campanha no getResponse que
																					// ja testei

		createMessageDTO.setSubject(assuntoEmail);
		createMessageDTO.setName(createMessageDTO.getSubject());

		createMessageDTO.getFromField().setFromFieldId("rH63t");

		createMessageDTO.getReplyTo().setFromFieldId("rH63t");

		createMessageDTO.getCampaign().setCampaignId("fmufd");

		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		LocalDate hoje = LocalDate.now();

		String dataEnvio = hoje.format(dateTimeFormatter);

		// createMessageDTO.getSendSettings().setSendOn(new SendOn());

		createMessageDTO.getContent().setHtml(mensagemEmail);

		String json = new ObjectMapper().writeValueAsString(createMessageDTO);

		Client client = new HostIgnoreClient(ApiTokenIntegracao.URL_EMAIL_MARKETING).hostIgnoreClient();

		WebResource webResource = client.resource(ApiTokenIntegracao.URL_EMAIL_MARKETING + "newsletters");

		ClientResponse clientResponse = webResource.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON)
				.header("X-Auth-Token", ApiTokenIntegracao.TOKEN_EMAIL_MARKETING).post(ClientResponse.class, json);

		String retorno = clientResponse.getEntity(String.class);
		
		if(clientResponse.getStatus() == 201) {
			
			retorno = "Enviado com sucesso";
		}
				
				
				
				clientResponse.close();
		return retorno;
	}
	
	
	public List<FromFieldEmailMarketing> ListaRemetenteEmailMarketing () throws Exception{
		

		Client client = new HostIgnoreClient(ApiTokenIntegracao.URL_EMAIL_MARKETING).hostIgnoreClient();

		WebResource webResource = client.resource(ApiTokenIntegracao.URL_EMAIL_MARKETING + "from-fields");

		String clientResponse = webResource.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON)
				.header("X-Auth-Token", ApiTokenIntegracao.TOKEN_EMAIL_MARKETING).get(String.class);

		
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
		System.out.println("RESPOSTA CRUA = " + clientResponse);
		List<FromFieldEmailMarketing> list = objectMapper.readValue(clientResponse, new TypeReference<List<FromFieldEmailMarketing>>() {
		});
		
		
		System.out.println("STATUS: " + clientResponse);
		
		return list;
		
		
	}
	

}
