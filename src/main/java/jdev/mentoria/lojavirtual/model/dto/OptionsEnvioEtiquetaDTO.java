package jdev.mentoria.lojavirtual.model.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class OptionsEnvioEtiquetaDTO implements Serializable {

	private static final long serialVersionUID = 1L;


    private String platform; 
    private String reminder; 
    
    @JsonProperty("insurance_value")
    private String insuranceValue; 
    private Boolean receipt;
    
    @JsonProperty("own_hand")
    private Boolean ownHand; 
    private Boolean reverse;
    
    
    private InvoiceEnvioDTO invoice;
    
    
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public String getReminder() {
		return reminder;
	}
	public void setReminder(String reminder) {
		this.reminder = reminder;
	}
	public String getInsuranceValue() {
		return insuranceValue;
	}
	public void setInsuranceValue(String insuranceValue) {
		this.insuranceValue = insuranceValue;
	}
	public Boolean getReceipt() {
		return receipt;
	}
	public void setReceipt(Boolean receipt) {
		this.receipt = receipt;
	}
	public boolean getOwnHand() {
		return ownHand;
	}
	public void setOwnHand(boolean b) {
		this.ownHand = b;
	}
	public Boolean getReverse() {
		return reverse;
	}
	public void setReverse(Boolean reverse) {
		this.reverse = reverse;
	}
	public InvoiceEnvioDTO getInvoice() {
		return invoice;
	}
	public void setInvoice(InvoiceEnvioDTO invoice) {
		this.invoice = invoice;
	}

    
    
    
    
    

    
    
	
}
