package jdev.mentoria.lojavirtual;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.exception.ConstraintViolationException;
import org.jspecify.annotations.Nullable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import jdev.mentoria.lojavirtual.model.dto.ObjetoErroDTO;

/**
 * Classe responsável por centralizar o tratamento de exceções da aplicação.
 *
 * <p>
 * Quando algum Controller REST lança uma exceção (erro), esta classe intercepta
 * e transforma o erro em uma resposta HTTP padronizada, com um corpo (JSON)
 * contendo informações úteis para o front-end ou para o cliente da API.
 * </p>
 *
 * <p>
 * Isso evita respostas “cruas” (stacktrace no retorno) e garante que o sistema
 * sempre responda num formato previsível, por exemplo:
 * </p>
 *
 * <p>
 * - erro: "Campo nome é obrigatório"<br>
 * - code: "400 ==> Bad Request"
 * </p>
 *
 * <p>
 * Em termos simples: essa classe é o “tradutor de erros” da sua API.
 * </p>
 */
@RestControllerAdvice
public class ControleExcecoes extends ResponseEntityExceptionHandler {

	/**
	 * Trata exceções genéricas do sistema (Exception, RuntimeException, Throwable).
	 *
	 * <p>
	 * Este método é chamado quando acontece um erro e não existe um tratamento mais
	 * específico. Ele também aproveita o tratamento de validação do Spring
	 * (MethodArgumentNotValidException), para juntar todas as mensagens de erro de
	 * validação e devolver em uma única resposta.
	 * </p>
	 *
	 * @param ex         Exceção capturada durante a requisição.
	 * @param body       Corpo da resposta (quando aplicável). Aqui quase sempre vem
	 *                   null.
	 * @param headers    Headers que podem ser devolvidos na resposta.
	 * @param statusCode Status HTTP que o Spring determinou para o erro.
	 * @param request    Contexto da requisição atual.
	 *
	 * @return ResponseEntity contendo um {@link ObjetoErroDTO} com mensagem e
	 *         código HTTP.
	 */
	@ExceptionHandler({ Exception.class, RuntimeException.class, Throwable.class })
	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, @Nullable Object body, HttpHeaders headers,
			HttpStatusCode statusCode, WebRequest request) {

		// Cria o DTO que será devolvido para o cliente (front-end / consumidor da API)
		ObjetoErroDTO objetoErroDTO = new ObjetoErroDTO();

		// Aqui vamos montar a mensagem final do erro
		String msg = "";

		// ------------------------------------------------------------
		// CASO 1: Erros de validação (@Valid) no request
		// ------------------------------------------------------------
		if (ex instanceof MethodArgumentNotValidException) {

			// Pega a lista de erros de validação (ex: "nome é obrigatório", "email
			// inválido", etc.)
			List<ObjectError> list = ((MethodArgumentNotValidException) ex).getBindingResult().getAllErrors();

			// Junta todas as mensagens numa string só (uma por linha)
			for (ObjectError objectError : list) {
				msg += objectError.getDefaultMessage() + "\n ";
			}

		} else {
			// ------------------------------------------------------------
			// CASO 2: Qualquer outro erro genérico
			// ------------------------------------------------------------
			msg = ex.getMessage();
		}

		// Monta uma descrição humana do status HTTP (ex: 400 ==> Bad Request)
		String reason = (statusCode instanceof HttpStatus hs) ? hs.getReasonPhrase() : "";

		// Preenche o DTO de erro com a mensagem e o "code"
		objetoErroDTO.setErro(msg);
		objetoErroDTO.setCode(statusCode.value() + " ==> " + reason);

		// Retorna a resposta com o DTO no corpo e o status que o Spring já definiu
		return new ResponseEntity<>(objetoErroDTO, statusCode);
	}

	/**
	 * Trata exceções relacionadas a integridade e constraints do banco de dados.
	 *
	 * <p>
	 * Aqui entram erros do tipo:
	 * </p>
	 * <p>
	 * - violação de chave estrangeira (FK)<br>
	 * - violação de unique (valor repetido)<br>
	 * - tentativa de salvar null em campo NOT NULL<br>
	 * - erros SQL gerais
	 * </p>
	 *
	 * <p>
	 * Esse tratamento é separado porque esses erros são muito comuns em CRUDs e
	 * normalmente o cliente precisa receber uma mensagem mais amigável, junto com
	 * um status HTTP coerente (ex: 409 Conflict).
	 * </p>
	 *
	 * @param ex Exceção capturada durante a operação no banco.
	 * @return ResponseEntity com {@link ObjetoErroDTO} e status adequado.
	 */
	@ExceptionHandler({ DataIntegrityViolationException.class, ConstraintViolationException.class, SQLException.class })
	public ResponseEntity<Object> handleExceptionDataIntegrity(Exception ex) {

		// DTO padrão de erro
		ObjetoErroDTO dto = new ObjetoErroDTO();

		// Mensagem final que será mostrada ao cliente
		String msg;

		// ------------------------------------------------------------
		// 1) Erro de integridade no Spring (geralmente vem do banco por baixo)
		// ------------------------------------------------------------
		if (ex instanceof DataIntegrityViolationException) {

			// Pega a mensagem raiz (mais útil do que a mensagem “embrulhada”)
			msg = "Erro de integridade no banco: " + rootMessage(ex);

			// 409 Conflict é típico quando a operação conflita com regras do banco
			return build(dto, msg, HttpStatus.CONFLICT);

			// ------------------------------------------------------------
			// 2) Erro de constraint no Hibernate (FK, UNIQUE, etc.)
			// ------------------------------------------------------------
		} else if (ex instanceof ConstraintViolationException) {

			msg = "Erro de constraint: " + rootMessage(ex);
			return build(dto, msg, HttpStatus.CONFLICT);

			// ------------------------------------------------------------
			// 3) Erro SQL direto
			// ------------------------------------------------------------
		} else if (ex instanceof SQLException) {

			msg = "Erro de SQL no banco: " + rootMessage(ex);
			return build(dto, msg, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		// Caso caia aqui por algum motivo, devolve erro genérico
		msg = ex.getMessage();
		return build(dto, msg, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	/**
	 * Monta uma resposta padrão de erro usando o {@link ObjetoErroDTO}.
	 *
	 * <p>
	 * Esse método existe para evitar repetição: várias exceções vão gerar o mesmo
	 * formato de resposta (erro + code).
	 * </p>
	 *
	 * @param dto    DTO que será preenchido e devolvido.
	 * @param msg    Mensagem final do erro.
	 * @param status Status HTTP que será usado na resposta.
	 * @return ResponseEntity com DTO e status.
	 */
	private ResponseEntity<Object> build(ObjetoErroDTO dto, String msg, HttpStatus status) {

		// Define a mensagem de erro
		dto.setErro(msg);

		// Define o "code" como "status ==> motivo"
		dto.setCode(status.value() + " ==> " + status.getReasonPhrase());

		// Retorna a resposta montada
		return new ResponseEntity<>(dto, status);
	}

	/**
	 * Recupera a mensagem raiz (root cause) de uma exceção.
	 *
	 * <p>
	 * Muitas exceções vêm “encapsuladas” (uma dentro da outra). Esse método desce
	 * até a última causa, que geralmente contém a mensagem mais útil para entender
	 * o problema real.
	 * </p>
	 *
	 * @param ex Exceção inicial (pode conter várias causas).
	 * @return String mensagem da causa raiz.
	 */
	private String rootMessage(Throwable ex) {

		// Começa pela exceção atual
		Throwable root = ex;

		// Enquanto houver uma causa, vai descendo até a última
		while (root.getCause() != null && root.getCause() != root) {
			root = root.getCause();
		}

		// Retorna a mensagem da causa mais profunda
		return root.getMessage();
	}

	
	
	
	
	
    /**
     * Trata exceções personalizadas do tipo {@link ExcepetionLojaVirtual}.
     *
     * <p>Esse método é acionado automaticamente pelo Spring sempre que uma
     * {@code ExcepetionLojaVirtual} é lançada em qualquer controller da aplicação.</p>
     *
     * <p>Ele transforma a exceção em uma resposta HTTP padronizada, devolvendo
     * um JSON com mensagem de erro e código HTTP, evitando que o front-end
     * receba uma resposta genérica ou sem contexto.</p>
     *
     * @param ex Exceção personalizada lançada pela aplicação.
     * @return ResponseEntity contendo um {@link ObjetoErroDTO} e o status HTTP 404.
     */
    @ExceptionHandler({ ExcepetionLojaVirtual.class })
    public ResponseEntity<Object> HandleExcepetionCustom(ExcepetionLojaVirtual ex) {

        // Cria o DTO padrão de erro que será enviado ao cliente
        ObjetoErroDTO objetoErroDTO = new ObjetoErroDTO();

        // Define a mensagem de erro com base na mensagem da exceção lançada
        objetoErroDTO.setErro(ex.getMessage());

        // Define manualmente o código HTTP e a descrição
        objetoErroDTO.setCode("404 ==> Not Found");

        // Retorna a resposta HTTP com o DTO e o status NOT_FOUND
        return new ResponseEntity<>(objetoErroDTO, HttpStatus.NOT_FOUND);
    }

    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
	/*
	 * ===================== EXPLICAÇÃO DIDÁTICA =====================
	 *
	 * Essa classe existe para resolver um problema muito comum em APIs: quando
	 * ocorre um erro, você não quer devolver um “erro feio”, stacktrace, ou uma
	 * resposta inconsistente para o cliente.
	 *
	 * Com @RestControllerAdvice, o Spring “escuta” todas as exceções que
	 * acontecerem nos seus controllers e joga para cá.
	 *
	 * Aqui você tem dois grandes cenários:
	 *
	 * 1) Erros de validação (MethodArgumentNotValidException) Exemplo: você manda
	 * um JSON inválido, um campo obrigatório vazio, tamanho menor que o permitido,
	 * etc. Nesse caso, a classe junta todas as mensagens e devolve tudo de uma vez.
	 *
	 * 2) Erros do banco (DataIntegrityViolationException,
	 * ConstraintViolationException, SQLException) Exemplo: tentar inserir valor
	 * repetido, violar FK, salvar null em campo NOT NULL. Aqui a classe devolve 409
	 * Conflict (quando faz sentido) ou 500 quando for erro mais grave.
	 *
	 * O ObjetoErroDTO é o formato padrão que você escolheu para o “corpo do erro”.
	 * Isso é ótimo porque o front-end consegue tratar sempre do mesmo jeito: -
	 * mostra dto.erro para o usuário - usa dto.code para log/depuração
	 *
	 * Em resumo: essa classe transforma exceções em respostas HTTP bem organizadas,
	 * com mensagens melhores, e sem bagunça para quem está consumindo a API.
	 * ===============================================================
	 */
}
