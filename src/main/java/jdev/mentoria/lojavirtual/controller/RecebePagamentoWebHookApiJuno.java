package jdev.mentoria.lojavirtual.controller;

import java.io.Serializable;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import jdev.mentoria.lojavirtual.model.BoletoJuno;
import jdev.mentoria.lojavirtual.model.dto.AttibutesNotificaoPagaApiJuno;
import jdev.mentoria.lojavirtual.model.dto.DataNotificacaoApiJunotPagamento;
import jdev.mentoria.lojavirtual.repository.BoletoJunoRepository;

/**
 * Controller responsável por receber as notificações de pagamento enviadas
 * pela API da Juno via WebHook.
 * 
 * <p>
 * Quando a Juno envia uma notificação informando alteração no status de uma cobrança,
 * este controller recebe os dados no formato JSON, localiza o boleto correspondente
 * no banco de dados através do código da cobrança e, caso o pagamento esteja confirmado,
 * realiza a quitação do boleto.
 * </p>
 * 
 * <p>
 * Fluxo resumido:
 * </p>
 * <ul>
 *   <li>Recebe a notificação da Juno via requisição HTTP POST</li>
 *   <li>Lê a lista de eventos recebidos no corpo da requisição</li>
 *   <li>Obtém o código da cobrança e o status do pagamento</li>
 *   <li>Busca o boleto correspondente no banco de dados</li>
 *   <li>Se o boleto ainda não estiver quitado e o status for CONFIRMED, marca como quitado</li>
 * </ul>
 * 
 * @author Em desenvolvimento
 */
@RestController(value = "/requisicaojunoboleto")
public class RecebePagamentoWebHookApiJuno implements Serializable {

	/**
	 * Serial version UID da classe para controle de serialização.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Repositório responsável por acessar e atualizar os dados da entidade
	 * {@link BoletoJuno} no banco de dados.
	 */
	private BoletoJunoRepository boletoJunoRepository;
	

	/**
	 * Recebe a notificação de pagamento enviada pela API Juno.
	 * 
	 * <p>
	 * Este método consome uma requisição do tipo POST com conteúdo JSON no charset UTF-8.
	 * O Spring converte automaticamente o corpo da requisição para o objeto
	 * {@link DataNotificacaoApiJunotPagamento}.
	 * </p>
	 * 
	 * <p>
	 * Para cada item recebido na lista de dados:
	 * </p>
	 * <ul>
	 *   <li>Obtém o código do boleto ou cobrança</li>
	 *   <li>Obtém o status do pagamento</li>
	 *   <li>Verifica se o status é {@code CONFIRMED}</li>
	 *   <li>Busca o boleto no banco pelo código</li>
	 *   <li>Se o boleto ainda não estiver quitado, realiza a baixa do pagamento</li>
	 * </ul>
	 * 
	 * @param dataNotificacaoApiJunotPagamento objeto que representa o corpo da notificação
	 * enviada pela API Juno, contendo os eventos e atributos do pagamento
	 * @return {@link HttpStatus#OK} caso a notificação seja processada
	 */
	@RequestMapping(
			value = "/notificacaoapiv2",
			consumes = { "application/json;charset=UTF-8" },
			headers = "Content-Type=application/json;charset=UTF-8",
			method = RequestMethod.POST)
	private HttpStatus recebeNotificacaoPagamentoJunoApiV2(
			@RequestBody DataNotificacaoApiJunotPagamento dataNotificacaoApiJunotPagamento) {
		
		for (AttibutesNotificaoPagaApiJuno data : dataNotificacaoApiJunotPagamento.getData()) {
			
			String codigoBoletoPix = data.getAttributes().getCharge().getCode();
			
			String status = data.getAttributes().getStatus();
			
			boolean boletoPago = status.equalsIgnoreCase("CONFIRMED") ? true : false;
			
			BoletoJuno boletoJuno = boletoJunoRepository.findByCode(codigoBoletoPix);
			
			if (boletoJuno.isQuitado() == false && boletoPago) {
				boletoJunoRepository.quitarBoletoById(boletoJuno.getId());
			}
		}
		
		return HttpStatus.OK;
	}

}