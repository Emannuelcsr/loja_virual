package jdev.mentoria.lojavirtual;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.hibernate.exception.ConstraintViolationException;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import jdev.mentoria.lojavirtual.model.dto.ObjetoErroDTO;
import jdev.mentoria.lojavirtual.service.SendEmailService;

/**
 * Central de erros da API (o "guarda-chuva" de exceções).
 *
 * O que essa classe faz, bem direto:
 * 1) Intercepta erros que acontecerem nos seus endpoints (controllers)
 * 2) Converte esses erros em um JSON padrão (ObjetoErroDTO)
 * 3) Devolve um HTTP status correto (400, 404, 409, 500...)
 * 4) Tenta mandar um e-mail com o stacktrace do erro (sem atrapalhar a resposta)
 *
 * Por que isso é útil:
 * - O front-end sempre recebe um formato fixo de erro (dto.erro e dto.code)
 * - Você não vaza stacktrace para o cliente
 * - Você consegue ser avisado por e-mail quando der problema no servidor
 *
 * Essa classe funciona porque:
 * - @RestControllerAdvice faz o Spring "ouvir" exceções de qualquer controller
 * - ResponseEntityExceptionHandler já tem um mecanismo padrão do Spring
 *   para tratar várias exceções comuns; aqui você está customizando.
 */
@RestControllerAdvice
public class ControleExcecoes extends ResponseEntityExceptionHandler {

	/**
	 * Serviço responsável por enviar e-mails (ex: via SMTP).
	 * Aqui ele é usado para notificar o dono do sistema quando ocorrer um erro.
	 */
	@Autowired
	private SendEmailService sendEmailService;

	/**
	 * Tratamento "geral" do Spring para exceções que passam pelo pipeline do MVC.
	 *
	 * Quando esse método roda?
	 * - Quando acontece algum erro durante a requisição e o Spring decide tratar
	 *   usando o ResponseEntityExceptionHandler (classe pai).
	 *
	 * O que ele devolve:
	 * - Um JSON (ObjetoErroDTO) com:
	 *   - erro: a mensagem do erro, de forma "mostrável" pro front
	 *   - code: "status ==> motivo" (ex: "400 ==> Bad Request")
	 *
	 * Regras de mensagem (msg):
	 * 1) Se for erro de validação (@Valid), ele junta todas as mensagens.
	 * 2) Se o BODY veio vazio / inválido (JSON mal formado), devolve uma mensagem fixa.
	 * 3) Se for qualquer outro erro, usa ex.getMessage().
	 *
	 * Importante:
	 * - Ele SEMPRE tenta mandar e-mail do stacktrace (enviarEmailErroSilencioso),
	 *   mas se o e-mail falhar, isso NÃO pode quebrar a resposta da API.
	 */
	@Override
	protected ResponseEntity<Object> handleExceptionInternal(
			Exception ex,
			@Nullable Object body,
			HttpHeaders headers,
			HttpStatusCode statusCode,
			WebRequest request) {

		// DTO que será retornado para o cliente (front-end) em formato JSON
		ObjetoErroDTO objetoErroDTO = new ObjetoErroDTO();

		// Mensagem final que o cliente vai receber no campo "erro"
		String msg = "";

		// 1) Quando falha validação (@Valid) em DTOs do request
		if (ex instanceof MethodArgumentNotValidException) {

			// Pega todos os erros de validação do request (um por campo inválido)
			List<ObjectError> list = ((MethodArgumentNotValidException) ex)
					.getBindingResult()
					.getAllErrors();

			// Junta tudo em uma string (cada erro em uma linha)
			for (ObjectError objectError : list) {
				msg += objectError.getDefaultMessage() + "\n ";
			}

		// 2) Quando o JSON do BODY está inválido ou não foi enviado corretamente
		} else if (ex instanceof HttpMessageNotReadableException) {

			msg = "não esta sendo enviado dados para o BODY corpo da requisição";

		// 3) Qualquer outro caso: pega a mensagem padrão da exceção
		} else {
			msg = ex.getMessage();
		}

		// Pega um texto "humano" do status (ex: Bad Request, Not Found etc.)
		String reason = (statusCode instanceof HttpStatus hs) ? hs.getReasonPhrase() : "";

		// Preenche o DTO com a mensagem e com o código formatado
		objetoErroDTO.setErro(msg);
		objetoErroDTO.setCode(statusCode.value() + " ==> " + reason);

		// Tenta avisar por e-mail o stacktrace do erro (sem quebrar o retorno da API)
		enviarEmailErroSilencioso(ex);

		// Devolve o JSON + status HTTP que o Spring já definiu
		return new ResponseEntity<>(objetoErroDTO, statusCode);
	}

