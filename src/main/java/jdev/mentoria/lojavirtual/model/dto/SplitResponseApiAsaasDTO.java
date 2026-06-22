package jdev.mentoria.lojavirtual.model.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SplitResponseApiAsaasDTO {

	private String id;
	private String walletId;
	private BigDecimal fixedValue;
	private BigDecimal percentualValue;
	private BigDecimal totalValue;
	private String cancellationReason;
	private String status;
	private String externalReference;
	private String description;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getWalletId() {
		return walletId;
	}

	public void setWalletId(String walletId) {
		this.walletId = walletId;
	}

	public BigDecimal getFixedValue() {
		return fixedValue;
	}

	public void setFixedValue(BigDecimal fixedValue) {
		this.fixedValue = fixedValue;
	}

	public BigDecimal getPercentualValue() {
		return percentualValue;
	}

	public void setPercentualValue(BigDecimal percentualValue) {
		this.percentualValue = percentualValue;
	}

	public BigDecimal getTotalValue() {
		return totalValue;
	}

	public void setTotalValue(BigDecimal totalValue) {
		this.totalValue = totalValue;
	}

	public String getCancellationReason() {
		return cancellationReason;
	}

	public void setCancellationReason(String cancellationReason) {
		this.cancellationReason = cancellationReason;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getExternalReference() {
		return externalReference;
	}

	public void setExternalReference(String externalReference) {
		this.externalReference = externalReference;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
