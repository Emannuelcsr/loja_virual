package jdev.mentoria.lojavirtual.model.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentDTO implements Serializable {

    private String object;
    private String id;
    private String dateCreated;
    private String customer;
    private String subscription;
    private String installment;
    private String paymentLink;
    private String dueDate;
    private String originalDueDate;

    private BigDecimal value;
    private BigDecimal netValue;
    private BigDecimal originalValue;
    private BigDecimal interestValue;

    private String nossoNumero;
    private String description;
    private String externalReference;
    private String billingType;
    private String status;

    private String pixTransaction;
    private String confirmedDate;
    private String paymentDate;
    private String clientPaymentDate;
    private Integer installmentNumber;
    private String creditDate;
    private String custody;
    private String estimatedCreditDate;

    private String invoiceUrl;
    private String bankSlipUrl;
    private String transactionReceiptUrl;
    private String invoiceNumber;

    private Boolean deleted;
    private Boolean anticipated;
    private Boolean anticipable;

    private String lastInvoiceViewedDate;
    private String lastBankSlipViewedDate;
    private Boolean postalService;

    private CreditCardDTO creditCard = new CreditCardDTO();
    private DiscountDTO discount = new DiscountDTO();
    private FineDTO fine = new FineDTO();
    private InterestDTO interest = new InterestDTO();

    private List<SplitDTO> split = new ArrayList<SplitDTO>();

    private ChargebackDTO chargeback = new ChargebackDTO();

    private Object refunds;

	public String getObject() {
		return object;
	}

	public void setObject(String object) {
		this.object = object;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(String dateCreated) {
		this.dateCreated = dateCreated;
	}

	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public String getSubscription() {
		return subscription;
	}

	public void setSubscription(String subscription) {
		this.subscription = subscription;
	}

	public String getInstallment() {
		return installment;
	}

	public void setInstallment(String installment) {
		this.installment = installment;
	}

	public String getPaymentLink() {
		return paymentLink;
	}

	public void setPaymentLink(String paymentLink) {
		this.paymentLink = paymentLink;
	}

	public String getDueDate() {
		return dueDate;
	}

	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}

	public String getOriginalDueDate() {
		return originalDueDate;
	}

	public void setOriginalDueDate(String originalDueDate) {
		this.originalDueDate = originalDueDate;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public BigDecimal getNetValue() {
		return netValue;
	}

	public void setNetValue(BigDecimal netValue) {
		this.netValue = netValue;
	}

	public BigDecimal getOriginalValue() {
		return originalValue;
	}

	public void setOriginalValue(BigDecimal originalValue) {
		this.originalValue = originalValue;
	}

	public BigDecimal getInterestValue() {
		return interestValue;
	}

	public void setInterestValue(BigDecimal interestValue) {
		this.interestValue = interestValue;
	}

	public String getNossoNumero() {
		return nossoNumero;
	}

	public void setNossoNumero(String nossoNumero) {
		this.nossoNumero = nossoNumero;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getExternalReference() {
		return externalReference;
	}

	public void setExternalReference(String externalReference) {
		this.externalReference = externalReference;
	}

	public String getBillingType() {
		return billingType;
	}

	public void setBillingType(String billingType) {
		this.billingType = billingType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPixTransaction() {
		return pixTransaction;
	}

	public void setPixTransaction(String pixTransaction) {
		this.pixTransaction = pixTransaction;
	}

	public String getConfirmedDate() {
		return confirmedDate;
	}

	public void setConfirmedDate(String confirmedDate) {
		this.confirmedDate = confirmedDate;
	}

	public String getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(String paymentDate) {
		this.paymentDate = paymentDate;
	}

	public String getClientPaymentDate() {
		return clientPaymentDate;
	}

	public void setClientPaymentDate(String clientPaymentDate) {
		this.clientPaymentDate = clientPaymentDate;
	}

	public Integer getInstallmentNumber() {
		return installmentNumber;
	}

	public void setInstallmentNumber(Integer installmentNumber) {
		this.installmentNumber = installmentNumber;
	}

	public String getCreditDate() {
		return creditDate;
	}

	public void setCreditDate(String creditDate) {
		this.creditDate = creditDate;
	}

	public String getCustody() {
		return custody;
	}

	public void setCustody(String custody) {
		this.custody = custody;
	}

	public String getEstimatedCreditDate() {
		return estimatedCreditDate;
	}

	public void setEstimatedCreditDate(String estimatedCreditDate) {
		this.estimatedCreditDate = estimatedCreditDate;
	}

	public String getInvoiceUrl() {
		return invoiceUrl;
	}

	public void setInvoiceUrl(String invoiceUrl) {
		this.invoiceUrl = invoiceUrl;
	}

	public String getBankSlipUrl() {
		return bankSlipUrl;
	}

	public void setBankSlipUrl(String bankSlipUrl) {
		this.bankSlipUrl = bankSlipUrl;
	}

	public String getTransactionReceiptUrl() {
		return transactionReceiptUrl;
	}

	public void setTransactionReceiptUrl(String transactionReceiptUrl) {
		this.transactionReceiptUrl = transactionReceiptUrl;
	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	public Boolean getAnticipated() {
		return anticipated;
	}

	public void setAnticipated(Boolean anticipated) {
		this.anticipated = anticipated;
	}

	public Boolean getAnticipable() {
		return anticipable;
	}

	public void setAnticipable(Boolean anticipable) {
		this.anticipable = anticipable;
	}

	public String getLastInvoiceViewedDate() {
		return lastInvoiceViewedDate;
	}

	public void setLastInvoiceViewedDate(String lastInvoiceViewedDate) {
		this.lastInvoiceViewedDate = lastInvoiceViewedDate;
	}

	public String getLastBankSlipViewedDate() {
		return lastBankSlipViewedDate;
	}

	public void setLastBankSlipViewedDate(String lastBankSlipViewedDate) {
		this.lastBankSlipViewedDate = lastBankSlipViewedDate;
	}

	public Boolean getPostalService() {
		return postalService;
	}

	public void setPostalService(Boolean postalService) {
		this.postalService = postalService;
	}

	public CreditCardDTO getCreditCard() {
		return creditCard;
	}

	public void setCreditCard(CreditCardDTO creditCard) {
		this.creditCard = creditCard;
	}

	public DiscountDTO getDiscount() {
		return discount;
	}

	public void setDiscount(DiscountDTO discount) {
		this.discount = discount;
	}

	public FineDTO getFine() {
		return fine;
	}

	public void setFine(FineDTO fine) {
		this.fine = fine;
	}

	public InterestDTO getInterest() {
		return interest;
	}

	public void setInterest(InterestDTO interest) {
		this.interest = interest;
	}

	public List<SplitDTO> getSplit() {
		return split;
	}

	public void setSplit(List<SplitDTO> split) {
		this.split = split;
	}

	public ChargebackDTO getChargeback() {
		return chargeback;
	}

	public void setChargeback(ChargebackDTO chargeback) {
		this.chargeback = chargeback;
	}

	public Object getRefunds() {
		return refunds;
	}

	public void setRefunds(Object refunds) {
		this.refunds = refunds;
	}

	
}
