package jdev.mentoria.lojavirtual.model.dto;

import java.io.Serializable;
import java.util.List;

public class CustomFieldValueEmailMarketing implements Serializable {

	
	
	private static final long serialVersionUID = 1L;
	
	private String customFieldId;
    private List<String> value;
    
	public String getCustomFieldId() {
		return customFieldId;
	}
	public void setCustomFieldId(String customFieldId) {
		this.customFieldId = customFieldId;
	}
	public List<String> getValue() {
		return value;
	}
	public void setValue(List<String> value) {
		this.value = value;
	}
    
    

	
}
