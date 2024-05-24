package com.VisNeo4j.App.Service;

import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.neo4j.driver.SessionConfig;
import org.springframework.data.neo4j.core.DatabaseSelectionProvider;
import org.springframework.stereotype.Service;

import com.VisNeo4j.App.Algoritmo.Parametros.BPSOParams;
import com.VisNeo4j.App.Constantes.Constantes;
import com.VisNeo4j.App.Lectura.LecturaDeDatos;
import com.VisNeo4j.App.Modelo.Individuo;
import com.VisNeo4j.App.Modelo.Entrada.Usuario;
import com.VisNeo4j.App.Modelo.Salida.Objetivo;
import com.VisNeo4j.App.Modelo.Salida.Proyecto;
import com.VisNeo4j.App.Modelo.Salida.Solucion;
import com.VisNeo4j.App.Modelo.Salida.TraducirSalida;
import com.VisNeo4j.App.Problemas.RRPS_PAT;
import com.VisNeo4j.App.Problemas.Datos.DatosProblema;
import com.VisNeo4j.App.Problemas.Datos.DatosProblemaDias;
import com.VisNeo4j.App.Problemas.Datos.DatosRRPS_PAT;
import com.VisNeo4j.App.Problemas.Datos.DatosRRPS_PATDiaI;
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


@Service
public class VisNeo4jService {

	private final Driver driver;

	private final DatabaseSelectionProvider databaseSelectionProvider;

	VisNeo4jService(Driver driver,
				 DatabaseSelectionProvider databaseSelectionProvider) {

		this.driver = driver;
		this.databaseSelectionProvider = databaseSelectionProvider;
	}
	
	public DatosRRPS_PAT obtenerDatosRRPS_PAT(String fecha_I, String fecha_F) throws ParseException, IOException {
		List<DatosRRPS_PATDiaI> datosPorDia = new ArrayList<>();
		
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
	    
	    
		return new DatosRRPS_PAT(numDias+1, Constantes.formatoFechaRRPS_PAT.format(fechaInicio), 
				Constantes.formatoFechaRRPS_PAT.format(fechaFinal), datosPorDia);
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
		List<String> areasInf = new ArrayList<>();
		List<String> continentes = new ArrayList<>();
		List<Boolean> capital = new ArrayList<>();
		Map<String, Integer> vuelosSalientes = new HashMap<>();
		Map<List<String>, Integer> vuelosEntrantesConexion = new HashMap<>();
		Map<String, Integer> vuelosSalientesAEspanya = new HashMap<>();
		List<Double> conectividades = new ArrayList<>();
		List<Double> tasasAeropuertos = new ArrayList<>();
		List<Integer> vuelosSalientesDeOrigen = new ArrayList<>();
		
		File file = new File(Constantes.rutaDatosPorDia + ruta + Constantes.extensionFichero);
		
		if (file.exists()) {
			LecturaDeDatos.leerDatosRRPS_PATDiaI(ruta, riesgos, conexiones, conexionesTotal, 
					pasajeros, dineroMedioT, dineroMedioN, companyias, areasInf, continentes, 
					capital, conectividades,  
					vuelosEntrantesConexion, vuelosSalientesAEspanya, tasasAeropuertos, 
					vuelosSalientes, vuelosSalientesDeOrigen);
		}
        else {
        	obtenerDatosRRPS_PAT_BBDD_DiaI(riesgos, conexiones, conexionesTotal, pasajeros, 
        			dineroMedioT, dineroMedioN, companyias, areasInf, continentes,
        			capital, conectividades, 
        			vuelosEntrantesConexion, vuelosSalientesAEspanya, tasasAeropuertos, 
        			vuelosSalientes, vuelosSalientesDeOrigen, 
        			dia_I, dia_F, mes_I, mes_F, año_I, año_F, ruta);
        }
		
		return new DatosRRPS_PATDiaI(riesgos, conexiones, conexionesTotal, pasajeros, 
				dineroMedioT, dineroMedioN, companyias, areasInf, continentes, capital, 
				vuelosSalientes, vuelosEntrantesConexion, vuelosSalientesAEspanya,  
				conectividades, tasasAeropuertos, vuelosSalientesDeOrigen);
	}
	
