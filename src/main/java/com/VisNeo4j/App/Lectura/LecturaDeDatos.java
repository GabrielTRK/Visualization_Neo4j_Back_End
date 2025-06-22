package com.VisNeo4j.App.Lectura;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.VisNeo4j.App.Constantes.Constantes;
import com.VisNeo4j.App.Model.Particle;
import com.VisNeo4j.App.Problems.Problem;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

public class LecturaDeDatos {
	
	public static void leerCoordenadasAeropuertos(Map<String, List<Double>> coordenadas) {
		try {
            Scanner scanner = new Scanner(new File(Constantes.rutaDatos + Constantes.nombreFicheroCoorenadas + Constantes.extensionFichero));
            //Comma as a delimiter
            scanner.useDelimiter("\n");
            scanner.next();
            while (scanner.hasNext()) {
                String str = scanner.next();
                String split[] = str.split(",");
                coordenadas.put(split[1], Stream.of(Double.parseDouble(split[2]), Double.parseDouble(split[3])).collect(Collectors.toList()));
            }
            // Closing the scanner
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("El path del documento conexiones no está bien especificado");
            //do something with e, or handle this case
        }
	}
	
	public static void leerDatosRRPS_PATDiaI(String ruta, List<Double> riesgos, 
			List<Double> riesgos_KP, List<List<String>> conexiones, 
			List<List<String>> conexionesTotal, List<Integer> pasajeros, 
			List<Integer> pasajeros_KP, List<Double> dineroMedioT, List<Double> dineroMedioT_KP, 
			List<Double> dineroMedioN, List<Double> dineroMedioN_KP, 
			List<String> companyias, List<List<Integer>> pasajerosCompanyias_KP, 
			List<List<String>> companyias_KP, List<String> areasInf, List<String> areasInf_KP, 
			List<String> continentes, List<Boolean> capital, List<Double> conectividades, 
			Map<List<String>, Integer> vuelosEntrantesConexion, 
			Map<String, Integer> vuelosSalientesAEspanya, List<Double> tasasAeropuertos, 
			List<Double> tasasAeropuertos_KP, Map<String, Integer> vuelosSalientes, 
			List<Integer> vuelosSalientesDeOrigen, List<List<String>> conexionesNombres,
			List<List<String>> conexionesNombresTotal, 
			List<String> aeropuertosOrigen) {
		try {
            Scanner scanner = new Scanner(new File(Constantes.rutaDatosPorDia + ruta + Constantes.extensionFichero));
            //Comma as a delimiter
            scanner.useDelimiter("\n");
            scanner.next();
            while (scanner.hasNext()) {
                String str = scanner.next();
                String split[] = str.split(",");
                
                if(!conexiones.contains(Stream.of(split[7], split[8]).collect(Collectors.toList()))) {
                	conexiones.add(Stream.of(split[7], split[8]).collect(Collectors.toList()));
                	conexionesNombres.add(Stream.of(split[13], split[14]).collect(Collectors.toList()));
                	vuelosEntrantesConexion.put(Stream.of(split[7], split[8]).collect(Collectors.toList()), 1);
                    continentes.add(split[11]);
                    capital.add(Boolean.parseBoolean(split[12]));
                    
                    riesgos_KP.add(Double.parseDouble(split[0]));
                    pasajeros_KP.add(Integer.parseInt(split[1]));
                    dineroMedioT_KP.add(Double.parseDouble(split[4]));
                    dineroMedioN_KP.add(Double.parseDouble(split[5]));
                    tasasAeropuertos_KP.add(Double.parseDouble(split[6]));
                    areasInf_KP.add(split[10]);
          
                    companyias_KP.add(Stream.of(split[2]).collect(Collectors.toList()));
                    pasajerosCompanyias_KP.add(Stream.of(Integer.parseInt(split[1])).collect(Collectors.toList()));
                    
                	if(Double.parseDouble(split[3]) == -1.0) {
                		conectividades.add(0.0);
					}else {
						conectividades.add(Double.parseDouble(split[3]));
					}
                }else {
                	vuelosEntrantesConexion.put(Stream.of(split[7], split[8]).collect(Collectors.toList()), 1 + vuelosEntrantesConexion.get(Stream.of(split[7], split[8]).collect(Collectors.toList())));
                	int posicion = conexiones.indexOf(Stream.of(split[7], split[8]).collect(Collectors.toList()));
                	riesgos_KP.set(posicion, riesgos_KP.get(posicion) + Double.parseDouble(split[0]));
                	pasajeros_KP.set(posicion, pasajeros_KP.get(posicion) + Integer.parseInt(split[1]));
                	dineroMedioT_KP.set(posicion, dineroMedioT_KP.get(posicion) + Double.parseDouble(split[4]));
                	dineroMedioN_KP.set(posicion, dineroMedioN_KP.get(posicion) + Double.parseDouble(split[5]));
                	tasasAeropuertos_KP.set(posicion, tasasAeropuertos_KP.get(posicion) + Double.parseDouble(split[6]));
                	
                	if(!companyias_KP.get(posicion).contains(split[2])) {
                		companyias_KP.get(posicion).add(split[2]);
                    	pasajerosCompanyias_KP.get(posicion).add(Integer.parseInt(split[1]));
                    }else {
                    	int posicionCompanyia = companyias_KP.get(posicion).indexOf(split[2]);
                    	
                    	pasajerosCompanyias_KP.get(posicion).set(posicionCompanyia, 
                    			pasajerosCompanyias_KP.get(posicion).get(posicionCompanyia) + Integer.parseInt(split[1]));
                    }
                }
                if(!vuelosSalientesAEspanya.keySet().contains(split[7])) {
					vuelosSalientesAEspanya.put(split[7], 1);
				}else {
					vuelosSalientesAEspanya.put(split[7], 1 + vuelosSalientesAEspanya.get(split[7]));
				}
                if(!vuelosSalientes.keySet().contains(split[7])) {
                	vuelosSalientes.put(split[7], Integer.parseInt(split[9]));
                }
                
                conexionesTotal.add(Stream.of(split[7], split[8]).collect(Collectors.toList()));
            	conexionesNombresTotal.add(Stream.of(split[13], split[14]).collect(Collectors.toList()));
                riesgos.add(Double.parseDouble(split[0]));
                pasajeros.add(Integer.parseInt(split[1]));
                dineroMedioT.add(Double.parseDouble(split[4]));
                dineroMedioN.add(Double.parseDouble(split[5]));
                tasasAeropuertos.add(Double.parseDouble(split[6]));
                companyias.add(split[2]);
                areasInf.add(split[10]);
                
                if(!aeropuertosOrigen.contains(split[7])) {
                	aeropuertosOrigen.add(split[7]);
                }
            }
            // Closing the scanner
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("El path del documento conexiones no está bien especificado");
            //do something with e, or handle this case
        }
	}

}
