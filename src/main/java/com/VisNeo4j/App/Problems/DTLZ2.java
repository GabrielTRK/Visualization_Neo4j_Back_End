package com.VisNeo4j.App.Problems;

import java.util.ArrayList;
import java.util.List;

import com.VisNeo4j.App.Constantes.Constantes;
import com.VisNeo4j.App.Model.Particle;
import com.VisNeo4j.App.Utils.Utils;

public class DTLZ2 extends Problem{

	//Inicializar parámetros utilizando la superclase
	
	public DTLZ2(Integer numberOfVariables, Integer numberOfObjectives){
		super(numberOfVariables, numberOfObjectives);

		List<Double> lowerLimit = new ArrayList<Double>(super.getNumVariables());
		List<Double> upperLimit = new ArrayList<Double>(super.getNumVariables());

		for (int i = 0; i < super.getNumVariables(); i++) {
			lowerLimit.add(i, 0.0);
			upperLimit.add(i, Math.nextUp(1.0));
		}
		super.setLimites(lowerLimit, upperLimit);
		super.setNombre(Constantes.nombreDTLZ2);
	  }

	//Calcular valores de funcion objetivo
	@Override
	public Particle evaluate(Particle solution) {
		int numberOfVariables = super.getNumVariables();
		int numberOfObjectives = super.getNumObjetivos();
		
		
		double[] f = new double[numberOfObjectives];
		double[] x = new double[numberOfVariables] ;

		for (int i = 0; i < numberOfVariables; i++) {
			x[i] = solution.getVariables().get(i);
		}

		int k = numberOfVariables - numberOfObjectives + 1;

		double g = 0.0;
		for (int i = numberOfVariables - k; i < numberOfVariables; i++) {
			g += (x[i] - 0.5) * (x[i] - 0.5);
		}

		for (int i = 0; i < numberOfObjectives; i++) {
			f[i] = 1.0 + g;
		}

		for (int i = 0; i < numberOfObjectives; i++) {
			for (int j = 0; j < numberOfObjectives - (i + 1); j++) {
				f[i] *= Math.cos(x[j] * 0.5 * Math.PI);
			}
			if (i != 0) {
				int aux = numberOfObjectives - (i + 1);
				f[i] *= Math.sin(x[aux] * 0.5 * Math.PI);
			}
		}

		solution.setObjectives(Utils.ArraytoArrayList(f));

		return solution ;
	 }
	
	//Inicializar de forma aleatoria los valores de las variables según los límites
	@Override
	 public Particle initializeValues(Particle ind) {
		 List<Double> valores = new ArrayList<>(super.getNumVariables());
		 for(int i = 0; i < super.getNumVariables(); i++) {
			 valores.add(i, Utils.getRandNumber(super.getLimitesInferiores().get(i),
					 super.getLimitesSuperiores().get(i)));
		 }
		 ind.setVariables(valores);
		 return ind;
	 }
	
}
