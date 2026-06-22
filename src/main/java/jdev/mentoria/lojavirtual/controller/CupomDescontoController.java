package jdev.mentoria.lojavirtual.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
import jdev.mentoria.lojavirtual.model.Acesso;
import jdev.mentoria.lojavirtual.model.CupomDesconto;
import jdev.mentoria.lojavirtual.model.MarcaProduto;
import jdev.mentoria.lojavirtual.model.PessoaJuridica;
import jdev.mentoria.lojavirtual.repository.CupomDescontoRepository;

@RestController
public class CupomDescontoController {

	@Autowired
	private CupomDescontoRepository cupomDescontoRepository;

	@GetMapping("/obterCupomDescontoPorEmpresa")
	public ResponseEntity<List<CupomDesconto>> obterCupomDescontoPorEmpresa(@PathVariable("idEmpresa") Long idEmpresa) {

		return new ResponseEntity<List<CupomDesconto>>(cupomDescontoRepository.cupomDescontoPorEmpresa(idEmpresa),
				HttpStatus.OK);
	}

	@GetMapping("/obterCupomDesconto")
	public ResponseEntity<List<CupomDesconto>> obterCupomDesconto() {

		return new ResponseEntity<List<CupomDesconto>>(cupomDescontoRepository.findAll(), HttpStatus.OK);
	}

	@PostMapping("/salvarCupomDesconto")
	public ResponseEntity<CupomDesconto> salvarCupomDesconto(@RequestBody @Valid CupomDesconto cupomDesconto)
			throws ExcepetionLojaVirtual {

		if (cupomDesconto.getId() == null) {

			List<CupomDesconto> cupomDescontosList = cupomDescontoRepository
					.buscaCupomDesc(cupomDesconto.getCodDescricao());

			if (!cupomDescontosList.isEmpty()) {

				throw new ExcepetionLojaVirtual(
						"Ja existe cupom de desconto com a descricao: " + cupomDesconto.getCodDescricao());
			}

		}

		CupomDesconto cupomSalvo = cupomDescontoRepository.save(cupomDesconto);

		return new ResponseEntity<CupomDesconto>(cupomSalvo, HttpStatus.OK);
	}

	@DeleteMapping(value = "/deleteCupomPorId/{id}")
	public ResponseEntity<?> deleteCupomPorId(@PathVariable("id") Long id) {

		cupomDescontoRepository.deleteById(id);

		return new ResponseEntity("Cupom Removido", HttpStatus.OK);
	}

	@GetMapping(value = "/obterCupom/{id}")
	public ResponseEntity<CupomDesconto> obterCupom(@PathVariable("id") Long id) throws ExcepetionLojaVirtual {

		CupomDesconto  cupomDesconto = cupomDescontoRepository.findById(id).orElse(null);

		if (cupomDesconto == null) {

			throw new ExcepetionLojaVirtual("Não encontou Cupom com código: " + id);
		}

		return new ResponseEntity<CupomDesconto>(cupomDesconto, HttpStatus.OK);
	}

	
	
	@GetMapping(value = "/listaPorPageCupons/{codEmp}/{pagina}")
	public ResponseEntity<List<CupomDesconto>> listaPorPageCupons(@PathVariable("codEmp") Long codEmp,
			@PathVariable("pagina") Integer pagina) {

		org.springframework.data.domain.Pageable pageable = PageRequest.of(pagina - 1, 5, Sort.by("codDescricao"));

		List<CupomDesconto> lista = cupomDescontoRepository.findbyPage(codEmp, pageable);

		return new ResponseEntity<List<CupomDesconto>>(lista, HttpStatus.OK);
	}
	
	
	
	@GetMapping(value = "/qtdadePaginaCupons/{codEmp}")
	public ResponseEntity<Map<String, Integer>> qtdadePaginaCupons(@PathVariable("codEmp") Long codEmp) {

		Integer qtdadePagina = cupomDescontoRepository.quantidadePagina(codEmp);

		Map<String, Integer> resposta = new HashMap<>();

		resposta.put("resposta", qtdadePagina);

		return new ResponseEntity<Map<String, Integer>>(resposta, HttpStatus.OK);
	}
	
}
