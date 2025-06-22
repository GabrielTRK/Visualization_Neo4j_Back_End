package com.VisNeo4j.App.Model.Outbound;

public class Objetivo {
	
	private String nombre;
	private Double valor;
	public Objetivo(String nombre, Double valor) {
		super();
		this.nombre = nombre;
		this.valor = valor;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public Double getValor() {
		return valor;
	}
	public void setValor(Double valor) {
		this.valor = valor;
	}
	
	

}
