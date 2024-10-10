package com.VisNeo4j.App.Problemas;

import java.util.ArrayList;
import java.util.List;

import com.VisNeo4j.App.Modelo.Individuo;
import com.VisNeo4j.App.Utils.Utils;

public class Sphere extends Problema{
	
	private int bitsEnteros = 6;
	private int bitsDecimales = 6;
	private int bitsPorVariable = 1 + this.bitsEnteros + this.bitsDecimales;

	public Sphere(int numVariables) {
		super(0, 1);
		super.setNumVariables(numVariables * this.bitsPorVariable);
		
		// TODO Auto-generated constructor stub
	}

	@Override
	 public Individuo evaluate(Individuo solution) {
		//Convertir las variables a decimal y aplicar la fórmula
		List<Double> variablesDecimales = Utils.decodificarBinario(this.bitsEnteros, 
				this.bitsDecimales, solution.getVariables());
		
		Double objetivo = 0.0;
		List<Double> objetivos = new ArrayList<>();
		
		for(int i = 0; i < variablesDecimales.size(); i++) {
			objetivo += variablesDecimales.get(i) * variablesDecimales.get(i);
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
		}
		ind.setVariables(valores);
		return ind;
	}
}
