package com.nsgaiii.nsgaiiidemo.App.Modelo.Salida;

public class Aeropuerto {
	
	private Double longitud;
	private Double latitud;
	private String iata;
	private boolean origen;
	
	public Aeropuerto(Double longitud, Double latitud, String iata, boolean origen) {
		super();
		this.longitud = longitud;
		this.latitud = latitud;
		this.iata = iata;
		this.origen = origen;
	}
	public Double getLongitud() {
		return longitud;
	}
	public void setLongitud(Double longitud) {
		this.longitud = longitud;
	}
	public Double getLatitud() {
		return latitud;
	}
	public void setLatitud(Double latitud) {
		this.latitud = latitud;
	}
	public String getIata() {
		return iata;
	}
	public void setIata(String iata) {
		this.iata = iata;
	}
	public boolean isOrigen() {
		return origen;
	}
	public void setOrigen(boolean origen) {
		this.origen = origen;
	}
	@Override
	public String toString() {
		return "Aeropuerto [longitud=" + longitud + ", latitud=" + latitud + ", iata=" + iata + ", origen=" + origen
				+ "]";
	}
	
	

}
