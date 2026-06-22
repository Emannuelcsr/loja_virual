package jdev.mentoria.lojavirtual.model.dto;

public class DiscountCobrancaAsaas {

	private float value;
	private float dueDateLimitDays;
	private String type = "PERCENTAGE";
	
	
	public float getValue() {
		return value;
	}
	public void setValue(float value) {
		this.value = value;
	}
	public float getDueDateLimitDays() {
		return dueDateLimitDays;
	}
	public void setDueDateLimitDays(float dueDateLimitDays) {
		this.dueDateLimitDays = dueDateLimitDays;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	
	
	
}
