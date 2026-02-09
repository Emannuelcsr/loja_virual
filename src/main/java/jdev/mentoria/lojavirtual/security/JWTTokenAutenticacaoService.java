package jdev.mentoria.lojavirtual.security;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jdev.mentoria.lojavirtual.model.Usuario;
import jdev.mentoria.lojavirtual.repository.UsuarioRepository;

@Service
public class JWTTokenAutenticacaoService {

    private static final long EXPIRATION_TIME = 259990000;

    /**
     * Segredo do JWT.
     * - Pode ter "_" e qualquer caractere normal.
     * - O importante é ter tamanho suficiente (pra HS512, 64+ bytes é o ideal).
     */
    private static final String SECRET =
            "chave_super_secreta_de_no_minimo_64_caracteres_para_jwt_em_2025_abc123@!";

    private static final String TOKEN_PREFIX = "Bearer ";

    private static final String HEADER_STRING = "Authorization";

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Cria a chave de assinatura (HMAC) a partir do SECRET.
     * Aqui o SECRET NÃO é tratado como Base64, e sim como bytes normais (UTF-8).
     */
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    }

    public void addAuthentication(HttpServletResponse response, String username) throws IOException {

        String jwt = Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSigningKey()) // ✅ jeito moderno
                .compact();

        String token = TOKEN_PREFIX + jwt;

        response.addHeader(HEADER_STRING, token);

        // (você já retorna também no body, então mantive)
        response.getWriter().write("{\"Authorization\": \"" + token + "\"}");
    }

    public Authentication getAuthentication(HttpServletResponse response, HttpServletRequest request) {

        String token = request.getHeader(HEADER_STRING);

        if (token != null) {

            // pega o token limpo, sem o "Bearer "
            String tokenLimpo = token.replace(TOKEN_PREFIX, "").trim();

            // pega o usuario do token, decodificando o codigo do token
            String user = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey()) // ✅ jeito moderno
                    .build()
                    .parseClaimsJws(tokenLimpo)
                    .getBody()
                    .getSubject();

            if (user != null) {

                Usuario usuario = usuarioRepository.findUserByLogin(user);

                if (usuario != null) {
                    return new UsernamePasswordAuthenticationToken(
                            usuario.getUsername(),
                            usuario.getSenha(),
                            usuario.getAuthorities()
                    );
                }
            }
        }

        return null;
    }
}