	public void obtenerDatosRRPS_PAT_BBDD_DiaI(List<Double> riesgos, List<List<String>> conexiones,
			List<List<String>> conexionesTotal, List<Integer> pasajeros, List<Double> dineroMedioT, 
			List<Double> dineroMedioN, List<String> companyias, 
			List<String> areasInf, List<String> continentes, List<Boolean> capital,
			List<Double> conectividades, Map<List<String>, Integer> vuelosEntrantesConexion, 
			Map<String, Integer> vuelosSalientesAEspanya, List<Double> tasasAeropuertos, 
			Map<String, Integer> vuelosSalientes, List<Integer> vuelosSalientesDeOrigen, 
			String dia_I, String dia_F, String mes_I, 
			String mes_F, String año_I, String año_F, String ruta) throws IOException {
		
		String fecha_I = "\"" + año_I + "-" + mes_I + "-" + dia_I + "\"";
		String fecha_F = "\"" + año_F + "-" + mes_F + "-" + dia_F + "\"";
		
		List<String[]> datosFichero = new ArrayList<>();
		String[] cabecera = new String[13];
		
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
		cabecera[11] = String.valueOf(Constantes.capitalDBField);
		
		datosFichero.add(cabecera);
		
		try (Session session = sessionFor(database())) {
			var records = session.readTransaction(tx -> tx.run(""
				+ " match (cont:Continent)<-[:BELONGS_TO]-(c:Country)-[:INFLUENCE_ZONE]->(a:Airport)-[r1]->(ao:AirportOperationDay)-[r2]->(f:FLIGHT)-[r3]->(ao2:AirportOperationDay)<-[r4]-(a2:Airport) "
				+ " where a2.countryIso = 'ES' and a.countryIso <>'ES' and f.dateOfDeparture <= date("+ fecha_F +") and f.dateOfDeparture >= date("+ fecha_I +") and f.operator <> 'UNKNOWN' and f.seatsCapacity > 0 and a2.weightFee > 0.0" 
				+ " with cont,f,a,a2 order by a.iata, a2.iata "
				
				+ " call{ "
				+ " with a "
				+ " match (a)-[r1]->(ao:AirportOperationDay)-[r2]->(f:FLIGHT)-[r3]->   (ao2:AirportOperationDay)<-[r4]-(a2:Airport) "
				+ " where f.dateOfDeparture <= date("+ fecha_F +") and f.dateOfDeparture >= date("+ fecha_I +") and f.operator <> 'UNKNOWN' "
				+ " return count(*) as numVuelos "
				+ " } "
				
				+ " return f.flightIfinal as sir,f.passengers as pasajeros,f.operator as companyia,a.connectivity as conectividad,f.incomeFromTurism as dineroT,f.incomeFromBusiness as dineroN,f.destinationAirportIncome as tasas,a.iata as origen,a2.iata as destino, numVuelos as VuelosDesdeOrigen, a2.regionName as areaInf, cont.continentId as continente, a.capital as capital"
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
				
				if(!conexiones.contains(List.of(origen, destino))) {
                	conexiones.add(List.of(origen, destino));
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
                
                String[] filaIFichero = new String[13];
                
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
                
                datosFichero.add(filaIFichero);
			});
		}
		Utils.crearFicheroConDatosDiaI(datosFichero, ruta);
	}
	
	public boolean guardarNuevoproyecto(String nombre, BPSOParams params, DMPreferences preferencias, 
			String fecha_I, String fecha_F, double resEpi, List<String> resPol) throws IOException {
		//Comprobar que el nombre no está duplicado
		File directoryPath = new File(Constantes.rutaFicherosProyectos);
	      //List of all files and directories
	    String contents[] = directoryPath.list();
	    int pos = 0;
	    boolean igual = false;
	    while(!igual && pos < contents.length) {
	    	if(contents[pos].equals(nombre)) {
	    		igual = true;
	    	}
	    	pos++;
	    }
	    
		if(igual) {
			return false;
		}else {
			Utils.crearDirectorioProyecto(nombre);
			
			Utils.crearDirectorioFitness(nombre);
			Utils.crearDirectorioObjetivos(nombre);
			//Guardar preferencias y parametros
			Utils.crearCSVFechas(fecha_I, fecha_F, nombre);
			Utils.crearCSVParams(params, nombre);
			Utils.crearCSVPref(preferencias, nombre);
			Utils.crearCSVRestricciones(resEpi, resPol, nombre);
			return true;
		}
	}
	
	public void guardarNuevaSolucionRRPS_PAT(Individuo ind, DatosRRPS_PAT datos, String nombre) throws IOException, CsvException {
		String fila = Utils.modificarCSVproblemaRRPS_PAT(ind, datos, nombre);
		Utils.crearCSVConFitnessPorIteracion(ind.getFitnessHist(), fila, nombre);
		Utils.crearCSVObjetivos(ind.getObjetivosNorm(), ind.getRestricciones(), fila, nombre);
	}
	
	public List<Proyecto> obtenerListaProyectos() throws IOException, CsvException{
		File directoryPath = new File(Constantes.rutaFicherosProyectos);
		String contents[] = directoryPath.list();
		List<Proyecto> proyectos = new ArrayList<>();
		for(String nombre : contents) {
			Proyecto proyecto = new Proyecto(nombre, 
					Utils.leerCSVFechasSalida(nombre), 
					Utils.leerCSVParamsSalida(nombre), 
					Utils.leerCSVPrefSalida(nombre),
					Utils.leerCSVRestriccionesSalida(nombre));
			proyectos.add(proyecto);
		}
		return proyectos;
	}
	
	public List<Solucion> obtenerListaSolucionesProyectoI(String nombre) throws IOException, CsvException{
		File directoryPath = new File(Constantes.rutaFicherosProyectos + "\\" + nombre + "\\" + Constantes.nombreDirectorioFicherosObjetivos);
		String contents[] = directoryPath.list();
		
		List<Solucion> soluciones = new ArrayList<>();
		for(String id : contents) {
			//Obtener fitness
			List<Double> fit = Utils.leerCSVConFitnessPorIteracionSalida(id.replace(Constantes.extensionFichero, ""), nombre);
			
			//Obtener obj y res
			List<Double> obj = Utils.leerCSVObjetivosSalida(id.replace(Constantes.extensionFichero, ""), nombre);
			
			Solucion sol = new Solucion(Integer.valueOf(id.replace(Constantes.extensionFichero, "")), (int)Math.round(fit.get(0))+1, fit.get(1), obj, this.cargarPreferenciasProyecto(nombre).getOrder());
			soluciones.add(sol);
		}
		
		return soluciones;
	}
	
	public List<Objetivo> obtenerObjSolucionI(String proyecto, int id) throws FileNotFoundException, IOException, CsvException{
		return TraducirSalida.obtenerObjetivos(Utils.leerCSVObjetivos(proyecto, String.valueOf(id)), this.cargarPreferenciasProyecto(proyecto).getOrder());
	}
	
	public DMPreferences cargarPreferenciasProyecto(String nombre) throws IOException, CsvException {
		ObjectivesOrder orden = Utils.leerCSVPref(nombre);
		DMPreferences pref = new DMPreferences(orden, Constantes.nombreQDMPSR);
		pref.generateWeightsVector(orden.getOrder().size());
		return pref;
	}
	
	public BPSOParams cargarParametrosProyecto(String nombre) throws IOException, CsvException {
		BPSOParams params = Utils.leerCSVParams(nombre);
		return params;
	}
	
	public Map<String, String> cargarFechasProyecto(String nombre) throws IOException, CsvException{
		return Utils.leerCSVFechas(nombre);
	}
	
	public String calcularFecha(String fechaInicial, int dia) throws ParseException {
		Date fechaInicio = Constantes.formatoFechaRRPS_PAT.parse(fechaInicial);
		Calendar c = Calendar.getInstance();
	    c.setTime(fechaInicio);
	    c.add(Calendar.DATE, dia);
	    
	    return this.formatearFecha(c);
	}
	
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
	
	public Map<String, List<String>> cargarRestriccionesProyecto(String nombre) throws IOException, CsvException{
		return Utils.leerCSVRestricciones(nombre);
	}
	
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
