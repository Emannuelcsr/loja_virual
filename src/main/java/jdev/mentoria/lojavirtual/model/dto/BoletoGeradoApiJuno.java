package jdev.mentoria.lojavirtual.model.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class BoletoGeradoApiJuno implements Serializable {

	private static final long serialVersionUID = 1L;

	private jdev.mentoria.lojavirtual.model.dto.Embedded _embedded = new jdev.mentoria.lojavirtual.model.dto.Embedded();

	private List<Links> _links = new ArrayList<Links>();



	public jdev.mentoria.lojavirtual.model.dto.Embedded get_embedded() {
		return _embedded;
	}

	public void set_embedded(jdev.mentoria.lojavirtual.model.dto.Embedded _embedded) {
		this._embedded = _embedded;
	}

	public void set_links(List<Links> _links) {
		this._links = _links;
	}

	public List<Links> get_links() {
		return _links;
	}
}