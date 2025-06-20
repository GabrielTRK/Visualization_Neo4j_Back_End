package com.VisNeo4j.App.Problems.Knapsack;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.VisNeo4j.App.Utils.Utils;

public class Knapsack01_Ks_20c extends Knapsack01{
	
	double capacity = 5063791.0;
	private List<Integer> profits = Stream.of(925328, 164044, 1966178, 1440910, 1288193, 741301, 1241063, 1447446, 1388189, 916476, 1240551, 639466, 1124003, 1867028, 40187, 1361579, 770874, 44934, 1671823, 119080).collect(Collectors.toList());
	private List<Integer> weights = Stream.of(440356, 79368, 945895, 666364, 594595, 358461, 666020, 801970, 716869, 431640, 576028, 308237, 541552, 879694, 18964, 741144, 410306, 24327, 868689, 57104).collect(Collectors.toList());
	
	public Knapsack01_Ks_20c() {
		super();
		super.setWeights(Utils.convertToDouble(weights));
		super.setProfits(Utils.convertToDouble(profits));
		super.setCapacity(capacity);
		int var = this.weights.size();
		super.setNumVariables(var);
	}

}
