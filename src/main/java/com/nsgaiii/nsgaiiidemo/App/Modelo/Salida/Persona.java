package com.nsgaiii.nsgaiiidemo.App.Modelo.Salida;

public class Persona {
	private String campoPersonas;
	private int valorPersonas;
	public Persona(String campoPersonas, int valorPersonas) {
		super();
		this.campoPersonas = campoPersonas;
		this.valorPersonas = valorPersonas;
	}
	public String getCampoPersonas() {
		return campoPersonas;
	}
	public void setCampoPersonas(String campoPersonas) {
		this.campoPersonas = campoPersonas;
	}
	public int getValorPersonas() {
		return valorPersonas;
	}
	public void setValorPersonas(int valorPersonas) {
		this.valorPersonas = valorPersonas;
	}
	
	

}
