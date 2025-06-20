package com.VisNeo4j.App.Problems.Knapsack;

import java.util.ArrayList;
import java.util.List;

import com.VisNeo4j.App.Constantes.ConstantesKP01_L19PA;
import com.VisNeo4j.App.Constantes.ConstantesKP01_L19PB;
import com.VisNeo4j.App.Constantes.ConstantesKP01_L19WA;
import com.VisNeo4j.App.Constantes.ConstantesKP01_L19WB;
import com.VisNeo4j.App.Utils.Utils;

public class Knapsack01_L19 extends Knapsack01{

	double capacity = 135889330.0;
	private List<Integer> weights = new ArrayList<>();
	private List<Integer> profits = new ArrayList<>();
	
	public Knapsack01_L19() {
		super();
		this.weights.addAll(ConstantesKP01_L19WA.KP01_L19WA);
		this.weights.addAll(ConstantesKP01_L19WB.KP01_L19WB);
		this.profits.addAll(ConstantesKP01_L19PA.KP01_L19PA);
		this.profits.addAll(ConstantesKP01_L19PB.KP01_L19PB);
		super.setWeights(Utils.convertToDouble(weights));
		super.setProfits(Utils.convertToDouble(profits));
		super.setCapacity(capacity);
		int var = this.weights.size();
		super.setNumVariables(var);
	}
}
