package com.VisNeo4j.App.Problems.Knapsack;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.VisNeo4j.App.Utils.Utils;

public class Knapsack01_M4 extends Knapsack01{

	double capacity = 8668.0;
	private List<Integer> weights = Stream.of(532,76,497,352,110,498,175,134,65,272
			,307,500,483,389,314,327,507,449,63,123
			,404,555,96,280,571,591,567,440,416,385
			,438,472,410,189,418,293,440,598,490,201
			,265,599,141,242,450,550,583,363,348,337
			,226,232,348,381,447,542,343,226,493,126).collect(Collectors.toList());
	private List<Integer> profits = Stream.of(27,26,20,56,18,52,9,51,19,35
			,13,21,8,50,60,35,58,44,18,42
			,59,37,31,60,6,3,18,51,7,26
			,50,30,49,12,34,34,57,54,4,59
			,51,32,17,5,21,6,53,5,15,29
			,39,34,5,26,4,39,12,24,4,33).collect(Collectors.toList());
	
	public Knapsack01_M4() {
		super();
		super.setWeights(Utils.convertToDouble(weights));
		super.setProfits(Utils.convertToDouble(profits));
		super.setCapacity(capacity);
		int var = this.weights.size();
		super.setNumVariables(var);
	}
}
