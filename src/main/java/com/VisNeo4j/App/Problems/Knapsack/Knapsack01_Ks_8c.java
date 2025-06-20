package com.VisNeo4j.App.Problems.Knapsack;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.VisNeo4j.App.Utils.Utils;

public class Knapsack01_Ks_8c extends Knapsack01{
	
	double capacity = 1609419.0;
	private List<Integer> profits = Stream.of(1155261, 73653, 1631996, 1130519, 1045697, 1061672, 193508, 296438).collect(Collectors.toList());
	private List<Integer> weights = Stream.of(580593, 34916, 848308, 528023, 497440, 490277, 97943, 141338).collect(Collectors.toList());
	
	public Knapsack01_Ks_8c() {
		super();
		super.setWeights(Utils.convertToDouble(weights));
		super.setProfits(Utils.convertToDouble(profits));
		super.setCapacity(capacity);
		int var = this.weights.size();
		super.setNumVariables(var);
	}

}
