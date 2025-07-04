package com.VisNeo4j.App.Service;

import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.neo4j.driver.SessionConfig;
import org.springframework.data.neo4j.core.DatabaseSelectionProvider;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.VisNeo4j.App.Algoritmo.BPSO;
import com.VisNeo4j.App.Algoritmo.Opciones.BPSOOpciones;
import com.VisNeo4j.App.Algoritmo.Parametros.BPSOParams;
import com.VisNeo4j.App.Constantes.Constantes;
import com.VisNeo4j.App.Lectura.LecturaDeDatos;
import com.VisNeo4j.App.Model.Particle;
import com.VisNeo4j.App.Model.Inbound.PreferencesConstraints;
import com.VisNeo4j.App.Model.Inbound.Usuario;
import com.VisNeo4j.App.Model.Outbound.Aeropuerto;
import com.VisNeo4j.App.Model.Outbound.DatosConexiones;
import com.VisNeo4j.App.Model.Outbound.FitnessI;
import com.VisNeo4j.App.Model.Outbound.Histogramas;
import com.VisNeo4j.App.Model.Outbound.Objetivo;
import com.VisNeo4j.App.Model.Outbound.Proyecto;
import com.VisNeo4j.App.Model.Outbound.Rangos;
import com.VisNeo4j.App.Model.Outbound.Respuesta;
import com.VisNeo4j.App.Model.Outbound.Solucion;
import com.VisNeo4j.App.Model.Outbound.TooltipTexts;
import com.VisNeo4j.App.Model.Outbound.TraducirSalida;
import com.VisNeo4j.App.Problems.Problem;
import com.VisNeo4j.App.Problems.RRPS_PAT;
import com.VisNeo4j.App.Problems.RRPS_PAT_ALT;
import com.VisNeo4j.App.Problems.Data.DataRRPS_PAT;
import com.VisNeo4j.App.Problems.Data.DataRRPS_PATDayI;
import com.VisNeo4j.App.QDMP.DMPreferences;
import com.VisNeo4j.App.QDMP.ObjectivesOrder;
import com.VisNeo4j.App.Utils.Utils;
import com.opencsv.exceptions.CsvException;

import java.io.File;
import java.io.FileNotFoundException;
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
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
public class VisNeo4jService {

	private final Driver driver;

	private final DatabaseSelectionProvider databaseSelectionProvider;
	
	private BPSO bpso;

	VisNeo4jService(Driver driver,
				 DatabaseSelectionProvider databaseSelectionProvider) {

		this.driver = driver;
		this.databaseSelectionProvider = databaseSelectionProvider;
	}
	
	//Obtiene los datos del problema entre las fechas indicadas, ambas incluidas
	public DataRRPS_PAT obtenerDatosRRPS_PAT(String fecha_I, String fecha_F) throws ParseException, IOException {
		List<DataRRPS_PATDayI> datosPorDia = new ArrayList<>();
		
		Date fechaInicio = Constantes.formatoFechaRRPS_PAT.parse(fecha_I);
		Date fechaFinal = Constantes.formatoFechaRRPS_PAT.parse(fecha_F);
		
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
	    
	    
		return new DataRRPS_PAT(numDias+1, Constantes.formatoFechaRRPS_PAT.format(fechaInicio), 
				Constantes.formatoFechaRRPS_PAT.format(fechaFinal), datosPorDia);
	}
	
	//Obtiene los datos del dia indicado. Si existen en el fichero, se leen, si no, se buscan en la BBDD. 
	public DataRRPS_PATDayI obtenerDatosRRPS_PATDiaI(String dia_I, String dia_F, String mes_I, String mes_F,
			String año_I, String año_F, String ruta) throws IOException{
		List<Double> riesgos = new ArrayList<>();
		List<Double> riesgos_KP = new ArrayList<>();
		List<List<String>> conexiones = new ArrayList<>();
		List<List<String>> conexionesTotal = new ArrayList<>();
		List<Integer> pasajeros = new ArrayList<>();
		List<Integer> pasajeros_KP = new ArrayList<>();
		List<Double> dineroMedioT = new ArrayList<>();
		List<Double> dineroMedioT_KP = new ArrayList<>();
		List<Double> dineroMedioN = new ArrayList<>();
		List<Double> dineroMedioN_KP = new ArrayList<>();
		List<String> companyias = new ArrayList<>();
		List<List<Integer>> pasajerosCompanyias_KP = new ArrayList<>();
		List<List<String>> companyias_KP = new ArrayList<>();
		List<String> areasInf = new ArrayList<>();
		List<String> areasInf_KP = new ArrayList<>();
		List<String> continentes = new ArrayList<>();
		List<Boolean> capital = new ArrayList<>();
		Map<String, Integer> vuelosSalientes = new HashMap<>();
		Map<List<String>, Integer> vuelosEntrantesConexion = new HashMap<>();
		Map<String, Integer> vuelosSalientesAEspanya = new HashMap<>();
		List<Double> conectividades = new ArrayList<>();
		List<Double> tasasAeropuertos = new ArrayList<>();
		List<Double> tasasAeropuertos_KP = new ArrayList<>();
		List<Integer> vuelosSalientesDeOrigen = new ArrayList<>();
		List<List<String>> conexionesNombres = new ArrayList<>();
		List<List<String>> conexionesNombresTotal = new ArrayList<>();
		List<String> aeropuertosOrigen = new ArrayList<>();
		
		File file = new File(Constantes.rutaDatosPorDia + ruta + Constantes.extensionFichero);
		
		if (file.exists()) {
			LecturaDeDatos.leerDatosRRPS_PATDiaI(ruta, riesgos, riesgos_KP, conexiones, 
					conexionesTotal, pasajeros, pasajeros_KP, 
					dineroMedioT, dineroMedioT_KP, dineroMedioN, dineroMedioN_KP, 
					companyias, pasajerosCompanyias_KP, companyias_KP, areasInf, areasInf_KP, continentes, 
					capital, conectividades, vuelosEntrantesConexion, vuelosSalientesAEspanya, 
					tasasAeropuertos, tasasAeropuertos_KP, vuelosSalientes, 
					vuelosSalientesDeOrigen, conexionesNombres, conexionesNombresTotal, aeropuertosOrigen);
		}
        else {
        	obtenerDatosRRPS_PAT_BBDD_DiaI(riesgos, conexiones, conexionesTotal, pasajeros, 
        			dineroMedioT, dineroMedioN, companyias, areasInf, continentes,
        			capital, conectividades, 
        			vuelosEntrantesConexion, vuelosSalientesAEspanya, tasasAeropuertos, 
        			vuelosSalientes, vuelosSalientesDeOrigen, conexionesNombres,
        			dia_I, dia_F, mes_I, mes_F, año_I, año_F, ruta);
        }
		
		return new DataRRPS_PATDayI(riesgos, riesgos_KP, conexiones, conexionesTotal, pasajeros, 
				pasajeros_KP, dineroMedioT, dineroMedioT_KP, dineroMedioN, dineroMedioN_KP, 
				companyias, pasajerosCompanyias_KP, companyias_KP, areasInf, areasInf_KP, continentes, capital, vuelosSalientes, 
				vuelosEntrantesConexion, vuelosSalientesAEspanya, conectividades, 
				tasasAeropuertos, tasasAeropuertos_KP, vuelosSalientesDeOrigen, 
				conexionesNombres, conexionesNombresTotal, aeropuertosOrigen);
	}
	
