package jdev.mentoria.lojavirtual.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import jdev.mentoria.lojavirtual.model.CupomDesconto;
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

		return new ResponseEntity<List<CupomDesconto>>(cupomDescontoRepository.findAll(),
				HttpStatus.OK);
	}

}
