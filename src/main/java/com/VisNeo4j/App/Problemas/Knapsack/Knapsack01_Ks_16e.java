package com.VisNeo4j.App.Problemas.Knapsack;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.VisNeo4j.App.Utils.Utils;

public class Knapsack01_Ks_16e extends Knapsack01{
	
	double capacity = 3760429.0;
	private List<Integer> profits = Stream.of(1188681, 160632, 225764, 1762724, 840566, 1501156, 1265916, 672380, 1336652, 289808, 1252296, 20947, 472835, 1828177, 759603, 1253380).collect(Collectors.toList());
	private List<Integer> weights = Stream.of(615938, 74618, 109110, 821054, 458488, 821360, 609945, 361196, 633910, 133731, 619385, 11437, 232580, 987645, 354237, 676225).collect(Collectors.toList());
	
	public Knapsack01_Ks_16e() {
		super();
		super.setWeights(Utils.convertToDouble(weights));
		super.setProfits(Utils.convertToDouble(profits));
		super.setCapacity(capacity);
		int var = this.weights.size();
		super.setNumVariables(var);
	}

}
