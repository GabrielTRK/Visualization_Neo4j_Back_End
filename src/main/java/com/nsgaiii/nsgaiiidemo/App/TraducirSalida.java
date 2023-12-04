package com.nsgaiii.nsgaiiidemo.App;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nsgaiii.nsgaiiidemo.App.Lectura.LecturaDeDatos;
import com.nsgaiii.nsgaiiidemo.App.Modelo.Individuo;
import com.nsgaiii.nsgaiiidemo.App.Modelo.Salida.Aeropuerto;
import com.nsgaiii.nsgaiiidemo.App.Modelo.Salida.FitnessI;
import com.nsgaiii.nsgaiiidemo.App.Modelo.Salida.Objetivo;
import com.nsgaiii.nsgaiiidemo.App.Modelo.Salida.Persona;
import com.nsgaiii.nsgaiiidemo.App.Modelo.Salida.Vuelos;

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
	
	public static List<Objetivo> obtenerObjetivos(List<List<String>> obj){
		List<Objetivo> listObj = new ArrayList<>();
		for(int i = 0; i < obj.size(); i++) {
			Objetivo objI = new Objetivo(obj.get(i).get(0), Double.valueOf(obj.get(i).get(1)));
			listObj.add(objI);
		}
		return listObj;
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
	
}
