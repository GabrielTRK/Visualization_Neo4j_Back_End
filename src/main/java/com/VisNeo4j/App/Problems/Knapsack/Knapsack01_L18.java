package com.VisNeo4j.App.Problems.Knapsack;

import java.util.ArrayList;
import java.util.List;

import com.VisNeo4j.App.Constantes.ConstantesKP01_L18PA;
import com.VisNeo4j.App.Constantes.ConstantesKP01_L18PB;
import com.VisNeo4j.App.Constantes.ConstantesKP01_L18WA;
import com.VisNeo4j.App.Constantes.ConstantesKP01_L18WB;
import com.VisNeo4j.App.Utils.Utils;

public class Knapsack01_L18 extends Knapsack01{

	double capacity = 105196160.0;
	private List<Integer> weights = new ArrayList<>();
	private List<Integer> profits = new ArrayList<>();
	
	public Knapsack01_L18() {
		super();
		this.weights.addAll(ConstantesKP01_L18WA.KP01_L18WA);
		this.weights.addAll(ConstantesKP01_L18WB.KP01_L18WB);
		this.profits.addAll(ConstantesKP01_L18PA.KP01_L18PA);
		this.profits.addAll(ConstantesKP01_L18PB.KP01_L18PB);
		super.setWeights(Utils.convertToDouble(weights));
		super.setProfits(Utils.convertToDouble(profits));
		super.setCapacity(capacity);
		int var = this.weights.size();
		super.setNumVariables(var);
	}
}
