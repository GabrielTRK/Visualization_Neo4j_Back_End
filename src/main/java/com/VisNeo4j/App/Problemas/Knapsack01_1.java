package com.VisNeo4j.App.Problemas;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Knapsack01_1 extends Knapsack01{
	
	double capacity = 269.0;
	private List<Integer> weights = Stream.of(95,4,60,32,23,72,80,62,65,46).collect(Collectors.toList());
	private List<Integer> profits = Stream.of(55,10,47,5,4,50,8,61,85,87).collect(Collectors.toList());
	
	public Knapsack01_1() {
		super();
		super.setWeights(weights);
		super.setProfits(profits);
		super.setCapacity(capacity);
		int var = this.weights.size();
		super.setNumVariables(var);
	}

}
