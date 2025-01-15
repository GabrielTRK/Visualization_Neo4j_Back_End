package com.VisNeo4j.App.Problemas.Knapsack;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.VisNeo4j.App.Constantes.ConstantesKP01_L17PA;
import com.VisNeo4j.App.Constantes.ConstantesKP01_L17PB;
import com.VisNeo4j.App.Constantes.ConstantesKP01_L17WA;
import com.VisNeo4j.App.Constantes.ConstantesKP01_L17WB;
import com.VisNeo4j.App.Utils.Utils;

public class Knapsack01_L17 extends Knapsack01{

	double capacity = 78058739.0;
	private List<Integer> weights = new ArrayList<>();
	private List<Integer> profits = new ArrayList<>();
	
	public Knapsack01_L17() {
		super();
		this.weights.addAll(ConstantesKP01_L17WA.KP01_L17WA);
		this.weights.addAll(ConstantesKP01_L17WB.KP01_L17WB);
		this.profits.addAll(ConstantesKP01_L17PA.KP01_L17PA);
		this.profits.addAll(ConstantesKP01_L17PB.KP01_L17PB);
		super.setWeights(Utils.convertToDouble(weights));
		super.setProfits(Utils.convertToDouble(profits));
		super.setCapacity(capacity);
		int var = this.weights.size();
		super.setNumVariables(var);
	}
}
