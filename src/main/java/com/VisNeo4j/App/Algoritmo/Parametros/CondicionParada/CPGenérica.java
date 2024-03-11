package com.VisNeo4j.App.Algoritmo.Parametros.CondicionParada;

public class CPGenérica {
	
	private int maxIteraciones;
	private int numIteracionesActual = 0;
	public CPGenérica(int maxIteraciones, int numIteracionesActual) {
		super();
		this.maxIteraciones = maxIteraciones;
		this.numIteracionesActual = numIteracionesActual;
	}
	
	public boolean condicionParadaConseguida() {
		boolean CP = false;
		if(this.numIteracionesActual >= this.maxIteraciones) {
			CP = true;
		}
		this.numIteracionesActual++;
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
	
	

}
