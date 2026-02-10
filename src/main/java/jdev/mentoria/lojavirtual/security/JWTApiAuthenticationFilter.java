package jdev.mentoria.lojavirtual.security;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Filtro do Spring Security responsável por autenticar requisições de API
 * usando JWT.
 *
 * <p>
 * Esse filtro roda em toda requisição HTTP que passa pela cadeia de filtros do
 * Spring Security. Ele tenta extrair e validar o token JWT enviado pelo cliente
 * (normalmente no header Authorization). Se o token estiver correto, ele cria
 * um objeto {@link Authentication} e registra esse usuário como autenticado no
 * {@link SecurityContextHolder}.
 * </p>
 *
 * <p>
 * Em termos simples: este filtro é uma das peças que faz o “login via token”
 * funcionar. Ao invés de login por sessão, cada requisição chega com um token e
 * o filtro decide se aquela requisição tem um usuário válido ou não.
 * </p>
 */
public class JWTApiAuthenticationFilter extends GenericFilterBean {

	/**
	 * Serviço responsável pela lógica de ler o JWT, validar e montar o objeto
	 * Authentication.
	 *
	 * <p>
	 * O filtro não entende o token por conta própria: ele delega essa
	 * responsabilidade para o {@link JWTTokenAutenticacaoService}.
	 * </p>
	 */
	private final JWTTokenAutenticacaoService jwtService;

	/**
	 * Construtor que recebe o serviço de JWT.
	 *
	 * <p>
	 * Esse padrão deixa a classe mais fácil de testar e mantém a responsabilidade
	 * bem separada: o filtro intercepta a requisição e o serviço faz a validação do
	 * token.
	 * </p>
	 *
	 * @param jwtService Serviço que sabe como validar o token e criar o
	 *                   Authentication.
	 */
	public JWTApiAuthenticationFilter(JWTTokenAutenticacaoService jwtService) {
		// Guarda o serviço para usar durante o filtro
		this.jwtService = jwtService;
	}

	/**
	 * Intercepta a requisição HTTP, tenta autenticar via JWT e segue o fluxo da
	 * aplicação.
	 *
	 * <p>
	 * Passo a passo do que acontece aqui:
	 * </p>
	 * <p>
	 * 1) Converte o request/response genéricos para HTTP (para acessar headers e
	 * status).<br>
	 * 2) Pede ao serviço de JWT para tentar criar um {@link Authentication}.<br>
	 * 3) Se vier um Authentication válido, registra no
	 * {@link SecurityContextHolder}.<br>
	 * 4) Continua a requisição chamando {@code chain.doFilter(...)}.
	 * </p>
	 *
	 * @param request  Requisição recebida (genérica do Servlet API).
	 * @param response Resposta recebida (genérica do Servlet API).
	 * @param chain    Cadeia de filtros (próximo filtro ou controller).
	 *
	 * @throws IOException      Em caso de erro de entrada/saída.
	 * @throws ServletException Em caso de erro no processamento do filtro.
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		// Converte o request genérico para HttpServletRequest para acessar headers
		// (Authorization, etc.)
		HttpServletRequest req = (HttpServletRequest) request;

		// Converte o response genérico para HttpServletResponse para permitir o serviço
		// escrever resposta se precisar
		HttpServletResponse res = (HttpServletResponse) response;

		try {

			// ------------------------------------------------------------
			// Tenta obter um usuário autenticado a partir do token JWT
			// ------------------------------------------------------------

			// Pede para o serviço validar o token e retornar um Authentication pronto (ou
			// null se não tiver token/for inválido)
			Authentication authentication = jwtService.getAuthentication(res, req);

			// Se a autenticação foi montada com sucesso, registra no contexto de segurança
			// do Spring
			if (authentication != null) {
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}

			// ------------------------------------------------------------
			// Continua o fluxo normal da requisição (próximo filtro / controller)
			// ------------------------------------------------------------
			chain.doFilter(request, response);
		} catch (io.jsonwebtoken.security.SignatureException e) {
			response.getWriter().write("Token esta invalido");

		} catch (io.jsonwebtoken.ExpiredJwtException e) {
			response.getWriter().write("Token esta expirado");
		} catch (Exception e) {

			e.printStackTrace();
			response.getWriter().write("Ocorreu um erro no sistema. Avise o administrador \n" + e.getMessage());
		}

	}

	/*
	 * ===================== EXPLICAÇÃO DIDÁTICA =====================
	 *
	 * Pense nesse filtro como o “porteiro” das rotas da sua API.
	 *
	 * Em uma aplicação com JWT, o servidor não guarda “sessão de login”. Quem prova
	 * que está logado é o próprio cliente, enviando um token em toda requisição
	 * (geralmente no header Authorization).
	 *
	 * O trabalho desse filtro é: - pegar a requisição - pedir para o jwtService
	 * analisar o token - se o token for válido, criar um Authentication - colocar
	 * esse Authentication no SecurityContextHolder
	 *
	 * Quando o Authentication está no SecurityContextHolder, o resto do Spring
	 * Security entende que existe um usuário autenticado ali e consegue aplicar
	 * regras como: - permitir acesso - bloquear acesso - verificar roles/permissões
	 * (ROLE_ADMIN, ROLE_USER, etc.)
	 *
	 * Se o token estiver ausente ou inválido, o jwtService devolve null. Nesse caso
	 * o filtro só não registra autenticação nenhuma e segue o fluxo. A decisão de
	 * bloquear ou permitir será feita pelas regras de segurança configuradas no seu
	 * SecurityConfig.
	 *
	 * Em resumo: esse filtro tenta “transformar token em usuário autenticado” em
	 * toda requisição.
	 * ===============================================================
	 */
}
