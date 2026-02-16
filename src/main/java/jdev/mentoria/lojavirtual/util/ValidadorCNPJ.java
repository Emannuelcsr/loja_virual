package jdev.mentoria.lojavirtual.util;
/**
 * Classe utilitária responsável pela validação de números de CNPJ.
 *
 * <p>
 * Esta validação verifica:
 * </p>
 * <ul>
 * <li>Se o CNPJ possui 14 dígitos numéricos</li>
 * <li>Se não é uma sequência inválida (ex: 00000000000000)</li>
 * <li>Se os dígitos verificadores (DV) são matematicamente corretos</li>
 * </ul>
 *
 * <p>
 * A validação é estrutural e matemática. Ela não garante que o CNPJ exista na
 * base da Receita Federal, apenas que o número informado é válido segundo o
 * algoritmo oficial.
 * </p>
 *
 * <p>
 * Uso típico:
 * </p>
 * 
 * <pre>
 * boolean valido = ValidadorCNPJ.validar("12.345.678/0001-95");
 * </pre>
 */
public class ValidadorCNPJ {

	/**
	 * Valida um número de CNPJ.
	 *
	 * <p>
	 * O método executa as seguintes etapas:
	 * </p>
	 * <ol>
	 * <li>Remove caracteres não numéricos</li>
	 * <li>Verifica se possui 14 dígitos</li>
	 * <li>Descarta sequências repetidas inválidas</li>
	 * <li>Calcula o primeiro dígito verificador</li>
	 * <li>Calcula o segundo dígito verificador</li>
	 * <li>Compara com os dígitos informados</li>
	 * </ol>
	 *
	 * @param cnpj String contendo o CNPJ com ou sem máscara (ex: "12345678000195"
	 *             ou "12.345.678/0001-95")
	 *
	 * @return {@code true} se o CNPJ for válido segundo o algoritmo oficial;
	 *         {@code false} caso contrário
	 */
	public static boolean validar(String cnpj) {

		if (cnpj == null) {
			return false;
		}

		// Remove tudo que não for número
		cnpj = cnpj.replaceAll("\\D", "");

		// Deve conter 14 dígitos
		if (cnpj.length() != 14) {
			return false;
		}

		// Elimina sequências inválidas
		if (cnpj.matches("(\\d)\\1{13}")) {
			return false;
		}

		try {
			int[] pesosDV1 = { 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2 };
			int[] pesosDV2 = { 6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2 };

			String base = cnpj.substring(0, 12);

			int dv1 = calcularDV(base, pesosDV1);
			int dv2 = calcularDV(base + dv1, pesosDV2);

			return cnpj.equals(base + dv1 + dv2);

		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Calcula um dígito verificador (DV) para CNPJ.
	 *
	 * @param str   String contendo os dígitos base para cálculo
	 * @param pesos Vetor de pesos aplicado na multiplicação
	 *
	 * @return Dígito verificador calculado
	 */
	private static int calcularDV(String str, int[] pesos) {

		int soma = 0;

		for (int i = 0; i < pesos.length; i++) {
			soma += Character.getNumericValue(str.charAt(i)) * pesos[i];
		}

		int resto = soma % 11;

		return (resto < 2) ? 0 : 11 - resto;
	}
}
