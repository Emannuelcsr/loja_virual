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
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import jdev.mentoria.lojavirtual.ExcepetionLojaVirtual;
import jdev.mentoria.lojavirtual.model.AvaliacaoProduto;
import jdev.mentoria.lojavirtual.model.dto.AvaliacaoProdutoDTO;
import jdev.mentoria.lojavirtual.repository.AvaliacaoProdutoRepository;

@RestController
public class AvaliacaoProdutoController {

	@Autowired
	private AvaliacaoProdutoRepository avaliacaoProdutoRepository;

	@PostMapping(value = "/salvarAvaliacaoProduto")
	public ResponseEntity<AvaliacaoProduto> salvarAvaliacaoProduto(
			@RequestBody @Valid AvaliacaoProduto avaliacaoProduto) throws ExcepetionLojaVirtual {

		if (avaliacaoProduto.getEmpresa() == null || avaliacaoProduto.getEmpresa().getId() <= 0) {

			throw new ExcepetionLojaVirtual("Informe a empresa a ser avaliada");

		}

		if (avaliacaoProduto.getProduto() == null || avaliacaoProduto.getProduto().getId() <= 0) {

			throw new ExcepetionLojaVirtual("Avaliação deve conter o produto associado");
		}

		if (avaliacaoProduto.getPessoa() == null || avaliacaoProduto.getPessoa().getId() <= 0) {

			throw new ExcepetionLojaVirtual("Avaliação deve conter o cliente associado");
		}

		AvaliacaoProduto avaliacaoProdutoSalva = avaliacaoProdutoRepository.save(avaliacaoProduto);

		return new ResponseEntity<AvaliacaoProduto>(avaliacaoProdutoSalva, HttpStatus.OK);
	}

	@DeleteMapping(value = "/deleteAvaliacaoPorId/{id}")
	public ResponseEntity<?> deleteAvaliacaoPorId(@PathVariable("id") Long id) {

		avaliacaoProdutoRepository.deleteById(id);

		return new ResponseEntity("Avaliacao Removida", HttpStatus.OK);
	}

	@GetMapping("/obterListaDeAvaliacoesDoProduto/{id}")
	public ResponseEntity<List<AvaliacaoProdutoDTO>> obterListaDeAvaliacoesDoProduto(@PathVariable Long id) {

		List<AvaliacaoProduto> produtosList = avaliacaoProdutoRepository.findByProdutoId(id);

		List<AvaliacaoProdutoDTO> dtos = produtosList.stream().map(av -> {
			AvaliacaoProdutoDTO dto = new AvaliacaoProdutoDTO();
			dto.setId(av.getId());
			dto.setNota(av.getNota());
			dto.setDescricao(av.getDescricao());
			dto.setPessoaId(av.getPessoa().getId());
			dto.setEmpresaId(av.getEmpresa().getId());
			dto.setProdutoId(av.getProduto().getId());
			return dto;
		}).toList();

		return ResponseEntity.ok(dtos);
	}

	@GetMapping("/obterListaDeAvaliacoesDoProduto/{idPessoa}/{idProduto}")
	public ResponseEntity<List<AvaliacaoProdutoDTO>> obterListaDeAvaliacoesDoProdutoPorPessoa(
			@PathVariable Long idPessoa, @PathVariable Long idProduto) {

		List<AvaliacaoProduto> produtosPessoaList = avaliacaoProdutoRepository.findByProdutoPessoaId(idPessoa,
				idProduto);


		List<AvaliacaoProdutoDTO> dtos = produtosPessoaList.stream().map(av -> {
			AvaliacaoProdutoDTO dto = new AvaliacaoProdutoDTO();
			dto.setId(av.getId());
			dto.setNota(av.getNota());
			dto.setDescricao(av.getDescricao());
			dto.setPessoaId(av.getPessoa().getId());
			dto.setEmpresaId(av.getEmpresa().getId());
			dto.setProdutoId(av.getProduto().getId());
			return dto;
		}).toList();

		return ResponseEntity.ok(dtos);
	}

	@GetMapping("/obterListaDeAvaliacoeDePessoa/{idPessoa}")
	public ResponseEntity<List<AvaliacaoProdutoDTO>> obterListaDeAvaliacoesDaPessoa(@PathVariable Long idPessoa) {

		List<AvaliacaoProduto> avaliacaoPessoaList = avaliacaoProdutoRepository.findByPessoaId(idPessoa);

		List<AvaliacaoProdutoDTO> dtos = avaliacaoPessoaList.stream().map(av -> {
			AvaliacaoProdutoDTO dto = new AvaliacaoProdutoDTO();
			dto.setId(av.getId());
			dto.setNota(av.getNota());
			dto.setDescricao(av.getDescricao());
			dto.setPessoaId(av.getPessoa().getId());
			dto.setEmpresaId(av.getEmpresa().getId());
			dto.setProdutoId(av.getProduto().getId());
			return dto;
		}).toList();

		return ResponseEntity.ok(dtos);
	}

}
