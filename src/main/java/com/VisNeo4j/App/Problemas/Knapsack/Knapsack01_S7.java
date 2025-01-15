package com.VisNeo4j.App.Problemas.Knapsack;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.VisNeo4j.App.Utils.Utils;

public class Knapsack01_S7 extends Knapsack01{

	double capacity = 303.0;
	private List<Integer> weights = Stream.of(55, 93, 51, 95, 95, 76, 56, 59, 86, 92).collect(Collectors.toList());
	private List<Integer> profits = Stream.of(7, 5, 10, 8, 6, 3, 3, 9, 5, 9).collect(Collectors.toList());
	
	public Knapsack01_S7() {
		super();
		super.setWeights(Utils.convertToDouble(weights));
		super.setProfits(Utils.convertToDouble(profits));
		super.setCapacity(capacity);
		int var = this.weights.size();
		super.setNumVariables(var);
	}
}
