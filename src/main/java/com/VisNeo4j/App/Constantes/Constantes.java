package com.VisNeo4j.App.Constantes;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class Constantes {
	public static final String rutaFicheros = "c:\\Users\\penad\\Desktop\\Doctorado\\NSGA-III\\Visualization_Neo4j_Back_End\\data\\\\CSV\\";
	public static final String rutaDatos = "c:\\Users\\penad\\Desktop\\Doctorado\\NSGA-III\\Visualization_Neo4j_Back_End\\data\\\\datos\\";
	public static final String rutaDatos_por_aeropuerto = "c:\\Users\\penad\\Desktop\\Doctorado\\NSGA-III\\Visualization_Neo4j_Back_End\\data\\\\datos\\datos_por_aeropuerto\\";
	public static final String rutaDatosPorDia = "c:\\Users\\penad\\Desktop\\Doctorado\\NSGA-III\\Visualization_Neo4j_Back_End\\data\\\\datos\\datos_por_dia\\";
	public static final String rutaFicherosFitness = "c:\\Users\\penad\\Desktop\\Doctorado\\NSGA-III\\Visualization_Neo4j_Back_End\\data\\\\CSV\\Ficheros_Fitness\\";
	public static final String rutaFicherosObjetivos = "c:\\Users\\penad\\Desktop\\Doctorado\\NSGA-III\\Visualization_Neo4j_Back_End\\data\\\\CSV\\Ficheros_Objetivos\\";
	public static final String rutaFicherosPersonas_Afectadas = "c:\\Users\\penad\\Desktop\\Doctorado\\NSGA-III\\Visualization_Neo4j_Back_End\\data\\\\CSV\\Ficheros_Personas_Afectadas\\";
	public static final String rutaFicherosVuelos_Cancelados = "c:\\Users\\penad\\Desktop\\Doctorado\\NSGA-III\\Visualization_Neo4j_Back_End\\data\\\\CSV\\Ficheros_Vuelos_Cancelados\\";
	public static final String extensionFichero = ".csv";
    public static final DateFormat formatoFecha = new SimpleDateFormat("yyyyMMddHHmmss");
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
    public static final List<String> nombresRestricciones = List.of("Epi");
    public static final List<String> nombreObjetivos = List.of("1","2","3","4","5","6","7");
    public static final List<Integer> idObjetivos = List.of(1,2,3,4,5,6,7);
    public static final List<String> nombresPersonas = List.of("Personas afectadas", "Personas restantes");
    public static final List<String> estadosVuelos = List.of("Vuelos cancelados", "Vuelos restantes");
    public static final String nombreCampoVuelosCancelados = "VuelosCancelados";
    
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
    
    
}
