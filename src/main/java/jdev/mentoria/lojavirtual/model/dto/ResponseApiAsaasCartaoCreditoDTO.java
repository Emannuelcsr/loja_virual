package jdev.mentoria.lojavirtual.model.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseApiAsaasCartaoCreditoDTO {

	private String object;
	private String id;
	private String dateCreated;
	private String customer;
	private String subscription;
	private String installment = null;
	private String checkoutSession;
	private String paymentLink = null;
	private BigDecimal value;
	private BigDecimal netValue;
	private BigDecimal originalValue = null;
	private BigDecimal interestValue = null;
	private String description; 
	private String billingType;
	private CreditCardResponseApiAsaasDTO creditCard = new CreditCardResponseApiAsaasDTO();
	private Boolean canBePaidAfterDueDate;
	private String pixTransaction = null;
	private String pixQrCodeId;
	private String status;
	private String dueDate;
	private String originalDueDate;
	private String paymentDate = null;
	private String clientPaymentDate;
	private Integer installmentNumber;
	private String invoiceUrl;
	private String invoiceNumber;
	private String externalReference = null;
	private Boolean deleted;
	private Boolean anticipated;
	private Boolean anticipable;
	private String creditDate;
	private String estimatedCreditDate = null;
	private String transactionReceiptUrl;
	private String nossoNumero = null;
	private String bankSlipUrl = null;
	private DiscountResponseApiAsaasDTO discount = new DiscountResponseApiAsaasDTO();
	private FineResponseApiAsaasDTO fine = new FineResponseApiAsaasDTO();
	private InterestResponseApiAsaasDTO interest = new InterestResponseApiAsaasDTO();
	private List<SplitResponseApiAsaasDTO> split = new ArrayList<SplitResponseApiAsaasDTO>();
	private Boolean postalService = null;
	private Integer daysAfterDueDateToRegistrationCancellation;
	private ChargebackResponseApiAsaasDTO chargeback;
	private EscrowResponseApiAsaasDTO escrow;
	private List<RefundResponseApiAsaasDTO> refunds = new ArrayList<RefundResponseApiAsaasDTO>();

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

	public String getCheckoutSession() {
		return checkoutSession;
	}

	public void setCheckoutSession(String checkoutSession) {
		this.checkoutSession = checkoutSession;
	}

	public String getPaymentLink() {
		return paymentLink;
	}

	public void setPaymentLink(String paymentLink) {
		this.paymentLink = paymentLink;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getBillingType() {
		return billingType;
	}

	public void setBillingType(String billingType) {
		this.billingType = billingType;
	}

	public CreditCardResponseApiAsaasDTO getCreditCard() {
		return creditCard;
	}

	public void setCreditCard(CreditCardResponseApiAsaasDTO creditCard) {
		this.creditCard = creditCard;
	}

	public Boolean getCanBePaidAfterDueDate() {
		return canBePaidAfterDueDate;
	}

	public void setCanBePaidAfterDueDate(Boolean canBePaidAfterDueDate) {
		this.canBePaidAfterDueDate = canBePaidAfterDueDate;
	}

	public String getPixTransaction() {
		return pixTransaction;
	}

	public void setPixTransaction(String pixTransaction) {
		this.pixTransaction = pixTransaction;
	}

	public String getPixQrCodeId() {
		return pixQrCodeId;
	}

	public void setPixQrCodeId(String pixQrCodeId) {
		this.pixQrCodeId = pixQrCodeId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public String getInvoiceUrl() {
		return invoiceUrl;
	}

	public void setInvoiceUrl(String invoiceUrl) {
		this.invoiceUrl = invoiceUrl;
	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public String getExternalReference() {
		return externalReference;
	}

	public void setExternalReference(String externalReference) {
		this.externalReference = externalReference;
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

	public String getCreditDate() {
		return creditDate;
	}

	public void setCreditDate(String creditDate) {
		this.creditDate = creditDate;
	}

	public String getEstimatedCreditDate() {
		return estimatedCreditDate;
	}

	public void setEstimatedCreditDate(String estimatedCreditDate) {
		this.estimatedCreditDate = estimatedCreditDate;
	}

	public String getTransactionReceiptUrl() {
		return transactionReceiptUrl;
	}

	public void setTransactionReceiptUrl(String transactionReceiptUrl) {
		this.transactionReceiptUrl = transactionReceiptUrl;
	}

	public String getNossoNumero() {
		return nossoNumero;
	}

	public void setNossoNumero(String nossoNumero) {
		this.nossoNumero = nossoNumero;
	}

	public String getBankSlipUrl() {
		return bankSlipUrl;
	}

	public void setBankSlipUrl(String bankSlipUrl) {
		this.bankSlipUrl = bankSlipUrl;
	}

	public DiscountResponseApiAsaasDTO getDiscount() {
		return discount;
	}

	public void setDiscount(DiscountResponseApiAsaasDTO discount) {
		this.discount = discount;
	}

	public FineResponseApiAsaasDTO getFine() {
		return fine;
	}

	public void setFine(FineResponseApiAsaasDTO fine) {
		this.fine = fine;
	}

	public InterestResponseApiAsaasDTO getInterest() {
		return interest;
	}

	public void setInterest(InterestResponseApiAsaasDTO interest) {
		this.interest = interest;
	}

	public List<SplitResponseApiAsaasDTO> getSplit() {
		return split;
	}

	public void setSplit(List<SplitResponseApiAsaasDTO> split) {
		this.split = split;
	}

	public Boolean getPostalService() {
		return postalService;
	}

	public void setPostalService(Boolean postalService) {
		this.postalService = postalService;
	}

	public Integer getDaysAfterDueDateToRegistrationCancellation() {
		return daysAfterDueDateToRegistrationCancellation;
	}

	public void setDaysAfterDueDateToRegistrationCancellation(Integer daysAfterDueDateToRegistrationCancellation) {
		this.daysAfterDueDateToRegistrationCancellation = daysAfterDueDateToRegistrationCancellation;
	}

	public ChargebackResponseApiAsaasDTO getChargeback() {
		return chargeback;
	}

	public void setChargeback(ChargebackResponseApiAsaasDTO chargeback) {
		this.chargeback = chargeback;
	}

	public EscrowResponseApiAsaasDTO getEscrow() {
		return escrow;
	}

	public void setEscrow(EscrowResponseApiAsaasDTO escrow) {
		this.escrow = escrow;
	}

	public List<RefundResponseApiAsaasDTO> getRefunds() {
		return refunds;
	}

	public void setRefunds(List<RefundResponseApiAsaasDTO> refunds) {
		this.refunds = refunds;
	}
}