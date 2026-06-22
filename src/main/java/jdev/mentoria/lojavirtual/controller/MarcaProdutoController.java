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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import jdev.mentoria.lojavirtual.ExcepetionLojaVirtual;
import jdev.mentoria.lojavirtual.model.MarcaProduto;
import jdev.mentoria.lojavirtual.model.dto.MarcaProdutoDTO;
import jdev.mentoria.lojavirtual.repository.MarcaProdutoRepository;

@RestController
public class MarcaProdutoController {


	@Autowired
	private MarcaProdutoRepository marcaProdutoRepository;



	@PostMapping(value = "/salvarmarca")
	public ResponseEntity<Map<String, MarcaProdutoDTO>> salvarmarca(
	        @RequestBody MarcaProduto marcaProduto) throws ExcepetionLojaVirtual {

	    if (marcaProduto.getEmpresa() == null || marcaProduto.getEmpresa().getId() == null) {
	        throw new ExcepetionLojaVirtual("Informe a empresa");
	    }

	    if (marcaProduto.getId() == null
	            && marcaProdutoRepository.existeMarca(marcaProduto.getNomeDesc().toUpperCase().trim())) {

	        throw new ExcepetionLojaVirtual("Não pode cadastrar marca com mesmo nome");
	    }

	    MarcaProduto marcaSalva = marcaProdutoRepository.save(marcaProduto);

	    MarcaProdutoDTO marcaProdutoDTO = new MarcaProdutoDTO();

	    marcaProdutoDTO.setId(marcaSalva.getId());
	    marcaProdutoDTO.setNomeDesc(marcaSalva.getNomeDesc());
	    marcaProdutoDTO.setEmpresa(marcaSalva.getEmpresa().getId().toString());

	    Map<String, MarcaProdutoDTO> resposta = new HashMap<>();

	    resposta.put("resposta", marcaProdutoDTO);

	    return new ResponseEntity<Map<String, MarcaProdutoDTO>>(resposta, HttpStatus.OK);
	}
	
	
	
	
	@ResponseBody // Ele pega o objeto Java que seu método retorna e transforma em JSON pra mandar
					// de volta pra tela.
	@PostMapping(value = "/deletemarca")
	public ResponseEntity<?> deletemarca(@RequestBody MarcaProduto marcaProduto) {// requestBody transforma JSON da tela em objeto

		marcaProdutoRepository.deleteById(marcaProduto.getId());

		return new ResponseEntity("Marca Removida", HttpStatus.OK);
	}
	
	
	

	@DeleteMapping(value = "/deleteMarcaPorId/{id}")
	public ResponseEntity<?> deleteMarcaPorId(@PathVariable("id") Long id) {

		marcaProdutoRepository.deleteById(id);

		return new ResponseEntity("Marca Removida", HttpStatus.OK);
	}

	@GetMapping(value = "/obterMarca/{id}")
	public ResponseEntity<MarcaProduto> obterMarca(@PathVariable("id") Long id) throws ExcepetionLojaVirtual {

		MarcaProduto marcaProduto = marcaProdutoRepository.findById(id).orElse(null);

		if (marcaProduto == null) {

			throw new ExcepetionLojaVirtual("Não encontou marca com código: " + id);
		}

		return new ResponseEntity<MarcaProduto>(marcaProduto, HttpStatus.OK);
	}

	@GetMapping(value = "/buscarPorMarcaPorDescricao/{desc}")
	public ResponseEntity<List<MarcaProduto>> buscarPorMarcaPorDescricao(@PathVariable("desc") String desc) {

		List<MarcaProduto> marcaProdutos = marcaProdutoRepository.buscarMarcaDesc(desc.toUpperCase());

		return new ResponseEntity<List<MarcaProduto>>(marcaProdutos, HttpStatus.OK);
	}
	
	
	