	//Obtiene los datos del dia indicado con la BBDD
	public void obtenerDatosRRPS_PAT_BBDD_DiaI(List<Double> riesgos, List<List<String>> conexiones,
			List<List<String>> conexionesTotal, List<Integer> pasajeros, List<Double> dineroMedioT, 
			List<Double> dineroMedioN, List<String> companyias, 
			List<String> areasInf, List<String> continentes, List<Boolean> capital,
			List<Double> conectividades, Map<List<String>, Integer> vuelosEntrantesConexion, 
			Map<String, Integer> vuelosSalientesAEspanya, List<Double> tasasAeropuertos, 
			Map<String, Integer> vuelosSalientes, List<Integer> vuelosSalientesDeOrigen, 
			List<List<String>> conexionesNombres, String dia_I, String dia_F, String mes_I, 
			String mes_F, String año_I, String año_F, String ruta) throws IOException {
		
		/*String fecha_I = "\"" + año_I + "-" + mes_I + "-" + dia_I + "\"";
		String fecha_F = "\"" + año_F + "-" + mes_F + "-" + dia_F + "\"";
		
		List<String[]> datosFichero = new ArrayList<>();
		String[] cabecera = new String[15];
		
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
		cabecera[11] = String.valueOf(Constantes.continentDBField);
		cabecera[12] = String.valueOf(Constantes.capitalDBField);
		cabecera[13] = String.valueOf(Constantes.origenNombreDBField);
		cabecera[14] = String.valueOf(Constantes.destinoNombreDBField);
		
		datosFichero.add(cabecera);
		
		try (Session session = sessionFor(database())) {
			var records = session.readTransaction(tx -> tx.run(""
				+ " match (cont:Continent)<-[:BELONGS_TO]-(c:Country)-[:INFLUENCE_ZONE]->(a:Airport)-[r1]->(ao:AirportOperationDay)-[r2]->(f:FLIGHT)-[r3]->(ao2:AirportOperationDay)<-[r4]-(a2:Airport) "
				+ " where a2.countryIso = 'ES' and a.countryIso <>'ES' and f.dateOfDeparture <= date("+ fecha_F +") and f.dateOfDeparture >= date("+ fecha_I +") and f.operator <> 'UNKNOWN' and f.seatsCapacity > 0 and a2.weightFee > 0.0 and f.passengers > 0" 
				+ " with cont,f,a,a2 order by a.iata, a2.iata "
				
				+ " call{ "
				+ " with a "
				+ " match (a)-[r1]->(ao:AirportOperationDay)-[r2]->(f:FLIGHT)-[r3]->   (ao2:AirportOperationDay)<-[r4]-(a2:Airport) "
				+ " where f.dateOfDeparture <= date("+ fecha_F +") and f.dateOfDeparture >= date("+ fecha_I +") and f.operator <> 'UNKNOWN' "
				+ " return count(*) as numVuelos "
				+ " } "
				
				+ " return f.flightIfinal as sir,f.passengers as pasajeros,f.operator as companyia,a.connectivity as conectividad,f.incomeFromTurism as dineroT,f.incomeFromBusiness as dineroN,f.destinationAirportIncome as tasas,a.iata as origen,a2.iata as destino, numVuelos as VuelosDesdeOrigen, a2.regionName as areaInf, cont.continentId as continente, a.capital as capital, a.airportName as origenNombre, a2.airportName as destinoNombre"
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
				var continente = record.get(Constantes.continentDBField).asString();
				var isCapital = record.get(Constantes.capitalDBField).asBoolean();
				var origenNombre = record.get(Constantes.origenNombreDBField).asString();
				var destinoNombre = record.get(Constantes.destinoNombreDBField).asString();
				
				if(!conexiones.contains(List.of(origen, destino))) {
                	conexiones.add(List.of(origen, destino));
                	conexionesNombres.add(List.of(origenNombre, destinoNombre));
                	vuelosEntrantesConexion.put(List.of(origen, destino), 1);
                    continentes.add(continente);
                    capital.add(isCapital);
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
                if(!vuelosSalientes.keySet().contains(origen)) {
                	vuelosSalientes.put(origen, VuelosDesdeOrigen);
                }
				
				conexionesTotal.add(List.of(origen, destino));
                riesgos.add(sir);
                pasajeros.add(Num_Pasajeros);
                dineroMedioT.add(dineroT);
                dineroMedioN.add(dineroN);
                tasasAeropuertos.add(tasas);
                companyias.add(companyia);
                areasInf.add(areaInf);
                
                String[] filaIFichero = new String[15];
                
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
                filaIFichero[11] = String.valueOf(continente);
                filaIFichero[12] = String.valueOf(isCapital);
                filaIFichero[13] = String.valueOf(origenNombre);
                filaIFichero[14] = String.valueOf(destinoNombre);
                
                datosFichero.add(filaIFichero);
			});
		}
		Utils.crearFicheroConDatosDiaI(datosFichero, ruta);*/
	}
	
