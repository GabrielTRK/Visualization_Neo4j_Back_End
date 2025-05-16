package com.VisNeo4j.App.Problemas.Datos;

import java.util.ArrayList;
import java.util.HashMap;
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
	private List<List<String>> conexionesNombresTotalesSeparadas;
	private List<List<String>> conexionesTotalesSeparadas;
	
	private List<Double> riesgos;
	private List<Integer> pasajeros;
	private List<Double> ingresos;
	private List<Double> tasas;
	
	private List<Double> riesgos_KP;
	private List<Integer> pasajeros_KP;
	private List<Double> ingresos_KP;
	private List<Double> tasas_KP;
	private List<String> areasInf_KP;
	private List<List<String>> companyias_KP;
	private List<List<Integer>> pasajerosCompanyias_KP;
	private List<List<Integer>> totalPasajerosCompanyias;
	
	private List<Integer> vuelosEntrantesConexionOrdenadoTotales;
	private List<Integer> vuelosEntrantesConexionOrdenadoTotalTotales;
	private List<Double> conectividadesTotales;
	
	private List<Double> ingresosAreaInfTotalTotales;
	
	private List<Double> ingresosAerDestTotalTotales;
	
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
		this.conexionesTotalesSeparadas = new ArrayList<>();
		this.conexionesNombresTotales = new ArrayList<>();
		this.conexionesNombresTotalesSeparadas = new ArrayList<>();
		this.riesgos = new ArrayList<>();
		this.riesgos_KP = new ArrayList<>();
		this.pasajeros = new ArrayList<>();
		this.pasajeros_KP = new ArrayList<>();
		this.ingresos = new ArrayList<>();
		List<Double> ingresosN = new ArrayList<>();
		this.ingresos_KP = new ArrayList<>();
		List<Double> ingresosN_KP = new ArrayList<>();
		this.tasas = new ArrayList<>();
		this.tasas_KP = new ArrayList<>();
		this.vuelosEntrantesConexionOrdenadoTotales = new ArrayList<>();
		this.conectividadesTotales = new ArrayList<>();
		this.companyiasTotales = new ArrayList<>();
		this.aresInfTotales = new ArrayList<>();
		this.continentesTotales = new ArrayList<>();
		this.capitalesTotales = new ArrayList<>();
		this.vuelosEntrantesConexionOrdenadoTotalTotales = new ArrayList<>();
		this.areasInf_KP = new ArrayList<>();
		this.ingresosAreaInfTotalTotales = new ArrayList<>();
		this.ingresosAerDestTotalTotales = new ArrayList<>();
		this.companyias_KP = new ArrayList<>();
		this.pasajerosCompanyias_KP =  new ArrayList<>();
		this.totalPasajerosCompanyias = new ArrayList<>();
		for(int i = 0; i < this.datosPorDia.size(); i++) {
			this.conexionesTotales.addAll(this.datosPorDia.get(i).getConexiones());
			this.conexionesTotalesSeparadas.addAll(this.datosPorDia.get(i).getConexionesTotal());
			this.conexionesNombresTotales.addAll(this.datosPorDia.get(i).getConexionesNombres());
			this.conexionesNombresTotalesSeparadas.addAll(this.datosPorDia.get(i).getConexionesNombresTotal());
			this.riesgos.addAll(this.datosPorDia.get(i).getRiesgos());
			this.riesgos_KP.addAll(this.datosPorDia.get(i).getRiesgos_KP());
			this.pasajeros.addAll(this.datosPorDia.get(i).getPasajeros());
			this.pasajeros_KP.addAll(this.datosPorDia.get(i).getPasajeros_KP());
			ingresosN.addAll(this.datosPorDia.get(i).getDineroMedioN());
			this.ingresos.addAll(this.datosPorDia.get(i).getDineroMedioT());
			ingresosN_KP.addAll(this.datosPorDia.get(i).getDineroMedioN_KP());
			this.ingresos_KP.addAll(this.datosPorDia.get(i).getDineroMedioT_KP());
			this.tasas.addAll(this.datosPorDia.get(i).getTasas());
			this.tasas_KP.addAll(this.datosPorDia.get(i).getTasas_KP());
			this.vuelosEntrantesConexionOrdenadoTotales.addAll(this.datosPorDia.get(i).getVuelosEntrantesConexionOrdenado());
			this.conectividadesTotales.addAll(this.datosPorDia.get(i).getConectividades());
			this.companyiasTotales.addAll(this.datosPorDia.get(i).getCompanyias());
			this.aresInfTotales.addAll(this.datosPorDia.get(i).getAreasInf());
			this.continentesTotales.addAll(this.datosPorDia.get(i).getContinentes());
			this.capitalesTotales.addAll(this.datosPorDia.get(i).getCapitales());
			this.vuelosEntrantesConexionOrdenadoTotalTotales.addAll(this.datosPorDia.get(i).getVuelosEntrantesConexionOrdenadoTotal());
			this.areasInf_KP.addAll(this.datosPorDia.get(i).getAreasInf_KP());
			//this.ingresosAreaInfTotalTotales.addAll(this.datosPorDia.get(i).getIngresosAreaInfTotal());
			//this.ingresosAerDestTotalTotales.addAll(this.datosPorDia.get(i).getIngresosAerDestTotal());
			this.companyias_KP.addAll(this.datosPorDia.get(i).getCompanyias_KP());
			this.pasajerosCompanyias_KP.addAll(this.datosPorDia.get(i).getPasajerosCompanyias_KP());
			//this.totalPasajerosCompanyias.addAll(this.datosPorDia.get(i).getTotalPasajerosCompanyias());
		}
		for(int i = 0; i < this.ingresos.size(); i++) {
			this.ingresos.set(i, this.ingresos.get(i) + ingresosN.get(i));
		}
		for(int i = 0; i < this.ingresos_KP.size(); i++) {
			this.ingresos_KP.set(i, this.ingresos_KP.get(i) + ingresosN_KP.get(i));
		}
		
		this.obtenerIngresosTotalesPorAreaInf();
		this.obtenerIngresosTotalesPorAerDest();
		this.obtenerPasajerosTotalesPorCompanyia();
	}
	
	private void obtenerIngresosTotalesPorAreaInf() {
		for(int i = 0; i < this.areasInf_KP.size(); i++) {
			double ingreso = 0.0;
			for(int j = 0; j < this.aresInfTotales.size(); j++) {
				if(this.areasInf_KP.get(i).equals(this.aresInfTotales.get(j))) {
					ingreso += this.ingresos.get(j);
				}
			}
			this.ingresosAreaInfTotalTotales.add(ingreso);
		}
	}
	
	private void obtenerIngresosTotalesPorAerDest() {
		for(int i = 0; i < this.conexionesTotales.size(); i++) {
			double ingreso = 0.0;
			for(int j = 0; j < this.conexionesTotalesSeparadas.size(); j++) {
				if(this.conexionesTotales.get(i).get(1).equals(this.conexionesTotalesSeparadas.get(j).get(1))) {
					ingreso += this.tasas.get(j);
				}
			}
			this.ingresosAerDestTotalTotales.add(ingreso);
		}
	}
	
	private void obtenerPasajerosTotalesPorCompanyia() {
		Map<String, Integer> totalPasajerosComp = new HashMap<>();
		
		for(int i = 0; i < this.companyias_KP.size(); i++) {
			List<Integer> listaTotalPasajeros = new ArrayList<>();
			this.totalPasajerosCompanyias.add(listaTotalPasajeros);
			for(int j = 0; j < this.companyias_KP.get(i).size(); j++) {
				
				if(totalPasajerosComp.containsKey(this.companyias_KP.get(i).get(j))) {
					this.totalPasajerosCompanyias.get(i).add(totalPasajerosComp.get(this.companyias_KP.get(i).get(j)));
				}else {
					int totalPasajeros = 0;
					for(int k = 0; k < this.companyias_KP.size(); k++) {
						int posicion = this.companyias_KP.get(k).indexOf(this.companyias_KP.get(i).get(j));
						if(posicion != -1) {
							totalPasajeros += this.pasajerosCompanyias_KP.get(k).get(posicion);
						}
					}
					this.totalPasajerosCompanyias.get(i).add(totalPasajeros);
					totalPasajerosComp.put(this.companyias_KP.get(i).get(j), totalPasajeros);
				}
				
				
				
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

	public List<List<String>> getConexionesNombresTotalesSeparadas() {
		return conexionesNombresTotalesSeparadas;
	}

	public void setConexionesNombresTotalesSeparadas(List<List<String>> conexionesNombresTotales) {
		this.conexionesNombresTotalesSeparadas = conexionesNombresTotales;
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

	public List<Double> getIngresos_KP() {
		return ingresos_KP;
	}

	public void setIngresos_KP(List<Double> ingresos_KP) {
		this.ingresos_KP = ingresos_KP;
	}

	public List<Double> getTasas_KP() {
		return tasas_KP;
	}

	public void setTasas_KP(List<Double> tasas_KP) {
		this.tasas_KP = tasas_KP;
	}

	public List<Integer> getVuelosEntrantesConexionOrdenadoTotalTotales() {
		return vuelosEntrantesConexionOrdenadoTotalTotales;
	}

	public void setVuelosEntrantesConexionOrdenadoTotalTotales(List<Integer> vuelosEntrantesConexionOrdenadoTotalTotales) {
		this.vuelosEntrantesConexionOrdenadoTotalTotales = vuelosEntrantesConexionOrdenadoTotalTotales;
	}

	public List<String> getAreasInf_KP() {
		return areasInf_KP;
	}

	public void setAreasInf_KP(List<String> areasInf_KP) {
		this.areasInf_KP = areasInf_KP;
	}

	public List<Double> getIngresosAreaInfTotalTotales() {
		return ingresosAreaInfTotalTotales;
	}

	public void setIngresosAreaInfTotalTotales(List<Double> ingresosAreaInfTotalTotales) {
		this.ingresosAreaInfTotalTotales = ingresosAreaInfTotalTotales;
	}

	public List<Double> getIngresosAerDestTotalTotales() {
		return ingresosAerDestTotalTotales;
	}

	public void setIngresosAerDestTotalTotales(List<Double> ingresosAerDestTotalTotales) {
		this.ingresosAerDestTotalTotales = ingresosAerDestTotalTotales;
	}

	public List<List<String>> getCompanyias_KP() {
		return companyias_KP;
	}

	public void setCompanyias_KP(List<List<String>> companyias_KP) {
		this.companyias_KP = companyias_KP;
	}

	public List<List<Integer>> getPasajerosCompanyias_KP() {
		return pasajerosCompanyias_KP;
	}

	public void setPasajerosCompanyias_KP(List<List<Integer>> pasajerosCompanyias_KP) {
		this.pasajerosCompanyias_KP = pasajerosCompanyias_KP;
	}

	public List<List<Integer>> getTotalPasajerosCompanyias() {
		return totalPasajerosCompanyias;
	}

	public void setTotalPasajerosCompanyias(List<List<Integer>> totalPasajerosCompanyias) {
		this.totalPasajerosCompanyias = totalPasajerosCompanyias;
	}
	
	
	
	
}
