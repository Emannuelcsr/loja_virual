package jdev.mentoria.lojavirtual.model;

import java.io.Serializable;
import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

/**
 * Entidade usada para o controle interno das cobranças de uma venda no sistema.
 *
 * <p>
 * Apesar do nome "BoletoJuno", esta classe não representa apenas boleto.
 * Ela funciona como um registro interno de cobrança da venda, podendo guardar
 * informações de pagamentos gerados por boleto, Pix ou cartão.
 * </p>
 *
 * <p>
 * Em outras palavras:
 * a API externa (como Asaas/Juno) é quem cria e processa a cobrança real,
 * enquanto esta entidade serve para o sistema salvar e controlar os dados
 * dessa cobrança no banco de dados.
 * </p>
 *
 * <p>
 * Essa classe pode ser usada para:
 * </p>
 * <ul>
 *   <li>Relacionar a cobrança com uma venda</li>
 *   <li>Relacionar a cobrança com a empresa</li>
 *   <li>Guardar código/id da cobrança gerada na API</li>
 *   <li>Guardar link de pagamento, link de checkout ou link da parcela</li>
 *   <li>Guardar valor, vencimento e número da parcela</li>
 *   <li>Controlar internamente se a cobrança foi marcada como quitada</li>
 *   <li>Armazenar dados de Pix, boleto ou cartão para consulta futura</li>
 * </ul>
 *
 * <p>
 * Importante:
 * esta classe é um espelho/controle interno da cobrança.
 * Ela não é a responsável por cobrar o cliente diretamente.
 * Quem faz a cobrança real é a API de pagamento.
 * </p>
 *
 * <p>
 * Observação:
 * o nome da classe ficou como "BoletoJuno" por questão histórica do projeto,
 * mas hoje seu uso é mais amplo, servindo como controle de cobrança da venda,
 * inclusive para cartão e Pix.
 * </p>
 */


@Entity
@Table(name = "boleto_juno")
@SequenceGenerator(name = "seq_boleto_juno", sequenceName = "seq_boleto_juno", allocationSize = 1, initialValue = 1)
public class BoletoJuno implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_boleto_juno")
	private Long id;

	/* Código ou identificador da cobrança retornado pela API */
	private String code = "";

	/* Link direto do boleto, quando existir */
	private String link;

	/* Link de checkout da cobrança, podendo envolver boleto, Pix ou cartão */
	private String checkoutUrl = "";

	/* Indica no controle interno se a cobrança foi considerada quitada */
	private boolean quitado = false;

	/* Data de vencimento da cobrança ou parcela */
	private String dataVencimento = "";

	/* Valor da cobrança ou da parcela */
	private BigDecimal valor = BigDecimal.ZERO;

	/* Número sequencial da parcela/recorrência: 1, 2, 3... */
	private Integer recorrencia = 0;

	/* Id da cobrança para controle interno e possível cancelamento/consulta via API */
	private String idChrBoleto = "";

	/* Link individual da parcela, quando existir */
	private String installmentLink = "";

	/* Id do Pix vinculado à cobrança, quando existir */
	private String IdPix = "";

	/* Payload do QR Code Pix em Base64, quando existir */
	private String payloadInBase64 = "";

	/* Imagem do QR Code em Base64, quando existir */
	@Column(columnDefinition = "text")
	private String imageInBase64 = "";

	/* Id da cobrança de cartão, quando a venda for paga com cartão */
	private String chargeICartao = "";

	@ManyToOne
	@JoinColumn(name = "venda_compra_loja_virt_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "venda_compra_loja_virt_fk"))
	private VendaCompraLojaVirtual vendaCompraLojaVirtual;

	@ManyToOne(targetEntity = PessoaJuridica.class)
	@JoinColumn(name = "empresa_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "empresa_id_fk"))
	private PessoaJuridica empresa;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getCheckoutUrl() {
		return checkoutUrl;
	}

	public void setCheckoutUrl(String checkoutUrl) {
		this.checkoutUrl = checkoutUrl;
	}

	public boolean isQuitado() {
		return quitado;
	}

	public void setQuitado(boolean quitado) {
		this.quitado = quitado;
	}

	public String getDataVencimento() {
		return dataVencimento;
	}

	public void setDataVencimento(String dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public Integer getRecorrencia() {
		return recorrencia;
	}

	public void setRecorrencia(Integer recorrencia) {
		this.recorrencia = recorrencia;
	}

	public String getIdChrBoleto() {
		return idChrBoleto;
	}

	public void setIdChrBoleto(String idChrBoleto) {
		this.idChrBoleto = idChrBoleto;
	}

	public String getInstallmentLink() {
		return installmentLink;
	}

	public void setInstallmentLink(String installmentLink) {
		this.installmentLink = installmentLink;
	}

	public String getIdPix() {
		return IdPix;
	}

	public void setIdPix(String idPix) {
		IdPix = idPix;
	}

	public String getPayloadInBase64() {
		return payloadInBase64;
	}

	public void setPayloadInBase64(String payloadInBase64) {
		this.payloadInBase64 = payloadInBase64;
	}

	public String getImageInBase64() {
		return imageInBase64;
	}

	public void setImageInBase64(String imageInBase64) {
		this.imageInBase64 = imageInBase64;
	}

	public String getChargeICartao() {
		return chargeICartao;
	}

	public void setChargeICartao(String chargeICartao) {
		this.chargeICartao = chargeICartao;
	}

	public VendaCompraLojaVirtual getVendaCompraLojaVirtual() {
		return vendaCompraLojaVirtual;
	}

	public void setVendaCompraLojaVirtual(VendaCompraLojaVirtual vendaCompraLojaVirtual) {
		this.vendaCompraLojaVirtual = vendaCompraLojaVirtual;
	}

	public PessoaJuridica getEmpresa() {
		return empresa;
	}

	public void setEmpresa(PessoaJuridica empresa) {
		this.empresa = empresa;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BoletoJuno other = (BoletoJuno) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