	//Guarda el proyecto con los datos introducidos.
	//Se hacen validaciones de nombre y de fechas.
	public Respuesta guardarProyecto(String fecha_I, String fecha_F, int numIteraciones,
			int numIndividuos, double inertiaW, double c1, double c2, double m, double p,
			String nombreProyecto, PreferencesConstraints resPolPref) throws IOException, ParseException {
		DMPreferences preferencias = new DMPreferences(new ObjectivesOrder(resPolPref.getOrdenObj()), Constantes.nombreQDMPSR);
		preferencias.generateWeightsVector(resPolPref.getOrdenObj().size());
		
		if(numIteraciones == 0) {
			numIteraciones = Utils.getRandNumber(500, 30000);
		}
		
		BPSOParams params = new BPSOParams(numIndividuos, inertiaW, c1, c2, 
				numIteraciones, m, p, Constantes.nombreCPGenerica, 
				Constantes.nombreIWLinearDecreasing);
		
		File directoryPath = new File(Constantes.rutaFicherosProyectos);
	      
	    String contents[] = directoryPath.list();
	    int pos = 0;
	    boolean igual = false;
	    while(!igual && pos < contents.length) {
	    	if(contents[pos].equals(nombreProyecto)) {
	    		igual = true;
	    	}
	    	pos++;
	    }
	    
	    Respuesta resp = new Respuesta(false, null);
	    
		boolean fechasCorrectas = this.comprobarFechas(fecha_I, fecha_F);
	    
		if(igual || !fechasCorrectas) {
			if(igual && fechasCorrectas) {
				resp.setMensaje(Constantes.KO_respuestaNombresIguales);
			}
			if(!igual && !fechasCorrectas) {
				resp.setMensaje(Constantes.KO_respuestaNoFlights);
			}
			if(igual && !fechasCorrectas) {
				resp.setMensaje(Constantes.KO_respuestaNoNombresIgualesYNoFlights);
			}
		}else {
			Utils.crearDirectorioProyecto(nombreProyecto);
			Utils.crearDirectorioTemp(nombreProyecto);
			Utils.crearDirectorioFitness(nombreProyecto);
			Utils.crearDirectorioObjetivos(nombreProyecto);
			Utils.crearDirectorioRangos(nombreProyecto);
			
			Utils.crearCSVFechas(fecha_I, fecha_F, nombreProyecto);
			Utils.crearCSVParams(params, nombreProyecto);
			Utils.crearCSVPref(preferencias, nombreProyecto);
			Utils.crearCSVRestricciones(resPolPref.getRestricciones(), resPolPref.getPol(), nombreProyecto);
			resp.setOK_KO(true);
			resp.setMensaje(Constantes.OK_respuestaProjectSaved);
			
		}
		return resp;
	}
	
	public Respuesta guardarProyectoALT(String fecha_I, String fecha_F, int numIteraciones,
			int numIndividuos, double inertiaW, double c1, double c2, double m, double p,
			double resEpi, String nombreProyecto, PreferencesConstraints resPolPref) throws IOException, ParseException {
		DMPreferences preferencias = new DMPreferences(new ObjectivesOrder(resPolPref.getOrdenObj()), Constantes.nombreQDMPSR);
		preferencias.generateWeightsVector(resPolPref.getOrdenObj().size());
		
		if(numIteraciones == 0) {
			numIteraciones = Utils.getRandNumber(500, 30000);
		}
		
		BPSOParams params = new BPSOParams(numIndividuos, inertiaW, c1, c2, 
				numIteraciones, m, p, Constantes.nombreCPGenerica, 
				Constantes.nombreIWLinearDecreasing);
		
		File directoryPath = new File(Constantes.rutaFicherosProyectos);
	      
	    String contents[] = directoryPath.list();
	    int pos = 0;
	    boolean igual = false;
	    while(!igual && pos < contents.length) {
	    	if(contents[pos].equals(nombreProyecto)) {
	    		igual = true;
	    	}
	    	pos++;
	    }
	    
	    Respuesta resp = new Respuesta(false, null);
	    
		boolean fechasCorrectas = this.comprobarFechas(fecha_I, fecha_F);
	    
		if(igual || !fechasCorrectas) {
			if(igual && fechasCorrectas) {
				resp.setMensaje(Constantes.KO_respuestaNombresIguales);
			}
			if(!igual && !fechasCorrectas) {
				resp.setMensaje(Constantes.KO_respuestaNoFlights);
			}
			if(igual && !fechasCorrectas) {
				resp.setMensaje(Constantes.KO_respuestaNoNombresIgualesYNoFlights);
			}
		}else {
			/*Utils.crearDirectorioProyecto(nombreProyecto);
			Utils.crearDirectorioTemp(nombreProyecto);
			Utils.crearDirectorioFitness(nombreProyecto);
			Utils.crearDirectorioObjetivos(nombreProyecto);
			Utils.crearDirectorioRangos(nombreProyecto);
			
			Utils.crearCSVFechas(fecha_I, fecha_F, nombreProyecto);
			Utils.crearCSVParams(params, nombreProyecto);
			Utils.crearCSVPref(preferencias, nombreProyecto);
			Utils.crearCSVRestricciones(resEpi, resPolPref.getPol(), nombreProyecto);*/
			resp.setOK_KO(true);
			resp.setMensaje(Constantes.OK_respuestaProjectSaved);
			
		}
		return resp;
	}
	
	//Borra el proyecto indicado
	//Se comprueba si el proyecto se está ejecutando
	public Respuesta borrarProyecto(String nombre) throws FileNotFoundException, IOException, CsvException{
		Respuesta resp = new Respuesta(false, null);
		
		if(Utils.leerFicheroCola().contains(nombre)) {
			resp.setMensaje(Constantes.KO_respuestaProjectDeletedRunning);
		}else {
			resp.setOK_KO(Utils.borrarDirectorioProyecto(new File(Constantes.rutaFicherosProyectos + nombre)));
			resp.setMensaje(Constantes.OK_respuestaProjectDeleted);
		}
		
		return resp;
	}
	
