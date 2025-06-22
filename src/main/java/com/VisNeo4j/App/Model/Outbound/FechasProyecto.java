package com.VisNeo4j.App.Model.Outbound;

public class FechasProyecto {
	
	private String fechaI;
	private String fechaF;
	
	public FechasProyecto(String fechaI, String fechaF) {
		super();
		this.fechaI = fechaI;
		this.fechaF = fechaF;
	}
	
	public String getFechaI() {
		return fechaI;
	}
	public void setFechaI(String fechaI) {
		this.fechaI = fechaI;
	}
	public String getFechaF() {
		return fechaF;
	}
	public void setFechaF(String fechaF) {
		this.fechaF = fechaF;
	}
	
	

}
