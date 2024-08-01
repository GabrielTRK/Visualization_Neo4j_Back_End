package com.VisNeo4j.App.Problemas.Datos;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.VisNeo4j.App.Modelo.Individuo;

public class DatosRRPS_PAT {
	
	private int numDias;
	private String fechaInicio;
	private String fechaFinal;
	
	private List<DatosRRPS_PATDiaI> datosPorDia;
	private List<List<String>> conexionesTotales;
	private List<List<String>> conexionesNombresTotales;
	private List<List<String>> conexionesTotalesSeparadas;
	
	private List<Double> riesgos;
	private List<Integer> pasajeros;
	private List<Double> ingresos;
	private List<Double> tasas;
	
	private List<Integer> vuelosEntrantesConexionOrdenadoTotales;
	private List<Double> conectividadesTotales;
	
	private List<String> companyiasTotales;
	
	private List<String> aresInfTotales;
	private List<String> continentesTotales;
	private List<Boolean> capitalesTotales;

	public DatosRRPS_PAT(int numDias, String fechaInicio, String fechaFinal, List<DatosRRPS_PATDiaI> datosPorDia) {
		this.numDias = numDias;
		this.fechaInicio = fechaInicio;
		this.fechaFinal = fechaFinal;
		this.datosPorDia = datosPorDia;
		this.conexionesTotales = new ArrayList<>();
		for(int i = 0; i < this.datosPorDia.size(); i++) {
			conexionesTotales.addAll(this.datosPorDia.get(i).getConexiones());
		}
		this.conexionesNombresTotales = new ArrayList<>();
		for(int i = 0; i < this.datosPorDia.size(); i++) {
			conexionesNombresTotales.addAll(this.datosPorDia.get(i).getConexionesNombres());
		}
		this.conexionesTotalesSeparadas = new ArrayList<>();
		for(int i = 0; i < this.datosPorDia.size(); i++) {
			conexionesTotalesSeparadas.addAll(this.datosPorDia.get(i).getConexionesTotal());
		}
		this.riesgos = new ArrayList<>();
		for(int i = 0; i < this.datosPorDia.size(); i++) {
			riesgos.addAll(this.datosPorDia.get(i).getRiesgos());
		}
		
		this.pasajeros = new ArrayList<>();
		for(int i = 0; i < this.datosPorDia.size(); i++) {
			pasajeros.addAll(this.datosPorDia.get(i).getPasajeros());
		}
		
		this.ingresos = new ArrayList<>();
		List<Double> ingresosN = new ArrayList<>();
		for(int i = 0; i < this.datosPorDia.size(); i++) {
			ingresosN.addAll(this.datosPorDia.get(i).getDineroMedioN());
			ingresos.addAll(this.datosPorDia.get(i).getDineroMedioT());
		}
		for(int i = 0; i < this.ingresos.size(); i++) {
			this.ingresos.set(i, this.ingresos.get(i) + ingresosN.get(i));
		}
		
		this.tasas = new ArrayList<>();
		for(int i = 0; i < this.datosPorDia.size(); i++) {
			tasas.addAll(this.datosPorDia.get(i).getTasas());
		}
		
		this.vuelosEntrantesConexionOrdenadoTotales = new ArrayList<>();
		for(int i = 0; i < this.datosPorDia.size(); i++) {
			this.vuelosEntrantesConexionOrdenadoTotales.addAll(this.datosPorDia.get(i).getVuelosEntrantesConexionOrdenado());
		}
		
		this.conectividadesTotales = new ArrayList<>();
		for(int i = 0; i < this.datosPorDia.size(); i++) {
			this.conectividadesTotales.addAll(this.datosPorDia.get(i).getConectividades());
		}
		
		this.companyiasTotales = new ArrayList<>();
		for(int i = 0; i < this.datosPorDia.size(); i++) {
			this.companyiasTotales.addAll(this.datosPorDia.get(i).getCompanyias());
		}
		
		this.aresInfTotales = new ArrayList<>();
		for(int i = 0; i < this.datosPorDia.size(); i++) {
			this.aresInfTotales.addAll(this.datosPorDia.get(i).getAreasInf());
		}
		this.continentesTotales = new ArrayList<>();
		for(int i = 0; i < this.datosPorDia.size(); i++) {
			this.continentesTotales.addAll(this.datosPorDia.get(i).getContinentes());
		}
		
		this.capitalesTotales = new ArrayList<>();
		for(int i = 0; i < this.datosPorDia.size(); i++) {
			this.capitalesTotales.addAll(this.datosPorDia.get(i).getCapitales());
		}
	}