	//Guarda el proyecto con los datos introducidos y se ejecuta
	//Se hacen validaciones de nombre, fechas y se comprueba si se está ejecutando
	public Respuesta guardarYOptimizar(String fecha_I, String fecha_F, int numIteraciones,
			int numIndividuos, double inertiaW, double c1, double c2, double m, double p,
			String nombreProyecto, PreferencesConstraints resPolPref) throws FileNotFoundException, IOException, CsvException, ParseException {
		
		DMPreferences preferencias = new DMPreferences(new ObjectivesOrder(resPolPref.getOrdenObj(), resPolPref.getRestricciones()), Constantes.nombreQDMPSR);
		preferencias.generateWeightsVector(resPolPref.getOrdenObj().size());
		
		numIteraciones = Utils.getRandNumber(5000, 5001);
		
		BPSOParams params = new BPSOParams(numIndividuos, inertiaW, c1, c2, 
				numIteraciones, m, p, Constantes.nombreCPGenerica, 
				Constantes.nombreIWLinearDecreasing);
		
		DataRRPS_PAT datos = this.obtenerDatosRRPS_PAT(fecha_I, fecha_F);
		
		Problem problema = new RRPS_PAT(datos, resPolPref.getPol(), preferencias);
		
		Respuesta resp = new Respuesta(false, null);
		
		if(problema.getNumVariables() > 0) {
			resp = this.guardarProyecto(fecha_I, fecha_F, numIteraciones, numIndividuos, 
					inertiaW, c1, c2, m, p, nombreProyecto, resPolPref);
		}else {
			resp.setMensaje(Constantes.KO_respuestaNoFlights);
		}
		
		if(resp.isOK_KO()) {
			if(Utils.leerFicheroCola().size() > 0) {
				resp.setOK_KO(false);
				resp.setMensaje(Constantes.KO_respuestaProjectRunning);
			}else {
				Utils.modificarFicheroCola(nombreProyecto);
				this.bpso = new BPSO(problema, params, nombreProyecto, new BPSOOpciones(false, 0));
				Particle ind = this.bpso.runBPSO();
				problema.devolverSolucionCompleta(ind);
				System.out.println(ind);
				Utils.modificarFicheroCola("");
				String fila = this.guardarNuevaSolucionRRPS_PAT(ind, datos, nombreProyecto);
				
				if(!Constantes.continueOpt) {
					Utils.crearDirectorioTempSolucion(nombreProyecto, fila);
					
					Utils.crearFicheroPoblacionSolucionITemp(nombreProyecto, fila, this.bpso.getparticlesPopulation());
					
					Utils.crearFicheroPbestsSolucionITemp(nombreProyecto, fila, this.bpso.getparticlesPopulation());
					
					Utils.crearFicheroV0SolucionITemp(nombreProyecto, fila, this.bpso.getV0());
					Utils.crearFicheroV1SolucionITemp(nombreProyecto, fila, this.bpso.getV1());
					
					//Utils.crearFicheroV0LSolucionITemp(nombreProyecto, fila, this.bpso.getV0L());
					//Utils.crearFicheroV1LSolucionITemp(nombreProyecto, fila, this.bpso.getV1L());
					
					Utils.crearFicheroParamsTemp(nombreProyecto, fila, params);
					
				}
				
				
				
				resp.setMensaje(Constantes.OK_respuestaProjectSavedRunning);
			}
			
		}
		Constantes.continueOpt = true;
		return resp;
		
		
	}
	
	public Respuesta guardarYOptimizarALT(String fecha_I, String fecha_F, int numIteraciones,
			int numIndividuos, double inertiaW, double c1, double c2, double m, double p,
			double resEpi, String nombreProyecto, PreferencesConstraints resPolPref) throws FileNotFoundException, IOException, CsvException, ParseException {
		
		DMPreferences preferencias = new DMPreferences(new ObjectivesOrder(resPolPref.getOrdenObj()), Constantes.nombreQDMPSR);
		preferencias.generateWeightsVector(resPolPref.getOrdenObj().size());
		
		numIteraciones = Utils.getRandNumber(500, 30000);
		
		BPSOParams params = new BPSOParams(numIndividuos, inertiaW, c1, c2, 
				numIteraciones, m, p, Constantes.nombreCPGenerica, 
				Constantes.nombreIWLinearDecreasing);
		
		DataRRPS_PAT datos = this.obtenerDatosRRPS_PAT(fecha_I, fecha_F);
		
		Problem problema = new RRPS_PAT_ALT(datos, Stream.of(0.50, 0.50).collect(Collectors.toList()), resPolPref.getPol(), preferencias);
		
		Respuesta resp = new Respuesta(false, null);
		
		if(problema.getNumVariables() > 0) {
			resp = this.guardarProyectoALT(fecha_I, fecha_F, numIteraciones, numIndividuos, 
					inertiaW, c1, c2, m, p, resEpi, nombreProyecto, resPolPref);
		}else {
			resp.setMensaje(Constantes.KO_respuestaNoFlights);
		}
		
		if(resp.isOK_KO()) {
			if(Utils.leerFicheroCola().size() > 0) {
				resp.setOK_KO(false);
				resp.setMensaje(Constantes.KO_respuestaProjectRunning);
			}else {
				Utils.modificarFicheroCola(nombreProyecto);
				this.bpso = new BPSO(problema, params, nombreProyecto, new BPSOOpciones(false, 0));
				Particle ind = this.bpso.runBPSO();
				problema.devolverSolucionCompleta(ind);
				System.out.println(ind);
				Utils.modificarFicheroCola("");
				//String fila = this.guardarNuevaSolucionRRPS_PAT(ind, datos, nombreProyecto);
				
				/*if(!Constantes.continueOpt) {
					Utils.crearDirectorioTempSolucion(nombreProyecto, fila);
					
					Utils.crearFicheroPoblacionSolucionITemp(nombreProyecto, fila, this.bpso.getparticlesPopulation());
					
					Utils.crearFicheroPbestsSolucionITemp(nombreProyecto, fila, this.bpso.getparticlesPopulation());
					
					Utils.crearFicheroV0SolucionITemp(nombreProyecto, fila, this.bpso.getV0());
					Utils.crearFicheroV1SolucionITemp(nombreProyecto, fila, this.bpso.getV1());
					
					Utils.crearFicheroV0LSolucionITemp(nombreProyecto, fila, this.bpso.getV0L());
					Utils.crearFicheroV1LSolucionITemp(nombreProyecto, fila, this.bpso.getV1L());
					
					Utils.crearFicheroParamsTemp(nombreProyecto, fila, params);
					
				}*/
				
				
				
				resp.setMensaje(Constantes.OK_respuestaProjectSavedRunning);
			}
			
		}
		Constantes.continueOpt = true;
		return resp;
		
		
	}
	
