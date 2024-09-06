package com.VisNeo4j.App.Algoritmo.Opciones;

public class BPSOOpciones {

	private boolean continuarOpt;
	private int id;
	public BPSOOpciones(boolean continuarOpt, int id) {
		super();
		this.continuarOpt = continuarOpt;
		this.id = id;
	}
	public boolean isContinuarOpt() {
		return continuarOpt;
	}
	public void setContinuarOpt(boolean continuarOpt) {
		this.continuarOpt = continuarOpt;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	
}
