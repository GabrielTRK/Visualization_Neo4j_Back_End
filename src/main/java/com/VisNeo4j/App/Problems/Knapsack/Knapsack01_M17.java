package com.VisNeo4j.App.Problems.Knapsack;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.VisNeo4j.App.Utils.Utils;

public class Knapsack01_M17 extends Knapsack01{

	double capacity = 1433.0;
	private List<Integer> weights = Stream.of(24, 45, 15, 40, 9, 37, 13, 5, 43, 35, 48, 50, 27, 46, 24,
			45, 2, 7, 38, 20, 20, 31, 2, 20, 3, 35, 27, 4, 21, 22, 33, 11,
			5, 24, 37, 31, 46, 13, 12, 12, 41, 36, 44, 36, 34, 22, 29, 50,
			48, 17, 8, 21, 28, 2, 44, 45, 25, 11, 37, 35, 24, 9, 40, 45, 8,
			47, 1, 22, 1, 12, 36, 35, 14, 17, 5).collect(Collectors.toList());
	private List<Integer> profits = Stream.of(2, 73, 82, 12, 49, 35, 78, 29, 83, 18, 87, 93, 20, 6, 55,
			1, 83, 91, 71, 25, 59, 94, 90, 61, 80, 84, 57, 1, 26, 44, 44,
			88, 7, 34, 18, 25, 73, 29, 24, 14, 23, 82, 38, 67, 94, 43, 61,
			97, 37, 67, 32, 89, 30, 30, 91, 50, 21, 3, 18, 31, 97, 79, 68,
			85, 43, 71, 49, 83, 44, 86, 1, 100, 28, 4, 16).collect(Collectors.toList());
	
	public Knapsack01_M17() {
		super();
		super.setWeights(Utils.convertToDouble(weights));
		super.setProfits(Utils.convertToDouble(profits));
		super.setCapacity(capacity);
		int var = this.weights.size();
		super.setNumVariables(var);
	}
}
