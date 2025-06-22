package com.VisNeo4j.App.Model.Outbound;

import java.util.List;

public class OrdenObjSalida {
	
	private List<Integer> order;

	public OrdenObjSalida(List<Integer> order) {
		super();
		this.order = order;
	}

	public List<Integer> getOrder() {
		return order;
	}

	public void setOrder(List<Integer> order) {
		this.order = order;
	}

	@Override
	public String toString() {
		return "ObjectivesOrder [order=" + order + "]";
	}

	
	

}
