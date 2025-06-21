package com.VisNeo4j.App.Modelo.Salida;

import com.VisNeo4j.App.Algoritmo.Parametros.InertiaWUpdate.InertiaW;
import com.VisNeo4j.App.Algoritmo.Parametros.StopCondition.CP;
import com.VisNeo4j.App.Modelo.Particle;
import com.VisNeo4j.App.Modelo.Population;

public class BPSOParamsSalida {
	
	private int numIndividuos;
	private double inertiaW;
	private double c1;
	private double c2;
	
	public BPSOParamsSalida(int numIndividuos, double inertiaW, double c1, double c2) {
		super();
		this.numIndividuos = numIndividuos;
		this.inertiaW = inertiaW;
		this.c1 = c1;
		this.c2 = c2;
	}
	public int getNumIndividuos() {
		return numIndividuos;
	}
	public void setNumIndividuos(int numIndividuos) {
		this.numIndividuos = numIndividuos;
	}
	public double getInertiaW() {
		return inertiaW;
	}
	public void setInertiaW(double inertiaW) {
		this.inertiaW = inertiaW;
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
	
	

}
