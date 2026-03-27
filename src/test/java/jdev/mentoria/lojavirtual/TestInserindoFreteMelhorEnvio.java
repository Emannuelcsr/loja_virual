package jdev.mentoria.lojavirtual;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TestInserindoFreteMelhorEnvio {
	public static void main(String[] args) throws IOException {

		OkHttpClient client = new OkHttpClient();

		MediaType mediaType = MediaType.parse("application/json");
		RequestBody body = RequestBody.create(mediaType, """
				{
				  "service": "2",
				  "from": {
				    "name": "Loja Teste",
				    "address": "Rua das Flores",
				    "document":"06826774933",
				    "city": "Pelotas",
				    "postal_code": "96020360"
				  },
				  "to": {
				    "name": "Cliente Teste",
				    "document":"84172575005",
				    "address": "Praca da Se",
				    "city": "Sao Paulo",
				    "postal_code": "01018020"
				  },
				  "products": [
				    {
				      "name": "Produto A",
				      "quantity": 1,
				      "unitary_value": 10.10
				    }
				  ],
				  "volumes": [
				    {
				      "weight": 1.0,
				      "length": 11,
				      "width": 11,
				      "height": 17
				    }
				  ],
				  "options": {
				    "receipt": true,
				    "own_hand": true,
				    "reverse": true,
				    "non_commercial": true
				  }
				}
				""");
		Request request = new Request.Builder().url("https://sandbox.melhorenvio.com.br/api/v2/me/cart").post(body)
				.addHeader("Accept", "application/json").addHeader("Content-Type", "application/json")
				.addHeader("Authorization", "Bearer " + ApiTokenIntegracao.TOKEN_SANDBOX_MELHOR_ENVIO)
				.addHeader("User-Agent", "eu@eu.com").build();

		Response response = client.newCall(request).execute();
		System.out.println(response.code());
		System.out.println(response.body().string());
	}

}
