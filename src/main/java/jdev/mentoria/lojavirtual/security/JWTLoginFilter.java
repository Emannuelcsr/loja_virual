package jdev.mentoria.lojavirtual.security;

import java.io.IOException;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jdev.mentoria.lojavirtual.model.Usuario;

public class JWTLoginFilter extends AbstractAuthenticationProcessingFilter {

    private final JWTTokenAutenticacaoService jwtService;

    public JWTLoginFilter(String url,
                          AuthenticationManager authenticationManager,
                          JWTTokenAutenticacaoService jwtService) {

        super(request ->
                request.getServletPath().equals(url)
                && "POST".equalsIgnoreCase(request.getMethod())
        );

        setAuthenticationManager(authenticationManager);
        this.jwtService = jwtService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException {

        // Lê o JSON do body (login/senha) e transforma em Usuario
        Usuario usuario = new ObjectMapper().readValue(request.getInputStream(), Usuario.class);

        // Cria o token de autenticação (ainda sem estar autenticado)
        var authToken = new UsernamePasswordAuthenticationToken(
                usuario.getLogin(),
                usuario.getSenha()
        );

        // Entrega pro AuthenticationManager validar (UserDetailsService + PasswordEncoder)
        return getAuthenticationManager().authenticate(authToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult)
            throws IOException, ServletException {

        // Gera e devolve o JWT (header + body, do jeito que você fez)
        jwtService.addAuthentication(response, authResult.getName());
    }
    
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
    		AuthenticationException failed) throws IOException, ServletException {
    	
    	if(failed instanceof BadCredentialsException) {
    		
    		response.getWriter().write("User e senha não encontrados");
    	}else {
    		response.getWriter().write("Falha ao logar: " + failed.getMessage());
    	}
    	
    	
    	//super.unsuccessfulAuthentication(request, response, failed);
    }
    
    
    
}
