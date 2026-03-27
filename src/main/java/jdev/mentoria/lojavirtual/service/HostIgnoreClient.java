package jdev.mentoria.lojavirtual.service;

import java.io.Serializable;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.glassfish.jersey.media.multipart.internal.MultiPartWriter;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.client.urlconnection.HTTPSProperties;

/**
 * Classe responsável por criar um Client (Jersey) que ignora validações de SSL/TLS.
 *
 * <p>
 * Normalmente quando uma aplicação Java faz requisição HTTPS para uma API,
 * o Java valida o certificado digital do servidor.
 * </p>
 *
 * <p>
 * Durante desenvolvimento algumas APIs utilizam certificados de teste
 * ou certificados autoassinados. Nesses casos o Java bloqueia a conexão.
 * </p>
 *
 * <p>
 * Esta classe cria um Client configurado para ignorar essas validações,
 * permitindo que o sistema consiga conversar com a API mesmo com
 * certificados inválidos.
 * </p>
 *
 * <p><b>IMPORTANTE:</b> esse tipo de configuração é usado apenas em ambiente
 * de desenvolvimento. Em produção o correto é validar o certificado.</p>
 */
public class HostIgnoreClient implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Nome do host que terá a verificação de SSL ignorada.
	 */
	private String hostName;

	/**
	 * Construtor que recebe o host da API que será acessada.
	 *
	 * @param hostName nome do host da API
	 */
	public HostIgnoreClient(String hostName) {
		this.hostName = hostName;
	}

	/**
	 * Cria um Client Jersey configurado para ignorar validações SSL.
	 *
	 * <p>Passos executados neste método:</p>
	 * <ol>
	 * <li>Cria um TrustManager que aceita qualquer certificado.</li>
	 * <li>Cria um SSLContext usando esse TrustManager.</li>
	 * <li>Configura o HostnameVerifier para ignorar verificação do host.</li>
	 * <li>Configura o Client do Jersey para usar essas propriedades HTTPS.</li>
	 * <li>Adiciona suporte para JSON (Jackson).</li>
	 * <li>Adiciona suporte para multipart (upload de arquivos).</li>
	 * </ol>
	 *
	 * @return Client configurado para ignorar SSL
	 * @throws Exception caso ocorra erro na criação do contexto SSL
	 */
	public Client hostIgnoreClient() throws Exception {

		/**
		 * TrustManager responsável por aceitar qualquer certificado SSL.
		 * Os métodos de verificação ficam vazios, portanto nenhum certificado
		 * será rejeitado.
		 */
		TrustManager[] trustManagers = new TrustManager[] {

				new X509TrustManager() {

					@Override
					public X509Certificate[] getAcceptedIssuers() {
						return null;
					}

					@Override
					public void checkServerTrusted(X509Certificate[] chain, String authType)
							throws CertificateException {

					}

					@Override
					public void checkClientTrusted(X509Certificate[] chain, String authType)
							throws CertificateException {

					}
				} };

		/**
		 * Cria um contexto SSL utilizando protocolo TLS.
		 */
		SSLContext sslContext = SSLContext.getInstance("TLS");

		/**
		 * Inicializa o contexto SSL usando o TrustManager que aceita tudo.
		 */
		sslContext.init(null, trustManagers, new SecureRandom());

		/**
		 * Lista de hosts que terão a validação ignorada.
		 */
		Set<String> hostNameList = new HashSet<String>();

		hostNameList.add(this.hostName);

		/**
		 * Define um verificador de hostname personalizado.
		 */
		HttpsURLConnection.setDefaultHostnameVerifier(new IgnoreHostNameSSL(hostNameList));

		/**
		 * Define o socket SSL padrão usando o contexto criado.
		 */
		HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());

		/**
		 * Configuração base do client Jersey.
		 */
		DefaultClientConfig config = new DefaultClientConfig();

		Map<String, Object> properties = config.getProperties();

		/**
		 * Propriedades HTTPS usadas pelo Jersey Client.
		 */
		HTTPSProperties httpsProperties = new HTTPSProperties(new HostnameVerifier() {

			@Override
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		}, sslContext);

		properties.put(HTTPSProperties.PROPERTY_HTTPS_PROPERTIES, httpsProperties);

		/**
		 * Registra suporte para JSON usando Jackson.
		 */
		config.getClasses().add(JacksonJsonProvider.class);

		/**
		 * Registra suporte para multipart (upload de arquivos).
		 */
		config.getClasses().add(MultiPartWriter.class);

		/**
		 * Cria e retorna o Client configurado.
		 */
		return Client.create(config);

	}

}