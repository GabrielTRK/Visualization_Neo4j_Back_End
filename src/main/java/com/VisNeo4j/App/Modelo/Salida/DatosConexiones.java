package com.VisNeo4j.App.Modelo.Salida;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.VisNeo4j.App.Modelo.Individuo;
import com.VisNeo4j.App.Problems.Data.DataRRPS_PAT;
import com.VisNeo4j.App.Problems.Data.DatosRRPS_PATDiaI;

public class DatosConexiones {
	
	private List<Aeropuerto> listaAeropuertos;
	private List<Conexion> coordenadasConexiones;
	private Map<String, String> fechas;
	private Individuo GBest;
	private Map<String, List<String>> extraSnapshot;

	/*public DatosConexiones(List<Aeropuerto> listaAeropuertos, Individuo ind, DatosRRPS_PAT datos, Map<String, String> fechas) {
		this.listaAeropuertos = listaAeropuertos;
		this.coordenadasConexiones = new ArrayList<>();
		this.fechas = fechas;
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
					listaAeropuertos.get(i+1).getIata(), 
					datos.getRiesgos().get(i/2), 
					datos.getPasajeros().get(i/2),
					datos.getIngresos().get(i/2), 
					datos.getTasas().get(i/2));
			coordenadasConexiones.add(con);
		}
		
	}*/
	
	public DatosConexiones(List<Aeropuerto> listaAeropuertos, List<Double> ind, DatosRRPS_PATDiaI datos, Map<String, String> fechas) {
		this.listaAeropuertos = listaAeropuertos;
		this.coordenadasConexiones = new ArrayList<>();
		this.fechas = fechas;
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
						listaAeropuertos.get(i+1).getIata(), 
						datos.getConexionesNombres().get(i/2).get(0),
						datos.getConexionesNombres().get(i/2).get(1),
						datos.getRiesgosJuntos().get(i/2), 
						datos.getPasajerosJuntos().get(i/2),
						datos.getIngresosJuntos().get(i/2), 
						datos.getTasasJuntos().get(i/2));
			this.coordenadasConexiones.add(con);
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

	public Map<String, String> getFechas() {
		return fechas;
	}

	public void setFechas(Map<String, String> fechas) {
		this.fechas = fechas;
	}

	public Individuo getGBest() {
		return GBest;
	}

	public void setGBest(Individuo gBest) {
		GBest = gBest;
	}

	public Map<String, List<String>> getExtraSnapshot() {
		return extraSnapshot;
	}

	public void setExtraSnapshot(Map<String, List<String>> extraSnapshot) {
		this.extraSnapshot = extraSnapshot;
	}
	
	

}
