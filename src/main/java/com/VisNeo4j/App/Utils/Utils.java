package com.VisNeo4j.App.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.tomcat.util.bcel.Const;

import com.VisNeo4j.App.Algoritmo.Parametros.BPSOParams;
import com.VisNeo4j.App.Constantes.Constantes;
import com.VisNeo4j.App.Modelo.Individuo;
import com.VisNeo4j.App.Modelo.Poblacion;
import com.VisNeo4j.App.Modelo.Salida.Aeropuerto;
import com.VisNeo4j.App.Modelo.Salida.BPSOParamsSalida;
import com.VisNeo4j.App.Modelo.Salida.FechasProyecto;
import com.VisNeo4j.App.Modelo.Salida.OrdenObjSalida;
import com.VisNeo4j.App.Modelo.Salida.Restricciones;
import com.VisNeo4j.App.Modelo.Salida.TraducirSalida;
import com.VisNeo4j.App.Problemas.Problema;
import com.VisNeo4j.App.Problemas.Datos.DatosProblema;
import com.VisNeo4j.App.Problemas.Datos.DatosProblemaDias;
import com.VisNeo4j.App.Problemas.Datos.DatosRRPS_PAT;
import com.VisNeo4j.App.Problemas.Datos.DatosRRPS_PATDiaI;
import com.VisNeo4j.App.QDMP.DMPreferences;
import com.VisNeo4j.App.QDMP.ObjectivesOrder;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;

public class Utils {
	
	public static Double getRandNumber(Double min, Double max) {
		return (Double) ((Math.random() * (max - min)) + min);
	}
	
	public static int getRandNumber(int min, int max) {
		return (int) ((Math.random() * (max - min)) + min);
	}
	
	public static Double getRandBinNumber() {
		double rand = Math.random();
		if (rand < 0.5) {
			return 0.0;
		}
		else
			return 1.0;
	}
	
	public static boolean isProbValid (Double prob) {
		if ((prob < 0.0) || (prob > 1.0)) {
		      return false;
		}
		else
			return true;
	}
	
	public static Double repararValorFueraDelRango(Double valor, Double min, Double max) {
		if(valor < min) {
			return min;
		}
		else if (valor > max) {
			return max;
		} else {
			return valor;
		}
	}
	
	public static Poblacion juntarPoblaciones (Poblacion padres, Poblacion hijos, Problema problema) {
		List<Individuo> lista = new ArrayList<>(2 * padres.getNumIndividuos());
		for (int i = 0; i < padres.getNumIndividuos(); i++) {
			lista.add(padres.getPoblacion().get(i));
		}
		for (int i = 0; i < padres.getNumIndividuos(); i++) {
			lista.add(hijos.getPoblacion().get(i));
		}
		Poblacion total = new Poblacion(2 * padres.getNumIndividuos(), problema);
		total.setPoblacion(lista);
		return total;
	}
	
	public static List<Individuo> obtenerFrenteConIndice(Poblacion p, int pos){
		List<Individuo> frente = new ArrayList<>();
		frente.add(p.getPoblacion().get(pos));
		pos++;
		while( pos < p.getPoblacion().size()) {
			if(frente.get(0).getdomina() == p.getPoblacion().get(pos).getdomina()) {
				frente.add(p.getPoblacion().get(pos));
			}
			pos++;
		}
		return frente;
	}
	
	public static List<Individuo> obtenerFrenteConIndice(List<Individuo> p, int pos){
		List<Individuo> frente = new ArrayList<>();
		frente.add(p.get(pos));
		pos++;
		while( pos < p.size()) {
			if(frente.get(0).getdomina() == p.get(pos).getdomina()) {
				frente.add(p.get(pos));
			}
			pos++;
		}
		return frente;
	}
	
	public static Poblacion borrarElementosDeLista(List<Individuo> lista, Poblacion p) {
		List<Individuo> poblacionABorrar = p.getPoblacion();
		for(int i = 0; i < lista.size(); i++) {
			Individuo ind = lista.get(i);
			poblacionABorrar.remove(ind);
		}
		p.setPoblacion(poblacionABorrar);
		return p;
	}
	
	public static List<Individuo> borrarElementosDeLista(List<Individuo> lista, List<Individuo> p) {
		List<Individuo> poblacionABorrar = p;
		for(int i = 0; i < lista.size(); i++) {
			Individuo ind = lista.get(i);
			poblacionABorrar.remove(ind);
		}
		p = poblacionABorrar;
		return p;
	}
	
	public static List<Individuo> juntarListas (List<Individuo> Alista, List<Individuo> frente){
		for(Individuo i : frente) {
			Alista.add(i);
		}
		return Alista;
	}
	
	public static List<Individuo> juntarListass (List<Individuo> Alista, List<Individuo> frente){
		List<Individuo> Blista = new ArrayList<>();
		for(Individuo i : Alista) {
			Blista.add(i);
		}
		for(Individuo i : frente) {
			Blista.add(i);
		}
		return Blista;
	}
	
	public static List<Double> inicializarLista (int tamaño){
		List<Double> lista = new ArrayList<>();
		for (int i = 0; i < tamaño; i++) {
			lista.add(0.0);
		}
		return lista;
	}
	
	public static int nextInt(int lower, int upper) {
		Random rand = new Random();
		return lower + rand.nextInt((upper - lower + 1)) ;
	}
	
	public static List<Double> ArraytoArrayList(double[] array){
		List<Double> list = new ArrayList<>(array.length);
		for (int i = 0; i < array.length; i++) {
			list.add(array[i]);
		}
		return list;
	}
	
	public static void imprimirFrente(List<Individuo> lista) {
		for (int i = 0; i < lista.size(); i++) {
			System.out.println(lista.get(i));
		}
	}
	
	public static List<Double> mediaVariables (List<Individuo> frente){
		List<Double> medias = new ArrayList<>();
		for(int j = 0; j < frente.get(0).getVariables().size(); j++) {
			medias.add(0.0);
		}
		
		for (int i = 0; i < frente.size(); i++) {
			Individuo ind = frente.get(i);
			for(int j = 0; j < ind.getVariables().size(); j++) {
				medias.set(j, medias.get(j) + ind.getVariables().get(j));
			}
		}
		
		for(int j = 0; j < frente.get(0).getVariables().size(); j++) {
			medias.set(j, medias.get(j) / frente.size());
		}
		return medias;
	}
	
	public static List<Double> mediaObjetivos (List<Individuo> frente){
		List<Double> medias = new ArrayList<>();
		for(int j = 0; j < frente.get(0).getObjetivos().size(); j++) {
			medias.add(0.0);
		}
		
		for (int i = 0; i < frente.size(); i++) {
			Individuo ind = frente.get(i);
			for(int j = 0; j < ind.getObjetivos().size(); j++) {
				medias.set(j, medias.get(j) + ind.getObjetivos().get(j));
			}
		}
		
		for(int j = 0; j < frente.get(0).getObjetivos().size(); j++) {
			medias.set(j, medias.get(j) / frente.size());
		}
		return medias;
	}
	
	public static String crearCSVConObjetivos(List<Individuo> frente, String nombreProblema) throws IOException {
		Date date = new Date();
		//String fileName = "problemaSubVuelosFrente" + Constantes.extensionFichero;
		String fileName = nombreProblema + Constantes.formatoFecha.format(date) + Constantes.extensionFichero;
		if(frente.size() == 0) {
			return fileName;
		}else {
			List<String[]> lista = new ArrayList<>();
			
			String[] Cabecera = new String[2];
			Cabecera[0] = String.valueOf(frente.get(0).getVariables().size());
			Cabecera[1] = String.valueOf(frente.get(0).getObjetivos().size());
			
			lista.add(Cabecera);
			
			for(int i = 0; i < frente.size(); i++) {
				Individuo ind = frente.get(i);
				String[] VariablesYObjetivos = new String[ind.getVariables().size()
				                                          + ind.getObjetivos().size()];
				
				for(int j = 0; j < ind.getVariables().size(); j++) {
					VariablesYObjetivos[j] = String.valueOf(ind.getVariables().get(j));
				}
				for(int k = ind.getVariables().size(); k < ind.getVariables().size() + ind.getObjetivos().size(); k++) {
					VariablesYObjetivos[k] = String.valueOf(ind.getObjetivos().get(k - ind.getVariables().size()));
				}
				lista.add(VariablesYObjetivos);
			}
			
			try (CSVWriter writer = new CSVWriter(new FileWriter(Constantes.rutaFicheros + fileName))) {
		            writer.writeAll(lista);
			}
			return fileName;
		}
		
	}
	
