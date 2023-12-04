package com.nsgaiii.nsgaiiidemo.App.Algoritmo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.nsgaiii.nsgaiiidemo.App.TraducirSalida;
import com.nsgaiii.nsgaiiidemo.App.Modelo.Individuo;
import com.nsgaiii.nsgaiiidemo.App.Modelo.Poblacion;
import com.nsgaiii.nsgaiiidemo.App.Modelo.Salida.Aeropuerto;
import com.nsgaiii.nsgaiiidemo.App.Modelo.Salida.DatosConexiones;
import com.nsgaiii.nsgaiiidemo.App.Problemas.Problema;
import com.nsgaiii.nsgaiiidemo.App.Utils.Utils;
import com.opencsv.exceptions.CsvException;

public class BPSO {
	
	private int numIndividuos;
	private Problema problema;
	private double inertiaW;
	private double c1;
	private double c2;
	private Poblacion poblacionPartículas;
	private Poblacion poblacionPbest;
	private Individuo Gbest;
	private List<List<Double>> v0;
	private List<List<Double>> v1;
	private double r1;
	private double r2;
	private int num_Iteraciones = 0;
	private int Max_Num_Iteraciones = 0;
	private List<Double> fitnessHist = new ArrayList<>();
	
	public BPSO (int numIndividuos, int Max_Num_Iteraciones, Problema problema, double inertiaW, double c1, double c2) throws FileNotFoundException, IOException, CsvException {
		//this.numIndividuos = numIndividuos;
		this.numIndividuos = problema.getNumVariables() + 2;
		this.problema = problema;
		this.inertiaW = inertiaW;
		this.c1 = c1;
		this.c2 = c2;
		this.r1 = Utils.getRandNumber(0.0, 1.0);
		this.r2 = Utils.getRandNumber(0.0, 1.0);
		/*this.r1 = 0.39;
		this.r2 = 0.18;*/
		this.v0 = new ArrayList<>();
		this.v1 = new ArrayList<>();
		this.poblacionPartículas = new Poblacion(this.numIndividuos, this.problema);
		//Inicializar población o partículas y calcular fitness
		this.poblacionPartículas.generarPoblacionInicial(problema, false, null);
		
		//Calcular Pbest
		this.poblacionPbest = new Poblacion(this.numIndividuos, problema);
		this.calcularPbests();
		//Calcular Gbest
		this.compararFitness();
		
		this.rellenarVelocidadesIniciales();
		this.Max_Num_Iteraciones = Max_Num_Iteraciones;
		
		this.fitnessHist.add(this.Gbest.getObjetivos().get(0));
		System.out.println(this.num_Iteraciones + ": " + this.Gbest.getObjetivos());
	}
	
	public Individuo ejecutarBPSO() throws FileNotFoundException, IOException, CsvException {
		while (!condicionParadaConseguida()){
				//Calcular velocidades para cada bit de cada partícula, actualizar bits y fitness
			this.calcularVelocidades();
				
			//Calcular Pbest
			this.calcularPbests();
			//Calcular Gbest
			this.compararFitness();
			
			
			
			this.r1 = Utils.getRandNumber(0.0, 1.0);
			this.r2 = Utils.getRandNumber(0.0, 1.0);
			this.num_Iteraciones++;
			this.inertiaW = this.inertiaW * 0.9;
			//System.out.println(r1);
			//System.out.println(r2);
			//System.out.println(poblacionPartículas);
			//System.out.println(poblacionPbest);
			//System.out.println(Gbest);
			//System.out.println(this.v0);
			//System.out.println(this.v1);
			//System.out.println();
			System.out.println(this.num_Iteraciones + ": " + this.Gbest.getObjetivos());
			this.fitnessHist.add(this.Gbest.getObjetivos().get(0));
		}
		//System.out.println(this.poblacionPartículas);
		//System.out.println(this.poblacionPbest);
		System.out.println(this.Gbest);
		//System.out.println("Num conexiones: " + problema.getNumVariables());
		System.out.println("fin");
		this.Gbest.setFitnessHist(fitnessHist);
		return this.Gbest;
	}
	
