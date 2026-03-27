package jdev.mentoria.lojavirtual.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import jakarta.ws.rs.core.MediaType;
import jdev.mentoria.lojavirtual.ApiTokenIntegracao;
import jdev.mentoria.lojavirtual.model.AcessTokenJunoAPI;
import jdev.mentoria.lojavirtual.model.BoletoJuno;
import jdev.mentoria.lojavirtual.model.VendaCompraLojaVirtual;
import jdev.mentoria.lojavirtual.model.dto.BoletoGeradoApiJuno;
import jdev.mentoria.lojavirtual.model.dto.CobrancaJunoAPI;
import jdev.mentoria.lojavirtual.model.dto.ConteudoBoletoJuno;
import jdev.mentoria.lojavirtual.model.dto.ObjetoPostCarneJuno;
import jdev.mentoria.lojavirtual.repository.AccesTokenJunoRepository;
import jdev.mentoria.lojavirtual.repository.BoletoJunoRepository;
import jdev.mentoria.lojavirtual.repository.Vd_cp_Loja_virtual_Repository;

@Service
public class ServiceJunoBoleto implements Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	private AcessTokenJunoService accessTokenJunoService;

	@Autowired
	private AccesTokenJunoRepository accesTokenJunoRepository;

	@Autowired
	private Vd_cp_Loja_virtual_Repository vd_Cp_Loja_virt_repository;

	@Autowired
	private BoletoJunoRepository boletoJunoRepository;

	/**
	 * Obtém um token de acesso válido da API da Juno para ser utilizado nas
	 * integrações do sistema.
	 *
	 * <p>
	 * Este método implementa uma estratégia simples de reaproveitamento de token,
	 * evitando que a aplicação solicite um novo token à Juno a cada chamada.
	 * </p>
	 *
	 * <p>
	 * O fluxo funciona assim:
	 * </p>
	 * <ul>
	 * <li>Primeiro tenta buscar no banco de dados um token já salvo.</li>
	 * <li>Se não existir token salvo, solicita um novo token à API da Juno.</li>
	 * <li>Se existir token salvo, verifica se ele expirou.</li>
	 * <li>Se o token estiver expirado, solicita um novo token à API da Juno.</li>
	 * <li>Se o token ainda estiver válido, reutiliza o token já salvo no
	 * banco.</li>
	 * </ul>
	 *
	 * <p>
	 * Quando é necessário gerar um novo token, o método utiliza o fluxo OAuth
	 * <strong>client_credentials</strong>, montando a autenticação no padrão
	 * <strong>Basic Auth</strong> com {@code clientID + ":" + secretID},
	 * convertendo esse valor para Base64 e enviando a requisição para o endpoint de
	 * autorização da Juno.
	 * </p>
	 *
	 * <p>
	 * Se a API responder com sucesso (status HTTP 200), o método remove os tokens
	 * antigos salvos no banco, mantendo apenas um único token para todo o projeto.
	 * Em seguida, converte a resposta da API em um objeto
	 * {@link jdev.mentoria.lojavirtual.model.AcessTokenJunoAPI}, salva esse novo
	 * token no banco e o retorna para uso da aplicação.
	 * </p>
	 *
	 * <p>
	 * Se a API externa não retornar sucesso, o método devolve {@code null},
	 * indicando que não foi possível obter um token válido naquele momento.
	 * </p>
	 *
	 * <p>
	 * Resumo da regra de negócio:
	 * </p>
	 * <ul>
	 * <li>Se já existe token válido, reutiliza.</li>
	 * <li>Se não existe ou expirou, gera um novo.</li>
	 * <li>Mantém somente um token salvo no banco.</li>
	 * </ul>
	 *
	 * @return um objeto {@code AcessTokenJunoAPI} contendo um token válido para uso
	 *         na integração com a Juno, ou {@code null} caso a API não retorne
	 *         sucesso na geração de um novo token.
	 *
	 * @throws Exception caso ocorra algum erro durante a comunicação com a API da
	 *                   Juno ou durante o processamento da resposta.
	 */
	public AcessTokenJunoAPI obterTokenApiJuno() throws Exception {
		// [PROFESSOR]
		// Método criado pelo professor para centralizar a lógica de obter um token
		// válido.
		// A API da Juno não manda criar esse método com esse nome.
		// Isso é organização interna do sistema.

		AcessTokenJunoAPI accessTokenJunoAPI = accessTokenJunoService.buscaTokenAtivo();
		// [PROFESSOR]
		// Aqui ele tenta buscar no banco um token já salvo.
		// Isso não veio da API.
		// Foi uma estratégia criada por ele para reaproveitar token e evitar gerar um
		// novo toda hora.

		if (accessTokenJunoAPI == null || (accessTokenJunoAPI != null && accessTokenJunoAPI.expirado())) {
			// [PROFESSOR]
			// Regra criada por ele:
			// se não existir token no banco OU se o token existente estiver expirado,
			// então precisa pedir um novo token para a Juno.
			// A API não manda fazer isso desse jeito.
			// A API só fornece o token; a decisão de reaproveitar ou renovar é do sistema.

			String clienteID = "";
			// [API]
			// A documentação da Juno exige um client_id para autenticar a aplicação.
			// O valor em si vem do cadastro da sua aplicação na plataforma da Juno.
			// Declarar essa variável no código foi o professor que fez, mas a necessidade
			// dela vem da API.

			String secretID = "";
			// [API]
			// A documentação também exige o client_secret.
			// Assim como o client_id, ele vem da Juno.
			// A variável foi criada pelo professor, mas a exigência vem da API.

			Client client = new HostIgnoreClient("https://api.juno.com.br/").hostIgnoreClient();
			// [PROFESSOR] + [BIBLIOTECA]
			// Client é da biblioteca usada para fazer chamadas HTTP.
			// Já HostIgnoreClient parece ser uma classe criada no projeto ou passada pelo
			// professor.
			// Isso não veio da documentação da Juno.
			// A API só informa a URL; a forma de montar o cliente HTTP foi escolha do
			// professor.

			WebResource webResource = client
					.resource("https://api.juno.com.br/authorization-server/oauth/token?grant_type=client_credentials");
			// [API] + [BIBLIOTECA]
			// A URL do endpoint e o grant_type=client_credentials vieram da documentação da
			// Juno.
			// O uso de WebResource é da biblioteca Jersey para representar a URL do recurso
			// HTTP.

			String basicChave = clienteID + ":" + secretID;
			// [API] + [PROFESSOR]
			// A documentação da API exige autenticação Basic no formato:
			// client_id:client_secret
			// A concatenação com ":" foi implementada pelo professor para seguir esse
			// padrão da API.

			String token_autenticao = DatatypeConverter.printBase64Binary(basicChave.getBytes());
			// [API] + [BIBLIOTECA]
			// A API exige que client_id:client_secret seja enviado em Base64 no header
			// Authorization.
			// A necessidade da codificação vem da API.
			// O uso de DatatypeConverter é recurso da biblioteca Java.

			ClientResponse clientResponse = webResource.accept(MediaType.APPLICATION_FORM_URLENCODED)
					// [API]
					// Define o tipo de mídia aceito/esperado na comunicação.
					// Isso segue o padrão exigido pela documentação desse endpoint.

					.type(MediaType.APPLICATION_FORM_URLENCODED)
					// [API]
					// Define o tipo de conteúdo enviado na requisição.
					// Esse endpoint trabalha com application/x-www-form-urlencoded.

					.header("Content-Type", "application/x-www-form-urlencoded")
					// [API]
					// Header exigido pela API para esse tipo de autenticação/token.

					.header("Authorization", "Basic " + token_autenticao)
					// [API]
					// Header de autenticação exigido pela documentação:
					// Authorization: Basic <base64(client_id:client_secret)>

					.post(ClientResponse.class);
			// [API] + [BIBLIOTECA]
			// A API manda fazer uma requisição POST nesse endpoint.
			// O .post(...) é a forma da biblioteca Jersey executar essa chamada.

			if (clientResponse.getStatus() == 200) { /* Sucesso */
				// [PROFESSOR]
				// Aqui ele trata a regra de sucesso da resposta HTTP.
				// A API usa códigos HTTP padronizados.
				// Escolher verificar explicitamente 200 aqui foi decisão do professor.

				accesTokenJunoRepository.deleteAll();
				// [PROFESSOR]
				// Isso foi totalmente decisão dele.
				// A API não manda apagar nada do banco.
				// Ele fez isso porque quer manter apenas um token salvo no sistema.

				accesTokenJunoRepository.flush();
				// [PROFESSOR] + [JPA]
				// Força a sincronização da exclusão com o banco imediatamente.
				// Não veio da API.
				// É detalhe de persistência do sistema.

				AcessTokenJunoAPI accessTokenJunoAPI2 = clientResponse.getEntity(AcessTokenJunoAPI.class);
				// [API] + [PROFESSOR] + [BIBLIOTECA]
				// A API devolve um JSON com os dados do token.
				// A classe AcessTokenJunoAPI foi criada para representar esse JSON no Java.
				// O getEntity(...) usa a biblioteca para converter a resposta em objeto.

				accessTokenJunoAPI2.setToken_acesso(token_autenticao);
				// [PROFESSOR]
				// Isso foi escolha dele.
				// A API não manda salvar esse token_autenticao nesse campo.
				// Ele decidiu guardar isso no objeto também.

				accessTokenJunoAPI2 = accesTokenJunoRepository.saveAndFlush(accessTokenJunoAPI2);
				// [PROFESSOR] + [JPA]
				// Salva o novo token no banco e sincroniza na hora.
				// A API não manda persistir nada.
				// Isso é arquitetura do backend.

				return accessTokenJunoAPI2;
				// [PROFESSOR]
				// Retorna o token novo obtido e salvo.
				// Isso faz parte da regra interna do método.
			} else {
				return null;
				// [PROFESSOR]
				// Se a API não respondeu com sucesso, ele decidiu retornar null.
				// A API não manda retornar null; isso é decisão de tratamento do sistema.
			}

		} else {
			return accessTokenJunoAPI;
			// [PROFESSOR]
			// Se já existia token válido no banco, ele simplesmente reutiliza.
			// Essa lógica inteira de reaproveitamento foi criada pelo professor.
		}
	}

	public String geraChaveBoletoPix() throws Exception {

		AcessTokenJunoAPI accessTokenJunoAPI = this.obterTokenApiJuno();

		Client client = new HostIgnoreClient("https://api.juno.com.br/").hostIgnoreClient();
		WebResource webResource = client.resource("https://api.juno.com.br/pix/keys");
		// WebResource webResource =
		// client.resource("https://api.juno.com.br/api-integration/pix/keys");

		ClientResponse clientResponse = webResource.accept("application/json;charset=UTF-8")
				.header("Content-Type", "application/json").header("X-API-Version", 2)
				.header("X-Resource-Token", ApiTokenIntegracao.TOKEN_PRIVATE_JUNO)
				.header("Authorization", "Bearer " + accessTokenJunoAPI.getAcess_token())
				.post(ClientResponse.class, "{ \"type\": \"RANDOM_KEY\" }");

		// .header("X-Idempotency-Key", "chave-boleto-pix")
		return clientResponse.getEntity(String.class);

	}

	/*
	 * Método que gera o PIX e Boleto com a API da Juno/Ebanx
	 */
	public String gerarCarneApi(ObjetoPostCarneJuno objetoPostCarneJuno) throws Exception {

		VendaCompraLojaVirtual vendaCompraLojaVirtual = vd_Cp_Loja_virt_repository
				.findById(objetoPostCarneJuno.getIdVenda()).get();

		CobrancaJunoAPI cobrancaJunoAPI = new CobrancaJunoAPI();

		cobrancaJunoAPI.getCharge().setPixKey(ApiTokenIntegracao.CHAVE_BOLETO_PIX);
		cobrancaJunoAPI.getCharge().setDescription(objetoPostCarneJuno.getDescription());
		cobrancaJunoAPI.getCharge().setAmount(Float.valueOf(objetoPostCarneJuno.getTotalAmount()));
		cobrancaJunoAPI.getCharge().setInstallments(Integer.parseInt(objetoPostCarneJuno.getInstallments()));

		Calendar dataVencimento = Calendar.getInstance();
		dataVencimento.add(Calendar.DAY_OF_MONTH, 7);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyy-MM-dd");
		cobrancaJunoAPI.getCharge().setDueDate(dateFormat.format(dataVencimento.getTime()));

		cobrancaJunoAPI.getCharge().setFine(BigDecimal.valueOf(1.00));
		cobrancaJunoAPI.getCharge().setInterest(BigDecimal.valueOf(1.00));
		cobrancaJunoAPI.getCharge().setMaxOverdueDays(10);
		cobrancaJunoAPI.getCharge().getPaymentTypes().add("BOLETO_PIX");

		cobrancaJunoAPI.getBilling().setName(objetoPostCarneJuno.getPayerName());
		cobrancaJunoAPI.getBilling().setDocument(objetoPostCarneJuno.getPayerCpfCnpj());
		cobrancaJunoAPI.getBilling().setEmail(objetoPostCarneJuno.getEmail());
		cobrancaJunoAPI.getBilling().setPhone(objetoPostCarneJuno.getPayerPhone());

		AcessTokenJunoAPI accessTokenJunoAPI = this.obterTokenApiJuno();
		if (accessTokenJunoAPI != null) {

			Client client = new HostIgnoreClient("https://api.juno.com.br/").hostIgnoreClient();
			WebResource webResource = client.resource("https://api.juno.com.br/charges");

			ObjectMapper objectMapper = new ObjectMapper();
			String json = objectMapper.writeValueAsString(cobrancaJunoAPI);

			ClientResponse clientResponse = webResource.accept("application/json;charset=UTF-8")
					.header("Content-Type", "application/json;charset=UTF-8").header("X-API-Version", 2)
					.header("X-Resource-Token", ApiTokenIntegracao.TOKEN_PRIVATE_JUNO)
					.header("Authorization", "Bearer " + accessTokenJunoAPI.getAcess_token())
					.post(ClientResponse.class, json);

			String stringRetorno = clientResponse.getEntity(String.class);// Pega o corpo da resposta da API como texto.

			if (clientResponse.getStatus() == 200) { /* Retornou com sucesso */

				clientResponse.close();
				objectMapper
						.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY); /*
																						 * Converte relacionamento um
																						 * para muitos dentro de json
																						 */

				BoletoGeradoApiJuno jsonRetornoObj = objectMapper.readValue(stringRetorno,
						new TypeReference<BoletoGeradoApiJuno>() {
						});

				int recorrencia = 1;

				List<BoletoJuno> boletoJunos = new ArrayList<BoletoJuno>();

				for (ConteudoBoletoJuno c : jsonRetornoObj.get_embedded().getCharges()) {

					BoletoJuno boletoJuno = new BoletoJuno();

					boletoJuno.setEmpresa(vendaCompraLojaVirtual.getEmpresa());
					boletoJuno.setVendaCompraLojaVirtual(vendaCompraLojaVirtual);
					boletoJuno.setCode(c.getCode());
					boletoJuno.setLink(c.getLink());
					boletoJuno.setDataVencimento(new SimpleDateFormat("yyyy-MM-dd")
							.format(new SimpleDateFormat("yyyy-MM-dd").parse(c.getDueDate())));
					boletoJuno.setCheckoutUrl(c.getCheckoutUrl());
					boletoJuno.setValor(new BigDecimal(c.getAmount()));
					boletoJuno.setIdChrBoleto(c.getId());
					boletoJuno.setInstallmentLink(c.getInstallmentLink());
					boletoJuno.setIdPix(c.getPix().getId());
					boletoJuno.setPayloadInBase64(c.getPix().getPayloadInBase64());
					boletoJuno.setImageInBase64(c.getPix().getImageInBase64());
					boletoJuno.setRecorrencia(recorrencia);

					boletoJunos.add(boletoJuno);
					recorrencia++;

				}

				boletoJunoRepository.saveAllAndFlush(boletoJunos);

				return boletoJunos.get(0).getLink();

			} else {
				return stringRetorno;
			}

		} else {
			return "Não exite chave de acesso para a API";
		}

	}
	
	public String cancelarBoleto(String code) throws Exception {
		
		AcessTokenJunoAPI accessTokenJunoAPI = this.obterTokenApiJuno();
		
		Client client = new HostIgnoreClient("https://api.juno.com.br/").hostIgnoreClient();
		WebResource webResource = client.resource("https://api.juno.com.br/charges/"+code+"/cancelation");
		
		ClientResponse clientResponse = webResource.accept(MediaType.APPLICATION_JSON)
				.header("X-Api-Version", 2)
				.header("X-Resource-Token", ApiTokenIntegracao.TOKEN_PRIVATE_JUNO)
				.header("Authorization", "Bearer " + accessTokenJunoAPI.getAcess_token())
				.put(ClientResponse.class);
		
		if (clientResponse.getStatus() == 204) {
			
			boletoJunoRepository.deleteByCode(code);
			
			return "Cancelado com sucesso";
		}
		
		return clientResponse.getEntity(String.class);
		
	}
	

}
