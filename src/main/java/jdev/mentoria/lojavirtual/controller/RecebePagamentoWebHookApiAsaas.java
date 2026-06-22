package jdev.mentoria.lojavirtual.controller;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jdev.mentoria.lojavirtual.model.BoletoJuno;
import jdev.mentoria.lojavirtual.model.dto.AsaasWebhookDTO;
import jdev.mentoria.lojavirtual.repository.BoletoJunoRepository;

@Controller
@RequestMapping(value = "/requisicaoapiasaas")
public class RecebePagamentoWebHookApiAsaas implements Serializable {

	private final ContaPagarController contaPagarController;

	private static final long serialVersionUID = 1L;

	@Autowired
	private BoletoJunoRepository boletoJunoRepository;

	RecebePagamentoWebHookApiAsaas(ContaPagarController contaPagarController) {
		this.contaPagarController = contaPagarController;
	}

	
	@RequestMapping(value = "/notificacaoapiasaas", consumes = {
			"application/json;charset=UTF-8" }, headers = "Content-Type=application/json;charset=UTF-8", method = RequestMethod.POST)
	private ResponseEntity<String> recebeNotificacaoPagamentoApiAsaas(@RequestBody AsaasWebhookDTO asaasWebhookDTO) {

		BoletoJuno boletoJuno = boletoJunoRepository.findByCode(asaasWebhookDTO.getPayment().getId());

		if(boletoJuno == null ) {
			
			return new ResponseEntity<String>("Boleto/Fatura não encontrada no banco de dados", HttpStatus.OK);
			
		}
		
		
		if (boletoJuno != null && asaasWebhookDTO.boletoPixFaturaPaga() && !boletoJuno.isQuitado()) {

			boletoJunoRepository.quitarBoletoById(boletoJuno.getId());

			System.out.println("Boleto: " + boletoJuno.getCode() + " foi quitado ");

			return new ResponseEntity<String>("Recebido do Asaas. Boleto id: " + boletoJuno.getId(), HttpStatus.OK);

		} else {

			System.out.println("Fatura : " + asaasWebhookDTO.getPayment().getId() + " não foi processada, quitada "
					+ asaasWebhookDTO.boletoPixFaturaPaga() + " valor quitado "
					+ (boletoJuno != null ? boletoJuno.isQuitado() : "boleto não encontrado"));
		}

		return new ResponseEntity<String>("Não foi processada a fatura: " + asaasWebhookDTO.getPayment().getId(),
				HttpStatus.OK);
	}

}
