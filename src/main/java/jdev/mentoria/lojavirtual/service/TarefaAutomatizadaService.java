package jdev.mentoria.lojavirtual.service;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jdev.mentoria.lojavirtual.model.Usuario;
import jdev.mentoria.lojavirtual.repository.UsuarioRepository;

@Service
public class TarefaAutomatizadaService {

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	
	@Autowired
	private SendEmailService sendEmailService;
	
	@Scheduled(initialDelay = 2000,fixedDelay = 86400000)
	//@Scheduled(cron = "0 0 11 * * * ", zone = "America/Sao_Paulo")
	public void notificaUserTrocaSenha() throws UnsupportedEncodingException, MessagingException, InterruptedException {

		List<Usuario> usuario  = usuarioRepository.usuarioSenhaVencida();
		
		for (Usuario usuario2 : usuario) {
			
			StringBuilder msg = new StringBuilder();
			
			msg.append("Olá, ").append(usuario2.getPessoa().getNome()).append("<br/>");
			msg.append("Está na hora de trocar sua senha, ja passou de 90 dias da validade").append("<br/>");
			msg.append("Troque sua senha em Emanuel - Loja Virtual").append("<br/>");
			
			sendEmailService.enviarEmailHtml("Troca de senha", msg.toString(), usuario2.getLogin());
			
			Thread.sleep(3000);
			
		}
		
		
	}
	

}
