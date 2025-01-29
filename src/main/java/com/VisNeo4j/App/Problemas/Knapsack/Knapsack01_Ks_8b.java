package com.VisNeo4j.App.Problemas.Knapsack;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.VisNeo4j.App.Utils.Utils;

public class Knapsack01_Ks_8b extends Knapsack01{
	
	double capacity = 1822718.0;
	private List<Integer> profits = Stream.of(1452321, 747077, 209067, 674618, 1076193, 1619423, 1212262, 538078).collect(Collectors.toList());
	private List<Integer> weights = Stream.of(666885, 346111, 97003, 337746, 507338, 836649, 564424, 289280).collect(Collectors.toList());
	
	public Knapsack01_Ks_8b() {
		super();
		super.setWeights(Utils.convertToDouble(weights));
		super.setProfits(Utils.convertToDouble(profits));
		super.setCapacity(capacity);
		int var = this.weights.size();
		super.setNumVariables(var);
	}

}
