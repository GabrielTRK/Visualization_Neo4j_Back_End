package com.VisNeo4j.App.Modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.VisNeo4j.App.Lectura.LecturaDeDatos;

public class DatosRRPS_PATDiaI {
	
	private List<List<String>> conexiones;
	private List<List<String>> conexionesAMantener = new ArrayList<>();
	private List<Integer> direccionesAMantener = new ArrayList<>();
	private List<List<String>> conexionesTotal;
	
	//Cálculo riesgo
	private List<Double> riesgos;
	
	//Cálculo pérdida de pasajeros
	private List<Integer> pasajeros;
	
	
	//Cálculo dinero
	private List<Double> dineroMedioT;
	private List<Double> dineroMedioN;
	
	//Cálculo pérdida de ingresos por turismo
	
	//Cálculo homogeneidad pasajeros aerolineas
	private List<String> companyias;
	private Map<List<String>, Integer> pasajerosCompanyia;
	
	
	//Cálculo homogeneidad ingresos turismo Aeropuertos
	private Map<String, Double> ingresosTAeropuertoTotal = new HashMap<>();
	
	//Calculo pérdida de ingresos por tasas aeropoertuarias
	private List<Double> tasas;
	private List<String> AeropuertosDestino;
	
	//Cálculo homogeneidad pérdida de ingresos entre aeropuertos destino mediante las tasas
	private Map<String, Double> tasasPorAeropuertoDestino;
	
	//Conectividad
	private List<String> AeropuertosOrigen;
	private Map<List<String>, Integer> vuelosEntrantesConexion;
	private List<Integer> vuelosEntrantesConexionOrdenado = new ArrayList<>();
	private Map<String, Integer> vuelosSalientesAEspanya;
	private Map<String, Integer> vuelosSalientes;
	private Map<String, Double> conectividadesAeropuertosOrigen;
	private Map<String, Set<String>> listaConexionesSalidas;
	
	private List<Double> conectividades;
	
	public DatosRRPS_PATDiaI(List<Double> riesgos, List<List<String>> conexiones, List<List<String>> conexionesTotal, 
			List<Integer> pasajeros, List<Double> dineroMedioT, List<Double> dineroMedioN,
			List<String> companyias, List<String> companyiasTotal, Map<List<String>, Integer> pasajerosCompanyia,
			List<String> AeropuertosOrigen, List<String> aeropuertosOrigenTotal, List<String> AeropuertosDestino, 
			List<String> aeropuertosDestinoTotal, Map<String, Integer> vuelosSalientes,
			Map<List<String>, Integer> vuelosEntrantesConexion, Map<String, Integer> vuelosSalientesAEspanya, 
			Map<String, Double> conectividadesAeropuertosOrigen, List<Double> conectividades, List<Double> tasas,
			Map<String, Double> tasasPorAeropuertoDestino, List<Integer> vuelosSalientesDeOrigen){
		
		this.conexiones = conexiones;
		this.conexionesTotal = conexionesTotal;
		
		this.riesgos = riesgos;
		this.pasajeros = pasajeros;
		this.dineroMedioT = dineroMedioT;
		this.dineroMedioN = dineroMedioN;
		this.tasas = tasas;
		
		this.vuelosEntrantesConexion = vuelosEntrantesConexion;
		this.vuelosSalientesAEspanya = vuelosSalientesAEspanya;
		this.conectividadesAeropuertosOrigen = conectividadesAeropuertosOrigen;
		this.vuelosSalientes = vuelosSalientes;
		this.AeropuertosOrigen = AeropuertosOrigen;
		this.conectividades = conectividades;
		
		this.obtenerDatosConectividad();
		
		LecturaDeDatos.leerConexionesAMantener(this.conexionesAMantener);
		this.calcularDireccionesAMantener();
	}
	
	private void calcularDireccionesAMantener() {
		for(int i = 0; i < this.conexionesAMantener.size(); i++) {
			this.direccionesAMantener.add(this.conexiones.indexOf(conexionesAMantener.get(i)));
			if(this.direccionesAMantener.get(this.direccionesAMantener.size()-1) == -1) {
				this.direccionesAMantener.remove(this.direccionesAMantener.size()-1);
			}
			
		}
		Collections.sort(this.direccionesAMantener);
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

	public List<List<String>> getConexiones() {
		return conexiones;
	}

	public void setConexiones(List<List<String>> conexiones) {
		this.conexiones = conexiones;
	}

	public List<List<String>> getConexionesAMantener() {
		return conexionesAMantener;
	}

	public void setConexionesAMantener(List<List<String>> conexionesAMantener) {
		this.conexionesAMantener = conexionesAMantener;
	}

	public List<Integer> getDireccionesAMantener() {
		return direccionesAMantener;
	}

	public void setDireccionesAMantener(List<Integer> direccionesAMantener) {
		this.direccionesAMantener = direccionesAMantener;
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

	public Map<List<String>, Integer> getPasajerosCompanyia() {
		return pasajerosCompanyia;
	}

	public void setPasajerosCompanyia(Map<List<String>, Integer> pasajerosCompanyia) {
		this.pasajerosCompanyia = pasajerosCompanyia;
	}

	public Map<String, Double> getIngresosTAeropuertoTotal() {
		return ingresosTAeropuertoTotal;
	}

	public void setIngresosTAeropuertoTotal(Map<String, Double> ingresosTAeropuertoTotal) {
		this.ingresosTAeropuertoTotal = ingresosTAeropuertoTotal;
	}

	public List<Double> getTasas() {
		return tasas;
	}

	public void setTasas(List<Double> tasas) {
		this.tasas = tasas;
	}

	public List<String> getAeropuertosDestino() {
		return AeropuertosDestino;
	}

	public void setAeropuertosDestino(List<String> aeropuertosDestino) {
		AeropuertosDestino = aeropuertosDestino;
	}

	public Map<String, Double> getTasasPorAeropuertoDestino() {
		return tasasPorAeropuertoDestino;
	}

	public void setTasasPorAeropuertoDestino(Map<String, Double> tasasPorAeropuertoDestino) {
		this.tasasPorAeropuertoDestino = tasasPorAeropuertoDestino;
	}

	public List<String> getAeropuertosOrigen() {
		return AeropuertosOrigen;
	}

	public void setAeropuertosOrigen(List<String> aeropuertosOrigen) {
		AeropuertosOrigen = aeropuertosOrigen;
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

	public Map<String, Double> getConectividadesAeropuertosOrigen() {
		return conectividadesAeropuertosOrigen;
	}

	public void setConectividadesAeropuertosOrigen(Map<String, Double> conectividadesAeropuertosOrigen) {
		this.conectividadesAeropuertosOrigen = conectividadesAeropuertosOrigen;
	}

	public Map<String, Set<String>> getListaConexionesSalidas() {
		return listaConexionesSalidas;
	}

	public void setListaConexionesSalidas(Map<String, Set<String>> listaConexionesSalidas) {
		this.listaConexionesSalidas = listaConexionesSalidas;
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

	
}
