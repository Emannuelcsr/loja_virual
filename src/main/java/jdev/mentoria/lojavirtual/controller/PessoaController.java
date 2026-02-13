package jdev.mentoria.lojavirtual.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jdev.mentoria.lojavirtual.ExcepetionLojaVirtual;
import jdev.mentoria.lojavirtual.model.PessoaJuridica;
import jdev.mentoria.lojavirtual.repository.PessoaRepository;
import jdev.mentoria.lojavirtual.service.PessoaUserService;

@RestController
public class PessoaController {
	
	@Autowired
	private PessoaRepository pessoaRepository; 
	
	@Autowired
	private PessoaUserService pessoaUserService;
	
	@PostMapping(value = "/salvarpj")
	public ResponseEntity<PessoaJuridica> salvarPJ(@RequestBody PessoaJuridica pessoaJuridica) throws ExcepetionLojaVirtual{
		
		if(pessoaJuridica == null) {
			throw new ExcepetionLojaVirtual("Pessoa juridica não pode ser NULL");
		}
		
		if(pessoaJuridica.getId() == null && pessoaRepository.existeCnpjCadastrado(pessoaJuridica.getCnpj()) != null) {
			
			throw new ExcepetionLojaVirtual("Ja existe CNPJ cadastrado com o numero: "+ pessoaJuridica.getCnpj());
			
		}
		
		
		pessoaJuridica = pessoaUserService.salvarPessoaJuridica(pessoaJuridica);
		
		
		return new ResponseEntity<PessoaJuridica>(pessoaJuridica,HttpStatus.OK);
		
	}
	 
}
