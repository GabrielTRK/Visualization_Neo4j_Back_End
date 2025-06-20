package com.VisNeo4j.App.Problems.Knapsack;

import java.util.ArrayList;
import java.util.List;

import com.VisNeo4j.App.Constantes.ConstantesKP01_L20PA;
import com.VisNeo4j.App.Constantes.ConstantesKP01_L20PB;
import com.VisNeo4j.App.Constantes.ConstantesKP01_L20WA;
import com.VisNeo4j.App.Constantes.ConstantesKP01_L20WB;
import com.VisNeo4j.App.Utils.Utils;

public class Knapsack01_L20 extends Knapsack01{

	double capacity = 170502369.0;
	private List<Integer> weights = new ArrayList<>();
	private List<Integer> profits = new ArrayList<>();
	
	public Knapsack01_L20() {
		super();
		this.weights.addAll(ConstantesKP01_L20WA.KP01_L20WA);
		this.weights.addAll(ConstantesKP01_L20WB.KP01_L20WB);
		this.profits.addAll(ConstantesKP01_L20PA.KP01_L20PA);
		this.profits.addAll(ConstantesKP01_L20PB.KP01_L20PB);
		super.setWeights(Utils.convertToDouble(weights));
		super.setProfits(Utils.convertToDouble(profits));
		super.setCapacity(capacity);
		int var = this.weights.size();
		super.setNumVariables(var);
	}
}
