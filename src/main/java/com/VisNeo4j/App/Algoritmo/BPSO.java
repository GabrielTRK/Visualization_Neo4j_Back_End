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
import com.VisNeo4j.App.Model.Particle;
import com.VisNeo4j.App.Model.Population;
import com.VisNeo4j.App.Problems.Problem;
import com.VisNeo4j.App.Utils.Utils;
import com.opencsv.exceptions.CsvException;

public class BPSO {

	private BPSOParams params;
	private Problem problem;
	private Population particlesPopulation;
	private Population PbestPopulation;
	//private Poblacion poblacionLbest;
	//private int neighborhood = 10;
	private List<Particle> listaAux;
	private Particle Gbest;
	private List<List<Double>> v0;
	private List<List<Double>> v1;
	//private List<List<Double>> v0L;
	//private List<List<Double>> v1L;
	//private double u = 0.0;
	//private double u2 = 0.0;
	private double r1;
	private double r2;
	private List<Double> fitnessHist = new ArrayList<>();
	private String proyecto;
	//private int minDist = 3;
	//private int maxIterAux = 3;
	//private List<Integer> timers;
	//private List<List<Integer>> timers2;
	private C1_C2 c1c2;
	// private C1_C2 c1c2Temp;
	private int maxDistH = 1;
	private double maxSlope = 5.0;
	private double minSlope = 1.0;
	//private double maxPendienteL = 7.0;
	//private double minPendienteL = 3.0;
	private double maxV = 3.0;
	private double minV = -3.0;

	public BPSO(Problem problem, BPSOParams params, String proyecto, BPSOOpciones opciones)
			throws FileNotFoundException, IOException, CsvException {
		this.params = params;
		this.problem = problem;
		this.r1 = Utils.getRandNumber(0.0, 1.0);
		this.r2 = Utils.getRandNumber(0.0, 1.0);
		this.v0 = new ArrayList<>();
		this.v1 = new ArrayList<>();
		//this.v0L = new ArrayList<>();
		//this.v1L = new ArrayList<>();
		this.proyecto = proyecto;
		this.particlesPopulation = new Population(this.params.getnumParticles(), this.problem);
		//this.timers = new ArrayList<>(this.params.getnumParticles());
		//this.timers2 = new ArrayList<>();
		this.listaAux = new ArrayList<>();
		this.c1c2 = new C1_C2(params.getC1(), 0.0, 0.0, params.getC2(), Constantes.nombreC1_C2UpdateTimeVaryingLineal);
		// Inicializar población o partículas y calcular fitness
		if (opciones.isContinuarOpt()) {
			this.PbestPopulation = new Population(this.params.getnumParticles(), problem);
			// this.poblacionLbest = new Poblacion(this.params.getnumParticles(),
			// problem);
			// Llamar a utils para recuperar valores
			Utils.leerCSVPoblacionTemp(problem, proyecto, opciones.getId(), this.particlesPopulation);
			Utils.leerCSVPbestsTemp(problem, proyecto, opciones.getId(), this.PbestPopulation);
			// Calcular objetivos
			this.particlesPopulation.getObjectives(problem);
			this.PbestPopulation.getObjectives(problem);
			// this.calcularAux();

			// this.compararFitness();
			this.initialVelocities();

			Utils.leerCSVV0Temp(proyecto, opciones.getId(), this.v0);
			Utils.leerCSVV1Temp(proyecto, opciones.getId(), this.v1);
			// Utils.leerCSVV0LTemp(proyecto, opciones.getId(), this.v0L);
			// Utils.leerCSVV1LTemp(proyecto, opciones.getId(), this.v1L);

			this.fitnessHist = Utils.leerCSVHistFitnessTemp(proyecto, String.valueOf(opciones.getId()));
		} else {
			this.particlesPopulation.generateInitialPopulation(problem, false, 29, proyecto);
			// Calcular Pbest
			this.PbestPopulation = new Population(this.params.getnumParticles(), problem);

			this.initialVelocities();

			this.fitnessHist.add(this.Gbest.getObjectives().get(0));
		}
	}

	public Particle runBPSO() throws FileNotFoundException, IOException, CsvException {
		while (!this.params.stopConditionMet(this.particlesPopulation, this.Gbest)) {
			// Calcular velocidades para cada bit de cada partícula, actualizar bits y
			// fitness
			this.params
					.setC1(this.c1c2.updateC1(this.params.getMax_FEs(), this.params.getcurrentFE()));
			this.params
					.setC2(this.c1c2.updateC2(this.params.getMax_FEs(), this.params.getcurrentFE()));

			
			this.updatePositionsAndPbestGbest();

			this.r1 = Utils.getRandNumber(0.0, 1.0);
			this.r2 = Utils.getRandNumber(0.0, 1.0);

			this.params.updateInertiaW();
			
			System.out.println(this.params.getcurrentFE() + ": " + Gbest);
			
			this.fitnessHist.add(this.Gbest.getObjectives().get(0));
			this.Gbest.setFitnessHist(fitnessHist);
		}

		this.problem.extra(this.Gbest);
		return this.Gbest;
	}

