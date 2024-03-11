package com.VisNeo4j.App.Problemas.Datos;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.VisNeo4j.App.Modelo.Individuo;

public class DatosProblemaDias {
	private int numDias;
	private String fechaInicio;
	private String fechaFinal;
	private int numPasajerosTotales = 0;
	private int numVuelosTotales = 0;
	
	private List<DatosProblema> datosPorDia;
	private List<List<String>> conexionesTotales;
	

	public DatosProblemaDias(int numDias, String fechaInicio, String fechaFinal, List<DatosProblema> datosPorDia) {
		super();
		this.numDias = numDias;
		this.fechaInicio = fechaInicio;
		this.fechaFinal = fechaFinal;
		this.datosPorDia = datosPorDia;
		this.conexionesTotales = new ArrayList<>();
		for(int i = 0; i < this.datosPorDia.size(); i++) {
			conexionesTotales.addAll(this.datosPorDia.get(i).getConexiones());
		}
		for(int i = 0; i < this.datosPorDia.size(); i++) {
			for(int j = 0; j < this.datosPorDia.get(i).getPasajeros().size(); j++) {
				numPasajerosTotales += this.datosPorDia.get(i).getPasajeros().get(j);
			}
		}
		for(int i = 0; i < this.datosPorDia.size(); i++) {
			for(int j = 0; j < this.datosPorDia.get(i).getConexiones().size(); j++) {
				numVuelosTotales += this.datosPorDia.get(i).getVuelosEntrantesConexion().get(this.datosPorDia.get(i).getConexiones().get(i));
			}
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

	public List<DatosProblema> getDatosPorDia() {
		return datosPorDia;
	}

	public void setDatosPorDia(List<DatosProblema> datosPorDia) {
		this.datosPorDia = datosPorDia;
	}
	
	public List<List<String>> getConexionesTotales() {
		return conexionesTotales;
	}

	public void setConexionesTotales(List<List<String>> conexionesTotales) {
		this.conexionesTotales = conexionesTotales;
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
	
	
}
