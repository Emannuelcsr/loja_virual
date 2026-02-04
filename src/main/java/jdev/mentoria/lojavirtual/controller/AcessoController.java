package jdev.mentoria.lojavirtual.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import jdev.mentoria.lojavirtual.model.Acesso;
import jdev.mentoria.lojavirtual.repository.AcessoRepository;
import jdev.mentoria.lojavirtual.service.AcessoService;

@RestController
public class AcessoController {

	@Autowired
	private AcessoService acessoService;
	
	@Autowired
	private AcessoRepository acessoRepository;
	
	@ResponseBody //Ele pega o objeto Java que seu método retorna e transforma em JSON pra mandar de volta pra tela.
	@PostMapping(value = "/salvarAcesso")
	public ResponseEntity<Acesso> salvarAcesso(@RequestBody Acesso acesso) {//requestBody transforma JSON da tela em objeto
		
		Acesso acessoSalvo = acessoService.save(acesso);
		
		return new ResponseEntity<Acesso>(acessoSalvo,HttpStatus.OK);
	}
	
	@ResponseBody //Ele pega o objeto Java que seu método retorna e transforma em JSON pra mandar de volta pra tela.
	@PostMapping(value = "/deleteAcesso")
	public ResponseEntity<?> deleteAcesso(@RequestBody Acesso acesso) {//requestBody transforma JSON da tela em objeto
		
		acessoRepository.deleteById(acesso.getId());
		
		return new ResponseEntity(HttpStatus.OK);
	}
	
}
