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
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.tomcat.util.bcel.Const;
import org.springframework.util.FileSystemUtils;

import com.VisNeo4j.App.Algoritmo.Parametros.BPSOParams;
import com.VisNeo4j.App.Constantes.Constantes;
import com.VisNeo4j.App.Modelo.Individuo;
import com.VisNeo4j.App.Modelo.Poblacion;
import com.VisNeo4j.App.Modelo.Salida.Aeropuerto;
import com.VisNeo4j.App.Modelo.Salida.BPSOParamsSalida;
import com.VisNeo4j.App.Modelo.Salida.Conexion;
import com.VisNeo4j.App.Modelo.Salida.DatosConexiones;
import com.VisNeo4j.App.Modelo.Salida.FechasProyecto;
import com.VisNeo4j.App.Modelo.Salida.FitnessI;
import com.VisNeo4j.App.Modelo.Salida.Objetivo;
import com.VisNeo4j.App.Modelo.Salida.OrdenObjSalida;
import com.VisNeo4j.App.Modelo.Salida.Restricciones;
import com.VisNeo4j.App.Modelo.Salida.TraducirSalida;
import com.VisNeo4j.App.Problemas.Problema;
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
		} else
			return 1.0;
	}

	public static boolean isProbValid(Double prob) {
		if ((prob < 0.0) || (prob > 1.0)) {
			return false;
		} else
			return true;
	}

	public static Double repararValorFueraDelRango(Double valor, Double min, Double max) {
		if (valor < min) {
			return min;
		} else if (valor > max) {
			return max;
		} else {
			return valor;
		}
	}

	public static List<Double> decodificarBinario(int bitsEnteros, int bitsDecimales, List<Double> bits) {
		int numVariDecimales = (bits.size()) / (1 + bitsEnteros + bitsDecimales);
		List<Double> valoresDecimales = new ArrayList<>();
		
		int offset = 0;
		for (int i = 0; i < numVariDecimales; i++) {
			Double parteEntera = 0.0;
			Double parteDecimal = 0.0;
			Double total = 0.0;
			boolean positivo;
			
			if(bits.get(offset) == 1.0) {
				positivo = false;
			}else {
				positivo = true;
			}
			
			offset++;
			
			// Parte entera
			for (int j = 0; j < bitsEnteros; j++) {
				if(bits.get(offset) == 1.0) {
					parteEntera += Math.pow(2, bitsEnteros - j - 1);
				}
				offset++;
			}
			
			// Parte decimal
			for (int j = 0; j < bitsDecimales; j++) {
				if(bits.get(offset) == 1.0) {
					parteDecimal += Math.pow(2, -(j + 1));
				}
				offset++;
			}
			total = parteEntera + parteDecimal;
			if(!positivo) {
				total *= -1;
			}
			
			valoresDecimales.add(total);
		}
		return valoresDecimales;
	}
	
	public static int distanciaHamming(List<Double> a, List<Double> b) {
		int dist = 0;
		for(int i = 0; i < a.size(); i++) {
			double a1 = a.get(i);
			double b1 = b.get(i);
			if(a1 != b1) {
				dist++;
			}
		}
		return dist;
	}
	
	public static Double calcularMenor(List<Individuo> lista) {
		Double menor = Double.MAX_VALUE;
		
		for(int i = 0; i < lista.size(); i++) {
			if(lista.get(i).getObjetivos().get(0) < menor) {
				menor = lista.get(i).getObjetivos().get(0);
			}
		}
		
		return menor;
	}
	
	public static Double calcularMayor(List<Individuo> lista) {
		Double mayor = Double.MIN_VALUE;
		
		for(int i = 0; i < lista.size(); i++) {
			if(lista.get(i).getObjetivos().get(0) > mayor) {
				mayor = lista.get(i).getObjetivos().get(0);
			}
		}
		
		return mayor;
	}
	
	public static Double calcularAVGF(List<Individuo> lista) {
		Double media = 0.0;
		
		for(int i = 0; i < lista.size(); i++) {
			media += lista.get(i).getObjetivos().get(0);
		}
		
		return media/lista.size();
	}
	
	public static Double calcularSTDF(List<Individuo> lista) {
		Double std = 0.0;
		Double media = 0.0;
		
		for(int i = 0; i < lista.size(); i++) {
			media += lista.get(i).getObjetivos().get(0);
		}
		
		media /= lista.size();
		
		for(int i = 0; i < lista.size(); i++) {
			std += Math.pow(lista.get(i).getObjetivos().get(0) - media, 2);
		}
		
		std /= lista.size();
		
		std = Math.sqrt(std);
		
		return std;
	}
	
	public static Double calcularSR(List<Individuo> lista, Double opt) {
		Double SR = 0.0;
		Double cont = 0.0;
		
		for(int i = 0; i < lista.size(); i++) {
			if(lista.get(i).getObjetivos().get(0).equals(opt)) {
				cont++;
			}
		}
		System.out.println("Hit: " + cont);
		
		SR = cont / (lista.size() * 1.0);
		
		return SR * 100.0;
	}
	
	public static Double calcularMinIter(List<Individuo> lista) {
		Double menor = Double.MAX_VALUE;
		
		for(int i = 0; i < lista.size(); i++) {
			Double valor = lista.get(i).getObjetivos().get(0);
			int pos = lista.get(i).getFitnessHist().indexOf(valor);
			
			if(pos < menor) {
				menor = pos * 1.0;
			}
		}
		
		return menor;
	}
	
	public static Double calcularMaxIter(List<Individuo> lista) {
		Double mayor = Double.MIN_VALUE;
		
		for(int i = 0; i < lista.size(); i++) {
			Double valor = lista.get(i).getObjetivos().get(0);
			int pos = lista.get(i).getFitnessHist().indexOf(valor);
			
			if(pos > mayor) {
				mayor = pos * 1.0;
			}
		}
		
		return mayor;
	}
	
	public static Double calcularAVGIter(List<Individuo> lista) {
		Double media = 0.0;
		
		for(int i = 0; i < lista.size(); i++) {
			Double valor = lista.get(i).getObjetivos().get(0);
			int pos = lista.get(i).getFitnessHist().indexOf(valor);
			
			media += pos;
		}
		
		return media / lista.size();
	}
	
	public static Double calcularSTDIter(List<Individuo> lista) {
		Double std = 0.0;
		Double media = 0.0;
		List<Integer> posiciones = new ArrayList<>();
		
		for(int i = 0; i < lista.size(); i++) {
			Double valor = lista.get(i).getObjetivos().get(0);
			int pos = lista.get(i).getFitnessHist().indexOf(valor);
			posiciones.add(pos);
			
			media += pos;
		}
		
		media /= lista.size();
		
		for(int i = 0; i < posiciones.size(); i++) {
			std += Math.pow(posiciones.get(i) - media, 2);
		}
		
		std /= posiciones.size();
		
		std = Math.sqrt(std);
		
		return std;
	}

	public static Poblacion juntarPoblaciones(Poblacion padres, Poblacion hijos, Problema problema) {
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

	public static List<Individuo> obtenerFrenteConIndice(Poblacion p, int pos) {
		List<Individuo> frente = new ArrayList<>();
		frente.add(p.getPoblacion().get(pos));
		pos++;
		while (pos < p.getPoblacion().size()) {
			if (frente.get(0).getdomina() == p.getPoblacion().get(pos).getdomina()) {
				frente.add(p.getPoblacion().get(pos));
			}
			pos++;
		}
		return frente;
	}

	public static List<Individuo> obtenerFrenteConIndice(List<Individuo> p, int pos) {
		List<Individuo> frente = new ArrayList<>();
		frente.add(p.get(pos));
		pos++;
		while (pos < p.size()) {
			if (frente.get(0).getdomina() == p.get(pos).getdomina()) {
				frente.add(p.get(pos));
			}
			pos++;
		}
		return frente;
	}

	public static Poblacion borrarElementosDeLista(List<Individuo> lista, Poblacion p) {
		List<Individuo> poblacionABorrar = p.getPoblacion();
		for (int i = 0; i < lista.size(); i++) {
			Individuo ind = lista.get(i);
			poblacionABorrar.remove(ind);
		}
		p.setPoblacion(poblacionABorrar);
		return p;
	}

	public static List<Individuo> borrarElementosDeLista(List<Individuo> lista, List<Individuo> p) {
		List<Individuo> poblacionABorrar = p;
		for (int i = 0; i < lista.size(); i++) {
			Individuo ind = lista.get(i);
			poblacionABorrar.remove(ind);
		}
		p = poblacionABorrar;
		return p;
	}

	public static List<Individuo> juntarListas(List<Individuo> Alista, List<Individuo> frente) {
		for (Individuo i : frente) {
			Alista.add(i);
		}
		return Alista;
	}

	public static List<Double> inicializarLista(int tamaño) {
		List<Double> lista = new ArrayList<>();
		for (int i = 0; i < tamaño; i++) {
			lista.add(0.0);
		}
		return lista;
	}

	public static int nextInt(int lower, int upper) {
		Random rand = new Random();
		return lower + rand.nextInt((upper - lower + 1));
	}

	public static List<Double> ArraytoArrayList(double[] array) {
		List<Double> list = new ArrayList<>(array.length);
		for (int i = 0; i < array.length; i++) {
			list.add(array[i]);
		}
		return list;
	}
	
	public static void crearFicheroConDatosDiaI(List<String[]> datosFichero, String nombre) throws IOException {
		try (CSVWriter writer = new CSVWriter(
				new FileWriter(Constantes.rutaDatosPorDia + nombre + Constantes.extensionFichero), ',',
				CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END)) {
			writer.writeAll(datosFichero);
		}
	}

	public static List<Individuo> leerCSV(String nombre) throws FileNotFoundException, IOException, CsvException {
		try (CSVReader reader = new CSVReader(new FileReader(Constantes.rutaFicheros + nombre))) {
			List<String[]> r = reader.readAll();
			List<Individuo> frente = new ArrayList<>();

			int numVariables = Integer.valueOf((r.get(0)[0]));
			int numObjetivos = Integer.valueOf((r.get(0)[1]));

			for (int i = 1; i < r.size(); i++) {
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

	public static List<String> leerCSVconexion(String proyecto, int numFila)
			throws FileNotFoundException, IOException, CsvException {
		try (CSVReader reader = new CSVReader(new FileReader(Constantes.rutaFicherosProyectos + "//" + proyecto + "//"
				+ Constantes.nombreFicheroConexiones + Constantes.extensionFichero))) {
			List<String[]> r = reader.readAll();
			List<String> conexiones = new ArrayList<>();

			for (int i = 0; i < r.get(numFila).length; i++) {

				conexiones.add(r.get(numFila)[i]);

			}
			return conexiones;
		}
	}

	public static List<String> leerCSVproblema(String proyecto, int numFila)
			throws FileNotFoundException, IOException, CsvException {
		try (CSVReader reader = new CSVReader(new FileReader(Constantes.rutaFicherosProyectos + "//" + proyecto + "//"
				+ Constantes.nombreProblemaRRPS_PAT + Constantes.extensionFichero))) {
			List<String[]> r = reader.readAll();
			List<String> solucion = new ArrayList<>();
			if (r.size() > 0) {
				for (int i = 0; i < r.get(numFila).length; i++) {

					solucion.add(r.get(numFila)[i]);
				}
			}
			return solucion;
		}
	}

	public static List<Aeropuerto> obtenerSolucionDiaI(String proyecto, int numFila, int dia)
			throws FileNotFoundException, IOException, CsvException {
		List<String> solucion = leerCSVproblema(proyecto, numFila);
		List<String> conexiones = leerCSVconexion(proyecto, Integer.valueOf(solucion.get(0)));
		List<String> numConDia = leerCSVnumDiasYCon(proyecto, Integer.valueOf(solucion.get(1)));
		int offsetBits = 0;
		for (int i = 0; i < dia; i++) {
			offsetBits += Integer.valueOf(numConDia.get(i));
		}
		solucion.remove(0);
		solucion.remove(0);
		List<Double> variablesDouble = new ArrayList<>();
		List<String> variablesString = solucion.subList(offsetBits, offsetBits + Integer.valueOf(numConDia.get(dia)));
		for (int i = 0; i < variablesString.size(); i++) {
			variablesDouble.add(Double.valueOf(variablesString.get(i)));
		}

		return TraducirSalida.añadirCoordenadas(variablesDouble,
				conexiones.subList(offsetBits * 2, (offsetBits * 2) + Integer.valueOf(numConDia.get(dia)) * 2));
	}

	public static List<Aeropuerto> obtenerSolucionDiaI(int dia, List<Double> variablesDouble, DatosRRPS_PAT datos)
			throws FileNotFoundException, IOException, CsvException {
		int offsetBits = 0;
		for (int i = 0; i < dia; i++) {
			offsetBits += Integer.valueOf(datos.getDatosPorDia().get(i).getConexiones().size());
		}
		variablesDouble = variablesDouble.subList(offsetBits,
				offsetBits + Integer.valueOf(datos.getDatosPorDia().get(dia).getConexiones().size()));

		return TraducirSalida.añadirCoordenadas(datos.getDatosPorDia().get(dia).getConexiones());
	}

	public static List<Double> obtenerBitsSolDiaI(String proyecto, int sol, int dia)
			throws FileNotFoundException, IOException, CsvException {
		List<String> solucion = leerCSVproblema(proyecto, sol);
		List<String> numConDia = leerCSVnumDiasYCon(proyecto, Integer.valueOf(solucion.get(1)));
		int offsetBits = 0;
		for (int i = 0; i < dia; i++) {
			offsetBits += Integer.valueOf(numConDia.get(i));
		}
		solucion.remove(0);
		solucion.remove(0);
		List<Double> variablesDouble = new ArrayList<>();
		List<String> variablesString = solucion.subList(offsetBits, offsetBits + Integer.valueOf(numConDia.get(dia)));
		for (int i = 0; i < variablesString.size(); i++) {
			variablesDouble.add(Double.valueOf(variablesString.get(i)));
		}

		return variablesDouble;
	}

	public static List<Double> obtenerBitsSolDiaI(int dia, List<Double> variablesDouble, DatosRRPS_PAT datos)
			throws FileNotFoundException, IOException, CsvException {
		int offsetBits = 0;
		for (int i = 0; i < dia; i++) {
			offsetBits += Integer.valueOf(datos.getDatosPorDia().get(i).getConexiones().size());
		}
		variablesDouble = variablesDouble.subList(offsetBits,
				offsetBits + Integer.valueOf(datos.getDatosPorDia().get(dia).getConexiones().size()));

		return variablesDouble;
	}

	public static int obtenernumDiasSolucionI(String proyecto, int sol)
			throws FileNotFoundException, IOException, CsvException {
		List<String> solucion = leerCSVproblema(proyecto, sol);
		List<String> numConDia = leerCSVnumDiasYCon(proyecto, Integer.valueOf(solucion.get(1)));

		return numConDia.size();
	}

	public static List<String> leerCSVnumDiasYCon(String proyecto, int numFila)
			throws FileNotFoundException, IOException, CsvException {
		try (CSVReader reader = new CSVReader(new FileReader(Constantes.rutaFicherosProyectos + "//" + proyecto + "//"
				+ Constantes.nombreFicheroNumConDia + Constantes.extensionFichero))) {
			List<String[]> r = reader.readAll();
			List<String> solucion = new ArrayList<>();
			if (r.size() > 0) {
				for (int i = 0; i < r.get(numFila).length; i++) {

					solucion.add(r.get(numFila)[i]);
				}
			}
			return solucion;
		}
	}

	public static List<List<String>> leerCSVnombre(String nombreFichero, String nombreProyecto)
			throws FileNotFoundException, IOException, CsvException {
		List<List<String>> fichero = new ArrayList<>();
		int numFilas;
		try (CSVReader reader = new CSVReader(new FileReader(Constantes.rutaFicherosProyectos + "//" + nombreProyecto
				+ "//" + nombreFichero + Constantes.extensionFichero))) {
			List<String[]> r = reader.readAll();
			numFilas = r.size();
			for (int fila = 0; fila < numFilas; fila++) {
				List<String> filaI = new ArrayList<>();
				for (int columna = 0; columna < r.get(fila).length; columna++) {

					filaI.add(r.get(fila)[columna]);
				}
				fichero.add(filaI);
			}
		}
		return fichero;
	}

	public static List<List<String>> leerCSVHistFitness(String proyecto, String id)
			throws FileNotFoundException, IOException, CsvException {
		List<List<String>> fichero = new ArrayList<>();
		int numFilas;
		try (CSVReader reader = new CSVReader(new FileReader(Constantes.rutaFicherosProyectos + "//" + proyecto + "//"
				+ Constantes.nombreDirectorioFicherosFitness + "//" + id + Constantes.extensionFichero))) {
			List<String[]> r = reader.readAll();
			numFilas = r.size();
			for (int fila = 0; fila < numFilas; fila++) {
				List<String> filaI = new ArrayList<>();
				for (int columna = 0; columna < r.get(fila).length; columna++) {

					filaI.add(r.get(fila)[columna]);
				}
				fichero.add(filaI);
			}
		}
		return fichero;
	}

	public static List<Double> leerCSVHistFitnessTemp(String proyecto, String id)
			throws FileNotFoundException, IOException, CsvException {
		List<Double> fichero = new ArrayList<>();
		int numFilas;
		try (CSVReader reader = new CSVReader(new FileReader(Constantes.rutaFicherosProyectos + "//" + proyecto + "//"
				+ Constantes.nombreDirectorioFicherosFitness + "//" + id + Constantes.extensionFichero))) {
			List<String[]> r = reader.readAll();
			numFilas = r.size();
			for (int fila = 0; fila < numFilas; fila++) {

				fichero.add(Double.valueOf(r.get(fila)[1]));
			}
		}
		return fichero;
	}

	public static List<List<String>> leerCSVObjetivos(String proyecto, String id)
			throws FileNotFoundException, IOException, CsvException {
		List<List<String>> fichero = new ArrayList<>();
		int numFilas;
		try (CSVReader reader = new CSVReader(new FileReader(Constantes.rutaFicherosProyectos + "//" + proyecto + "//"
				+ Constantes.nombreDirectorioFicherosObjetivos + "//" + id + Constantes.extensionFichero))) {
			List<String[]> r = reader.readAll();
			numFilas = r.size();
			for (int fila = 0; fila < numFilas; fila++) {
				List<String> filaI = new ArrayList<>();
				for (int columna = 0; columna < r.get(fila).length; columna++) {

					filaI.add(r.get(fila)[columna]);
				}
				fichero.add(filaI);
			}
		}
		return fichero;
	}

	public static int modificarCSVconexiones(List<List<String>> conexiones, String nombre)
			throws IOException, CsvException {
		if (conexiones.size() == 0) {
			return -1;
		} else {
			File file = new File(Constantes.rutaFicherosProyectos + "//" + nombre + "//"
					+ Constantes.nombreFicheroConexiones + Constantes.extensionFichero);
			List<List<String>> listaPrevia;
			if (file.exists()) {
				listaPrevia = leerCSVnombre(Constantes.nombreFicheroConexiones, nombre);
			} else {
				listaPrevia = new ArrayList<>();
			}

			int pos = listaPrevia.size();
			List<String> listaNueva = new ArrayList<>();
			for (int i = 0; i < conexiones.size(); i++) {
				listaNueva.add(conexiones.get(i).get(0));
				listaNueva.add(conexiones.get(i).get(1));
			}

			for (int i = 0; i < listaPrevia.size(); i++) {
				if (comprobarListasIguales(listaNueva, listaPrevia.get(i))) {
					pos = i;
				}
			}
			if (pos < listaPrevia.size()) {
				return pos;
			} else {
				List<String[]> lista = new ArrayList<>();
				listaPrevia.add(listaNueva);
				for (int i = 0; i < listaPrevia.size(); i++) {
					String[] fila = new String[listaPrevia.get(i).size()];
					for (int j = 0; j < listaPrevia.get(i).size(); j++) {
						fila[j] = listaPrevia.get(i).get(j);
					}
					lista.add(fila);
				}
				try (CSVWriter writer = new CSVWriter(
						new FileWriter(Constantes.rutaFicherosProyectos + "//" + nombre + "//"
								+ Constantes.nombreFicheroConexiones + Constantes.extensionFichero),
						',', CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.DEFAULT_ESCAPE_CHARACTER,
						CSVWriter.DEFAULT_LINE_END)) {
					writer.writeAll(lista);
				}
				return listaPrevia.size() - 1;
			}

		}

	}

	public static int modificarCSVNumConYDias(List<String> numConexionesPorDia, String nombre)
			throws IOException, CsvException {
		if (numConexionesPorDia.size() == 0) {
			return -1;
		} else {
			File file = new File(Constantes.rutaFicherosProyectos + "//" + nombre + "//"
					+ Constantes.nombreFicheroNumConDia + Constantes.extensionFichero);
			List<List<String>> listaPrevia;
			if (file.exists()) {
				listaPrevia = leerCSVnombre(Constantes.nombreFicheroNumConDia, nombre);
			} else {
				listaPrevia = new ArrayList<>();
			}

			int pos = listaPrevia.size();

			for (int i = 0; i < listaPrevia.size(); i++) {
				if (comprobarListasIguales(numConexionesPorDia, listaPrevia.get(i))) {
					pos = i;
				}
			}
			if (pos < listaPrevia.size()) {
				return pos;
			} else {
				List<String[]> lista = new ArrayList<>();
				listaPrevia.add(numConexionesPorDia);
				for (int i = 0; i < listaPrevia.size(); i++) {
					String[] fila = new String[listaPrevia.get(i).size()];
					for (int j = 0; j < listaPrevia.get(i).size(); j++) {
						fila[j] = listaPrevia.get(i).get(j);
					}
					lista.add(fila);
				}
				try (CSVWriter writer = new CSVWriter(
						new FileWriter(Constantes.rutaFicherosProyectos + "//" + nombre + "//"
								+ Constantes.nombreFicheroNumConDia + Constantes.extensionFichero),
						',', CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.DEFAULT_ESCAPE_CHARACTER,
						CSVWriter.DEFAULT_LINE_END)) {
					writer.writeAll(lista);
				}
				return listaPrevia.size() - 1;
			}

		}

	}

	public static String modificarCSVproblemaRRPS_PAT(Individuo ind, DatosRRPS_PAT datos, String nombre)
			throws IOException, CsvException {
		int filaConexiones = modificarCSVconexiones(datos.getConexionesTotales(), nombre);
		List<String> numConDia = new ArrayList<>();
		for (DatosRRPS_PATDiaI dato : datos.getDatosPorDia()) {
			numConDia.add(String.valueOf(dato.getConexiones().size()));
		}
		int filaNumDiasYCon = modificarCSVNumConYDias(numConDia, nombre);

		File file = new File(Constantes.rutaFicherosProyectos + "//" + nombre + "//" + Constantes.nombreProblemaRRPS_PAT
				+ Constantes.extensionFichero);
		List<List<String>> solucionesExistentes;
		if (file.exists()) {
			solucionesExistentes = leerCSVnombre(Constantes.nombreProblemaRRPS_PAT, nombre);
		} else {
			solucionesExistentes = new ArrayList<>();
		}

		List<String> solucionNueva = new ArrayList<>();
		solucionNueva.add(String.valueOf(filaConexiones));
		solucionNueva.add(String.valueOf(filaNumDiasYCon));
		for (int i = 0; i < ind.getVariables().size(); i++) {
			solucionNueva.add(String.valueOf(ind.getVariables().get(i)));
		}
		solucionesExistentes.add(solucionNueva);

		List<String[]> lista = new ArrayList<>();

		for (int i = 0; i < solucionesExistentes.size(); i++) {
			String[] filaI = new String[solucionesExistentes.get(i).size()];
			for (int j = 0; j < solucionesExistentes.get(i).size(); j++) {
				filaI[j] = solucionesExistentes.get(i).get(j);
			}
			lista.add(filaI);
		}
		try (CSVWriter writer = new CSVWriter(
				new FileWriter(Constantes.rutaFicherosProyectos + "//" + nombre + "//"
						+ Constantes.nombreProblemaRRPS_PAT + Constantes.extensionFichero),
				',', CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END)) {
			writer.writeAll(lista);

		}
		return String.valueOf(solucionesExistentes.size() - 1);
	}

	public static void crearFicheroPoblacionSolucionITemp(String nombre, String id, Poblacion poblacion)
			throws IOException {
		List<String[]> lista = new ArrayList<>();

		for (int i = 0; i < poblacion.getPoblacion().size(); i++) {
			String[] filaI = new String[poblacion.getPoblacion().get(i).getVariables().size()];
			for (int j = 0; j < poblacion.getPoblacion().get(i).getVariables().size(); j++) {
				filaI[j] = String.valueOf(poblacion.getPoblacion().get(i).getVariables().get(j));
			}
			lista.add(filaI);
		}

		try (CSVWriter writer = new CSVWriter(
				new FileWriter(Constantes.rutaFicherosProyectos + "//" + nombre + "//" + Constantes.nombreDirectorioTemp
						+ "//" + id + "//" + Constantes.nombreFicheroPoblacionTemp + Constantes.extensionFichero),
				',', CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END)) {
			writer.writeAll(lista);

		}
	}

	public static void crearFicheroPbestsSolucionITemp(String nombre, String id, Poblacion poblacion)
			throws IOException {
		List<String[]> lista = new ArrayList<>();

		for (int i = 0; i < poblacion.getPoblacion().size(); i++) {
			String[] filaI = new String[poblacion.getPoblacion().get(i).getVariables().size()];
			for (int j = 0; j < poblacion.getPoblacion().get(i).getVariables().size(); j++) {
				filaI[j] = String.valueOf(poblacion.getPoblacion().get(i).getVariables().get(j));
			}
			lista.add(filaI);
		}

		try (CSVWriter writer = new CSVWriter(
				new FileWriter(Constantes.rutaFicherosProyectos + "//" + nombre + "//" + Constantes.nombreDirectorioTemp
						+ "//" + id + "//" + Constantes.nombreFicheroPbestsTemp + Constantes.extensionFichero),
				',', CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END)) {
			writer.writeAll(lista);

		}
	}

	public static void crearFicheroV0SolucionITemp(String nombre, String id, List<List<Double>> v0) throws IOException {
		List<String[]> lista = new ArrayList<>();

		for (int i = 0; i < v0.size(); i++) {
			String[] filaI = new String[v0.get(i).size()];
			for (int j = 0; j < v0.get(i).size(); j++) {
				filaI[j] = String.valueOf(v0.get(i).get(j));
			}
			lista.add(filaI);
		}

		try (CSVWriter writer = new CSVWriter(
				new FileWriter(Constantes.rutaFicherosProyectos + "//" + nombre + "//" + Constantes.nombreDirectorioTemp
						+ "//" + id + "//" + Constantes.nombreFicherov0Temp + Constantes.extensionFichero),
				',', CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END)) {
			writer.writeAll(lista);

		}
	}
	
	public static void crearFicheroV0LSolucionITemp(String nombre, String id, List<List<Double>> v0L) throws IOException {
		List<String[]> lista = new ArrayList<>();

		for (int i = 0; i < v0L.size(); i++) {
			String[] filaI = new String[v0L.get(i).size()];
			for (int j = 0; j < v0L.get(i).size(); j++) {
				filaI[j] = String.valueOf(v0L.get(i).get(j));
			}
			lista.add(filaI);
		}

		try (CSVWriter writer = new CSVWriter(
				new FileWriter(Constantes.rutaFicherosProyectos + "//" + nombre + "//" + Constantes.nombreDirectorioTemp
						+ "//" + id + "//" + Constantes.nombreFicherov0LTemp + Constantes.extensionFichero),
				',', CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END)) {
			writer.writeAll(lista);

		}
	}

	public static void crearFicheroV1SolucionITemp(String nombre, String id, List<List<Double>> v1) throws IOException {
		List<String[]> lista = new ArrayList<>();

		for (int i = 0; i < v1.size(); i++) {
			String[] filaI = new String[v1.get(i).size()];
			for (int j = 0; j < v1.get(i).size(); j++) {
				filaI[j] = String.valueOf(v1.get(i).get(j));
			}
			lista.add(filaI);
		}

		try (CSVWriter writer = new CSVWriter(
				new FileWriter(Constantes.rutaFicherosProyectos + "//" + nombre + "//" + Constantes.nombreDirectorioTemp
						+ "//" + id + "//" + Constantes.nombreFicherov1Temp + Constantes.extensionFichero),
				',', CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END)) {
			writer.writeAll(lista);

		}
	}
	
	public static void crearFicheroV1LSolucionITemp(String nombre, String id, List<List<Double>> v1L) throws IOException {
		List<String[]> lista = new ArrayList<>();

		for (int i = 0; i < v1L.size(); i++) {
			String[] filaI = new String[v1L.get(i).size()];
			for (int j = 0; j < v1L.get(i).size(); j++) {
				filaI[j] = String.valueOf(v1L.get(i).get(j));
			}
			lista.add(filaI);
		}

		try (CSVWriter writer = new CSVWriter(
				new FileWriter(Constantes.rutaFicherosProyectos + "//" + nombre + "//" + Constantes.nombreDirectorioTemp
						+ "//" + id + "//" + Constantes.nombreFicherov1LTemp + Constantes.extensionFichero),
				',', CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END)) {
			writer.writeAll(lista);

		}
	}

	public static void crearFicheroParamsTemp(String nombre, String id, BPSOParams params) throws IOException {
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
		if (params.getCondicionParada().getMethod().equals(Constantes.nombreCPGenerica)) {
			paramI[0] = Constantes.nombreParamCPNumIter;
			paramI[1] = String.valueOf(params.getMax_Num_Iteraciones());
			lista.add(paramI);

			paramI = new String[2];
			paramI[0] = Constantes.nombreParamCPIterActualTemp;
			paramI[1] = String.valueOf(params.getIteracionActual());
			lista.add(paramI);
		} else if (params.getCondicionParada().getMethod().equals(Constantes.nombreCPMaxDistQuick)) {
			paramI[0] = Constantes.nombreParamCPM;
			paramI[1] = String.valueOf(params.getCondicionParada().getM());
			lista.add(paramI);
			paramI = new String[2];
			paramI[0] = Constantes.nombreParamCPP;
			paramI[1] = String.valueOf(params.getCondicionParada().getP());
			lista.add(paramI);
		}

		try (CSVWriter writer = new CSVWriter(
				new FileWriter(Constantes.rutaFicherosProyectos + "//" + nombre + "//" + Constantes.nombreDirectorioTemp
						+ "//" + id + "//" + Constantes.nombreFicheroParamsTemp + Constantes.extensionFichero),
				',', CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END)) {
			writer.writeAll(lista);
		}
	}

	public static String borrarSolucionCSVproblemaRRPS_PAT(String nombre, int id) throws IOException, CsvException {

		File file = new File(Constantes.rutaFicherosProyectos + "//" + nombre + "//" + Constantes.nombreProblemaRRPS_PAT
				+ Constantes.extensionFichero);
		List<List<String>> solucionesExistentes;
		if (file.exists()) {
			solucionesExistentes = leerCSVnombre(Constantes.nombreProblemaRRPS_PAT, nombre);
		} else {
			solucionesExistentes = new ArrayList<>();
		}

		solucionesExistentes.remove(id);

		List<String[]> lista = new ArrayList<>();

		for (int i = 0; i < solucionesExistentes.size(); i++) {
			String[] filaI = new String[solucionesExistentes.get(i).size()];
			for (int j = 0; j < solucionesExistentes.get(i).size(); j++) {
				filaI[j] = solucionesExistentes.get(i).get(j);
			}
			lista.add(filaI);
		}
		try (CSVWriter writer = new CSVWriter(
				new FileWriter(Constantes.rutaFicherosProyectos + "//" + nombre + "//"
						+ Constantes.nombreProblemaRRPS_PAT + Constantes.extensionFichero),
				',', CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END)) {
			writer.writeAll(lista);

		}
		return String.valueOf(solucionesExistentes.size() - 1);
	}

	public static boolean comprobarListasIguales(List<String> a, List<String> b) {
		boolean iguales = true;
		if (a.size() != b.size()) {
			return false;
		} else {
			int pos = 0;
			while (pos < a.size() && iguales) {
				if (!a.get(pos).equals(b.get(pos))) {
					iguales = false;
				}
				pos++;
			}
		}
		return iguales;
	}

	public static void crearCSVParams(BPSOParams params, String nombre) throws IOException {
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
		if (params.getCondicionParada().getMethod().equals(Constantes.nombreCPGenerica)) {
			paramI[0] = Constantes.nombreParamCPNumIter;
			paramI[1] = String.valueOf(params.getMax_Num_Iteraciones());
			lista.add(paramI);
		} else if (params.getCondicionParada().getMethod().equals(Constantes.nombreCPMaxDistQuick)) {
			paramI[0] = Constantes.nombreParamCPM;
			paramI[1] = String.valueOf(params.getCondicionParada().getM());
			lista.add(paramI);
			paramI = new String[2];
			paramI[0] = Constantes.nombreParamCPP;
			paramI[1] = String.valueOf(params.getCondicionParada().getP());
			lista.add(paramI);
		}

		try (CSVWriter writer = new CSVWriter(
				new FileWriter(Constantes.rutaFicherosProyectos + "//" + nombre + "//"
						+ Constantes.nombreFicheroParametros + Constantes.extensionFichero),
				',', CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END)) {
			writer.writeAll(lista);
		}

	}

	public static BPSOParamsSalida leerCSVParamsSalida(String nombre) throws IOException, CsvException {
		List<List<String>> fichero = new ArrayList<>();
		int numFilas;
		try (CSVReader reader = new CSVReader(new FileReader(Constantes.rutaFicherosProyectos + "//" + nombre + "//"
				+ Constantes.nombreFicheroParametros + Constantes.extensionFichero))) {
			List<String[]> r = reader.readAll();
			numFilas = r.size();
			for (int fila = 0; fila < numFilas; fila++) {
				List<String> filaI = new ArrayList<>();
				for (int columna = 0; columna < r.get(fila).length; columna++) {

					filaI.add(r.get(fila)[columna]);
				}
				fichero.add(filaI);
			}
		}
		BPSOParamsSalida params = new BPSOParamsSalida(Integer.valueOf(fichero.get(0).get(1)),
				Double.valueOf(fichero.get(1).get(1)), Double.valueOf(fichero.get(2).get(1)),
				Double.valueOf(fichero.get(3).get(1)));
		return params;

	}

	public static BPSOParams leerCSVParams(String nombre) throws IOException, CsvException {
		List<List<String>> fichero = new ArrayList<>();
		int numFilas;
		try (CSVReader reader = new CSVReader(new FileReader(Constantes.rutaFicherosProyectos + "//" + nombre + "//"
				+ Constantes.nombreFicheroParametros + Constantes.extensionFichero))) {
			List<String[]> r = reader.readAll();
			numFilas = r.size();
			for (int fila = 0; fila < numFilas; fila++) {
				List<String> filaI = new ArrayList<>();
				for (int columna = 0; columna < r.get(fila).length; columna++) {

					filaI.add(r.get(fila)[columna]);
				}
				fichero.add(filaI);
			}
		}
		BPSOParams params = new BPSOParams(Integer.valueOf(fichero.get(0).get(1)),
				Double.valueOf(fichero.get(1).get(1)), Double.valueOf(fichero.get(2).get(1)),
				Double.valueOf(fichero.get(3).get(1)), Integer.valueOf(fichero.get(4).get(1)), 0.0, 0.0,
				Constantes.nombreCPGenerica, Constantes.nombreIWDyanamicDecreasing);
		return params;

	}

	public static BPSOParams leerCSVParamsTemp(String nombre, int id) throws IOException, CsvException {
		List<List<String>> fichero = new ArrayList<>();
		int numFilas;
		try (CSVReader reader = new CSVReader(new FileReader(Constantes.rutaFicherosProyectos + "//" + nombre + "//"
				+ Constantes.nombreDirectorioTemp + "//" + String.valueOf(id) + "//"
				+ Constantes.nombreFicheroParamsTemp + Constantes.extensionFichero))) {
			List<String[]> r = reader.readAll();
			numFilas = r.size();
			for (int fila = 0; fila < numFilas; fila++) {
				List<String> filaI = new ArrayList<>();
				for (int columna = 0; columna < r.get(fila).length; columna++) {

					filaI.add(r.get(fila)[columna]);
				}
				fichero.add(filaI);
			}
		}
		BPSOParams params = new BPSOParams(Integer.valueOf(fichero.get(0).get(1)),
				Double.valueOf(fichero.get(1).get(1)), Double.valueOf(fichero.get(2).get(1)),
				Double.valueOf(fichero.get(3).get(1)), Integer.valueOf(fichero.get(4).get(1)), 0.0, 0.0,
				Constantes.nombreCPGenerica, Constantes.nombreIWDyanamicDecreasing);

		params.getCondicionParada().setNumIteracionesActual(Integer.valueOf(fichero.get(5).get(1)));
		return params;

	}

	public static Poblacion leerCSVPoblacionTemp(Problema problema, String nombre, int id, Poblacion poblacion)
			throws FileNotFoundException, IOException, CsvException {

		int numFilas;
		try (CSVReader reader = new CSVReader(new FileReader(Constantes.rutaFicherosProyectos + "//" + nombre + "//"
				+ Constantes.nombreDirectorioTemp + "//" + String.valueOf(id) + "//"
				+ Constantes.nombreFicheroPoblacionTemp + Constantes.extensionFichero))) {
			List<String[]> r = reader.readAll();
			numFilas = r.size();
			for (int fila = 0; fila < numFilas; fila++) {
				List<Double> filaI = new ArrayList<>();

				for (int columna = 0; columna < r.get(fila).length; columna++) {

					filaI.add(Double.valueOf(r.get(fila)[columna]));
				}

				poblacion.setIIndividuo(fila, filaI);
			}
		}
		return poblacion;
	}

	public static Poblacion leerCSVPbestsTemp(Problema problema, String nombre, int id, Poblacion pbests)
			throws FileNotFoundException, IOException, CsvException {

		int numFilas;
		try (CSVReader reader = new CSVReader(new FileReader(Constantes.rutaFicherosProyectos + "//" + nombre + "//"
				+ Constantes.nombreDirectorioTemp + "//" + String.valueOf(id) + "//"
				+ Constantes.nombreFicheroPbestsTemp + Constantes.extensionFichero))) {
			List<String[]> r = reader.readAll();
			numFilas = r.size();
			for (int fila = 0; fila < numFilas; fila++) {
				List<Double> filaI = new ArrayList<>();

				for (int columna = 0; columna < r.get(fila).length; columna++) {

					filaI.add(Double.valueOf(r.get(fila)[columna]));
				}

				pbests.setIIndividuo(fila, filaI);
			}
		}
		return pbests;
	}

	public static void leerCSVV0Temp(String nombre, int id, List<List<Double>> v0)
			throws FileNotFoundException, IOException, CsvException {

		int numFilas;
		try (CSVReader reader = new CSVReader(new FileReader(
				Constantes.rutaFicherosProyectos + "//" + nombre + "//" + Constantes.nombreDirectorioTemp + "//"
						+ String.valueOf(id) + "//" + Constantes.nombreFicherov0Temp + Constantes.extensionFichero))) {
			List<String[]> r = reader.readAll();
			numFilas = r.size();
			for (int fila = 0; fila < numFilas; fila++) {
				List<Double> filaI = new ArrayList<>();

				for (int columna = 0; columna < r.get(fila).length; columna++) {

					filaI.add(Double.valueOf(r.get(fila)[columna]));
				}

				v0.add(filaI);
			}
		}
	}
	
	public static void leerCSVV0LTemp(String nombre, int id, List<List<Double>> v0L)
			throws FileNotFoundException, IOException, CsvException {

		int numFilas;
		try (CSVReader reader = new CSVReader(new FileReader(
				Constantes.rutaFicherosProyectos + "//" + nombre + "//" + Constantes.nombreDirectorioTemp + "//"
						+ String.valueOf(id) + "//" + Constantes.nombreFicherov0LTemp + Constantes.extensionFichero))) {
			List<String[]> r = reader.readAll();
			numFilas = r.size();
			for (int fila = 0; fila < numFilas; fila++) {
				List<Double> filaI = new ArrayList<>();

				for (int columna = 0; columna < r.get(fila).length; columna++) {

					filaI.add(Double.valueOf(r.get(fila)[columna]));
				}

				v0L.add(filaI);
			}
		}
	}

	public static void leerCSVV1Temp(String nombre, int id, List<List<Double>> v1)
			throws FileNotFoundException, IOException, CsvException {

		int numFilas;
		try (CSVReader reader = new CSVReader(new FileReader(
				Constantes.rutaFicherosProyectos + "//" + nombre + "//" + Constantes.nombreDirectorioTemp + "//"
						+ String.valueOf(id) + "//" + Constantes.nombreFicherov1Temp + Constantes.extensionFichero))) {
			List<String[]> r = reader.readAll();
			numFilas = r.size();
			for (int fila = 0; fila < numFilas; fila++) {
				List<Double> filaI = new ArrayList<>();

				for (int columna = 0; columna < r.get(fila).length; columna++) {

					filaI.add(Double.valueOf(r.get(fila)[columna]));
				}

				v1.add(filaI);
			}
		}
	}
	
	public static void leerCSVV1LTemp(String nombre, int id, List<List<Double>> v1L)
			throws FileNotFoundException, IOException, CsvException {

		int numFilas;
		try (CSVReader reader = new CSVReader(new FileReader(
				Constantes.rutaFicherosProyectos + "//" + nombre + "//" + Constantes.nombreDirectorioTemp + "//"
						+ String.valueOf(id) + "//" + Constantes.nombreFicherov1LTemp + Constantes.extensionFichero))) {
			List<String[]> r = reader.readAll();
			numFilas = r.size();
			for (int fila = 0; fila < numFilas; fila++) {
				List<Double> filaI = new ArrayList<>();

				for (int columna = 0; columna < r.get(fila).length; columna++) {

					filaI.add(Double.valueOf(r.get(fila)[columna]));
				}

				v1L.add(filaI);
			}
		}
	}

	public static void crearCSVPref(DMPreferences preferencias, String nombre) throws IOException {
		List<String[]> lista = new ArrayList<>();

		for (int i = 0; i < preferencias.getOrder().getOrder().size(); i++) {
			String[] objI = new String[1];
			objI[0] = String.valueOf(preferencias.getOrder().getOrder().get(i));
			lista.add(objI);
		}
		try (CSVWriter writer = new CSVWriter(
				new FileWriter(Constantes.rutaFicherosProyectos + "//" + nombre + "//"
						+ Constantes.nombreFicheroPreferencias + Constantes.extensionFichero),
				',', CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END)) {
			writer.writeAll(lista);
		}

	}

	public static OrdenObjSalida leerCSVPrefSalida(String nombre) throws IOException, CsvException {
		List<List<String>> fichero = new ArrayList<>();
		int numFilas;
		try (CSVReader reader = new CSVReader(new FileReader(Constantes.rutaFicherosProyectos + "//" + nombre + "//"
				+ Constantes.nombreFicheroPreferencias + Constantes.extensionFichero))) {
			List<String[]> r = reader.readAll();
			numFilas = r.size();
			for (int fila = 0; fila < numFilas; fila++) {
				List<String> filaI = new ArrayList<>();
				for (int columna = 0; columna < r.get(fila).length; columna++) {

					filaI.add(r.get(fila)[columna]);
				}
				fichero.add(filaI);
			}
		}
		List<Integer> listaOrden = new ArrayList<>();
		for (List<String> lista : fichero) {
			listaOrden.add(Integer.valueOf(lista.get(0)));
		}
		OrdenObjSalida orden = new OrdenObjSalida(listaOrden);
		return orden;
	}

	public static ObjectivesOrder leerCSVPref(String nombre) throws IOException, CsvException {
		List<List<String>> fichero = new ArrayList<>();
		int numFilas;
		try (CSVReader reader = new CSVReader(new FileReader(Constantes.rutaFicherosProyectos + "//" + nombre + "//"
				+ Constantes.nombreFicheroPreferencias + Constantes.extensionFichero))) {
			List<String[]> r = reader.readAll();
			numFilas = r.size();
			for (int fila = 0; fila < numFilas; fila++) {
				List<String> filaI = new ArrayList<>();
				for (int columna = 0; columna < r.get(fila).length; columna++) {

					filaI.add(r.get(fila)[columna]);
				}
				fichero.add(filaI);
			}
		}
		List<Integer> listaOrden = new ArrayList<>();
		for (List<String> lista : fichero) {
			listaOrden.add(Integer.valueOf(lista.get(0)));
		}
		ObjectivesOrder orden = new ObjectivesOrder();
		orden.setOrder(listaOrden);
		return orden;
	}

	public static void crearCSVFechas(String fecha_I, String fecha_F, String nombre) throws IOException {
		List<String[]> lista = new ArrayList<>();
		String[] paramI = new String[2];
		paramI[0] = Constantes.nombreFechaInicial;
		paramI[1] = String.valueOf(fecha_I);
		lista.add(paramI);

		paramI = new String[2];
		paramI[0] = Constantes.nombreFechaFinal;
		paramI[1] = String.valueOf(fecha_F);
		lista.add(paramI);

		try (CSVWriter writer = new CSVWriter(
				new FileWriter(Constantes.rutaFicherosProyectos + "//" + nombre + "//"
						+ Constantes.nombreFicheroFechasSoluciones + Constantes.extensionFichero),
				',', CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END)) {
			writer.writeAll(lista);
		}

	}

	public static FechasProyecto leerCSVFechasSalida(String nombre) throws IOException, CsvException {
		List<List<String>> fichero = new ArrayList<>();
		int numFilas;
		try (CSVReader reader = new CSVReader(new FileReader(Constantes.rutaFicherosProyectos + "//" + nombre + "//"
				+ Constantes.nombreFicheroFechasSoluciones + Constantes.extensionFichero))) {
			List<String[]> r = reader.readAll();
			numFilas = r.size();
			for (int fila = 0; fila < numFilas; fila++) {
				List<String> filaI = new ArrayList<>();
				for (int columna = 0; columna < r.get(fila).length; columna++) {

					filaI.add(r.get(fila)[columna]);
				}
				fichero.add(filaI);
			}
		}
		FechasProyecto fechas = new FechasProyecto(fichero.get(0).get(1), fichero.get(1).get(1));
		return fechas;

	}

	public static Map<String, String> leerCSVFechas(String nombre) throws IOException, CsvException {
		List<List<String>> fichero = new ArrayList<>();
		int numFilas;
		try (CSVReader reader = new CSVReader(new FileReader(Constantes.rutaFicherosProyectos + "//" + nombre + "//"
				+ Constantes.nombreFicheroFechasSoluciones + Constantes.extensionFichero))) {
			List<String[]> r = reader.readAll();
			numFilas = r.size();
			for (int fila = 0; fila < numFilas; fila++) {
				List<String> filaI = new ArrayList<>();
				for (int columna = 0; columna < r.get(fila).length; columna++) {

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

	public static void crearCSVRestricciones(double epi, List<String> pol, String nombre) throws IOException {
		List<String[]> lista = new ArrayList<>();
		String[] paramI = new String[2];
		paramI[0] = Constantes.nombreRestriccionEpidemiologica;
		paramI[1] = String.valueOf(epi);
		lista.add(paramI);

		paramI = new String[1 + pol.size()];
		paramI[0] = Constantes.nombreRestriccionPolitica;
		for (int i = 0; i < pol.size(); i++) {
			paramI[i + 1] = pol.get(i);
		}
		lista.add(paramI);

		try (CSVWriter writer = new CSVWriter(
				new FileWriter(Constantes.rutaFicherosProyectos + "//" + nombre + "//"
						+ Constantes.nombreFicheroRestricciones + Constantes.extensionFichero),
				',', CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END)) {
			writer.writeAll(lista);
		}

	}

	public static Restricciones leerCSVRestriccionesSalida(String nombre) throws IOException, CsvException {
		List<List<String>> fichero = new ArrayList<>();
		int numFilas;
		try (CSVReader reader = new CSVReader(new FileReader(Constantes.rutaFicherosProyectos + "//" + nombre + "//"
				+ Constantes.nombreFicheroRestricciones + Constantes.extensionFichero))) {
			List<String[]> r = reader.readAll();
			numFilas = r.size();
			for (int fila = 0; fila < numFilas; fila++) {
				List<String> filaI = new ArrayList<>();
				for (int columna = 0; columna < r.get(fila).length; columna++) {

					filaI.add(r.get(fila)[columna]);
				}
				fichero.add(filaI);
			}
		}
		Restricciones res;
		Double epi = Double.valueOf(fichero.get(0).get(1));
		if (fichero.get(1).size() > 1) {

			res = new Restricciones(fichero.get(1).subList(1, fichero.get(1).size()), epi);
		} else {
			res = new Restricciones(new ArrayList<>(), epi);
		}

		return res;
	}

	public static Map<String, List<String>> leerCSVRestricciones(String nombre) throws IOException, CsvException {
		List<List<String>> fichero = new ArrayList<>();
		int numFilas;
		try (CSVReader reader = new CSVReader(new FileReader(Constantes.rutaFicherosProyectos + "//" + nombre + "//"
				+ Constantes.nombreFicheroRestricciones + Constantes.extensionFichero))) {
			List<String[]> r = reader.readAll();
			numFilas = r.size();
			for (int fila = 0; fila < numFilas; fila++) {
				List<String> filaI = new ArrayList<>();
				for (int columna = 0; columna < r.get(fila).length; columna++) {

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

	public static String crearCSVObjetivos(List<Double> obj, List<Double> res, String nombreFichero,
			String nombreProyecto) throws IOException {
		String fileName = nombreFichero + Constantes.extensionFichero;
		if (obj.size() == 0) {
			return fileName;
		} else {
			List<String[]> lista = new ArrayList<>();
			String[] iter_Fit;

			for (int i = 0; i < res.size(); i++) {
				iter_Fit = new String[2];

				iter_Fit[0] = Constantes.nombresRestricciones.get(i);
				iter_Fit[1] = String.valueOf(res.get(i));

				lista.add(iter_Fit);
			}

			for (int i = 0; i < obj.size(); i++) {
				iter_Fit = new String[2];

				iter_Fit[0] = Constantes.nombreObjetivos.get(i);
				iter_Fit[1] = String.valueOf(obj.get(i));

				lista.add(iter_Fit);
			}

			try (CSVWriter writer = new CSVWriter(
					new FileWriter(Constantes.rutaFicherosProyectos + "//" + nombreProyecto + "//"
							+ Constantes.nombreDirectorioFicherosObjetivos + "//" + fileName),
					',', CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.DEFAULT_ESCAPE_CHARACTER,
					CSVWriter.DEFAULT_LINE_END)) {
				writer.writeAll(lista);
			}
			return fileName;
		}

	}

	public static List<Double> leerCSVObjetivosSalida(String id, String nombreProyecto)
			throws IOException, CsvException {
		List<List<String>> fichero = new ArrayList<>();
		DecimalFormat df = new DecimalFormat("0.00");
		int numFilas;
		try (CSVReader reader = new CSVReader(new FileReader(Constantes.rutaFicherosProyectos + "//" + nombreProyecto
				+ "//" + Constantes.nombreDirectorioFicherosObjetivos + "//" + id + Constantes.extensionFichero))) {
			List<String[]> r = reader.readAll();
			numFilas = r.size();
			for (int fila = 0; fila < numFilas; fila++) {
				List<String> filaI = new ArrayList<>();
				for (int columna = 0; columna < r.get(fila).length; columna++) {

					filaI.add(r.get(fila)[columna]);
				}
				fichero.add(filaI);
			}
		}

		List<Double> obj = new ArrayList<>();

		for (int i = 0; i < fichero.size(); i++) {
			String format = df.format(Double.valueOf(fichero.get(i).get(1)));
			format = format.replace(",", ".");
			obj.add(Double.valueOf(format));
		}

		return obj;

	}

	public static String crearCSVConFitnessPorIteracion(List<Double> listaF, String nombreFichero,
			String nombreProyecto) throws IOException {
		String fileName = nombreFichero + Constantes.extensionFichero;
		if (listaF.size() == 0) {
			return fileName;
		} else {
			List<String[]> lista = new ArrayList<>();

			for (int i = 0; i < listaF.size(); i++) {
				String[] iter_Fit = new String[2];

				iter_Fit[0] = String.valueOf(i * 1.0);
				iter_Fit[1] = String.valueOf(listaF.get(i));

				lista.add(iter_Fit);
			}

			try (CSVWriter writer = new CSVWriter(
					new FileWriter(Constantes.rutaFicherosProyectos + "//" + nombreProyecto + "//"
							+ Constantes.nombreDirectorioFicherosFitness + "//" + fileName),
					',', CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.DEFAULT_ESCAPE_CHARACTER,
					CSVWriter.DEFAULT_LINE_END)) {
				writer.writeAll(lista);
			}
			return fileName;
		}

	}

	public static List<Double> leerCSVConFitnessPorIteracionSalida(String id, String nombreProyecto)
			throws IOException, CsvException {
		List<List<String>> fichero = new ArrayList<>();
		int numFilas;
		try (CSVReader reader = new CSVReader(new FileReader(Constantes.rutaFicherosProyectos + "//" + nombreProyecto
				+ "//" + Constantes.nombreDirectorioFicherosFitness + "//" + id + Constantes.extensionFichero))) {
			List<String[]> r = reader.readAll();
			numFilas = r.size();
			for (int fila = 0; fila < numFilas; fila++) {
				List<String> filaI = new ArrayList<>();
				for (int columna = 0; columna < r.get(fila).length; columna++) {

					filaI.add(r.get(fila)[columna]);
				}
				fichero.add(filaI);
			}
		}

		List<Double> fit = new ArrayList<>();

		fit.add(Double.valueOf(fichero.get(numFilas - 1).get(0)));
		fit.add(Double.valueOf(fichero.get(numFilas - 1).get(1)));

		return fit;

	}

	public static String crearCSVHistogramas(List<Double> listaPasajerosPerdidosPorCompanyia,
			List<Double> listaIngresoPerdidoPorAreaInf, List<Double> listaIngresoPerdidoPorAerDest,
			String nombreFichero, String nombreProyecto) throws IOException {
		String fileName = nombreFichero + Constantes.extensionFichero;
		if (listaPasajerosPerdidosPorCompanyia.size() == 0 || listaIngresoPerdidoPorAreaInf.size() == 0
				|| listaIngresoPerdidoPorAerDest.size() == 0) {
			return fileName;
		} else {
			List<String[]> lista = new ArrayList<>();

			String[] valores = new String[listaPasajerosPerdidosPorCompanyia.size() + 1];
			valores[0] = Constantes.nombreCampoPasajerosPerdidosPorCompañía;
			for (int i = 0; i < listaPasajerosPerdidosPorCompanyia.size(); i++) {

				valores[i + 1] = String.valueOf(listaPasajerosPerdidosPorCompanyia.get(i));

			}
			lista.add(valores);

			valores = new String[listaIngresoPerdidoPorAreaInf.size() + 1];
			valores[0] = Constantes.nombreCampoIngresoPerdidoPorAreaInf;
			for (int i = 0; i < listaIngresoPerdidoPorAreaInf.size(); i++) {

				valores[i + 1] = String.valueOf(listaIngresoPerdidoPorAreaInf.get(i));

			}
			lista.add(valores);

			valores = new String[listaIngresoPerdidoPorAerDest.size() + 1];
			valores[0] = Constantes.nombreCampoIngresoPerdidoPorAerDest;
			for (int i = 0; i < listaIngresoPerdidoPorAerDest.size(); i++) {

				valores[i + 1] = String.valueOf(listaIngresoPerdidoPorAerDest.get(i));

			}
			lista.add(valores);

			try (CSVWriter writer = new CSVWriter(
					new FileWriter(Constantes.rutaFicherosProyectos + "//" + nombreProyecto + "//"
							+ Constantes.nombreDirectorioFicherosRangos + "//" + fileName),
					',', CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.DEFAULT_ESCAPE_CHARACTER,
					CSVWriter.DEFAULT_LINE_END)) {
				writer.writeAll(lista);
			}
			return fileName;
		}
	}

	public static Map<String, List<Double>> leerCSVHistogramas(String nombreProyecto, String id)
			throws FileNotFoundException, IOException, CsvException {
		Map<String, List<Double>> valores = new HashMap<>();

		try (CSVReader reader = new CSVReader(new FileReader(Constantes.rutaFicherosProyectos + "//" + nombreProyecto
				+ "//" + Constantes.nombreDirectorioFicherosRangos + "//" + id + Constantes.extensionFichero))) {
			List<String[]> r = reader.readAll();

			List<Double> filaI = new ArrayList<>();
			for (int columna = 1; columna < r.get(0).length; columna++) {

				filaI.add(Double.valueOf(r.get(0)[columna]));
			}
			valores.put(r.get(0)[0], filaI);

			filaI = new ArrayList<>();
			for (int columna = 1; columna < r.get(1).length; columna++) {

				filaI.add(Double.valueOf(r.get(1)[columna]));
			}
			valores.put(r.get(1)[0], filaI);

			filaI = new ArrayList<>();
			for (int columna = 1; columna < r.get(2).length; columna++) {

				filaI.add(Double.valueOf(r.get(2)[columna]));
			}
			valores.put(r.get(2)[0], filaI);

		}

		return valores;
	}

	public static void borrarCSVObjetivoI(String proyecto, int id) {
		// Borrar el fichero indicado
		File file = new File(
				Constantes.rutaFicherosProyectos + "//" + proyecto + "//" + Constantes.nombreDirectorioFicherosObjetivos
						+ "//" + String.valueOf(id) + Constantes.extensionFichero);
		file.delete();

		File directoryPath = new File(Constantes.rutaFicherosProyectos + "//" + proyecto + "//"
				+ Constantes.nombreDirectorioFicherosObjetivos + "//");

		String contents[] = directoryPath.list();

		// Renombrar los ficheros con id superior al indicado

		for (int i = 0; i < contents.length; i++) {
			if (Integer.valueOf(contents[i].replace(Constantes.extensionFichero, "")) > id) {
				int newId = Integer.valueOf(contents[i].replace(Constantes.extensionFichero, "")) - 1;
				File newFile = new File(Constantes.rutaFicherosProyectos + "//" + proyecto + "//"
						+ Constantes.nombreDirectorioFicherosObjetivos + "//" + String.valueOf(newId)
						+ Constantes.extensionFichero);
				File fileI = new File(Constantes.rutaFicherosProyectos + "//" + proyecto + "//"
						+ Constantes.nombreDirectorioFicherosObjetivos + "//" + contents[i]);

				fileI.renameTo(newFile);
			}
		}

	}

	public static void borrarCSVFitnessI(String proyecto, int id) {
		// Borrar el fichero indicado
		File file = new File(Constantes.rutaFicherosProyectos + "//" + proyecto + "//"
				+ Constantes.nombreDirectorioFicherosFitness + "//" + String.valueOf(id) + Constantes.extensionFichero);
		file.delete();

		File directoryPath = new File(Constantes.rutaFicherosProyectos + "//" + proyecto + "//"
				+ Constantes.nombreDirectorioFicherosFitness + "//");

		String contents[] = directoryPath.list();

		// Renombrar los ficheros con id superior al indicado

		for (int i = 0; i < contents.length; i++) {
			if (Integer.valueOf(contents[i].replace(Constantes.extensionFichero, "")) > id) {
				int newId = Integer.valueOf(contents[i].replace(Constantes.extensionFichero, "")) - 1;
				File newFile = new File(Constantes.rutaFicherosProyectos + "//" + proyecto + "//"
						+ Constantes.nombreDirectorioFicherosFitness + "//" + String.valueOf(newId)
						+ Constantes.extensionFichero);
				File fileI = new File(Constantes.rutaFicherosProyectos + "//" + proyecto + "//"
						+ Constantes.nombreDirectorioFicherosFitness + "//" + contents[i]);

				fileI.renameTo(newFile);
			}
		}
	}

	public static void borrarCSVRangosI(String proyecto, int id) {
		// Borrar el fichero indicado
		File file = new File(Constantes.rutaFicherosProyectos + "//" + proyecto + "//"
				+ Constantes.nombreDirectorioFicherosRangos + "//" + String.valueOf(id) + Constantes.extensionFichero);
		file.delete();

		File directoryPath = new File(Constantes.rutaFicherosProyectos + "//" + proyecto + "//"
				+ Constantes.nombreDirectorioFicherosRangos + "//");

		String contents[] = directoryPath.list();

		// Renombrar los ficheros con id superior al indicado

		for (int i = 0; i < contents.length; i++) {
			if (Integer.valueOf(contents[i].replace(Constantes.extensionFichero, "")) > id) {
				int newId = Integer.valueOf(contents[i].replace(Constantes.extensionFichero, "")) - 1;
				File newFile = new File(Constantes.rutaFicherosProyectos + "//" + proyecto + "//"
						+ Constantes.nombreDirectorioFicherosRangos + "//" + String.valueOf(newId)
						+ Constantes.extensionFichero);
				File fileI = new File(Constantes.rutaFicherosProyectos + "//" + proyecto + "//"
						+ Constantes.nombreDirectorioFicherosRangos + "//" + contents[i]);

				fileI.renameTo(newFile);
			}
		}
	}

	public static void borrarDirectorioTempSolucionI(File solucion, String proyecto, int id) {
		FileSystemUtils.deleteRecursively(solucion);

		File directoryPath = new File(
				Constantes.rutaFicherosProyectos + "//" + proyecto + "//" + Constantes.nombreDirectorioTemp + "//");

		String contents[] = directoryPath.list();

		// Renombrar los ficheros con id superior al indicado

		for (int i = 0; i < contents.length; i++) {
			if (Integer.valueOf(contents[i]) > id) {
				int newId = Integer.valueOf(contents[i]) - 1;
				File newFile = new File(Constantes.rutaFicherosProyectos + "//" + proyecto + "//"
						+ Constantes.nombreDirectorioTemp + "//" + String.valueOf(newId));

				File fileI = new File(Constantes.rutaFicherosProyectos + "//" + proyecto + "//"
						+ Constantes.nombreDirectorioTemp + "//" + contents[i]);

				fileI.renameTo(newFile);
			}
		}
	}

	public static Map<String, List<String>> obtenerRangos(String id, String nombreProyecto)
			throws FileNotFoundException, IOException, CsvException {
		Map<String, List<Double>> valores = leerCSVHistogramas(id, nombreProyecto);
		Map<String, List<String>> valoresSalida = new HashMap<>();
		DecimalFormat df = new DecimalFormat("0.00");

		int indMin = 0;
		int indMax = valores.get(Constantes.nombreCampoPasajerosPerdidosPorCompañía).size() - 1;

		double valorMin = valores.get(Constantes.nombreCampoPasajerosPerdidosPorCompañía).get(indMin);
		double valorMax = valores.get(Constantes.nombreCampoPasajerosPerdidosPorCompañía).get(indMax);

		while (indMin < indMax && (valorMin == 0.0 || valorMax == 1.0)) {
			if (valorMin == 0.0) {
				valorMin = valores.get(Constantes.nombreCampoPasajerosPerdidosPorCompañía).get(indMin);
				indMin++;
			}
			if (valorMax == 1.0) {
				valorMax = valores.get(Constantes.nombreCampoPasajerosPerdidosPorCompañía).get(indMax);
				indMax--;
			}
		}
		valoresSalida.put(Constantes.nombreCampoPasajerosPerdidosPorCompañía,
				Stream.of(df.format(valorMin), df.format(valorMax)).collect(Collectors.toList()));

		indMin = 0;
		indMax = valores.get(Constantes.nombreCampoIngresoPerdidoPorAreaInf).size() - 1;

		valorMin = valores.get(Constantes.nombreCampoIngresoPerdidoPorAreaInf).get(indMin);
		valorMax = valores.get(Constantes.nombreCampoIngresoPerdidoPorAreaInf).get(indMax);

		while (indMin < indMax && (valorMin == 0.0 || valorMax == 1.0)) {
			if (valorMin == 0.0) {
				valorMin = valores.get(Constantes.nombreCampoIngresoPerdidoPorAreaInf).get(indMin);
				indMin++;
			}
			if (valorMax == 1.0) {
				valorMax = valores.get(Constantes.nombreCampoIngresoPerdidoPorAreaInf).get(indMax);
				indMax--;
			}
		}
		valoresSalida.put(Constantes.nombreCampoIngresoPerdidoPorAreaInf,
				Stream.of(df.format(valorMin), df.format(valorMax)).collect(Collectors.toList()));

		indMin = 0;
		indMax = valores.get(Constantes.nombreCampoIngresoPerdidoPorAerDest).size() - 1;

		valorMin = valores.get(Constantes.nombreCampoIngresoPerdidoPorAerDest).get(indMin);
		valorMax = valores.get(Constantes.nombreCampoIngresoPerdidoPorAerDest).get(indMax);

		while (indMin < indMax && (valorMin == 0.0 || valorMax == 1.0)) {
			if (valorMin == 0.0) {
				valorMin = valores.get(Constantes.nombreCampoIngresoPerdidoPorAerDest).get(indMin);
				indMin++;
			}
			if (valorMax == 1.0) {
				valorMax = valores.get(Constantes.nombreCampoIngresoPerdidoPorAerDest).get(indMax);
				indMax--;
			}
		}
		valoresSalida.put(Constantes.nombreCampoIngresoPerdidoPorAerDest,
				Stream.of(df.format(valorMin), df.format(valorMax)).collect(Collectors.toList()));

		return valoresSalida;
	}

	public static Map<String, List<String>> obtenerRangosSnapshot(List<Double> pasajerosPerdidosPorCompanyia,
			List<Double> ingresoPerdidoAreasInf, List<Double> ingresoPerdidoAerDest)
			throws FileNotFoundException, IOException, CsvException {

		Map<String, List<String>> valoresSalida = new HashMap<>();
		DecimalFormat df = new DecimalFormat("0.00");

		int indMin = 0;
		int indMax = pasajerosPerdidosPorCompanyia.size() - 1;

		double valorMin = pasajerosPerdidosPorCompanyia.get(indMin);
		double valorMax = pasajerosPerdidosPorCompanyia.get(indMax);

		while (indMin < indMax && (valorMin == 0.0 || valorMax == 1.0)) {
			if (valorMin == 0.0) {
				valorMin = pasajerosPerdidosPorCompanyia.get(indMin);
				indMin++;
			}
			if (valorMax == 1.0) {
				valorMax = pasajerosPerdidosPorCompanyia.get(indMax);
				indMax--;
			}
		}
		valoresSalida.put(Constantes.nombreCampoPasajerosPerdidosPorCompañía,
				Stream.of(df.format(valorMin), df.format(valorMax)).collect(Collectors.toList()));

		indMin = 0;
		indMax = ingresoPerdidoAreasInf.size() - 1;

		valorMin = ingresoPerdidoAreasInf.get(indMin);
		valorMax = ingresoPerdidoAreasInf.get(indMax);

		while (indMin < indMax && (valorMin == 0.0 || valorMax == 1.0)) {
			if (valorMin == 0.0) {
				valorMin = ingresoPerdidoAreasInf.get(indMin);
				indMin++;
			}
			if (valorMax == 1.0) {
				valorMax = ingresoPerdidoAreasInf.get(indMax);
				indMax--;
			}
		}
		valoresSalida.put(Constantes.nombreCampoIngresoPerdidoPorAreaInf,
				Stream.of(df.format(valorMin), df.format(valorMax)).collect(Collectors.toList()));

		indMin = 0;
		indMax = ingresoPerdidoAerDest.size() - 1;

		valorMin = ingresoPerdidoAerDest.get(indMin);
		valorMax = ingresoPerdidoAerDest.get(indMax);

		while (indMin < indMax && (valorMin == 0.0 || valorMax == 1.0)) {
			if (valorMin == 0.0) {
				valorMin = ingresoPerdidoAerDest.get(indMin);
				indMin++;
			}
			if (valorMax == 1.0) {
				valorMax = ingresoPerdidoAerDest.get(indMax);
				indMax--;
			}
		}
		valoresSalida.put(Constantes.nombreCampoIngresoPerdidoPorAerDest,
				Stream.of(df.format(valorMin), df.format(valorMax)).collect(Collectors.toList()));

		return valoresSalida;
	}

	public static Map<Integer, String> obtenerTooltips(DatosRRPS_PAT datos) {
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

				if (!areasInf.contains(datos.getAresInfTotales().get(pos))) {
					areasInf.add(datos.getAresInfTotales().get(pos));
				}
				if (!companyias.contains(datos.getCompanyiasTotales().get(pos))) {
					companyias.add(datos.getCompanyiasTotales().get(pos));
				}

				/*
				 * if (pasajerosPorCompanyia.get(this.datos.getCompanyiasTotales().get(pos)) !=
				 * null) { pasajerosPorCompanyia.put(this.datos.getCompanyiasTotales().get(pos),
				 * Stream.of(pasajerosPorCompanyia.get(this.datos.getCompanyiasTotales().get(pos
				 * )).get(0) + this.datos.getPasajeros().get(pos) *
				 * solucion.getVariables().get(i),
				 * pasajerosPorCompanyia.get(this.datos.getCompanyiasTotales().get(pos)).get(1)
				 * + this.datos.getPasajeros().get(pos))); } else {
				 * pasajerosPorCompanyia.put(this.datos.getCompanyiasTotales().get(pos),
				 * Stream.of(this.datos.getPasajeros().get(pos) *
				 * solucion.getVariables().get(i), this.datos.getPasajeros().get(pos) * 1.0)); }
				 * if (ingresosPorAreaInf.get(this.datos.getAresInfTotales().get(pos)) != null)
				 * { ingresosPorAreaInf.put(this.datos.getAresInfTotales().get(pos),
				 * Stream.of(ingresosPorAreaInf.get(this.datos.getAresInfTotales().get(pos)).get
				 * (0) + this.datos.getIngresos().get(pos) * solucion.getVariables().get(i),
				 * ingresosPorAreaInf.get(this.datos.getAresInfTotales().get(pos)).get(1) +
				 * this.datos.getIngresos().get(pos))); } else {
				 * ingresosPorAreaInf.put(this.datos.getAresInfTotales().get(pos),
				 * Stream.of(this.datos.getIngresos().get(pos) * solucion.getVariables().get(i),
				 * this.datos.getIngresos().get(pos))); } if
				 * (ingresosPorAerDest.get(this.datos.getConexionesTotalesSeparadas().get(pos).
				 * get(1)) != null) {
				 * ingresosPorAerDest.put(this.datos.getConexionesTotalesSeparadas().get(pos).
				 * get(1), Stream.of(
				 * ingresosPorAerDest.get(this.datos.getConexionesTotalesSeparadas().get(pos).
				 * get(1)).get(0) + this.datos.getTasas().get(pos) *
				 * solucion.getVariables().get(i),
				 * ingresosPorAerDest.get(this.datos.getConexionesTotalesSeparadas().get(pos).
				 * get(1)).get(0) + this.datos.getTasas().get(pos))); } else {
				 * ingresosPorAerDest.put(this.datos.getConexionesTotalesSeparadas().get(pos).
				 * get(1), Stream.of(this.datos.getTasas().get(pos) *
				 * solucion.getVariables().get(i), this.datos.getTasas().get(pos))); }
				 */

				pos++;
			}
			if (!aerDest.contains(datos.getConexionesTotales().get(i).get(1))) {
				aerDest.add(datos.getConexionesTotales().get(i).get(1));
			}
			if (!aerOr.contains(datos.getConexionesTotales().get(i).get(0))) {
				aerOr.add(datos.getConexionesTotales().get(i).get(0));
			}

			/*
			 * if (!this.datos.getConexionesTotales().get(i).get(0).equals(origen)) { if (i
			 * > 0) { Double Conectividadaux = 0.0; if (ConectividadauxTotalSuma != 0) {
			 * Conectividadaux = ConectividadauxSuma / ConectividadauxTotalSuma; }
			 * Conectividadsuma += this.datos.getConectividadesTotales().get(i - 1) * (1 -
			 * Conectividadaux); ConectividadtotalSuma +=
			 * this.datos.getConectividadesTotales().get(i - 1); } // Sumar las
			 * conectividades de los destinos y guardar el origen origen =
			 * this.datos.getConexionesTotales().get(i).get(0); ConectividadauxSuma = 0.0;
			 * ConectividadauxTotalSuma = 0.0; } // origen =
			 * this.datos.getConexionesTotales().get(i).get(0); ConectividadauxSuma +=
			 * solucion.getVariables().get(i)
			 * this.datos.getVuelosEntrantesConexionOrdenadoTotales().get(i);
			 * ConectividadauxTotalSuma +=
			 * this.datos.getVuelosEntrantesConexionOrdenadoTotales().get(i);
			 */
		}

		tooltips.put(Constantes.idObjetivo1,
				"The losses in the catchment areas if all connections are cancelled would be "
						+ String.valueOf(df.format(IngresosTtotalSuma)) + " euros.");
		tooltips.put(Constantes.idObjetivo2,
				"The number of catchment areas with losses if all connections are cancelled would be "
						+ String.valueOf(areasInf.size()) + ".");
		tooltips.put(Constantes.idObjetivo3,
				"The number of airlines with losses if all connections are cancelled would be "
						+ String.valueOf(companyias.size()) + ".");
		tooltips.put(Constantes.idObjetivo4, "The destination airport losses if all connections are cancelled would be "
				+ String.valueOf(df.format(Tasastotal)) + " euros.");
		tooltips.put(Constantes.idObjetivo5,
				"The number of destination airports with losses if all connections are cancelled would be "
						+ String.valueOf(aerDest.size()) + ".");
		tooltips.put(Constantes.idObjetivo6,
				"The number of affected passengers if all connections are cancelled would be "
						+ String.valueOf(Pasajerostotal) + ".");
		tooltips.put(Constantes.idObjetivo7,
				"The number of origin airports with reduced communication if all connections are cancelled would be "
						+ String.valueOf(aerOr.size()) + ".");

		return tooltips;
	}

	public static List<String> leerFicheroCola() throws FileNotFoundException, IOException, CsvException {
		List<List<String>> fichero = new ArrayList<>();
		int numFilas;
		try (CSVReader reader = new CSVReader(
				new FileReader(Constantes.rutaFicheros + Constantes.nombreFicheroCola + Constantes.extensionFichero))) {
			List<String[]> r = reader.readAll();
			numFilas = r.size();
			for (int fila = 0; fila < numFilas; fila++) {
				List<String> filaI = new ArrayList<>();
				for (int columna = 0; columna < r.get(fila).length; columna++) {

					filaI.add(r.get(fila)[columna]);
				}
				fichero.add(filaI);
			}
		}

		if (fichero.size() == 0) {
			return new ArrayList<>();
		} else {
			return fichero.get(0);
		}

	}

	public static void modificarFicheroCola(String nombre) throws IOException, CsvException {

		List<String[]> lista = new ArrayList<>();

		if (!nombre.equals("")) {
			String[] filaI = new String[1];
			filaI[0] = nombre;
			lista.add(filaI);
		}

		/*
		 * String[] filaI = new String[proyectosExistentes.size()]; for(int j = 0; j <
		 * proyectosExistentes.size(); j++) { filaI[j] = proyectosExistentes.get(j); }
		 * lista.add(filaI);
		 */

		try (CSVWriter writer = new CSVWriter(
				new FileWriter(Constantes.rutaFicheros + Constantes.nombreFicheroCola + Constantes.extensionFichero),
				',', CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END)) {
			writer.writeAll(lista);

		}

	}

	public static Map<String, String> leerCSVUsuarios() throws IOException, CsvException {
		List<List<String>> fichero = new ArrayList<>();
		int numFilas;
		try (CSVReader reader = new CSVReader(new FileReader(
				Constantes.rutaFicheros + Constantes.nombreFicheroUsuarios + Constantes.extensionFichero))) {
			List<String[]> r = reader.readAll();
			numFilas = r.size();
			for (int fila = 0; fila < numFilas; fila++) {
				List<String> filaI = new ArrayList<>();
				for (int columna = 0; columna < r.get(fila).length; columna++) {

					filaI.add(r.get(fila)[columna]);
				}
				fichero.add(filaI);
			}
		}

		Map<String, String> usuarios = new HashMap<>();

		for (List<String> usuario : fichero) {
			usuarios.put(usuario.get(0), usuario.get(1));
		}

		return usuarios;

	}

	public static void crearDirectorioProyecto(String nombre) throws IOException {
		Path path = Paths.get(Constantes.rutaFicherosProyectos + "//" + nombre);
		Files.createDirectories(path);
	}

	public static void crearDirectorioTemp(String nombre) throws IOException {
		Path path = Paths
				.get(Constantes.rutaFicherosProyectos + "//" + nombre + "//" + Constantes.nombreDirectorioTemp);
		Files.createDirectories(path);
	}

	public static void crearDirectorioTempSolucion(String nombre, String id) throws IOException {
		Path path = Paths.get(
				Constantes.rutaFicherosProyectos + "//" + nombre + "//" + Constantes.nombreDirectorioTemp + "//" + id);
		Files.createDirectories(path);
	}

	public static boolean borrarDirectorioProyecto(File proyecto) {
		return FileSystemUtils.deleteRecursively(proyecto);
	}

	public static void crearDirectorioFitness(String nombre) throws IOException {
		Path path = Paths.get(
				Constantes.rutaFicherosProyectos + "//" + nombre + "//" + Constantes.nombreDirectorioFicherosFitness);
		Files.createDirectories(path);
	}

	public static void crearDirectorioObjetivos(String nombre) throws IOException {
		Path path = Paths.get(
				Constantes.rutaFicherosProyectos + "//" + nombre + "//" + Constantes.nombreDirectorioFicherosObjetivos);
		Files.createDirectories(path);
	}

	public static void crearDirectorioRangos(String nombre) throws IOException {
		Path path = Paths.get(
				Constantes.rutaFicherosProyectos + "//" + nombre + "//" + Constantes.nombreDirectorioFicherosRangos);
		Files.createDirectories(path);
	}

	public static List<Individuo> quitarDuplicados(List<Individuo> p) {
		List<Individuo> indAQuitar = new ArrayList<Individuo>();
		for (int i = 0; i < p.size(); i++) {
			Individuo target = p.get(i);
			for (int j = i + 1; j < p.size(); j++) {
				if (target.getObjetivos().equals(p.get(j).getObjetivos())) {
					indAQuitar.add(p.get(j));
				}
			}
		}
		for (Individuo ind : indAQuitar) {
			p.remove(ind);
		}
		return p;
	}

	public static Individuo copiarIndividuo(Individuo ind) {
		Individuo nuevo = new Individuo(ind.getVariables().size(), ind.getObjetivos().size());
		List<Double> variables = ind.getVariables();
		int domina = ind.getdomina();
		List<Double> objetivos = ind.getObjetivos();
		List<Double> objetivosNorm = ind.getObjetivosNorm();
		List<Double> restricciones = ind.getRestricciones();
		boolean factible = ind.isFactible();
		Double constraintViolation = ind.getConstraintViolation();
		List<Double> fitnessHist = ind.getFitnessHist();
		Map<String, List<Double>> extra = ind.getExtra();

		nuevo.setConstraintViolation(constraintViolation);
		nuevo.setdomina(domina);
		nuevo.setFactible(factible);
		nuevo.setObjetivos(copiarLista(objetivos));
		nuevo.setObjetivosNorm(copiarLista(objetivosNorm));
		nuevo.setRestricciones(copiarLista(restricciones));
		nuevo.setVariables(copiarLista(variables));
		nuevo.setFitnessHist(fitnessHist);
		nuevo.setExtra(extra);

		return nuevo;
	}

	public static List<Double> copiarLista(List<Double> lista) {
		List<Double> nueva = new ArrayList<>();
		if (lista != null) {
			for (Double i : lista) {
				nueva.add(i);
			}
		}
		return nueva;
	}

	public static List<Double> rellenarListaDeCeros(List<Double> lista, int numCeros) {
		for (int i = 0; i < numCeros; i++) {
			lista.add(0.0);
		}
		return lista;
	}

	public static void formatearIndividuo(Individuo ind) {
		List<Double> objNormForm = new ArrayList<>();
		List<Double> fitnessHistForm = new ArrayList<>();

		for (int i = 0; i < ind.getObjetivosNorm().size(); i++) {
			String format = Constantes.df.format(ind.getObjetivosNorm().get(i));
			format = format.replace(",", ".");
			objNormForm.add(Double.valueOf(format));
		}

		for (int i = 0; i < ind.getFitnessHist().size(); i++) {
			String format = Constantes.df.format(ind.getFitnessHist().get(i));
			format = format.replace(",", ".");
			fitnessHistForm.add(Double.valueOf(format));
		}

		ind.setObjetivosNorm(objNormForm);
		ind.setFitnessHist(fitnessHistForm);
	}

	public static Individuo crearIndividuoConAtributos(List<Objetivo> obj, List<FitnessI> fit) {
		Individuo ind = new Individuo(0, obj.size());
		List<Double> objLista = new ArrayList<>();
		for (Objetivo o : obj) {
			objLista.add(o.getValor());
		}
		ind.setObjetivosNorm(objLista);

		List<Double> fitLista = new ArrayList<>();
		for (FitnessI f : fit) {
			fitLista.add(f.getFitness());
		}

		ind.setFitnessHist(fitLista);

		return ind;
	}
	
	public static List<Double> convertToDouble(List<Integer> ints){
		
		List<Double> doubles = new ArrayList<>();
		
		for(int i = 0; i < ints.size(); i++) {
			doubles.add(ints.get(i) * 1.0);
		}
		
		return doubles;
	}
}
