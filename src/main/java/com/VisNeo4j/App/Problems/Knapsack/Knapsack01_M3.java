package com.VisNeo4j.App.Problems.Knapsack;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.VisNeo4j.App.Utils.Utils;

public class Knapsack01_M3 extends Knapsack01{

	double capacity = 5258.0;
	private List<Integer> weights = Stream.of(55,444,321,351,108,202,335,101,179,142
			,336,331,357,310,58,247,290,100,486,260
			,391,448,191,181,131,408,60,277,466,178
			,499,321,107,461,97,178,128,153,249,180
			,73,278,497,334,351,466,312,279,146,293).collect(Collectors.toList());
	private List<Integer> profits = Stream.of(39,8,36,1,44,30,38,28,31,11
			,12,47,26,29,13,23,47,26,35,12
			,18,23,45,39,30,14,24,21,22,1
			,35,11,47,19,10,25,50,50,17,50
			,41,11,10,17,47,38,28,14,14,13).collect(Collectors.toList());
	
	public Knapsack01_M3() {
		super();
		super.setWeights(Utils.convertToDouble(weights));
		super.setProfits(Utils.convertToDouble(profits));
		super.setCapacity(capacity);
		int var = this.weights.size();
		super.setNumVariables(var);
	}
}
