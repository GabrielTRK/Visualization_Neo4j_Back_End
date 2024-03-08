package com.VisNeo4j.App.Service;

import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.neo4j.driver.SessionConfig;
import org.springframework.data.neo4j.core.DatabaseSelectionProvider;
import org.springframework.stereotype.Service;

import com.VisNeo4j.App.Constantes.Constantes;
import com.VisNeo4j.App.Lectura.LecturaDeDatos;
import com.VisNeo4j.App.Modelo.DatosProblema;
import com.VisNeo4j.App.Modelo.DatosProblemaDias;
import com.VisNeo4j.App.Modelo.DatosRRPS_PAT;
import com.VisNeo4j.App.Modelo.DatosRRPS_PATDiaI;
import com.VisNeo4j.App.Utils.Utils;

import java.io.File;
import java.io.IOException;
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
	
	public DatosRRPS_PAT obtenerDatosRRPS_PAT(String dia_I, String dia_F, String mes_I, String mes_F,
	String año_I, String año_F) throws ParseException, IOException {
	List<DatosRRPS_PATDiaI> datosPorDia = new ArrayList<>();
		
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
		    
		    datosPorDia.add(obtenerDatosRRPS_PATDiaI(dia_Inicial, dia_Final, mes_Inicial, mes_Final, 
		    		año_Inicial, año_Final, año_Inicial + "-" + mes_Inicial + "-" + dia_Inicial));
		    
		    
		    c.add(Calendar.DATE, 1);
	    }
	    
	    
		return new DatosRRPS_PAT(numDias, sdf.format(fechaInicio), sdf.format(fechaFinal), datosPorDia);
	}
	
	public DatosRRPS_PATDiaI obtenerDatosRRPS_PATDiaI(String dia_I, String dia_F, String mes_I, String mes_F,
			String año_I, String año_F, String ruta) throws IOException{
		List<Double> riesgos = new ArrayList<>();
		List<List<String>> conexiones = new ArrayList<>();
		List<List<String>> conexionesTotal = new ArrayList<>();
		List<Integer> pasajeros = new ArrayList<>();
		List<Double> dineroMedioT = new ArrayList<>();
		List<Double> dineroMedioN = new ArrayList<>();
		List<String> companyias = new ArrayList<>();
		List<String> companyiasTotal = new ArrayList<>();
		Map<List<String>, Integer> pasajerosCompanyia = new HashMap<>();//
		List<String> aeropuertosOrigen = new ArrayList<>();
		List<String> aeropuertosOrigenTotal = new ArrayList<>();
		List<String> aeropuertosDestino = new ArrayList<>();
		List<String> aeropuertosDestinoTotal = new ArrayList<>();
		Map<String, Integer> vuelosSalientes = new HashMap<>();
		Map<List<String>, Integer> vuelosEntrantesConexion = new HashMap<>();//
		Map<String, Integer> vuelosSalientesAEspanya = new HashMap<>();
		Map<String, Double> conectividadesAeropuertosOrigen = new HashMap<>();//
		List<Double> conectividades = new ArrayList<>();
		List<Double> tasasAeropuertos = new ArrayList<>();
		Map<String, Double> tasasPorAeropuertoDestino = new HashMap<>();//
		List<Integer> vuelosSalientesDeOrigen = new ArrayList<>();
		
		//TODO: Comprobar si existe el fichero paar ese día. Sino buscar en la BBDD y guardar el fichero
		File file = new File(Constantes.rutaDatosPorDia + ruta + Constantes.extensionFichero);
		
		if (file.exists()) {
			LecturaDeDatos.leerDatosRRPS_PATDiaI(ruta, riesgos, conexiones, conexionesTotal, pasajeros, dineroMedioT, dineroMedioN, companyias, companyiasTotal, pasajerosCompanyia, aeropuertosOrigen, aeropuertosOrigenTotal, aeropuertosDestino, aeropuertosDestinoTotal, conectividadesAeropuertosOrigen, conectividades, vuelosEntrantesConexion, vuelosSalientesAEspanya, tasasAeropuertos, tasasPorAeropuertoDestino, vuelosSalientes, vuelosSalientesDeOrigen);
		}
        else {
        	obtenerDatosRRPS_PAT_BBDD(riesgos, conexiones, conexionesTotal, pasajeros, 
        			dineroMedioT, dineroMedioN, companyias, companyiasTotal, pasajerosCompanyia, 
        			aeropuertosOrigen, aeropuertosOrigenTotal, aeropuertosDestino, 
        			aeropuertosDestinoTotal, conectividadesAeropuertosOrigen, conectividades, 
        			vuelosEntrantesConexion, vuelosSalientesAEspanya, tasasAeropuertos, tasasPorAeropuertoDestino, 
        			vuelosSalientes, vuelosSalientesDeOrigen, dia_I, dia_F, mes_I, mes_F, 
        			año_I, año_F, ruta);
        }
		
		
		
		return new DatosRRPS_PATDiaI(riesgos, conexiones, conexionesTotal, pasajeros, 
				dineroMedioT, dineroMedioN, companyias, companyiasTotal, pasajerosCompanyia, 
				aeropuertosOrigen, aeropuertosOrigenTotal, aeropuertosDestino, 
				aeropuertosDestinoTotal, vuelosSalientes, vuelosEntrantesConexion, vuelosSalientesAEspanya, 
				conectividadesAeropuertosOrigen, conectividades, tasasAeropuertos, 
				tasasPorAeropuertoDestino, vuelosSalientesDeOrigen);
	}
	
	public void obtenerDatosRRPS_PAT_BBDD(List<Double> riesgos, List<List<String>> conexiones,
			List<List<String>> conexionesTotal, List<Integer> pasajeros, List<Double> dineroMedioT, List<Double> dineroMedioN,
			List<String> companyias, List<String> companyiasTotal, Map<List<String>, Integer> pasajerosCompanyia, 
			List<String> aeropuertosOrigen, List<String> aeropuertosOrigenTotal, List<String> aeropuertosDestino, List<String> areasInf,
			Map<String, Double> conectividadesAeropuertosOrigen, List<Double> conectividades, Map<List<String>, Integer> vuelosEntrantesConexion, 
			Map<String, Integer> vuelosSalientesAEspanya, List<Double> tasasAeropuertos, 
			Map<String, Double> tasasPorAeropuertoDestino, Map<String, Integer> vuelosSalientes, List<Integer> vuelosSalientesDeOrigen,
			String dia_I, String dia_F, String mes_I, String mes_F,
			String año_I, String año_F, String ruta) throws IOException {
		
		String fecha_I = "\"" + año_I + "-" + mes_I + "-" + dia_I + "\"";
		String fecha_F = "\"" + año_F + "-" + mes_F + "-" + dia_F + "\"";
		
		List<String[]> datosFichero = new ArrayList<>();
		String[] cabecera = new String[11];
		
		cabecera[0] = String.valueOf(Constantes.sirDBField);
		cabecera[1] = String.valueOf(Constantes.pasajerosDBField);
		cabecera[2] = String.valueOf(Constantes.companyiaDBField);
		cabecera[3] = String.valueOf(Constantes.conectividadDBField);
		cabecera[4] = String.valueOf(Constantes.dineroTDBField);
		cabecera[5] = String.valueOf(Constantes.dineroNDBField);
		cabecera[6] = String.valueOf(Constantes.tasasDBField);
		cabecera[7] = String.valueOf(Constantes.origenDBField);
		cabecera[8] = String.valueOf(Constantes.destinoDBField);
		cabecera[9] = String.valueOf(Constantes.VuelosDesdeOrigenDBField);
		cabecera[10] = String.valueOf(Constantes.areaInfDBField);
		
		datosFichero.add(cabecera);
		
		try (Session session = sessionFor(database())) {
			var records = session.readTransaction(tx -> tx.run(""
				+ " match (a:Airport)-[r1]->(ao:AirportOperationDay)-[r2]->(f:FLIGHT)-[r3]->(ao2:AirportOperationDay)<-[r4]-(a2:Airport) "
				+ " where a2.countryIso = 'ES' and a.countryIso <>'ES' and f.dateOfDeparture <= date("+ fecha_F +") and f.dateOfDeparture >= date("+ fecha_I +") and f.operator <> 'UNKNOWN' and f.seatsCapacity > 0" 
				+ " with f,a,a2 order by a.iata, a2.iata "
				
				+ " call{ "
				+ " with a "
				+ " match (a)-[r1]->(ao:AirportOperationDay)-[r2]->(f:FLIGHT)-[r3]->   (ao2:AirportOperationDay)<-[r4]-(a2:Airport) "
				+ " where f.dateOfDeparture <= date("+ fecha_F +") and f.dateOfDeparture >= date("+ fecha_I +") and f.operator <> 'UNKNOWN' "
				+ " return count(*) as numVuelos "
				+ " } "
				
				+ " return f.flightIfinal as sir,f.passengers as pasajeros,f.operator as companyia,a.connectivity as conectividad,f.incomeFromTurism as dineroT,f.incomeFromBusiness as dineroN,f.destinationAirportIncome as tasas,a.iata as origen,a2.iata as destino, numVuelos as VuelosDesdeOrigen, a2.regionName as areaInf"
			).list());
			records.forEach(record -> {
				var sir = record.get(Constantes.sirDBField).asDouble();
				var origen = record.get(Constantes.origenDBField).asString();
				var destino = record.get(Constantes.destinoDBField).asString();
				var Num_Pasajeros = record.get(Constantes.pasajerosDBField).asInt();
				var companyia = record.get(Constantes.companyiaDBField).asString();
				var conectividad = record.get(Constantes.conectividadDBField).asDouble();
				var dineroT = record.get(Constantes.dineroTDBField).asDouble();
				var dineroN = record.get(Constantes.dineroNDBField).asDouble();
				var tasas = record.get(Constantes.tasasDBField).asDouble();
				var VuelosDesdeOrigen = record.get(Constantes.VuelosDesdeOrigenDBField).asInt();
				var areaInf = record.get(Constantes.areaInfDBField).asString();
				
				if(!conexiones.contains(List.of(origen, destino))) {
                	conexiones.add(List.of(origen, destino));
                	vuelosEntrantesConexion.put(List.of(origen, destino), 1);
                	if(conectividad == -1.0) {
                		conectividades.add(0.0);
					}else {
						conectividades.add(conectividad);
					}
                }else {
                	vuelosEntrantesConexion.put(List.of(origen, destino), 1 + vuelosEntrantesConexion.get(List.of(origen, destino)));
                }
                if(!vuelosSalientesAEspanya.keySet().contains(origen)) {
					vuelosSalientesAEspanya.put(origen, 1);
				}else {
					vuelosSalientesAEspanya.put(origen, 1 + vuelosSalientesAEspanya.get(origen));
				}
                if(!aeropuertosOrigen.contains(origen)) {
					aeropuertosOrigen.add(origen);
					if(conectividad == -1.0) {
						conectividadesAeropuertosOrigen.put(origen,0.0);
					}else {
						conectividadesAeropuertosOrigen.put(origen,conectividad);
					}
				}
                if(!vuelosSalientes.keySet().contains(origen)) {
                	vuelosSalientes.put(origen, VuelosDesdeOrigen);
                }
                if(!aeropuertosDestino.contains(destino)) {
                	aeropuertosDestino.add(destino);
                }
				
				conexionesTotal.add(List.of(origen, destino));
                riesgos.add(sir);
                pasajeros.add(Num_Pasajeros);
                dineroMedioT.add(dineroT);
                dineroMedioN.add(dineroN);
                tasasAeropuertos.add(tasas);
                companyias.add(companyia);
                areasInf.add(areaInf);
                
                String[] filaIFichero = new String[11];
                
                filaIFichero[0] = String.valueOf(sir);
                filaIFichero[1] = String.valueOf(Num_Pasajeros);
                filaIFichero[2] = String.valueOf(companyia);
                filaIFichero[3] = String.valueOf(conectividad);
                filaIFichero[4] = String.valueOf(dineroT);
                filaIFichero[5] = String.valueOf(dineroN);
                filaIFichero[6] = String.valueOf(tasas);
                filaIFichero[7] = String.valueOf(origen);
                filaIFichero[8] = String.valueOf(destino);
                filaIFichero[9] = String.valueOf(VuelosDesdeOrigen);
                filaIFichero[10] = String.valueOf(areaInf);
                
                datosFichero.add(filaIFichero);
			});
		}
		//TODO: Guardar en fichero
		Utils.crearFicheroConDatosDiaI(datosFichero, ruta);
	}
	
	public DatosRRPS_PAT obtenerDatosRRPS_PATFichero(String dia_I, String dia_F, String mes_I, String mes_F,
			String año_I, String año_F) throws ParseException {
		List<DatosRRPS_PATDiaI> datosPorDia = new ArrayList<>();
		
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
		    
		    datosPorDia.add(obtenerDatosRRPS_PATFicheroDiaI(dia_Inicial, dia_Final, mes_Inicial, mes_Final, 
		    		año_Inicial, año_Final, año_Inicial + "-" + mes_Inicial + "-" + dia_Inicial));
		    
		    
		    c.add(Calendar.DATE, 1);
	    }
	    
	    
		return new DatosRRPS_PAT(numDias, sdf.format(fechaInicio), sdf.format(fechaFinal), datosPorDia);
	}
	
	public DatosRRPS_PATDiaI obtenerDatosRRPS_PATFicheroDiaI(String dia_I, String dia_F, String mes_I, String mes_F,
			String año_I, String año_F, String ruta){
		List<Double> riesgos = new ArrayList<>();
		List<List<String>> conexiones = new ArrayList<>();
		List<List<String>> conexionesTotal = new ArrayList<>();
		List<Integer> pasajeros = new ArrayList<>();
		List<Double> dineroMedioT = new ArrayList<>();
		List<Double> dineroMedioN = new ArrayList<>();
		List<String> companyias = new ArrayList<>();
		List<String> companyiasTotal = new ArrayList<>();
		Map<List<String>, Integer> pasajerosCompanyia = new HashMap<>();//
		List<String> aeropuertosOrigen = new ArrayList<>();
		List<String> aeropuertosOrigenTotal = new ArrayList<>();
		List<String> aeropuertosDestino = new ArrayList<>();
		List<String> aeropuertosDestinoTotal = new ArrayList<>();
		Map<String, Integer> vuelosSalientes = new HashMap<>();
		Map<List<String>, Integer> vuelosEntrantesConexion = new HashMap<>();//
		Map<String, Integer> vuelosSalientesAEspanya = new HashMap<>();
		Map<String, Double> conectividadesAeropuertosOrigen = new HashMap<>();//
		List<Double> conectividades = new ArrayList<>();
		List<Double> tasasAeropuertos = new ArrayList<>();
		Map<String, Double> tasasPorAeropuertoDestino = new HashMap<>();//
		List<Integer> vuelosSalientesDeOrigen = new ArrayList<>();
		
		LecturaDeDatos.leerDatosRRPS_PATDiaI(ruta, riesgos, conexiones, conexionesTotal, pasajeros, dineroMedioT, dineroMedioN, companyias, companyiasTotal, pasajerosCompanyia, aeropuertosOrigen, aeropuertosOrigenTotal, aeropuertosDestino, aeropuertosDestinoTotal, conectividadesAeropuertosOrigen, conectividades, vuelosEntrantesConexion, vuelosSalientesAEspanya, tasasAeropuertos, tasasPorAeropuertoDestino, vuelosSalientes, vuelosSalientesDeOrigen);
		
		return new DatosRRPS_PATDiaI(riesgos, conexiones, conexionesTotal, pasajeros, dineroMedioT, dineroMedioN,
				companyias, companyiasTotal, pasajerosCompanyia, aeropuertosOrigen, aeropuertosOrigenTotal, aeropuertosDestino, aeropuertosDestinoTotal, vuelosSalientes, 
				vuelosEntrantesConexion, vuelosSalientesAEspanya, 
				conectividadesAeropuertosOrigen, conectividades, tasasAeropuertos, 
				tasasPorAeropuertoDestino, vuelosSalientesDeOrigen);
	}
	
	
	
	public DatosProblemaDias obtenerDatosDiasFichero(String dia_I, String dia_F, String mes_I, String mes_F,
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
		    
		    datosPorDia.add(obtenerDatosFichero(dia_Inicial, dia_Final, mes_Inicial, mes_Final, 
		    		año_Inicial, año_Final, año_Inicial + "-" + mes_Inicial + "-" + dia_Inicial));
		    
		    
		    c.add(Calendar.DATE, 1);
	    }
	    
	    
		return new DatosProblemaDias(numDias, sdf.format(fechaInicio), sdf.format(fechaFinal), datosPorDia);
	}
	
	public DatosProblema obtenerDatosFichero(String dia_I, String dia_F, String mes_I, String mes_F,
			String año_I, String año_F, String ruta){
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
		Map<String, Integer> vuelosSalientes = new HashMap<>();
		
		LecturaDeDatos.leerDatosProblemaDiaI(ruta, riesgos, conexiones, pasajeros, dineroMedioT, dineroMedioN, companyias, pasajerosCompanyia, aeropuertosOrigen, aeropuertosDestino, conectividadesAeropuertosOrigen, vuelosEntrantesConexion, vuelosSalientesAEspanya, tasasAeropuertos, tasasPorAeropuertoDestino, vuelosSalientes);
		//Map<String, Integer> vuelosSalientes = this.vuelosSalientesDeOrigen(dia_I, dia_F, mes_I, mes_F, año_I, año_F, aeropuertosOrigen);
		
		return new DatosProblema(riesgos, conexiones, pasajeros, dineroMedioT, dineroMedioN,
				companyias, pasajerosCompanyia, aeropuertosOrigen, aeropuertosDestino, vuelosSalientes, 
				vuelosEntrantesConexion, vuelosSalientesAEspanya, 
				conectividadesAeropuertosOrigen, tasasAeropuertos, 
				tasasPorAeropuertoDestino);
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
				+ " where a2.countryIso = 'ES' and a.countryIso <>'ES' and f.dateOfDeparture <= date("+ fecha_F +") and f.dateOfDeparture >= date("+ fecha_I +") and f.operator <> 'UNKNOWN' and f.seatsCapacity > 0" 
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