	public static List<Individuo> leerCSV(String nombre) throws FileNotFoundException, IOException, CsvException {
		try (CSVReader reader = new CSVReader(new FileReader(Constantes.rutaFicheros + nombre))) {
			List<String[]> r = reader.readAll();
			List<Individuo> frente = new ArrayList<>();
			
			int numVariables = Integer.valueOf((r.get(0)[0]));
			int numObjetivos = Integer.valueOf((r.get(0)[1]));
			
			for(int i = 1; i < r.size(); i++) {
		    	Individuo ind = new Individuo(numVariables, numObjetivos);
		    	List<Double> Var = new ArrayList<>();
		    	List<Double> Fobj = new ArrayList<>();
		    	for (int k = 0; k < numVariables; k++) {
		    		Var.add(Double.valueOf(r.get(i)[k]));
		    	}
		    	for (int j = numVariables; j < r.get(i).length; j++) {
		    		Fobj.add(Double.valueOf(r.get(i)[j]));
		    	}
		    	ind.setVariables(Var);
		    	ind.setObjetivos(Fobj);
		    	frente.add(ind);
		    }
		    return frente;
		}
	}
	
	public static List<String> leerCSVconexion(String proyecto, int numFila) throws FileNotFoundException, IOException, CsvException {
		try (CSVReader reader = new CSVReader(new FileReader(Constantes.rutaFicherosProyectos + "\\" + proyecto + "\\" + Constantes.nombreFicheroConexiones + Constantes.extensionFichero))) {
			List<String[]> r = reader.readAll();
			List<String> conexiones = new ArrayList<>();
			
			for(int i = 0; i < r.get(numFila).length; i++) {
				
		    	conexiones.add(r.get(numFila)[i]);
		    	
		    }
		    return conexiones;
		}
	}
	
	public static List<String> leerCSVproblema(String proyecto, int numFila) throws FileNotFoundException, IOException, CsvException {
		try (CSVReader reader = new CSVReader(new FileReader(Constantes.rutaFicherosProyectos + "\\" + proyecto + "\\" + Constantes.nombreProblemaRRPS_PAT + Constantes.extensionFichero))) {
			List<String[]> r = reader.readAll();
			List<String> solucion = new ArrayList<>();
			if(r.size() > 0) {
				for(int i = 0; i < r.get(numFila).length; i++) {
					
					solucion.add(r.get(numFila)[i]);
			    }
			}
		    return solucion;
		}
	}
	
	public static List<String> leerCSVproblemaUltimo() throws FileNotFoundException, IOException, CsvException {
		try (CSVReader reader = new CSVReader(new FileReader(Constantes.rutaFicheros + Constantes.nombreProblemaGestionConexionesAeropuertosPorDia + Constantes.extensionFichero))) {
			List<String[]> r = reader.readAll();
			List<String> solucion = new ArrayList<>();
			if(r.size() > 0) {
				for(int i = 0; i < r.get(r.size() - 1).length; i++) {
					
					solucion.add(r.get(r.size() - 1)[i]);
			    }
			}
		    return solucion;
		}
	}
	
	public static int leerCSVproblemaNumSoluciones() throws FileNotFoundException, IOException, CsvException {
		try (CSVReader reader = new CSVReader(new FileReader(Constantes.rutaFicheros + Constantes.nombreProblemaGestionConexionesAeropuertosPorDia + Constantes.extensionFichero))) {
			List<String[]> r = reader.readAll();
			
		    return r.size();
		}
	}
	
	public static List<Aeropuerto> obtenerSolucionDiaI(String proyecto, int numFila, int dia) throws FileNotFoundException, IOException, CsvException {
		List<String> solucion = leerCSVproblema(proyecto, numFila);
		List<String> conexiones = leerCSVconexion(proyecto, Integer.valueOf(solucion.get(0)));
		List<String> numConDia = leerCSVnumDiasYCon(proyecto, Integer.valueOf(solucion.get(1)));
		int offsetBits = 0;
		for(int i = 0; i < dia; i++) {
			offsetBits += Integer.valueOf(numConDia.get(i));
		}
		solucion.remove(0);
		solucion.remove(0);
		List<Double> variablesDouble = new ArrayList<>();
		List<String> variablesString = solucion.subList(offsetBits, offsetBits + Integer.valueOf(numConDia.get(dia)));
		for(int i = 0; i < variablesString.size(); i++) {
			variablesDouble.add(Double.valueOf(variablesString.get(i)));
		}
		
		return TraducirSalida.añadirCoordenadas(variablesDouble, conexiones.subList(offsetBits * 2, (offsetBits * 2) + Integer.valueOf(numConDia.get(dia)) * 2));
	}
	
	public static List<Double> obtenerBitsSolDiaI(String proyecto, int sol, int dia) throws FileNotFoundException, IOException, CsvException {
		List<String> solucion = leerCSVproblema(proyecto, sol);
		List<String> numConDia = leerCSVnumDiasYCon(proyecto, Integer.valueOf(solucion.get(1)));
		int offsetBits = 0;
		for(int i = 0; i < dia; i++) {
			offsetBits += Integer.valueOf(numConDia.get(i));
		}
		solucion.remove(0);
		solucion.remove(0);
		List<Double> variablesDouble = new ArrayList<>();
		List<String> variablesString = solucion.subList(offsetBits, offsetBits + Integer.valueOf(numConDia.get(dia)));
		for(int i = 0; i < variablesString.size(); i++) {
			variablesDouble.add(Double.valueOf(variablesString.get(i)));
		}
		
		return variablesDouble;
	}
	
	public static int obtenernumDiasSolucionI(String proyecto, int sol) throws FileNotFoundException, IOException, CsvException {
		List<String> solucion = leerCSVproblema(proyecto, sol);
		List<String> numConDia = leerCSVnumDiasYCon(proyecto, Integer.valueOf(solucion.get(1)));
		
		
		
		return numConDia.size();
	}
	
	public static List<String> leerCSVnumDiasYCon(String proyecto, int numFila) throws FileNotFoundException, IOException, CsvException {
		try (CSVReader reader = new CSVReader(new FileReader(Constantes.rutaFicherosProyectos + "\\" + proyecto + "\\" + Constantes.nombreFicheroNumConDia + Constantes.extensionFichero))) {
			List<String[]> r = reader.readAll();
			List<String> solucion = new ArrayList<>();
			if(r.size() > 0) {
				for(int i = 0; i < r.get(numFila).length; i++) {
					
					solucion.add(r.get(numFila)[i]);
			    }
			}
		    return solucion;
		}
	}
	
	public static List<List<String>> leerCSVnombre(String nombreFichero, String nombreProyecto) throws FileNotFoundException, IOException, CsvException {
		List<List<String>> fichero = new ArrayList<>();
		int numFilas;
		try (CSVReader reader = new CSVReader(new FileReader(Constantes.rutaFicherosProyectos + "\\" + nombreProyecto + "\\" + nombreFichero + Constantes.extensionFichero))) {
			List<String[]> r = reader.readAll();
			numFilas = r.size();
			for(int fila = 0; fila < numFilas; fila++) {
				List<String> filaI = new ArrayList<>();
				for(int columna = 0; columna < r.get(fila).length; columna++) {
					
					filaI.add(r.get(fila)[columna]);
			    }
				fichero.add(filaI);
			}
		}
		return fichero;
	}
	
