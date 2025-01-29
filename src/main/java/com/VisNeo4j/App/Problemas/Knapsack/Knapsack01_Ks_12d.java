package com.VisNeo4j.App.Problemas.Knapsack;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.VisNeo4j.App.Utils.Utils;

public class Knapsack01_Ks_12d extends Knapsack01{

	double capacity = 3453702.0;
	
	private List<Integer> weights = Stream.of(992884, 417147, 996822, 591627, 482278, 651305, 491683, 727443, 135904, 152947, 590330, 677035).collect(Collectors.toList());
	private List<Integer> profits = Stream.of(2157066, 853212, 1845571, 1068849, 962615, 1278897, 1026191, 1377079, 264669, 299959, 1080762, 1263347).collect(Collectors.toList());
	
	public Knapsack01_Ks_12d() {
		super();
		super.setWeights(Utils.convertToDouble(weights));
		super.setProfits(Utils.convertToDouble(profits));
		super.setCapacity(capacity);
		int var = this.weights.size();
		super.setNumVariables(var);
	}
}
