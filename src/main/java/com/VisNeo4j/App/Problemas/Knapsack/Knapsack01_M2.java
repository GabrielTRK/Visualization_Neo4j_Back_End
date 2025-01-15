package com.VisNeo4j.App.Problemas.Knapsack;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.VisNeo4j.App.Utils.Utils;

public class Knapsack01_M2 extends Knapsack01{

	double capacity = 3635.0;
	private List<Integer> weights = Stream.of(394,219,269,261,199,121,386,262,240,333
			,255,72,67,399,53,284,188,202,219,80
			,268,340,71,359,66,389,171,136,356,303
			,70,199,58,318,106,379,350,150,394,101).collect(Collectors.toList());
	private List<Integer> profits = Stream.of(17,10,8,33,7,28,18,39,23,24
			,37,34,12,37,27,6,1,3,3,19
			,24,38,11,35,4,38,27,10,7,11
			,15,32,38,36,19,26,3,18,14,26).collect(Collectors.toList());
	
	public Knapsack01_M2() {
		super();
		super.setWeights(Utils.convertToDouble(weights));
		super.setProfits(Utils.convertToDouble(profits));
		super.setCapacity(capacity);
		int var = this.weights.size();
		super.setNumVariables(var);
	}
}