	public Particle ejecutarBPSOALT() throws FileNotFoundException, IOException, CsvException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String command = "";
		while (!command.equals("1")) {
			this.params.getstopCondition().setcurrentFE(this.params.getcurrentFE() + 1);
			// Calcular velocidades para cada bit de cada partícula, actualizar bits y
			// fitness
			this.params
					.setC1(this.c1c2.updateC1(this.params.getMax_FEs(), this.params.getcurrentFE()));
			this.params
					.setC2(this.c1c2.updateC2(this.params.getMax_FEs(), this.params.getcurrentFE()));

			this.updatePositionsAndPbestGbest();

			// System.out.println("Gbest: " + this.DistanciasGbest());
			// System.out.println("Pbest: " + this.DistanciasPbest());
			// System.out.println("Lbest: " + this.DistanciasLbest());

			// Calcular Pbest
			// this.calcularPbestsYGbest();
			// this.calcularAux();
			// Calcular Gbest
			// this.compararFitness();

			this.r1 = Utils.getRandNumber(0.0, 1.0);
			this.r2 = Utils.getRandNumber(0.0, 1.0);

			this.params.updateInertiaW();
			// this.updateU(this.params.getMax_Num_Iteraciones(),
			// this.params.getIteracionActual());
			// System.out.println(r1);
			// System.out.println(r2);
			// System.out.println("Pbest: " +
			// PbestPopulation.getPopulation().get(0).getVariables());
			// System.out.println("Lbest: " +
			// poblacionLbest.getPopulation().get(0).getVariables());
			// System.out.println("Gbest: " + this.Gbest.getVariables());
			// System.out.println();
			// System.out.println(this.params.getIteracionActual());
			System.out.println(this.params.getcurrentFE() + ": " + Gbest);
			System.out.println("Ind: " + particlesPopulation.getPopulation().get(0));
			// System.out.println(this.params.getInertiaW().getInertiaW());
			// System.out.println(this.v0.get(0));
			// System.out.println(this.v1.get(0));
			// System.out.println("c1: " + this.params.getC1());
			// System.out.println("c2: " + this.params.getC2());
			// System.out.println();
			// System.out.println(this.params.getIteracionActual() + ": " +
			// this.Gbest.getObjectives() + " " + this.Gbest.getObjectivesNorm() + " " +
			// this.Gbest.getRestricciones());
			this.fitnessHist.add(this.Gbest.getObjectives().get(0));
			command = reader.readLine();
		}
		// System.out.println(this.particlesPopulation);
		// System.out.println(this.PbestPopulation);

