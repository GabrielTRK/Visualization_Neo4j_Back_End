package com.VisNeo4j.App.Problems.Knapsack;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.VisNeo4j.App.Utils.Utils;

public class Knapsack01_Ks_8d extends Knapsack01{
	
	double capacity = 2112292.0;
	private List<Integer> profits = Stream.of(1521886, 919285, 794937, 1296002, 354090, 1582223, 409313, 1401234).collect(Collectors.toList());
	private List<Integer> weights = Stream.of(800588, 496540, 364175, 699421, 179078, 778021, 206205, 700556).collect(Collectors.toList());
	
	public Knapsack01_Ks_8d() {
		super();
		super.setWeights(Utils.convertToDouble(weights));
		super.setProfits(Utils.convertToDouble(profits));
		super.setCapacity(capacity);
		int var = this.weights.size();
		super.setNumVariables(var);
	}

}
