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
	private List<Double> riesgos_KP;
	
	//Cálculo pérdida de pasajeros
	private List<Integer> pasajeros;
	private List<Integer> pasajerosJuntos;
	private List<Integer> pasajeros_KP;
	
	
	//Cálculo dinero
	private List<Double> dineroMedioT;
	private List<Double> dineroMedioT_KP;
	private List<Double> dineroMedioN;
	private List<Double> dineroMedioN_KP;
	private List<Double> ingresosJuntos;
	
	
	//Cálculo homogeneidad pasajeros aerolineas
	private List<String> companyias;
	
	
	//Calculo pérdida de ingresos por tasas aeropoertuarias
	private List<Double> tasas;
	private List<Double> tasasJuntos;
	private List<Double> tasas_KP;
	
	//Cálculo homogeneidad pérdida de ingresos entre aeropuertos destino mediante las tasas
	private List<String> areasInf;
	private List<String> areasInf_KP;
	private List<Double> ingresosAreaInfTotal = new ArrayList<>();
	
	//Conectividad
	
	private Map<List<String>, Integer> vuelosEntrantesConexion;
	private List<Integer> vuelosEntrantesConexionOrdenado = new ArrayList<>();
	private List<Integer> vuelosEntrantesConexionOrdenadoTotal = new ArrayList<>();
	private Map<String, Integer> vuelosSalientesAEspanya;
	private Map<String, Integer> vuelosSalientes;
	
	private List<Double> conectividades;
	
	private List<String> aeropuertosOrigen;
	
	public DatosRRPS_PATDiaI(List<Double> riesgos, List<Double> riesgos_KP, 
			List<List<String>> conexiones, List<List<String>> conexionesTotal, 
			List<Integer> pasajeros, List<Integer> pasajeros_KP, List<Double> dineroMedioT, 
			List<Double> dineroMedioT_KP, List<Double> dineroMedioN, List<Double> dineroMedioN_KP, 
			List<String> companyias, List<String> areasInf, List<String> areasInf_KP,
			List<String> continentes, List<Boolean> capitales, 
			Map<String, Integer> vuelosSalientes, 
			Map<List<String>, Integer> vuelosEntrantesConexion, 
			Map<String, Integer> vuelosSalientesAEspanya,  
			List<Double> conectividades, List<Double> tasas, List<Double> tasas_KP,
			List<Integer> vuelosSalientesDeOrigen, List<List<String>> conexionesNombres, 
			List<String> aeropuertosOrigen){
		
		this.conexiones = conexiones;
		this.conexionesNombres = conexionesNombres;
		this.conexionesTotal = conexionesTotal;
		
		this.continentes = continentes;
		this.capitales = capitales;
		
		this.riesgos = riesgos;
		this.riesgos_KP = riesgos_KP;
		this.pasajeros = pasajeros;
		this.pasajeros_KP = pasajeros_KP;
		this.dineroMedioT = dineroMedioT;
		this.dineroMedioT_KP = dineroMedioT_KP;
		this.dineroMedioN = dineroMedioN;
		this.dineroMedioN_KP = dineroMedioN_KP;
		this.tasas = tasas;
		this.tasas_KP = tasas_KP;
		
		this.vuelosEntrantesConexion = vuelosEntrantesConexion;
		this.vuelosSalientesAEspanya = vuelosSalientesAEspanya;
		
		this.vuelosSalientes = vuelosSalientes;
		
		this.conectividades = conectividades;
		
		this.companyias = companyias;
		this.areasInf = areasInf;
		
		this.aeropuertosOrigen = aeropuertosOrigen;
		
		this.obtenerDatosConectividad();
		this.obtenerSumaTotalVuelosEntrantes();
		this.obtenerIngresosTotalesPorAreaInf();
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
	
	private void obtenerSumaTotalVuelosEntrantes() {
		for(int i = 0; i < this.aeropuertosOrigen.size(); i++) {
			String origen = this.aeropuertosOrigen.get(i);
			int sumaVuelosTotal = 0;
			for(int j = 0; j < this.conexiones.size(); j++) {
				if(this.conexiones.get(j).get(0).equals(origen)) {
					sumaVuelosTotal += this.vuelosEntrantesConexionOrdenado.get(j);
				}
			}
			this.vuelosEntrantesConexionOrdenadoTotal.add(sumaVuelosTotal);
		}
	}
	
	private void obtenerIngresosTotalesPorAreaInf() {
		for(int i = 0; i < this.areasInf_KP.size(); i++) {
			double ingreso = 0.0;
			for(int j = 0; j < this.areasInf.size(); j++) {
				if(this.areasInf_KP.get(i).equals(this.areasInf.get(j))) {
					ingreso += this.dineroMedioN.get(j) + this.dineroMedioT.get(j);
				}
			}
			this.ingresosAreaInfTotal.add(ingreso);
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



	public List<Double> getRiesgos_KP() {
		return riesgos_KP;
	}



	public void setRiesgos_KP(List<Double> riesgos_KP) {
		this.riesgos_KP = riesgos_KP;
	}



	public List<Integer> getPasajeros_KP() {
		return pasajeros_KP;
	}



	public void setPasajeros_KP(List<Integer> pasajeros_KP) {
		this.pasajeros_KP = pasajeros_KP;
	}



	public List<Double> getDineroMedioT_KP() {
		return dineroMedioT_KP;
	}



	public void setDineroMedioT_KP(List<Double> dineroMedioT_KP) {
		this.dineroMedioT_KP = dineroMedioT_KP;
	}



	public List<Double> getDineroMedioN_KP() {
		return dineroMedioN_KP;
	}



	public void setDineroMedioN_KP(List<Double> dineroMedioN_KP) {
		this.dineroMedioN_KP = dineroMedioN_KP;
	}



	public List<Double> getTasas_KP() {
		return tasas_KP;
	}



	public void setTasas_KP(List<Double> tasas_KP) {
		this.tasas_KP = tasas_KP;
	}



	public List<Integer> getVuelosEntrantesConexionOrdenadoTotal() {
		return vuelosEntrantesConexionOrdenadoTotal;
	}



	public void setVuelosEntrantesConexionOrdenadoTotal(List<Integer> vuelosEntrantesConexionOrdenadoTotal) {
		this.vuelosEntrantesConexionOrdenadoTotal = vuelosEntrantesConexionOrdenadoTotal;
	}



	public List<String> getAreasInf_KP() {
		return areasInf_KP;
	}



	public void setAreasInf_KP(List<String> areasInf_KP) {
		this.areasInf_KP = areasInf_KP;
	}



	public List<Double> getIngresosAreaInfTotal() {
		return ingresosAreaInfTotal;
	}



	public void setIngresosAreaInfTotal(List<Double> ingresosAreaInfTotal) {
		this.ingresosAreaInfTotal = ingresosAreaInfTotal;
	}
	
	
}
