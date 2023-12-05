package com.VisNeo4j.App.Modelo.Salida;

import java.util.ArrayList;
import java.util.List;

import com.VisNeo4j.App.Modelo.Individuo;

public class DatosConexiones {
	
	private List<Aeropuerto> listaAeropuertos;
	private List<Conexion> coordenadasConexiones;

	public DatosConexiones(List<Aeropuerto> listaAeropuertos, Individuo ind) {
		this.listaAeropuertos = listaAeropuertos;
		this.coordenadasConexiones = new ArrayList<>();
		for(int i = 0; i < listaAeropuertos.size(); i = i+2) {
			boolean abierto = false;
				if(ind.getVariables().get(i/2) == 1.0) {
					abierto = true;
				}
			
			
			Conexion con = new Conexion(listaAeropuertos.get(i).getLongitud(), 
					listaAeropuertos.get(i).getLatitud(), 
					listaAeropuertos.get(i+1).getLongitud(), 
					listaAeropuertos.get(i+1).getLatitud(),
					abierto);
			coordenadasConexiones.add(con);
		}
		
	}
	
	public DatosConexiones(List<Aeropuerto> listaAeropuertos, List<Double> ind) {
		this.listaAeropuertos = listaAeropuertos;
		this.coordenadasConexiones = new ArrayList<>();
		for(int i = 0; i < listaAeropuertos.size(); i = i+2) {
			boolean abierto = false;
				if(ind.get(i/2) == 1.0) {
					abierto = true;
				}
			
			
			Conexion con = new Conexion(listaAeropuertos.get(i).getLongitud(), 
					listaAeropuertos.get(i).getLatitud(), 
					listaAeropuertos.get(i+1).getLongitud(), 
					listaAeropuertos.get(i+1).getLatitud(),
					abierto);
			coordenadasConexiones.add(con);
		}
		
	}

	public List<Aeropuerto> getListaAeropuertos() {
		return listaAeropuertos;
	}

	public void setListaAeropuertos(List<Aeropuerto> listaAeropuertos) {
		this.listaAeropuertos = listaAeropuertos;
	}

	public List<Conexion> getCoordenadasConexiones() {
		return coordenadasConexiones;
	}

	public void setCoordenadasConexiones(List<Conexion> coordenadasConexiones) {
		this.coordenadasConexiones = coordenadasConexiones;
	}

}
