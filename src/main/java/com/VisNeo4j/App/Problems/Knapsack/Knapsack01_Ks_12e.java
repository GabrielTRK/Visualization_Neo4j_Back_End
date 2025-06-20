package com.VisNeo4j.App.Problems.Knapsack;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.VisNeo4j.App.Utils.Utils;

public class Knapsack01_Ks_12e extends Knapsack01{

	double capacity = 2520392.0;
	
	private List<Integer> weights = Stream.of(187536, 919812, 281447, 290967, 293933, 146982, 335995, 76949, 296586, 732368, 912094, 566115).collect(Collectors.toList());
	private List<Integer> profits = Stream.of(390564, 1896554, 518776, 534038, 539357, 281514, 679085, 164965, 603431, 1601666, 1826086, 1235821).collect(Collectors.toList());
	
	public Knapsack01_Ks_12e() {
		super();
		super.setWeights(Utils.convertToDouble(weights));
		super.setProfits(Utils.convertToDouble(profits));
		super.setCapacity(capacity);
		int var = this.weights.size();
		super.setNumVariables(var);
	}
}
