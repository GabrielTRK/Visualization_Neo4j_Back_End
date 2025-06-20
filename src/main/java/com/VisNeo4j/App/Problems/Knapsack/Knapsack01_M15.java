package com.VisNeo4j.App.Problems.Knapsack;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.VisNeo4j.App.Utils.Utils;

public class Knapsack01_M15 extends Knapsack01{

	double capacity = 1319.0;
	private List<Integer> weights = Stream.of(47, 27, 24, 27, 17, 17, 50, 24, 38, 34, 40, 14, 15, 36,
			10, 42, 9, 48, 37, 7, 43, 47, 29, 20, 23, 36, 14, 2, 48, 50,
			39, 50, 25, 7, 24, 38, 34, 44, 38, 31, 14, 17, 42, 20, 5,
			44, 22, 9, 1, 33, 19, 19, 23, 26, 16, 24, 1, 9, 16, 38, 30,
			36, 41, 43, 6).collect(Collectors.toList());
	private List<Integer> profits = Stream.of(47, 63, 81, 57, 3, 80, 28, 83, 69, 61, 39, 7, 100, 67, 23,
			10, 25, 91, 22, 48, 91, 20, 45, 62, 60, 67, 27, 43, 80, 94, 47,
			31, 44, 31, 28, 14, 17, 50, 9, 93, 15, 17, 72, 68, 36, 10, 1,
			38, 79, 45, 10, 81, 66, 46, 54, 53, 63, 65, 20, 81, 20, 42,
			24, 28, 1).collect(Collectors.toList());
	
	public Knapsack01_M15() {
		super();
		super.setWeights(Utils.convertToDouble(weights));
		super.setProfits(Utils.convertToDouble(profits));
		super.setCapacity(capacity);
		int var = this.weights.size();
		super.setNumVariables(var);
	}
}
