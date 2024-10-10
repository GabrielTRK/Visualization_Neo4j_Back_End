package com.VisNeo4j.App.Algoritmo.Parametros.AccelerationCoefficientsUpdate;

import com.VisNeo4j.App.Constantes.Constantes;

public class C1_C2 {
	
	private double c1I;
	private double c2I;
	
	private double c1F;
	private double c2F;
	
	private String method;
	
	public C1_C2(double c1i, double c2i, double c1f, double c2f, String method) {
		super();
		c1I = c1i;
		c2I = c2i;
		c1F = c1f;
		c2F = c2f;
		this.method = method;
	}
	
	public double updateC1(int maxIter, int currIter) {
		switch(this.method) {
		case Constantes.nombreC1_C2UpdateTimeVarying:
			return this.updateC1TimeVarying(maxIter, currIter);
		default:
			return this.c1I;
		}
	}
	
	public double updateC2(int maxIter, int currIter) {
		switch(this.method) {
		case Constantes.nombreC1_C2UpdateTimeVarying:
			return this.updateC2TimeVarying(maxIter, currIter);
		default:
			return this.c1I;
		}
	}
	
	public double updateC1TimeVarying(int maxIter, int currIter) {
		return this.c1I + ((this.c1F - this.c1I)/maxIter) * currIter;
	}
	
	public double updateC2TimeVarying(int maxIter, int currIter) {
		return this.c2I + ((this.c2F - this.c2I)/maxIter) * currIter;
	}
	
	

}
