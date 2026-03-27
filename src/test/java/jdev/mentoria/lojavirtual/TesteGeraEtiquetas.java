package jdev.mentoria.lojavirtual;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TesteGeraEtiquetas {

	public static void main(String[] args) throws IOException {

		OkHttpClient client = new OkHttpClient();

		MediaType mediaType = MediaType.parse("application/json");
		RequestBody body = RequestBody.create(mediaType, """
				{
				  "orders": [
				    "a13ce1b1-7856-4b75-a1cf-724bf6f3a908"
				  ]
				}
						""");

		Request request = new Request.Builder().url("https://sandbox.melhorenvio.com.br/api/v2/me/shipment/generate")
				.post(body).addHeader("Accept", "application/json").addHeader("Content-Type", "application/json")
				.addHeader("Authorization", "Bearer "+ApiTokenIntegracao.TOKEN_SANDBOX_MELHOR_ENVIO)
				.addHeader("User-Agent", "eu@eu.com.br").build();

		Response response = client.newCall(request).execute();
		System.out.println(response.code());
		System.out.println(response.body().string());
	}

}
