package com.VisNeo4j.App.Model.Outbound;

public class Vuelos {
	
	String nombreVuelos;
	int numVuelos;
	
	public Vuelos(String nombreVuelos, int numVuelos) {
		super();
		this.nombreVuelos = nombreVuelos;
		this.numVuelos = numVuelos;
	}
	public String getNombreVuelos() {
		return nombreVuelos;
	}
	public void setNombreVuelos(String nombreVuelos) {
		this.nombreVuelos = nombreVuelos;
	}
	public int getNumVuelos() {
		return numVuelos;
	}
	public void setNumVuelos(int numVuelos) {
		this.numVuelos = numVuelos;
	}
	
	

}
