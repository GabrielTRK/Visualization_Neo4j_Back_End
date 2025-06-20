package com.VisNeo4j.App.Problems.Knapsack;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.VisNeo4j.App.Utils.Utils;

public class Knapsack01_M16 extends Knapsack01{

	double capacity = 1426.0;
	private List<Integer> weights = Stream.of(4, 16, 16, 2, 9, 44, 33, 43, 14, 45, 11, 49, 21, 12, 41,
			19, 26, 38, 42, 20, 5, 14, 40, 47, 29, 47, 30, 50, 39, 10, 26,
			33, 44, 31, 50, 7, 15, 24, 7, 12, 10, 34, 17, 40, 28, 12, 35, 3,
			29, 50, 19, 28, 47, 13, 42, 9, 44, 14, 43, 41, 10, 49, 13, 39,
			41, 25, 46, 6, 7, 43).collect(Collectors.toList());
	private List<Integer> profits = Stream.of(66, 76, 71, 61, 4, 20, 34, 65, 22, 8, 99, 21, 99, 62, 25,
			52, 72, 26, 12, 55, 22, 32, 98, 31, 95, 42, 2, 32, 16, 100,
			46, 55, 27, 89, 11, 83, 43, 93, 53, 88, 36, 41, 60, 92, 14, 5,
			41, 60, 92, 30, 55, 79, 33, 10, 45, 3, 68, 12, 20, 54, 63, 38,
			61, 85, 71, 40, 58, 25, 73, 35).collect(Collectors.toList());
	
	public Knapsack01_M16() {
		super();
		super.setWeights(Utils.convertToDouble(weights));
		super.setProfits(Utils.convertToDouble(profits));
		super.setCapacity(capacity);
		int var = this.weights.size();
		super.setNumVariables(var);
	}
}