	//Se ejecuta el proyecto indicado
	//Se comprueba si se está ejecutando
	public Respuesta optimizar(String proyecto) throws IOException, CsvException, ParseException {
		DMPreferences preferencias = this.cargarPreferenciasProyecto(proyecto);
		
		BPSOParams params = this.cargarParametrosProyecto(proyecto);
		
		Map<String, String> fechas = this.cargarFechasProyecto(proyecto);
		
		Map<String, List<String>> res = this.cargarRestriccionesProyecto(proyecto);
		Map<Integer, Double> restricciones;
		
		restricciones = new HashMap<>();
		
		for(String key : res.keySet()) {
			if(!key.equals(Constantes.nombreRestriccionPolitica)) {
				restricciones.put(Integer.valueOf(key), Double.valueOf(res.get(key).get(0)));
			}
		}
		
		preferencias.getOrder().setRestricciones(restricciones);
		
		DataRRPS_PAT datos = this.obtenerDatosRRPS_PAT(fechas.get(Constantes.nombreFechaInicial), fechas.get(Constantes.nombreFechaFinal));;
		
		Problem problema = new RRPS_PAT(datos, res.get(Constantes.nombreRestriccionPolitica), preferencias);
		
		Respuesta resp = new Respuesta(false, null);
		
		if(Utils.leerFicheroCola().size() > 0) {
			resp.setMensaje(Constantes.KO_respuestaProjectRunning);
		}else {
			Utils.modificarFicheroCola(proyecto);
			this.bpso = new BPSO(problema, params, proyecto, new BPSOOpciones(false, 0));
			Particle ind = this.bpso.runBPSO();
			problema.devolverSolucionCompleta(ind);
			System.out.println(ind);
			Utils.modificarFicheroCola("");
			String fila = this.guardarNuevaSolucionRRPS_PAT(ind, datos, proyecto);
			
			if(!Constantes.continueOpt) {
				Utils.crearDirectorioTempSolucion(proyecto, fila);
				
				
				Utils.crearFicheroPoblacionSolucionITemp(proyecto, fila, this.bpso.getparticlesPopulation());
				
				Utils.crearFicheroPbestsSolucionITemp(proyecto, fila, this.bpso.getparticlesPopulation());
				
				Utils.crearFicheroV0SolucionITemp(proyecto, fila, this.bpso.getV0());
				Utils.crearFicheroV1SolucionITemp(proyecto, fila, this.bpso.getV1());
				
				//Utils.crearFicheroV0LSolucionITemp(proyecto, fila, this.bpso.getV0L());
				//Utils.crearFicheroV1LSolucionITemp(proyecto, fila, this.bpso.getV1L());
				
				Utils.crearFicheroParamsTemp(proyecto, fila, params);
				
			}
			
			resp.setOK_KO(true);
			resp.setMensaje(proyecto);
		}
		
		Constantes.continueOpt = true;
		return resp;
	}
	
	//Se continua una optimización pausada.
	//Se comprueba si se está ejecutando
	public Respuesta continuarOpt(String proyecto, int id) throws IOException, CsvException, ParseException {
		DMPreferences preferencias = this.cargarPreferenciasProyecto(proyecto);
		
		BPSOParams params = this.cargarParametrosProyecto(proyecto, id);
		
		Map<String, String> fechas = this.cargarFechasProyecto(proyecto);
		
		Map<String, List<String>> res = this.cargarRestriccionesProyecto(proyecto);
		
		DataRRPS_PAT datos = this.obtenerDatosRRPS_PAT(fechas.get(Constantes.nombreFechaInicial), fechas.get(Constantes.nombreFechaFinal));
		
		Map<Integer, Double> restricciones;
		
		restricciones = new HashMap<>();
		
		for(String key : res.keySet()) {
			if(!key.equals(Constantes.nombreRestriccionPolitica)) {
				restricciones.put(Integer.valueOf(key), Double.valueOf(res.get(key).get(0)));
			}
		}
		
		preferencias.getOrder().setRestricciones(restricciones);

		Problem problema = new RRPS_PAT(datos, res.get(Constantes.nombreRestriccionPolitica), preferencias);
		
		Respuesta resp = new Respuesta(false, null);
		
		if(Utils.leerFicheroCola().size() > 0) {
			resp.setMensaje(Constantes.KO_respuestaProjectRunning);
		}else {
			Utils.modificarFicheroCola(proyecto);
			this.bpso = new BPSO(problema, params, proyecto, new BPSOOpciones(true, id));
			Particle ind = this.bpso.runBPSO();
			problema.devolverSolucionCompleta(ind);
			System.out.println(ind);
			Utils.modificarFicheroCola("");
			String fila = this.guardarNuevaSolucionRRPS_PAT(ind, datos, proyecto);
			
			if(!Constantes.continueOpt) {
				Utils.crearDirectorioTempSolucion(proyecto, fila);
				
				
				Utils.crearFicheroPoblacionSolucionITemp(proyecto, fila, this.bpso.getparticlesPopulation());
				
				Utils.crearFicheroPbestsSolucionITemp(proyecto, fila, this.bpso.getparticlesPopulation());
				
				Utils.crearFicheroV0SolucionITemp(proyecto, fila, this.bpso.getV0());
				Utils.crearFicheroV1SolucionITemp(proyecto, fila, this.bpso.getV1());
				
				//Utils.crearFicheroV0LSolucionITemp(proyecto, fila, this.bpso.getV0L());
				//Utils.crearFicheroV1LSolucionITemp(proyecto, fila, this.bpso.getV1L());
				
				Utils.crearFicheroParamsTemp(proyecto, fila, params);
				
			}
			
			resp.setOK_KO(true);
			resp.setMensaje(proyecto);
		}
		
		Constantes.continueOpt = true;
		return resp;
	}
	
