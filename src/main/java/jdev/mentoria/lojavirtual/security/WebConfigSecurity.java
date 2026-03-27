package jdev.mentoria.lojavirtual.security;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


// CORS (Cross-Origin Resource Sharing)
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * Configuração central do Spring Security (estilo moderno).
 *
 * Aqui você controla:
 * - Como o Spring valida senhas (PasswordEncoder)
 * - Como o Spring consegue um AuthenticationManager
 * - Quais rotas são públicas ou protegidas
 * - E principalmente: CORS (para o Angular conseguir chamar a API sem o navegador bloquear)
 *
 * Observação importante:
 * CORS é uma regra do NAVEGADOR, não do Spring.
 * Postman não tem CORS, por isso "funciona no Postman e dá erro no Angular".
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class WebConfigSecurity {

	
	private final JWTTokenAutenticacaoService jwtTokenAutenticacaoService;

	public WebConfigSecurity(JWTTokenAutenticacaoService jwtTokenAutenticacaoService) {
	    this.jwtTokenAutenticacaoService = jwtTokenAutenticacaoService;
	}
	
	
    /**
     * Define como as senhas serão comparadas na autenticação.
     *
     * Na prática:
     * - quando o usuário faz login, o Spring pega a senha digitada
     * - criptografa/valida com BCrypt
     * - compara com a senha criptografada no banco
     *
     * Sem isso, você cai em erro de "There is no PasswordEncoder mapped..." ou login falha.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
 
       
    
    /**
     * Cadeia principal de segurança.
     *
     * Tudo que chega na API passa por aqui antes de ir para seu Controller.
     *
     * Onde entra o CORS?
     * - O navegador (Angular) só deixa chamar a API se a API "autorizar" no cabeçalho.
     * - Quem garante isso aqui é: http.cors(...)
     * - e as regras ficam no bean corsConfigurationSource().
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationConfiguration authConfig) throws Exception {

    	
    	AuthenticationManager authenticationManager = authConfig.getAuthenticationManager();

    	
    	JWTLoginFilter jwtLoginFilter =
    	        new JWTLoginFilter("/login", authenticationManager, jwtTokenAutenticacaoService);

    	JWTApiAuthenticationFilter jwtApiFilter =
    	        new JWTApiAuthenticationFilter(jwtTokenAutenticacaoService);
        http
            /**
             * CSRF:
             * - Para APIs REST stateless (principalmente com JWT), normalmente desabilita CSRF.
             * - CSRF é mais ligado a sessão/cookie em aplicações web tradicionais.
             */
            .csrf(csrf -> csrf.disable())

            /**
             * LIGA O CORS dentro do Spring Security.
             *
             * Isso é o ponto mais importante:
             * - Se você configurar CORS e não ligar aqui, o Security pode bloquear antes do CORS aplicar.
             * - Com isso, o Security passa a usar as regras definidas em corsConfigurationSource().
             */
            .cors(Customizer.withDefaults())

            /**
             * ✅ JWT = STATELESS (sem sessão)
             *
             * Por que isso importa?
             * - Com JWT, cada requisição se autentica pelo token.
             * - Você não quer o servidor guardando sessão de login.
             */
            .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            /**
             * Regras de acesso às rotas:
             * - Primeiro permitimos OPTIONS para o navegador conseguir fazer "preflight".
             * - Depois definimos quais endpoints são públicos.
             * - O resto exige autenticação.
             *
             * Preflight (OPTIONS):
             * - Quando você envia Authorization (JWT) ou Content-Type JSON,
             *   o navegador manda um OPTIONS antes perguntando: "posso enviar esses headers?"
             * - Se a API não liberar, dá erro de CORS e a requisição real nem acontece.
             */
            .authorizeHttpRequests(auth -> auth

                // ✅ libera preflight do navegador para qualquer rota
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                /**
                 * ✅ Login precisa ser público
                 * - Esse endpoint é o que recebe login/senha
                 * - e devolve o token JWT no header/body
                 */
                .requestMatchers(HttpMethod.POST, "/login").permitAll()
                .requestMatchers(HttpMethod.POST, "/requisicaojunoboleto/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/").permitAll()
                 // 🔒 todo o resto exige estar autenticado
                .anyRequest().authenticated()
            )

            /**
             * ✅ Filtros JWT
             *
             * - jwtApiFilter: roda em TODA requisição, lê o Authorization Bearer e autentica no SecurityContext.
             * - jwtLoginFilter: roda no /login (POST), valida usuário/senha e devolve o token.
             */
            .addFilterBefore(jwtApiFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterAfter(jwtLoginFilter, UsernamePasswordAuthenticationFilter.class);

            /**
             * ⚠️ Importante:
             * - Quando você usa JWT, normalmente você REMOVE httpBasic
             * - porque não quer enviar usuário/senha em toda requisição.
             */

        return http.build();
    }

    
    
    
    
    /**
     * Regras do CORS (a "política" que o navegador precisa enxergar).
     *
     * Pense nisso como a API dizendo pro navegador:
     * - "Eu deixo o site X me chamar"
     * - "Eu deixo usar métodos Y"
     * - "Eu aceito headers Z (ex: Authorization, Content-Type)"
     * - "Eu deixo o JavaScript ler headers específicos na resposta (ex: Authorization)"
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        
        
        
        /**
         * QUEM pode chamar a API (origem permitida).
         *
         * Angular em dev roda aqui:
         * http://localhost:4200
         *
         * Se você não colocar essa origem, o navegador bloqueia com erro de CORS.
         */
        config.setAllowedOrigins(List.of("http://localhost:4200"));

        
        
        
        /**
         * QUAIS métodos HTTP são permitidos.
         *
         * Inclua OPTIONS por causa do preflight do navegador.
         */
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        
        
        
        
        /**
         * QUAIS headers o front pode ENVIAR.
         *
         * JWT usa "Authorization".
         * JSON usa "Content-Type".
         *
         * Se você não liberar "Authorization", o browser não deixa mandar o token.
         */
        config.setAllowedHeaders(List.of("Authorization", "Content-Type"));

        
        
        
        /**
         * QUAIS headers o front pode LER na RESPOSTA.
         *
         * Importantíssimo:
         * Mesmo que a API devolva o header Authorization,
         * o JavaScript (Angular) NÃO consegue enxergar o header a menos que ele esteja exposto.
         *
         * Se você pretende pegar o token do header da resposta, isso é obrigatório.
         */
        config.setExposedHeaders(List.of("Authorization"));

        
        
        
        /**
         * Permite credenciais (cookies/sessão).
         *
         * Se você estiver usando JWT puro (sem cookie), pode deixar false.
         * Se deixar true, EVITE usar AllowedOrigins = "*" (não é uma boa combinação).
         */
        config.setAllowCredentials(true);

        
        
        
        /**
         * Aplica essa configuração para TODAS as rotas.
         */
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }
}
