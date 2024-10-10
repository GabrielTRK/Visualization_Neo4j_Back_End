package com.VisNeo4j.App.Algoritmo;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.VisNeo4j.App.Algoritmo.Opciones.BPSOOpciones;
import com.VisNeo4j.App.Algoritmo.Parametros.BPSOParams;
import com.VisNeo4j.App.Algoritmo.Parametros.AccelerationCoefficientsUpdate.C1_C2;
import com.VisNeo4j.App.Constantes.Constantes;
import com.VisNeo4j.App.Modelo.Individuo;
import com.VisNeo4j.App.Modelo.Poblacion;
import com.VisNeo4j.App.Problemas.Problema;
import com.VisNeo4j.App.Utils.Utils;
import com.opencsv.exceptions.CsvException;

public class BPSO {
	
	private BPSOParams params;
	private Problema problema;
	private Poblacion poblacionPartículas;
	private Poblacion poblacionPbest;
	private List<Individuo> listaAux;
	private Individuo Gbest;
	private List<List<Double>> v0;
	private List<List<Double>> v1;
	private double r1;
	private double r2;
	private List<Double> fitnessHist = new ArrayList<>();
	private String proyecto;
	private int minDist = 3;
	private int maxIterAux = 3;
	private List<Integer> timers;
	private C1_C2 c1c2;
	
	public BPSO (Problema problema, BPSOParams params, String proyecto, BPSOOpciones opciones) throws FileNotFoundException, IOException, CsvException {
		this.params = params;
		this.problema = problema;
		this.r1 = Utils.getRandNumber(0.0, 1.0);
		this.r2 = Utils.getRandNumber(0.0, 1.0);
		this.v0 = new ArrayList<>();
		this.v1 = new ArrayList<>();
		this.proyecto = proyecto;
		this.poblacionPartículas = new Poblacion(this.params.getNumIndividuos(), this.problema);
		this.timers = new ArrayList<>(this.params.getNumIndividuos());
		this.listaAux = new ArrayList<>();
		this.c1c2 = new C1_C2(params.getC1(), 0.0, 0.0, params.getC2(), Constantes.nombreC1_C2UpdateTimeVarying);
		//Inicializar población o partículas y calcular fitness
		if(opciones.isContinuarOpt()) {
			this.poblacionPbest = new Poblacion(this.params.getNumIndividuos(), problema);
			//Llamar a utils para recuperar valores
			Utils.leerCSVPoblacionTemp(problema, proyecto, opciones.getId(), this.poblacionPartículas);
			Utils.leerCSVPbestsTemp(problema, proyecto, opciones.getId(), this.poblacionPbest);
			//Calcular objetivos
			this.poblacionPartículas.calcularObjetivos(problema);
			this.poblacionPbest.calcularObjetivos(problema);
			this.calcularAux();
			
			this.compararFitness();
			
			Utils.leerCSVV0Temp(proyecto, opciones.getId(), this.v0);
			Utils.leerCSVV1Temp(proyecto, opciones.getId(), this.v1);
			
			this.fitnessHist = Utils.leerCSVHistFitnessTemp(proyecto, String.valueOf(opciones.getId()));
		}else {
			this.poblacionPartículas.generarPoblacionInicial(problema, false, 29, proyecto);
			//Calcular Pbest
			this.poblacionPbest = new Poblacion(this.params.getNumIndividuos(), problema);
			this.calcularPbests();
			//Calcular aux
			this.calcularAux();
			//Calcular Gbest
			this.compararFitness();
			
			this.rellenarVelocidadesIniciales();
			
			this.fitnessHist.add(this.Gbest.getObjetivos().get(0));
		}
		
		System.out.println(this.params.getIteracionActual() + ": " + this.Gbest + " " + this.Gbest.getObjetivosNorm() + " " + this.Gbest.getRestricciones());
	}
	