	public static List<List<String>> leerCSVHistFitness(String proyecto, String id) throws FileNotFoundException, IOException, CsvException {
		List<List<String>> fichero = new ArrayList<>();
		int numFilas;
		try (CSVReader reader = new CSVReader(new FileReader(Constantes.rutaFicherosProyectos + 
				"\\" + proyecto + "\\" + Constantes.nombreDirectorioFicherosFitness + "\\" +
				id + Constantes.extensionFichero))) {
			List<String[]> r = reader.readAll();
			numFilas = r.size();
			for(int fila = 0; fila < numFilas; fila++) {
				List<String> filaI = new ArrayList<>();
				for(int columna = 0; columna < r.get(fila).length; columna++) {
					
					filaI.add(r.get(fila)[columna]);
			    }
				fichero.add(filaI);
			}
		}
		return fichero;
	}
	
	public static List<List<String>> leerCSVObjetivos(String proyecto, String id) throws FileNotFoundException, IOException, CsvException {
		List<List<String>> fichero = new ArrayList<>();
		int numFilas;
		try (CSVReader reader = new CSVReader(new FileReader(Constantes.rutaFicherosProyectos + 
				"\\" + proyecto + "\\" + Constantes.nombreDirectorioFicherosObjetivos + "\\" +
				id + Constantes.extensionFichero))) {
			List<String[]> r = reader.readAll();
			numFilas = r.size();
			for(int fila = 0; fila < numFilas; fila++) {
				List<String> filaI = new ArrayList<>();
				for(int columna = 0; columna < r.get(fila).length; columna++) {
					
					filaI.add(r.get(fila)[columna]);
			    }
				fichero.add(filaI);
			}
		}
		return fichero;
	}
	
	
	public static int modificarCSVconexiones(List<List<String>> conexiones, String nombre) throws IOException, CsvException {
		if(conexiones.size() == 0) {
			return -1;
		}
		else {
			File file = new File(Constantes.rutaFicherosProyectos + "\\" + nombre + "\\" + Constantes.nombreFicheroConexiones + Constantes.extensionFichero);
			List<List<String>> listaPrevia;
			if(file.exists()) {
				listaPrevia = leerCSVnombre(Constantes.nombreFicheroConexiones, nombre);
			}else {
				listaPrevia = new ArrayList<>();
			}
			
			int pos = listaPrevia.size();
			List<String> listaNueva = new ArrayList<>();
			for(int i = 0; i < conexiones.size(); i++) {
				listaNueva.add(conexiones.get(i).get(0));
				listaNueva.add(conexiones.get(i).get(1));
			}
			
			
			for(int i = 0; i < listaPrevia.size(); i++) {
				if(comprobarListasIguales(listaNueva, listaPrevia.get(i))) {
					pos = i;
				}
			}
			if(pos < listaPrevia.size()) {
				return pos;
			}
			else {
				List<String[]> lista = new ArrayList<>();
				listaPrevia.add(listaNueva);
				for(int i = 0; i < listaPrevia.size(); i++) {
					String[] fila = new String[listaPrevia.get(i).size()];
					for(int j = 0; j < listaPrevia.get(i).size(); j++) {
						fila[j] = listaPrevia.get(i).get(j);
					}
					lista.add(fila);
				}
				try (CSVWriter writer = new CSVWriter(new FileWriter(Constantes.rutaFicherosProyectos + "\\" + nombre + "\\" + Constantes.nombreFicheroConexiones + Constantes.extensionFichero), ',', 
	    	            CSVWriter.NO_QUOTE_CHARACTER, 
	    	            CSVWriter.DEFAULT_ESCAPE_CHARACTER, 
	    	            CSVWriter.DEFAULT_LINE_END)) {
		            writer.writeAll(lista);
				}
				return listaPrevia.size()-1;
			}
			
		}
		
	}
	
	public static int modificarCSVNumConYDias(List<String> numConexionesPorDia, String nombre) throws IOException, CsvException {
		if(numConexionesPorDia.size() == 0) {
			return -1;
		}
		else {
			File file = new File(Constantes.rutaFicherosProyectos + "\\" + nombre + "\\" + Constantes.nombreFicheroNumConDia + Constantes.extensionFichero);
			List<List<String>> listaPrevia;
			if(file.exists()) {
				listaPrevia = leerCSVnombre(Constantes.nombreFicheroNumConDia, nombre);
			}else {
				listaPrevia = new ArrayList<>();
			}
			
			int pos = listaPrevia.size();
			
			
			for(int i = 0; i < listaPrevia.size(); i++) {
				if(comprobarListasIguales(numConexionesPorDia, listaPrevia.get(i))) {
					pos = i;
				}
			}
			if(pos < listaPrevia.size()) {
				return pos;
			}
			else {
				List<String[]> lista = new ArrayList<>();
				listaPrevia.add(numConexionesPorDia);
				for(int i = 0; i < listaPrevia.size(); i++) {
					String[] fila = new String[listaPrevia.get(i).size()];
					for(int j = 0; j < listaPrevia.get(i).size(); j++) {
						fila[j] = listaPrevia.get(i).get(j);
					}
					lista.add(fila);
				}
				try (CSVWriter writer = new CSVWriter(new FileWriter(Constantes.rutaFicherosProyectos + "\\" + nombre + "\\" + Constantes.nombreFicheroNumConDia + Constantes.extensionFichero), ',', 
	    	            CSVWriter.NO_QUOTE_CHARACTER, 
	    	            CSVWriter.DEFAULT_ESCAPE_CHARACTER, 
	    	            CSVWriter.DEFAULT_LINE_END)) {
		            writer.writeAll(lista);
				}
				return listaPrevia.size()-1;
			}
			
		}
		
	}
	
	public static String modificarCSVproblemaRRPS_PAT(Individuo ind, DatosRRPS_PAT datos, String nombre) throws IOException, CsvException {
		int filaConexiones = modificarCSVconexiones(datos.getConexionesTotales(), nombre);
		List<String> numConDia = new ArrayList<>();
		for(DatosRRPS_PATDiaI  dato : datos.getDatosPorDia()) {
			numConDia.add(String.valueOf(dato.getConexiones().size()));
		}
		int filaNumDiasYCon = modificarCSVNumConYDias(numConDia, nombre);
		
		File file = new File(Constantes.rutaFicherosProyectos + "\\" + nombre + "\\" + Constantes.nombreProblemaRRPS_PAT + Constantes.extensionFichero);
		List<List<String>> solucionesExistentes;
		if(file.exists()) {
			solucionesExistentes = leerCSVnombre(Constantes.nombreProblemaRRPS_PAT, nombre);
		}else {
			solucionesExistentes = new ArrayList<>();
		}
		
		List<String> solucionNueva = new ArrayList<>();
		solucionNueva.add(String.valueOf(filaConexiones));
		solucionNueva.add(String.valueOf(filaNumDiasYCon));
		for(int i = 0; i < ind.getVariables().size(); i++) {
			solucionNueva.add(String.valueOf(ind.getVariables().get(i)));
		}
		solucionesExistentes.add(solucionNueva);
		
		List<String[]> lista = new ArrayList<>();
		
		for(int i = 0; i < solucionesExistentes.size(); i++) {
			String[] filaI = new String[solucionesExistentes.get(i).size()];
			for(int j = 0; j < solucionesExistentes.get(i).size(); j++) {
				filaI[j] = solucionesExistentes.get(i).get(j);
			}
			lista.add(filaI);
		}
		try (CSVWriter writer = new CSVWriter(new FileWriter(Constantes.rutaFicherosProyectos + "\\" + nombre + "\\" + Constantes.nombreProblemaRRPS_PAT + Constantes.extensionFichero), ',', 
	            CSVWriter.NO_QUOTE_CHARACTER, 
	            CSVWriter.DEFAULT_ESCAPE_CHARACTER, 
	            CSVWriter.DEFAULT_LINE_END)) {
            writer.writeAll(lista);
            
            
		}
		return String.valueOf(solucionesExistentes.size()-1);
	}
	
