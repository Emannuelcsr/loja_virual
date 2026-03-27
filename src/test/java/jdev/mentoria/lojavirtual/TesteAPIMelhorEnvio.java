package jdev.mentoria.lojavirtual;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jdev.mentoria.lojavirtual.model.dto.EmpresaTransporteDTO;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Classe de teste usada para chamar a API do Melhor Envio (sandbox) e imprimir
 * o resultado.
 *
 * <p>
 * Ela monta um JSON com os dados do cálculo de frete, envia via POST para o
 * endpoint de cálculo e imprime no console o HTTP status e o body retornado.
 * </p>
 *
 * <p>
 * Em termos simples: essa classe é um “mini Postman em Java” para validar se: o
 * token está funcionando, a URL está correta e o payload está no formato
 * esperado.
 * </p>
 */
public class TesteAPIMelhorEnvio {

	/**
	 * Tipo de conteúdo JSON usado no corpo da requisição.
	 */
	private static final MediaType JSON = MediaType.parse("application/json");

	/**
	 * Executa o teste de cálculo de frete no Melhor Envio (sandbox).
	 *
	 * <p>
	 * Fluxo:
	 * </p>
	 * <p>
	 * 1) monta o JSON do cálculo<br>
	 * 2) cria o RequestBody<br>
	 * 3) cria a requisição com headers (Accept, Authorization, User-Agent)<br>
	 * 4) executa e imprime o retorno
	 * </p>
	 *
	 * @param args não utilizado.
	 * @throws IOException caso ocorra falha de rede/HTTP.
	 */
	public static void main(String[] args) throws IOException {

		// ------------------------------------------------------------
		// 1) Payload JSON: dados que a API precisa para calcular o frete
		// ------------------------------------------------------------

		// JSON com CEP origem/destino, produtos (peso/dimensões), opções e serviços
		String json = """
				{
				  "from": { "postal_code": "96020360" },
				  "to": { "postal_code": "01018020" },
				  "products": [
				    {
				      "id": "Produto A",
				      "width": 11,
				      "height": 17,
				      "length": 11,
				      "weight": 1,
				      "insurance_value": 10.1,
				      "quantity": 1
				    },
				    {
				      "id": "Produto B",
				      "width": 10,
				      "height": 10,
				      "length": 12,
				      "weight": 0.2,
				      "insurance_value": 10.1,
				      "quantity": 5
				    }
				  ],
				  "options": {
				    "receipt": false,
				    "own_hand": false
				  },
				  "services": "1,2,18"
				}
				""";

		// Transforma o JSON em corpo de requisição (RequestBody) do OkHttp
		RequestBody body = RequestBody.create(json, JSON);

		// ------------------------------------------------------------
		// 2) Cria o cliente HTTP
		// ------------------------------------------------------------

		// Cliente HTTP que executa as chamadas
		OkHttpClient client = new OkHttpClient();

		// ------------------------------------------------------------
		// 3) Monta a requisição HTTP
		// ------------------------------------------------------------

		// Monta a URL final do endpoint de cálculo
		String url = ApiTokenIntegracao.URL_MELHOR_ENVIO_SANDBOX + "api/v2/me/shipment/calculate";

		// Cria a requisição com método POST + headers exigidos pela API
		Request request = new Request.Builder().url(url) // Endpoint do cálculo de frete
				.post(body) // Envia o JSON no corpo
				.addHeader("Accept", "application/json") // Espera resposta JSON
				.addHeader("Content-Type", "application/json") // Envia JSON
				.addHeader("Authorization", "Bearer " + ApiTokenIntegracao.TOKEN_SANDBOX_MELHOR_ENVIO) // Token de
																										// acesso
				.addHeader("User-Agent", "suporte@manel.com.br") // Identificação do cliente
				.build();

		// ------------------------------------------------------------
		// 4) Executa a chamada e imprime a resposta
		// ------------------------------------------------------------

		// try-with-resources garante que a resposta será fechada corretamente
		Response resposta = client.newCall(request).execute();

		JsonNode jsonNode = new ObjectMapper().readTree(resposta.body().string());


		List<EmpresaTransporteDTO> transporteDTOs = new ArrayList<EmpresaTransporteDTO>();
				
	
		for (JsonNode node : jsonNode) {
			EmpresaTransporteDTO dto = new EmpresaTransporteDTO();

			if (node.get("id") != null) {

				dto.setId(node.get("id").asText());
			}
			if (node.get("name") != null) {
				dto.setName(node.get("name").asText());
			}
			if (node.get("price") != null) {
				dto.setPrice(node.get("price").asText());
			}
			if (node.get("company") != null) {
				dto.setCompany(node.get("company").get("name").asText());
				dto.setPicture(node.get("company").get("picture").asText());
				
			}
			if (dto.dadosOk()) {
				transporteDTOs.add(dto);
			}
		}
				

	}
}
/*
 * ===================== EXPLICAÇÃO DIDÁTICA =====================
 *
 * Essa classe foi enxugada para ficar só com o que importa: montar o payload,
 * fazer o POST e imprimir o retorno.
 *
 * O OkHttp funciona como um cliente HTTP. A diferença entre “post(null)” e
 * “post(body)” é enorme: - post(null) envia POST sem corpo (muitas APIs
 * recusam) - post(body) envia o JSON que a API precisa para calcular o frete
 *
 * Aqui o JSON tem: - from/to: CEP de origem e destino - products: lista com
 * dimensões, peso e quantidade - options: opções extras do envio - services:
 * serviços de frete que você quer cotar
 *
 * O header Authorization é obrigatório: sem ele, você recebe 401 (não
 * autorizado).
 *
 * O print do HTTP e do BODY serve para você validar rapidamente: - se o token
 * está certo - se a URL está certa - se o JSON está no formato esperado
 *
 * Em resumo: este código é um teste rápido de integração para “confirmar que a
 * API responde”.
 * ===============================================================
 */
