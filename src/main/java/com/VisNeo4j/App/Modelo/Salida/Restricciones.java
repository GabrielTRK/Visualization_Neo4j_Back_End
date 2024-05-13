package com.VisNeo4j.App.Modelo.Salida;

import java.util.List;

public class Restricciones {
	
	private List<String> pol;
	private Double epi;
	public Restricciones(List<String> pol, Double epi) {
		super();
		this.pol = pol;
		this.epi = epi;
	}
	
	public List<String> getPol() {
		return pol;
	}

	public void setPol(List<String> pol) {
		this.pol = pol;
	}

	public Double getEpi() {
		return epi;
	}
	public void setEpi(Double epi) {
		this.epi = epi;
	}
	
	

}
