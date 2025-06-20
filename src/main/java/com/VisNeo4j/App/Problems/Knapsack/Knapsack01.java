package com.VisNeo4j.App.Problems.Knapsack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.VisNeo4j.App.Modelo.Individuo;
import com.VisNeo4j.App.Problems.Problem;
import com.VisNeo4j.App.Utils.Utils;

public class Knapsack01 extends Problem {

	private int numInicializaciones = 0;

	double capacity = 200.0;
	private List<Double> weights = Stream.of(95.0, 4.0, 60.0, 32.0, 23.0, 72.0, 80.0, 62.0, 65.0, 46.0)
			.collect(Collectors.toList());
	private List<Double> profits = Stream.of(55.0, 10.0, 47.0, 5.0, 4.0, 50.0, 8.0, 61.0, 85.0, 87.0)
			.collect(Collectors.toList());
	private Double SRate = 0.5;

	public Knapsack01() {
		super(0, 1);
		int var = this.weights.size();
		super.setNumVariables(var);
		//super.setMinOMax(Stream.of(false).collect(Collectors.toList()));
		super.setMinOMax(Stream.of(false).collect(Collectors.toList()));
	}

	@Override
	public Individuo evaluate(Individuo solution) {
		double sumaW = 0.0;
		double sumaP = 0.0;
		for (int i = 0; i < super.getNumVariables(); i++) {
			if (solution.getVariables().get(i) == 1.0) {
				sumaW += this.weights.get(i);
				sumaP += this.profits.get(i);
			}
			//sumaP += this.profits.get(i) * (1.0 - solution.getVariables().get(i));
		}
		solution.setObjetivos(Stream.of(sumaP).collect(Collectors.toList()));
		solution.setRestricciones(Stream.of(sumaW).collect(Collectors.toList()));
		if (sumaW > this.capacity) {
			solution.setFactible(false);
			solution.setConstraintViolation(Math.abs(sumaW - this.capacity));
		} else {
			solution.setFactible(true);
			solution.setConstraintViolation(0.0);
		}
	
		return solution;
	}
	
	@Override
	public Individuo repararMejorar(Individuo solution) {
		if(solution.getRestricciones().get(0) > this.capacity) {
			while (solution.getRestricciones().get(0) > this.capacity) {
				if(Utils.getRandNumber(0.0, 1.0) >= this.SRate) {
					Double minRatio = Double.MAX_VALUE;
					int minRatioPos = 0;
					for(int i = 0; i < this.getNumVariables(); i++) {
						if(solution.getVariables().get(i) == 1.0 && this.profits.get(i)/this.weights.get(i) < minRatio) {
							minRatio = this.profits.get(i)/this.weights.get(i);
							minRatioPos = i;
						}
					}
					solution.modIVariable(minRatioPos, 0.0);
				}else {
					int minRatioPos = Utils.getRandNumber(0, getNumVariables());
					solution.modIVariable(minRatioPos, 0.0);
				}
				this.evaluate(solution);
			}
		}if(solution.getRestricciones().get(0) < this.capacity){
			boolean terminate = false;
			while (!terminate) {
				Double dif = this.capacity - solution.getRestricciones().get(0);
				
				List<Double> candidatos = new ArrayList<>();
				Map<Double, Integer> candidatosRatio = new HashMap<>();
				
				for(int i = 0; i < this.getNumVariables(); i++) {
					if(solution.getVariables().get(i) == 0.0 && this.weights.get(i) <= dif) {
						candidatos.add(this.profits.get(i)/this.weights.get(i));
						candidatosRatio.put(this.profits.get(i)/this.weights.get(i), i);
					}
				}
				if(candidatos.size() != 0) {
					Collections.sort(candidatos, Collections.reverseOrder());
					
					if(Utils.getRandNumber(0.0, 1.0) >= this.SRate) {
						int pos = candidatosRatio.get(candidatos.get(0));
						solution.modIVariable(pos, 1.0);
					}else {
						int pos = candidatosRatio.get(candidatos.get(Utils.getRandNumber(0, candidatos.size())));
						solution.modIVariable(pos, 1.0);
					}
					this.evaluate(solution);
				}else {
					terminate = true;
				}
				
			}
		}
		
		
		return solution;
	}
	
	

	@Override
	public Individuo inicializarValores(Individuo ind) {
		List<Double> valores = new ArrayList<>(super.getNumVariables());
		for (int i = 0; i < super.getNumVariables(); i++) {

			if (this.numInicializaciones == 0) {
				valores.add(i, 0.0);
			} else if (this.numInicializaciones == 1) {
				valores.add(i, 1.0);
			} else {
				if (this.numInicializaciones - 2 <= super.getNumVariables()) {
					if (i == this.numInicializaciones - 2) {
						valores.add(i, 1.0);
					}else {
						valores.add(i, 0.0);
					}
				} else {
					valores.add(i, Utils.getRandBinNumber());
				}
			}

			// valores.add(i, 0.0);

		}
		this.numInicializaciones++;
		ind.setVariables(valores);
		return ind;
	}

	public double getCapacity() {
		return capacity;
	}

	public void setCapacity(double capacity) {
		this.capacity = capacity;
	}

	public List<Double> getWeights() {
		return weights;
	}

	public void setWeights(List<Double> weights) {
		this.weights = weights;
	}

	public List<Double> getProfits() {
		return profits;
	}

	public void setProfits(List<Double> profits) {
		this.profits = profits;
	}

}
