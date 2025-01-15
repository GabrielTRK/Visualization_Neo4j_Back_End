package com.VisNeo4j.App.Problemas.Knapsack;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.VisNeo4j.App.Utils.Utils;

public class Knapsack01_S6 extends Knapsack01{

	double capacity = 879.0;
	private List<Integer> weights = Stream.of(84, 83, 43, 4, 44, 6, 82, 92, 25, 83,
			56, 18, 58, 14, 48, 70, 96, 32, 68, 92).collect(Collectors.toList());
	private List<Integer> profits = Stream.of(91, 72, 90, 46, 55, 8, 35, 75,
			61, 15, 77, 40, 63, 75, 29, 75, 17, 78, 40,
			44).collect(Collectors.toList());
	
	public Knapsack01_S6() {
		super();
		super.setWeights(Utils.convertToDouble(weights));
		super.setProfits(Utils.convertToDouble(profits));
		super.setCapacity(capacity);
		int var = this.weights.size();
		super.setNumVariables(var);
	}
}
