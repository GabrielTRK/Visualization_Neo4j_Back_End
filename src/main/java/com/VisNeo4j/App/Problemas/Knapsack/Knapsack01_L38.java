package com.VisNeo4j.App.Problemas.Knapsack;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.VisNeo4j.App.Utils.Utils;

public class Knapsack01_L38 extends Knapsack01{

	double capacity = 493.0;
	private List<Integer> weights = Stream.of(89, 83, 1, 36, 39, 53, 76, 9, 41, 15, 27, 26, 62, 3, 97, 45, 38, 24, 51, 72, 45, 56, 8, 8, 21, 96, 77, 91, 90, 72, 54, 93, 86, 83, 21, 39, 54, 72, 91, 31, 90, 23, 49, 40, 77, 41, 48, 93, 6, 94, 77, 32, 39, 5, 29, 45, 39, 87, 60, 6, 67, 54, 93, 45, 17, 69, 10, 32, 21, 1, 6, 86, 1, 78, 98, 32, 41, 52, 44, 41, 48, 9, 67, 83, 23, 71, 83, 100, 52, 32, 92, 24, 7, 14, 3, 54, 88, 62, 17, 67).collect(Collectors.toList());
	private List<Integer> profits = Stream.of(89, 83, 1, 36, 39, 53, 76, 9, 41, 15, 27, 26, 62, 3, 97, 45, 38, 24, 51, 72, 45, 56, 8, 8, 21, 96, 77, 91, 90, 72, 54, 93, 86, 83, 21, 39, 54, 72, 91, 31, 90, 23, 49, 40, 77, 41, 48, 93, 6, 94, 77, 32, 39, 5, 29, 45, 39, 87, 60, 6, 67, 54, 93, 45, 17, 69, 10, 32, 21, 1, 6, 86, 1, 78, 98, 32, 41, 52, 44, 41, 48, 9, 67, 83, 23, 71, 83, 100, 52, 32, 92, 24, 7, 14, 3, 54, 88, 62, 17, 67).collect(Collectors.toList());
	
	public Knapsack01_L38() {
		super();
		super.setWeights(Utils.convertToDouble(weights));
		super.setProfits(Utils.convertToDouble(profits));
		super.setCapacity(capacity);
		int var = this.weights.size();
		super.setNumVariables(var);
	}
}
