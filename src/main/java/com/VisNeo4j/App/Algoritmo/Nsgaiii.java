package com.VisNeo4j.App.Algoritmo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.VisNeo4j.App.Model.Particle;
import com.VisNeo4j.App.Model.Population;
import com.VisNeo4j.App.Model.ReferencePoint;
import com.VisNeo4j.App.Operators.CrossoverOperator;
import com.VisNeo4j.App.Operators.MutationOperator;
import com.VisNeo4j.App.Operators.ReplacementOperator;
import com.VisNeo4j.App.Operators.ChoosingOperator;
import com.VisNeo4j.App.Problems.Problem;
import com.VisNeo4j.App.Utils.Utils;
import com.opencsv.exceptions.CsvException;

public class Nsgaiii {
	
	private Population poblacion;
	private List<Particle> frenteDePareto;
	private List<Particle> frentesAux = new ArrayList<>();
	private int numGeneraciones;
	private CrossoverOperator cruce;
	private MutationOperator mutacion;
	private Double indiceDistrC;
	private Double indiceDistrM;
	private ChoosingOperator seleccion;
	private ReplacementOperator reemplazo;
	private List<ReferencePoint> referencePoints = new Vector<>();
	private Problem problema;
	private boolean elitismo;
	private int tamañoAux;
	private List<Particle> structAux = new ArrayList<>();
	private List<Particle> structAuxAnterior = new ArrayList<>();
	private int contIguales = 0;
	
	public Nsgaiii (int numIndividuos,
			int numGeneraciones, Double indiceDistrC, Double indiceDistrM, double probCruce,
			double probMut, int numberOfDivisions, Problem prob, boolean leerF, 
			String nombreFichero, boolean elitismo, int tamañoAux) throws FileNotFoundException, IOException, CsvException {
		
		//Inicializar problema, poblacion y operadores
		this.problema = prob;
		this.poblacion = new Population(numIndividuos, this.problema);
		this.indiceDistrC = indiceDistrC;
		this.indiceDistrM = indiceDistrM;
		this.cruce = new CrossoverOperator(probCruce, this.indiceDistrC);
		this.mutacion = new MutationOperator(probMut, this.indiceDistrM);
		this.seleccion = new ChoosingOperator();
		(new ReferencePoint()).generateReferencePoints(referencePoints, this.problema.getNumObjetivos(), numberOfDivisions);
		this.reemplazo = new ReplacementOperator(this.problema.getNumObjetivos(), referencePoints);
		this.numGeneraciones = numGeneraciones;
		this.elitismo = elitismo;
		this.structAux = new ArrayList<>();
		this.tamañoAux = tamañoAux;
		this.poblacion.generateInitialPopulation(this.problema, leerF, nombreFichero);

	}
	
	public List<Particle> ejecutarNSGAIII() throws FileNotFoundException, IOException, CsvException{
		Population hijos;
		
		int contadorGeneraciones = 0;
		long startTime = 0;
		long elapsedTime = 0;
		//Se mantiene en el bucle hasta que se lleguen al nº de generaciones especificado
		while (!condicionDeParadaConseguida(contadorGeneraciones, this.poblacion, elapsedTime)) {
			startTime = System.nanoTime();
			System.out.println(contadorGeneraciones);
			hijos = generarDescendientes(contadorGeneraciones); //Seleccion, cruce y mutacion
			obtenerNuevaGeneracion(hijos); //Reemplazo
			contadorGeneraciones++;
			elapsedTime = (System.nanoTime() - startTime) / 1000000000;
		}
		//this.frenteDePareto = this.reemplazo.obtenerFrentes(frentesAux, problema).get(0);
		this.frenteDePareto = this.reemplazo.obtenerFrentes(poblacion, problema).get(0);
		return this.structAux; //Devuelve el frente de pareto obtenido
	}
	
	private Population generarDescendientes(int contadorGeneraciones) throws FileNotFoundException, IOException, CsvException {
		Population poblacionHijos = new Population(this.poblacion.getnumParticles(), this.problema);
		List<Particle> totalHijos = new ArrayList<Particle>(this.poblacion.getnumParticles());
		
		int contadorIndividuos = 0;
		
		while (totalHijos.size() < this.poblacion.getnumParticles()) {
			List<Particle> nuevosHijos = new ArrayList<>(2);
			//Seleccion
			if(this.elitismo/* && contadorGeneraciones > 0*/) {
				nuevosHijos = this.seleccion.seleccionPorTorneoNSGAIII(this.poblacion);
			}else {
				nuevosHijos = this.seleccion.seleccionAleatoria(this.poblacion);
			}
			
			
			//Cruce
			nuevosHijos = this.cruce.SBX(
					nuevosHijos.get(0),
					nuevosHijos.get(1),
					this.problema);
			//Mutacion
			nuevosHijos.set(0, this.mutacion.polyMut(nuevosHijos.get(0), this.problema));
			nuevosHijos.set(1, this.mutacion.polyMut(nuevosHijos.get(1), this.problema));
			
			//Añadir hijos e incrementar el contador
			contadorIndividuos = contadorIndividuos + 2;
			if(this.poblacion.getnumParticles() - totalHijos.size() == 1) {
				totalHijos.add(contadorIndividuos - 2, nuevosHijos.get(0));
			}else {
				totalHijos.add(contadorIndividuos - 2, nuevosHijos.get(0));
				totalHijos.add(contadorIndividuos - 1, nuevosHijos.get(1));
			}
		}
		poblacionHijos.setPopulation(totalHijos);
		poblacionHijos.getObjectives(this.problema);
		
		
		return poblacionHijos;
	}
	
	private void obtenerNuevaGeneracion(Population hijos) {
		//Reemplazo
		Population total = Utils.juntarPoblaciones(this.poblacion, hijos, this.problema);
		Population totalAux = total;
		this.frentesAux = this.reemplazo.obtenerFrentes(totalAux, this.problema).get(0);
		//Elegir grupos según el ranking y aplicar el método de Das y Dennis cuando corresponda
		this.poblacion = this.reemplazo.rellenarPoblacionConFrentes(this.poblacion,
				total, this.problema);
		
		
		this.structAux.addAll(this.frentesAux);
		//quitar duplicados
		this.structAux = Utils.quitarDuplicados(this.structAux);
		//quitar dominados
		this.structAux = this.reemplazo.obtenerPrimerFrente(this.structAux, problema);
		if(this.structAux.size() > this.tamañoAux) {
			this.structAux = this.structAux.subList(0, this.tamañoAux);
		}
		if(this.structAux.size() == this.tamañoAux) {
			this.contIguales++;
		}
		System.out.println("Tamaño: " + this.structAux.size());
		System.out.println("Cuenta: " + this.contIguales);
	}
	
	private boolean condicionDeParadaConseguida(int contadorGeneraciones, Population p, long tiempo) {
		
		//Se comprueba la generación en la que se encuentra el algoritmo
		if(contIguales >= this.numGeneraciones /*|| tiempo >= 5*/) {
			return true;
		} else {
			return false;
		}
	}

}