	public int getNumDias() {
		return numDias;
	}

	public void setNumDias(int numDias) {
		this.numDias = numDias;
	}

	public String getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(String fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public String getFechaFinal() {
		return fechaFinal;
	}

	public void setFechaFinal(String fechaFinal) {
		this.fechaFinal = fechaFinal;
	}

	public List<DatosRRPS_PATDiaI> getDatosPorDia() {
		return datosPorDia;
	}

	public void setDatosPorDia(List<DatosRRPS_PATDiaI> datosPorDia) {
		this.datosPorDia = datosPorDia;
	}

	public List<List<String>> getConexionesTotales() {
		return conexionesTotales;
	}

	public void setConexionesTotales(List<List<String>> conexionesTotales) {
		this.conexionesTotales = conexionesTotales;
	}

	public List<List<String>> getConexionesNombresTotales() {
		return conexionesNombresTotales;
	}

	public void setConexionesNombresTotales(List<List<String>> conexionesNombresTotales) {
		this.conexionesNombresTotales = conexionesNombresTotales;
	}

	public List<Double> getRiesgos() {
		return riesgos;
	}

	public void setRiesgos(List<Double> riesgos) {
		this.riesgos = riesgos;
	}

	public List<Integer> getPasajeros() {
		return pasajeros;
	}

	public void setPasajeros(List<Integer> pasajeros) {
		this.pasajeros = pasajeros;
	}

	public List<Double> getIngresos() {
		return ingresos;
	}

	public void setIngresos(List<Double> ingresos) {
		this.ingresos = ingresos;
	}

	public List<Double> getTasas() {
		return tasas;
	}

	public void setTasas(List<Double> tasas) {
		this.tasas = tasas;
	}

	public List<List<String>> getConexionesTotalesSeparadas() {
		return conexionesTotalesSeparadas;
	}

	public void setConexionesTotalesSeparadas(List<List<String>> conexionesTotalesSeparadas) {
		this.conexionesTotalesSeparadas = conexionesTotalesSeparadas;
	}

	public List<Integer> getVuelosEntrantesConexionOrdenadoTotales() {
		return vuelosEntrantesConexionOrdenadoTotales;
	}

	public void setVuelosEntrantesConexionOrdenadoTotales(List<Integer> vuelosEntrantesConexionOrdenadoTotales) {
		this.vuelosEntrantesConexionOrdenadoTotales = vuelosEntrantesConexionOrdenadoTotales;
	}

	public List<Double> getConectividadesTotales() {
		return conectividadesTotales;
	}

	public void setConectividadesTotales(List<Double> conectividadesTotales) {
		this.conectividadesTotales = conectividadesTotales;
	}

	public List<String> getCompanyiasTotales() {
		return companyiasTotales;
	}

	public void setCompanyiasTotales(List<String> companyiasTotales) {
		this.companyiasTotales = companyiasTotales;
	}

	public List<String> getAresInfTotales() {
		return aresInfTotales;
	}

	public void setAresInfTotales(List<String> aresInfTotales) {
		this.aresInfTotales = aresInfTotales;
	}

	public List<String> getContinentesTotales() {
		return continentesTotales;
	}

	public void setContinentesTotales(List<String> continentesTotales) {
		this.continentesTotales = continentesTotales;
	}

	public List<Boolean> getCapitalesTotales() {
		return capitalesTotales;
	}

	public void setCapitalesTotales(List<Boolean> capitalesTotales) {
		this.capitalesTotales = capitalesTotales;
	}
	
	
	
	
}
