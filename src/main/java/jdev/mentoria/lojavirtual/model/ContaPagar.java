package jdev.mentoria.lojavirtual.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

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
import jdev.mentoria.lojavirtual.enums.StatusContaPagar;

@Entity
@Table(name = "contaPagar")
@SequenceGenerator(name = "seq_contaPagar", sequenceName = "seq_contaPagar", allocationSize = 1, initialValue = 1)
public class ContaPagar implements Serializable {
	private static final long serialVersionUID = -1964283040320971342L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_contaPagar")
	private Long id;

	@ManyToOne(targetEntity = Pessoa.class)
	@JoinColumn(name = "pessoa_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "pessoa_fk"))
	private Pessoa pessoa;

	private String descricao;

	private BigDecimal valorTotal;

	private BigDecimal valorDesconto;

	@Enumerated(EnumType.STRING)
	private StatusContaPagar statusContaPagar;

	@Temporal(TemporalType.DATE)
	private Date dateVencimento;

	@Temporal(TemporalType.DATE)
	private Date datePagamento;

	@ManyToOne(targetEntity = Pessoa.class)
	@JoinColumn(name = "pessoaForn_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "pessoaForn_id"))
	private Pessoa pessoa_fornecedor;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
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

	public Pessoa getPessoa_fornecedor() {
		return pessoa_fornecedor;
	}

	public void setPessoa_fornecedor(Pessoa pessoa_fornecedor) {
		this.pessoa_fornecedor = pessoa_fornecedor;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
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
		return Objects.equals(id, other.id);
	}

}
