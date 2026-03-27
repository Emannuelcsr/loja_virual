package jdev.mentoria.lojavirtual;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TestListarAgencia {
	
	public static void main(String[] args) throws IOException {
		
		OkHttpClient client = new OkHttpClient();

		Request request = new Request.Builder()
		  .url("https://sandbox.melhorenvio.com.br/api/v2/me/shipment/agencies")
		  .get()
		  .addHeader("accept", "application/json")
		  .addHeader("Authorization", "Bearer " + ApiTokenIntegracao.TOKEN_SANDBOX_MELHOR_ENVIO)
	      .addHeader("User-Agent", "eu@eu.com.br")
		  .build();

		Response response = client.newCall(request).execute();
		String respostaAgencias = response.body().string();
		System.out.println("RESPOSTA AGENCIAS = " + respostaAgencias);
	}

}
