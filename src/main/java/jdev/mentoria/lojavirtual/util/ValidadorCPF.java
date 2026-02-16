package jdev.mentoria.lojavirtual.util;

/**
 * Classe utilitária responsável pela validação de números de CPF.
 *
 * <p>
 * Esta validação verifica:
 * </p>
 * <ul>
 * <li>Se o CPF possui 11 dígitos numéricos</li>
 * <li>Se não é uma sequência inválida (ex: 00000000000)</li>
 * <li>Se os dígitos verificadores (DV) são matematicamente corretos</li>
 * </ul>
 *
 * <p>
 * A validação é estrutural e matemática. Ela não garante que o CPF exista na
 * Receita Federal, apenas que o número informado é válido segundo o algoritmo
 * oficial.
 * </p>
 *
 * <p>
 * Uso típico:
 * </p>
 * 
 * <pre>
 * boolean valido = ValidadorCPF.validar("123.456.789-09");
 * </pre>
 */
public class ValidadorCPF {

	/**
	 * Valida um número de CPF.
	 *
	 * <p>
	 * O método executa as seguintes etapas:
	 * </p>
	 * <ol>
	 * <li>Remove caracteres não numéricos</li>
	 * <li>Verifica se possui 11 dígitos</li>
	 * <li>Descarta sequências repetidas inválidas</li>
	 * <li>Calcula o primeiro dígito verificador</li>
	 * <li>Calcula o segundo dígito verificador</li>
	 * <li>Compara com os dígitos informados</li>
	 * </ol>
	 *
	 * @param cpf String contendo o CPF com ou sem máscara (ex: "12345678909" ou
	 *            "123.456.789-09")
	 *
	 * @return {@code true} se o CPF for válido segundo o algoritmo oficial;
	 *         {@code false} caso contrário
	 */
	public static boolean validar(String cpf) {

		if (cpf == null) {
			return false;
		}

		// Remove tudo que não for número
		cpf = cpf.replaceAll("\\D", "");

		// Deve conter 11 dígitos
		if (cpf.length() != 11) {
			return false;
		}

		// Elimina sequências inválidas (11111111111, 00000000000, etc.)
		if (cpf.matches("(\\d)\\1{10}")) {
			return false;
		}

		try {
			String base = cpf.substring(0, 9);

			int dv1 = calcularDV(base, 10);
			int dv2 = calcularDV(base + dv1, 11);

			return cpf.equals(base + dv1 + dv2);

		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Calcula um dígito verificador (DV) para CPF.
	 *
	 * @param str         String contendo os dígitos base
	 * @param pesoInicial Peso inicial para multiplicação regressiva
	 *
	 * @return Dígito verificador calculado
	 */
	private static int calcularDV(String str, int pesoInicial) {

		int soma = 0;
		int peso = pesoInicial;

		for (int i = 0; i < str.length(); i++) {
			soma += Character.getNumericValue(str.charAt(i)) * peso--;
		}

		int resto = soma % 11;

		return (resto < 2) ? 0 : 11 - resto;
	}
}
