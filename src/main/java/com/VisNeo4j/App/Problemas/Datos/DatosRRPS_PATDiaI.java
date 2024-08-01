package com.VisNeo4j.App.Problemas.Datos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.VisNeo4j.App.Lectura.LecturaDeDatos;

public class DatosRRPS_PATDiaI {
	
	private List<List<String>> conexiones;
	
	private List<List<String>> conexionesNombres;
	
	private List<List<String>> conexionesTotal;
	
	private List<String> continentes;
	private List<Boolean> capitales;
	
	//Cálculo riesgo
	private List<Double> riesgos;
	private List<Double> riesgosJuntos;
	
	//Cálculo pérdida de pasajeros
	private List<Integer> pasajeros;
	private List<Integer> pasajerosJuntos;
	
	
	//Cálculo dinero
	private List<Double> dineroMedioT;
	private List<Double> dineroMedioN;
	private List<Double> ingresosJuntos;
	
	
	//Cálculo homogeneidad pasajeros aerolineas
	private List<String> companyias;
	
	
	//Calculo pérdida de ingresos por tasas aeropoertuarias
	private List<Double> tasas;
	private List<Double> tasasJuntos;
	
	
	//Cálculo homogeneidad pérdida de ingresos entre aeropuertos destino mediante las tasas
	private List<String> areasInf;
	
	//Conectividad
	
	private Map<List<String>, Integer> vuelosEntrantesConexion;
	private List<Integer> vuelosEntrantesConexionOrdenado = new ArrayList<>();
	private Map<String, Integer> vuelosSalientesAEspanya;
	private Map<String, Integer> vuelosSalientes;
	
	private List<Double> conectividades;
	
	public DatosRRPS_PATDiaI(List<Double> riesgos, List<List<String>> conexiones, List<List<String>> conexionesTotal, 
			List<Integer> pasajeros, List<Double> dineroMedioT, List<Double> dineroMedioN,
			List<String> companyias, List<String> areasInf, List<String> continentes, 
			List<Boolean> capitales, Map<String, Integer> vuelosSalientes,
			Map<List<String>, Integer> vuelosEntrantesConexion, 
			Map<String, Integer> vuelosSalientesAEspanya,  
			List<Double> conectividades, List<Double> tasas, 
			List<Integer> vuelosSalientesDeOrigen, List<List<String>> conexionesNombres){
		
		this.conexiones = conexiones;
		this.conexionesNombres = conexionesNombres;
		this.conexionesTotal = conexionesTotal;
		
		this.continentes = continentes;
		this.capitales = capitales;
		
		this.riesgos = riesgos;
		this.pasajeros = pasajeros;
		this.dineroMedioT = dineroMedioT;
		this.dineroMedioN = dineroMedioN;
		this.tasas = tasas;
		
		this.vuelosEntrantesConexion = vuelosEntrantesConexion;
		this.vuelosSalientesAEspanya = vuelosSalientesAEspanya;
		
		this.vuelosSalientes = vuelosSalientes;
		
		this.conectividades = conectividades;
		
		this.companyias = companyias;
		this.areasInf = areasInf;
		
		this.obtenerDatosConectividad();
		
		
	}
	
	
	
	private void obtenerDatosConectividad() {
		for(int i = 0; i < this.conexiones.size(); i++) {
			this.vuelosEntrantesConexionOrdenado.add(this.vuelosEntrantesConexion.get(this.conexiones.get(i)));
			if (this.vuelosSalientes.get(this.conexiones.get(i).get(0)) != 0) {
				this.conectividades.set(i, this.conectividades.get(i) * 
						this.vuelosSalientesAEspanya.get(this.conexiones.get(i).get(0))
						/ 
						this.vuelosSalientes.get(this.conexiones.get(i).get(0)));
            }
		}
	}
	
	public void calcularDatosJuntos() {
		this.riesgosJuntos = new ArrayList<>();
		this.pasajerosJuntos = new ArrayList<>();
		this.ingresosJuntos = new ArrayList<>();
		this.tasasJuntos = new ArrayList<>();
		
		for(int i = 0; i < this.conexionesTotal.size(); i++) {
			
			if(this.conexiones.indexOf(this.conexionesTotal.get(i)) < this.riesgosJuntos.size()) {
				this.riesgosJuntos.set(this.conexiones.indexOf(this.conexionesTotal.get(i)), 
						this.riesgos.get(i) + 
						this.riesgosJuntos.get(this.conexiones.indexOf(this.conexionesTotal.get(i))));
			}else {
				this.riesgosJuntos.add(this.riesgos.get(i));
			}
			if(this.conexiones.indexOf(this.conexionesTotal.get(i)) < this.pasajerosJuntos.size()) {
				this.pasajerosJuntos.set(this.conexiones.indexOf(this.conexionesTotal.get(i)), 
						this.pasajeros.get(i) + 
						this.pasajerosJuntos.get(this.conexiones.indexOf(this.conexionesTotal.get(i))));
			}else {
				this.pasajerosJuntos.add(this.pasajeros.get(i));
			}
			if(this.conexiones.indexOf(this.conexionesTotal.get(i)) < this.ingresosJuntos.size()) {
				this.ingresosJuntos.set(this.conexiones.indexOf(this.conexionesTotal.get(i)), 
						this.dineroMedioN.get(i) + this.dineroMedioT.get(i) + 
						this.ingresosJuntos.get(this.conexiones.indexOf(this.conexionesTotal.get(i))));
			}else {
				this.ingresosJuntos.add(this.dineroMedioN.get(i) + this.dineroMedioT.get(i));
			}
			if(this.conexiones.indexOf(this.conexionesTotal.get(i)) < this.tasasJuntos.size()) {
				this.tasasJuntos.set(this.conexiones.indexOf(this.conexionesTotal.get(i)), 
						this.tasas.get(i) + 
						this.tasasJuntos.get(this.conexiones.indexOf(this.conexionesTotal.get(i))));
			}else {
				this.tasasJuntos.add(this.tasas.get(i));
			}
		}
	}

