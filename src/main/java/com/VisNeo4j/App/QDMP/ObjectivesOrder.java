package com.VisNeo4j.App.QDMP;

import java.util.List;
import java.util.Map;

public class ObjectivesOrder {
	
	private List<Integer> order;
	private Map<Integer, Double> restricciones;

	public ObjectivesOrder() {
		super();
	}
	
	public ObjectivesOrder(List<Integer> order) {
		this.order = order;
	}
	
	public ObjectivesOrder(List<Integer> order, Map<Integer, Double> restricciones) {
		this.order = order;
		this.restricciones = restricciones;
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
		return "ObjectivesOrder [order=" + order + "]";
	}

	
	

}
