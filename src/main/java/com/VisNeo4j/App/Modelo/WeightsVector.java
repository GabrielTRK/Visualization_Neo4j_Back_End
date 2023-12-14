package com.VisNeo4j.App.Modelo;

import java.util.List;

public class WeightsVector {
	
	private List<Double> weights;

	public WeightsVector() {
		super();
	}

	public List<Double> getWeights() {
		return weights;
	}

	public void setWeights(List<Double> weights) {
		this.weights = weights;
	}

	@Override
	public String toString() {
		return "WeightsVector [weights=" + weights + "]";
	}
	
	

}
