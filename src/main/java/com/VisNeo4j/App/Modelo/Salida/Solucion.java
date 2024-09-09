package com.VisNeo4j.App.Modelo.Salida;

import java.util.ArrayList;
import java.util.List;

import com.VisNeo4j.App.Constantes.Constantes;
import com.VisNeo4j.App.QDMP.ObjectivesOrder;

public class Solucion {
	
	private int id;
	private int iter;
	private double fitness;
	private List<Objetivo> obj;
	private boolean temporal;
	
	public Solucion(int id, int iter, double fitness, List<Double> obj, ObjectivesOrder order) {
		super();
		this.id = id;
		this.iter = iter;
		this.fitness = fitness;
		this.obj = new ArrayList<>();
		this.obj.add(new Objetivo(Constantes.nombresRestricciones.get(0), obj.get(0)));
		for(int i = 0; i < order.getOrder().size(); i++) {
			this.obj.add(new Objetivo(Constantes.nombreObjetivos.get(order.getOrder().get(i)-1), obj.get(order.getOrder().get(i))));
		}
		this.temporal = false;
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
	public List<Objetivo> getObj() {
		return obj;
	}
	public void setObj(List<Objetivo> obj) {
		this.obj = obj;
	}
	public boolean isTemporal() {
		return temporal;
	}
	public void setTemporal(boolean temporal) {
		this.temporal = temporal;
	}
	
	
	

}
