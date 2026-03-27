package jdev.mentoria.lojavirtual.model.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProductsEnvioEtiquetaDTO implements Serializable {

	private static final long serialVersionUID = 1L;

    private String name;
    private String quantity;
    
    @JsonProperty("unitary_value")
    private BigDecimal unitaryValue;
    
    
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	public BigDecimal getUnitaryValue() {
		return unitaryValue;
	}
	public void setUnitaryValue(BigDecimal unitaryValue) {
		this.unitaryValue = unitaryValue;
	}
	
	
}