	public Individuo ejecutarBPSO() throws FileNotFoundException, IOException, CsvException {
		while (!this.params.condicionParadaConseguida(this.poblacionPartículas, this.Gbest)){
				//Calcular velocidades para cada bit de cada partícula, actualizar bits y fitness
			this.params.setC1(this.c1c2.updateC1(this.params.getMax_Num_Iteraciones(), this.params.getIteracionActual()));
			this.params.setC2(this.c1c2.updateC2(this.params.getMax_Num_Iteraciones(), this.params.getIteracionActual()));
			this.calcularVelocidades();
				
			//Calcular Pbest
			this.calcularPbests();
			//Calcular Gbest
			this.compararFitness();
			
			
			
			this.r1 = Utils.getRandNumber(0.0, 1.0);
			this.r2 = Utils.getRandNumber(0.0, 1.0);
			
			this.params.updateInertiaW();
			//System.out.println(r1);
			//System.out.println(r2);
			//System.out.println(poblacionPartículas);
			//System.out.println(poblacionPbest);
			System.out.println(this.params.getIteracionActual() + ": " + Gbest);
			System.out.println(this.v0);
			System.out.println(this.v1);
			//System.out.println();
			//System.out.println(this.params.getIteracionActual() + ": " + this.Gbest.getObjetivos() + " " + this.Gbest.getObjetivosNorm() + " " + this.Gbest.getRestricciones());
			this.fitnessHist.add(this.Gbest.getObjetivos().get(0));
			this.Gbest.setFitnessHist(fitnessHist);
		}
		
		//System.out.println(this.poblacionPartículas);
		//System.out.println(this.poblacionPbest);
		
		//System.out.println("Num conexiones: " + problema.getNumVariables());
		System.out.println("fin");
		//this.Gbest.setFitnessHist(fitnessHist);
		this.Gbest.initExtra();
		this.problema.extra(this.Gbest);
		return this.Gbest;
	}
	
	public Individuo ejecutarBPSOALT() throws FileNotFoundException, IOException, CsvException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String command = "";
		while (!command.equals("1")){
			this.params.getCondicionParada().setNumIteracionesActual(this.params.getIteracionActual() +1);
			//Calcular velocidades para cada bit de cada partícula, actualizar bits y fitness
			this.params.setC1(this.c1c2.updateC1(this.params.getMax_Num_Iteraciones(), this.params.getIteracionActual()));
			this.params.setC2(this.c1c2.updateC2(this.params.getMax_Num_Iteraciones(), this.params.getIteracionActual()));
			this.calcularVelocidades();
				
			//Calcular Pbest
			this.calcularPbests();
			this.calcularAux();
			//Calcular Gbest
			this.compararFitness();
			
			
			
			this.r1 = Utils.getRandNumber(0.0, 1.0);
			this.r2 = Utils.getRandNumber(0.0, 1.0);
			
			this.params.updateInertiaW();
			//System.out.println(r1);
			//System.out.println(r2);
			//System.out.println(poblacionPartículas);
			//System.out.println(poblacionPbest);
			//System.out.println();
			//System.out.println(Gbest);
			//System.out.println(this.v0);
			//System.out.println(this.v1);
			System.out.println("c1: " + this.params.getC1());
			System.out.println("c2: " + this.params.getC2());
			//System.out.println();
			//System.out.println(this.params.getIteracionActual() + ": " + this.Gbest.getObjetivos() + " " + this.Gbest.getObjetivosNorm() + " " + this.Gbest.getRestricciones());
			this.fitnessHist.add(this.Gbest.getObjetivos().get(0));
			command = reader.readLine();
		}
		//System.out.println(this.poblacionPartículas);
		//System.out.println(this.poblacionPbest);
		
