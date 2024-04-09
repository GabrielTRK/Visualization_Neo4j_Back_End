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
					abierto, listaAeropuertos.get(i).getIata(), 
					listaAeropuertos.get(i+1).getIata());
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
					abierto, listaAeropuertos.get(i).getIata(), 
					listaAeropuertos.get(i+1).getIata());
			coordenadasConexiones.add(con);
		}
		
	}
	
	public void aplicarFiltro(String con) {
		
		List<Aeropuerto> nuevaListaAeropuertos = new ArrayList<>();
		List<Conexion> nuevasCoordenadasConexiones = new ArrayList<>();
		
		for(int i = 0; i < this.listaAeropuertos.size(); i = i+2) {
			
			if(this.listaAeropuertos.get(i).getIata().contains(con) || this.listaAeropuertos.get(i+1).getIata().contains(con)) {
				nuevaListaAeropuertos.add(this.listaAeropuertos.get(i));
				nuevaListaAeropuertos.add(this.listaAeropuertos.get(i+1));
					
				nuevasCoordenadasConexiones.add(this.coordenadasConexiones.get(i/2));
			}
		}
		this.listaAeropuertos = nuevaListaAeropuertos;
		this.coordenadasConexiones = nuevasCoordenadasConexiones;
		
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
