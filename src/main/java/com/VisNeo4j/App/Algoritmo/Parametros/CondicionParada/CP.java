package com.VisNeo4j.App.Algoritmo.Parametros.CondicionParada;

import com.VisNeo4j.App.Constantes.Constantes;
import com.VisNeo4j.App.Modelo.Individuo;
import com.VisNeo4j.App.Modelo.Poblacion;

public class CP {

	private int maxIteraciones;
	private int numIteracionesActual = -1;
	private double m;
	private double p;

	private String method;

	public CP(int maxIteraciones, double m, double p, String method) {
		super();
		this.maxIteraciones = maxIteraciones;
		this.m = m;
		this.p = p;
		this.method = method;
	}

	public boolean condicionParadaConseguida(Poblacion particulas, Individuo GBest) {
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
		if (this.numIteracionesActual >= this.maxIteraciones) {
			CP = true;
		}
		this.numIteracionesActual++;
		return CP;
	}

	public boolean maxDistQuick(Poblacion particulas, Individuo GBest) {

		int NumClosestParticles = Math.toIntExact(Math.round(particulas.getNumIndividuos() * this.p));
		int count = 0, pos = 0;

		boolean CP = false;

		while (count < NumClosestParticles && pos < particulas.getNumIndividuos()) {
			if (Math.abs(
					particulas.getPoblacion().get(pos).getObjetivos().get(0) - GBest.getObjetivos().get(0)) < this.m) {
				count++;
			}
			pos++;
		}
		if (count >= NumClosestParticles) {
			CP = true;
		}

		return CP;
	}

	public int getMaxIteraciones() {
		return maxIteraciones;
	}

	public void setMaxIteraciones(int maxIteraciones) {
		this.maxIteraciones = maxIteraciones;
	}

	public int getNumIteracionesActual() {
		return numIteracionesActual;
	}

	public void setNumIteracionesActual(int numIteracionesActual) {
		this.numIteracionesActual = numIteracionesActual;
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
