package jdev.mentoria.lojavirtual.model.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class SendSettingsNewsLetter implements Serializable {

	private static final long serialVersionUID = 1L;
    private List<String> selectedCampaigns = new ArrayList<String>();
    private List<String> selectedSegments= new ArrayList<String>();
    private List<String> selectedSuppressions= new ArrayList<String>();
    private List<String> excludedCampaigns = new ArrayList<String>();
    private List<String> excludedSegments = new ArrayList<String>();
    private List<String> selectedContacts= new ArrayList<String>();
    private String timeTravel = "false";
    private String perfectTiming ="false";
    private ExternalLexpad externalLexpad;
    private SendOn sendOn;
    
	public List<String> getSelectedCampaigns() {
		return selectedCampaigns;
	}
	public void setSelectedCampaigns(List<String> selectedCampaigns) {
		this.selectedCampaigns = selectedCampaigns;
	}
	public List<String> getSelectedSegments() {
		return selectedSegments;
	}
	public void setSelectedSegments(List<String> selectedSegments) {
		this.selectedSegments = selectedSegments;
	}
	public List<String> getSelectedSuppressions() {
		return selectedSuppressions;
	}
	public void setSelectedSuppressions(List<String> selectedSuppressions) {
		this.selectedSuppressions = selectedSuppressions;
	}
	public List<String> getExcludedCampaigns() {
		return excludedCampaigns;
	}
	public void setExcludedCampaigns(List<String> excludedCampaigns) {
		this.excludedCampaigns = excludedCampaigns;
	}
	public List<String> getExcludedSegments() {
		return excludedSegments;
	}
	public void setExcludedSegments(List<String> excludedSegments) {
		this.excludedSegments = excludedSegments;
	}
	public List<String> getSelectedContacts() {
		return selectedContacts;
	}
	public void setSelectedContacts(List<String> selectedContacts) {
		this.selectedContacts = selectedContacts;
	}
	public String getTimeTravel() {
		return timeTravel;
	}
	public void setTimeTravel(String timeTravel) {
		this.timeTravel = timeTravel;
	}
	public String getPerfectTiming() {
		return perfectTiming;
	}
	public void setPerfectTiming(String perfectTiming) {
		this.perfectTiming = perfectTiming;
	}
	public ExternalLexpad getExternalLexpad() {
		return externalLexpad;
	}
	public void setExternalLexpad(ExternalLexpad externalLexpad) {
		this.externalLexpad = externalLexpad;
	}
	public SendOn getSendOn() {
		return sendOn;
	}
	public void setSendOn(SendOn sendOn) {
		this.sendOn = sendOn;
	}
	
    
    
}
