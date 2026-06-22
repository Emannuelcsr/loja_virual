package jdev.mentoria.lojavirtual.model.dto;

import java.util.ArrayList;
import java.util.List;

public class ErroResponseApiAsaasCartaoCredito {

	private List<ObjetoErroResponseApiAsaasCartaoCredito> errors = new ArrayList<ObjetoErroResponseApiAsaasCartaoCredito>();

	public List<ObjetoErroResponseApiAsaasCartaoCredito> getErrors() {
		return errors;
	}

	public void setErrors(List<ObjetoErroResponseApiAsaasCartaoCredito> errors) {
		this.errors = errors;
	}

	public String listErros() {

		StringBuilder builder = new StringBuilder();

		for (ObjetoErroResponseApiAsaasCartaoCredito erro : errors) {

			builder.append(erro.getDescription()).append(" - Code : ").append(erro.getCode()).append("\n");

		}
		
		return builder.toString();
		
	}

}