		// System.out.println("Num conexiones: " + problem.getNumVariables());
		System.out.println("fin");
		this.Gbest.setFitnessHist(fitnessHist);
		//this.Gbest.initExtra();
		this.problem.extra(this.Gbest);
		return this.Gbest;
	}

	/*public void calcularVelocidades() throws FileNotFoundException, IOException, CsvException {
		// Calacular pendiente de funcione sigmoide
		// Distancia de hamming con todas las partículas
		// Porcentaje de partículas con una distancia < z
		double pendiente = this.calcularPendienteFuncion();
		// double pendiente = 1.0;
		// System.out.println(pendiente);
		// System.out.println("Gbest: " + this.DistanciasGbest());
		// System.out.println("Pbest: " + this.DistanciasPbest());
		for (int i = 0; i < this.params.getnumParticles(); i++) {
			// System.out.println(this.particlesPopulation.getPopulation().get(i) + " " +
			// this.PbestPopulation.getPopulation().get(i));
			double vc;

			for (int j = 0; j < this.problem.getNumVariables(); j++) {
				double d1_1, d0_1, d1_2, d0_2;
				double v1, v0;
				if (this.PbestPopulation.getPopulation().get(i).getVariables().get(j) == 1.0) {
					d1_1 = this.params.getC1() * this.r1;
					d0_1 = -this.params.getC1() * this.r1;
				} else {
					d1_1 = -this.params.getC1() * this.r1;
					d0_1 = this.params.getC1() * this.r1;
				}
				if (this.Gbest.getVariables().get(j) == 1.0) {
					d1_2 = this.params.getC2() * this.r2;
					d0_2 = -this.params.getC2() * this.r2;
				} else {
					d1_2 = -this.params.getC2() * this.r2;
					d0_2 = this.params.getC2() * this.r2;
				}
				v1 = this.params.getInertiaW().getInertiaW() * this.v1.get(i).get(j) + d1_1 + d1_2;
				v0 = this.params.getInertiaW().getInertiaW() * this.v0.get(i).get(j) + d0_1 + d0_2;

				if (v1 > this.maxV) {
					this.v1.get(i).set(j, this.maxV);
				} else if (v1 < this.minV) {
					this.v1.get(i).set(j, this.minV);
				} else {
					this.v1.get(i).set(j, v1);
				}

				if (v0 > this.maxV) {
					this.v0.get(i).set(j, this.maxV);
				} else if (v0 < this.minV) {
					this.v0.get(i).set(j, this.minV);
				} else {
					this.v0.get(i).set(j, v0);
				}

				if (this.particlesPopulation.getPopulation().get(i).getVariables().get(j) == 0.0) {
					// vc = 1/(1+ Math.pow(Math.E, -10*v1));
					// vc = Math.abs(Math.tanh(v1));
					// System.out.print(v1 + " ");
					vc = 1 / (1 + Math.pow(Math.E, -pendiente * v1));
					// System.out.println(1/(1+ Math.pow(Math.E, -pendiente*v1)));
				} else {
					// vc = 1/(1+ Math.pow(Math.E, -10*v0));
					// vc = Math.abs(Math.tanh(v0));
					// System.out.print(v0 + " ");
					vc = 1 / (1 + Math.pow(Math.E, -pendiente * v0));
					// System.out.println(1/(1+ Math.pow(Math.E, -pendiente*v0)));
				}
				if (Utils.getRandNumber(0.0, 1.0) < vc) {
					if (this.particlesPopulation.getPopulation().get(i).getVariables().get(j) == 0.0) {
						this.particlesPopulation.getPopulation().get(i).modIVariable(j, 1.0);
					} else {
						this.particlesPopulation.getPopulation().get(i).modIVariable(j, 0.0);
					}
				}

				if (pendiente == this.minPendiente && this.params.getC2() > this.params.getC1()) {
					this.v0.get(i).set(j, 0.0);
					this.v1.get(i).set(j, 0.0);
				}

			}
			// System.out.println();
			this.problem.evaluate(this.particlesPopulation.getPopulation().get(i));
		}
	}*/

	/*public void calcularVelocidades2() throws FileNotFoundException, IOException, CsvException {
		// Calacular pendiente de funcione sigmoide
		// Distancia de hamming con todas las partículas
		// Porcentaje de partículas con una distancia < z
		// double pendiente = this.calcularPendienteFuncion();
		// System.out.println("Gbest: " + this.DistanciasGbest());
		// System.out.println("Pbest: " + this.DistanciasPbest());
		for (int i = 0; i < this.params.getnumParticles(); i++) {
			// System.out.println(this.particlesPopulation.getPopulation().get(i) + " " +
			// this.PbestPopulation.getPopulation().get(i));
			double vc;

			double pendiente = this.calcularPendienteFuncion2(i);
			// System.out.print(pendiente + ", ");

			for (int j = 0; j < this.problem.getNumVariables(); j++) {
				double d1_1, d0_1, d1_2, d0_2;
				double v1, v0;
				if (this.PbestPopulation.getPopulation().get(i).getVariables().get(j) == 1.0) {
					d1_1 = this.params.getC1() * this.r1;
					d0_1 = -this.params.getC1() * this.r1;
				} else {
					d1_1 = -this.params.getC1() * this.r1;
					d0_1 = this.params.getC1() * this.r1;
				}
				if (this.Gbest.getVariables().get(j) == 1.0) {
					d1_2 = this.params.getC2() * this.r2;
					d0_2 = -this.params.getC2() * this.r2;
				} else {
					d1_2 = -this.params.getC2() * this.r2;
					d0_2 = this.params.getC2() * this.r2;
				}
				v1 = this.params.getInertiaW().getInertiaW() * this.v1.get(i).get(j) + d1_1 + d1_2;
				v0 = this.params.getInertiaW().getInertiaW() * this.v0.get(i).get(j) + d0_1 + d0_2;
				this.v1.get(i).set(j, v1);
				this.v0.get(i).set(j, v0);

				if (this.particlesPopulation.getPopulation().get(i).getVariables().get(j) == 0.0) {
					// vc = 1/(1+ Math.pow(Math.E, -v1));
					// vc = Math.abs(Math.tanh(v1));
					// System.out.print(v1 + " ");
					vc = 1 / (1 + Math.pow(Math.E, -pendiente * v1));
					// System.out.println(1/(1+ Math.pow(Math.E, -pendiente*v1)));
				} else {
					// vc = 1/(1+ Math.pow(Math.E, -v0));
					// vc = Math.abs(Math.tanh(v0));
					// System.out.print(v0 + " ");
					vc = 1 / (1 + Math.pow(Math.E, -pendiente * v0));
					// System.out.println(1/(1+ Math.pow(Math.E, -pendiente*v0)));
				}
				if (Utils.getRandNumber(0.0, 1.0) < vc) {
					if (this.particlesPopulation.getPopulation().get(i).getVariables().get(j) == 0.0) {
						this.particlesPopulation.getPopulation().get(i).modIVariable(j, 1.0);
					} else {
						this.particlesPopulation.getPopulation().get(i).modIVariable(j, 0.0);
					}
				}
			}
			// System.out.println();
			this.problem.evaluate(this.particlesPopulation.getPopulation().get(i));
		}
	}*/

	public void updatePositionsAndPbestGbest() throws FileNotFoundException, IOException, CsvException {

		Particle Temp_GBest = new Particle(0, 0);
		List<Particle> listaPBests = new ArrayList<>();
//-----------------------Compute slope------------------------------------------------------
		double slope = this.computeSlope();
		
//-----------------------Update velocity for each particle in each dimension----------------
		for (int i = 0; i < this.params.getnumParticles(); i++) {
			double vc;

			for (int j = 0; j < this.problem.getNumVariables(); j++) {
				double d1_1, d0_1, d1_2, d0_2;
				double v1, v0;
				if (this.PbestPopulation.getPopulation().get(i).getVariables().get(j) == 1.0) {
					d1_1 = this.params.getC1() * this.r1;
					d0_1 = -this.params.getC1() * this.r1;
				} else {
					d1_1 = -this.params.getC1() * this.r1;
					d0_1 = this.params.getC1() * this.r1;
				}
				if (this.Gbest.getVariables().get(j) == 1.0) {
					d1_2 = this.params.getC2() * this.r2;
					d0_2 = -this.params.getC2() * this.r2;
				} else {
					d1_2 = -this.params.getC2() * this.r2;
					d0_2 = this.params.getC2() * this.r2;
				}


				v1 = this.params.getInertiaW().getInertiaW() * this.v1.get(i).get(j) + d1_1 + d1_2;
				v0 = this.params.getInertiaW().getInertiaW() * this.v0.get(i).get(j) + d0_1 + d0_2;


				if (v1 > this.maxV) {
					this.v1.get(i).set(j, this.maxV);
				} else if (v1 < this.minV) {
					this.v1.get(i).set(j, this.minV);
				} else {
					this.v1.get(i).set(j, v1);
				}

				if (v0 > this.maxV) {
					this.v0.get(i).set(j, this.maxV);
				} else if (v0 < this.minV) {
					this.v0.get(i).set(j, this.minV);
				} else {
					this.v0.get(i).set(j, v0);
				}

				if (this.particlesPopulation.getPopulation().get(i).getVariables().get(j) == 0.0) {
					vc = 1 / (1 + Math.pow(Math.E, -slope * v1));
				} else {
					vc = 1 / (1 + Math.pow(Math.E, -slope * v0));
				}
				
//---------------------------Update position of particle i on dimension j---------------------
				if (Utils.getRandNumber(0.0, 1.0) < vc) {
					if (this.particlesPopulation.getPopulation().get(i).getVariables().get(j) == 0.0) {
						this.particlesPopulation.getPopulation().get(i).modIVariable(j, 1.0);
					} else {
						this.particlesPopulation.getPopulation().get(i).modIVariable(j, 0.0);
					}
				}

				if (slope == this.minSlope && this.params.getC2() > this.params.getC1()) {
					this.v0.get(i).set(j, 0.0);
					this.v1.get(i).set(j, 0.0);
				}

			}
//-------------------------Compute objective function values and repair/improve solution--------
			this.problem.evaluate(this.particlesPopulation.getPopulation().get(i));
			this.particlesPopulation.getPopulation().set(i, this.problem.repairImprove(this.particlesPopulation.getPopulation().get(i)));
			
//----------------------------------Update PBest---------------------------------------------

			if (this.PbestPopulation.getPopulation().get(i).getObjectives().size() == this.particlesPopulation
					.getPopulation().get(i).getObjectives().size()) {
				if (this.particlesPopulation.getPopulation().get(i).isFeasible()
						&& this.PbestPopulation.getPopulation().get(i).isFeasible()) {
					if (this.problem.getMinOMax().get(this.problem.getNumObjetivos() - 1)) {
						if (this.particlesPopulation.getPopulation().get(i).getObjectives()
								.get(this.problem.getNumObjetivos() - 1) < this.PbestPopulation.getPopulation().get(i)
										.getObjectives().get(this.problem.getNumObjetivos() - 1)) {
							Particle nuevo = Utils.copiarIndividuo(this.particlesPopulation.getPopulation().get(i));
							listaPBests.add(nuevo);
						} else {
							Particle nuevo = Utils.copiarIndividuo(this.PbestPopulation.getPopulation().get(i));
							listaPBests.add(nuevo);
						}
					} else {
						if (this.particlesPopulation.getPopulation().get(i).getObjectives()
								.get(this.problem.getNumObjetivos() - 1) > this.PbestPopulation.getPopulation().get(i)
										.getObjectives().get(this.problem.getNumObjetivos() - 1)) {
							Particle nuevo = Utils.copiarIndividuo(this.particlesPopulation.getPopulation().get(i));
							listaPBests.add(nuevo);
						} else {
							Particle nuevo = Utils.copiarIndividuo(this.PbestPopulation.getPopulation().get(i));
							listaPBests.add(nuevo);
						}
					}

				} else if (!this.particlesPopulation.getPopulation().get(i).isFeasible()
						&& !this.PbestPopulation.getPopulation().get(i).isFeasible()) {
					if (this.particlesPopulation.getPopulation().get(i).getConstraintViolation() < this.PbestPopulation
							.getPopulation().get(i).getConstraintViolation()) {
						Particle nuevo = Utils.copiarIndividuo(this.particlesPopulation.getPopulation().get(i));
						listaPBests.add(nuevo);
					} else {
						Particle nuevo = Utils.copiarIndividuo(this.PbestPopulation.getPopulation().get(i));
						listaPBests.add(nuevo);
					}

				} else if (this.particlesPopulation.getPopulation().get(i).isFeasible()
						&& !this.PbestPopulation.getPopulation().get(i).isFeasible()) {
					Particle nuevo = Utils.copiarIndividuo(this.particlesPopulation.getPopulation().get(i));
					listaPBests.add(nuevo);
				} else {
					Particle nuevo = Utils.copiarIndividuo(this.PbestPopulation.getPopulation().get(i));
					listaPBests.add(nuevo);
				}

			} else {
				Particle nuevo = Utils.copiarIndividuo(this.particlesPopulation.getPopulation().get(i));
				listaPBests.add(nuevo);
			}

//---------------------------------Update Gbest---------------------------------------
			
			if (i == 0) {
				Temp_GBest = Utils.copiarIndividuo(particlesPopulation.getPopulation().get(i));
			} else {
				if (Temp_GBest.isFeasible() && this.particlesPopulation.getPopulation().get(i).isFeasible()) {
					if (this.problem.getMinOMax().get(this.problem.getNumObjetivos() - 1)) {
						if (this.particlesPopulation.getPopulation().get(i).getObjectives()
								.get(this.problem.getNumObjetivos() - 1) < Temp_GBest.getObjectives()
										.get(this.problem.getNumObjetivos() - 1)) {
							Temp_GBest = Utils.copiarIndividuo(particlesPopulation.getPopulation().get(i));
						}
					} else {
						if (this.particlesPopulation.getPopulation().get(i).getObjectives()
								.get(this.problem.getNumObjetivos() - 1) > Temp_GBest.getObjectives()
										.get(this.problem.getNumObjetivos() - 1)) {
							Temp_GBest = Utils.copiarIndividuo(particlesPopulation.getPopulation().get(i));
						}
					}

				} else if (!Temp_GBest.isFeasible() && !this.particlesPopulation.getPopulation().get(i).isFeasible()) {
					if (this.particlesPopulation.getPopulation().get(i).getConstraintViolation() < Temp_GBest
							.getConstraintViolation()) {
						Temp_GBest = Utils.copiarIndividuo(particlesPopulation.getPopulation().get(i));
					}

				} else if (!Temp_GBest.isFeasible() && this.particlesPopulation.getPopulation().get(i).isFeasible()) {
					Temp_GBest = Utils.copiarIndividuo(particlesPopulation.getPopulation().get(i));
				}
			}
		}

		this.PbestPopulation.setPopulation(listaPBests);

		if (this.Gbest != null) {
			if (this.Gbest.isFeasible() && Temp_GBest.isFeasible()) {
				if (this.problem.getMinOMax().get(this.problem.getNumObjetivos() - 1)) {
					if (Temp_GBest.getObjectives().get(this.problem.getNumObjetivos() - 1) < this.Gbest.getObjectives()
							.get(this.problem.getNumObjetivos() - 1)) {
						this.Gbest = Utils.copiarIndividuo(Temp_GBest);
					}
				} else {
					if (Temp_GBest.getObjectives().get(this.problem.getNumObjetivos() - 1) > this.Gbest.getObjectives()
							.get(this.problem.getNumObjetivos() - 1)) {
						this.Gbest = Utils.copiarIndividuo(Temp_GBest);
					}
				}

			} else if (!this.Gbest.isFeasible() && !Temp_GBest.isFeasible()) {
				if (Temp_GBest.getConstraintViolation() < this.Gbest.getConstraintViolation()) {
					this.Gbest = Utils.copiarIndividuo(Temp_GBest);
				}
			}

		} else {
			this.Gbest = Utils.copiarIndividuo(Temp_GBest);
		}

	}

	private void compararFitness() {
		// Al comparar 2 soluciones:
		// 1. Si se comparan 2 soluciones factibles es mejor la que tenga mejor Fitness
		// 2. Si se compara 1 solucion factible con otra infactible es mejor la factible
		// 3. Si se comparan 2 soluciones infactibles es mejor la que tenga menor
		// Contraint Violation
		Particle Temp_GBest = Utils.copiarIndividuo(particlesPopulation.getPopulation().get(0));
		for (int i = 1; i < this.params.getnumParticles(); i++) {
			if (Temp_GBest.isFeasible() && this.particlesPopulation.getPopulation().get(i).isFeasible()) {
				if (this.particlesPopulation.getPopulation().get(i).getObjectives().get(this.problem.getNumObjetivos()
						- 1) < Temp_GBest.getObjectives().get(this.problem.getNumObjetivos() - 1)) {
					Temp_GBest = Utils.copiarIndividuo(particlesPopulation.getPopulation().get(i));
				}
			} else if (!Temp_GBest.isFeasible() && !this.particlesPopulation.getPopulation().get(i).isFeasible()) {
				if (this.particlesPopulation.getPopulation().get(i).getConstraintViolation() < Temp_GBest
						.getConstraintViolation()) {
					Temp_GBest = Utils.copiarIndividuo(particlesPopulation.getPopulation().get(i));
				}
			} else if (!Temp_GBest.isFeasible() && this.particlesPopulation.getPopulation().get(i).isFeasible()) {
				Temp_GBest = Utils.copiarIndividuo(particlesPopulation.getPopulation().get(i));
			}

		}

		if (this.Gbest != null) {
			if (this.Gbest.isFeasible() && Temp_GBest.isFeasible()) {
				if (Temp_GBest.getObjectives().get(this.problem.getNumObjetivos() - 1) < this.Gbest.getObjectives()
						.get(this.problem.getNumObjetivos() - 1)) {
					this.Gbest = Utils.copiarIndividuo(Temp_GBest);
				}
			} else if (!this.Gbest.isFeasible() && !Temp_GBest.isFeasible()) {
				if (Temp_GBest.getConstraintViolation() < this.Gbest.getConstraintViolation()) {
					this.Gbest = Utils.copiarIndividuo(Temp_GBest);
				}
			}

		} else {
			this.Gbest = Utils.copiarIndividuo(Temp_GBest);
		}
	}

	private void calcularPbestsYGbest() {
		// Al comparar 2 soluciones:
		// 1. Si se comparan 2 soluciones factibles es mejor la que tenga mejor Fitness
		// 2. Si se compara 1 solucion factible con otra infactible es mejor la factible
		// 3. Si se comparan 2 soluciones infactibles es mejor la que tenga menor
		// Contraint Violation
		List<Particle> listaPBests = new ArrayList<>();

		Particle Temp_GBest = Utils.copiarIndividuo(particlesPopulation.getPopulation().get(0));

		for (int i = 0; i < this.params.getnumParticles(); i++) {

			// Cálculo de los Pbest
			if (this.PbestPopulation.getPopulation().get(i).getObjectives().size() == this.particlesPopulation
					.getPopulation().get(i).getObjectives().size()) {
				if (this.particlesPopulation.getPopulation().get(i).isFeasible()
						&& this.PbestPopulation.getPopulation().get(i).isFeasible()) {
					if (this.particlesPopulation.getPopulation().get(i).getObjectives()
							.get(this.problem.getNumObjetivos() - 1) < this.PbestPopulation.getPopulation().get(i)
									.getObjectives().get(this.problem.getNumObjetivos() - 1)) {
						Particle nuevo = Utils.copiarIndividuo(this.particlesPopulation.getPopulation().get(i));
						listaPBests.add(nuevo);
					} else {
						Particle nuevo = Utils.copiarIndividuo(this.PbestPopulation.getPopulation().get(i));
						listaPBests.add(nuevo);
					}
				} else if (!this.particlesPopulation.getPopulation().get(i).isFeasible()
						&& !this.PbestPopulation.getPopulation().get(i).isFeasible()) {
					if (this.particlesPopulation.getPopulation().get(i).getConstraintViolation() < this.PbestPopulation
							.getPopulation().get(i).getConstraintViolation()) {
						Particle nuevo = Utils.copiarIndividuo(this.particlesPopulation.getPopulation().get(i));
						listaPBests.add(nuevo);
					} else {
						Particle nuevo = Utils.copiarIndividuo(this.PbestPopulation.getPopulation().get(i));
						listaPBests.add(nuevo);
					}
				} else if (this.particlesPopulation.getPopulation().get(i).isFeasible()
						&& !this.PbestPopulation.getPopulation().get(i).isFeasible()) {
					Particle nuevo = Utils.copiarIndividuo(this.particlesPopulation.getPopulation().get(i));
					listaPBests.add(nuevo);
				} else {
					Particle nuevo = Utils.copiarIndividuo(this.PbestPopulation.getPopulation().get(i));
					listaPBests.add(nuevo);
				}

			} else {
				Particle nuevo = Utils.copiarIndividuo(this.particlesPopulation.getPopulation().get(i));
				listaPBests.add(nuevo);
			}

			if (i >= 1) {
				// Cálculo del Gbest
				if (Temp_GBest.isFeasible() && this.particlesPopulation.getPopulation().get(i).isFeasible()) {
					if (this.particlesPopulation.getPopulation().get(i).getObjectives()
							.get(this.problem.getNumObjetivos() - 1) < Temp_GBest.getObjectives()
									.get(this.problem.getNumObjetivos() - 1)) {
						Temp_GBest = Utils.copiarIndividuo(particlesPopulation.getPopulation().get(i));
					}
				} else if (!Temp_GBest.isFeasible() && !this.particlesPopulation.getPopulation().get(i).isFeasible()) {
					if (this.particlesPopulation.getPopulation().get(i).getConstraintViolation() < Temp_GBest
							.getConstraintViolation()) {
						Temp_GBest = Utils.copiarIndividuo(particlesPopulation.getPopulation().get(i));
					}
				} else if (!Temp_GBest.isFeasible() && this.particlesPopulation.getPopulation().get(i).isFeasible()) {
					Temp_GBest = Utils.copiarIndividuo(particlesPopulation.getPopulation().get(i));
				}
			}

		}

		this.PbestPopulation.setPopulation(listaPBests);

		if (this.Gbest != null) {
			if (this.Gbest.isFeasible() && Temp_GBest.isFeasible()) {
				if (Temp_GBest.getObjectives().get(this.problem.getNumObjetivos() - 1) < this.Gbest.getObjectives()
						.get(this.problem.getNumObjetivos() - 1)) {
					this.Gbest = Utils.copiarIndividuo(Temp_GBest);
				}
			} else if (!this.Gbest.isFeasible() && !Temp_GBest.isFeasible()) {
				if (Temp_GBest.getConstraintViolation() < this.Gbest.getConstraintViolation()) {
					this.Gbest = Utils.copiarIndividuo(Temp_GBest);
				}
			}

		} else {
			this.Gbest = Utils.copiarIndividuo(Temp_GBest);
		}
	}

	/*private void calcularAux() {
		if (this.listaAux.size() == this.params.getnumParticles()) {
			for (int i = 0; i < this.params.getnumParticles(); i++) {
				if (Utils.distanciaHamming(this.listaAux.get(i).getVariables(),
						this.particlesPopulation.getPopulation().get(i).getVariables()) < 1) {
					int timerAntes = this.timers.get(i);
					if (timerAntes > 0) {
						timerAntes--;
						this.timers.set(i, timerAntes);
					}
				} else {
					Individuo nuevo = Utils.copiarIndividuo(this.particlesPopulation.getPopulation().get(i));
					this.listaAux.set(i, nuevo);
					this.timers.set(i, this.maxIterAux);
				}
			}
		} else {
			for (Individuo i : this.particlesPopulation.getPopulation()) {
				Individuo nuevo = Utils.copiarIndividuo(i);
				this.timers.add(this.maxIterAux);
				this.listaAux.add(nuevo);
			}
		}
	}*/

	/*private void calcularAux2() {
		if (this.listaAux.size() == this.params.getnumParticles()) {
			for (int i = 0; i < this.params.getnumParticles(); i++) {
				for (int j = 0; j < this.particlesPopulation.getPopulation().get(i).getVariables().size(); j++) {
					double a = this.listaAux.get(i).getVariables().get(j);
					double b = this.particlesPopulation.getPopulation().get(i).getVariables().get(j);
					if (a == b) {
						int timerAntes = this.timers2.get(i).get(j);
						if (timerAntes > 0) {
							timerAntes--;
							this.timers2.get(i).set(j, timerAntes);
						}

					} else {
						this.timers2.get(i).set(j, this.maxIterAux);
					}
				}
				if (Utils.distanciaHamming(this.listaAux.get(i).getVariables(),
						this.particlesPopulation.getPopulation().get(i).getVariables()) >= 1) {
					Individuo nuevo = Utils.copiarIndividuo(this.particlesPopulation.getPopulation().get(i));
					this.listaAux.set(i, nuevo);
				}
				
				 * if(Utils.distanciaHamming(this.listaAux.get(i).getVariables(),
				 * this.particlesPopulation.getPopulation().get(i).getVariables()) < 1) { int
				 * timerAntes = this.timers.get(i); if(timerAntes > 0) { timerAntes--;
				 * this.timers.set(i, timerAntes); } }else { Individuo nuevo =
				 * Utils.copiarIndividuo(this.particlesPopulation.getPopulation().get(i));
				 * this.listaAux.set(i, nuevo); this.timers.set(i, this.maxIterAux); }
				 
			}
		} else {
			for (Individuo i : this.particlesPopulation.getPopulation()) {
				Individuo nuevo = Utils.copiarIndividuo(i);
				// this.timers.add(this.maxIterAux);
				List<Integer> timersBitsI = new ArrayList<>();
				for (int bit = 0; bit < i.getVariables().size(); bit++) {
					timersBitsI.add(this.maxIterAux);
				}
				this.timers2.add(timersBitsI);
				this.listaAux.add(nuevo);
			}
		}
	}*/

	private void initialVelocities() {
		Particle Temp_GBest = new Particle(0, 0);
		List<Particle> listaPBests = new ArrayList<>();
		//List<Individuo> listaLBests = new ArrayList<>();

		for (int i = 0; i < this.params.getnumParticles(); i++) {
			List<Double> v0_i = new ArrayList<>();
			List<Double> v1_i = new ArrayList<>();
			//List<Double> v0_iL = new ArrayList<>();
			//List<Double> v1_iL = new ArrayList<>();
			for (int j = 0; j < this.problem.getNumVariables(); j++) {
				v0_i.add(0.0);
				v1_i.add(0.0);
				//v0_iL.add(0.0);
				//v1_iL.add(0.0);
			}

			this.v0.add(v0_i);
			this.v1.add(v1_i);
			//this.v0L.add(v0_iL);
			//this.v1L.add(v1_iL);

			if (this.PbestPopulation.getPopulation().get(i).getObjectives().size() == this.particlesPopulation
					.getPopulation().get(i).getObjectives().size()) {
				if (this.particlesPopulation.getPopulation().get(i).isFeasible()
						&& this.PbestPopulation.getPopulation().get(i).isFeasible()) {
					if (this.problem.getMinOMax().get(this.problem.getNumObjetivos() - 1)) {
						if (this.particlesPopulation.getPopulation().get(i).getObjectives()
								.get(this.problem.getNumObjetivos() - 1) < this.PbestPopulation.getPopulation().get(i)
										.getObjectives().get(this.problem.getNumObjetivos() - 1)) {
							Particle nuevo = Utils.copiarIndividuo(this.particlesPopulation.getPopulation().get(i));
							listaPBests.add(nuevo);
						} else {
							Particle nuevo = Utils.copiarIndividuo(this.PbestPopulation.getPopulation().get(i));
							listaPBests.add(nuevo);
						}
					} else {
						if (this.particlesPopulation.getPopulation().get(i).getObjectives()
								.get(this.problem.getNumObjetivos() - 1) > this.PbestPopulation.getPopulation().get(i)
										.getObjectives().get(this.problem.getNumObjetivos() - 1)) {
							Particle nuevo = Utils.copiarIndividuo(this.particlesPopulation.getPopulation().get(i));
							listaPBests.add(nuevo);
						} else {
							Particle nuevo = Utils.copiarIndividuo(this.PbestPopulation.getPopulation().get(i));
							listaPBests.add(nuevo);
						}
					}
				} else if (!this.particlesPopulation.getPopulation().get(i).isFeasible()
						&& !this.PbestPopulation.getPopulation().get(i).isFeasible()) {
					if (this.particlesPopulation.getPopulation().get(i).getConstraintViolation() < this.PbestPopulation
							.getPopulation().get(i).getConstraintViolation()) {
						Particle nuevo = Utils.copiarIndividuo(this.particlesPopulation.getPopulation().get(i));
						listaPBests.add(nuevo);
					} else {
						Particle nuevo = Utils.copiarIndividuo(this.PbestPopulation.getPopulation().get(i));
						listaPBests.add(nuevo);
					}
				} else if (this.particlesPopulation.getPopulation().get(i).isFeasible()
						&& !this.PbestPopulation.getPopulation().get(i).isFeasible()) {
					Particle nuevo = Utils.copiarIndividuo(this.particlesPopulation.getPopulation().get(i));
					listaPBests.add(nuevo);
				} else {
					Particle nuevo = Utils.copiarIndividuo(this.PbestPopulation.getPopulation().get(i));
					listaPBests.add(nuevo);
				}

			} else {
				Particle nuevo = Utils.copiarIndividuo(this.particlesPopulation.getPopulation().get(i));
				listaPBests.add(nuevo);
			}

			//listaLBests.add(obtenerLbest(this.particlesPopulation.getPopulation().get(i)));

			if (i == 0) {
				Temp_GBest = Utils.copiarIndividuo(particlesPopulation.getPopulation().get(i));
			} else {
				if (Temp_GBest.isFeasible() && this.particlesPopulation.getPopulation().get(i).isFeasible()) {
					if (this.problem.getMinOMax().get(this.problem.getNumObjetivos() - 1)) {
						if (this.particlesPopulation.getPopulation().get(i).getObjectives()
								.get(this.problem.getNumObjetivos() - 1) < Temp_GBest.getObjectives()
										.get(this.problem.getNumObjetivos() - 1)) {
							Temp_GBest = Utils.copiarIndividuo(particlesPopulation.getPopulation().get(i));
						}
					} else {
						if (this.particlesPopulation.getPopulation().get(i).getObjectives()
								.get(this.problem.getNumObjetivos() - 1) > Temp_GBest.getObjectives()
										.get(this.problem.getNumObjetivos() - 1)) {
							Temp_GBest = Utils.copiarIndividuo(particlesPopulation.getPopulation().get(i));
						}
					}
				} else if (!Temp_GBest.isFeasible() && !this.particlesPopulation.getPopulation().get(i).isFeasible()) {
					if (this.particlesPopulation.getPopulation().get(i).getConstraintViolation() < Temp_GBest
							.getConstraintViolation()) {
						Temp_GBest = Utils.copiarIndividuo(particlesPopulation.getPopulation().get(i));
					}
				} else if (!Temp_GBest.isFeasible() && this.particlesPopulation.getPopulation().get(i).isFeasible()) {
					Temp_GBest = Utils.copiarIndividuo(particlesPopulation.getPopulation().get(i));
				}
			}

		}

		this.PbestPopulation.setPopulation(listaPBests);
		//this.poblacionLbest.setPoblacion(listaLBests);

		if (this.Gbest != null) {
			if (this.Gbest.isFeasible() && Temp_GBest.isFeasible()) {
				if (Temp_GBest.getObjectives().get(this.problem.getNumObjetivos() - 1) < this.Gbest.getObjectives()
						.get(this.problem.getNumObjetivos() - 1)) {
					this.Gbest = Utils.copiarIndividuo(Temp_GBest);
				}
			} else if (!this.Gbest.isFeasible() && !Temp_GBest.isFeasible()) {
				if (Temp_GBest.getConstraintViolation() < this.Gbest.getConstraintViolation()) {
					this.Gbest = Utils.copiarIndividuo(Temp_GBest);
				}
			}

		} else {
			this.Gbest = Utils.copiarIndividuo(Temp_GBest);
		}

	}

	/*private Double calcularPendienteFuncion() {
		
		 * List<Integer> distanciaParticulasGbest = new ArrayList<>(); for(int i = 0; i
		 * < this.particlesPopulation.getPopulation().size(); i++) { int distH =
		 * Utils.distanciaHamming(this.particlesPopulation.getPopulation().get(i).
		 * getVariables(), this.Gbest.getVariables());
		 * distanciaParticulasGbest.add(distH); }
		 * System.out.println("Distancia con Gbest: " + distanciaParticulasGbest);
		 * 
		 * List<Integer> distanciaParticulasPbest = new ArrayList<>();
		 * 
		 * for(int i = 0; i < this.particlesPopulation.getPopulation().size(); i++) { int
		 * distH =
		 * Utils.distanciaHamming(this.particlesPopulation.getPopulation().get(i).
		 * getVariables(), this.PbestPopulation.getPopulation().get(i).getVariables());
		 * distanciaParticulasPbest.add(distH); }
		 * System.out.println("Distancia con Pbest: " + distanciaParticulasPbest);
		 

		double sumaTimers0 = 0;
		for (int i = 0; i < this.timers.size(); i++) {
			if (this.timers.get(i) == 0) {
				sumaTimers0++;
			}
		}

		if (sumaTimers0 != 0) {
			System.out.println(sumaTimers0);
		}

		// System.out.println(this.timers);
		double distMaxMinPendientes = this.maxPendiente - this.minPendiente;

		return this.minPendiente
				+ distMaxMinPendientes * (1 - (sumaTimers0 / this.particlesPopulation.getnumParticles()));
	}*/

	/*private Double calcularPendienteFuncion2(int id) {
		double sumaTimers0 = 0;
		// System.out.println(this.timers2.get(id));
		for (int j = 0; j < this.timers2.get(id).size(); j++) {
			if (this.timers2.get(id).get(j) == 0) {
				sumaTimers0++;
			}
		}

		// System.out.println(this.timers);
		double distMaxMinPendientes = this.maxPendiente - this.minPendiente;

		return this.minPendiente + distMaxMinPendientes
				* (1 - (sumaTimers0 / this.particlesPopulation.getPopulation().get(id).getVariables().size()));
	}*/

	private Double computeSlope() {

		double sumDistH = 0;

		for (int i = 0; i < this.particlesPopulation.getPopulation().size(); i++) {
			if (Utils.hammingDistance(this.Gbest.getVariables(),
					this.particlesPopulation.getPopulation().get(i).getVariables()) < this.maxDistH) {
				sumDistH++;
			}
		}
		if (sumDistH != 0.0) {
			System.out.println("Particles in Global Best: " + sumDistH);
		}
		
		double distMaxMinSlopes = this.maxSlope - this.minSlope;

		return this.minSlope
				+ distMaxMinSlopes * (1.0 - (sumDistH / (this.particlesPopulation.getnumParticles() * 1.0)));
		
	}

	/*private Individuo obtenerLbest(Individuo ind) {
		Individuo Lbest = Utils.copiarIndividuo(ind);

		for (Individuo i : this.particlesPopulation.getPopulation()) {
			if (i.getObjectives().get(0) < Lbest.getObjectives().get(0)
					&& Utils.distanciaHamming(i.getVariables(), Lbest.getVariables()) < this.neighborhood) {
				Lbest = Utils.copiarIndividuo(i);
			}
		}
		return Lbest;
	}*/

	/*private void updateU(int maxIter, int currIter) {
		if (currIter > maxIter) {
			this.u = 1.0;
		} else {
			this.u = 0.0 + ((1.0 - 0.0) / maxIter) * currIter;
		}
		// System.out.println(this.u);
	}*/

	/*private void updateU2(int maxIter, int currIter) {
		this.u2 = 1.0 - (0.0 + ((1.0 - 0.0) / maxIter) * currIter);
		// System.out.println(this.u);
	}*/

	public List<Integer> DistanciasGbest() {
		List<Integer> distanciaParticulasGbest = new ArrayList<>();
		for (int i = 0; i < this.particlesPopulation.getPopulation().size(); i++) {
			int distH = Utils.hammingDistance(this.particlesPopulation.getPopulation().get(i).getVariables(),
					this.Gbest.getVariables());
			distanciaParticulasGbest.add(distH);
		}
		return distanciaParticulasGbest;
	}

	public List<Integer> DistanciasPbest() {
		List<Integer> distanciaParticulasPbest = new ArrayList<>();

		for (int i = 0; i < this.particlesPopulation.getPopulation().size(); i++) {
			int distH = Utils.hammingDistance(this.particlesPopulation.getPopulation().get(i).getVariables(),
					this.PbestPopulation.getPopulation().get(i).getVariables());
			distanciaParticulasPbest.add(distH);
		}
		return distanciaParticulasPbest;
	}

	/*
	 * public List<Integer> DistanciasLbest() { List<Integer>
	 * distanciaParticulasLbest = new ArrayList<>();
	 * 
	 * for (int i = 0; i < this.particlesPopulation.getPopulation().size(); i++) {
	 * int distH =
	 * Utils.distanciaHamming(this.particlesPopulation.getPopulation().get(i).
	 * getVariables(), this.poblacionLbest.getPopulation().get(i).getVariables());
	 * distanciaParticulasLbest.add(distH); } return distanciaParticulasLbest; }
	 */

	public BPSOParams getParams() {
		return params;
	}

	public void setParams(BPSOParams params) {
		this.params = params;
	}

	public Problem getproblem() {
		return problem;
	}

	public void setproblem(Problem problem) {
		this.problem = problem;
	}

	public Population getparticlesPopulation() {
		return particlesPopulation;
	}

	public void setparticlesPopulation(Population particlesPopulation) {
		this.particlesPopulation = particlesPopulation;
	}

	public Population getPbestPopulation() {
		return PbestPopulation;
	}

	public void setPbestPopulation(Population PbestPopulation) {
		this.PbestPopulation = PbestPopulation;
	}

	public Particle getGbest() {
		return Gbest;
	}

	public void setGbest(Particle gbest) {
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

	/*
	 * public List<List<Double>> getV0L() { return v0L; }
	 * 
	 * public void setV0L(List<List<Double>> v0l) { v0L = v0l; }
	 * 
	 * public List<List<Double>> getV1L() { return v1L; }
	 * 
	 * public void setV1L(List<List<Double>> v1l) { v1L = v1l; }
	 */

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
