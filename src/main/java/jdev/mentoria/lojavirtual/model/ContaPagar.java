package jdev.mentoria.lojavirtual.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jdev.mentoria.lojavirtual.enums.StatusContaPagar;

@Entity
@Table(name = "contaPagar")
@SequenceGenerator(name = "seq_contaPagar", sequenceName = "seq_contaPagar", allocationSize = 1, initialValue = 1)
public class ContaPagar implements Serializable {
	private static final long serialVersionUID = -1964283040320971342L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_contaPagar")
	private Long id;

	@ManyToOne(targetEntity = PessoaFisica.class)
	@JoinColumn(name = "pessoa_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "pessoa_fk"))
	private PessoaFisica pessoa;
	
	@ManyToOne(targetEntity = PessoaJuridica.class)
	@JoinColumn(name = "empresa_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "empresa_fk"))
	private PessoaJuridica empresa;

	@NotNull(message = "Informe o campo descrição na Conta a Pagar")
	@Column(nullable = false)
	private String descricao;

	@NotNull(message = "Informe o valor da conta a pagar")
	@Column(nullable = false)
	private BigDecimal valorTotal;

	private BigDecimal valorDesconto;

	@NotNull(message = "Informe o status da Conta a Pagar")
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private StatusContaPagar statusContaPagar;

	@NotNull(message = "Informe a data de vencimento da conta a pagar")
	@Column(nullable = false)
	@Temporal(TemporalType.DATE)
	private Date dateVencimento;

	@Temporal(TemporalType.DATE)
	private Date datePagamento;

	@ManyToOne(targetEntity = PessoaJuridica.class)
	@JoinColumn(name = "pessoaForn_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "pessoaForn_id"))
	private PessoaJuridica pessoa_fornecedor;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public PessoaFisica getPessoa() {
		return pessoa;
	}

	public void setPessoa(PessoaFisica pessoa) {
		this.pessoa = pessoa;
	}

	public PessoaJuridica getEmpresa() {
		return empresa;
	}

	public void setEmpresa(PessoaJuridica empresa) {
		this.empresa = empresa;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public BigDecimal getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}

	public BigDecimal getValorDesconto() {
		return valorDesconto;
	}

	public void setValorDesconto(BigDecimal valorDesconto) {
		this.valorDesconto = valorDesconto;
	}

	public StatusContaPagar getStatusContaPagar() {
		return statusContaPagar;
	}

	public void setStatusContaPagar(StatusContaPagar statusContaPagar) {
		this.statusContaPagar = statusContaPagar;
	}

	public Date getDateVencimento() {
		return dateVencimento;
	}

	public void setDateVencimento(Date dateVencimento) {
		this.dateVencimento = dateVencimento;
	}

	public Date getDatePagamento() {
		return datePagamento;
	}

	public void setDatePagamento(Date datePagamento) {
		this.datePagamento = datePagamento;
	}

	public PessoaJuridica getPessoa_fornecedor() {
		return pessoa_fornecedor;
	}

	public void setPessoa_fornecedor(PessoaJuridica pessoa_fornecedor) {
		this.pessoa_fornecedor = pessoa_fornecedor;
	}

	@Override
	public int hashCode() {
		return Objects.hash(datePagamento, dateVencimento, descricao, empresa, id, pessoa, pessoa_fornecedor,
				statusContaPagar, valorDesconto, valorTotal);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ContaPagar other = (ContaPagar) obj;
		return Objects.equals(datePagamento, other.datePagamento)
				&& Objects.equals(dateVencimento, other.dateVencimento) && Objects.equals(descricao, other.descricao)
				&& Objects.equals(empresa, other.empresa) && Objects.equals(id, other.id)
				&& Objects.equals(pessoa, other.pessoa) && Objects.equals(pessoa_fornecedor, other.pessoa_fornecedor)
				&& statusContaPagar == other.statusContaPagar && Objects.equals(valorDesconto, other.valorDesconto)
				&& Objects.equals(valorTotal, other.valorTotal);
	}

	
	
	

}