	/**
	 * Tratamento específico para erros de integridade do banco.
	 *
	 * Quando esse método roda?
	 * - Quando o controller (ou service) estoura algum desses erros:
	 *   - DataIntegrityViolationException (Spring)
	 *   - ConstraintViolationException (Hibernate)
	 *   - SQLException (SQL puro)
	 *
	 * Ideia principal:
	 * - Para esse tipo de erro, geralmente faz sentido devolver 409 (Conflict),
	 *   porque a operação bateu numa regra do banco (FK, UNIQUE, NOT NULL etc.).
	 *
	 * O retorno é sempre um JSON padronizado (ObjetoErroDTO) usando o método build().
	 */
	@ExceptionHandler({ DataIntegrityViolationException.class, ConstraintViolationException.class, SQLException.class })
	public ResponseEntity<Object> handleExceptionDataIntegrity(Exception ex) {

		// DTO que será devolvido no corpo da resposta
		ObjetoErroDTO dto = new ObjetoErroDTO();

		// Mensagem que vai para dto.erro
		String msg;

		// 1) Exceção do Spring relacionada a integridade (vem do banco por baixo)
		if (ex instanceof DataIntegrityViolationException) {

			// rootMessage(ex) pega a causa mais profunda, geralmente com o detalhe real
			msg = "Erro de integridade no banco: " + rootMessage(ex);

			// Envia e-mail do erro, mas sem atrapalhar o retorno
			enviarEmailErroSilencioso(ex);

			// 409 porque a ação conflitou com uma regra do banco
			return build(dto, msg, HttpStatus.CONFLICT);

		// 2) Exceção do Hibernate: constraint (FK, UNIQUE, etc.)
		} else if (ex instanceof ConstraintViolationException) {

			msg = "Erro de constraint: " + rootMessage(ex);
			enviarEmailErroSilencioso(ex);
			return build(dto, msg, HttpStatus.CONFLICT);

		// 3) SQL direto (às vezes dá pra considerar 500)
		} else if (ex instanceof SQLException) {

			msg = "Erro de SQL no banco: " + rootMessage(ex);
			enviarEmailErroSilencioso(ex);
			return build(dto, msg, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		// Se cair aqui, devolve um 500 genérico
		msg = ex.getMessage();
		enviarEmailErroSilencioso(ex);
		return build(dto, msg, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	/**
	 * Monta e devolve a resposta padrão no formato da sua API.
	 *
	 * Isso evita repetição de código, porque vários handlers precisam devolver:
	 * - dto.erro
	 * - dto.code
	 * - status HTTP
	 *
	 * Exemplo de dto.code:
	 * - "409 ==> Conflict"
	 */
	private ResponseEntity<Object> build(ObjetoErroDTO dto, String msg, HttpStatus status) {

		// Mensagem que o front vai mostrar/usar
		dto.setErro(msg);

		// Código formatado para facilitar log/depuração no cliente
		dto.setCode(status.value() + " ==> " + status.getReasonPhrase());

		// Devolve o DTO com o status escolhido
		return new ResponseEntity<>(dto, status);
	}

	/**
	 * Pega a mensagem da causa raiz (o "erro de verdade") de uma exceção.
	 *
	 * Por que isso existe?
	 * - Muitas exceções vêm "embrulhadas" (uma dentro da outra).
	 * - A mensagem útil geralmente está lá no fundo, na última causa.
	 *
	 * Exemplo:
	 * - DataIntegrityViolationException (genérico)
	 *   -> ConstraintViolationException
	 *      -> PSQLException (a mensagem real do PostgreSQL)
	 *
	 * Esse método desce até a última causa e devolve o getMessage() dela.
	 */
	private String rootMessage(Throwable ex) {

		Throwable root = ex;

		// Desce até a última causa (a mais profunda)
		while (root.getCause() != null && root.getCause() != root) {
			root = root.getCause();
		}

		return root.getMessage();
	}

	/**
	 * Tratamento da sua exceção personalizada (ExcepetionLojaVirtual).
	 *
	 * Quando usar isso?
	 * - Quando você quer lançar um erro "da regra do seu sistema"
	 *   (ex: "não encontrou pessoa", "acesso negado", etc.)
	 *
	 * Aqui você escolheu:
	 * - Status 404 (Not Found)
	 * - JSON padronizado no ObjetoErroDTO
	 */
	@ExceptionHandler({ ExcepetionLojaVirtual.class })
	public ResponseEntity<Object> HandleExcepetionCustom(ExcepetionLojaVirtual ex) {

		ObjetoErroDTO objetoErroDTO = new ObjetoErroDTO();

		// Mensagem que você mesmo definiu ao lançar a exceção
		objetoErroDTO.setErro(ex.getMessage());

		// Code manual fixo (você escolheu 404)
		objetoErroDTO.setCode("404 ==> Not Found");

		return new ResponseEntity<>(objetoErroDTO, HttpStatus.NOT_FOUND);
	}

	/**
	 * Tratamento genérico para qualquer Exception não prevista.
	 *
	 * Atenção:
	 * - Esse handler pode "capturar tudo" que não foi pego pelos outros.
	 * - Ele devolve sempre 500.
	 *
	 * Ele existe para garantir:
	 * - Sempre voltar JSON padrão
	 * - Sempre tentar mandar e-mail do erro
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleGenericException(Exception ex) {

		ObjetoErroDTO dto = new ObjetoErroDTO();

		dto.setErro(ex.getMessage());
		dto.setCode("500 ==> Internal Server Error");

		enviarEmailErroSilencioso(ex);

		return new ResponseEntity<>(dto, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	/**
	 * Envia e-mail com o stacktrace do erro, sem deixar a API falhar por causa disso.
	 *
	 * Por que "silencioso"?
	 * - Se o e-mail falhar (senha errada, SMTP fora, destinatário inválido),
	 *   isso NÃO pode derrubar a resposta do seu endpoint.
	 *
	 * O que vai no e-mail:
	 * - Assunto: "Erro na loja virtual"
	 * - Corpo: stacktrace completo (ExceptionUtils.getStackTrace(ex))
	 * - Destinatário: seu e-mail fixo
	 */
	private void enviarEmailErroSilencioso(Exception ex) {
		try {
			sendEmailService.enviarEmailHtml(
					"Erro na loja virtual",
					ExceptionUtils.getStackTrace(ex),
					"emannuelsouza@hotmail.com"
			);
		} catch (Exception ignored) {
			// Ignora porque falha de e-mail não pode quebrar o retorno da API
		}
	}
}
