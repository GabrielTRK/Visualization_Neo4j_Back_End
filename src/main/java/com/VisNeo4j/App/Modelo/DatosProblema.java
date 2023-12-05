package com.VisNeo4j.App.Modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.VisNeo4j.App.Lectura.LecturaDeDatos;
import com.VisNeo4j.App.Utils.Utils;

public class DatosProblema {
	private List<List<String>> conexiones;
	private List<List<String>> conexionesAMantener = new ArrayList<>();
	private List<Integer> direccionesAMantener = new ArrayList<>();
	
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
	private Map<String, Integer> vuelosSalientesAEspanya;
	private Map<String, Integer> vuelosSalientes;
	private Map<String, Double> conectividadesAeropuertosOrigen;
	private Map<String, Set<String>> listaConexionesSalidas;
	
	public DatosProblema(List<Double> riesgos, List<List<String>> conexiones, 
			List<Integer> pasajeros, List<Double> dineroMedioT, List<Double> dineroMedioN,
			List<String> companyias, Map<List<String>, Integer> pasajerosCompanyia,
			List<String> AeropuertosOrigen, List<String> AeropuertosDestino, Map<String, Integer> vuelosSalientes,
			Map<List<String>, Integer> vuelosEntrantesConexion, Map<String, Integer> vuelosSalientesAEspanya, 
			Map<String, Double> conectividadesAeropuertosOrigen, List<Double> tasas,
			Map<String, Double> tasasPorAeropuertoDestino) {
		super();
		this.riesgos = riesgos;
		this.conexiones = conexiones;
		this.pasajeros = pasajeros;
		this.dineroMedioT = dineroMedioT;
		this.dineroMedioN = dineroMedioN;
		this.companyias = companyias;
		this.pasajerosCompanyia = pasajerosCompanyia;
		this.AeropuertosOrigen = AeropuertosOrigen;
		this.AeropuertosDestino = AeropuertosDestino;
		this.vuelosSalientes = vuelosSalientes;
		this.conectividadesAeropuertosOrigen = conectividadesAeropuertosOrigen;
		this.vuelosEntrantesConexion = vuelosEntrantesConexion;
		this.vuelosSalientesAEspanya = vuelosSalientesAEspanya;
		this.obtenerDatosConectividad();
		this.listaConexionesSalidas = new HashMap<>();
		this.leerDatosListaConexionesSalidas();
		this.tasas = tasas;
		this.tasasPorAeropuertoDestino = tasasPorAeropuertoDestino;
		LecturaDeDatos.leerConexionesAMantener(this.conexionesAMantener);
		this.calcularDireccionesAMantener();
		this.calcularIngresosTurismoAeropuerto();
	}
	
	public List<Double> getRiesgos() {
		return riesgos;
	}
	
	public void setRiesgos(List<Double> riesgos) {
		this.riesgos = riesgos;
	}
	
	public List<List<String>> getConexiones() {
		return conexiones;
	}
	
	public void setConexiones(List<List<String>> conexiones) {
		this.conexiones = conexiones;
	}

	public List<Integer> getPasajeros() {
		return pasajeros;
	}

	public void setPasajeros(List<Integer> pasajeros) {
		this.pasajeros = pasajeros;
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

	public List<String> getAeropuertosOrigen() {
		return AeropuertosOrigen;
	}

	public void setAeropuertosOrigen(List<String> aeropuertosOrigen) {
		AeropuertosOrigen = aeropuertosOrigen;
	}

	public Map<String, Integer> getVuelosSalientes() {
		return vuelosSalientes;
	}

	public void setVuelosSalientes(Map<String, Integer> vuelosSalientes) {
		this.vuelosSalientes = vuelosSalientes;
	}

	public Map<List<String>, Integer> getVuelosEntrantesConexion() {
		return vuelosEntrantesConexion;
	}

	public void setVuelosEntrantesConexion(Map<List<String>, Integer> vuelosEntrantesConexion) {
		this.vuelosEntrantesConexion = vuelosEntrantesConexion;
	}

	public Map<String, Double> getConectividadesAeropuertosOrigen() {
		return conectividadesAeropuertosOrigen;
	}

	public void setConectividadesAeropuertosOrigen(Map<String, Double> conectividadesAeropuertosOrigen) {
		this.conectividadesAeropuertosOrigen = conectividadesAeropuertosOrigen;
	}

	public Map<String, Integer> getVuelosSalientesAEspanya() {
		return vuelosSalientesAEspanya;
	}

	public void setVuelosSalientesAEspanya(Map<String, Integer> vuelosSalientesAEspanya) {
		this.vuelosSalientesAEspanya = vuelosSalientesAEspanya;
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

	public Map<String, Set<String>> getListaConexionesSalidas() {
		return listaConexionesSalidas;
	}

	public void setListaConexionesSalidas(Map<String, Set<String>> listaConexionesSalidas) {
		this.listaConexionesSalidas = listaConexionesSalidas;
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

	public Map<String, Double> getIngresosTAeropuertoTotal() {
		return ingresosTAeropuertoTotal;
	}

	public void setIngresosTAeropuertoTotal(Map<String, Double> ingresosTAeropuertoTotal) {
		this.ingresosTAeropuertoTotal = ingresosTAeropuertoTotal;
	}

	public void leerDatosListaConexionesSalidas() {
		for (String aeropuerto : this.AeropuertosOrigen) {
            Set<String> aux = Collections.emptySet();
            this.listaConexionesSalidas.put(aeropuerto, aux);
        }
        for (String aeropuerto : this.AeropuertosOrigen) {
            for (List<String> conexion : this.conexiones) {
                if (conexion.get(0).equals(aeropuerto)) {
                    Set<String> aux = new HashSet<>();
                    if (this.listaConexionesSalidas.get(aeropuerto).size() != 0) {
                        aux = new HashSet<>(this.listaConexionesSalidas.get(aeropuerto));
                    }
                    aux.add(conexion.get(1));
                    this.listaConexionesSalidas.put(aeropuerto, aux);
                }
            }
        }
	}

	private void obtenerDatosConectividad() {
		
		for (String aeropuerto : this.AeropuertosOrigen) {
            if (this.vuelosSalientes.get(aeropuerto) != 0) {
            	this.conectividadesAeropuertosOrigen.put(aeropuerto,
            			this.conectividadesAeropuertosOrigen.get(aeropuerto) *
            			this.vuelosSalientesAEspanya.get(aeropuerto)
                                / this.vuelosSalientes.get(aeropuerto));
            }
        }
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
	
	private void calcularIngresosTurismoAeropuerto () {
		for(int i = 0; i < this.conexiones.size(); i++) {
			if (this.ingresosTAeropuertoTotal.get(this.conexiones.get(i).get(1)) != null) {
				this.ingresosTAeropuertoTotal.put(this.conexiones.get(i).get(1), this.ingresosTAeropuertoTotal.get(
						this.conexiones.get(i).get(1)) + this.dineroMedioT.get(i)); //Sustituir por i
			} else {
				this.ingresosTAeropuertoTotal.put(this.conexiones.get(i).get(1), this.dineroMedioT.get(i)); //Sustituir por i
			}
		}
	}
	
	public Individuo rellenarConexionesFaltantes(Individuo ind) {
		List<Double> aux = ind.getVariables();
		for(int i = 0; i < this.direccionesAMantener.size(); i++) {
			aux.add(this.direccionesAMantener.get(i), 1.0);
		}
		ind.setVariables(aux);
		return ind;
	}

}
