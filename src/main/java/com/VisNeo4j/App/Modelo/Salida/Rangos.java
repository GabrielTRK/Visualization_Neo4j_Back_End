package com.VisNeo4j.App.Modelo.Salida;

import java.util.List;

public class Rangos {
	public List<String> listaPasajerosPerdidosPorCompanyia; 
	public List<String> listaIngresoPerdidoPorAreaInf; 
	public List<String> listaIngresoPerdidoPorAerDest;
	public Rangos(List<String> listaPasajerosPerdidosPorCompanyia, List<String> listaIngresoPerdidoPorAreaInf,
			List<String> listaIngresoPerdidoPorAerDest) {
		super();
		this.listaPasajerosPerdidosPorCompanyia = listaPasajerosPerdidosPorCompanyia;
		this.listaIngresoPerdidoPorAreaInf = listaIngresoPerdidoPorAreaInf;
		this.listaIngresoPerdidoPorAerDest = listaIngresoPerdidoPorAerDest;
	}
	public List<String> getListaPasajerosPerdidosPorCompanyia() {
		return listaPasajerosPerdidosPorCompanyia;
	}
	public void setListaPasajerosPerdidosPorCompanyia(List<String> listaPasajerosPerdidosPorCompanyia) {
		this.listaPasajerosPerdidosPorCompanyia = listaPasajerosPerdidosPorCompanyia;
	}
	public List<String> getListaIngresoPerdidoPorAreaInf() {
		return listaIngresoPerdidoPorAreaInf;
	}
	public void setListaIngresoPerdidoPorAreaInf(List<String> listaIngresoPerdidoPorAreaInf) {
		this.listaIngresoPerdidoPorAreaInf = listaIngresoPerdidoPorAreaInf;
	}
	public List<String> getListaIngresoPerdidoPorAerDest() {
		return listaIngresoPerdidoPorAerDest;
	}
	public void setListaIngresoPerdidoPorAerDest(List<String> listaIngresoPerdidoPorAerDest) {
		this.listaIngresoPerdidoPorAerDest = listaIngresoPerdidoPorAerDest;
	}
	
	

}
