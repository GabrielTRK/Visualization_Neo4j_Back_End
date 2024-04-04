package com.VisNeo4j.App.Modelo.Salida;

import java.util.List;

public class Solucion {
	
	private int id;
	private int iter;
	private double fitness;
	private List<Double> obj;
	private double res;
	
	public Solucion(int id, int iter, double fitness, List<Double> obj, double res) {
		super();
		this.id = id;
		this.iter = iter;
		this.fitness = fitness;
		this.obj = obj;
		this.res = res;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getIter() {
		return iter;
	}
	public void setIter(int iter) {
		this.iter = iter;
	}
	public double getFitness() {
		return fitness;
	}
	public void setFitness(double fitness) {
		this.fitness = fitness;
	}
	public List<Double> getObj() {
		return obj;
	}
	public void setObj(List<Double> obj) {
		this.obj = obj;
	}
	public double getRes() {
		return res;
	}
	public void setRes(double res) {
		this.res = res;
	}
	
	
	

}
