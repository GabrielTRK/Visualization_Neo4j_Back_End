package com.VisNeo4j.App.Problems.Knapsack;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.VisNeo4j.App.Utils.Utils;

public class Knapsack01_M14 extends Knapsack01{

	double capacity = 1006.0;
	private List<Integer> weights = Stream.of(7, 13, 47, 33, 38, 41, 3, 21, 37, 7, 32, 13, 42, 42, 23,
			20, 49, 1, 20, 25, 31, 4, 8, 33, 11, 6, 3, 9, 26, 44, 39, 7, 4,
			34, 25, 25, 16, 17, 46, 23, 38, 10, 5, 11, 28, 34, 47, 3, 9, 22,
			17, 5, 41, 20, 33, 29, 1, 33, 16, 14).collect(Collectors.toList());
	private List<Integer> profits = Stream.of(81, 37, 70, 64, 97, 21, 60, 9, 55, 85, 5, 33, 71, 87, 51,
			100, 43, 27, 48, 17, 16, 27, 76, 61, 97, 78, 58, 46, 29, 76,
			10, 11, 74, 36, 59, 30, 72, 37, 72, 100, 9, 47, 10, 73, 92,
			9, 52, 56, 69, 30, 61, 20, 66, 70, 46, 16, 43, 60, 33, 84).collect(Collectors.toList());
	
	public Knapsack01_M14() {
		super();
		super.setWeights(Utils.convertToDouble(weights));
		super.setProfits(Utils.convertToDouble(profits));
		super.setCapacity(capacity);
		int var = this.weights.size();
		super.setNumVariables(var);
	}
}
