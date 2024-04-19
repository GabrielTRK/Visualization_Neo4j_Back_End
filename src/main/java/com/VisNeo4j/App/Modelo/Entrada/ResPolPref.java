package com.VisNeo4j.App.Modelo.Entrada;

import java.util.List;

public class ResPolPref {
	
	private List<String> pol;
	private List<Integer> order;
	public ResPolPref(List<String> pol, List<Integer> order) {
		super();
		this.pol = pol;
		this.order = order;
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
	@Override
	public String toString() {
		return "ResPolPref [pol=" + pol + ", order=" + order + "]";
	}
	
	

}
