package com.nsgaiii.nsgaiiidemo.App.Modelo.Salida;

public class FitnessI {
	
	private Double iteracion;
	private Double fitness;
	public FitnessI(Double iteracion, Double fitness) {
		super();
		this.iteracion = iteracion;
		this.fitness = fitness;
	}
	public Double getIteracion() {
		return iteracion;
	}
	public void setIteracion(Double iteracion) {
		this.iteracion = iteracion;
	}
	public Double getFitness() {
		return fitness;
	}
	public void setFitness(Double fitness) {
		this.fitness = fitness;
	}
	
	

}
