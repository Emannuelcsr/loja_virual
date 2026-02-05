package jdev.mentoria.lojavirtual.security;

import java.io.IOException;
import java.util.Date;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class JWTTokenAutenticacaoService {

	private static final long EXPIRATION_TIME = 259990000;

	private static final String SECRET = "senhaqqeruma";

	private static final String TOKEN_PREFIX = "Bearer ";

	private static final String HEADER_STRING = "Authorization";

	public void addAuthentication(HttpServletResponse response, String username) throws IOException {
		
		String JWT = Jwts.builder().
				setSubject(username)
				.setExpiration(new Date (System.currentTimeMillis() + EXPIRATION_TIME ))
				.signWith(SignatureAlgorithm.HS512, SECRET)
				.compact();
		
		String token = TOKEN_PREFIX + JWT;
		
		response.addHeader(HEADER_STRING, token);
		
		
		response.getWriter().write("{\"Authorization\": \"" + token + "\"}");
		
	}
}
