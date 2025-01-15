package com.VisNeo4j.App.Problemas.Knapsack;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.VisNeo4j.App.Utils.Utils;

public class Knapsack01_S2 extends Knapsack01{

	double capacity = 878.0;
	private List<Integer> weights = Stream.of(92, 4, 43, 83, 84, 68, 92, 82, 6, 44,
			32, 18, 56, 83, 25, 96, 70, 48, 14, 58).collect(Collectors.toList());
	private List<Integer> profits = Stream.of(44, 46, 90, 72, 91, 40, 75,
			35, 8, 54, 78, 40, 77, 15, 61, 17, 75, 29,
			75, 63).collect(Collectors.toList());
	
	public Knapsack01_S2() {
		super();
		super.setWeights(Utils.convertToDouble(weights));
		super.setProfits(Utils.convertToDouble(profits));
		super.setCapacity(capacity);
		int var = this.weights.size();
		super.setNumVariables(var);
	}
}
