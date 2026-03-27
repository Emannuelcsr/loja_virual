package jdev.mentoria.lojavirtual.service;

import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Set;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class IgnoreHostNameSSL implements HostnameVerifier {

	private static final HostnameVerifier defaultHostNameVerifier = HttpsURLConnection.getDefaultHostnameVerifier();

	private Set<String> trustedHosts;

	public IgnoreHostNameSSL(Set<String> trustedHosts) {
		this.trustedHosts = trustedHosts;

	}

	@Override
	public boolean verify(String hostname, SSLSession session) {

		if (trustedHosts.contains(hostname)) {

			return true;
		} else {

			return defaultHostNameVerifier.verify(hostname, session);
		}

	}

	public static HostnameVerifier getHostnameVerifier() throws Exception {

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
				}

		};

		SSLContext sslContext = SSLContext.getInstance("SSL");

		sslContext.init(null, trustManagers, new SecureRandom());

		HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());

		HostnameVerifier hostnameVerifier = new HostnameVerifier() {

			@Override
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		};

		return hostnameVerifier;
	}

}
