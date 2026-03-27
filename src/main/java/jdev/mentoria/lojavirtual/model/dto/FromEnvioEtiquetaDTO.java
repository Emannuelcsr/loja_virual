package jdev.mentoria.lojavirtual.model.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FromEnvioEtiquetaDTO implements Serializable  {
	private static final long serialVersionUID = 1L;

	
    private String name;
    private String address;
    private String document;
    private String city;
    
    @JsonProperty("postal_code")
    private String postalCode;
    
    private String email;
    private String phone;
    
    @JsonProperty("company_document")
    private String companyDocument;
    
    @JsonProperty("state_register")
    private String stateRegister;
    
    @JsonProperty("economic_activity_code")
    private String economicActivityCode;
    private String complement;
    private String number;
    private String district;
    
    @JsonProperty("state_abbr")
    private String stateAbbr;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getDocument() {
		return document;
	}
	public void setDocument(String document) {
		this.document = document;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getCompanyDocument() {
		return companyDocument;
	}
	public void setCompanyDocument(String companyDocument) {
		this.companyDocument = companyDocument;
	}
	public String getStateRegister() {
		return stateRegister;
	}
	public void setStateRegister(String stateRegister) {
		this.stateRegister = stateRegister;
	}
	public String getEconomicActivityCode() {
		return economicActivityCode;
	}
	public void setEconomicActivityCode(String economicActivityCode) {
		this.economicActivityCode = economicActivityCode;
	}
	public String getComplement() {
		return complement;
	}
	public void setComplement(String complement) {
		this.complement = complement;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public String getStateAbbr() {
		return stateAbbr;
	}
	public void setStateAbbr(String stateAbbr) {
		this.stateAbbr = stateAbbr;
	}
	
    
    
}
