package com.VisNeo4j.App.Service;

import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.neo4j.driver.SessionConfig;
import org.springframework.data.neo4j.core.DatabaseSelectionProvider;
import org.springframework.stereotype.Service;

import com.VisNeo4j.App.Modelo.DatosProblema;
import com.VisNeo4j.App.Modelo.DatosProblemaDias;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


@Service
public class VisNeo4jService {

	private final Driver driver;

	private final DatabaseSelectionProvider databaseSelectionProvider;

	VisNeo4jService(Driver driver,
				 DatabaseSelectionProvider databaseSelectionProvider) {

		this.driver = driver;
		this.databaseSelectionProvider = databaseSelectionProvider;
	}
	
	public DatosProblema obtenerDatos(String dia_I, String dia_F, String mes_I, String mes_F,
			String año_I, String año_F) throws ParseException {
		List<Double> riesgos = new ArrayList<>();
		List<List<String>> conexiones = new ArrayList<>();
		List<Integer> pasajeros = new ArrayList<>();
		List<Double> dineroMedioT = new ArrayList<>();
		List<Double> dineroMedioN = new ArrayList<>();
		List<String> companyias = new ArrayList<>();
		Map<List<String>, Integer> pasajerosCompanyia = new HashMap<>();
		List<String> aeropuertosOrigen = new ArrayList<>();
		List<String> aeropuertosDestino = new ArrayList<>();
		Map<String, Double> conectividadesAeropuertosOrigen = new HashMap<>();
		Map<List<String>, Integer> vuelosEntrantesConexion = new HashMap<>();
		Map<String, Integer> vuelosSalientesAEspanya = new HashMap<>();
		List<Double> tasasAeropuertos = new ArrayList<>();
		Map<String, Double> tasasPorAeropuertoDestino = new HashMap<>();
		
		
		String fecha_I = "\"" + año_I + "-" + mes_I + "-" + dia_I + "\"";
		String fecha_F = "\"" + año_F + "-" + mes_F + "-" + dia_F + "\"";
		
		
	    //c1.continentId = 'EU' and 
		//f.originAirportIncome as tasas

		try (Session session = sessionFor(database())) {
			var records = session.readTransaction(tx -> tx.run(""
				+ " match (a:Airport)-[r1]->(ao:AirportOperationDay)-[r2]->(f:FLIGHT)-[r3]->(ao2:AirportOperationDay)<-[r4]-(a2:Airport) "
				+ " where a2.countryIso = 'ES' and a.countryIso <>'ES' and f.dateOfDeparture <= date("+ fecha_F +") and f.dateOfDeparture >= date("+ fecha_I +") and f.operator <> 'UNKNOWN'" 
				+ " with f,a,a2 order by a.iata, a2.iata "
				+ " return f.flightIfinal as sir,f.passengers as pasajeros,f.operator as companyia,a.connectivity as conectividad,f.incomeFromTurism as dineroT,f.incomeFromBusiness as dineroN,f.destinationAirportIncome as tasas,a.iata as origen,a2.iata as destino"
			).list());
			records.forEach(record -> {
				var sir = record.get("sir").asDouble();
				var origen = record.get("origen").asString();
				var destino = record.get("destino").asString();
				var Num_Pasajeros = record.get("pasajeros").asInt();
				var companyia = record.get("companyia").asString();
				var conectividad = record.get("conectividad").asDouble();
				var dineroT = record.get("dineroT").asDouble();
				var dineroN = record.get("dineroN").asDouble();
				var tasas = record.get("tasas").asDouble();
				
				if(!conexiones.contains(List.of(origen, destino))) {
                	conexiones.add(List.of(origen, destino));
                	vuelosEntrantesConexion.put(List.of(origen, destino), 1);
                }else {
                	vuelosEntrantesConexion.put(List.of(origen, destino), 1 + vuelosEntrantesConexion.get(List.of(origen, destino)));
                }
				if(!vuelosSalientesAEspanya.keySet().contains(origen)) {
					vuelosSalientesAEspanya.put(origen, 1);
				}else {
					vuelosSalientesAEspanya.put(origen, 1 + vuelosSalientesAEspanya.get(origen));
				}
				if(!companyias.contains(companyia)) {
                	companyias.add(companyia);
                }
				if(pasajerosCompanyia.keySet().contains(List.of(origen, destino, companyia))) {
					pasajerosCompanyia.put(List.of(origen, destino, companyia),
                            pasajerosCompanyia.get(List.of(origen, destino, companyia)) + Num_Pasajeros);
				}else {
					pasajerosCompanyia.put(List.of(origen, destino, companyia), Num_Pasajeros);
				}
				if(!aeropuertosOrigen.contains(origen)) {
					aeropuertosOrigen.add(origen);
					if(conectividad == -1.0) {
						conectividadesAeropuertosOrigen.put(origen,0.0);
					}else {
						conectividadesAeropuertosOrigen.put(origen,conectividad);
					}
				}
				if(!aeropuertosDestino.contains(destino)) {
					aeropuertosDestino.add(destino);
				}
				if(!tasasPorAeropuertoDestino.keySet().contains(destino)) {
					tasasPorAeropuertoDestino.put(destino, tasas);
				}else {
					tasasPorAeropuertoDestino.put(destino, tasas + tasasPorAeropuertoDestino.put(destino, tasas));
				}
				
				int posicion = conexiones.indexOf(List.of(origen, destino));
				
                	
                if (posicion < riesgos.size() && posicion < pasajeros.size() && posicion < dineroMedioT.size()&& posicion < dineroMedioN.size()&& posicion < tasasAeropuertos.size()) {
                   	riesgos.set(posicion, sir + riesgos.get(posicion));
                   	pasajeros.set(posicion, Num_Pasajeros + pasajeros.get(posicion));
                   	dineroMedioT.set(posicion, dineroT + dineroMedioT.get(posicion));
                   	dineroMedioN.set(posicion, dineroN + dineroMedioN.get(posicion));
                   	tasasAeropuertos.set(posicion, tasas + tasasAeropuertos.get(posicion));
                } else {
                    riesgos.add(posicion, sir + 0);
                    pasajeros.add(posicion, Num_Pasajeros + 0);
                    dineroMedioT.add(posicion, dineroT + 0);
                    dineroMedioN.add(posicion, dineroN + 0);
                    tasasAeropuertos.add(posicion, tasas + 0);
                }
                    
			});
		}
		Map<String, Integer> vuelosSalientes = this.vuelosSalientesDeOrigen(dia_I, dia_F, mes_I, mes_F, año_I, año_F, aeropuertosOrigen);
		
		return new DatosProblema(riesgos, conexiones, pasajeros, dineroMedioT, dineroMedioN,
				companyias, pasajerosCompanyia, aeropuertosOrigen, aeropuertosDestino, vuelosSalientes, 
				vuelosEntrantesConexion, vuelosSalientesAEspanya, 
				conectividadesAeropuertosOrigen, tasasAeropuertos, 
				tasasPorAeropuertoDestino);
	}
	