	//Borra la solucion indicada
	public Respuesta borrarSolucion(String proyecto, int id) throws IOException, CsvException {
		Respuesta resp = new Respuesta(false, null);
		
		Utils.borrarSolucionCSVproblemaRRPS_PAT(proyecto, id);
		Utils.borrarCSVObjetivoI(proyecto, id);
		Utils.borrarCSVFitnessI(proyecto, id);
		Utils.borrarCSVRangosI(proyecto, id);
		Utils.borrarDirectorioTempSolucionI(new File(Constantes.rutaFicherosProyectos + proyecto + "//" + Constantes.nombreDirectorioTemp + "//" + String.valueOf(id)), proyecto, id);
		
		resp.setOK_KO(true);
		resp.setMensaje(Constantes.OK_respuestaSolutionDeleted);
		return resp;
	}
	
	/*public boolean optimizarALT(String proyecto) throws IOException, CsvException, ParseException {
		DMPreferences preferencias = this.cargarPreferenciasProyecto(proyecto);
		
		BPSOParams params = this.cargarParametrosProyecto(proyecto);
		
		Map<String, String> fechas = this.cargarFechasProyecto(proyecto);
		
		Map<String, List<String>> res = this.cargarRestriccionesProyecto(proyecto);
		
		DatosRRPS_PAT datos = this.obtenerDatosRRPS_PAT(fechas.get(Constantes.nombreFechaInicial), fechas.get(Constantes.nombreFechaFinal));
		
		Problema problema = new RRPS_PAT(datos, Double.valueOf(res.get(Constantes.nombreRestriccionEpidemiologica).get(0)), res.get(Constantes.nombreRestriccionPolitica), preferencias);
		
		BPSO bpso = new BPSO(problema, params, proyecto);
		Individuo ind = bpso.ejecutarBPSOALT();
		problema.devolverSolucionCompleta(ind);
		System.out.println(ind);
			
		this.guardarNuevaSolucionRRPS_PAT(ind, datos, proyecto);
		return true;
	}*/
	
	//Guarda la solucion nueva asociada al proyecto ejecutado
	public String guardarNuevaSolucionRRPS_PAT(Particle ind, DataRRPS_PAT datos, String nombre) throws IOException, CsvException {
		String fila = Utils.modificarCSVproblemaRRPS_PAT(ind, datos, nombre);
		Utils.crearCSVConFitnessPorIteracion(ind.getFitnessHist(), fila, nombre);
		Utils.crearCSVObjetivos(ind.getObjectivesNorm(), ind.getConstraints(), fila, nombre);
		Utils.crearCSVHistogramas(ind.getExtra().get(Constantes.nombreCampoPasajerosPerdidosPorCompañía), 
				ind.getExtra().get(Constantes.nombreCampoIngresoPerdidoPorAreaInf), 
				ind.getExtra().get(Constantes.nombreCampoIngresoPerdidoPorAerDest), fila, nombre);
		return fila;
	}
	
	//Se obtienen los proyectos guardados
	public List<Proyecto> obtenerListaProyectos() throws IOException, CsvException{
		List<String> proyectoEjecutando = Utils.leerFicheroCola();
		File directoryPath = new File(Constantes.rutaFicherosProyectos);
		String contents[] = directoryPath.list();
		List<Proyecto> proyectos = new ArrayList<>();
		for(String nombre : contents) {
			Proyecto proyecto = new Proyecto(nombre, (proyectoEjecutando.size() > 0 && proyectoEjecutando.get(0).equals(nombre)),
					Utils.leerCSVFechasSalida(nombre), 
					Utils.leerCSVParamsSalida(nombre), 
					Utils.leerCSVPrefSalida(nombre),
					Utils.leerCSVRestriccionesSalida(nombre));
			proyectos.add(proyecto);
		}
		return proyectos;
	}
	
	//Se obtienen los datos del proyecto indicado
	public Proyecto obtenerProyecto(String nombre) throws FileNotFoundException, IOException, CsvException {
		List<String> proyectoEjecutando = Utils.leerFicheroCola();
		Proyecto proyecto = new Proyecto(nombre, (proyectoEjecutando.size() > 0 && proyectoEjecutando.get(0).equals(nombre)),
		Utils.leerCSVFechasSalida(nombre), 
		Utils.leerCSVParamsSalida(nombre), 
		Utils.leerCSVPrefSalida(nombre),
		Utils.leerCSVRestriccionesSalida(nombre));
		
		return proyecto;
	}
	
	//Se obtienen las soluciones del proyecto indicado
	public List<Solucion> obtenerListaSolucionesProyectoI(String nombre) throws IOException, CsvException{
		File directoryPath = new File(Constantes.rutaFicherosProyectos + "//" + nombre + "//" + Constantes.nombreDirectorioFicherosObjetivos);
		String contents[] = directoryPath.list();
		
		List<Solucion> soluciones = new ArrayList<>();
		for(String id : contents) {
			List<Double> fit = Utils.leerCSVConFitnessPorIteracionSalida(id.replace(Constantes.extensionFichero, ""), nombre);
			
			List<Double> obj = Utils.leerCSVObjetivosSalida(id.replace(Constantes.extensionFichero, ""), nombre);
			
			Solucion sol = new Solucion(Integer.valueOf(id.replace(Constantes.extensionFichero, "")), (int)Math.round(fit.get(0))+1, fit.get(1), obj, this.cargarPreferenciasProyecto(nombre).getOrder());
			
			
			File directoryPath2 = new File(Constantes.rutaFicherosProyectos + "//" + nombre + "//" + Constantes.nombreDirectorioTemp);
			String contentsSol[] = directoryPath2.list();
			
			for(String temp : contentsSol) {
				if(temp.equals(id.replace(Constantes.extensionFichero, ""))) {
					sol.setTemporal(true);
				}
			}
			
			soluciones.add(sol);
		}
		
		return TraducirSalida.ordenarSoluciones(soluciones);
	}
	
	//Se obtienen los datos de las conexiones de la solución y proyecto indicados en el dia k
	public DatosConexiones cargarProyectoISolucionJDiaK(String proyecto, int id, int dia) throws FileNotFoundException, IOException, CsvException, ParseException {
		Map<String, String> fechas = this.cargarFechasProyecto(proyecto);
		DataRRPS_PAT datos = this.obtenerDatosRRPS_PAT(fechas.get(Constantes.nombreFechaInicial), fechas.get(Constantes.nombreFechaFinal));
		
		List<Aeropuerto> lista = Utils.obtenerSolucionDiaI(proyecto, id, dia);
		List<Double> bits = Utils.obtenerBitsSolDiaI(proyecto, id, dia);
		
		datos.getDatosPorDia().get(dia).calcularDatosJuntos();
		
		fechas.put(Constantes.nombreFechaActual, this.calcularFecha(fechas.get(Constantes.nombreFechaInicial), dia));
		
		DatosConexiones datosConexiones = new DatosConexiones(lista, bits, datos.getDatosPorDia().get(dia), fechas);
		
		return datosConexiones;
	}
	
