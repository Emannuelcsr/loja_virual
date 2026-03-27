package jdev.mentoria.lojavirtual.model.dto;

import java.io.Serializable;

public class VolumesEnvioEtiquetaDTO  implements Serializable {

	private static final long serialVersionUID = 1L;

    private String weight;
    private String length;
    private String width;
    private String height;
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public String getLength() {
		return length;
	}
	public void setLength(String length) {
		this.length = length;
	}
	public String getWidth() {
		return width;
	}
	public void setWidth(String width) {
		this.width = width;
	}
	public String getHeight() {
		return height;
	}
	public void setHeight(String height) {
		this.height = height;
	}
	
    
    
    
}