	public Map<String, Integer> vuelosSalientesDeOrigen(String dia_I, String dia_F, String mes_I, String mes_F,
			String año_I, String año_F, List<String> aeropuertosOrigen) {
		
		Map<String, Integer> vuelosSalientes = new HashMap<>();
		
		String fecha_I = "\"" + año_I + "-" + mes_I + "-" + dia_I + "\"";
		String fecha_F = "\"" + año_F + "-" + mes_F + "-" + dia_F + "\"";
		
		for(String origen: aeropuertosOrigen) {

			try (Session session = sessionFor(database())) {
				var records = session.readTransaction(tx -> tx.run(""
						+ " match (a:Airport)-[r1]->(ao:AirportOperationDay)-[r2]->(f:FLIGHT)-[r3]->(ao2:AirportOperationDay)<-[r4]-(a2:Airport) "
						+ " where a.iata = '"+ origen +"' and f.dateOfDeparture <= date(" + fecha_F + ") and f.dateOfDeparture >= date(" + fecha_I + ") and f.operator <> 'UNKNOWN'" 
						+ " return count(*) as numVuelos"
						).list());
				records.forEach(record -> {
				var vuelos = record.get("numVuelos").asInt();
				
				vuelosSalientes.put(origen, vuelos);
				});
			}
		}
		
		return vuelosSalientes;
	}
	
	public DatosProblemaDias obtenerDatosDias(String dia_I, String dia_F, String mes_I, String mes_F,
			String año_I, String año_F) throws ParseException {
		List<DatosProblema> datosPorDia = new ArrayList<>();
		
		String fecha_I = año_I + "-" + mes_I + "-" + dia_I;
		String fecha_F = año_F + "-" + mes_F + "-" + dia_F;
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		Date fechaInicio = sdf.parse(fecha_I);
		Date fechaFinal = sdf.parse(fecha_F);
		
		long diffInMillies = Math.abs(fechaFinal.getTime() - fechaInicio.getTime());
	    int numDias = (int)TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
	    
	    Calendar c = Calendar.getInstance();
	    c.setTime(fechaInicio);
	    
	    for(int i = 0; i <= numDias; i++) {
		    
		    String dia_Inicial = String.valueOf(c.get(Calendar.DATE));
		    if(dia_Inicial.length() == 1) {
		    	dia_Inicial = "0" + dia_Inicial;
		    }
		    String dia_Final = dia_Inicial;
		    String mes_Inicial = String.valueOf(c.get(Calendar.MONTH)+1);
		    if(mes_Inicial.length() == 1) {
		    	mes_Inicial = "0" + mes_Inicial;
		    }
		    String mes_Final = mes_Inicial;
			String año_Inicial = String.valueOf(c.get(Calendar.YEAR));
			String año_Final = año_Inicial;
		    
		    datosPorDia.add(obtenerDatos(dia_Inicial, dia_Final, mes_Inicial, mes_Final, 
		    		año_Inicial, año_Final));
		    
		    
		    c.add(Calendar.DATE, 1);
	    }
	    
	    
		return new DatosProblemaDias(numDias, sdf.format(fechaInicio), sdf.format(fechaFinal), datosPorDia);
	}
	
	
	

	private Session sessionFor(String database) {
		if (database == null) {
			return driver.session();
		}
		return driver.session(SessionConfig.forDatabase(database));
	}

	private String database() {
		return databaseSelectionProvider.getDatabaseSelection().getValue();
	}
	
}
