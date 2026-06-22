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
import jdev.mentoria.lojavirtual.model.Acesso;
import jdev.mentoria.lojavirtual.repository.AcessoRepository;
import jdev.mentoria.lojavirtual.repository.UsuarioRepository;
import jdev.mentoria.lojavirtual.service.AcessoService;

@RestController
public class AcessoController {

    private final CupomDescontoController cupomDescontoController;

	@Autowired
	private AcessoService acessoService;

	@Autowired
	private AcessoRepository acessoRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;
	
    AcessoController(CupomDescontoController cupomDescontoController) {
        this.cupomDescontoController = cupomDescontoController;
    }

	@ResponseBody // Ele pega o objeto Java que seu método retorna e transforma em JSON pra mandar
					// de volta pra tela.
	@PostMapping(value = "/salvarAcesso")
	public ResponseEntity<Acesso> salvarAcesso(@RequestBody Acesso acesso) throws ExcepetionLojaVirtual {// requestBody
																											// transforma
																											// JSON da
																											// tela em
		// objeto

		if (acesso.getId() == null) {
			List<Acesso> acessos = acessoRepository.buscarAcessoDesc(acesso.getDescricao().toUpperCase());

			if (!acessos.isEmpty()) {

				throw new ExcepetionLojaVirtual("Ja existe acesso com a descricao: " + acesso.getDescricao());
			}

		}

		Acesso acessoSalvo = acessoService.save(acesso);

		return new ResponseEntity<Acesso>(acessoSalvo, HttpStatus.OK);
	}

	
	
	
	
	
	@ResponseBody // Ele pega o objeto Java que seu método retorna e transforma em JSON pra mandar
					// de volta pra tela.
	@PostMapping(value = "/deleteAcesso")
	public ResponseEntity<?> deleteAcesso(@RequestBody Acesso acesso) {// requestBody transforma JSON da tela em objeto

		acessoRepository.deleteById(acesso.getId());

		return new ResponseEntity("Cadastro Removido", HttpStatus.OK);
	}

	@DeleteMapping(value = "/deleteAcessoPorId/{id}")
	public ResponseEntity<?> deleteAcessoPorId(@PathVariable("id") Long id) {

		acessoRepository.deleteById(id);

		return new ResponseEntity("Cadastro Removido", HttpStatus.OK);
	}
	
	

	@GetMapping(value = "/obterAcesso/{id}")
	public ResponseEntity<Acesso> obterAcesso(@PathVariable("id") Long id) throws ExcepetionLojaVirtual {

		Acesso acesso = acessoRepository.findById(id).orElse(null);

		if (acesso == null) {

			throw new ExcepetionLojaVirtual("Não encontou acesso com código: " + id);
		}

		return new ResponseEntity<Acesso>(acesso, HttpStatus.OK);
	}
	
	

	@GetMapping(value = "/buscarPorDescricao/{desc}")
	public ResponseEntity<List<Acesso>> buscarPorDescricao(@PathVariable("desc") String desc) {

		List<Acesso> acesso = acessoRepository.buscarAcessoDesc(desc.toUpperCase());

		return new ResponseEntity<List<Acesso>>(acesso, HttpStatus.OK);
	}
	
	
    @GetMapping(value = "/listaPorPageAcesso/{codEmp}/{pagina}")
    public ResponseEntity<List<Acesso>> listaPorPageAcesso(
            @PathVariable("codEmp") Long codEmp,
            @PathVariable("pagina") Integer pagina) {

        org.springframework.data.domain.Pageable pageable =
                PageRequest.of(pagina - 1, 5, Sort.by("descricao"));

        List<Acesso> lista =
        		acessoRepository.findbyPage(codEmp, pageable);

        return new ResponseEntity<List<Acesso>>(lista, HttpStatus.OK);
    }
	
    
    
    
    @GetMapping(value = "/qtdadePaginaAcesso/{codEmp}")
    public ResponseEntity<Map<String, Integer>> qtdadePaginaAcesso(
            @PathVariable("codEmp") Long codEmp) {

        Integer qtdadePagina =
        		acessoRepository.quantidadePagina(codEmp);

        Map<String, Integer> resposta = new HashMap<>();

        resposta.put("resposta", qtdadePagina);

        return new ResponseEntity<Map<String, Integer>>(resposta, HttpStatus.OK);
    }

    
    @GetMapping(value = "/buscarPorAcessoPorEmpresa/{desc}/{empresa}")
    public ResponseEntity<Map<String, Object>> buscarPorAcessoPorEmpresa(
            @PathVariable("desc") String desc,
            @PathVariable("empresa") String empresa) {

        List<Acesso> categoriaProduto =
        		acessoRepository.buscarCategoriaDesc(desc.toUpperCase(), empresa);

        Map<String, Object> resposta = new HashMap<>();

        resposta.put("resposta", categoriaProduto);
        resposta.put("total", categoriaProduto.size());

        return new ResponseEntity<Map<String, Object>>(resposta, HttpStatus.OK);
    }

    
    
    @GetMapping(value = "/quantidadeDeAcessos/{codEmp}")
    public ResponseEntity<Integer> quantidadeDeAcessos(@PathVariable("codEmp")Long codEmp){
    	
    	Integer quantidadeTotal= acessoRepository.findAll(codEmp).size();
    	
    	return new ResponseEntity<Integer>(quantidadeTotal,HttpStatus.OK);
    }
    
    @GetMapping(value = "/listarAcesso/{codEmp}")
    public ResponseEntity<List<Acesso>> listarmarcaproduto(
            @PathVariable("codEmp") Long codEmp) {

        List<Acesso> marcaProduto =
        		acessoRepository.findAll(codEmp);

        return new ResponseEntity<List<Acesso>>(marcaProduto, HttpStatus.OK);
    }
    
    
    @GetMapping(value = "/listaAcessoPorEmpresa/{codEmp}")
    public ResponseEntity<List<Acesso>> listaAcessoPorEmpresa(
            @PathVariable("codEmp") Long codEmp) {


        List<Acesso> lista = acessoRepository.findAcessos(codEmp);

        return new ResponseEntity<List<Acesso>>(lista, HttpStatus.OK);
    }
    
    @PostMapping(value = "/adicionaRemoveAcesso")
    public ResponseEntity<String> adicionaRemoveAcesso(@RequestBody String params){
		
    	String[] paramAcesso =  params.split("-");
    	
    	Long idAcesso = Long.parseLong(paramAcesso[0]);
    	Long idUser = Long.parseLong(paramAcesso[1]);
    	
    	Boolean possuiAcesso = acessoRepository.possuiAcesso(idUser, idAcesso);
    	
    	if(possuiAcesso) {
    		
    		usuarioRepository.deleteByAcesso(idAcesso, idUser);
    	}else {
    		
    		usuarioRepository.addAcesso(idAcesso, idUser);
    	}
    		
    	
    	
    	Map<String, String> resposta = new HashMap<>();
	    resposta.put("mensagem", "Acesso Atualizado");
	    
	    return new ResponseEntity(resposta, HttpStatus.OK); 	
    }
    
    
}
