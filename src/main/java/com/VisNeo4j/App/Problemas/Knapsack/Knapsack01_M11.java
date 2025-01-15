package com.VisNeo4j.App.Problemas.Knapsack;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.VisNeo4j.App.Utils.Utils;

public class Knapsack01_M11 extends Knapsack01{

	double capacity = 907.0;
	private List<Integer> weights = Stream.of(18, 12, 38, 12, 23, 13, 18, 46, 1, 7, 20, 43, 11, 47, 49,
			19, 50, 7, 39, 29, 32, 25, 12, 8, 32, 41, 34, 24, 48, 30, 12,
			35, 17, 38, 50, 14, 47, 35, 5, 13, 47, 24, 45, 39, 1).collect(Collectors.toList());
	private List<Integer> profits = Stream.of(98, 70, 66, 33, 2, 58, 4, 27, 20, 45, 77, 63, 32, 30, 8,
			18, 73, 9, 92, 43, 8, 58, 84, 35, 78, 71, 60, 38, 40, 43, 43,
			22, 50, 4, 57, 5, 88, 87, 34, 98, 96, 99, 16, 1, 2598, 70, 66, 33, 2, 58, 4, 27, 20, 45, 77, 63, 32, 30, 8,
			18, 73, 9, 92, 43, 8, 58, 84, 35, 78, 71, 60, 38, 40, 43, 43,
			22, 50, 4, 57, 5, 88, 87, 34, 98, 96, 99, 16, 1, 25).collect(Collectors.toList());
	
	public Knapsack01_M11() {
		super();
		super.setWeights(Utils.convertToDouble(weights));
		super.setProfits(Utils.convertToDouble(profits));
		super.setCapacity(capacity);
		int var = this.weights.size();
		super.setNumVariables(var);
	}
}
