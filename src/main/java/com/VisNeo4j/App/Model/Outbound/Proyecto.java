package com.VisNeo4j.App.Model.Outbound;

import com.VisNeo4j.App.Algoritmo.Parametros.BPSOParams;
import com.VisNeo4j.App.QDMP.ObjectivesOrder;

public class Proyecto {
	
	private String nombre;
	private boolean ejecutando;
	private FechasProyecto fechas;
	private BPSOParamsSalida params;
	private OrdenObjSalida order;
	private Restricciones res;
	
	public Proyecto(String nombre, boolean ejecutando, FechasProyecto fechas, BPSOParamsSalida params, OrdenObjSalida order, Restricciones res) {
		super();
		this.nombre = nombre;
		this.ejecutando = ejecutando;
		this.fechas = fechas;
		this.params = params;
		this.order = order;
		this.res = res;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public boolean isEjecutando() {
		return ejecutando;
	}
	public void setEjecutando(boolean ejecutando) {
		this.ejecutando = ejecutando;
	}
	public FechasProyecto getFechas() {
		return fechas;
	}
	public void setFechas(FechasProyecto fechas) {
		this.fechas = fechas;
	}
	public BPSOParamsSalida getParams() {
		return params;
	}
	public void setParams(BPSOParamsSalida params) {
		this.params = params;
	}
	public OrdenObjSalida getOrder() {
		return order;
	}
	public void setOrder(OrdenObjSalida order) {
		this.order = order;
	}
	public Restricciones getRes() {
		return res;
	}
	public void setRes(Restricciones res) {
		this.res = res;
	}
	
	

}
