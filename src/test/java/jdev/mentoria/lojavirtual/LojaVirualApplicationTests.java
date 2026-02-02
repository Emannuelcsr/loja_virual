package jdev.mentoria.lojavirtual;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import jdev.mentoria.lojavirtual.model.Acesso;
import jdev.mentoria.lojavirtual.repository.AcessoRepository;
import jdev.mentoria.lojavirtual.service.AcessoService;

@SpringBootTest(classes = LojaVirualApplication.class)
class LojaVirualApplicationTests {
	
	@Autowired	
	private AcessoService acessoService;
	
	@Autowired
	private AcessoRepository acessoRepository;
	

	@Test
	public  void testCadastroAcesso() {
		
		Acesso acesso = new Acesso();
		
		acesso.setDescricao("teste");
		
		
		acessoRepository.save(acesso);
	}

}
