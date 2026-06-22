package jdev.mentoria.lojavirtual.model.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ChargebackResponseApiAsaasDTO {

	private String id;
	private String payment;
	private String installment;
	private String customerAccount;
	private String status;
	private String reason;
	private String disputeStartDate;
	private BigDecimal value;
	private String paymentDate;
	private ChargebackCreditCardResponseApiAsaasDTO creditCard = new ChargebackCreditCardResponseApiAsaasDTO();
	private String disputeStatus;
	private String deadlineToSendDisputeDocuments;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPayment() {
		return payment;
	}

	public void setPayment(String payment) {
		this.payment = payment;
	}

	public String getInstallment() {
		return installment;
	}

	public void setInstallment(String installment) {
		this.installment = installment;
	}

	public String getCustomerAccount() {
		return customerAccount;
	}

	public void setCustomerAccount(String customerAccount) {
		this.customerAccount = customerAccount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getDisputeStartDate() {
		return disputeStartDate;
	}

	public void setDisputeStartDate(String disputeStartDate) {
		this.disputeStartDate = disputeStartDate;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public String getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(String paymentDate) {
		this.paymentDate = paymentDate;
	}

	public ChargebackCreditCardResponseApiAsaasDTO getCreditCard() {
		return creditCard;
	}

	public void setCreditCard(ChargebackCreditCardResponseApiAsaasDTO creditCard) {
		this.creditCard = creditCard;
	}

	public String getDisputeStatus() {
		return disputeStatus;
	}

	public void setDisputeStatus(String disputeStatus) {
		this.disputeStatus = disputeStatus;
	}

	public String getDeadlineToSendDisputeDocuments() {
		return deadlineToSendDisputeDocuments;
	}

	public void setDeadlineToSendDisputeDocuments(String deadlineToSendDisputeDocuments) {
		this.deadlineToSendDisputeDocuments = deadlineToSendDisputeDocuments;
	}
}
