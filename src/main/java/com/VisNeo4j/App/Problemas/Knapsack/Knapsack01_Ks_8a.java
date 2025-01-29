package com.VisNeo4j.App.Problemas.Knapsack;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.VisNeo4j.App.Utils.Utils;

public class Knapsack01_Ks_8a extends Knapsack01{
	
	double capacity = 1863633.0;
	private List<Integer> profits = Stream.of(25424, 604597, 1272766, 1174735, 1707707, 313906, 1689410, 860062).collect(Collectors.toList());
	private List<Integer> weights = Stream.of(12630, 284975, 583838, 575342, 780934, 164152, 912739, 412657).collect(Collectors.toList());
	
	public Knapsack01_Ks_8a() {
		super();
		super.setWeights(Utils.convertToDouble(weights));
		super.setProfits(Utils.convertToDouble(profits));
		super.setCapacity(capacity);
		int var = this.weights.size();
		super.setNumVariables(var);
	}

}