	public static boolean comprobarListasIguales(List<String> a, List<String> b) {
		boolean iguales = true;
		if(a.size() != b.size()) {
			return false;
		}else {
			int pos = 0;
			while(pos < a.size() && iguales) {
				if(!a.get(pos).equals(b.get(pos))) {
					iguales = false;
				}
				pos++;
			}
		}
		return iguales;
	}
	
	public static void crearCSVParams(BPSOParams params, String nombre) throws IOException{
			List<String[]> lista = new ArrayList<>();
			String[] paramI = new String[2];
			paramI[0] = Constantes.nombreParamNumIndividuos;
			paramI[1] = String.valueOf(params.getNumIndividuos());
			lista.add(paramI);
			
			paramI = new String[2];
			paramI[0] = Constantes.nombreParamInertiaW;
			paramI[1] = String.valueOf(params.getInertiaW().getInertiaW());
			lista.add(paramI);
			
			paramI = new String[2];
			paramI[0] = Constantes.nombreParamC1;
			paramI[1] = String.valueOf(params.getC1());
			lista.add(paramI);
			
			paramI = new String[2];
			paramI[0] = Constantes.nombreParamC2;
			paramI[1] = String.valueOf(params.getC2());
			lista.add(paramI);
			
			paramI = new String[2];
			if(params.getCondicionParada().getMethod().equals(Constantes.nombreCPGenerica)) {
				paramI[0] = Constantes.nombreParamCPNumIter;
				paramI[1] = String.valueOf(params.getMax_Num_Iteraciones());
				lista.add(paramI);
			}else if(params.getCondicionParada().getMethod().equals(Constantes.nombreCPMaxDistQuick)){
				paramI[0] = Constantes.nombreParamCPM;
				paramI[1] = String.valueOf(params.getCondicionParada().getM());
				lista.add(paramI);
				paramI = new String[2];
				paramI[0] = Constantes.nombreParamCPP;
				paramI[1] = String.valueOf(params.getCondicionParada().getP());
				lista.add(paramI);
			}
			
			try (CSVWriter writer = new CSVWriter(new FileWriter(Constantes.rutaFicherosProyectos + "\\" + nombre + "\\" + Constantes.nombreFicheroParametros + Constantes.extensionFichero), ',', 
                    CSVWriter.NO_QUOTE_CHARACTER, 
                    CSVWriter.DEFAULT_ESCAPE_CHARACTER, 
                    CSVWriter.DEFAULT_LINE_END)) {
		            writer.writeAll(lista);
			}
			
		
	}
	
	public static BPSOParamsSalida leerCSVParamsSalida(String nombre) throws IOException, CsvException{
		List<List<String>> fichero = new ArrayList<>();
		int numFilas;
		try (CSVReader reader = new CSVReader(new FileReader(Constantes.rutaFicherosProyectos + "\\" + nombre + "\\" + Constantes.nombreFicheroParametros + Constantes.extensionFichero))) {
			List<String[]> r = reader.readAll();
			numFilas = r.size();
			for(int fila = 0; fila < numFilas; fila++) {
				List<String> filaI = new ArrayList<>();
				for(int columna = 0; columna < r.get(fila).length; columna++) {
					
					filaI.add(r.get(fila)[columna]);
			    }
				fichero.add(filaI);
			}
		}
		BPSOParamsSalida params = new BPSOParamsSalida(Integer.valueOf(fichero.get(0).get(1)), Double.valueOf(fichero.get(1).get(1)), Double.valueOf(fichero.get(2).get(1)), Double.valueOf(fichero.get(3).get(1)));
		return params;
	
	}
	
	public static BPSOParams leerCSVParams(String nombre) throws IOException, CsvException{
		List<List<String>> fichero = new ArrayList<>();
		int numFilas;
		try (CSVReader reader = new CSVReader(new FileReader(Constantes.rutaFicherosProyectos + "\\" + nombre + "\\" + Constantes.nombreFicheroParametros + Constantes.extensionFichero))) {
			List<String[]> r = reader.readAll();
			numFilas = r.size();
			for(int fila = 0; fila < numFilas; fila++) {
				List<String> filaI = new ArrayList<>();
				for(int columna = 0; columna < r.get(fila).length; columna++) {
					
					filaI.add(r.get(fila)[columna]);
			    }
				fichero.add(filaI);
			}
		}
		BPSOParams params = new BPSOParams(Integer.valueOf(fichero.get(0).get(1)), 
				Double.valueOf(fichero.get(1).get(1)), 
				Double.valueOf(fichero.get(2).get(1)), 
				Double.valueOf(fichero.get(3).get(1)),
				0,
				Double.valueOf(fichero.get(4).get(1)), 
				Double.valueOf(fichero.get(5).get(1)), 
				Constantes.nombreCPMaxDistQuick, 
				Constantes.nombreIWDyanamicDecreasing);
		return params;
	
	}
	
	public static void crearCSVPref(DMPreferences preferencias, String nombre) throws IOException{
			List<String[]> lista = new ArrayList<>();
			
			for(int i = 0; i < preferencias.getOrder().getOrder().size(); i++) {
				String[] objI = new String[1];
				objI[0] = String.valueOf(preferencias.getOrder().getOrder().get(i));
				lista.add(objI);
			}
			try (CSVWriter writer = new CSVWriter(new FileWriter(Constantes.rutaFicherosProyectos + "\\" + nombre + "\\" + Constantes.nombreFicheroPreferencias + Constantes.extensionFichero), ',', 
                    CSVWriter.NO_QUOTE_CHARACTER, 
                    CSVWriter.DEFAULT_ESCAPE_CHARACTER, 
                    CSVWriter.DEFAULT_LINE_END)) {
		            writer.writeAll(lista);
			}
		
	}
	
	public static OrdenObjSalida leerCSVPrefSalida(String nombre) throws IOException, CsvException{
		List<List<String>> fichero = new ArrayList<>();
		int numFilas;
		try (CSVReader reader = new CSVReader(new FileReader(Constantes.rutaFicherosProyectos + "\\" + nombre + "\\" + Constantes.nombreFicheroPreferencias + Constantes.extensionFichero))) {
			List<String[]> r = reader.readAll();
			numFilas = r.size();
			for(int fila = 0; fila < numFilas; fila++) {
				List<String> filaI = new ArrayList<>();
				for(int columna = 0; columna < r.get(fila).length; columna++) {
					
					filaI.add(r.get(fila)[columna]);
			    }
				fichero.add(filaI);
			}
		}
		List<Integer> listaOrden = new ArrayList<>();
		for(List<String> lista : fichero) {
			listaOrden.add(Integer.valueOf(lista.get(0)));
		}
		OrdenObjSalida orden = new OrdenObjSalida(listaOrden);
		return orden;
	}
	
	public static ObjectivesOrder leerCSVPref(String nombre) throws IOException, CsvException{
		List<List<String>> fichero = new ArrayList<>();
		int numFilas;
		try (CSVReader reader = new CSVReader(new FileReader(Constantes.rutaFicherosProyectos + "\\" + nombre + "\\" + Constantes.nombreFicheroPreferencias + Constantes.extensionFichero))) {
			List<String[]> r = reader.readAll();
			numFilas = r.size();
			for(int fila = 0; fila < numFilas; fila++) {
				List<String> filaI = new ArrayList<>();
				for(int columna = 0; columna < r.get(fila).length; columna++) {
					
					filaI.add(r.get(fila)[columna]);
			    }
				fichero.add(filaI);
			}
		}
		List<Integer> listaOrden = new ArrayList<>();
		for(List<String> lista : fichero) {
			listaOrden.add(Integer.valueOf(lista.get(0)));
		}
		ObjectivesOrder orden = new ObjectivesOrder();
		orden.setOrder(listaOrden);
		return orden;
	}
	
