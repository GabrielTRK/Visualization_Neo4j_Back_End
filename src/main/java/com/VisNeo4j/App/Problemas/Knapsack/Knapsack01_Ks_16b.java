package com.VisNeo4j.App.Problemas.Knapsack;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.VisNeo4j.App.Utils.Utils;

public class Knapsack01_Ks_16b extends Knapsack01{
	
	double capacity = 4426945.0;
	private List<Integer> profits = Stream.of(66111, 215853, 452976, 1435157, 1901253, 649654, 2034198, 496995, 1725886, 678356, 1329367, 1088047, 1507322, 1041781, 1607816, 1634282).collect(Collectors.toList());
	private List<Integer> weights = Stream.of(31957, 117108, 250834, 775747, 994544, 315281, 937184, 258099, 877986, 327103, 632921, 518873, 708189, 537974, 732066, 838024).collect(Collectors.toList());
	
	public Knapsack01_Ks_16b() {
		super();
		super.setWeights(Utils.convertToDouble(weights));
		super.setProfits(Utils.convertToDouble(profits));
		super.setCapacity(capacity);
		int var = this.weights.size();
		super.setNumVariables(var);
	}
}
