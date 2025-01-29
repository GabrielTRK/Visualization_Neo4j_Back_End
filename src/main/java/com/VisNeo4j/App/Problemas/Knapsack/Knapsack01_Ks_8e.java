package com.VisNeo4j.App.Problemas.Knapsack;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.VisNeo4j.App.Utils.Utils;

public class Knapsack01_Ks_8e extends Knapsack01{
	
	double capacity = 2493250.0;
	private List<Integer> profits = Stream.of(1582289, 640086, 1270769, 1839736, 795116, 1099087, 1524697, 951036).collect(Collectors.toList());
	private List<Integer> weights = Stream.of(862754, 318418, 651921, 844733, 438185, 591527, 810761, 468202).collect(Collectors.toList());
	
	public Knapsack01_Ks_8e() {
		super();
		super.setWeights(Utils.convertToDouble(weights));
		super.setProfits(Utils.convertToDouble(profits));
		super.setCapacity(capacity);
		int var = this.weights.size();
		super.setNumVariables(var);
	}

}
