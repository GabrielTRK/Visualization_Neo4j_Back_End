package com.VisNeo4j.App.Modelo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.VisNeo4j.App.Problems.Problem;
import com.VisNeo4j.App.Utils.Utils;
import com.opencsv.exceptions.CsvException;


public class Population {
	/*Una población está compuesta por: 
		- El número de individuos
		- Una lista de individuos
	 */
	
	private int numParticles;
	private List<Particle> population;
	
	public Population(int numParticles, Problem p) {
		this.numParticles = numParticles;
		this.population = new ArrayList<Particle>(this.numParticles);
		
		populationPorDefecto(p.getNumVariables(), p.getNumObjetivos());
	}
	
	private void populationPorDefecto(int numValores, int numObjetivos) {
		for (int i = 0; i < this.numParticles; i++) {
			this.population.add(i, new Particle(numValores, numObjetivos));
		}
	}

	public int getnumParticles() {
		return numParticles;
	}
	
	
	public void setnumParticles(int numParticles) {
		this.numParticles = numParticles;
	}
	
	public void setIParticle(int posicion, List<Double> valores) {
		if (!population.isEmpty()) {
			Particle individuo = population.get(posicion);
			individuo.setVariables(valores);
			population.set(posicion, individuo);
		}
	}

	public void generateInitialPopulation(Problem p, boolean leerFichero, String nombreFichero) throws FileNotFoundException, IOException, CsvException {
		if(!leerFichero) {
			this.getValues(p);
			this.getObjectives(p);
		}else {
			List<Particle> populationAux = new ArrayList<Particle>();
			populationAux = Utils.leerCSV(nombreFichero);
			if (populationAux.size() > this.numParticles) {
				populationAux = populationAux.subList(0, this.numParticles);
			}
			this.population = populationAux;
			
			this.obtenerIndividuosRestantes(p);
		}
		
	}
	
	public void generateInitialPopulation(Problem p, boolean leerFichero, int filaFichero, String proyecto) throws FileNotFoundException, IOException, CsvException {
		if(!leerFichero) {
			this.getValues(p);
			this.getObjectives(p);
		}else {
			List<Particle> populationAux = new ArrayList<Particle>();
			List<String> bits = Utils.leerCSVproblema(proyecto, filaFichero);
			List<Double> bitsDouble = new ArrayList<>();
			for(int i = 2; i < bits.size(); i++) {
				bitsDouble.add(Double.valueOf(bits.get(i)));
			}
			Particle ind = new Particle(p.getNumVariables(), p.getNumObjetivos());
			ind.setVariables(bitsDouble);
			ind = p.evaluate(ind);
			populationAux.add(ind);
			this.population = populationAux;
			p.sumarNumInicializaciones();
			this.obtenerIndividuosRestantes(p);
		}
		
	}
	
	public void getValues(Problem p) {
		for (int i = 0; i < this.numParticles; i++) {
			Particle individuo = this.population.get(i);
			individuo = p.inicializarValores(individuo);
			this.population.set(i, individuo);
		}
	}
	
	public void getObjectives(Problem p) throws FileNotFoundException, IOException, CsvException {
		for (int i = 0; i < this.numParticles; i++) {
			Particle individuo = this.population.get(i);
			individuo = p.evaluate(individuo);
			individuo = p.repararMejorar(individuo);
			this.population.set(i, individuo);
		}
	}
	
	public void obtenerIndividuosRestantes(Problem p) throws FileNotFoundException, IOException, CsvException {
		while(this.population.size() < this.numParticles) {
			Particle individuo = new Particle(p.getNumVariables(), p.getNumObjetivos());
			individuo = p.inicializarValores(individuo);
			individuo = p.evaluate(individuo);
			this.population.add(individuo);
		}
	}
	
	public int contarIndividuosDeUnRank(Population p, int rank) {
		int contador = 0;
		for (int i = 0; i < p.getnumParticles(); i++) {
			if(p.getPopulation().get(i).getdomina() == rank) {
				contador++;
			}
		}
		return contador;
	}
	
	public void añadirALapopulation(List<Particle> lista) {
		for (int i = 0; i < lista.size(); i++) {
			this.population.add(lista.get(i));
		}
	}
	
	
	public List<Particle> getPopulation() {
		return population;
	}
	
	
	public void setPopulation(List<Particle> population) {
		this.population = population;
	}


	@Override
	public String toString() {
		return "population [population=" + population + "]";
	}
	
	

}
