package com.VisNeo4j.App.Algoritmo.Parametros;

import com.VisNeo4j.App.Algoritmo.Parametros.InertiaWUpdate.InertiaW;
import com.VisNeo4j.App.Algoritmo.Parametros.StopCondition.CP;
import com.VisNeo4j.App.Modelo.Particle;
import com.VisNeo4j.App.Modelo.Population;

public class BPSOParams {
	
	private int numParticles;
	private InertiaW inertiaW;
	private double c1;
	private double c2;
	private CP stopCondition;
	
	public BPSOParams(int numParticles, double inertiaW, double c1, double c2, int maxIteraciones, 
			String CPMethod, String IWMethod) {
		super();
		this.numParticles = numParticles;
		this.inertiaW = new InertiaW(inertiaW, IWMethod);
		this.c1 = c1;
		this.c2 = c2;
		this.stopCondition = new CP(maxIteraciones, 0, 0, CPMethod);
	}
	
	public BPSOParams(int numParticles, double inertiaW, double c1, double c2, int maxIteraciones, 
			double m, double p, String CPMethod, String IWMethod) {
		super();
		this.numParticles = numParticles;
		this.inertiaW = new InertiaW(inertiaW, IWMethod);
		this.c1 = c1;
		this.c2 = c2;
		this.stopCondition = new CP(maxIteraciones, m, p, CPMethod);
	}
	
	public boolean stopConditionMet(Population particulas, Particle GBest) {
		return this.stopCondition.stopConditionMet(particulas, GBest);
	}

	public int getnumParticles() {
		return numParticles;
	}

	public void setnumParticles(int numParticles) {
		this.numParticles = numParticles;
	}

	public InertiaW getInertiaW() {
		return inertiaW;
	}

	public void setInertiaW(InertiaW inertiaW) {
		this.inertiaW = inertiaW;
	}
	
	public void updateInertiaW() {
		this.inertiaW.updateIntertiaW(this.getcurrentFE(), this.stopCondition.getMaxFEs());
	}

	public double getC1() {
		return c1;
	}

	public void setC1(double c1) {
		this.c1 = c1;
	}

	public double getC2() {
		return c2;
	}

	public void setC2(double c2) {
		this.c2 = c2;
	}

	public int getMax_FEs() {
		return this.stopCondition.getMaxFEs();
	}

	public void setMaxFEs(int maxFEs) {
		this.stopCondition.setMaxFEs(maxFEs);
	}
	
	public int getcurrentFE() {
		return this.stopCondition.getcurrentFE();
	}

	public CP getstopCondition() {
		return stopCondition;
	}

	public void setstopCondition(CP stopCondition) {
		this.stopCondition = stopCondition;
	}

}
