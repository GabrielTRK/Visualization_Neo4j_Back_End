package com.VisNeo4j.App.Model.Outbound;

public class Respuesta {
	
	private boolean OK_KO;
	private String mensaje;
	public Respuesta(boolean oK_KO, String mensaje) {
		super();
		OK_KO = oK_KO;
		this.mensaje = mensaje;
	}
	public boolean isOK_KO() {
		return OK_KO;
	}
	public void setOK_KO(boolean oK_KO) {
		OK_KO = oK_KO;
	}
	public String getMensaje() {
		return mensaje;
	}
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	
	

}
