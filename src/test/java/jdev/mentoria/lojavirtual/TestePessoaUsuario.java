package jdev.mentoria.lojavirtual;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import jdev.mentoria.lojavirtual.controller.PessoaController;
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
	public void testCadPessoa() throws ExcepetionLojaVirtual {

		PessoaJuridica pessoaJuridica = new PessoaJuridica();

		pessoaJuridica.setCnpj(""+Calendar.getInstance().getTimeInMillis());
		pessoaJuridica.setInscEstadual("213321231");
		pessoaJuridica.setNome("affaasffsfas");
		pessoaJuridica.setEmail("tUIUIUUIU");
		pessoaJuridica.setTelefone("2142124421");
		pessoaJuridica.setInscMunicipal("121r1fwfs");
		pessoaJuridica.setNomeFantasia("43wfsa");
		pessoaJuridica.setRazaoSocial("fasafsfas");

		
		pessoaController.salvarPJ(pessoaJuridica);
		

}
}