	public static void crearCSVFechas(String fecha_I, String fecha_F, String nombre) throws IOException{
		List<String[]> lista = new ArrayList<>();
		String[] paramI = new String[2];
		paramI[0] = Constantes.nombreFechaInicial;
		paramI[1] = String.valueOf(fecha_I);
		lista.add(paramI);
		
		paramI = new String[2];
		paramI[0] = Constantes.nombreFechaFinal;
		paramI[1] = String.valueOf(fecha_F);
		lista.add(paramI);
		
		try (CSVWriter writer = new CSVWriter(new FileWriter(Constantes.rutaFicherosProyectos + "\\" + nombre + "\\" + Constantes.nombreFicheroFechasSoluciones + Constantes.extensionFichero), ',', 
                CSVWriter.NO_QUOTE_CHARACTER, 
                CSVWriter.DEFAULT_ESCAPE_CHARACTER, 
                CSVWriter.DEFAULT_LINE_END)) {
	            writer.writeAll(lista);
		}
	
	}
	
	public static FechasProyecto leerCSVFechasSalida(String nombre) throws IOException, CsvException{
		List<List<String>> fichero = new ArrayList<>();
		int numFilas;
		try (CSVReader reader = new CSVReader(new FileReader(Constantes.rutaFicherosProyectos + "\\" + nombre + "\\" + Constantes.nombreFicheroFechasSoluciones + Constantes.extensionFichero))) {
			List<String[]> r = reader.readAll();
			numFilas = r.size();
			for(int fila = 0; fila < numFilas; fila++) {
				List<String> filaI = new ArrayList<>();
				for(int columna = 0; columna < r.get(fila).length; columna++) {
					
					filaI.add(r.get(fila)[columna]);
			    }
				fichero.add(filaI);
			}
		}
		FechasProyecto fechas = new FechasProyecto(fichero.get(0).get(1), fichero.get(1).get(1));
		return fechas;
	
	}
	
	public static Map<String, String> leerCSVFechas(String nombre) throws IOException, CsvException{
		List<List<String>> fichero = new ArrayList<>();
		int numFilas;
		try (CSVReader reader = new CSVReader(new FileReader(Constantes.rutaFicherosProyectos + "\\" + nombre + "\\" + Constantes.nombreFicheroFechasSoluciones + Constantes.extensionFichero))) {
			List<String[]> r = reader.readAll();
			numFilas = r.size();
			for(int fila = 0; fila < numFilas; fila++) {
				List<String> filaI = new ArrayList<>();
				for(int columna = 0; columna < r.get(fila).length; columna++) {
					
					filaI.add(r.get(fila)[columna]);
			    }
				fichero.add(filaI);
			}
		}
		Map<String, String> fechas = new HashMap<>();
		fechas.put(Constantes.nombreFechaInicial, fichero.get(0).get(1));
		fechas.put(Constantes.nombreFechaFinal, fichero.get(1).get(1));
		
		return fechas;
	}
	
	public static void crearCSVRestricciones(double epi, List<String> pol, String nombre) throws IOException{
		List<String[]> lista = new ArrayList<>();
		String[] paramI = new String[2];
		paramI[0] = Constantes.nombreRestriccionEpidemiologica;
		paramI[1] = String.valueOf(epi);
		lista.add(paramI);
		
		paramI = new String[1+pol.size()];
		paramI[0] = Constantes.nombreRestriccionPolitica;
		for(int i = 0; i < pol.size(); i++) {
			paramI[i+1] = pol.get(i);
		}
		lista.add(paramI);
		
		try (CSVWriter writer = new CSVWriter(new FileWriter(Constantes.rutaFicherosProyectos + "\\" + nombre + "\\" + Constantes.nombreFicheroRestricciones + Constantes.extensionFichero), ',', 
                CSVWriter.NO_QUOTE_CHARACTER, 
                CSVWriter.DEFAULT_ESCAPE_CHARACTER, 
                CSVWriter.DEFAULT_LINE_END)) {
	            writer.writeAll(lista);
		}
	
	}
	
	public static Restricciones leerCSVRestriccionesSalida(String nombre) throws IOException, CsvException{
		List<List<String>> fichero = new ArrayList<>();
		int numFilas;
		try (CSVReader reader = new CSVReader(new FileReader(Constantes.rutaFicherosProyectos + "\\" + nombre + "\\" + Constantes.nombreFicheroRestricciones + Constantes.extensionFichero))) {
			List<String[]> r = reader.readAll();
			numFilas = r.size();
			for(int fila = 0; fila < numFilas; fila++) {
				List<String> filaI = new ArrayList<>();
				for(int columna = 0; columna < r.get(fila).length; columna++) {
					
					filaI.add(r.get(fila)[columna]);
			    }
				fichero.add(filaI);
			}
		}
		Restricciones res;
		if(fichero.get(1).size() > 1){
			res = new Restricciones(fichero.get(1).subList(1, fichero.get(1).size()), Double.valueOf(fichero.get(0).get(1)));
		}else {
			res = new Restricciones(List.of(), Double.valueOf(fichero.get(0).get(1)));
		}
		
		return res;
	}
	
	public static Map<String, List<String>> leerCSVRestricciones(String nombre) throws IOException, CsvException{
		List<List<String>> fichero = new ArrayList<>();
		int numFilas;
		try (CSVReader reader = new CSVReader(new FileReader(Constantes.rutaFicherosProyectos + "\\" + nombre + "\\" + Constantes.nombreFicheroRestricciones + Constantes.extensionFichero))) {
			List<String[]> r = reader.readAll();
			numFilas = r.size();
			for(int fila = 0; fila < numFilas; fila++) {
				List<String> filaI = new ArrayList<>();
				for(int columna = 0; columna < r.get(fila).length; columna++) {
					
					filaI.add(r.get(fila)[columna]);
			    }
				fichero.add(filaI);
			}
		}
		
		Map<String, List<String>> res = new HashMap<>();
		
		res.put(Constantes.nombreRestriccionEpidemiologica, fichero.get(0).subList(1, fichero.get(0).size()));
		res.put(Constantes.nombreRestriccionPolitica, fichero.get(1).subList(1, fichero.get(1).size()));
		
		return res;
	}
	
	public static String crearCSVObjetivos(List<Double> obj, List<Double> res, String nombreFichero, String nombreProyecto) throws IOException {
		String fileName = nombreFichero + Constantes.extensionFichero;
		if(obj.size() == 0) {
			return fileName;
		}else {
			List<String[]> lista = new ArrayList<>();
			String[] iter_Fit;
			DecimalFormat df = new DecimalFormat("0.00");
			
			for(int i = 0; i < res.size(); i++) {
				iter_Fit = new String[2];
				
				iter_Fit[0] = Constantes.nombresRestricciones.get(i);
				iter_Fit[1] = String.valueOf(df.format(res.get(i)));
				
				lista.add(iter_Fit);
			}
			
			for(int i = 0; i < obj.size(); i++) {
				iter_Fit = new String[2];
				
				iter_Fit[0] = Constantes.nombreObjetivos.get(i);
				iter_Fit[1] = String.valueOf(df.format(obj.get(i)));
				
				lista.add(iter_Fit);
			}
			
			try (CSVWriter writer = new CSVWriter(new FileWriter(Constantes.rutaFicherosProyectos + "\\" + nombreProyecto + "\\" + Constantes.nombreDirectorioFicherosObjetivos + "\\" + fileName), ',', 
                    CSVWriter.NO_QUOTE_CHARACTER, 
                    CSVWriter.DEFAULT_ESCAPE_CHARACTER, 
                    CSVWriter.DEFAULT_LINE_END)) {
		            writer.writeAll(lista);
			}
			return fileName;
		}
		
	}
	
	public static List<Double> leerCSVObjetivosSalida(String id, String nombreProyecto) throws IOException, CsvException {
		List<List<String>> fichero = new ArrayList<>();
		int numFilas;
		try (CSVReader reader = new CSVReader(new FileReader(Constantes.rutaFicherosProyectos + "\\" + nombreProyecto + "\\" + Constantes.nombreDirectorioFicherosObjetivos + "\\" + id + Constantes.extensionFichero))) {
			List<String[]> r = reader.readAll();
			numFilas = r.size();
			for(int fila = 0; fila < numFilas; fila++) {
				List<String> filaI = new ArrayList<>();
				for(int columna = 0; columna < r.get(fila).length; columna++) {
					
					filaI.add(r.get(fila)[columna]);
			    }
				fichero.add(filaI);
			}
		}
		
		List<Double> obj = new ArrayList<>();
		
		for(int i = 0; i < fichero.size(); i++) {
			obj.add(Double.valueOf(fichero.get(i).get(1)));
		}
		
		return obj;
		
	}
	
