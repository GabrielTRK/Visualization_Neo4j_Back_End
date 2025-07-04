package com.VisNeo4j.App.Problems.Knapsack;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.VisNeo4j.App.Utils.Utils;

public class Knapsack01_M10 extends Knapsack01{

	double capacity = 819.0;
	private List<Integer> weights = Stream.of(28, 23, 35, 38, 20, 29, 11, 48, 26, 14, 12, 48, 35, 36,
			33, 39, 30, 26, 44, 20, 13, 15, 46, 36, 43, 19, 32, 2, 47, 24,
			26, 39, 17, 32, 17, 16, 33, 22, 6, 12).collect(Collectors.toList());
	private List<Integer> profits = Stream.of(13, 16, 42, 69, 66, 68, 1, 13, 77, 85, 75, 95, 92, 23, 51,
			79, 53, 62, 56, 74, 7, 50, 23, 34, 56, 75, 42, 51, 13, 22, 30,
			45, 25, 27, 90, 59, 94, 62, 26, 11).collect(Collectors.toList());
	
	public Knapsack01_M10() {
		super();
		super.setWeights(Utils.convertToDouble(weights));
		super.setProfits(Utils.convertToDouble(profits));
		super.setCapacity(capacity);
		int var = this.weights.size();
		super.setNumVariables(var);
	}
}
