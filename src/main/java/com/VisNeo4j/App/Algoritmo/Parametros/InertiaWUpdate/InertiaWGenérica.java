package com.VisNeo4j.App.Algoritmo.Parametros.InertiaWUpdate;

public class InertiaWGenérica {
	
	private double inertiaW;

	public InertiaWGenérica(double inertiaW) {
		super();
		this.inertiaW = inertiaW;
	}
	
	public void updateIntertiaW(int iter) {
		this.inertiaW *= 0.9;
	}

	public double getInertiaW() {
		return inertiaW;
	}

	public void setInertiaW(double inertiaW) {
		this.inertiaW = inertiaW;
	}
	
	

}
