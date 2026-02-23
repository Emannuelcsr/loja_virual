package jdev.mentoria.lojavirtual.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import jdev.mentoria.lojavirtual.ExcepetionLojaVirtual;
import jdev.mentoria.lojavirtual.model.NotaItemProduto;
import jdev.mentoria.lojavirtual.repository.NotaItemProdutoRepository;

@RestController
public class NotaItemProdutoController {


	@Autowired
	private NotaItemProdutoRepository notaItemProdutoRepository;

	

	@PostMapping(value = "/salvarNotaItemProduto")
	public ResponseEntity<NotaItemProduto> salvarNotaItemProduto(@RequestBody @Valid NotaItemProduto notaItemProduto)
			throws ExcepetionLojaVirtual {

		if (notaItemProduto.getId() == null) {

			if (notaItemProduto.getEmpresa() == null || notaItemProduto.getEmpresa().getId() <= 0) {

				throw new ExcepetionLojaVirtual("A empresa deve ser informado");
			}

			if (notaItemProduto.getProduto() == null || notaItemProduto.getProduto().getId() <= 0) {

				throw new ExcepetionLojaVirtual("O produto deve ser informado");
			}

			if (notaItemProduto.getNotaFiscalCompra() == null || notaItemProduto.getNotaFiscalCompra().getId() <= 0) {

				throw new ExcepetionLojaVirtual("A nota fiscal deve ser informado");
			}

			List<NotaItemProduto> NotaItemProdutoExiste = notaItemProdutoRepository.buscaNotaItemPorProdutoNota(
					notaItemProduto.getProduto().getId(), notaItemProduto.getNotaFiscalCompra().getId());

			if (!NotaItemProdutoExiste.isEmpty()) {

				throw new ExcepetionLojaVirtual("Ja existe esse produto cadastrado para essa nota");
			}
		}

		
		if(notaItemProduto.getQuantidade() <=0) {
			
			throw new ExcepetionLojaVirtual("A quantidade do produto deve ser informada");
		}
		
		NotaItemProduto notaItemProdutoSalvo = notaItemProdutoRepository.save(notaItemProduto);

		return new ResponseEntity<NotaItemProduto>(notaItemProdutoSalvo, HttpStatus.OK);

	}
	
	
	@DeleteMapping(value = "/deleteNotaItemPorId/{id}")
	public ResponseEntity<?> deleteNotaItemPorId(@PathVariable("id") Long id) {

		notaItemProdutoRepository.deleteById(id);

		return new ResponseEntity("Nota Item Removido", HttpStatus.OK);
	}

}
