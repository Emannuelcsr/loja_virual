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
import jdev.mentoria.lojavirtual.model.ContaPagar;
import jdev.mentoria.lojavirtual.repository.ContaPagarRepository;

@RestController
public class ContaPagarController {

	@Autowired
	private ContaPagarRepository contaPagarRepository;

	@ResponseBody // Ele pega o objeto Java que seu método retorna e transforma em JSON pra mandar
					// de volta pra tela.
	@PostMapping(value = "/salvarContaPagar")
	public ResponseEntity<ContaPagar> salvarContaPagar(@RequestBody @Valid ContaPagar contaPagar)
			throws ExcepetionLojaVirtual {

		
		System.out.println("TIPO pessoa recebido = " 
			    + (contaPagar.getPessoa() == null ? "null" : contaPagar.getPessoa().getClass().getName()));
			System.out.println("ID pessoa recebido = " 
			    + (contaPagar.getPessoa() == null ? "null" : contaPagar.getPessoa().getId()));
		if (contaPagar.getEmpresa() == null || contaPagar.getEmpresa().getId() <= 0) {

			throw new ExcepetionLojaVirtual("A empresa responsável deve ser informada");
		}

		
		if (contaPagar.getPessoa() == null || contaPagar.getPessoa().getId() <= 0) {

			throw new ExcepetionLojaVirtual("Pessoa responsável deve ser informada");
		}

		
		if (contaPagar.getPessoa_fornecedor() == null || contaPagar.getPessoa_fornecedor().getId() <= 0) {

			throw new ExcepetionLojaVirtual("Fornecedor responsável deve ser informada");
		}
		
		
		
		if(contaPagar.getId() == null ) {
			
			List<ContaPagar> contaPagars =   contaPagarRepository.buscaContaDesc(contaPagar.getDescricao().toUpperCase().trim());
			
			if(!contaPagars.isEmpty()) {

				throw new ExcepetionLojaVirtual("Ja existe conta a pagar com a mesma descricao");
				
			}
		}
		
	
		
		
		
		
		ContaPagar ContaPagarSalvo = contaPagarRepository.save(contaPagar);

		return new ResponseEntity<ContaPagar>(ContaPagarSalvo, HttpStatus.OK);
	}

	@ResponseBody
	@PostMapping(value = "/deleteContaPagar")
	public ResponseEntity<?> deleteContaPagar(@RequestBody ContaPagar contaPagar) {

		contaPagarRepository.deleteById(contaPagar.getId());

		return new ResponseEntity("Conta a Pagar Removido", HttpStatus.OK);
	}

	@DeleteMapping(value = "/deleteContaPagarPorId/{id}")
	public ResponseEntity<?> deleteContaPagarPorId(@PathVariable("id") Long id) {

		contaPagarRepository.deleteById(id);

		return new ResponseEntity("Conta a Pagar Removido", HttpStatus.OK);
	}

	@GetMapping(value = "/obterContaPagar/{id}")
	public ResponseEntity<ContaPagar> obterContaPagar(@PathVariable("id") Long id) throws ExcepetionLojaVirtual {

		ContaPagar contaPagar = contaPagarRepository.findById(id).orElse(null);

		if (contaPagar == null) {

			throw new ExcepetionLojaVirtual("Não encontou Conta a Pagar com código: " + id);
		}

		return new ResponseEntity<ContaPagar>(contaPagar, HttpStatus.OK);
	}

	@GetMapping(value = "/buscarContaPagarPorNome/{desc}")
	public ResponseEntity<List<ContaPagar>> buscarContaPagarPorNome(@PathVariable("desc") String desc) {

		List<ContaPagar> contaPagar = contaPagarRepository.buscaContaDesc(desc.toUpperCase());

		return new ResponseEntity<List<ContaPagar>>(contaPagar, HttpStatus.OK);
	}

}
