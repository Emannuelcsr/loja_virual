package jdev.mentoria.lojavirtual.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import jdev.mentoria.lojavirtual.ExcepetionLojaVirtual;
import jdev.mentoria.lojavirtual.model.Acesso;
import jdev.mentoria.lojavirtual.model.CategoriaProduto;
import jdev.mentoria.lojavirtual.model.dto.CategoriaProdutoDTO;
import jdev.mentoria.lojavirtual.repository.CategoriaProdutoRepository;

@RestController
public class CategoriaProdutoController {

	@Autowired
	private CategoriaProdutoRepository categoriaProdutoRepository;

	@PostMapping(value = "/salvarcategoria")
	public ResponseEntity<CategoriaProdutoDTO> salvarCategoria(@RequestBody CategoriaProduto categoriaProduto)
			throws ExcepetionLojaVirtual {

		if (categoriaProduto.getEmpresa() == null || (categoriaProduto.getEmpresa().getId() == null)) {

			throw new ExcepetionLojaVirtual("Informe a empresa");
		}

		if (categoriaProduto.getId() == null
				&& categoriaProdutoRepository.existeCategoria(categoriaProduto.getNomeDesc().toUpperCase().trim())) {

			throw new ExcepetionLojaVirtual("Não pode cadastrar categoria com mesmo nome");

		}

		CategoriaProduto categoriaSalva = categoriaProdutoRepository.save(categoriaProduto);

		CategoriaProdutoDTO categoriaProdutoDTO = new CategoriaProdutoDTO();

		categoriaProdutoDTO.setId(categoriaSalva.getId());
		categoriaProdutoDTO.setNomeDesc(categoriaSalva.getNomeDesc());

		categoriaProdutoDTO.setEmpresa(categoriaSalva.getEmpresa().getId().toString());

		return new ResponseEntity<CategoriaProdutoDTO>(categoriaProdutoDTO, HttpStatus.OK);
	}

	
	
	@ResponseBody // Ele pega o objeto Java que seu método retorna e transforma em JSON pra mandar
	// de volta pra tela.
	@PostMapping(value = "/deletecategoria")
	public ResponseEntity<?> deletecategoria(@RequestBody CategoriaProduto categoriaProduto) {// requestBody transforma JSON da tela em objeto

		if(!categoriaProdutoRepository.findById(categoriaProduto.getId()).isPresent()) {
			
			return new ResponseEntity("Categoria já foi Removida", HttpStatus.OK);

		}
		
		
		categoriaProdutoRepository.deleteById(categoriaProduto.getId());

		return new ResponseEntity("Categoria Removida", HttpStatus.OK);
	}
	
	
	@GetMapping(value = "/buscarPorCatDescricao/{desc}")
	public ResponseEntity<List<CategoriaProduto>> buscarPorCatDescricao(@PathVariable("desc") String desc) {

		List<CategoriaProduto> categoriaProduto = categoriaProdutoRepository.buscarCategoriaDesc(desc.toUpperCase());

		return new ResponseEntity<List<CategoriaProduto>>(categoriaProduto, HttpStatus.OK);
	}

}
