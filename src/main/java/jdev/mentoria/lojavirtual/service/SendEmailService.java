package jdev.mentoria.lojavirtual.service;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.mail.Address;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

@Service
public class SendEmailService {

	private String username = "emannuel.souza.csr@gmail.com";

	private String senha = "catakiujmjcawbjy";

	@Async
	public void enviarEmailHtml(String assunto, String mensagem, String emailDestino) throws MessagingException, UnsupportedEncodingException {
		
		Properties properties = new Properties();
		
        // Define as configurações do servidor SMTP do Gmail
		properties.put("mail.smtp.ssl.trust", "*");
		properties.put("mail.smtp.starttls", false);
        properties.put("mail.smtp.auth", true); // exige autenticação
        properties.put("mail.smtp.host", "smtp.gmail.com"); // servidor do Gmail
        properties.put("mail.smtp.port", "465"); // porta padrão SSL
        properties.put("mail.smtp.socketFactory.port", "465");
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory"); // usa SSL para criptografar
        

        // Cria uma sessão autenticada com o servidor de e-mail
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                // Retorna o usuário e a senha de autenticação SMTP
                return new PasswordAuthentication(username, senha);
            }
        });

        session.setDebug(true);
        
        // Define o(s) destinatário(s)
        Address[] toUser = InternetAddress.parse(emailDestino);

        // Cria o objeto da mensagem
        Message message = new MimeMessage(session);

        // Define o remetente (quem envia o e-mail)
        message.setFrom(new InternetAddress(username,"Emannuel - oioioi","UTF-8"));

        // Define os destinatários (para quem o e-mail será enviado)
        message.setRecipients(Message.RecipientType.TO, toUser);

        // Assunto
        message.setSubject(assunto);

        // Corpo HTML (isso aqui já basta)
        message.setContent(mensagem, "text/html; charset=UTF-8");

        // REMOVER:
        // message.setText(mensagem);

        Transport.send(message);
		
		
		
	}

	
	
	
	
}
