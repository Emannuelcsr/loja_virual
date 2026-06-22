package jdev.mentoria.lojavirtual.model.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LeadCampanhaEmailMarketing implements Serializable {

	private static final long serialVersionUID = 1L;

	
	private String name;
    private LeadCampanhaEmailCadastrado campaign = new LeadCampanhaEmailCadastrado();
    private String email;
    private String dayOfCycle;
    private Integer scoring;
    private String ipAddress;
    private List<TagEmailMarketing> tags = new ArrayList<TagEmailMarketing>();
    private List<CustomFieldValueEmailMarketing> customFieldValues = new ArrayList<CustomFieldValueEmailMarketing>();
    
    
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public LeadCampanhaEmailCadastrado getCampaign() {
		return campaign;
	}
	public void setCampaign(LeadCampanhaEmailCadastrado campaign) {
		this.campaign = campaign;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getDayOfCycle() {
		return dayOfCycle;
	}
	public void setDayOfCycle(String dayOfCycle) {
		this.dayOfCycle = dayOfCycle;
	}
	public Integer getScoring() {
		return scoring;
	}
	public void setScoring(Integer scoring) {
		this.scoring = scoring;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public List<TagEmailMarketing> getTags() {
		return tags;
	}
	public void setTags(List<TagEmailMarketing> tags) {
		this.tags = tags;
	}
	public List<CustomFieldValueEmailMarketing> getCustomFieldValues() {
		return customFieldValues;
	}
	public void setCustomFieldValues(List<CustomFieldValueEmailMarketing> customFieldValues) {
		this.customFieldValues = customFieldValues;
	}
	
    
    
    
}
