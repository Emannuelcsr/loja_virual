package jdev.mentoria.lojavirtual.model.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AsaasWebhookDTO  implements Serializable{

    private String id;
    private String event;
    private String dateCreated;

    private AccountDTO account = new AccountDTO();
    private PaymentDTO payment = new PaymentDTO();
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getEvent() {
		return event;
	}
	public void setEvent(String event) {
		this.event = event;
	}
	public String getDateCreated() {
		return dateCreated;
	}
	public void setDateCreated(String dateCreated) {
		this.dateCreated = dateCreated;
	}
	public AccountDTO getAccount() {
		return account;
	}
	public void setAccount(AccountDTO account) {
		this.account = account;
	}
	public PaymentDTO getPayment() {
		return payment;
	}
	public void setPayment(PaymentDTO payment) {
		this.payment = payment;
	}
    
	public Boolean boletoPixFaturaPaga() {
		
		return payment.getStatus().equalsIgnoreCase("CONFIRMED") || payment.getStatus().equalsIgnoreCase("RECEIVED");
	}
    
	
}
