package jdev.mentoria.lojavirtual;

import jdev.mentoria.lojavirtual.util.ValidadorCNPJ;
import jdev.mentoria.lojavirtual.util.ValidadorCPF;

public class TesteCpfECnpj {

	public static void main(String[] args) {
		
		boolean isValidCnpj =  ValidadorCNPJ.validar("04.812.335/0001-65");
		
		System.out.println(isValidCnpj);
		
		
		boolean isValidCpf =  ValidadorCPF.validar("0682674933");
	
		System.out.println(isValidCpf);
	}
	

}
