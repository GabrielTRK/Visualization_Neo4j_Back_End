package com.VisNeo4j.App.Problemas;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.VisNeo4j.App.Modelo.Individuo;

public class Knapsack01 extends Problema {

	private int numInicializaciones = 0;

	double capacity = 200.0;
	private List<Integer> weights = Stream.of(95, 4, 60, 32, 23, 72, 80, 62, 65, 46).collect(Collectors.toList());
	private List<Integer> profits = Stream.of(55, 10, 47, 5, 4, 50, 8, 61, 85, 87).collect(Collectors.toList());

	public Knapsack01() {
		super(0, 1);
		int var = this.weights.size();
		super.setNumVariables(var);
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
		}
		solution.setObjetivos(Stream.of(sumaP).collect(Collectors.toList()));
		solution.setRestricciones(Stream.of(sumaW).collect(Collectors.toList()));
		if (sumaW > this.capacity) {
			solution.setFactible(false);
			solution.setConstraintViolation(Math.abs(sumaW - this.capacity));
		}else {
			solution.setFactible(true);
			solution.setConstraintViolation(0.0);
		}

		return solution;
	}

	@Override
	public Individuo inicializarValores(Individuo ind) {
		List<Double> valores = new ArrayList<>(super.getNumVariables());
		for (int i = 0; i < super.getNumVariables(); i++) {

			/*if (this.numInicializaciones == 0) {
				valores.add(i, 0.0);
			} else if (this.numInicializaciones == 1) {
				valores.add(i, 1.0);
			} else {*/

				if (i == this.numInicializaciones) {
					valores.add(i, 1.0);
				} else {
					valores.add(i, 0.0);
				}
			//}

			/*
			 * if (this.numInicializaciones < super.getNumVariables()) { if (i ==
			 * this.numInicializaciones) { valores.add(i, 1.0); } else { valores.add(i,
			 * 0.0); } } else if (this.numInicializaciones == super.getNumVariables()) {
			 * valores.add(i, 0.0); } else if (this.numInicializaciones ==
			 * super.getNumVariables() + 1) { valores.add(i, 1.0); }
			 */

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

	public List<Integer> getWeights() {
		return weights;
	}

	public void setWeights(List<Integer> weights) {
		this.weights = weights;
	}

	public List<Integer> getProfits() {
		return profits;
	}

	public void setProfits(List<Integer> profits) {
		this.profits = profits;
	}

}
