package com.VisNeo4j.App.Problems.Knapsack;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.VisNeo4j.App.Utils.Utils;

public class Knapsack01_S10 extends Knapsack01{

	double capacity = 11258.0;
	private List<Integer> weights = Stream.of(438,754,699,587,789,912,819,347,511,287,541,784,676,198,572,914,988,4,355,569,144,272,531,556,741,489,321,84, 194,483,205,607, 399,747,118,651,806,9,607,121,370,999,494,743,967,718,397,589,193,369).collect(Collectors.toList());
	private List<Integer> profits = Stream.of(72,490,651,833,883,489,359,337,267,441,70,934,467,661,220,329,440,774,595,98,424,37,807,320,501,309,834,851,34, 459,111,253,159, 858,793,145,651,856,400,285,405,95,391,19,96,273,152,473,448,231).collect(Collectors.toList());
	
	public Knapsack01_S10() {
		super();
		super.setWeights(Utils.convertToDouble(weights));
		super.setProfits(Utils.convertToDouble(profits));
		super.setCapacity(capacity);
		int var = this.weights.size();
		super.setNumVariables(var);
	}
}
