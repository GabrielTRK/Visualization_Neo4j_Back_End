package com.VisNeo4j.App.Constantes;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Constantes {
	public static final String rutaFicheros = "c:\\Users\\penad\\Desktop\\Doctorado\\NSGA-III\\Visualization_Neo4j_Back_End\\data\\\\CSV\\";
	public static final String rutaDatos = "c:\\Users\\penad\\Desktop\\Doctorado\\NSGA-III\\Visualization_Neo4j_Back_End\\data\\\\datos\\";
	public static final String rutaDatos_por_aeropuerto = "c:\\Users\\penad\\Desktop\\Doctorado\\NSGA-III\\Visualization_Neo4j_Back_End\\data\\\\datos\\datos_por_aeropuerto\\";
	public static final String rutaDatosPorDia = "c:\\Users\\penad\\Desktop\\Doctorado\\NSGA-III\\Visualization_Neo4j_Back_End\\data\\\\datos\\datos_por_dia\\";
	public static final String rutaFicherosFitness = "c:\\Users\\penad\\Desktop\\Doctorado\\NSGA-III\\Visualization_Neo4j_Back_End\\data\\\\CSV\\Ficheros_Fitness\\";
	public static final String rutaFicherosObjetivos = "c:\\Users\\penad\\Desktop\\Doctorado\\NSGA-III\\Visualization_Neo4j_Back_End\\data\\\\CSV\\Ficheros_Objetivos\\";
	public static final String rutaFicherosParams = "c:\\Users\\penad\\Desktop\\Doctorado\\NSGA-III\\Visualization_Neo4j_Back_End\\data\\\\CSV\\Ficheros_Parámetros\\";
	public static final String rutaFicherosPreferencias = "c:\\Users\\penad\\Desktop\\Doctorado\\NSGA-III\\Visualization_Neo4j_Back_End\\data\\\\CSV\\Ficheros_Preferencias\\";
	public static final String rutaFicherosProyectos = "c:\\Users\\penad\\Desktop\\Doctorado\\NSGA-III\\Visualization_Neo4j_Back_End\\data\\\\CSV\\Proyectos\\";
	
	public static final String extensionFichero = ".csv";
    public static final DateFormat formatoFecha = new SimpleDateFormat("yyyyMMddHHmmss");
    public static final SimpleDateFormat formatoFechaRRPS_PAT = new SimpleDateFormat("yyyy-MM-dd");
    public static final String fechaSDMin = "2020-04-01";
    public static final String fechaSDMax = "2020-08-31";
    public static final String nombreProblemaDefecto = "problema";
    public static final String nombreDTLZ1 = "dtlz1";
    public static final String nombreDTLZ2 = "dtlz2";
    public static final String nombreDTLZ5 = "dtlz5";
    public static final String nombreDTLZ7 = "dtlz7";
    public static final String nombreProblemaVuelos = "problemaVuelos";
    public static final String nombreProblemaSubVuelos = "problemaSubVuelos";
    public static final String nombreProblemaVuelosExt = "problemaVuelosExt";
    public static final String nombreProblemaGestionConexionesAeropuertos = "problemaGestionConexionesAeropuertos";
    public static final String nombreProblemaGestionConexionesAeropuertosPorDia = "problemaGestionConexionesAeropuertosPorDia";
    public static final String nombreProblemaRRPS_PAT = "problemaRRPS_PAT";
    public static final String nombreFicheroSIR = "sir";
    public static final String nombreFicheroAeropuertos_Entradas = "aeropuertos_entradas";
    public static final String nombreFicheroAeropuertos_Espanyoles = "aeropuertos_españoles";
    public static final String nombreFicheroAeropuertos_Origen = "aeropuertos_salida";
    public static final String nombreFicheroCompanyias = "companyias";
    public static final String nombreFicheroDineroPorVuelo = "dinero_por_vuelo";
    public static final String nombreFicheroPasajerosPorVuelo = "pasajeros_por_vuelo";
    public static final String nombreFicheroPasajerosCompanyia = "pasajeros_por_vuelo_y_companyias";
    public static final String nombreFicheroPasajerosConectividad = "conectividad_por_aeropuerto";
    public static final String nombreFicheroConexionesAMantener = "conexiones_A_Mantener";
    public static final String nombreFicheroCoorenadas = "coordenadasAeropuertos";
    public static final String nombreFicheroConexiones = "conexionesAeropuertos";
    public static final String nombreFicheroNumConDia = "numConexionesDia";
    public static final String nombreFicheroFechas = "fechasSoluciones";
    public static final String nombreFicheroParametros = "parametros";
    public static final String nombreFicheroPreferencias = "preferencias";
    public static final String nombreFicheroRestricciones = "restricciones";
    public static final String nombreFicheroFechasSoluciones = "fechasSoluciones";
    public static final String nombreFicheroUsuarios = "Usuarios";
    public static final String nombreFicheroCola = "Queue";
    public static final String nombreDirectorioFicherosFitness = "Ficheros_Fitness";
    public static final String nombreDirectorioFicherosObjetivos = "Ficheros_Objetivos";
    public static final String nombreDirectorioFicherosRangos = "Ficheros_Rangos";
    public static final String nombreDirectorioTemp = "temp";
    public static final String nombreFicheroPoblacionTemp = "poblacion";
    public static final String nombreFicheroPbestsTemp = "pbests";
    public static final String nombreFicherov0Temp = "v0";
    public static final String nombreFicherov1Temp = "v1";
    
    public static final List<String> nombresRestricciones = Stream.of("Epi").collect(Collectors.toList());;
    public static final List<String> nombreObjetivos = Stream.of("1","2","3","4","5","6","7").collect(Collectors.toList());;
    public static final List<Integer> idObjetivos = Stream.of(1,2,3,4,5,6,7).collect(Collectors.toList());;
    public static final int idObjetivo1 = 1;
    public static final int idObjetivo2 = 2;
    public static final int idObjetivo3 = 3;
    public static final int idObjetivo4 = 4;
    public static final int idObjetivo5 = 5;
    public static final int idObjetivo6 = 6;
    public static final int idObjetivo7 = 7;
    public static final List<String> idContinentes = Stream.of("AF","AS","EU","NA","OC","SA").collect(Collectors.toList());;
    public static final List<String> nombresPersonas = Stream.of("Personas afectadas", "Personas restantes").collect(Collectors.toList());;
    public static final List<String> estadosVuelos = Stream.of("Vuelos cancelados", "Vuelos restantes").collect(Collectors.toList());;
    public static final String nombreCampoVuelosCancelados = "VuelosCancelados";
    public static final String nombreCampoPasajerosPerdidosPorCompañía = "PasajerosPerdidosPorCompañía";
    public static final String nombreCampoIngresoPerdidoPorAreaInf = "IngresoPerdidoPorAreaInf";
    public static final String nombreCampoIngresoPerdidoPorAerDest = "IngresoPerdidoPorAerDest";
    
    public static final String sirDBField = "sir";
    public static final String pasajerosDBField = "pasajeros";
    public static final String companyiaDBField = "companyia";
    public static final String conectividadDBField = "conectividad";
    public static final String dineroTDBField = "dineroT";
    public static final String dineroNDBField = "dineroN";
    public static final String tasasDBField = "tasas";
    public static final String origenDBField = "origen";
    public static final String destinoDBField = "destino";
    public static final String VuelosDesdeOrigenDBField = "VuelosDesdeOrigen";
    public static final String areaInfDBField = "areaInf";
    public static final String continentDBField = "continente";
    public static final String capitalDBField = "capital";
    public static final String origenNombreDBField = "origenNombre";
    public static final String destinoNombreDBField = "destinoNombre";
    
    public static final String nombreQDMPGenerica = "QDMPGenerico";
    public static final String nombreQDMPSR = "SR";
	public static final String nombreIWGenerica = "IWGenerica";
    public static final String nombreIWDyanamicDecreasing = "DynamicDecreasingIW";
    public static final String nombreCPGenerica = "maxIteraciones";
    public static final String nombreCPMaxDistQuick = "MaxDistQuick";
    
    public static final String nombreParamNumIndividuos = "numIndividuos";
    public static final String nombreParamInertiaW = "inertiaW";
    public static final String nombreParamC1 = "c1";
    public static final String nombreParamC2 = "c2";
    public static final String nombreParamCPNumIter = "numIteraciones";
    public static final String nombreParamCPM = "m";
    public static final String nombreParamCPP = "p";
    
    public static final String nombreFechaInicial = "Fecha_Inicial";
    public static final String nombreFechaFinal = "Fecha_Final";
    public static final String nombreFechaActual = "Fecha_Actual";
    
    public static final String nombreRestriccionEpidemiologica = "Restricción Epidemiológica";
    public static final String nombreRestriccionPolitica = "Restricción Política";
    
    public static final String defaultTooltipTextZ1 = "The losses in the catchment areas if all connections are cancelled would be over 40 million euros per day.";
    public static final String defaultTooltipTextZ2 = "Depending on the popularity of the destination, their income may vary from €100,000 up to €9,000,000 per day.";
    public static final String defaultTooltipTextZ3 = "Depending on the activity of each airline, they operate for range of 200 to more than 15,000 passengers per day.";
    public static final String defaultTooltipTextZ4 = "Each flight may produce from €400 to €3,500 due to airport taxes depending on the airport fee and the weight of the aircraft.";
    public static final String defaultTooltipTextZ5 = "The least busy airports may gain around €3,000 per day from taxes, while the bussiest ones may gain up to €100,000.";
    public static final String defaultTooltipTextZ6 = "Most commercial flights carry between 80 to 300 passengers.";
    public static final String defaultTooltipTextZ7 = "The measurment of Hub Connectivity proposed by ACI Europe may vary from below 30 up to 70,000 depending on the activity of the airport.";
    
    public static final String KO_respuestaNombresIguales = "The name of the project already exists.";
    public static final String KO_respuestaProjectRunning = "There is another project currently running an optimization. Please wait until its finished to run a new one. To check the saved projects go to the Load Project tab.";
    public static final String KO_respuestaSavedProjectRunning = "The project has been saved, but there is another project currently running an optimization. Please wait until its finished to run a new one. To check the saved projects go to the Load Project tab.";
    public static final String OK_respuestaProjectSaved = "Project saved.";
    public static final String OK_respuestaRunning = "Running optimization...";
    public static final String OK_respuestaProjectSavedRunning = "Saving project and Running optimization...";
    public static final String KO_respuestaNoFlights = "Due to the lockdown, there were no flights programmed between April 1st of 2020 and August 31st of 2020. Please chose a time window that doesn't overlap with this one.";
    public static final String KO_respuestaNoNombresIgualesYNoFlights = "The name of the project already exists. Aditionally, due to the lockdown, there were no flights programmed between April 1st of 2020 and August 31st of 2020. Please chose a time window that doesn't overlap with this one.";
    
    public static final String OK_respuestaOptimizationCancelled = "Optimization cancelled.";
    public static final String OK_respuestaProjectDeleted = "Project deleted.";
    public static final String KO_respuestaProjectDeletedRunning = "The project is currently running. In order to delete it you must first cancel its optimization.";
    
    public static final String OK_respuestaSolutionDeleted = "Solution deleted.";
    
    public static boolean continueOpt = true;
}
