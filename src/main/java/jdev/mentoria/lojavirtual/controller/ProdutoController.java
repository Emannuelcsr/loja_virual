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
import jdev.mentoria.lojavirtual.model.Produto;
import jdev.mentoria.lojavirtual.repository.ProdutoRepository;

@RestController
public class ProdutoController {

	@Autowired
	private ProdutoRepository produtoRepository;

	@ResponseBody // Ele pega o objeto Java que seu método retorna e transforma em JSON pra mandar
					// de volta pra tela.
	@PostMapping(value = "/salvarProduto")
	public ResponseEntity<Produto> salvarProduto(@RequestBody @Valid Produto produto) throws ExcepetionLojaVirtual {
		
		
		if(produto.getEmpresa() == null || produto.getEmpresa().getId() <=0) {
			
			throw new ExcepetionLojaVirtual("A empresa responsável deve ser informada");
		}

		if (produto.getId() == null) {
			List<Produto> produtos = produtoRepository.buscarProdutoNome(produto.getNome().toUpperCase(),produto.getEmpresa().getId());

			
		    if (!produtos.isEmpty()) {
		        throw new ExcepetionLojaVirtual("Já existe produto com o nome: " + produto.getNome());
		    }
		}
		
		

		
		
		if(produto.getCategoriaProduto() == null || produto.getCategoriaProduto().getId() <=0) {
			
			throw new ExcepetionLojaVirtual("Categoria deve ser informada");
		}
		
		
		if(produto.getMarcaProduto() == null ||  produto.getMarcaProduto().getId() <=0) {
			
			throw new ExcepetionLojaVirtual("Marca deve ser informada");
		}
		

		Produto produtoSalvo = produtoRepository.save(produto);

		return new ResponseEntity<Produto>(produtoSalvo, HttpStatus.OK);
	}

	@ResponseBody // Ele pega o objeto Java que seu método retorna e transforma em JSON pra mandar
					// de volta pra tela.
	@PostMapping(value = "/deleteProduto")
	public ResponseEntity<?> deleteProduto(@RequestBody Produto produto) {// requestBody transforma JSON da tela em
																			// objeto

		produtoRepository.deleteById(produto.getId());

		return new ResponseEntity("Produto Removido", HttpStatus.OK);
	}

	@DeleteMapping(value = "/deleteProdutoPorId/{id}")
	public ResponseEntity<?> deleteProdutoPorId(@PathVariable("id") Long id) {

		produtoRepository.deleteById(id);

		return new ResponseEntity("Produto Removido", HttpStatus.OK);
	}

	@GetMapping(value = "/obterProduto/{id}")
	public ResponseEntity<Produto> obterProduto(@PathVariable("id") Long id) throws ExcepetionLojaVirtual {

		Produto produto = produtoRepository.findById(id).orElse(null);

		if (produto == null) {

			throw new ExcepetionLojaVirtual("Não encontou produto com código: " + id);
		}

		return new ResponseEntity<Produto>(produto, HttpStatus.OK);
	}

	@GetMapping(value = "/buscarProdutoPorNome/{desc}")
	public ResponseEntity<List<Produto>> buscarPorDescricao(@PathVariable("desc") String desc) {

		List<Produto> produtos = produtoRepository.buscarProdutoNome(desc.toUpperCase());

		return new ResponseEntity<List<Produto>>(produtos, HttpStatus.OK);
	}

}
