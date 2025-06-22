package com.VisNeo4j.App.Model.Inbound;

import java.util.List;
import java.util.Map;

public class PreferencesConstraints {
	
	private List<String> pol;
	private List<Integer> order;
	private Map<Integer, Double> restricciones;
	/*public ResPolPref(List<String> pol, List<Integer> order) {
		super();
		this.pol = pol;
		this.order = order;
	}*/
	
	public PreferencesConstraints(List<String> pol, List<Integer> order, Map<Integer, Double> restricciones) {
		super();
		this.pol = pol;
		this.order = order;
		this.restricciones = restricciones;
	}
	
	public List<String> getPol() {
		return pol;
	}
	public void setPol(List<String> pol) {
		this.pol = pol;
	}
	public List<Integer> getOrdenObj() {
		return order;
	}
	public void setOrdenObj(List<Integer> order) {
		this.order = order;
	}
	
	public List<Integer> getOrder() {
		return order;
	}

	public void setOrder(List<Integer> order) {
		this.order = order;
	}

	public Map<Integer, Double> getRestricciones() {
		return restricciones;
	}

	public void setRestricciones(Map<Integer, Double> restricciones) {
		this.restricciones = restricciones;
	}

	@Override
	public String toString() {
		return "ResPolPref [pol=" + pol + ", order=" + order + "]";
	}
	
	

}
