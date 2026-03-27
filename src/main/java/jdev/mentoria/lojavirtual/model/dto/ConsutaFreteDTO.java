package jdev.mentoria.lojavirtual.model.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ConsutaFreteDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private FromDTO from;
	
	private ToDTO to;
	
	private List<ProductosDTO> products = new ArrayList<ProductosDTO>();

	public FromDTO getFrom() {
		return from;
	}

	public void setFrom(FromDTO from) {
		this.from = from;
	}

	public ToDTO getTo() {
		return to;
	}

	public void setTo(ToDTO to) {
		this.to = to;
	}

	public List<ProductosDTO> getProducts() {
		return products;
	}

	public void setProducts(List<ProductosDTO> products) {
		this.products = products;
	}
	
	
	
	
}
