package jdev.mentoria.lojavirtual;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import jdev.mentoria.lojavirtual.controller.PessoaController;
import jdev.mentoria.lojavirtual.enums.TipoEndereco;
import jdev.mentoria.lojavirtual.model.Endereco;
import jdev.mentoria.lojavirtual.model.PessoaFisica;
import jdev.mentoria.lojavirtual.model.PessoaJuridica;
import jdev.mentoria.lojavirtual.repository.PessoaRepository;
import jdev.mentoria.lojavirtual.service.PessoaUserService;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Calendar;

@ActiveProfiles("test")
@SpringBootTest(classes = LojaVirualApplication.class)
public class TestePessoaUsuario {

	@Autowired
	private PessoaController pessoaController;

	@Test
	public void testCadPessoaJuridica() throws ExcepetionLojaVirtual {

		PessoaJuridica pessoaJuridica = new PessoaJuridica();

		pessoaJuridica.setCnpj("" + Calendar.getInstance().getTimeInMillis());
		pessoaJuridica.setInscEstadual("213321231");
		pessoaJuridica.setNome("affaasffsfas");
		pessoaJuridica.setEmail("tUIUIUUIU@gmail.com");
		pessoaJuridica.setTelefone("2142124421");
		pessoaJuridica.setInscMunicipal("121r1fwfs");
		pessoaJuridica.setNomeFantasia("43wfsa");
		pessoaJuridica.setRazaoSocial("fasafsfas");

		Endereco endereco1 = new Endereco();

		endereco1.setRuaLogra("Av Brasil");
		endereco1.setNumero("356");
		endereco1.setBairro("JD Dias");
		endereco1.setCep("782877");
		endereco1.setCidade("Maringá");
		endereco1.setUf("PR");
		endereco1.setComplemtento("Apto 12");
		endereco1.setEmpresa(pessoaJuridica);
		endereco1.setTipoEndereco(TipoEndereco.COBRANCA);
		endereco1.setPessoa(pessoaJuridica);

		Endereco endereco2 = new Endereco();

		endereco2.setRuaLogra("Av ooioi");
		endereco2.setNumero("341241");
		endereco2.setBairro("Jpoliars");
		endereco2.setCep("7421421");
		endereco2.setCidade("foz");
		endereco2.setUf("PS");
		endereco2.setComplemtento("Apt2121");
		endereco2.setEmpresa(pessoaJuridica);
		endereco2.setTipoEndereco(TipoEndereco.ENTREGA);
		endereco2.setPessoa(pessoaJuridica);
		
		pessoaJuridica.getEnderecos().add(endereco2);

		pessoaJuridica.getEnderecos().add(endereco1);
		
		pessoaJuridica = pessoaController.salvarPJ(pessoaJuridica).getBody();

		assertEquals(true, pessoaJuridica.getId()>0);
		
		for(Endereco endereco : pessoaJuridica.getEnderecos()) {
			
			assertEquals(true,endereco.getId()>0);
		}
		
		assertEquals(2,pessoaJuridica.getEnderecos().size());
	}
	
	

	@Test
	public void testCadPessoaFisica() throws ExcepetionLojaVirtual {

		PessoaFisica pessoaFisica = new PessoaFisica();

		pessoaFisica.setCpf("298.605.310-64");
		pessoaFisica.setNome("affaasffsfas");
		pessoaFisica.setEmail("aiaiai@gmail.com");
		pessoaFisica.setTelefone("2142124421");
	
		Endereco endereco1 = new Endereco();

		endereco1.setRuaLogra("Av Brasil");
		endereco1.setNumero("356");
		endereco1.setBairro("JD Dias");
		endereco1.setCep("782877");
		endereco1.setCidade("Maringá");
		endereco1.setUf("PR");
		endereco1.setComplemtento("Apto 12");
		endereco1.setEmpresa(pessoaFisica);
		endereco1.setTipoEndereco(TipoEndereco.COBRANCA);
		endereco1.setPessoa(pessoaFisica);

		Endereco endereco2 = new Endereco();

		endereco2.setRuaLogra("Av ooioi");
		endereco2.setNumero("341241");
		endereco2.setBairro("Jpoliars");
		endereco2.setCep("7421421");
		endereco2.setCidade("foz");
		endereco2.setUf("PS");
		endereco2.setComplemtento("Apt2121");
		endereco2.setEmpresa(pessoaFisica);
		endereco2.setTipoEndereco(TipoEndereco.ENTREGA);
		endereco2.setPessoa(pessoaFisica);
		
		pessoaFisica.getEnderecos().add(endereco2);

		pessoaFisica.getEnderecos().add(endereco1);
		
		pessoaFisica = pessoaController.salvarPF(pessoaFisica).getBody();

		assertEquals(true, pessoaFisica.getId()>0);
		
		for(Endereco endereco : pessoaFisica.getEnderecos()) {
			
			assertEquals(true,endereco.getId()>0);
		}
		
		assertEquals(2,pessoaFisica.getEnderecos().size());
	}
	
}