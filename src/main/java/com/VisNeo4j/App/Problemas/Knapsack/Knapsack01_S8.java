package com.VisNeo4j.App.Problemas.Knapsack;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.VisNeo4j.App.Utils.Utils;

public class Knapsack01_S8 extends Knapsack01{

	double capacity = 954.0;
	private List<Integer> weights = Stream.of(145,74,51,159,169,173,85,72,147,80
			,87,51,150,168,137,191,93,165,64,123).collect(Collectors.toList());
	private List<Integer> profits = Stream.of(18,18,20,12,8,5,3,10,8,14
			,17,12,12,14,7,3,6,4,1,18).collect(Collectors.toList());
	
	public Knapsack01_S8() {
		super();
		super.setWeights(Utils.convertToDouble(weights));
		super.setProfits(Utils.convertToDouble(profits));
		super.setCapacity(capacity);
		int var = this.weights.size();
		super.setNumVariables(var);
	}
}
