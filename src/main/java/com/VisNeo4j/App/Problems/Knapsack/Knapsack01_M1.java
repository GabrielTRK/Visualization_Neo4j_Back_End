package com.VisNeo4j.App.Problems.Knapsack;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.VisNeo4j.App.Utils.Utils;

public class Knapsack01_M1 extends Knapsack01{

	double capacity = 1953.0;
	private List<Integer> weights = Stream.of(103,200,59,261,248,280,77,253,161,175
			,192,130,230,269,123,224,53,134,210,69
			,198,137,89,77,115,238,203,170,141,63).collect(Collectors.toList());
	private List<Integer> profits = Stream.of(20,15,17,19,18,3,17,27,18,22
			,3,29,14,23,10,28,11,5,11,11
			,17,25,7,5,13,11,29,28,10,7).collect(Collectors.toList());
	
	public Knapsack01_M1() {
		super();
		super.setWeights(Utils.convertToDouble(weights));
		super.setProfits(Utils.convertToDouble(profits));
		super.setCapacity(capacity);
		int var = this.weights.size();
		super.setNumVariables(var);
	}
}
