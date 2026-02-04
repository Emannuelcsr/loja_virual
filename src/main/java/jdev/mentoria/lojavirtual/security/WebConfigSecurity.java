package jdev.mentoria.lojavirtual.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import jakarta.servlet.http.HttpSessionListener;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class WebConfigSecurity implements HttpSessionListener {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

	    http
	        .csrf(csrf -> csrf.disable())
	        .authorizeHttpRequests(auth -> auth
	            .requestMatchers(HttpMethod.POST, "/salvarAcesso", "/deleteAcesso").permitAll()
	            .requestMatchers(HttpMethod.GET, "/salvarAcesso","/deleteAcesso").permitAll()
	            .anyRequest().authenticated()
	        )
	        .httpBasic(Customizer.withDefaults());

	    return http.build();
	}
}
