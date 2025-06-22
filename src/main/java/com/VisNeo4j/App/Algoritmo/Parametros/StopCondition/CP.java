package com.VisNeo4j.App.Algoritmo.Parametros.StopCondition;

import com.VisNeo4j.App.Constantes.Constantes;
import com.VisNeo4j.App.Model.Particle;
import com.VisNeo4j.App.Model.Population;

public class CP {

	private int maxFEs;
	private int currentFE = -1;
	private double m;
	private double p;

	private String method;

	public CP(int maxFEs, double m, double p, String method) {
		super();
		this.maxFEs = maxFEs;
		this.m = m;
		this.p = p;
		this.method = method;
	}

	public boolean stopConditionMet(Population particulas, Particle GBest) {
		if(!Constantes.continueOpt) {
			return true;
		}else {
			switch (this.method) {
			case Constantes.nombreCPMaxDistQuick:
				return this.maxDistQuick(particulas, GBest);

			default:
				return this.maxIter();
			}
		}
	}
	
	public boolean maxIter() {
		boolean CP = false;
		if (this.currentFE >= this.maxFEs) {
			CP = true;
		}
		this.currentFE++;
		return CP;
	}

	public boolean maxDistQuick(Population particulas, Particle GBest) {

		int NumClosestParticles = Math.toIntExact(Math.round(particulas.getnumParticles() * this.p));
		int count = 0, pos = 0;

		boolean CP = false;

		while (count < NumClosestParticles && pos < particulas.getnumParticles()) {
			if (Math.abs(
					particulas.getPopulation().get(pos).getObjectives().get(0) - GBest.getObjectives().get(0)) < this.m) {
				count++;
			}
			pos++;
		}
		if (count >= NumClosestParticles) {
			CP = true;
		}

		return CP;
	}

	public int getMaxFEs() {
		return maxFEs;
	}

	public void setMaxFEs(int maxFEs) {
		this.maxFEs = maxFEs;
	}

	public int getcurrentFE() {
		return currentFE;
	}

	public void setcurrentFE(int currentFE) {
		this.currentFE = currentFE;
	}

	public double getM() {
		return m;
	}

	public void setM(double m) {
		this.m = m;
	}

	public double getP() {
		return p;
	}

	public void setP(double p) {
		this.p = p;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

}
