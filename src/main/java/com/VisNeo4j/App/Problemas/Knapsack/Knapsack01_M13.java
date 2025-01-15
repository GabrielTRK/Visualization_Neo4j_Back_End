package com.VisNeo4j.App.Problemas.Knapsack;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.VisNeo4j.App.Utils.Utils;

public class Knapsack01_M13 extends Knapsack01{

	double capacity = 1050.0;
	private List<Integer> weights = Stream.of(27, 15, 46, 5, 40, 9, 36, 12, 11, 11, 49, 20, 32, 3, 12,
			44, 24, 1, 24, 42, 44, 16, 12, 42, 22, 26, 10, 8, 46, 50, 20,
			42, 48, 45, 43, 35, 9, 12, 22, 2, 14, 50, 16, 29, 31, 46, 20,
			35, 11, 4, 32, 35, 15, 29, 16).collect(Collectors.toList());
	private List<Integer> profits = Stream.of(98, 74, 76, 4, 12, 27, 90, 98, 100, 35, 30, 19, 75, 72,
			19, 44, 5, 66, 79, 87, 79, 44, 35, 6, 82, 11, 1, 28, 95, 68, 39,
			86, 68, 61, 44, 97, 83, 2, 15, 49, 59, 30, 44, 40, 14, 96, 37,
			84, 5, 43, 8, 32, 95, 86, 18).collect(Collectors.toList());
	
	public Knapsack01_M13() {
		super();
		super.setWeights(Utils.convertToDouble(weights));
		super.setProfits(Utils.convertToDouble(profits));
		super.setCapacity(capacity);
		int var = this.weights.size();
		super.setNumVariables(var);
	}
}
