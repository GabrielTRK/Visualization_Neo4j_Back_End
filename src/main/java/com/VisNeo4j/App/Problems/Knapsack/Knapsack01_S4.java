package com.VisNeo4j.App.Problems.Knapsack;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.VisNeo4j.App.Utils.Utils;

public class Knapsack01_S4 extends Knapsack01{
	
	double capacity = 60.0;
	private List<Integer> weights = Stream.of(30,25,20,18,17,11,5,2,1,1).collect(Collectors.toList());
	private List<Integer> profits = Stream.of(20,18,17,15,15,10,5,3,1,1).collect(Collectors.toList());
	
	public Knapsack01_S4() {
		super();
		super.setWeights(Utils.convertToDouble(weights));
		super.setProfits(Utils.convertToDouble(profits));
		super.setCapacity(capacity);
		int var = this.weights.size();
		super.setNumVariables(var);
	}

}