	//Se obtienen los datos de las conexiones de la solución y proyecto indicados en el dia k
	//Se aplica un filtro en base a la IATA indicada 
	public DatosConexiones cargarProyectoISolucionJDiaKFiltro(String proyecto, int id,
			int dia, String con) throws FileNotFoundException, IOException, CsvException, ParseException {
		DatosConexiones datosConexiones = this.cargarProyectoISolucionJDiaK(proyecto, id, dia);
		datosConexiones.aplicarFiltro(con);
		
		return datosConexiones;
	}
	
	//Se obtienen los datos temporales de las conexiones de la solución del proyecto ejecutandose indicados en el dia k
	public DatosConexiones obtenerSnapshot(String proyecto, int dia) throws IOException, CsvException, ParseException {
		
		Map<String, String> fechas = this.cargarFechasProyecto(proyecto);
		DataRRPS_PAT datos = this.obtenerDatosRRPS_PAT(fechas.get(Constantes.nombreFechaInicial), fechas.get(Constantes.nombreFechaFinal));
		datos.getDatosPorDia().get(dia).calcularDatosJuntos();
		fechas.put(Constantes.nombreFechaActual, this.calcularFecha(fechas.get(Constantes.nombreFechaInicial), dia));
		
		DatosConexiones datosConexiones;
		
		if(Utils.leerFicheroCola().contains(proyecto) && this.bpso != null) {
			System.out.println(this.bpso.getGbest());
			Particle GBest = Utils.copiarIndividuo(this.bpso.getGbest());
			
			GBest.initExtra();
			
			this.bpso.getproblem().extra(GBest);
			
			Utils.formatearIndividuo(GBest);
			
			GBest = this.bpso.getproblem().devolverSolucionCompleta(GBest);
			
			
			
			
			List<Aeropuerto> lista = Utils.obtenerSolucionDiaI(dia, GBest.getVariables(), datos);
			List<Double> bits = Utils.obtenerBitsSolDiaI(dia, GBest.getVariables(), datos);
			
			
			
			
			
			datosConexiones = new DatosConexiones(lista, bits, datos.getDatosPorDia().get(dia), fechas);
			datosConexiones.setGBest(GBest);
			datosConexiones.setExtraSnapshot(Utils.obtenerRangosSnapshot(
					GBest.getExtra().get(Constantes.nombreCampoPasajerosPerdidosPorCompañía), 
					GBest.getExtra().get(Constantes.nombreCampoIngresoPerdidoPorAreaInf), 
					GBest.getExtra().get(Constantes.nombreCampoIngresoPerdidoPorAerDest)));
		}else {
			int numSol = this.obtenerListaSolucionesProyectoI(proyecto).size();
			datosConexiones = this.cargarProyectoISolucionJDiaK(proyecto, numSol-1, dia);
			List<Objetivo> obj = this.obtenerObjSolucionI(proyecto, numSol-1);
			List<FitnessI> fit = this.obtenerHistSolucionI(proyecto, numSol-1);
			Rangos rangos = this.obtenerRangosSolucionI(proyecto, numSol-1);
			Map<String, List<String>> rangosSnapshot = new HashMap<>();
			rangosSnapshot.put(Constantes.nombreCampoPasajerosPerdidosPorCompañía, rangos.getListaPasajerosPerdidosPorCompanyia());
			rangosSnapshot.put(Constantes.nombreCampoIngresoPerdidoPorAreaInf, rangos.getListaIngresoPerdidoPorAreaInf());
			rangosSnapshot.put(Constantes.nombreCampoIngresoPerdidoPorAerDest, rangos.listaIngresoPerdidoPorAerDest);
			
			datosConexiones.setExtraSnapshot(rangosSnapshot);
			
			Particle ind = Utils.crearIndividuoConAtributos(obj, fit);
			
			datosConexiones.setGBest(ind);
		}
		
		
		
		return datosConexiones;
	}

	//Se obtienen los datos temporales de las conexiones de la solución del proyecto ejecutandose indicados en el dia k
	//Se aplica un filtro en base a la IATA indicada 
	public DatosConexiones obtenerSnapshotFiltroK(String proyecto, int dia, 
			String con) throws IOException, CsvException, ParseException {
		
		DatosConexiones snapshotFiltro = this.obtenerSnapshot(proyecto, dia);
		snapshotFiltro.aplicarFiltro(con);
		
		return snapshotFiltro;
	}

	//Se obtiene el número de dias que se consideran en la solucion indicada
	public int numDiasSolucionI(String proyecto, int id) throws FileNotFoundException, IOException, CsvException, ParseException {
		return Utils.obtenernumDiasSolucionI(proyecto, id);
	}
	
	//Se obtiene el número de dias de un proyecto que se está ejecutando
	public int numDiasSolucionISnapshot(String proyecto) throws FileNotFoundException, IOException, CsvException, ParseException {
		Map<String, String> fechas = this.cargarFechasProyecto(proyecto);
		DataRRPS_PAT datos = this.obtenerDatosRRPS_PAT(fechas.get(Constantes.nombreFechaInicial), fechas.get(Constantes.nombreFechaFinal));
		return datos.getNumDias();
	}
	
	//Se obtiene el histórico de fitness de una solucion de un proyecto indicados
	public List<FitnessI> obtenerHistSolucionI(String proyecto, int id) throws FileNotFoundException, IOException, CsvException, ParseException {
		return TraducirSalida.obtenerHistoricoDeFitness(Utils.leerCSVHistFitness(proyecto, String.valueOf(id)));
	}
	
	//Se obtiene los valores de los objetivos optimizados de una solucion de un proyecto indicados
	public List<Objetivo> obtenerObjSolucionI(String proyecto, int id) throws FileNotFoundException, IOException, CsvException{
		return TraducirSalida.obtenerObjetivos(Utils.leerCSVObjetivos(proyecto, String.valueOf(id)), this.cargarPreferenciasProyecto(proyecto).getOrder());
	}
	
