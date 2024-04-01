package com.VisNeo4j.App.Modelo.Salida;

public class Restricciones {
	
	private String soc;
	private Double epi;
	public Restricciones(String soc, Double epi) {
		super();
		this.soc = soc;
		this.epi = epi;
	}
	public String getSoc() {
		return soc;
	}
	public void setSoc(String soc) {
		this.soc = soc;
	}
	public Double getEpi() {
		return epi;
	}
	public void setEpi(Double epi) {
		this.epi = epi;
	}
	
	

}
