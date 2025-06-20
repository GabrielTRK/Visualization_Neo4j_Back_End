package com.VisNeo4j.App.Problems.Knapsack;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.VisNeo4j.App.Utils.Utils;

public class Knapsack01_M19 extends Knapsack01{

	double capacity = 1000.0;
	private List<Integer> weights = Stream.of(80, 82, 85, 70, 72, 70, 66, 50, 55,
			25, 50, 55, 40, 48, 50, 32, 22, 60, 30, 32, 40, 38, 35,
			32, 25, 28, 3, 22, 50, 30, 45, 30, 60, 50, 20, 65, 20,
			25, 30, 10, 20, 25, 15, 10, 10, 10, 4, 4, 2, 1).collect(Collectors.toList());
	private List<Integer> profits = Stream.of(220, 208, 198, 192, 180, 180, 165, 162, 160,
			158, 155, 130, 125, 122, 120, 118, 115, 110, 105,
			101, 100, 100, 98, 96, 95, 90, 88, 82, 80, 77, 75, 73,
			72, 70, 69, 66, 65, 63, 60, 58, 56, 50, 30, 20, 15, 10,
			8, 5, 3, 1).collect(Collectors.toList());
	
	public Knapsack01_M19() {
		super();
		super.setWeights(Utils.convertToDouble(weights));
		super.setProfits(Utils.convertToDouble(profits));
		super.setCapacity(capacity);
		int var = this.weights.size();
		super.setNumVariables(var);
	}
}
