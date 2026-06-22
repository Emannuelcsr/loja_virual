package jdev.mentoria.lojavirtual.model.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RefundResponseApiAsaasDTO {

	private String dateCreated;
	private String status;
	private BigDecimal value;
	private String endToEndIdentifier;
	private String description;
	private String effectiveDate;
	private String transactionReceiptUrl;
	private List<RefundedSplitResponseApiAsaasDTO> refundedSplits = new ArrayList<RefundedSplitResponseApiAsaasDTO>();

	public String getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(String dateCreated) {
		this.dateCreated = dateCreated;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public String getEndToEndIdentifier() {
		return endToEndIdentifier;
	}

	public void setEndToEndIdentifier(String endToEndIdentifier) {
		this.endToEndIdentifier = endToEndIdentifier;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public String getTransactionReceiptUrl() {
		return transactionReceiptUrl;
	}

	public void setTransactionReceiptUrl(String transactionReceiptUrl) {
		this.transactionReceiptUrl = transactionReceiptUrl;
	}

	public List<RefundedSplitResponseApiAsaasDTO> getRefundedSplits() {
		return refundedSplits;
	}

	public void setRefundedSplits(List<RefundedSplitResponseApiAsaasDTO> refundedSplits) {
		this.refundedSplits = refundedSplits;
	}
}
