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
 * Aqui você controla: - Como o Spring valida senhas (PasswordEncoder) - Como o
 * Spring consegue um AuthenticationManager - Quais rotas são públicas ou
 * protegidas - E principalmente: CORS (para o Angular conseguir chamar a API
 * sem o navegador bloquear)
 *
 * Observação importante: CORS é uma regra do NAVEGADOR, não do Spring. Postman
 * não tem CORS, por isso "funciona no Postman e dá erro no Angular".
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
	 * Na prática: - quando o usuário faz login, o Spring pega a senha digitada -
	 * criptografa/valida com BCrypt - compara com a senha criptografada no banco
	 *
	 * Sem isso, você cai em erro de "There is no PasswordEncoder mapped..." ou
	 * login falha.
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
	 * Onde entra o CORS? - O navegador (Angular) só deixa chamar a API se a API
	 * "autorizar" no cabeçalho. - Quem garante isso aqui é: http.cors(...) - e as
	 * regras ficam no bean corsConfigurationSource().
	 */
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationConfiguration authConfig)
	        throws Exception {

	    AuthenticationManager authenticationManager = authConfig.getAuthenticationManager();

	    JWTLoginFilter jwtLoginFilter = new JWTLoginFilter("/login", authenticationManager,
	            jwtTokenAutenticacaoService);

	    JWTApiAuthenticationFilter jwtApiFilter = new JWTApiAuthenticationFilter(jwtTokenAutenticacaoService);

	    http
	        .csrf(csrf -> csrf.disable())
	        .cors(Customizer.withDefaults())
	        .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
	        .authorizeHttpRequests(auth -> auth

	            // libera preflight do navegador
	            .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

	            // rotas públicas POST
	            .requestMatchers(HttpMethod.POST,
	                    "/index",
	                    "/pagamento/**",
	                    "/requisicaojunoboleto/**",
	                    "/resources/**",
	                    "/finalizarCompraCartao",
	                    "/static/**",
	                    "/requisicaoapiasaas",
	                    "/notificacaoapiasaas",
	                    "/listarcategoriaproduto/**",
	                    "/salvarmarca",
	                    "/deleteMarcaPorId/**",
	                    "/obterMarca/**",
	                    "/buscarPorMarcaPorDescricao/**",
	                    "/qtdadePaginaMarcaProduto/**",
	                    "/buscarPorCatMarcaPorEmpresa/**",
	                    "/buscarMarcaporid/**",
	                    "/listaPorPageMarcaProduto/**",
	                    "/listarmarcaproduto/**",
	                    "/buscarporidMarca/**",
	                    "/deleteMarca",
	                    "/recuperarSenha",
	                    "/quantidadeDeCategorias",
	                    "/salvarAcesso",
	                    "/deleteAcesso",
	                    "/obterAcesso",
	                    "/buscarPorDescricao",
	                    "/listaPorPageAcesso",
	                    "/qtdadePaginaAcesso",
	                    "/buscarPorAcessoPorEmpresa",
	                    "/quantidadeDeAcessos",
	                    "/listarAcesso",
	                    "/listUserByEmpresa/**",
	                    "/templates/**")
	            .permitAll()

	            // rotas públicas GET
	            .requestMatchers(HttpMethod.GET,
	                    "/",
	                    "/index",
	                    "/pagamento/**",
	                    "/requisicaojunoboleto/**",
	                    "/resources/**",
	                    "/finalizarCompraCartao",
	                    "/static/**",
	                    "/requisicaoapiasaas",
	                    "/notificacaoapiasaas",
	                    "/listarcategoriaproduto/**",
	                    "/salvarmarca",
	                    "/deleteMarcaPorId/**",
	                    "/obterMarca/**",
	                    "/buscarPorMarcaPorDescricao/**",
	                    "/qtdadePaginaMarcaProduto/**",
	                    "/buscarPorCatMarcaPorEmpresa/**",
	                    "/buscarMarcaporid/**",
	                    "/listaPorPageMarcaProduto/**",
	                    "/listarmarcaproduto/**",
	                    "/buscarporidMarca/**",
	                    "/deleteMarca",
	                    "/recuperarSenha",
	                    "/quantidadeDeCategorias",
	                    "/salvarAcesso",
	                    "/deleteAcesso",
	                    "/obterAcesso",
	                    "/buscarPorDescricao",
	                    "/listaPorPageAcesso",
	                    "/qtdadePaginaAcesso",
	                    "/buscarPorAcessoPorEmpresa",
	                    "/quantidadeDeAcessos",
	                    "/listarAcesso",
	                    "/listUserByEmpresa/**",
	                    "/templates/**")
	            .permitAll()

	            // todo o resto exige autenticação
	            .anyRequest().authenticated()
	        )
	        .addFilterBefore(jwtApiFilter, UsernamePasswordAuthenticationFilter.class)
	        .addFilterAfter(jwtLoginFilter, UsernamePasswordAuthenticationFilter.class);

	    return http.build();
	}
	/**
	 * Regras do CORS (a "política" que o navegador precisa enxergar).
	 *
	 * Pense nisso como a API dizendo pro navegador: - "Eu deixo o site X me chamar"
	 * - "Eu deixo usar métodos Y" - "Eu aceito headers Z (ex: Authorization,
	 * Content-Type)" - "Eu deixo o JavaScript ler headers específicos na resposta
	 * (ex: Authorization)"
	 */
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		
		
		
		CorsConfiguration config = new CorsConfiguration();

		/**
		 * QUEM pode chamar a API (origem permitida).
		 *
		 * Angular em dev roda aqui: http://localhost:4200
		 *
		 * Se você não colocar essa origem, o navegador bloqueia com erro de CORS.
		 */
		config.setAllowedOrigins(List.of(
			    "http://localhost:4200",
			    "https://localhost:4200"
			));
		/**
		 * QUAIS métodos HTTP são permitidos.
		 *
		 * Inclua OPTIONS por causa do preflight do navegador.
		 */
		config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

		/**
		 * QUAIS headers o front pode ENVIAR.
		 *
		 * JWT usa "Authorization". JSON usa "Content-Type".
		 *
		 * Se você não liberar "Authorization", o browser não deixa mandar o token.
		 */
		config.setAllowedHeaders(List.of("Authorization", "Content-Type"));

		/**
		 * QUAIS headers o front pode LER na RESPOSTA.
		 *
		 * Importantíssimo: Mesmo que a API devolva o header Authorization, o JavaScript
		 * (Angular) NÃO consegue enxergar o header a menos que ele esteja exposto.
		 *
		 * Se você pretende pegar o token do header da resposta, isso é obrigatório.
		 */
		config.setExposedHeaders(List.of("Authorization"));

		/**
		 * Permite credenciais (cookies/sessão).
		 *
		 * Se você estiver usando JWT puro (sem cookie), pode deixar false. Se deixar
		 * true, EVITE usar AllowedOrigins = "*" (não é uma boa combinação).
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
