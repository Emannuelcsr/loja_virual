package jdev.mentoria.lojavirtual;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import jdev.mentoria.lojavirtual.model.dto.ObjetoPostCarneJuno;
import jdev.mentoria.lojavirtual.service.ServiceAsaasBoleto;
import junit.framework.TestCase;

@org.springframework.test.context.ActiveProfiles("dev")
@SpringBootTest(classes = LojaVirualApplication.class)
public class TesteAsaasBoleto extends TestCase {

	@Autowired
	private ServiceAsaasBoleto serviceAsaasBoleto;

	@Test
	public void testCriarChavesAsaas() throws Exception {

		String chaveApi = serviceAsaasBoleto.criarChavePixAsaas();

		System.out.println("resposta: " + chaveApi);
	}

	@Test
	public void testBuscaCliente() throws Exception {

		ObjetoPostCarneJuno dados = new ObjetoPostCarneJuno();

		dados.setEmail("oioi@gmail.com");
		dados.setPayerName("euuuuu");
		dados.setPayerCpfCnpj("06826774933");
		dados.setPayerPhone("41995559429");

		String customer_id = serviceAsaasBoleto.buscaClientePessoaApiAssas(dados);

		assertEquals("cus_000057212", customer_id);

	}

	@Test
	public void testGeraCarneApiAsaas() throws Exception {

		ObjetoPostCarneJuno dados = new ObjetoPostCarneJuno();

		dados.setEmail("oioi@gmail.com");
		dados.setPayerName("euuuuu");
		dados.setPayerCpfCnpj("06826774933");
		dados.setPayerPhone("41995559429");
		dados.setIdVenda(31L);

		 String retorno = serviceAsaasBoleto.gerarCarneApiAsaas(dados);
		
		System.out.println(retorno);
	}
	
	
	
	
	
}
