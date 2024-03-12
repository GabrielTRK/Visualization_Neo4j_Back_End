package com.VisNeo4j.App.Algoritmo.Parametros.InertiaWUpdate;

import com.VisNeo4j.App.Constantes.Constantes;

public class InertiaW {
	
	private double inertiaW;
	private String method;
	private double u = 1.0001;

	public InertiaW(double inertiaW, String method) {
		super();
		this.inertiaW = inertiaW;
		this.method = method;
	}
	
	public void updateIntertiaW(int iter) {
		switch(this.method) {
		case Constantes.nombreIWDyanamicDecreasing:
			this.DynamicDecreasingIW(iter);
			break;
		default:
			this.generic();
			break; 
		}
		
	}
	
	public void DynamicDecreasingIW(int iter) {
		this.inertiaW *= Math.pow(this.u, -iter);
	}
	
	public void generic() {
		this.inertiaW *= 0.9;
	}

	public double getInertiaW() {
		return inertiaW;
	}

	public void setInertiaW(double inertiaW) {
		this.inertiaW = inertiaW;
	}
	
	

}
