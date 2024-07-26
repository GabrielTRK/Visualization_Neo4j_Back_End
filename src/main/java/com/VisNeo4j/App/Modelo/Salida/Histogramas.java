package com.VisNeo4j.App.Modelo.Salida;

import java.util.List;

public class Histogramas {
	
	public List<Double> listaPasajerosPerdidosPorCompanyia; 
	public List<Double> listaIngresoPerdidoPorAreaInf; 
	public List<Double> listaIngresoPerdidoPorAerDest;
	public Histogramas(List<Double> listaPasajerosPerdidosPorCompanyia, List<Double> listaIngresoPerdidoPorAreaInf,
			List<Double> listaIngresoPerdidoPorAerDest) {
		super();
		this.listaPasajerosPerdidosPorCompanyia = listaPasajerosPerdidosPorCompanyia;
		this.listaIngresoPerdidoPorAreaInf = listaIngresoPerdidoPorAreaInf;
		this.listaIngresoPerdidoPorAerDest = listaIngresoPerdidoPorAerDest;
	}
	public List<Double> getListaPasajerosPerdidosPorCompanyia() {
		return listaPasajerosPerdidosPorCompanyia;
	}
	public void setListaPasajerosPerdidosPorCompanyia(List<Double> listaPasajerosPerdidosPorCompanyia) {
		this.listaPasajerosPerdidosPorCompanyia = listaPasajerosPerdidosPorCompanyia;
	}
	public List<Double> getListaIngresoPerdidoPorAreaInf() {
		return listaIngresoPerdidoPorAreaInf;
	}
	public void setListaIngresoPerdidoPorAreaInf(List<Double> listaIngresoPerdidoPorAreaInf) {
		this.listaIngresoPerdidoPorAreaInf = listaIngresoPerdidoPorAreaInf;
	}
	public List<Double> getListaIngresoPerdidoPorAerDest() {
		return listaIngresoPerdidoPorAerDest;
	}
	public void setListaIngresoPerdidoPorAerDest(List<Double> listaIngresoPerdidoPorAerDest) {
		this.listaIngresoPerdidoPorAerDest = listaIngresoPerdidoPorAerDest;
	}
	
	

}
