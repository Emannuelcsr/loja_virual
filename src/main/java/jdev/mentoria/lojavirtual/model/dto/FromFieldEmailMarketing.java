package jdev.mentoria.lojavirtual.model.dto;

import java.io.Serializable;

public class FromFieldEmailMarketing  implements Serializable{
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String fromFieldId;
    private String email;
    private String rewrittenEmail;
    private String name;
    private String isDefault;
    private String isActive;
    private String createdOn;
    private String href;
    private DomainDTO domain;
	public String getFromFieldId() {
		return fromFieldId;
	}
	public void setFromFieldId(String fromFieldId) {
		this.fromFieldId = fromFieldId;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getRewrittenEmail() {
		return rewrittenEmail;
	}
	public void setRewrittenEmail(String rewrittenEmail) {
		this.rewrittenEmail = rewrittenEmail;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIsDefault() {
		return isDefault;
	}
	public void setIsDefault(String isDefault) {
		this.isDefault = isDefault;
	}
	public String getIsActive() {
		return isActive;
	}
	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}
	public String getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}
	public String getHref() {
		return href;
	}
	public void setHref(String href) {
		this.href = href;
	}
	public DomainDTO getDomain() {
		return domain;
	}
	public void setDomain(DomainDTO domain) {
		this.domain = domain;
	}
    
    

}
