package jdev.mentoria.lojavirtual.model.dto;

import java.io.Serializable;

public class ExternalLexpad implements Serializable {

	private static final long serialVersionUID = 1L;
    private String dataSourceUrl;
    private String dataSourceToken;
    
    
	public String getDataSourceUrl() {
		return dataSourceUrl;
	}
	public void setDataSourceUrl(String dataSourceUrl) {
		this.dataSourceUrl = dataSourceUrl;
	}
	public String getDataSourceToken() {
		return dataSourceToken;
	}
	public void setDataSourceToken(String dataSourceToken) {
		this.dataSourceToken = dataSourceToken;
	}

    
    
}
