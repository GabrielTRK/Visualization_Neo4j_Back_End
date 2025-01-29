package com.VisNeo4j.App.Problemas.Knapsack;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.VisNeo4j.App.Utils.Utils;

public class Knapsack01_kp_wc_100 extends Knapsack01{

	double capacity = 525.0;
	private List<Integer> weights = Stream.of(89, 1, 39, 76, 41, 27, 62, 97, 38, 51, 45, 8, 21, 77, 90, 54, 86, 21, 54, 91, 90, 49, 77, 48, 6, 77, 39, 29, 39, 60, 67, 93, 17, 10, 21, 6, 1, 98, 41, 44, 48, 67, 23, 83, 52, 92, 7, 3, 88, 17, 20, 89, 70, 49, 90, 85, 94, 52, 13, 15, 90, 88, 45, 52, 57, 1, 26, 76, 49, 67, 87, 59, 95, 28, 13, 42, 88, 16, 70, 60, 27, 3, 52, 91, 99, 63, 17, 49, 91, 100, 82, 46, 62, 49, 5, 82, 74, 54, 5, 61).collect(Collectors.toList());
	private List<Integer> profits = Stream.of(82, 4, 33, 67, 42, 28, 61, 107, 36, 59, 43, 1, 14, 79, 96, 51, 78, 21, 63, 90, 90, 41, 68, 42, 1, 75, 48, 33, 38, 50, 61, 103, 12, 18, 21, 1, 1, 108, 46, 52, 56, 76, 17, 73, 53, 86, 3, 11, 90, 19, 20, 99, 74, 49, 92, 76, 89, 56, 12, 19, 81, 84, 52, 61, 47, 1, 35, 74, 58, 66, 95, 58, 98, 37, 9, 44, 83, 6, 69, 52, 36, 7, 45, 83, 93, 58, 27, 50, 82, 91, 76, 46, 68, 57, 7, 73, 80, 54, 1, 71).collect(Collectors.toList());
	
	public Knapsack01_kp_wc_100() {
		super();
		super.setWeights(Utils.convertToDouble(weights));
		super.setProfits(Utils.convertToDouble(profits));
		super.setCapacity(capacity);
		int var = this.weights.size();
		super.setNumVariables(var);
	}
}
