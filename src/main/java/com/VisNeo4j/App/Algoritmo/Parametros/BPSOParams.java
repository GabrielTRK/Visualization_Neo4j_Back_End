package com.VisNeo4j.App.Algoritmo.Parametros;

import com.VisNeo4j.App.Algoritmo.Parametros.CondicionParada.CPGenérica;
import com.VisNeo4j.App.Algoritmo.Parametros.InertiaWUpdate.InertiaWGenérica;

public class BPSOParams {
	
	private int numIndividuos;
	private InertiaWGenérica inertiaW;
	private double c1;
	private double c2;
	private CPGenérica condicionParada;
	
	public BPSOParams(int numIndividuos, InertiaWGenérica inertiaW, double c1, double c2, int max_Num_Iteraciones) {
		super();
		this.numIndividuos = numIndividuos;
		this.inertiaW = inertiaW;
		this.c1 = c1;
		this.c2 = c2;
		this.condicionParada = new CPGenérica(max_Num_Iteraciones, 0);
	}
	
	public boolean condicionParadaConseguida() {
		return this.condicionParada.condicionParadaConseguida();
	}

	public int getNumIndividuos() {
		return numIndividuos;
	}

	public void setNumIndividuos(int numIndividuos) {
		this.numIndividuos = numIndividuos;
	}

	public InertiaWGenérica getInertiaW() {
		return inertiaW;
	}

	public void setInertiaW(InertiaWGenérica inertiaW) {
		this.inertiaW = inertiaW;
	}
	
	public void updateInertiaW() {
		this.inertiaW.updateIntertiaW(this.getIteracionActual());
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

}
