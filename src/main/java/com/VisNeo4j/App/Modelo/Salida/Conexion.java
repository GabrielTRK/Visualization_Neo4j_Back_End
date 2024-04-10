package com.VisNeo4j.App.Modelo.Salida;

public class Conexion {
	
	private Double longitudeO;
	private Double latitudeO;
	private Double longitudeD;
	private Double latitudeD;
	private boolean abierto_cerrado;
	private String iataOrigen;
	private String iataDestino;
	private Double riesgo;
	private Integer pasajeros;
	private Double ingresos;
	private Double tasas;
	
	public Conexion(Double longitudeO, Double latitudeO, Double longitudeD, Double latitudeD, 
			boolean abierto_cerrado, String iataOrigen, String iataDestino, Double riesgo, 
			Integer pasajeros, Double ingresos, Double tasas) {
		this.longitudeO = longitudeO;
		this.latitudeO = latitudeO;
		this.longitudeD = longitudeD;
		this.latitudeD = latitudeD;
		this.abierto_cerrado = abierto_cerrado;
		this.iataOrigen = iataOrigen;
		this.iataDestino = iataDestino;
		this.riesgo = riesgo;
		this.pasajeros = pasajeros;
		this.ingresos = ingresos;
		this.tasas = tasas;
	}

	public Double getLongitudeO() {
		return longitudeO;
	}

	public void setLongitudeO(Double longitudeO) {
		this.longitudeO = longitudeO;
	}

	public Double getLatitudeO() {
		return latitudeO;
	}

	public void setLatitudeO(Double latitudeO) {
		this.latitudeO = latitudeO;
	}

	public Double getLongitudeD() {
		return longitudeD;
	}

	public void setLongitudeD(Double longitudeD) {
		this.longitudeD = longitudeD;
	}

	public Double getLatitudeD() {
		return latitudeD;
	}

	public void setLatitudeD(Double latitudeD) {
		this.latitudeD = latitudeD;
	}

	public boolean isAbierto_cerrado() {
		return abierto_cerrado;
	}

	public void setAbierto_cerrado(boolean abierto_cerrado) {
		this.abierto_cerrado = abierto_cerrado;
	}

	public String getIataOrigen() {
		return iataOrigen;
	}

	public void setIataOrigen(String iataOrigen) {
		this.iataOrigen = iataOrigen;
	}

	public String getIataDestino() {
		return iataDestino;
	}

	public void setIataDestino(String iataDestino) {
		this.iataDestino = iataDestino;
	}

	public Double getRiesgo() {
		return riesgo;
	}

	public void setRiesgo(Double riesgo) {
		this.riesgo = riesgo;
	}

	public Integer getPasajeros() {
		return pasajeros;
	}

	public void setPasajeros(Integer pasajeros) {
		this.pasajeros = pasajeros;
	}

	public Double getIngresos() {
		return ingresos;
	}

	public void setIngresos(Double ingresos) {
		this.ingresos = ingresos;
	}

	public Double getTasas() {
		return tasas;
	}

	public void setTasas(Double tasas) {
		this.tasas = tasas;
	}
	
	
}