	//Se obtienen los histogramas de una solucion de un proyecto indicados
	//Los histogramas son en relacion a:
	//Pasajeros perdidos por compañia
	//Ingreso perdido por area de influencia
	//Ingreso perdido por aeropuerto destino
	public Histogramas obtenerHistogramasSolucionI(String proyecto, int id) throws FileNotFoundException, IOException, CsvException{
		return TraducirSalida.obtenerHistogramas(Utils.leerCSVHistogramas(proyecto, String.valueOf(id)));
	}
	
	//Se obtienen los valores minimos y maximos de una solucion de un proyecto indicados
	//Los valores minimos y maximos son en relacion a:
	//Pasajeros perdidos por compañia
	//Ingreso perdido por area de influencia
	//Ingreso perdido por aeropuerto destino
	public Rangos obtenerRangosSolucionI(String proyecto, int id) throws FileNotFoundException, IOException, CsvException{
		return TraducirSalida.obtenerRangos(Utils.obtenerRangos(proyecto, String.valueOf(id)));
	}
	
	//Se obtienen los tooltips asociados a las fechas indicadas
	public TooltipTexts obtenerTooltips(String fecha_I, String fecha_F) throws ParseException, IOException {
		if(comprobarFechas(fecha_I, fecha_F)) {
			return TraducirSalida.obtenerTooltips(Utils.obtenerTooltips(this.obtenerDatosRRPS_PAT(fecha_I, fecha_F)));
		}else {
			return TraducirSalida.obtenerTooltipsDefault();
		}
		
	}
	
	//Se obtiene el orden de prioridad que se ha indicado para los objetivos del proyecto guardado
	public DMPreferences cargarPreferenciasProyecto(String nombre) throws IOException, CsvException {
		ObjectivesOrder orden = Utils.leerCSVPref(nombre);
		DMPreferences pref = new DMPreferences(orden, Constantes.nombreQDMPSR);
		pref.generateWeightsVector(orden.getOrder().size());
		return pref;
	}
	
	//Se obtienen los parámetros de la metaheurística del proyecto indicado
	public BPSOParams cargarParametrosProyecto(String nombre) throws IOException, CsvException {
		BPSOParams params = Utils.leerCSVParams(nombre);
		return params;
	}
	
	//Se obtienen los parámetros de la metaheurística del proyecto pausado indicado
	public BPSOParams cargarParametrosProyecto(String nombre, int id) throws IOException, CsvException {
		BPSOParams params = Utils.leerCSVParamsTemp(nombre, id);
		return params;
	}
	
	//Se obtienen las fechas consideradas en el proyecto indicado
	public Map<String, String> cargarFechasProyecto(String nombre) throws IOException, CsvException{
		return Utils.leerCSVFechas(nombre);
	}
	
	//Se le suma a la fecha inicial el numero de dias indicados
	public String calcularFecha(String fechaInicial, int dia) throws ParseException {
		Date fechaInicio = Constantes.formatoFechaRRPS_PAT.parse(fechaInicial);
		Calendar c = Calendar.getInstance();
	    c.setTime(fechaInicio);
	    c.add(Calendar.DATE, dia);
	    
	    return this.formatearFecha(c);
	}
	
	//Se devuelve la fecha en formato AAAA-MM-dd
	public String formatearFecha(Calendar c) {
		String dia_Inicial = String.valueOf(c.get(Calendar.DATE));
	    if(dia_Inicial.length() == 1) {
	    	dia_Inicial = "0" + dia_Inicial;
	    }
	    
	    String mes_Inicial = String.valueOf(c.get(Calendar.MONTH)+1);
	    if(mes_Inicial.length() == 1) {
	    	mes_Inicial = "0" + mes_Inicial;
	    }
	    
		String año_Inicial = String.valueOf(c.get(Calendar.YEAR));
		
		return año_Inicial + "-" + mes_Inicial + "-" + dia_Inicial;
	}
	
	//Se obtienen las restricciones epidemiológicas o politicas
	public Map<String, List<String>> cargarRestriccionesProyecto(String nombre) throws IOException, CsvException{
		return Utils.leerCSVRestricciones(nombre);
	}
	
	//Se comprueba el usuario y contraseña para el login
	public boolean comprobarUsuario(Usuario usuario) throws IOException, CsvException {
		Map<String, String> usuarios = Utils.leerCSVUsuarios();
		
		for(String username : usuarios.keySet()) {
			if(username.equals(usuario.getUsuario())) {
				if(usuarios.get(username).equals(usuario.getPass())) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	//Se pausa la optimización poniendo la variable estatica continueOpt a falso
	public Respuesta pausarOpt() {
		Constantes.continueOpt = false;
		
        return new Respuesta(true, Constantes.OK_respuestaOptimizationCancelled);
	}
	
	private boolean comprobarFechas(String fecha_I, String fecha_F) throws ParseException {
		Date fechaInicio = Constantes.formatoFechaRRPS_PAT.parse(fecha_I);
		Date fechaFinal = Constantes.formatoFechaRRPS_PAT.parse(fecha_F);
		
		Date fechaSDMin = Constantes.formatoFechaRRPS_PAT.parse(Constantes.fechaSDMin);
		Date fechaSDMax = Constantes.formatoFechaRRPS_PAT.parse(Constantes.fechaSDMax);
		
		boolean fechasCorrectas;
		
		if(fechaInicio.compareTo(fechaSDMin) >= 0 && fechaSDMax.compareTo(fechaInicio) >= 0 && fechaFinal.compareTo(fechaSDMax) > 0) {
			fechasCorrectas = false;
		}else if(fechaSDMin.compareTo(fechaInicio) > 0 && fechaFinal.compareTo(fechaSDMin) >= 0 && fechaSDMax.compareTo(fechaFinal) >= 0) {
			fechasCorrectas = false;
		}else if(fechaInicio.compareTo(fechaSDMin) >= 0 && fechaSDMax.compareTo(fechaFinal) >= 0) {
			fechasCorrectas = false;
		}else if(fechaSDMin.compareTo(fechaInicio) >= 0 && fechaFinal.compareTo(fechaSDMax) >= 0) {
			fechasCorrectas = false;
		}else {
			fechasCorrectas = true;
		}
		return fechasCorrectas;
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
