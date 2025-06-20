package com.VisNeo4j.App.Problems.Knapsack;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.VisNeo4j.App.Utils.Utils;

public class Knapsack01_S11 extends Knapsack01{

	double capacity = 1173.0;
	private List<Integer> weights = Stream.of(40,27,5,21,51,16,42,18,52,28,57,34,44,43,52,55,53,42,47,56,57,44,16,2,12,9,40,23,56,3,39,16,54,36,52,5,53,48,23, 47,41,49,22,42,10,16, 53,58,40,1,43,56,40,32,44,35,37,45,52,56,40,2,23,49,50,26,11,35,32,34,58,6,52,26,31,23,4,52,53,19).collect(Collectors.toList());
	private List<Integer> profits = Stream.of(199,194,193,191,189,178,174,169,164,164,161,158,157,154,152,152,149,142,131,125,124,124,124,122,119,116,114, 113,111,110,109,100, 97,94,91,82,82,81,80,80,80,79,77,76,74,72,71,70,69,68,65,65,61,56,55,54,53,47,47,46,41,36,34, 32,32,30,29,29,26,25,23,22,20,11,10,9,5,4,3,1).collect(Collectors.toList());
	
	public Knapsack01_S11() {
		super();
		super.setWeights(Utils.convertToDouble(weights));
		super.setProfits(Utils.convertToDouble(profits));
		super.setCapacity(capacity);
		int var = this.weights.size();
		super.setNumVariables(var);
	}
}
