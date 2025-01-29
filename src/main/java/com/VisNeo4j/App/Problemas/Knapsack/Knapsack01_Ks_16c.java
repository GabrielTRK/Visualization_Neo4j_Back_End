package com.VisNeo4j.App.Problemas.Knapsack;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.VisNeo4j.App.Utils.Utils;

public class Knapsack01_Ks_16c extends Knapsack01{
	
	double capacity = 4323280.0;
	private List<Integer> profits = Stream.of(837989, 370555, 843247, 1625049, 1127636, 664244, 900936, 747010, 517890, 2092844, 1749694, 1376143, 756658, 483621, 1583667, 1826529).collect(Collectors.toList());
	private List<Integer> weights = Stream.of(406270, 182392, 468242, 749900, 581384, 334532, 483985, 342610, 260722, 970898, 887883, 657188, 369265, 252140, 727195, 971955).collect(Collectors.toList());
	
	public Knapsack01_Ks_16c() {
		super();
		super.setWeights(Utils.convertToDouble(weights));
		super.setProfits(Utils.convertToDouble(profits));
		super.setCapacity(capacity);
		int var = this.weights.size();
		super.setNumVariables(var);
	}

}
