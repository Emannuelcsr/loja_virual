package jdev.mentoria.lojavirtual.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jdev.mentoria.lojavirtual.model.ImagemProduto;
import jdev.mentoria.lojavirtual.model.dto.ImagemProdutoDTO;
import jdev.mentoria.lojavirtual.repository.ImagemProdutoRepository;

@RestController
public class ImagemProdutoController {

	private final Executor getAsyncExecutor;

	@Autowired
	private ImagemProdutoRepository imagemProdutoRepository;

	ImagemProdutoController(Executor getAsyncExecutor) {
		this.getAsyncExecutor = getAsyncExecutor;
	}

	@GetMapping(value = "/obterImagemProduto/{idProduto}")
	public ResponseEntity<List<ImagemProdutoDTO>> obterImagemProduto(@PathVariable("idProduto") Long idProduto) {

	    List<ImagemProdutoDTO> dtos = new ArrayList<>();

	    List<ImagemProduto> imagemProdutoSalva = imagemProdutoRepository.buscaImagemProduto(idProduto);

	    for (ImagemProduto imagemProduto : imagemProdutoSalva) {

	        ImagemProdutoDTO dto = new ImagemProdutoDTO();

	        dto.setId(imagemProduto.getId());
	        dto.setEmpresa(imagemProduto.getEmpresa().getId());
	        dto.setProduto(imagemProduto.getProduto().getId());
	        dto.setImagemMiniatura(imagemProduto.getImagemMiniatura());
	        dto.setImagemOriginal(imagemProduto.getImagemOriginal());

	        dtos.add(dto);
	    }

	    return new ResponseEntity<>(dtos, HttpStatus.OK);
	}

	@DeleteMapping("/deleteImagemProduto/{idProduto}")
	public ResponseEntity<?> deleteImagemProduto(@PathVariable("idProduto") Long idProduto) {

		imagemProdutoRepository.deleteById(idProduto);

		return new ResponseEntity("Imagem Removida", HttpStatus.OK);

	}

	@DeleteMapping("/deleteImagemObjeto")
	public ResponseEntity<?> deleteImagemProduto(@RequestBody ImagemProduto imagemProduto) {

		imagemProdutoRepository.deleteById(imagemProduto.getId());

		return new ResponseEntity("Imagem do produto Removida", HttpStatus.OK);

	}

	@DeleteMapping("/deleteTodasImagensProduto/{idProduto}")
	public ResponseEntity<?> deleteTodasImagensProduto(@PathVariable("idProduto") Long idProduto) {

		imagemProdutoRepository.deleteImagens(idProduto);

		return new ResponseEntity<String>("Todas imagens do produto foram Removida", HttpStatus.OK);

	}

	@PostMapping(value = "/salvarImagemProduto")
	public ResponseEntity<ImagemProdutoDTO> salvarImagemProduto(@RequestBody ImagemProduto imagemProduto) {

		ImagemProduto produtoSalvo = imagemProdutoRepository.saveAndFlush(imagemProduto);

		ImagemProdutoDTO imagemProdutoDTO = new ImagemProdutoDTO();

		imagemProdutoDTO.setId(imagemProduto.getId());
		imagemProdutoDTO.setEmpresa(imagemProduto.getEmpresa().getId());
		imagemProdutoDTO.setProduto(imagemProduto.getProduto().getId());
		imagemProdutoDTO.setImagemMiniatura(imagemProduto.getImagemMiniatura());
		imagemProdutoDTO.setImagemOriginal(imagemProduto.getImagemOriginal());

		return new ResponseEntity<ImagemProdutoDTO>(imagemProdutoDTO, HttpStatus.OK);
	}
}