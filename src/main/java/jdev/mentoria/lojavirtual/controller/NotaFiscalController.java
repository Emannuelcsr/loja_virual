package jdev.mentoria.lojavirtual.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jdev.mentoria.lojavirtual.model.NotaFiscalVenda;
import jdev.mentoria.lojavirtual.model.VendaCompraLojaVirtual;
import jdev.mentoria.lojavirtual.model.dto.NotaFiscalRetornoEnvioDTO;
import jdev.mentoria.lojavirtual.repository.Vd_cp_Loja_virtual_Repository;
import jdev.mentoria.lojavirtual.service.FocusNotaFiscalService;

@RestController
@RequestMapping("/nota-fiscal")
public class NotaFiscalController {

	@Autowired
	private FocusNotaFiscalService service;

	private Vd_cp_Loja_virtual_Repository vendaCompraLojaVirtualRepository;

	@PostMapping("/emitir/{idVenda}")
	public ResponseEntity<String> emitir(@PathVariable Long idVenda) throws Exception {

		String resposta = service.emitirNotaFiscalPorVenda(idVenda);

		return ResponseEntity.ok(resposta);
	}

	/*---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------*/

	@PostMapping("/cancelar/{ref}")
	public ResponseEntity<String> cancelar(@PathVariable String ref, @RequestParam String justificativa)
			throws Exception {

		String resposta = service.cancelarNotaFiscal(ref, justificativa);

		return ResponseEntity.ok(resposta);
	}

	/*---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------*/

	@GetMapping("/consultar/{ref}")
	public ResponseEntity<String> consultar(@PathVariable String ref) throws Exception {

		String resposta = service.consultarNotaFiscal(ref);

		return ResponseEntity.ok(resposta);
	}

	/*---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------*/

	@PostMapping("/gravar/{idVenda}")
	public ResponseEntity<NotaFiscalVenda> gravarNotaFiscalVenda(@PathVariable Long idVenda,
			@RequestBody NotaFiscalRetornoEnvioDTO retornoDTO) {

		VendaCompraLojaVirtual venda = vendaCompraLojaVirtualRepository.findById(idVenda)
				.orElseThrow(() -> new RuntimeException("Venda não encontrada: " + idVenda));

		NotaFiscalVenda notaSalva = service.gravaNotaParaVenda(retornoDTO, venda);
		
		
		
		

		return ResponseEntity.ok(notaSalva);
	}

}
