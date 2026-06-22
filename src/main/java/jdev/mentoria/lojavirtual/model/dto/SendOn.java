package jdev.mentoria.lojavirtual.model.dto;

import java.io.Serializable;

public class SendOn implements Serializable {

	private static final long serialVersionUID = 1L;
    private String date;
    private TimeZone timeZone;
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public TimeZone getTimeZone() {
		return timeZone;
	}
	public void setTimeZone(TimeZone timeZone) {
		this.timeZone = timeZone;
	}
	
    
    
}
