package jdev.mentoria.lojavirtual.model;

import java.time.LocalDate;
import java.util.Date;

import org.hibernate.validator.constraints.br.CPF;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "pessoa_fisica")
public class PessoaFisica extends Pessoa {

	private static final long serialVersionUID = 1L;
	
	@CPF(message = "Cpf esta invalido")
	@Column(nullable = false)
	private String cpf;

	
	@Temporal(TemporalType.DATE)
	private LocalDate dataNascimento;

	
	
	
	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public LocalDate getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(LocalDate dataNascimento) {
		this.dataNascimento = dataNascimento;
	}


	
	
	
	
}
