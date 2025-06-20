package com.VisNeo4j.App.Problems.Knapsack;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.VisNeo4j.App.Utils.Utils;

public class Knapsack01_M9 extends Knapsack01{

	double capacity = 655.0;
	private List<Integer> weights = Stream.of(7, 4, 36, 47, 6, 33, 8, 35, 32, 3, 40, 50, 22, 18, 3, 12,
			30, 31, 13, 33, 4, 48, 5, 17, 33, 26, 27, 19, 39, 15, 33, 47,
			17, 41, 40).collect(Collectors.toList());
	private List<Integer> profits = Stream.of(35, 67, 30, 69, 40, 40, 21, 73, 82, 93, 52, 20, 61, 20, 42,
			86, 43, 93, 38, 70, 59, 11, 42, 93, 6, 39, 25, 23, 36, 93, 51,
			81, 36, 46, 96).collect(Collectors.toList());
	
	public Knapsack01_M9() {
		super();
		super.setWeights(Utils.convertToDouble(weights));
		super.setProfits(Utils.convertToDouble(profits));
		super.setCapacity(capacity);
		int var = this.weights.size();
		super.setNumVariables(var);
	}
}
