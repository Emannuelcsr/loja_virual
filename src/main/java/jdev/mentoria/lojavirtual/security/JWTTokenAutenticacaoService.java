package jdev.mentoria.lojavirtual.security;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

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

    
    
    
    
    /**
     * Gera um token JWT para o usuário autenticado e adiciona esse token
     * na resposta HTTP que será enviada para o frontend.
     *
     * Esse método é chamado depois que o login foi validado com sucesso.
     *
     * @param response objeto de resposta HTTP usado para enviar dados ao frontend
     * @param username login/nome do usuário autenticado
     * @throws IOException caso ocorra erro ao escrever a resposta para o frontend
     */
    public void addAuthentication(HttpServletResponse response, String username) throws IOException {

        /*
         * Cria o JWT puro.
         *
         * Aqui ainda não existe o prefixo "Bearer ".
         * O token JWT é gerado com:
         * - subject: identificação do usuário
         * - expiration: data de expiração
         * - assinatura: chave secreta do sistema
         */
        String jwt = Jwts.builder()

                /*
                 * Define o "dono" do token.
                 *
                 * O subject normalmente guarda o login, e-mail ou username do usuário.
                 * Depois, quando o backend receber esse token novamente,
                 * ele poderá extrair esse username de dentro do JWT.
                 */
                .setSubject(username)

                /*
                 * Define quando o token vai expirar.
                 *
                 * System.currentTimeMillis() pega o horário atual em milissegundos.
                 * EXPIRATION_TIME soma o tempo de validade do token.
                 *
                 * Exemplo:
                 * agora + 1 dia = token válido até amanhã.
                 */
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))

                /*
                 * Assina o token usando a chave secreta do sistema.
                 *
                 * Essa assinatura garante que o token não foi alterado.
                 * Se alguém modificar o token manualmente, a assinatura deixa de bater.
                 */
                .signWith(getSigningKey())

                /*
                 * Finaliza a construção do JWT e transforma tudo em uma String.
                 *
                 * O resultado é algo parecido com:
                 * eyJhbGciOiJIUzI1NiJ9...
                 */
                .compact();

        /*
         * Adiciona o prefixo antes do JWT.
         *
         * Normalmente TOKEN_PREFIX vale "Bearer ".
         *
         * Então o resultado final fica:
         * Bearer eyJhbGciOiJIUzI1NiJ9...
         */
        String token = TOKEN_PREFIX + jwt;

        /*
         * Adiciona o token no cabeçalho da resposta HTTP.
         *
         * Normalmente HEADER_STRING vale "Authorization".
         *
         * Então a resposta volta com um header assim:
         * Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
         */
        response.addHeader(HEADER_STRING, token);

        /*
         * Informa que o corpo da resposta será um JSON.
         *
         * Isso ajuda o frontend a entender que a resposta não é texto comum,
         * mas sim um objeto JSON.
         */
        response.setContentType("application/json");

        /*
         * Define a codificação da resposta como UTF-8.
         *
         * Isso evita problemas com acentos e caracteres especiais.
         */
        response.setCharacterEncoding("UTF-8");

        /*
         * Escreve o JSON no corpo da resposta.
         *
         * Esse JSON será recebido pelo Angular no login.
         *
         * Exemplo de resposta:
         * {
         *   "Authorization": "Bearer eyJhbGciOiJIUzI1NiJ9...",
         *   "username": "emannuel"
         * }
         */
        Map<String, Object> body = new HashMap<>();

        body.put("Authorization", token);
        body.put("username", username);
       
        
        Usuario usuario = usuarioRepository.findUserByLogin(username);
        
        Long usuarioEmpresa = usuario.getEmpresa().getId();
        
       body.put("idUsuarioEmpresa", usuarioEmpresa);

       String json = new ObjectMapper().writeValueAsString(body);

        response.getWriter().write(json);
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
