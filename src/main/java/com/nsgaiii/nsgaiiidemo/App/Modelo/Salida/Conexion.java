package com.nsgaiii.nsgaiiidemo.App.Modelo.Salida;

public class Conexion {
	
	private Double longitudeO;
	private Double latitudeO;
	private Double longitudeD;
	private Double latitudeD;
	private boolean abierto_cerrado;
	
	public Conexion(Double longitudeO, Double latitudeO, Double longitudeD, Double latitudeD, boolean abierto_cerrado) {
		this.longitudeO = longitudeO;
		this.latitudeO = latitudeO;
		this.longitudeD = longitudeD;
		this.latitudeD = latitudeD;
		this.abierto_cerrado = abierto_cerrado;
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
	
	
}
