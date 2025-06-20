package com.VisNeo4j.App.Problems.Knapsack;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.VisNeo4j.App.Utils.Utils;

public class Knapsack01_M8 extends Knapsack01{

	double capacity = 577.0;
	private List<Integer> weights = Stream.of(46, 17, 35, 1, 26, 17, 17, 48, 38, 17, 32, 21, 29, 48,
			31, 8, 42, 37, 6, 9, 15, 22, 27, 14, 42, 40, 14, 31, 6, 34).collect(Collectors.toList());
	private List<Integer> profits = Stream.of(57, 64, 50, 6, 52, 6, 85, 60, 70, 65, 63, 96, 18, 48, 85,
			50, 77, 18, 70, 92, 17, 43, 5, 23, 67, 88, 35, 3, 91, 48).collect(Collectors.toList());
	
	public Knapsack01_M8() {
		super();
		super.setWeights(Utils.convertToDouble(weights));
		super.setProfits(Utils.convertToDouble(profits));
		super.setCapacity(capacity);
		int var = this.weights.size();
		super.setNumVariables(var);
	}
}
