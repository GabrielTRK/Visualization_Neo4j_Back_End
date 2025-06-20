package com.VisNeo4j.App.Problems.Knapsack;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.VisNeo4j.App.Utils.Utils;

public class Knapsack01_kp_uc_100 extends Knapsack01{

	double capacity = 525.0;
	private List<Integer> weights = Stream.of(89, 1, 39, 76, 41, 27, 62, 97, 38, 51, 45, 8, 21, 77, 90, 54, 86, 21, 54, 91, 90, 49, 77, 48, 6, 77, 39, 29, 39, 60, 67, 93, 17, 10, 21, 6, 1, 98, 41, 44, 48, 67, 23, 83, 52, 92, 7, 3, 88, 17, 20, 89, 70, 49, 90, 85, 94, 52, 13, 15, 90, 88, 45, 52, 57, 1, 26, 76, 49, 67, 87, 59, 95, 28, 13, 42, 88, 16, 70, 60, 27, 3, 52, 91, 99, 63, 17, 49, 91, 100, 82, 46, 62, 49, 5, 82, 74, 54, 5, 61).collect(Collectors.toList());
	private List<Integer> profits = Stream.of(83, 36, 53, 9, 15, 26, 3, 45, 24, 72, 56, 8, 96, 91, 72, 93, 83, 39, 72, 31, 23, 40, 41, 93, 94, 32, 5, 45, 87, 6, 54, 45, 69, 32, 1, 86, 78, 32, 52, 41, 9, 83, 71, 100, 32, 24, 14, 54, 62, 67, 56, 12, 100, 14, 41, 23, 16, 13, 29, 60, 41, 71, 43, 31, 12, 86, 62, 19, 94, 15, 46, 60, 30, 71, 28, 27, 7, 10, 65, 53, 12, 51, 95, 83, 82, 30, 39, 16, 9, 76, 67, 63, 43, 66, 41, 92, 38, 29, 86, 38).collect(Collectors.toList());
	
	public Knapsack01_kp_uc_100() {
		super();
		super.setWeights(Utils.convertToDouble(weights));
		super.setProfits(Utils.convertToDouble(profits));
		super.setCapacity(capacity);
		int var = this.weights.size();
		super.setNumVariables(var);
	}
}
