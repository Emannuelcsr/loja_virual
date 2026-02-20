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
import jdev.mentoria.lojavirtual.model.Acesso;
import jdev.mentoria.lojavirtual.model.MarcaProduto;
import jdev.mentoria.lojavirtual.repository.AcessoRepository;
import jdev.mentoria.lojavirtual.repository.MarcaProdutoRepository;
import jdev.mentoria.lojavirtual.service.AcessoService;

@RestController
public class MarcaProdutoController {


	@Autowired
	private MarcaProdutoRepository marcaProdutoRepository;

	@ResponseBody 					
	@PostMapping(value = "/salvarMarca")
	public ResponseEntity<MarcaProduto> salvarMarca(@RequestBody @Valid MarcaProduto marcaProduto) throws ExcepetionLojaVirtual {
		
		if (marcaProduto.getId() == null) {
			List<MarcaProduto> marcaProdutoList = marcaProdutoRepository.buscarMarcaDesc(marcaProduto.getNomeDesc().toUpperCase());

			if (!marcaProdutoList.isEmpty()) {

				throw new ExcepetionLojaVirtual("Ja existe marca com a descricao: " + marcaProduto.getNomeDesc());
			}

		}

		MarcaProduto marcaProdutoSalva = marcaProdutoRepository.save(marcaProduto);

		return new ResponseEntity<MarcaProduto>(marcaProdutoSalva, HttpStatus.OK);
	}

	
	
	
	
	
	@ResponseBody // Ele pega o objeto Java que seu método retorna e transforma em JSON pra mandar
					// de volta pra tela.
	@PostMapping(value = "/deleteMarca")
	public ResponseEntity<?> deleteMarca(@RequestBody MarcaProduto marcaProduto) {// requestBody transforma JSON da tela em objeto

		marcaProdutoRepository.deleteById(marcaProduto.getId());

		return new ResponseEntity("Marca Removida", HttpStatus.OK);
	}
	
	
	

	@DeleteMapping(value = "/deleteMarcaPorId/{id}")
	public ResponseEntity<?> deleteMarcaPorId(@PathVariable("id") Long id) {

		marcaProdutoRepository.deleteById(id);

		return new ResponseEntity("Marca Removida", HttpStatus.OK);
	}

	@GetMapping(value = "/obterMarca/{id}")
	public ResponseEntity<MarcaProduto> obterMarca(@PathVariable("id") Long id) throws ExcepetionLojaVirtual {

		MarcaProduto marcaProduto = marcaProdutoRepository.findById(id).orElse(null);

		if (marcaProduto == null) {

			throw new ExcepetionLojaVirtual("Não encontou marca com código: " + id);
		}

		return new ResponseEntity<MarcaProduto>(marcaProduto, HttpStatus.OK);
	}

	@GetMapping(value = "/buscarPorMarcaPorDescricao/{desc}")
	public ResponseEntity<List<MarcaProduto>> buscarPorMarcaPorDescricao(@PathVariable("desc") String desc) {

		List<MarcaProduto> marcaProdutos = marcaProdutoRepository.buscarMarcaDesc(desc.toUpperCase());

		return new ResponseEntity<List<MarcaProduto>>(marcaProdutos, HttpStatus.OK);
	}

}
