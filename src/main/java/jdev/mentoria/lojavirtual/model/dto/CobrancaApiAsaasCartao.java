package jdev.mentoria.lojavirtual.model.dto;

public class CobrancaApiAsaasCartao {

	private String customer;
	private String billingType;
	private float value;
	private String dueDate;
	private String description;
	private String daysAfterDueDateToRegistrationCancellation;
	private String externalReference;
	private Integer installmentCount;
	private float totalValue;
	private float installmentValue;
	private DiscountCobrancaAsaas discount = new DiscountCobrancaAsaas();
	private InterestCobrancaAsaas interest = new InterestCobrancaAsaas();
	private FineCobrancaAsaas fine = new FineCobrancaAsaas();
	private boolean postalService = false;
	private CartaoCreditoApiAsaas creditCard = new CartaoCreditoApiAsaas();
	private CartaoCreditoApiAsaasHolderInfo creditCardHolderInfo = new CartaoCreditoApiAsaasHolderInfo();
	public String getCustomer() {
		return customer;
	}
	public void setCustomer(String customer) {
		this.customer = customer;
	}
	public String getBillingType() {
		return billingType;
	}
	public void setBillingType(String billingType) {
		this.billingType = billingType;
	}
	public float getValue() {
		return value;
	}
	public void setValue(float value) {
		this.value = value;
	}
	public String getDueDate() {
		return dueDate;
	}
	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDaysAfterDueDateToRegistrationCancellation() {
		return daysAfterDueDateToRegistrationCancellation;
	}
	public void setDaysAfterDueDateToRegistrationCancellation(String daysAfterDueDateToRegistrationCancellation) {
		this.daysAfterDueDateToRegistrationCancellation = daysAfterDueDateToRegistrationCancellation;
	}
	public String getExternalReference() {
		return externalReference;
	}
	public void setExternalReference(String externalReference) {
		this.externalReference = externalReference;
	}
	public Integer getInstallmentCount() {
		return installmentCount;
	}
	public void setInstallmentCount(Integer installmentCount) {
		this.installmentCount = installmentCount;
	}
	public float getTotalValue() {
		return totalValue;
	}
	public void setTotalValue(float totalValue) {
		this.totalValue = totalValue;
	}
	public float getInstallmentValue() {
		return installmentValue;
	}
	public void setInstallmentValue(float installmentValue) {
		this.installmentValue = installmentValue;
	}
	public DiscountCobrancaAsaas getDiscount() {
		return discount;
	}
	public void setDiscount(DiscountCobrancaAsaas discount) {
		this.discount = discount;
	}
	public InterestCobrancaAsaas getInterest() {
		return interest;
	}
	public void setInterest(InterestCobrancaAsaas interest) {
		this.interest = interest;
	}
	public FineCobrancaAsaas getFine() {
		return fine;
	}
	public void setFine(FineCobrancaAsaas fine) {
		this.fine = fine;
	}
	public boolean isPostalService() {
		return postalService;
	}
	public void setPostalService(boolean postalService) {
		this.postalService = postalService;
	}
	public CartaoCreditoApiAsaas getCreditCard() {
		return creditCard;
	}
	public void setCreditCard(CartaoCreditoApiAsaas creditCard) {
		this.creditCard = creditCard;
	}
	public CartaoCreditoApiAsaasHolderInfo getCreditCardHolderInfo() {
		return creditCardHolderInfo;
	}
	public void setCreditCardHolderInfo(CartaoCreditoApiAsaasHolderInfo creditCardHolderInfo) {
		this.creditCardHolderInfo = creditCardHolderInfo;
	}
	
	
	
}
