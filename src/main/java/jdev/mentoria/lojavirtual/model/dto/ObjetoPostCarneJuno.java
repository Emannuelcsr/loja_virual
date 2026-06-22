package jdev.mentoria.lojavirtual.model.dto;

import java.io.Serializable;

/**
 * Classe DTO responsável por transportar os dados necessários
 * para criação de cobrança (carnê/boleto) na API externa (Juno/Asaas).
 *
 * <p>Função dessa classe no sistema:</p>
 * <p>Essa classe serve como um "pacote de dados" que leva as informações
 * do cliente e da venda para outro sistema (API de pagamento).</p>
 *
 * <p>Ela NÃO acessa banco, NÃO tem regra de negócio e NÃO executa lógica.
 * Apenas carrega dados.</p>
 *
 * <p>Exemplo prático:</p>
 * <p>Quando o usuário finaliza uma compra, o sistema precisa mandar
 * para a API de pagamento informações como:</p>
 * <ul>
 *   <li>nome do cliente</li>
 *   <li>cpf</li>
 *   <li>valor</li>
 *   <li>parcelas</li>
 * </ul>
 *
 * <p>Essa classe é usada exatamente para isso.</p>
 *
 * <p>Resumo simples:</p>
 * <p>Esse DTO é o "formulário" que o sistema usa para enviar
 * dados de cobrança para a API externa.</p>
 */
public class ObjetoPostCarneJuno implements Serializable {

	/**
	 * Controle de versão da serialização.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Descrição da cobrança.
	 *
	 * Exemplo: "Compra de produtos da loja"
	 */
	private String description;

	/**
	 * Nome do cliente/comprador.
	 *
	 * Exemplo: "João da Silva"
	 */
	private String payerName;

	/**
	 * Telefone do cliente.
	 *
	 * Exemplo: "44999999999"
	 */
	private String payerPhone;

	/**
	 * Valor total da compra ou parcela.
	 *
	 * Exemplo: "150.00"
	 *
	 * ⚠️ Observação:
	 * Está como String, mas normalmente valores monetários
	 * deveriam ser BigDecimal para maior precisão.
	 */
	private String totalAmount;

	/**
	 * Quantidade de parcelas.
	 *
	 * Exemplo: "3"
	 */
	private String installments;

	/**
	 * Referência da venda ou produto.
	 *
	 * Exemplo: código interno da venda.
	 */
	private String reference;
	
	/**
	 * CPF ou CNPJ do cliente.
	 *
	 * Esse dado é usado na API de pagamento para identificar o pagador.
	 */
	private String payerCpfCnpj;
	
	/**
	 * Email do cliente.
	 *
	 * Usado para localizar ou criar o cliente na API externa.
	 */
	private String email;
	
	/**
	 * ID da venda no sistema interno.
	 *
	 * Esse valor serve para relacionar a cobrança com a venda.
	 */
	private Long idVenda;
	
	/**
	 * Define o ID da venda.
	 *
	 * @param idVenda identificador da venda no sistema
	 */
	public void setIdVenda(Long idVenda) {
		this.idVenda = idVenda;
	}
	
	/**
	 * Retorna o ID da venda.
	 *
	 * @return id da venda
	 */
	public Long getIdVenda() {
		return idVenda;
	}
	
	/**
	 * Define o email do cliente.
	 *
	 * @param email email do cliente
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	
	/**
	 * Retorna o email do cliente.
	 *
	 * @return email do cliente
	 */
	public String getEmail() {
		return email;
	}
	
	/**
	 * Define CPF ou CNPJ do cliente.
	 *
	 * @param payerCpfCnpj documento do cliente
	 */
	public void setPayerCpfCnpj(String payerCpfCnpj) {
		this.payerCpfCnpj = payerCpfCnpj;
	}
	
	/**
	 * Retorna CPF ou CNPJ do cliente.
	 *
	 * @return documento do cliente
	 */
	public String getPayerCpfCnpj() {
		return payerCpfCnpj;
	}

	/**
	 * Retorna a descrição da cobrança.
	 *
	 * @return descrição
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Define a descrição da cobrança.
	 *
	 * @param description descrição da cobrança
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Retorna o nome do cliente.
	 *
	 * @return nome do cliente
	 */
	public String getPayerName() {
		return payerName;
	}

	/**
	 * Define o nome do cliente.
	 *
	 * @param payerName nome do cliente
	 */
	public void setPayerName(String payerName) {
		this.payerName = payerName;
	}

	/**
	 * Retorna o telefone do cliente.
	 *
	 * @return telefone do cliente
	 */
	public String getPayerPhone() {
		return payerPhone;
	}

	/**
	 * Define o telefone do cliente.
	 *
	 * @param payerPhone telefone do cliente
	 */
	public void setPayerPhone(String payerPhone) {
		this.payerPhone = payerPhone;
	}

	/**
	 * Retorna o valor total da compra.
	 *
	 * @return valor total (em String)
	 */
	public String getTotalAmount() {
		return totalAmount;
	}

	/**
	 * Define o valor total da compra.
	 *
	 * @param totalAmount valor total
	 */
	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}

	/**
	 * Retorna a quantidade de parcelas.
	 *
	 * @return número de parcelas
	 */
	public String getInstallments() {
		return installments;
	}

	/**
	 * Define a quantidade de parcelas.
	 *
	 * @param installments número de parcelas
	 */
	public void setInstallments(String installments) {
		this.installments = installments;
	}

	/**
	 * Retorna a referência da venda.
	 *
	 * @return referência
	 */
	public String getReference() {
		return reference;
	}

	/**
	 * Define a referência da venda.
	 *
	 * @param reference código ou identificador da venda
	 */
	public void setReference(String reference) {
		this.reference = reference;
	}

}