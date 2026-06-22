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

import com.google.gson.Gson;

import jakarta.validation.Valid;
import jdev.mentoria.lojavirtual.ExcepetionLojaVirtual;
import jdev.mentoria.lojavirtual.model.FormaPagamento;
import jdev.mentoria.lojavirtual.repository.FormaPagamentoRepository;

@RestController
public class FormaPagamentoController {

	@Autowired
	private FormaPagamentoRepository formaPagamentoRepository;
	
	@ResponseBody 					
	@PostMapping(value = "/salvarFormaPagamento")
	public ResponseEntity<FormaPagamento> salvarFormaPagamento(@RequestBody @Valid FormaPagamento formaPagamento) throws ExcepetionLojaVirtual {
		

		System.out.println(formaPagamento);
		FormaPagamento formaPagamentoSalva = formaPagamentoRepository.save(formaPagamento);

		return new ResponseEntity<FormaPagamento>(formaPagamentoSalva, HttpStatus.OK);
	}
	
					
	
	@GetMapping("/listFormaPagamento")
	public ResponseEntity<List<FormaPagamento>> listFormaPagamento(){
		
		
		
		
		return new ResponseEntity<List<FormaPagamento>>(formaPagamentoRepository.findAll(),HttpStatus.OK);
	}
	
	
	@GetMapping("/listFormaPagamentoPorEmpresa/{idEmpresa}")
	public ResponseEntity<List<FormaPagamento>> listFormaPagamentoPorEmpresa(@PathVariable("idEmpresa")Long idEmpresa){
		
		
		
		
		return new ResponseEntity<List<FormaPagamento>>(formaPagamentoRepository.findAll(idEmpresa),HttpStatus.OK);
	}
	
	
	@ResponseBody 
	@PostMapping(value = "/deleteFormaPagamento")
	public ResponseEntity<?> deleteFormaPagamento(@RequestBody FormaPagamento formaPagamento) throws ExcepetionLojaVirtual {

		
		if(formaPagamentoRepository.findById(formaPagamento.getId()).isPresent()==false) {
			throw new ExcepetionLojaVirtual("Forma de pagamento ja deletada");
		}
		
		
		formaPagamentoRepository.deleteById(formaPagamento.getId());
		
	

		
		return new ResponseEntity<String>(new Gson().toJson("Forma de pagamento removida"), HttpStatus.OK);
	}
	
	@GetMapping(value = "/obterFormaPagamento/{id}")// na vdd é pra editar
	public ResponseEntity<FormaPagamento> obterFormaPagamento(@PathVariable("id") Long id) throws ExcepetionLojaVirtual {

		
		
		
		FormaPagamento formaPagamento = formaPagamentoRepository.findById(id).orElse(null);

		if (formaPagamento == null) {

			throw new ExcepetionLojaVirtual("Não encontou forma de pagamento com código: " + id);
		}

		return new ResponseEntity<FormaPagamento>(formaPagamento, HttpStatus.OK);
	}
	
	
	
	
	
}