	public static String crearCSVConFitnessPorIteracion(List<Double> listaF, String nombreFichero, String nombreProyecto) throws IOException {
		String fileName = nombreFichero + Constantes.extensionFichero;
		if(listaF.size() == 0) {
			return fileName;
		}else {
			List<String[]> lista = new ArrayList<>();
			
			for(int i = 0; i < listaF.size(); i++) {
				String[] iter_Fit = new String[2];
				
				iter_Fit[0] = String.valueOf(i * 1.0);
				iter_Fit[1] = String.valueOf(listaF.get(i));
				
				lista.add(iter_Fit);
			}
			
			try (CSVWriter writer = new CSVWriter(new FileWriter(Constantes.rutaFicherosProyectos + "\\" + nombreProyecto + "\\" + Constantes.nombreDirectorioFicherosFitness + "\\" + fileName), ',', 
                    CSVWriter.NO_QUOTE_CHARACTER, 
                    CSVWriter.DEFAULT_ESCAPE_CHARACTER, 
                    CSVWriter.DEFAULT_LINE_END)) {
		            writer.writeAll(lista);
			}
			return fileName;
		}
		
	}
	
	public static List<Double> leerCSVConFitnessPorIteracionSalida(String id, String nombreProyecto) throws IOException, CsvException {
		List<List<String>> fichero = new ArrayList<>();
		int numFilas;
		try (CSVReader reader = new CSVReader(new FileReader(Constantes.rutaFicherosProyectos + "\\" + nombreProyecto + "\\" + Constantes.nombreDirectorioFicherosFitness + "\\" + id + Constantes.extensionFichero))) {
			List<String[]> r = reader.readAll();
			numFilas = r.size();
			for(int fila = 0; fila < numFilas; fila++) {
				List<String> filaI = new ArrayList<>();
				for(int columna = 0; columna < r.get(fila).length; columna++) {
					
					filaI.add(r.get(fila)[columna]);
			    }
				fichero.add(filaI);
			}
		}
		
		List<Double> fit = new ArrayList<>();
		
		fit.add(Double.valueOf(fichero.get(numFilas-1).get(0)));
		fit.add(Double.valueOf(fichero.get(numFilas-1).get(1)));
		
		return fit;
		
	}
	
	public static String crearCSVHistogramas(List<Double> listaPasajerosPerdidosPorCompanyia, List<Double> listaIngresoPerdidoPorAreaInf, List<Double> listaIngresoPerdidoPorAerDest, String nombreFichero, String nombreProyecto) throws IOException{
		String fileName = nombreFichero + Constantes.extensionFichero;
		if(listaPasajerosPerdidosPorCompanyia.size() == 0 || listaIngresoPerdidoPorAreaInf.size() == 0 || listaIngresoPerdidoPorAerDest.size() == 0) {
			return fileName;
		}else {
			List<String[]> lista = new ArrayList<>();
			
			String[] valores = new String[listaPasajerosPerdidosPorCompanyia.size()+1];
			valores[0] = Constantes.nombreCampoPasajerosPerdidosPorCompañía;
			for(int i = 0; i < listaPasajerosPerdidosPorCompanyia.size(); i++) {
				
				valores[i+1] = String.valueOf(listaPasajerosPerdidosPorCompanyia.get(i));
				
			}
			lista.add(valores);
			
			valores = new String[listaIngresoPerdidoPorAreaInf.size()+1];
			valores[0] = Constantes.nombreCampoIngresoPerdidoPorAreaInf;
			for(int i = 0; i < listaIngresoPerdidoPorAreaInf.size(); i++) {
				
				valores[i+1] = String.valueOf(listaIngresoPerdidoPorAreaInf.get(i));
				
			}
			lista.add(valores);
			
			valores = new String[listaIngresoPerdidoPorAerDest.size()+1];
			valores[0] = Constantes.nombreCampoIngresoPerdidoPorAerDest;
			for(int i = 0; i < listaIngresoPerdidoPorAerDest.size(); i++) {
				
				valores[i+1] = String.valueOf(listaIngresoPerdidoPorAerDest.get(i));
				
			}
			lista.add(valores);
			
			try (CSVWriter writer = new CSVWriter(new FileWriter(Constantes.rutaFicherosProyectos + "\\" + nombreProyecto + "\\" + Constantes.nombreDirectorioFicherosRangos + "\\" + fileName), ',', 
                    CSVWriter.NO_QUOTE_CHARACTER, 
                    CSVWriter.DEFAULT_ESCAPE_CHARACTER, 
                    CSVWriter.DEFAULT_LINE_END)) {
		            writer.writeAll(lista);
			}
			return fileName;
		}
	}
	
	public static Map<String, List<Double>> leerCSVHistogramas(String nombreProyecto, String id) throws FileNotFoundException, IOException, CsvException{
		Map<String, List<Double>> valores = new HashMap<>();
		
		try (CSVReader reader = new CSVReader(new FileReader(Constantes.rutaFicherosProyectos + "\\" + nombreProyecto + "\\" + Constantes.nombreDirectorioFicherosRangos + "\\" + id + Constantes.extensionFichero))) {
			List<String[]> r = reader.readAll();
			
			List<Double> filaI = new ArrayList<>();
			for(int columna = 1; columna < r.get(0).length; columna++) {
					
				filaI.add(Double.valueOf(r.get(0)[columna]));
			}
			valores.put(r.get(0)[0], filaI);
			
			filaI = new ArrayList<>();
			for(int columna = 1; columna < r.get(1).length; columna++) {
					
				filaI.add(Double.valueOf(r.get(1)[columna]));
			}
			valores.put(r.get(1)[0], filaI);
			
			filaI = new ArrayList<>();
			for(int columna = 1; columna < r.get(2).length; columna++) {
					
				filaI.add(Double.valueOf(r.get(2)[columna]));
			}
			valores.put(r.get(2)[0], filaI);
			
		}
		
		return valores;
	}
	
	public static Map<String, List<String>> obtenerRangos(String id, String nombreProyecto) throws FileNotFoundException, IOException, CsvException{
		Map<String, List<Double>> valores = leerCSVHistogramas(id, nombreProyecto);
		Map<String, List<String>> valoresSalida = new HashMap<>();
		DecimalFormat df = new DecimalFormat("0.00");
		
		int indMin = 0;
		int indMax = valores.get(Constantes.nombreCampoPasajerosPerdidosPorCompañía).size()-1;
		
		double valorMin = valores.get(Constantes.nombreCampoPasajerosPerdidosPorCompañía).get(indMin);
		double valorMax = valores.get(Constantes.nombreCampoPasajerosPerdidosPorCompañía).get(indMax);
		
		while(indMin < indMax && (valorMin == 0.0 || valorMax == 1.0)) {
			if(valorMin == 0.0) {
				valorMin = valores.get(Constantes.nombreCampoPasajerosPerdidosPorCompañía).get(indMin);
				indMin++;
			}
			if(valorMax == 1.0) {
				valorMax = valores.get(Constantes.nombreCampoPasajerosPerdidosPorCompañía).get(indMax);
				indMax--;
			}
		}
		valoresSalida.put(Constantes.nombreCampoPasajerosPerdidosPorCompañía, List.of(df.format(valorMin), df.format(valorMax)));
		
		
		indMin = 0;
		indMax = valores.get(Constantes.nombreCampoIngresoPerdidoPorAreaInf).size()-1;
		
		valorMin = valores.get(Constantes.nombreCampoIngresoPerdidoPorAreaInf).get(indMin);
		valorMax = valores.get(Constantes.nombreCampoIngresoPerdidoPorAreaInf).get(indMax);
		
		while(indMin < indMax && (valorMin == 0.0 || valorMax == 1.0)) {
			if(valorMin == 0.0) {
				valorMin = valores.get(Constantes.nombreCampoIngresoPerdidoPorAreaInf).get(indMin);
				indMin++;
			}
			if(valorMax == 1.0) {
				valorMax = valores.get(Constantes.nombreCampoIngresoPerdidoPorAreaInf).get(indMax);
				indMax--;
			}
		}
		valoresSalida.put(Constantes.nombreCampoIngresoPerdidoPorAreaInf, List.of(df.format(valorMin), df.format(valorMax)));
		
		
		indMin = 0;
		indMax = valores.get(Constantes.nombreCampoIngresoPerdidoPorAerDest).size()-1;
		
		valorMin = valores.get(Constantes.nombreCampoIngresoPerdidoPorAerDest).get(indMin);
		valorMax = valores.get(Constantes.nombreCampoIngresoPerdidoPorAerDest).get(indMax);
		
		while(indMin < indMax && (valorMin == 0.0 || valorMax == 1.0)) {
			if(valorMin == 0.0) {
				valorMin = valores.get(Constantes.nombreCampoIngresoPerdidoPorAerDest).get(indMin);
				indMin++;
			}
			if(valorMax == 1.0) {
				valorMax = valores.get(Constantes.nombreCampoIngresoPerdidoPorAerDest).get(indMax);
				indMax--;
			}
		}
		valoresSalida.put(Constantes.nombreCampoIngresoPerdidoPorAerDest, List.of(df.format(valorMin), df.format(valorMax)));
		
		
		return valoresSalida;
	}
	
