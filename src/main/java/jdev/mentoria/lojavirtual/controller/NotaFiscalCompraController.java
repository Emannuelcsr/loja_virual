package jdev.mentoria.lojavirtual.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import jdev.mentoria.lojavirtual.ExcepetionLojaVirtual;
import jdev.mentoria.lojavirtual.model.NotaFiscalCompra;
import jdev.mentoria.lojavirtual.repository.NotaFiscalCompraRepository;

@RestController
public class NotaFiscalCompraController {

	@Autowired
	private NotaFiscalCompraRepository notaFiscalCompraRepository;

	@SuppressWarnings("unchecked")
	@GetMapping(value = "/buscarPorNotaFiscalPorDescricao/{desc}")
	public ResponseEntity<List<NotaFiscalCompra>> buscarPorNotaFiscalPorDescricao(@PathVariable("desc") String desc) {

		List<NotaFiscalCompra> notaFiscalCompra = notaFiscalCompraRepository
				.buscarNotaFiscalPorDesc(desc.toUpperCase());

		return new ResponseEntity<List<NotaFiscalCompra>>(notaFiscalCompra, HttpStatus.OK);
	}

	@GetMapping(value = "/obterNotaFiscalCompra/{id}")
	public ResponseEntity<NotaFiscalCompra> obterNotaFiscalCompra(@PathVariable("id") Long id)
			throws ExcepetionLojaVirtual {

		NotaFiscalCompra notaFiscalCompra = notaFiscalCompraRepository.findById(id).orElse(null);

		if (notaFiscalCompra == null) {

			throw new ExcepetionLojaVirtual("Não encontou nota fiscal de compra com código: " + id);
		}

		return new ResponseEntity<NotaFiscalCompra>(notaFiscalCompra, HttpStatus.OK);
	}

	@DeleteMapping(value = "/deleteNotaFiscaldeCompraPorId/{id}")
	public ResponseEntity<?> deleteNotaFiscaldeCompraPorId(@PathVariable("id") Long id) {

		notaFiscalCompraRepository.deleteItemNotaFiscalCompra(id);// deleta filho

		notaFiscalCompraRepository.deleteById(id);// deleta pai

		return new ResponseEntity("Nota Fiscal de compra Removida", HttpStatus.OK);
	}

	@ResponseBody
	@PostMapping(value = "/salvarNotaFiscalCompra")
	public ResponseEntity<NotaFiscalCompra> salvarNotaFiscalCompra(
			@RequestBody @Valid NotaFiscalCompra notaFiscalCompra) throws ExcepetionLojaVirtual {

		if (notaFiscalCompra.getId() == null) {

			if (notaFiscalCompra.getDescricaoObs() != null) {

				List<NotaFiscalCompra> fiscalCompras = notaFiscalCompraRepository
						.buscarNotaFiscalPorDesc(notaFiscalCompra.getDescricaoObs().toUpperCase().trim());

				if (!fiscalCompras.isEmpty()) {

					throw new ExcepetionLojaVirtual(
							"Ja existe Nota Fiscal com a descricao: " + notaFiscalCompra.getDescricaoObs());
				}

			}

		}

		if (notaFiscalCompra.getPessoa() == null || notaFiscalCompra.getPessoa().getId() <= 0) {

			throw new ExcepetionLojaVirtual("A pessoa juridica da nota fiscal de compra deve ser informada");

		}

		if (notaFiscalCompra.getEmpresa() == null || notaFiscalCompra.getEmpresa().getId() <= 0) {

			throw new ExcepetionLojaVirtual("A empresa responsavel da nota fiscal de compra deve ser informada");

		}

		
		if (notaFiscalCompra.getContaPagar() == null || notaFiscalCompra.getContaPagar().getId() <= 0) {

			throw new ExcepetionLojaVirtual("A conta a pagar da nota fiscal de compra deve ser informada");

		}

		NotaFiscalCompra notaFiscalCompraSalva = notaFiscalCompraRepository.save(notaFiscalCompra);

		return new ResponseEntity<NotaFiscalCompra>(notaFiscalCompraSalva, HttpStatus.OK);

	}
}
