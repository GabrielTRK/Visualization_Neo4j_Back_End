package com.VisNeo4j.App.Modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DatosRRPS_PAT {
	
	private int numDias;
	private String fechaInicio;
	private String fechaFinal;
	private int numPasajerosTotales = 0;
	private int numVuelosTotales = 0;
	
	private List<DatosRRPS_PATDiaI> datosPorDia;
	private List<List<String>> conexionesTotales;
	private List<List<String>> conexionesTotalesSeparadas;
	
	private List<Double> riesgos;
	private List<Integer> pasajeros;
	private List<Double> ingresos;
	private List<Double> tasas;
	
	private List<Integer> vuelosEntrantesConexionOrdenadoTotales;
	private List<Double> conectividadesTotales;
	
	private List<String> companyiasTotales;
	
	private List<String> aresInfTotales;

	public DatosRRPS_PAT(int numDias, String fechaInicio, String fechaFinal, List<DatosRRPS_PATDiaI> datosPorDia) {
		this.numDias = numDias;
		this.fechaInicio = fechaInicio;
		this.fechaFinal = fechaFinal;
		this.datosPorDia = datosPorDia;
		this.conexionesTotales = new ArrayList<>();
		for(int i = 0; i < this.datosPorDia.size(); i++) {
			conexionesTotales.addAll(this.datosPorDia.get(i).getConexiones());
		}
		this.conexionesTotalesSeparadas = new ArrayList<>();
		for(int i = 0; i < this.datosPorDia.size(); i++) {
			conexionesTotalesSeparadas.addAll(this.datosPorDia.get(i).getConexionesTotal());
		}
		for(int i = 0; i < this.datosPorDia.size(); i++) {
			for(int j = 0; j < this.datosPorDia.get(i).getPasajeros().size(); j++) {
				numPasajerosTotales += this.datosPorDia.get(i).getPasajeros().get(j);
			}
		}
		/*for(int i = 0; i < this.datosPorDia.size(); i++) {
			for(int j = 0; j < this.datosPorDia.get(i).getConexiones().size(); j++) {
				numVuelosTotales += this.datosPorDia.get(i).getVuelosEntrantesConexion().get(this.datosPorDia.get(i).getConexiones().get(i));
			}
		}*/
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
	}
	
	public Individuo rellenarConexionesFaltantes(Individuo ind) {
		List<Double> aux = ind.getVariables();
		for(int i = 0; i < this.datosPorDia.size(); i++) {
			this.rellenarConexionesFaltantesDia(i, aux);
		}
		ind.setVariables(aux);
		return ind;
	}
	
	private void rellenarConexionesFaltantesDia(int dia, List<Double> aux) {
		int offset = 0;
		for(int i = 0; i < dia; i++) {
			offset += this.datosPorDia.get(i).getConexiones().size();
		}
		for(int i = 0; i < this.datosPorDia.get(dia).getDireccionesAMantener().size(); i++) {
			aux.add(offset + this.datosPorDia.get(dia).getDireccionesAMantener().get(i), 1.0);
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

	public int getNumPasajerosTotales() {
		return numPasajerosTotales;
	}

	public void setNumPasajerosTotales(int numPasajerosTotales) {
		this.numPasajerosTotales = numPasajerosTotales;
	}

	public int getNumVuelosTotales() {
		return numVuelosTotales;
	}

	public void setNumVuelosTotales(int numVuelosTotales) {
		this.numVuelosTotales = numVuelosTotales;
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
	
	
	
	
}