	public static Map<Integer, String> obtenerTooltips(DatosRRPS_PAT datos){
		Map<Integer, String> tooltips = new HashMap<>();
		DecimalFormat df = new DecimalFormat("0.00");
		
		Double IngresosTtotalSuma = 0.0;
		int Pasajerostotal = 0;
		Double Tasastotal = 0.0;
		
		List<String> areasInf = new ArrayList<>();
		List<String> companyias = new ArrayList<>();
		List<String> aerOr = new ArrayList<>();
		List<String> aerDest = new ArrayList<>();
		
		int pos = 0;
		for (int i = 0; i < datos.getConexionesTotales().size(); i++) {
			while (pos < datos.getConexionesTotalesSeparadas().size()
					&& datos.getConexionesTotalesSeparadas().get(pos).get(0)
							.equals(datos.getConexionesTotales().get(i).get(0))
					&& datos.getConexionesTotalesSeparadas().get(pos).get(1)
							.equals(datos.getConexionesTotales().get(i).get(1))) {

				IngresosTtotalSuma += datos.getIngresos().get(pos);

				Pasajerostotal += datos.getPasajeros().get(pos);

				Tasastotal += datos.getTasas().get(pos);
				
				if(!areasInf.contains(datos.getAresInfTotales().get(pos))) {
					areasInf.add(datos.getAresInfTotales().get(pos));
				}
				if(!companyias.contains(datos.getCompanyiasTotales().get(pos))) {
					companyias.add(datos.getCompanyiasTotales().get(pos));
				}

				/*if (pasajerosPorCompanyia.get(this.datos.getCompanyiasTotales().get(pos)) != null) {
					pasajerosPorCompanyia.put(this.datos.getCompanyiasTotales().get(pos),
							List.of(pasajerosPorCompanyia.get(this.datos.getCompanyiasTotales().get(pos)).get(0)
									+ this.datos.getPasajeros().get(pos) * solucion.getVariables().get(i),
									pasajerosPorCompanyia.get(this.datos.getCompanyiasTotales().get(pos)).get(1)
											+ this.datos.getPasajeros().get(pos)));
				} else {
					pasajerosPorCompanyia.put(this.datos.getCompanyiasTotales().get(pos),
							List.of(this.datos.getPasajeros().get(pos) * solucion.getVariables().get(i),
									this.datos.getPasajeros().get(pos) * 1.0));
				}
				if (ingresosPorAreaInf.get(this.datos.getAresInfTotales().get(pos)) != null) {
					ingresosPorAreaInf.put(this.datos.getAresInfTotales().get(pos),
							List.of(ingresosPorAreaInf.get(this.datos.getAresInfTotales().get(pos)).get(0)
									+ this.datos.getIngresos().get(pos) * solucion.getVariables().get(i),
									ingresosPorAreaInf.get(this.datos.getAresInfTotales().get(pos)).get(1)
											+ this.datos.getIngresos().get(pos)));
				} else {
					ingresosPorAreaInf.put(this.datos.getAresInfTotales().get(pos),
							List.of(this.datos.getIngresos().get(pos) * solucion.getVariables().get(i),
									this.datos.getIngresos().get(pos)));
				}
				if (ingresosPorAerDest.get(this.datos.getConexionesTotalesSeparadas().get(pos).get(1)) != null) {
					ingresosPorAerDest.put(this.datos.getConexionesTotalesSeparadas().get(pos).get(1), List.of(
							ingresosPorAerDest.get(this.datos.getConexionesTotalesSeparadas().get(pos).get(1)).get(0)
									+ this.datos.getTasas().get(pos) * solucion.getVariables().get(i),
							ingresosPorAerDest.get(this.datos.getConexionesTotalesSeparadas().get(pos).get(1)).get(0)
									+ this.datos.getTasas().get(pos)));
				} else {
					ingresosPorAerDest.put(this.datos.getConexionesTotalesSeparadas().get(pos).get(1),
							List.of(this.datos.getTasas().get(pos) * solucion.getVariables().get(i),
									this.datos.getTasas().get(pos)));
				}*/

				pos++;
			}
			if(!aerDest.contains(datos.getConexionesTotales().get(i).get(1))) {
				aerDest.add(datos.getConexionesTotales().get(i).get(1));
			}
			if(!aerOr.contains(datos.getConexionesTotales().get(i).get(0))) {
				aerOr.add(datos.getConexionesTotales().get(i).get(0));
			}

			/*if (!this.datos.getConexionesTotales().get(i).get(0).equals(origen)) {
				if (i > 0) {
					Double Conectividadaux = 0.0;
					if (ConectividadauxTotalSuma != 0) {
						Conectividadaux = ConectividadauxSuma / ConectividadauxTotalSuma;
					}
					Conectividadsuma += this.datos.getConectividadesTotales().get(i - 1) * (1 - Conectividadaux);
					ConectividadtotalSuma += this.datos.getConectividadesTotales().get(i - 1);
				}
				// Sumar las conectividades de los destinos y guardar el origen
				origen = this.datos.getConexionesTotales().get(i).get(0);
				ConectividadauxSuma = 0.0;
				ConectividadauxTotalSuma = 0.0;
			}
			// origen = this.datos.getConexionesTotales().get(i).get(0);
			ConectividadauxSuma += solucion.getVariables().get(i)
					* this.datos.getVuelosEntrantesConexionOrdenadoTotales().get(i);
			ConectividadauxTotalSuma += this.datos.getVuelosEntrantesConexionOrdenadoTotales().get(i);*/
		}
		
		tooltips.put(Constantes.idObjetivo1, "The losses in the catchment areas if all connections are cancelled would be " + String.valueOf(df.format(IngresosTtotalSuma)) + " euros.");
		tooltips.put(Constantes.idObjetivo2, "The number of catchment areas with losses if all connections are cancelled would be " + String.valueOf(areasInf.size()) + ".");
		tooltips.put(Constantes.idObjetivo3, "The number of airlines with losses if all connections are cancelled would be " + String.valueOf(companyias.size()) + ".");
		tooltips.put(Constantes.idObjetivo4, "The destination airport losses if all connections are cancelled would be " + String.valueOf(df.format(Tasastotal)) + " euros.");
		tooltips.put(Constantes.idObjetivo5, "The number of destination airports with losses if all connections are cancelled would be " + String.valueOf(aerDest.size()) + ".");
		tooltips.put(Constantes.idObjetivo6, "The number of affected passengers if all connections are cancelled would be " + String.valueOf(Pasajerostotal) + ".");
		tooltips.put(Constantes.idObjetivo7, "The number of origin airports with reduced communication if all connections are cancelled would be " + String.valueOf(aerOr.size()) + ".");
		
		return tooltips;
	}
	
