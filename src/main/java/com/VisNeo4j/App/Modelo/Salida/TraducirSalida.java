package com.VisNeo4j.App.Modelo.Salida;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.VisNeo4j.App.Constantes.Constantes;
import com.VisNeo4j.App.Lectura.LecturaDeDatos;
import com.VisNeo4j.App.Modelo.Individuo;
import com.VisNeo4j.App.QDMP.ObjectivesOrder;

public class TraducirSalida {
	
	public static List<Aeropuerto> traducir(Individuo ind, List<List<String>> conexiones){
		List<Aeropuerto> listaAeropuertos = new ArrayList<>();
		
		Map<String, List<Double>> coordenadas = new HashMap<>(); 
		LecturaDeDatos.leerCoordenadasAeropuertos(coordenadas);
		
		for(int i = 0; i < ind.getVariables().size(); i++) {
				Aeropuerto nuevo1 = new Aeropuerto(coordenadas.get(conexiones.get(i).get(0)).get(0), 
						coordenadas.get(conexiones.get(i).get(0)).get(1), 
						conexiones.get(i).get(0), true);
				
				
				Aeropuerto nuevo2 = new Aeropuerto(coordenadas.get(conexiones.get(i).get(1)).get(0), 
						coordenadas.get(conexiones.get(i).get(1)).get(1), 
						conexiones.get(i).get(1), false);
				listaAeropuertos.add(nuevo1);
				listaAeropuertos.add(nuevo2);
		}
		
		return listaAeropuertos;
	}

	public static List<Aeropuerto> añadirCoordenadas(List<Double> ind, List<String> conexiones){
		List<Aeropuerto> listaAeropuertos = new ArrayList<>();
		
		Map<String, List<Double>> coordenadas = new HashMap<>(); 
		LecturaDeDatos.leerCoordenadasAeropuertos(coordenadas);
		
		for(int i = 0; i < ind.size(); i++) {
				Aeropuerto nuevo1 = new Aeropuerto(coordenadas.get(conexiones.get(i * 2)).get(0), 
						coordenadas.get(conexiones.get(i * 2)).get(1), 
						conexiones.get(i * 2), true);
				
				Aeropuerto nuevo2 = new Aeropuerto(coordenadas.get(conexiones.get((i * 2) + 1)).get(0), 
						coordenadas.get(conexiones.get((i * 2) + 1)).get(1), 
						conexiones.get((i * 2) + 1), false);
				listaAeropuertos.add(nuevo1);
				listaAeropuertos.add(nuevo2);
		}
		
		return listaAeropuertos;
	}
	
	public static List<FitnessI> obtenerHistoricoDeFitness(List<List<String>> hist){
		List<FitnessI> histFit = new ArrayList<>();
		for(int i = 0; i < hist.size(); i++) {
			FitnessI fitnessi = new FitnessI(Double.valueOf(hist.get(i).get(0)), Double.valueOf(hist.get(i).get(1)));
			histFit.add(fitnessi);
		}
		return histFit;
	}
	
	public static List<Objetivo> obtenerObjetivos(List<List<String>> obj, ObjectivesOrder order){
		List<Objetivo> listObj = new ArrayList<>();
		DecimalFormat df = new DecimalFormat("0.00");
		listObj.add(new Objetivo(obj.get(0).get(0), Double.valueOf(obj.get(0).get(1))));
		/*for(int i = 0; i < obj.size(); i++) {
			Objetivo objI = new Objetivo(obj.get(i).get(0), Double.valueOf(obj.get(i).get(1)));
			listObj.add(objI);
		}*/
		for(int i = 0; i < order.getOrder().size(); i++) {
			listObj.add(new Objetivo(obj.get(order.getOrder().get(i)).get(0), Double.valueOf(df.format(Double.valueOf(obj.get(order.getOrder().get(i)).get(1))))));
		}
		return listObj;
	}
	
	public static Histogramas obtenerHistogramas(Map<String, List<Double>> valores){
		Histogramas histogramas = new Histogramas(valores.get(
				Constantes.nombreCampoPasajerosPerdidosPorCompañía), 
				valores.get(Constantes.nombreCampoIngresoPerdidoPorAreaInf), 
				valores.get(Constantes.nombreCampoIngresoPerdidoPorAerDest));
		return histogramas;
	}
	
	public static Rangos obtenerRangos(Map<String, List<String>> valores) {
		Rangos rangos = new Rangos(valores.get(
				Constantes.nombreCampoPasajerosPerdidosPorCompañía), 
				valores.get(Constantes.nombreCampoIngresoPerdidoPorAreaInf), 
				valores.get(Constantes.nombreCampoIngresoPerdidoPorAerDest));
		return rangos;
	}
	
	public static TooltipTexts obtenerTooltips(Map<Integer, String> tooltips) {
		return new TooltipTexts(tooltips.get(Constantes.idObjetivo1), 
				tooltips.get(Constantes.idObjetivo2), tooltips.get(Constantes.idObjetivo3), 
				tooltips.get(Constantes.idObjetivo4), tooltips.get(Constantes.idObjetivo5), 
				tooltips.get(Constantes.idObjetivo6), tooltips.get(Constantes.idObjetivo7));
	}
	
	public static TooltipTexts obtenerTooltipsDefault() {
		return new TooltipTexts(Constantes.defaultTooltipTextZ1, 
				Constantes.defaultTooltipTextZ2, Constantes.defaultTooltipTextZ3, 
				Constantes.defaultTooltipTextZ4, Constantes.defaultTooltipTextZ5, 
				Constantes.defaultTooltipTextZ6, Constantes.defaultTooltipTextZ7);
	}
	
	public static List<Persona> obtenerPersonasAfectadas(List<List<String>> per){
		List<Persona> listPer = new ArrayList<>();
		for(int i = 0; i < per.size(); i++) {
			Persona perI = new Persona(per.get(i).get(0), Integer.valueOf(per.get(i).get(1)));
			listPer.add(perI);
		}
		return listPer;
	}
	
	public static List<Vuelos> obtenerVuelosCancelados(List<List<String>> vue){
		List<Vuelos> listVue = new ArrayList<>();
		for(int i = 0; i < vue.size(); i++) {
			Vuelos perI = new Vuelos(vue.get(i).get(0), Integer.valueOf(vue.get(i).get(1)));
			listVue.add(perI);
		}
		return listVue;
	}
	
	public static List<Solucion> ordenarSoluciones(List<Solucion> soluciones){
		List<Solucion> solucionesSalida = new ArrayList<>();
		int numS = soluciones.size();
		for(int i = 0; i < numS; i++) {
			for(int j = 0; j < numS; j++) {
				if(soluciones.get(j).getId() == i) {
					solucionesSalida.add(soluciones.get(j));
				}
			}
		}
		return solucionesSalida; 
	}
	
}
