package com.VisNeo4j.App.Modelo.Salida;

import java.util.List;
import java.util.Map;

public class Restricciones {
	
	private List<String> pol;
	private Map<Integer, Double> restricciones;
	public Restricciones(List<String> pol, Map<Integer, Double> restricciones) {
		super();
		this.pol = pol;
		this.restricciones = restricciones;
	}
	
	public List<String> getPol() {
		return pol;
	}

	public void setPol(List<String> pol) {
		this.pol = pol;
	}

	public Map<Integer, Double> getRestricciones() {
		return restricciones;
	}

	public void setRestricciones(Map<Integer, Double> restricciones) {
		this.restricciones = restricciones;
	}

	
	

}