	public static Map<String, String> leerCSVUsuarios() throws IOException, CsvException {
		List<List<String>> fichero = new ArrayList<>();
		int numFilas;
		try (CSVReader reader = new CSVReader(new FileReader(Constantes.rutaFicheros + Constantes.nombreFicheroUsuarios + Constantes.extensionFichero))) {
			List<String[]> r = reader.readAll();
			numFilas = r.size();
			for(int fila = 0; fila < numFilas; fila++) {
				List<String> filaI = new ArrayList<>();
				for(int columna = 0; columna < r.get(fila).length; columna++) {
					
					filaI.add(r.get(fila)[columna]);
			    }
				fichero.add(filaI);
			}
		}
		
		Map<String, String> usuarios = new HashMap<>();
		
		for(List<String> usuario : fichero) {
			usuarios.put(usuario.get(0), usuario.get(1));
		}
		
		return usuarios;
		
	}
	
	public static String modificarCSV(String nombre, List<Individuo> listanueva) throws IOException, CsvException {
		String fileName = nombre + Constantes.extensionFichero;
		if(listanueva.size() == 0) {
			return fileName;
		}
		else {
			List<Individuo> listaPrevia = leerCSV(nombre + Constantes.extensionFichero);
			listaPrevia = juntarListas(listaPrevia, listanueva);
			List<String[]> lista = new ArrayList<>();
			
			String[] Cabecera = new String[2];
			Cabecera[0] = String.valueOf(listanueva.get(0).getVariables().size());
			Cabecera[1] = String.valueOf(listanueva.get(0).getObjetivos().size());
			
			lista.add(Cabecera);
			
			for(int i = 0; i < listaPrevia.size(); i++) {
				Individuo ind = listaPrevia.get(i);
				String[] VariablesYObjetivos = new String[ind.getVariables().size()
				                                          + ind.getObjetivos().size()];
				
				for(int j = 0; j < ind.getVariables().size(); j++) {
					VariablesYObjetivos[j] = String.valueOf(ind.getVariables().get(j));
				}
				for(int k = ind.getVariables().size(); k < ind.getVariables().size() + ind.getObjetivos().size(); k++) {
					VariablesYObjetivos[k] = String.valueOf(ind.getObjetivos().get(k - ind.getVariables().size()));
				}
				lista.add(VariablesYObjetivos);
			}
			
			
			try (CSVWriter writer = new CSVWriter(new FileWriter(Constantes.rutaFicheros + fileName))) {
		            writer.writeAll(lista);
			}
			return fileName;
		}
		
	}
	
	public static void crearDirectorioProyecto(String nombre) throws IOException {
		Path path = Paths.get(Constantes.rutaFicherosProyectos + "\\" + nombre);
		Files.createDirectories(path);
	}
	
	public static void crearDirectorioFitness(String nombre) throws IOException {
		Path path = Paths.get(Constantes.rutaFicherosProyectos + "\\" + nombre + "\\" + Constantes.nombreDirectorioFicherosFitness);
		Files.createDirectories(path);
	}
	
	public static void crearDirectorioObjetivos(String nombre) throws IOException {
		Path path = Paths.get(Constantes.rutaFicherosProyectos + "\\" + nombre + "\\" + Constantes.nombreDirectorioFicherosObjetivos);
		Files.createDirectories(path);
	}
	
	public static void crearDirectorioRangos(String nombre) throws IOException {
		Path path = Paths.get(Constantes.rutaFicherosProyectos + "\\" + nombre + "\\" + Constantes.nombreDirectorioFicherosRangos);
		Files.createDirectories(path);
	}
	
	public static int encontrarIndiceEnLista(List<List<String>> listaConexiones, List<String> origenDestino){
		int num = 0;
		int indice = 0;
		boolean encontrado = false;
		
		while(!encontrado && indice < listaConexiones.size()) {
			if (listaConexiones.get(indice).equals(origenDestino)) {
				num = indice;
				encontrado = true;
			}
			indice++;
		}
		/*for (int i = 0; i < listaConexiones.size(); i++) {
			if (listaConexiones.get(i).equals(origenDestino)) {
				num = i;
			}
		}*/
		return num;
	}

	public static Double mediaDeValoresObjetivo(List<Double> valores) {
		double suma = 0.0;
		for (int i = 0; i < valores.size(); i++) {
			suma += valores.get(i);
		}
		return suma / valores.size();
	}
	
	public static List<Double> copiarValoresDeLista(List<Double> listaaCopiar){
		List<Double> nueva = new ArrayList<>();
		for(Double num : listaaCopiar) {
			nueva.add(num);
		}
		return nueva;
	}
	
	public static Double sumaPonderada(Individuo ind, List<Double> pesos) {
		double sumaP = 0;
		for(int i = 0; i < ind.getObjetivos().size(); i++) {
			sumaP = sumaP + ind.getObjetivos().get(i) * pesos.get(i);
		}
		return sumaP;
	}
	
	public static List<Individuo> quitarDuplicados(List<Individuo> p){
		List<Individuo> indAQuitar = new ArrayList<Individuo>();
		for(int i = 0; i < p.size(); i++) {
			Individuo target = p.get(i);
			for(int j = i + 1; j < p.size(); j++) {
				if(target.getObjetivos().equals(p.get(j).getObjetivos())) {
					indAQuitar.add(p.get(j));
				}
			}
		}
		for(Individuo ind : indAQuitar) {
			p.remove(ind);
		}
		return p;
	}
	
	public static boolean listasIguales(List<Individuo> p, List<Individuo> q) {
		boolean iguales = true;
		
		if(p.size() == q.size()) {
			int i = 0;
			while(iguales && i < p.size()) {
				int j = 0;
				boolean existe = false;
				while(!existe && j < q.size()) {
					if(p.get(i).getObjetivos().equals(q.get(j).getObjetivos())) {
						existe = true;
					}
					j++;
				}
				if(!existe) {
					iguales = false;
				}
				i++;
			}
		}
		else {
			iguales = false;
		}
		return iguales;
	}
	
	public static Individuo copiarIndividuo (Individuo ind){
		Individuo nuevo = new Individuo(ind.getVariables().size(), ind.getObjetivos().size());
		List<Double> variables = ind.getVariables();
		int domina = ind.getdomina();
		List<Double> objetivos = ind.getObjetivos();
		List<Double> objetivosNorm = ind.getObjetivosNorm();
		List<Double> restricciones = ind.getRestricciones();
		boolean factible = ind.isFactible();
		Double constraintViolation = ind.getConstraintViolation();
		
		nuevo.setConstraintViolation(constraintViolation);
		nuevo.setdomina(domina);
		nuevo.setFactible(factible);
		nuevo.setObjetivos(copiarLista(objetivos));
		nuevo.setObjetivosNorm(copiarLista(objetivosNorm));
		nuevo.setRestricciones(copiarLista(restricciones));
		nuevo.setVariables(copiarLista(variables));
		
		return nuevo;
	}
	
	public static List<Double> copiarLista (List<Double> lista){
		List<Double> nueva = new ArrayList<>();
		if(lista != null) {
			for(Double i : lista) {
				nueva.add(i);
			}
		}
		return nueva;
	}
	
	public static List<Double> rellenarListaDeCeros(List<Double> lista, int numCeros){
		for(int i = 0; i < numCeros; i++) {
			lista.add(0.0);
		}
		return lista;
	}
	
	public static void crearFicheroConDatosDiaI(List<String[]> datosFichero, String nombre) throws IOException {
		try (CSVWriter writer = new CSVWriter(new FileWriter(Constantes.rutaDatosPorDia + nombre + Constantes.extensionFichero), ',', 
                CSVWriter.NO_QUOTE_CHARACTER, 
                CSVWriter.DEFAULT_ESCAPE_CHARACTER, 
                CSVWriter.DEFAULT_LINE_END)) {
			
			
            writer.writeAll(datosFichero);
	}
	}
}