		//System.out.println("Num conexiones: " + problema.getNumVariables());
		System.out.println("fin");
		this.Gbest.setFitnessHist(fitnessHist);
		this.Gbest.initExtra();
		this.problema.extra(this.Gbest);
		return this.Gbest;
	}
	
	public void calcularVelocidades() throws FileNotFoundException, IOException, CsvException {
		//Calacular pendiente de funcione sigmoide
		//Distancia de hamming con todas las partículas
		//Porcentaje de partículas con una distancia < z
		double pendiente = this.calcularPendienteFuncion();
		//System.out.println("Pendiente: " + pendiente);
		for(int i = 0; i < this.params.getNumIndividuos(); i++) {
			//System.out.println(this.poblacionPartículas.getPoblacion().get(i) + " " + this.poblacionPbest.getPoblacion().get(i)); 
			double vc;
			for(int j = 0; j < this.problema.getNumVariables(); j++) {
				double d1_1, d0_1, d1_2, d0_2;
				double v1, v0;
				if(this.poblacionPbest.getPoblacion().get(i).getVariables().get(j) == 1.0) {
					d1_1 = this.params.getC1() * this.r1;
					d0_1 = -this.params.getC1() * this.r1;
				}else {
					d1_1 = -this.params.getC1() * this.r1;
					d0_1 = this.params.getC1() * this.r1;
				}
				if(this.Gbest.getVariables().get(j) == 1.0) {
					d1_2 = this.params.getC2() * this.r2;
					d0_2 = -this.params.getC2() * this.r2;
				}else {
					d1_2 = -this.params.getC2() * this.r2;
					d0_2 = this.params.getC2() * this.r2;
				}
				v1 = this.params.getInertiaW().getInertiaW() * this.v1.get(i).get(j) + d1_1 + d1_2;
				v0 = this.params.getInertiaW().getInertiaW() * this.v0.get(i).get(j) + d0_1 + d0_2;
				this.v1.get(i).set(j, v1);
				this.v0.get(i).set(j, v0);
				
				if(this.poblacionPartículas.getPoblacion().get(i).getVariables().get(j) == 0.0) {
					//vc = 1/(1+ Math.pow(Math.E, -v1));
					//vc = Math.abs(Math.tanh(v1));
					//System.out.print(v1 + " ");
					vc = 1/(1+ Math.pow(Math.E, -pendiente*v1));
					//System.out.println(1/(1+ Math.pow(Math.E, -pendiente*v1)));
				}else {
					//vc = 1/(1+ Math.pow(Math.E, -v0));
					//vc = Math.abs(Math.tanh(v0));
					//System.out.print(v0 + " ");
					vc = 1/(1+ Math.pow(Math.E, -pendiente*v0));
					//System.out.println(1/(1+ Math.pow(Math.E, -pendiente*v0)));
				}
				if(Utils.getRandNumber(0.0, 1.0) < vc) {
					if(this.poblacionPartículas.getPoblacion().get(i).getVariables().get(j) == 0.0) {
						this.poblacionPartículas.getPoblacion().get(i).modIVariable(j, 1.0);
					}else {
						this.poblacionPartículas.getPoblacion().get(i).modIVariable(j, 0.0);
					}
				}
			}
			//System.out.println();
			this.problema.evaluate(this.poblacionPartículas.getPoblacion().get(i));
		}
	}
	
	private void compararFitness() {
		//Al comparar 2 soluciones:
		//1. Si se comparan 2 soluciones factibles es mejor la que tenga mejor Fitness
		//2. Si se compara 1 solucion factible con otra infactible es mejor la factible
		//3. Si se comparan 2 soluciones infactibles es mejor la que tenga menor Contraint Violation
		Individuo Temp_GBest = Utils.copiarIndividuo(poblacionPartículas.getPoblacion().get(0));
		for (int i = 1; i < this.params.getNumIndividuos(); i++) {
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
		for(int i = 0; i < this.params.getNumIndividuos(); i++) {
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
	
	private void calcularAux() {
		if(this.listaAux.size() == this.params.getNumIndividuos()) {
			for(int i = 0; i < this.params.getNumIndividuos(); i++) {
				if(Utils.distanciaHamming(this.listaAux.get(i).getVariables(), this.poblacionPartículas.getPoblacion().get(i).getVariables()) < 1) {
					int timerAntes = this.timers.get(i);
					if(timerAntes > 0) {
						timerAntes--;
						this.timers.set(i, timerAntes);
					}
				}else {
					Individuo nuevo = Utils.copiarIndividuo(this.poblacionPartículas.getPoblacion().get(i));
					this.listaAux.set(i, nuevo);
					this.timers.set(i, this.maxIterAux);
				}
			}
		}else {
			for(Individuo i : this.poblacionPartículas.getPoblacion()) {
				Individuo nuevo = Utils.copiarIndividuo(i);
				this.timers.add(this.maxIterAux);
				this.listaAux.add(nuevo);
			}
		}
	}
	
	private void rellenarVelocidadesIniciales() {
		for(int i = 0; i < this.params.getNumIndividuos(); i++) {
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
	
	private Double calcularPendienteFuncion() {
		/*List<Integer> distanciaParticulasGbest = new ArrayList<>();
		for(int i = 0; i < this.poblacionPartículas.getPoblacion().size(); i++) {
			int distH = Utils.distanciaHamming(this.poblacionPartículas.getPoblacion().get(i).getVariables(), this.Gbest.getVariables());
			distanciaParticulasGbest.add(distH);
		}
		System.out.println("Distancia con Gbest: " + distanciaParticulasGbest);
		
		List<Integer> distanciaParticulasPbest = new ArrayList<>();
		
		for(int i = 0; i < this.poblacionPartículas.getPoblacion().size(); i++) {
			int distH = Utils.distanciaHamming(this.poblacionPartículas.getPoblacion().get(i).getVariables(), this.poblacionPbest.getPoblacion().get(i).getVariables());
			distanciaParticulasPbest.add(distH);
		}
		System.out.println("Distancia con Pbest: " + distanciaParticulasPbest);*/
		
		double sumaTimers0 = 0;
		for(int i = 0; i < this.timers.size(); i++) {
			if(this.timers.get(i) == 0) {
				sumaTimers0++;
			}
		}
		
		//System.out.println(this.timers);
		
		return 100.0 * (1 - (sumaTimers0 / this.poblacionPartículas.getNumIndividuos()));
	}

	public BPSOParams getParams() {
		return params;
	}

	public void setParams(BPSOParams params) {
		this.params = params;
	}

	public Problema getProblema() {
		return problema;
	}

	public void setProblema(Problema problema) {
		this.problema = problema;
	}

	public Poblacion getPoblacionPartículas() {
		return poblacionPartículas;
	}

	public void setPoblacionPartículas(Poblacion poblacionPartículas) {
		this.poblacionPartículas = poblacionPartículas;
	}

	public Poblacion getPoblacionPbest() {
		return poblacionPbest;
	}

	public void setPoblacionPbest(Poblacion poblacionPbest) {
		this.poblacionPbest = poblacionPbest;
	}

	public Individuo getGbest() {
		return Gbest;
	}

	public void setGbest(Individuo gbest) {
		Gbest = gbest;
	}

	public List<List<Double>> getV0() {
		return v0;
	}

	public void setV0(List<List<Double>> v0) {
		this.v0 = v0;
	}

	public List<List<Double>> getV1() {
		return v1;
	}

	public void setV1(List<List<Double>> v1) {
		this.v1 = v1;
	}

	public double getR1() {
		return r1;
	}

	public void setR1(double r1) {
		this.r1 = r1;
	}

	public double getR2() {
		return r2;
	}

	public void setR2(double r2) {
		this.r2 = r2;
	}

	public List<Double> getFitnessHist() {
		return fitnessHist;
	}

	public void setFitnessHist(List<Double> fitnessHist) {
		this.fitnessHist = fitnessHist;
	}

	public String getProyecto() {
		return proyecto;
	}

	public void setProyecto(String proyecto) {
		this.proyecto = proyecto;
	}

}