	public List<List<String>> getConexiones() {
		return conexiones;
	}

	public void setConexiones(List<List<String>> conexiones) {
		this.conexiones = conexiones;
	}
	
	public List<List<String>> getConexionesNombres() {
		return conexionesNombres;
	}

	public void setConexionesNombres(List<List<String>> conexionesNombres) {
		this.conexionesNombres = conexionesNombres;
	}

	public List<List<String>> getConexionesTotal() {
		return conexionesTotal;
	}

	public void setConexionesTotal(List<List<String>> conexionesTotal) {
		this.conexionesTotal = conexionesTotal;
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

	public List<Double> getDineroMedioT() {
		return dineroMedioT;
	}

	public void setDineroMedioT(List<Double> dineroMedioT) {
		this.dineroMedioT = dineroMedioT;
	}

	public List<Double> getDineroMedioN() {
		return dineroMedioN;
	}

	public void setDineroMedioN(List<Double> dineroMedioN) {
		this.dineroMedioN = dineroMedioN;
	}

	public List<String> getCompanyias() {
		return companyias;
	}

	public void setCompanyias(List<String> companyias) {
		this.companyias = companyias;
	}

	public List<Double> getTasas() {
		return tasas;
	}

	public void setTasas(List<Double> tasas) {
		this.tasas = tasas;
	}

	public Map<List<String>, Integer> getVuelosEntrantesConexion() {
		return vuelosEntrantesConexion;
	}

	public void setVuelosEntrantesConexion(Map<List<String>, Integer> vuelosEntrantesConexion) {
		this.vuelosEntrantesConexion = vuelosEntrantesConexion;
	}

	public Map<String, Integer> getVuelosSalientesAEspanya() {
		return vuelosSalientesAEspanya;
	}

	public void setVuelosSalientesAEspanya(Map<String, Integer> vuelosSalientesAEspanya) {
		this.vuelosSalientesAEspanya = vuelosSalientesAEspanya;
	}

	public Map<String, Integer> getVuelosSalientes() {
		return vuelosSalientes;
	}

	public void setVuelosSalientes(Map<String, Integer> vuelosSalientes) {
		this.vuelosSalientes = vuelosSalientes;
	}

	public List<Integer> getVuelosEntrantesConexionOrdenado() {
		return vuelosEntrantesConexionOrdenado;
	}

	public void setVuelosEntrantesConexionOrdenado(List<Integer> vuelosEntrantesConexionOrdenado) {
		this.vuelosEntrantesConexionOrdenado = vuelosEntrantesConexionOrdenado;
	}

	public List<Double> getConectividades() {
		return conectividades;
	}

	public void setConectividades(List<Double> conectividades) {
		this.conectividades = conectividades;
	}

	public List<String> getAreasInf() {
		return areasInf;
	}

	public void setAreasInf(List<String> areasInf) {
		this.areasInf = areasInf;
	}

	public List<String> getContinentes() {
		return continentes;
	}

	public void setContinentes(List<String> continentes) {
		this.continentes = continentes;
	}

	public List<Boolean> getCapitales() {
		return capitales;
	}

	public void setCapitales(List<Boolean> capitales) {
		this.capitales = capitales;
	}

	public List<Double> getRiesgosJuntos() {
		return riesgosJuntos;
	}

	public void setRiesgosJuntos(List<Double> riesgosJuntos) {
		this.riesgosJuntos = riesgosJuntos;
	}

	public List<Integer> getPasajerosJuntos() {
		return pasajerosJuntos;
	}

	public void setPasajerosJuntos(List<Integer> pasajerosJuntos) {
		this.pasajerosJuntos = pasajerosJuntos;
	}

	public List<Double> getIngresosJuntos() {
		return ingresosJuntos;
	}

	public void setIngresosJuntos(List<Double> ingresosJuntos) {
		this.ingresosJuntos = ingresosJuntos;
	}

	public List<Double> getTasasJuntos() {
		return tasasJuntos;
	}

	public void setTasasJuntos(List<Double> tasasJuntos) {
		this.tasasJuntos = tasasJuntos;
	}
}
