package jdev.mentoria.lojavirtual.service;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.tomcat.util.json.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import jdev.mentoria.lojavirtual.AsaasConfig;
import jdev.mentoria.lojavirtual.model.BoletoJuno;
import jdev.mentoria.lojavirtual.model.VendaCompraLojaVirtual;
import jdev.mentoria.lojavirtual.model.dto.AsaasApiPagamentoStatus;
import jdev.mentoria.lojavirtual.model.dto.ClienteAsaasApiPagamento;
import jdev.mentoria.lojavirtual.model.dto.CobrancaApiAsaas;
import jdev.mentoria.lojavirtual.model.dto.CobrancaGeradaAsaasData;
import jdev.mentoria.lojavirtual.model.dto.CobrancaGeradaAssasApi;
import jdev.mentoria.lojavirtual.model.dto.ObjetoPostCarneJuno;
import jdev.mentoria.lojavirtual.model.dto.ObjetoQrCodePixAssas;
import jdev.mentoria.lojavirtual.repository.BoletoJunoRepository;
import jdev.mentoria.lojavirtual.repository.Vd_cp_Loja_virtual_Repository;
import jdev.mentoria.lojavirtual.util.ValidadorCPF;

@Service
public class ServiceAsaasBoleto implements Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	private Vd_cp_Loja_virtual_Repository vd_Cp_Loja_virt_repository;

	@Autowired
	private BoletoJunoRepository boletoJunoRepository;
	
	 @Autowired
	    private AsaasConfig asaasConfig;
	
	

	public String criarChavePixAsaas() throws Exception {

		Client client = new HostIgnoreClient(AsaasApiPagamentoStatus.URL_API_ASAAS_SANDBOX).hostIgnoreClient();

		WebResource webResource = client.resource(AsaasApiPagamentoStatus.URL_API_ASAAS_SANDBOX + "pix/addressKeys");

		ClientResponse clientResponse = webResource.accept("application/json;charset=UTF-8")
				.header("Content-Type", "application/json")
				.header("access_token", asaasConfig.getApiKeySandbox())
				.post(ClientResponse.class, "{\"type\":\"EVP\"}");

		String retorno = clientResponse.getEntity(String.class);

		System.out.println("STATUS=" + clientResponse.getStatus() + " | BODY=" + retorno);

		clientResponse.close();

		return retorno;

	}

	public String gerarCarneApiAsaas(ObjetoPostCarneJuno objetoPostCarneJuno) throws Exception {

		VendaCompraLojaVirtual vendaCompraLojaVirtual = vd_Cp_Loja_virt_repository
				.findById(objetoPostCarneJuno.getIdVenda()).get();

		/*
		 * ASSAS CRIA UMA COBRANCA COM ESSES DADOS, EU CRIEI O OBJETO Q VOCE PEDIU E
		 * AGORA VOU TE ENVIAR
		 *************************************************************************/

		CobrancaApiAsaas cobrancaApiAsaas = new CobrancaApiAsaas();

		cobrancaApiAsaas.setCustomer(this.buscaClientePessoaApiAssas(objetoPostCarneJuno));

		// pix, boleto ou UNDEFINED
		cobrancaApiAsaas.setBillingType("UNDEFINED");// ( gera boleto e pix)
		cobrancaApiAsaas.setDescription("Pix ou boleto gerado para cobranca:" + vendaCompraLojaVirtual.getId());
		cobrancaApiAsaas.setInstallmentValue(vendaCompraLojaVirtual.getValorTotal().floatValue());
		cobrancaApiAsaas.setInstallmentCount(1);// quantidade de parcelas

		Calendar dataVencimento = Calendar.getInstance();// pega data atual, armaneza em datavencimento
		dataVencimento.add(Calendar.DAY_OF_MONTH, 7);// adicionando 7 dias do mes a data vencimento
		cobrancaApiAsaas.setDueDate(new SimpleDateFormat("yyyy-MM-dd")
				.format(dataVencimento.getTime()));/* formatando conforme a documentação pede */

		cobrancaApiAsaas.getInterest().setValue(1F);

		cobrancaApiAsaas.getFine().setValue(1F);

		/*
		 * ASSAS CRIA UMA COBRANCA COM ESSES DADOS, EU CRIEI O OBJETO Q VOCE PEDIU E
		 * AGORA VOU TE ENVIAR
		 *************************************************************************/

		String json = new ObjectMapper().writeValueAsString(
				cobrancaApiAsaas);/*
									 * converte o objeto para JSON
									 *****************************************************************/

		/*
		 * ENVIANDO PARA ASAAS
		 *******************************************************************************************************************************************/

		Client client = new HostIgnoreClient(AsaasApiPagamentoStatus.URL_API_ASAAS_SANDBOX)
				.hostIgnoreClient();/*
									 * por causa do https pra nao gerar erro de acesso
									 ******************************************************************/

		WebResource webResource = client.resource(AsaasApiPagamentoStatus.URL_API_ASAAS_SANDBOX + "payments");

		ClientResponse clientResponse = webResource.accept("application/json;charset=UTF-8")
				.header("Content-Type", "application/json")
				.header("access_token", asaasConfig.getApiKeySandbox()).post(ClientResponse.class, json);

		String stringRetorno = clientResponse.getEntity(String.class);
		clientResponse.close();

		/*
		 * ENVIANDO PARA ASAAS
		 *******************************************************************************************************************************************/

		/* ASAAS RESPONDEU */

		// ************ BUSCANDO PARCELAS GERADAS

		LinkedHashMap<String, Object> parser = new JSONParser(stringRetorno).parseObject();

		String installment = parser.get("installment").toString();

		Client clientParcelas = new HostIgnoreClient(AsaasApiPagamentoStatus.URL_API_ASAAS).hostIgnoreClient();
		WebResource webResourceParcelas = clientParcelas
				.resource(AsaasApiPagamentoStatus.URL_API_ASAAS_SANDBOX + "payments?installment=" + installment);

		ClientResponse clientResponseParcelas = webResourceParcelas.accept("application/json;charset=UTF-8")
				.header("Content-Type", "application/json")
				.header("access_token", asaasConfig.getApiKeySandbox()).get(ClientResponse.class);

		String stringRetornoClienteParcelas = clientResponseParcelas.getEntity(String.class);

		clientResponseParcelas.close();

		// ************ BUSCANDO PARCELAS GERADAS

		/* ASAAS RESPONDEU */

		/* converte todo o json de retorno para o nosso objeto */

		ObjectMapper objectMapper = new ObjectMapper();

		objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

		CobrancaGeradaAssasApi listaCobranca = objectMapper.readValue(stringRetornoClienteParcelas,
				new TypeReference<CobrancaGeradaAssasApi>() {
				});

		/* converte todo o json de retorno para o nosso objeto */

		List<BoletoJuno> boletoJunos = new ArrayList<BoletoJuno>();

		int recorrencia = 1;

		for (CobrancaGeradaAsaasData data : listaCobranca.getData()) {

			BoletoJuno boletoJuno = new BoletoJuno();

			boletoJuno.setEmpresa(vendaCompraLojaVirtual.getEmpresa());
			boletoJuno.setVendaCompraLojaVirtual(vendaCompraLojaVirtual);
			boletoJuno.setCode(data.getId());
			boletoJuno.setLink(data.getInvoiceUrl());
			boletoJuno.setDataVencimento(new SimpleDateFormat("yyyy-MM-dd")
					.format(new SimpleDateFormat("yyyy-MM-dd").parse(data.getDueDate())));
			boletoJuno.setCheckoutUrl(data.getInvoiceUrl());
			boletoJuno.setValor(data.getValue());
			boletoJuno.setIdChrBoleto(data.getId());
			boletoJuno.setInstallmentLink(data.getInvoiceUrl());
			boletoJuno.setRecorrencia(recorrencia);

			// boletoJuno.setIdPix(c.getPix().getId());

			ObjetoQrCodePixAssas codePixAssas = this.buscarQrCodeCodigoPix(data.getId());

			boletoJuno.setPayloadInBase64(codePixAssas.getPayload());
			boletoJuno.setImageInBase64(codePixAssas.getEncodedImage());
			boletoJunos.add(boletoJuno);
			recorrencia++;
		}

		boletoJunoRepository.saveAllAndFlush(boletoJunos);

		return boletoJunos.get(0).getCheckoutUrl();

	}

	public ObjetoQrCodePixAssas buscarQrCodeCodigoPix(String idCobranca) throws Exception {

		Client client = new HostIgnoreClient(AsaasApiPagamentoStatus.URL_API_ASAAS).hostIgnoreClient();
		WebResource webResource = client
				.resource(AsaasApiPagamentoStatus.URL_API_ASAAS_SANDBOX + "payments/" + idCobranca + "/pixQrCode");

		ClientResponse clientResponse = webResource.accept("application/json;charset=UTF-8")
				.header("Content-Type", "application/json")
				.header("access_token", asaasConfig.getApiKeySandbox()).get(ClientResponse.class);

		String stringRetornoCliente = clientResponse.getEntity(String.class);

		clientResponse.close();

		ObjetoQrCodePixAssas qrCodePixAssas = new ObjetoQrCodePixAssas();

		LinkedHashMap<String, Object> parser = new JSONParser(stringRetornoCliente).parseObject();

		qrCodePixAssas.setEncodedImage(parser.get("encodedImage").toString());

		qrCodePixAssas.setPayload(parser.get("payload").toString());

		return qrCodePixAssas;
	}

	
	
	
	/**
	 * Busca um cliente na API do Asaas pelo email informado e, caso ele não exista,
	 * cria um novo cliente na plataforma.
	 *
	 * <p>Função desse método no sistema:</p>
	 * <p>Antes de gerar uma cobrança, boleto, carnê ou pagamento no Asaas,
	 * normalmente o sistema precisa saber quem é o cliente dentro da plataforma.
	 * Esse método faz exatamente isso: ele tenta localizar o cliente pelo email.
	 * Se encontrar, reaproveita o cadastro existente. Se não encontrar,
	 * cria um novo cadastro e devolve o id gerado.</p>
	 *
	 * <p>Em linguagem simples:</p>
	 * <p>Esse método garante que o cliente exista no Asaas antes de continuar
	 * o processo de cobrança.</p>
	 *
	 * <p>Fluxo geral:</p>
	 * <ol>
	 *   <li>Consulta a API do Asaas usando o email do cliente</li>
	 *   <li>Verifica se já existe cadastro</li>
	 *   <li>Se não existir, monta um objeto de cliente e envia para criação</li>
	 *   <li>Se já existir, pega o id do primeiro cliente encontrado</li>
	 *   <li>Retorna o id do cliente no Asaas</li>
	 * </ol>
	 *
	 * @param dados objeto com os dados do cliente que serão usados
	 *              para consulta ou criação no Asaas.
	 *              Exemplo: email, nome, CPF/CNPJ e telefone.
	 *
	 * @return uma {@link String} com o id do cliente no Asaas.
	 *         Esse id será usado depois para vincular cobranças a esse cliente.
	 *
	 * @throws Exception pode lançar exceção caso ocorra erro de comunicação
	 *                   com a API, erro de conversão de JSON ou qualquer outro
	 *                   erro durante o processo.
	 *
	 * <p>Exemplo prático:</p>
	 * <p>Se o cliente "joao@email.com" já existir no Asaas,
	 * o método só retorna o id dele.
	 * Se não existir, ele cria esse cliente e retorna o novo id.</p>
	 */
	public String buscaClientePessoaApiAssas(ObjetoPostCarneJuno dados) throws Exception {

		/*
		 * Guarda o id do cliente dentro do Asaas.
		 *
		 * Esse id será o resultado final do método e servirá para relacionar
		 * o cliente com futuras cobranças.
		 */
		String customer_id = "";// id do cliente para ligar com a cobranca

		/*
		 * -------------------- INICIO *********** criando ou consultando o cliente
		 *
		 * A ideia aqui é:
		 * primeiro consultar se o cliente já existe.
		 * Só cria um novo se realmente não encontrar.
		 */

		/*
		 * Cria um client HTTP ignorando validações de host.
		 *
		 * Pelo nome da classe HostIgnoreClient, parece que ela foi criada
		 * para evitar problemas com certificado/host em ambiente de teste
		 * ou integração.
		 */
		Client client = new HostIgnoreClient(AsaasApiPagamentoStatus.URL_API_ASAAS_SANDBOX).hostIgnoreClient();

		/*
		 * Monta o endpoint de consulta do cliente no Asaas.
		 *
		 * Aqui a busca está sendo feita pelo email:
		 * customers?email=emailDoCliente
		 *
		 * Exemplo:
		 * .../customers?email=joao@email.com
		 */
		WebResource webResource = client
				.resource(AsaasApiPagamentoStatus.URL_API_ASAAS_SANDBOX + "customers?email=" + dados.getEmail());

		/*
		 * Faz a requisição GET para a API.
		 *
		 * accept(...)          -> informa que espera JSON na resposta
		 * header Content-Type  -> informa o tipo de conteúdo enviado/esperado
		 * header access_token  -> autentica a chamada na API do Asaas
		 * get(...)             -> executa a consulta
		 */
		ClientResponse clientResponse = webResource.accept("application/json;charset=UTF-8")
				.header("Content-Type", "application/json")
				.header("access_token", asaasConfig.getApiKeySandbox()).get(ClientResponse.class);

		/*
		 * Lê o JSON da resposta e converte para LinkedHashMap.
		 *
		 * Em vez de mapear para um DTO fixo, aqui o código optou por
		 * fazer um parser genérico do JSON.
		 */
		LinkedHashMap<String, Object> parser = new JSONParser(clientResponse.getEntity(String.class)).parseObject();

		/*
		 * Fecha a resposta HTTP para liberar recursos.
		 */
		clientResponse.close();

		/*
		 * Pega o campo totalCount do retorno da API.
		 *
		 * Esse campo informa quantos clientes foram encontrados
		 * com esse email.
		 */
		Integer total = Integer.parseInt(parser.get("totalCount").toString());

		/*
		 * Se total for menor ou igual a zero, significa que não encontrou cliente.
		 * Então será necessário criar um novo cadastro no Asaas.
		 */
		if (total <= 0) {// cria o cliente

			/*
			 * Cria o objeto que representa o cliente a ser enviado ao Asaas.
			 */
			ClienteAsaasApiPagamento clienteAsaasApiPagamento = new ClienteAsaasApiPagamento();

			/*
			 * Valida o CPF/CNPJ informado.
			 *
			 * Se for inválido, usa um CPF fixo de fallback.
			 *
			 * Observação didática:
			 * isso evita que a API rejeite o cadastro por documento inválido,
			 * mas em sistema real isso merece atenção, porque usar CPF fixo
			 * pode mascarar erro de dado cadastral.
			 */
			if (!ValidadorCPF.validar(dados.getPayerCpfCnpj())) {

				clienteAsaasApiPagamento.setCpfCnpj("30223625019");
			} else {

				/*
				 * Se o CPF/CNPJ estiver válido, usa o valor vindo nos dados.
				 */
				clienteAsaasApiPagamento.setCpfCnpj(dados.getPayerCpfCnpj());
			}

			/*
			 * Define o email do cliente.
			 */
			clienteAsaasApiPagamento.setEmail(dados.getEmail());

			/*
			 * Define o nome do cliente.
			 */
			clienteAsaasApiPagamento.setName(dados.getPayerName());

			/*
			 * Define o telefone do cliente.
			 */
			clienteAsaasApiPagamento.setPhone(dados.getPayerPhone());

			/*
			 * Cria um novo client HTTP para enviar o cadastro.
			 *
			 * Aqui o nome da constante chama atenção:
			 * foi usado URL_API_ASAAS no construtor,
			 * mas depois a resource aponta para URL_API_ASAAS_SANDBOX.
			 *
			 * Isso pode ser intencional ou pode ser um detalhe a revisar.
			 */
			Client clientCria = new HostIgnoreClient(AsaasApiPagamentoStatus.URL_API_ASAAS).hostIgnoreClient();

			/*
			 * Monta o endpoint de criação do cliente.
			 */
			WebResource webResourceCria = clientCria
					.resource(AsaasApiPagamentoStatus.URL_API_ASAAS_SANDBOX + "customers");

			/*
			 * Faz a chamada POST para criar o cliente no Asaas.
			 *
			 * new ObjectMapper().writeValueAsBytes(clienteAsaasApiPagamento)
			 * transforma o objeto Java em JSON para enviar no corpo da requisição.
			 */
			ClientResponse clientResponseCria = webResourceCria.accept("application/json;charset=UTF-8")
					.header("Content-Type", "application/json")
					.header("access_token", asaasConfig.getApiKeySandbox())
					.post(ClientResponse.class, new ObjectMapper().writeValueAsBytes(clienteAsaasApiPagamento));

			/*
			 * Lê a resposta do cadastro criado e converte para Map.
			 */
			LinkedHashMap<String, Object> parserCria = new JSONParser(clientResponseCria.getEntity(String.class))
					.parseObject();

			/*
			 * Fecha a resposta HTTP da criação.
			 */
			clientResponseCria.close();

			/*
			 * Pega o id do cliente recém-criado.
			 *
			 * Esse id é o dado mais importante do retorno,
			 * porque será usado para vincular cobranças a essa pessoa.
			 */
			customer_id = parserCria.get("id").toString();

		} else {// ja tem cliente cadastrado

			/*
			 * Se já existe cliente cadastrado, pega a lista "data"
			 * retornada pela API.
			 *
			 * Normalmente o Asaas devolve os clientes encontrados
			 * dentro de um array chamado "data".
			 */
			List<Object> data = (List<Object>) parser.get("data");

			/*
			 * Pega o primeiro cliente da lista,
			 * transforma em JSON com Gson,
			 * acessa o campo "id"
			 * e remove as aspas.
			 *
			 * Em resumo:
			 * aqui ele está extraindo o id do primeiro cliente encontrado.
			 */
			customer_id = new Gson().toJsonTree(data.get(0)).getAsJsonObject().get("id").toString().replaceAll("\"",
					"");

		}

		/*
		 * Retorna o id do cliente encontrado ou criado.
		 */
		return customer_id;
	}

}