	public void calcularVelocidades() throws FileNotFoundException, IOException, CsvException {
		for(int i = 0; i < this.numIndividuos; i++) {
			double vc;
			for(int j = 0; j < this.problema.getNumVariables(); j++) {
				double d1_1, d0_1, d1_2, d0_2;
				double v1, v0;
				if(this.poblacionPbest.getPoblacion().get(i).getVariables().get(j) == 1.0) {
					d1_1 = this.c1 * this.r1;
					d0_1 = -this.c1 * this.r1;
				}else {
					d1_1 = -this.c1 * this.r1;
					d0_1 = this.c1 * this.r1;
				}
				if(this.Gbest.getVariables().get(j) == 1.0) {
					d1_2 = this.c2 * this.r2;
					d0_2 = -this.c2 * this.r2;
				}else {
					d1_2 = -this.c2 * this.r2;
					d0_2 = this.c2 * this.r2;
				}
				v1 = this.inertiaW * this.v1.get(i).get(j) + d1_1 + d1_2;
				v0 = this.inertiaW * this.v0.get(i).get(j) + d0_1 + d0_2;
				this.v1.get(i).set(j, v1);
				this.v0.get(i).set(j, v0);
				
				if(this.poblacionPartículas.getPoblacion().get(i).getVariables().get(j) == 0.0) {
					vc = 1/(1+ Math.pow(Math.E, -v1));
				}else {
					vc = 1/(1+ Math.pow(Math.E, -v0));
				}
				if(Utils.getRandNumber(0.0, 1.0) < vc) {
					if(this.poblacionPartículas.getPoblacion().get(i).getVariables().get(j) == 0.0) {
						this.poblacionPartículas.getPoblacion().get(i).modIVariable(j, 1.0);
					}else {
						this.poblacionPartículas.getPoblacion().get(i).modIVariable(j, 0.0);
					}
				}
			}
			this.problema.evaluate(this.poblacionPartículas.getPoblacion().get(i));
		}
	}
	
	private void compararFitness() { 
		//Al comparar 2 soluciones:
		//1. Si se comparan 2 soluciones factibles es mejor la que tenga mejor Fitness
		//2. Si se compara 1 solucion factible con otra infactible es mejor la factible
		//3. Si se comparan 2 soluciones infactibles es mejor la que tenga menor Contraint Violation
		Individuo Temp_GBest = Utils.copiarIndividuo(poblacionPartículas.getPoblacion().get(0));
		for (int i = 1; i < this.numIndividuos; i++) {
			if(Temp_GBest.isFactible() && this.poblacionPartículas.getPoblacion().get(i).isFactible()) {
				if(this.poblacionPartículas.getPoblacion().get(i).getObjetivos()
						.get(this.problema.getNumObjetivos() - 1) 
						< Temp_GBest.getObjetivos().get(this.problema.getNumObjetivos() - 1)){
					Temp_GBest = Utils.copiarIndividuo(poblacionPartículas.getPoblacion().get(i));
				}
			}else if(!Temp_GBest.isFactible() && !this.poblacionPartículas.getPoblacion().get(i).isFactible()) {
				if(this.poblacionPartículas.getPoblacion().get(i).getConstraintViolation()
						< Temp_GBest.getConstraintViolation()) {
					Temp_GBest = Utils.copiarIndividuo(poblacionPartículas.getPoblacion().get(i));
				}
			}else if(!Temp_GBest.isFactible() && this.poblacionPartículas.getPoblacion().get(i).isFactible()){
				Temp_GBest = Utils.copiarIndividuo(poblacionPartículas.getPoblacion().get(i));
			}
			
		}
		
		if(this.Gbest != null) {
			if(this.Gbest.isFactible() && Temp_GBest.isFactible()) {
				if(Temp_GBest.getObjetivos().get(this.problema.getNumObjetivos() - 1) < 
						this.Gbest.getObjetivos().get(this.problema.getNumObjetivos() - 1)){
					this.Gbest = Utils.copiarIndividuo(Temp_GBest);
				}
			}else if(!this.Gbest.isFactible() && !Temp_GBest.isFactible()) {
				if(Temp_GBest.getConstraintViolation() < 
						this.Gbest.getConstraintViolation()){
					this.Gbest = Utils.copiarIndividuo(Temp_GBest);
				}
			}
			
		}else {
			this.Gbest = Utils.copiarIndividuo(Temp_GBest);
		}
	}
	
