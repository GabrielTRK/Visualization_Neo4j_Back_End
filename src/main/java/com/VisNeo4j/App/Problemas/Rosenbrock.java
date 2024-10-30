package com.VisNeo4j.App.Problemas;

import java.util.ArrayList;
import java.util.List;

import com.VisNeo4j.App.Modelo.Individuo;
import com.VisNeo4j.App.Utils.Utils;

public class Rosenbrock extends Problema{
	
	private int bitsEnteros = 19;
	private int bitsDecimales = 0;
	private int bitsPorVariable = 1 + this.bitsEnteros + this.bitsDecimales;
	private int numInicializaciones = 0;
	
	public Rosenbrock(int numVariables) {
		super(0, 1);
		super.setNumVariables(numVariables * this.bitsPorVariable);
		
	}
	
	@Override
	 public Individuo evaluate(Individuo solution) {
		//Convertir las variables a decimal y aplicar la fórmula
		List<Double> variablesDecimales = Utils.decodificarBinario(this.bitsEnteros, 
				this.bitsDecimales, solution.getVariables());
		//System.out.println(variablesDecimales);
		Double objetivo = 0.0;
		List<Double> objetivos = new ArrayList<>();
		
		for(int i = 0; i < variablesDecimales.size()-1; i++) {
			double temp1 = variablesDecimales.get(i+1) - (variablesDecimales.get(i) * variablesDecimales.get(i));
		    double temp2 = variablesDecimales.get(i) - 1.0;
		    objetivo += (100.0 * temp1 * temp1) + (temp2 * temp2);
			
		}
		
		objetivos.add(objetivo);
		
		solution.setObjetivos(objetivos);
		
		return solution;
	}
	
	@Override
	 public Individuo inicializarValores(Individuo ind) {
		List<Double> valores = new ArrayList<>(super.getNumVariables());
		for(int i = 0; i < super.getNumVariables(); i++) {
			//valores.add(Utils.getRandBinNumber());
			valores.add(1.0);
			/*if (i == this.numInicializaciones) {
				valores.add(i, 1.0);
			} else {
				valores.add(i, 0.0);
			}*/
		}
		//this.numInicializaciones++;
		ind.setVariables(valores);
		return ind;
	}

}