	@GetMapping(value = "/qtdadePaginaMarcaProduto/{codEmp}")
	public ResponseEntity<Map<String, Integer>> qtdadePaginaMarcaProduto(@PathVariable("codEmp") Long codEmp){
		
		Integer qtdadePagina = marcaProdutoRepository.quantidadePagina(codEmp);
		
		Map<String, Integer> resposta = new HashMap<>();
		resposta.put("resposta", qtdadePagina);
		
		
		return new ResponseEntity<Map<String, Integer>>(resposta,HttpStatus.OK);	 	
	}
	
	
	@GetMapping(value = "/buscarPorCatMarcaPorEmpresa/{desc}/{empresa}")
	public ResponseEntity<Map<String, List<MarcaProduto>>> buscarPorCatMarcaPorEmpresa(
	        @PathVariable("desc") String desc,@PathVariable("empresa") String empresa) {

	    List<MarcaProduto> marcaProduto = marcaProdutoRepository.buscarMarcaDesc(desc.toUpperCase(),empresa);

	    Map<String, List<MarcaProduto>> resposta = new HashMap<>();
	    resposta.put("resposta", marcaProduto);

	    return new ResponseEntity<Map<String, List<MarcaProduto>>>(resposta, HttpStatus.OK); 
	}
	
	@GetMapping(value = "/buscarMarcaporid/{id}")
	public ResponseEntity<Map<String, MarcaProduto>> buscarMarcaporid(@PathVariable("id") Long id) throws ExcepetionLojaVirtual {

		MarcaProduto marcaProduto = marcaProdutoRepository.findById(id).get();

		if(marcaProduto == null) {
			
			throw new ExcepetionLojaVirtual("Não encontrou marca com o código: "+id);
		}
		
	    Map<String, MarcaProduto> resposta = new HashMap<>();
	    resposta.put("resposta", marcaProduto);
		
		
		return new ResponseEntity<Map<String, MarcaProduto>>(resposta, HttpStatus.OK); 
	}

	
	@GetMapping(value = "/listaPorPageMarcaProduto/{codEmp}/{pagina}")
	public ResponseEntity<List<MarcaProduto>> listaPorPageMarcaProduto(
	        @PathVariable("codEmp") Long codEmp,
	        @PathVariable("pagina") Integer pagina) {
		
	    org.springframework.data.domain.Pageable pageable = PageRequest.of(pagina - 1, 5, Sort.by("nomeDesc"));

	    List<MarcaProduto> lista = marcaProdutoRepository.findbyPage(codEmp,pageable);

	    return new ResponseEntity<List<MarcaProduto>>(lista, HttpStatus.OK);
	}
	
	

	
	
 
    @GetMapping(value = "/listarmarcaproduto/{codEmp}")
    public ResponseEntity<List<MarcaProduto>> listarmarcaproduto(
            @PathVariable("codEmp") Long codEmp) {

        List<MarcaProduto> marcaProduto =
        		marcaProdutoRepository.findAll(codEmp);

        return new ResponseEntity<List<MarcaProduto>>(marcaProduto, HttpStatus.OK);
    }

    
    
    @GetMapping(value = "/buscarporidMarca/{id}")
    public ResponseEntity<Map<String, MarcaProduto>> buscarporidMarca(
            @PathVariable("id") Long id) {

    	MarcaProduto marcaProduto = marcaProdutoRepository.findById(id).get();

        Map<String, MarcaProduto> resposta = new HashMap<>();

        resposta.put("resposta", marcaProduto);

        return new ResponseEntity<Map<String, MarcaProduto>>(resposta, HttpStatus.OK);
    }

    
    @GetMapping(value = "/quantidadeDeMarcas/{codEmp}")
    public ResponseEntity<Integer> quantidadeDeMarcas(@PathVariable("codEmp")Long codEmp){
    	
    	Integer quantidadeTotal= marcaProdutoRepository.findAll(codEmp).size();
    	
    	return new ResponseEntity<Integer>(quantidadeTotal,HttpStatus.OK);
    }
	
	

}
