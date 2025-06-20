package com.VisNeo4j.App.Problems.Knapsack;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Knapsack01_S1 extends Knapsack01{
	
	double capacity = 269.0;
	private List<Double> weights = Stream.of(95.0, 4.0, 60.0, 32.0, 23.0, 72.0, 80.0, 62.0, 65.0, 46.0).collect(Collectors.toList());
	private List<Double> profits = Stream.of(55.0, 10.0, 47.0, 5.0, 4.0, 50.0, 8.0, 61.0, 85.0, 87.0).collect(Collectors.toList());
	
	public Knapsack01_S1() {
		super();
		super.setWeights(weights);
		super.setProfits(profits);
		super.setCapacity(capacity);
		int var = this.weights.size();
		super.setNumVariables(var);
	}

}
