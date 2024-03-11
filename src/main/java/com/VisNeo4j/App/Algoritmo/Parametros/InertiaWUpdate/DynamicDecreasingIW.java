package com.VisNeo4j.App.Algoritmo.Parametros.InertiaWUpdate;

public class DynamicDecreasingIW extends InertiaWGenérica{
	
	private double u = 1.0001;

	public DynamicDecreasingIW(double inertiaW) {
		super(inertiaW);
	}
	
	@Override
	public void updateIntertiaW(int iter) {
		super.setInertiaW(super.getInertiaW() * Math.pow(this.u, -iter));
	}

}
