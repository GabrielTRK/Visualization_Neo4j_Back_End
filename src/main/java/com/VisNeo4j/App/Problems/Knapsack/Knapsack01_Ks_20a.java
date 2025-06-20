package com.VisNeo4j.App.Problems.Knapsack;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.VisNeo4j.App.Utils.Utils;

public class Knapsack01_Ks_20a extends Knapsack01{

	double capacity = 5169647.0;
	
	private List<Integer> profits = Stream.of(1490790, 691574, 150435, 1164050, 1227890, 219877, 1934511, 199053, 514991, 1522492, 769943, 1680173, 1839759, 523879, 800279, 1097473, 1091605, 1921590, 1495883, 139897).collect(Collectors.toList());
	private List<Integer> weights = Stream.of(807304, 365268, 71972, 635962, 628375, 105248, 912959, 102532, 258867, 778387, 351744, 775629, 997576, 264796, 438778, 549088, 522456, 920079, 775693, 76582).collect(Collectors.toList());
	
	public Knapsack01_Ks_20a() {
		super();
		super.setWeights(Utils.convertToDouble(weights));
		super.setProfits(Utils.convertToDouble(profits));
		super.setCapacity(capacity);
		int var = this.weights.size();
		super.setNumVariables(var);
	}
}
