package com.VisNeo4j.App.Problems.Knapsack;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.VisNeo4j.App.Utils.Utils;

public class Knapsack01_Ks_20e extends Knapsack01{
	
	double capacity = 4476000.0;
	private List<Integer> profits = Stream.of(128319, 1601667, 475634, 531575, 1251993, 1108634, 1039759, 468728, 698158, 372342, 1161048, 403961, 1362470, 1773653, 580301, 1947699, 495773, 2007127, 472069, 245042).collect(Collectors.toList());
	private List<Integer> weights = Stream.of(63416, 838949, 231705, 278430, 579076, 542663, 544786, 226831, 362836, 192630, 543297, 194244, 668225, 858827, 306890, 914752, 243468, 983177, 262040, 115758).collect(Collectors.toList());
	
	public Knapsack01_Ks_20e() {
		super();
		super.setWeights(Utils.convertToDouble(weights));
		super.setProfits(Utils.convertToDouble(profits));
		super.setCapacity(capacity);
		int var = this.weights.size();
		super.setNumVariables(var);
	}

}
