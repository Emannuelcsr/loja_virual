package jdev.mentoria.lojavirtual.service;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import jdev.mentoria.lojavirtual.model.PessoaJuridica;
import jdev.mentoria.lojavirtual.model.Usuario;
import jdev.mentoria.lojavirtual.repository.PessoaRepository;
import jdev.mentoria.lojavirtual.repository.UsuarioRepository;

@Service
public class PessoaUserService {

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	public PessoaJuridica salvarPessoaJuridica (PessoaJuridica pessoaJuridica) {
		
		 pessoaJuridica = pessoaRepository.save(pessoaJuridica);
		
		 Usuario usuarioPJ = usuarioRepository.findUserByPessoa(pessoaJuridica.getId(),pessoaJuridica.getEmail());
		 
		 if(usuarioPJ == null) {
			 
			 usuarioPJ = new Usuario();
			 usuarioPJ.setDataAtualSenha(Calendar.getInstance().getTime());
			 usuarioPJ.setEmpresa(pessoaJuridica);
			 usuarioPJ.setPessoa(pessoaJuridica);
			 usuarioPJ.setLogin(pessoaJuridica.getEmail());
			 
			 String senha = "" + Calendar.getInstance().getTimeInMillis();
			 
			 String senhaCrypt = new BCryptPasswordEncoder().encode(senha);
			 
			 usuarioPJ.setSenha(senhaCrypt);
		 
			 usuarioPJ = usuarioRepository.save(usuarioPJ);
			 
			 usuarioRepository.insereAcessoUserPj(usuarioPJ.getId());
		 }
		 
				 
				 
		 return pessoaJuridica;
	}
	
}
