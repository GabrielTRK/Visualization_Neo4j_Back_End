package com.VisNeo4j.App.Problems.Knapsack;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.VisNeo4j.App.Utils.Utils;

public class Knapsack01_M12 extends Knapsack01{

	double capacity = 882.0;
	private List<Integer> weights = Stream.of(15, 40, 22, 28, 50, 35, 49, 5, 45, 3, 7, 32, 19,16, 40, 16,
			31, 24, 15, 42, 29, 4, 14, 9, 29, 11, 25, 37, 48, 39, 5, 47, 49,
			31, 48, 17, 46, 1, 25, 8, 16, 9, 30, 33, 18, 3, 3, 3, 4, 1).collect(Collectors.toList());
	private List<Integer> profits = Stream.of(78, 69, 87, 59, 63, 12, 22, 4, 45, 33, 29, 50, 19, 94, 95,
			60, 1, 91, 69, 8, 100, 32, 81, 47, 59, 48, 56, 18, 59, 16, 45,
			54, 47, 84, 100, 98, 75, 20, 4, 19, 58, 63, 37, 64, 90, 26,
			29, 13, 53, 83).collect(Collectors.toList());
	
	public Knapsack01_M12() {
		super();
		super.setWeights(Utils.convertToDouble(weights));
		super.setProfits(Utils.convertToDouble(profits));
		super.setCapacity(capacity);
		int var = this.weights.size();
		super.setNumVariables(var);
	}
}
