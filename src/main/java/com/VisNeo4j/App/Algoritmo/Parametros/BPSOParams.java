package com.VisNeo4j.App.Algoritmo.Parametros;

import com.VisNeo4j.App.Algoritmo.Parametros.CondicionParada.CP;
import com.VisNeo4j.App.Algoritmo.Parametros.InertiaWUpdate.InertiaW;
import com.VisNeo4j.App.Modelo.Individuo;
import com.VisNeo4j.App.Modelo.Poblacion;

public class BPSOParams {
	
	private int numIndividuos;
	private InertiaW inertiaW;
	private double c1;
	private double c2;
	private CP condicionParada;
	
	public BPSOParams(int numIndividuos, double inertiaW, double c1, double c2, int maxIteraciones, 
			double m, double p, String CPMethod, String IWMethod) {
		super();
		this.numIndividuos = numIndividuos;
		this.inertiaW = new InertiaW(inertiaW, IWMethod);
		this.c1 = c1;
		this.c2 = c2;
		this.condicionParada = new CP(maxIteraciones, m, p, CPMethod);
	}
	
	public boolean condicionParadaConseguida(Poblacion particulas, Individuo GBest) {
		return this.condicionParada.condicionParadaConseguida(particulas, GBest);
	}

	public int getNumIndividuos() {
		return numIndividuos;
	}

	public void setNumIndividuos(int numIndividuos) {
		this.numIndividuos = numIndividuos;
	}

	public InertiaW getInertiaW() {
		return inertiaW;
	}

	public void setInertiaW(InertiaW inertiaW) {
		this.inertiaW = inertiaW;
	}
	
	public void updateInertiaW() {
		this.inertiaW.updateIntertiaW(this.getIteracionActual(), this.condicionParada.getMaxIteraciones());
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

	public int getMax_Num_Iteraciones() {
		return this.condicionParada.getMaxIteraciones();
	}

	public void setMax_Num_Iteraciones(int max_Num_Iteraciones) {
		this.condicionParada.setMaxIteraciones(max_Num_Iteraciones);
	}
	
	public int getIteracionActual() {
		return this.condicionParada.getNumIteracionesActual();
	}

	public CP getCondicionParada() {
		return condicionParada;
	}

	public void setCondicionParada(CP condicionParada) {
		this.condicionParada = condicionParada;
	}

}