	private void calcularPbests() {
		//Al comparar 2 soluciones:
		//1. Si se comparan 2 soluciones factibles es mejor la que tenga mejor Fitness
		//2. Si se compara 1 solucion factible con otra infactible es mejor la factible
		//3. Si se comparan 2 soluciones infactibles es mejor la que tenga menor Contraint Violation
		List<Individuo> listaPBests = new ArrayList<>();
		for(int i = 0; i < this.numIndividuos; i++) {
			
			if(this.poblacionPbest.getPoblacion().get(i).getObjetivos().size() == this.poblacionPartículas.getPoblacion().get(i).getObjetivos().size()) {
				if(this.poblacionPartículas.getPoblacion().get(i).isFactible() && this.poblacionPbest.getPoblacion().get(i).isFactible()) {
					if(this.poblacionPartículas.getPoblacion().get(i).getObjetivos().get(this.problema.getNumObjetivos()-1) 
							< this.poblacionPbest.getPoblacion().get(i).getObjetivos().get(this.problema.getNumObjetivos()-1)) {
						Individuo nuevo = Utils.copiarIndividuo(this.poblacionPartículas.getPoblacion().get(i));
						listaPBests.add(nuevo);
					}else {
						Individuo nuevo = Utils.copiarIndividuo(this.poblacionPbest.getPoblacion().get(i));
						listaPBests.add(nuevo);
					}
				}else if(!this.poblacionPartículas.getPoblacion().get(i).isFactible() && !this.poblacionPbest.getPoblacion().get(i).isFactible()) {
					if(this.poblacionPartículas.getPoblacion().get(i).getConstraintViolation() 
							< this.poblacionPbest.getPoblacion().get(i).getConstraintViolation()) {
						Individuo nuevo = Utils.copiarIndividuo(this.poblacionPartículas.getPoblacion().get(i));
						listaPBests.add(nuevo);
					}else {
						Individuo nuevo = Utils.copiarIndividuo(this.poblacionPbest.getPoblacion().get(i));
						listaPBests.add(nuevo);
					}
				}else if(this.poblacionPartículas.getPoblacion().get(i).isFactible() && !this.poblacionPbest.getPoblacion().get(i).isFactible()) {
					Individuo nuevo = Utils.copiarIndividuo(this.poblacionPartículas.getPoblacion().get(i));
					listaPBests.add(nuevo);
				}else {
					Individuo nuevo = Utils.copiarIndividuo(this.poblacionPbest.getPoblacion().get(i));
					listaPBests.add(nuevo);
				}
				
			}else {
				Individuo nuevo = Utils.copiarIndividuo(this.poblacionPartículas.getPoblacion().get(i));
				listaPBests.add(nuevo);
			}
			
		}
		this.poblacionPbest.setPoblacion(listaPBests);
	}
	
	private void rellenarVelocidadesIniciales() {
		for(int i = 0; i < this.numIndividuos; i++) {
			List<Double> v0_i = new ArrayList<>();
			List<Double> v1_i = new ArrayList<>();
			for(int j = 0; j < this.problema.getNumVariables(); j++) {
				v0_i.add(0.0);
				v1_i.add(0.0);
			}
			this.v0.add(v0_i);
			this.v1.add(v1_i);
		}
	}
	
	private boolean condicionParadaConseguida () {
		if(this.num_Iteraciones >= this.Max_Num_Iteraciones) {
			return true;
		}else {
			return false;
		}
		
	}

}
