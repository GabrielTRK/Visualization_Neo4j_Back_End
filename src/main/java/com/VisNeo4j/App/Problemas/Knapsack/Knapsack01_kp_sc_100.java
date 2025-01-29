package com.VisNeo4j.App.Problemas.Knapsack;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.VisNeo4j.App.Utils.Utils;

public class Knapsack01_kp_sc_100 extends Knapsack01{

	double capacity = 493.0;
	private List<Integer> weights = Stream.of(89, 83, 1, 36, 39, 53, 76, 9, 41, 15, 27, 26, 62, 3, 97, 45, 38, 24, 51, 72, 45, 56, 8, 8, 21, 96, 77, 91, 90, 72, 54, 93, 86, 83, 21, 39, 54, 72, 91, 31, 90, 23, 49, 40, 77, 41, 48, 93, 6, 94, 77, 32, 39, 5, 29, 45, 39, 87, 60, 6, 67, 54, 93, 45, 17, 69, 10, 32, 21, 1, 6, 86, 1, 78, 98, 32, 41, 52, 44, 41, 48, 9, 67, 83, 23, 71, 83, 100, 52, 32, 92, 24, 7, 14, 3, 54, 88, 62, 17, 67).collect(Collectors.toList());
	private List<Integer> profits = Stream.of(99, 93, 11, 46, 49, 63, 86, 19, 51, 25, 37, 36, 72, 13, 107, 55, 48, 34, 61, 82, 55, 66, 18, 18, 31, 106, 87, 101, 100, 82, 64, 103, 96, 93, 31, 49, 64, 82, 101, 41, 100, 33, 59, 50, 87, 51, 58, 103, 16, 104, 87, 42, 49, 15, 39, 55, 49, 97, 70, 16, 77, 64, 103, 55, 27, 79, 20, 42, 31, 11, 16, 96, 11, 88, 108, 42, 51, 62, 54, 51, 58, 19, 77, 93, 33, 81, 93, 110, 62, 42, 102, 34, 17, 24, 13, 64, 98, 72, 27, 77).collect(Collectors.toList());
	
	public Knapsack01_kp_sc_100() {
		super();
		super.setWeights(Utils.convertToDouble(weights));
		super.setProfits(Utils.convertToDouble(profits));
		super.setCapacity(capacity);
		int var = this.weights.size();
		super.setNumVariables(var);
	}
}
