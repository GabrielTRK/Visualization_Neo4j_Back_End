package com.VisNeo4j.App.Problemas.Knapsack;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.VisNeo4j.App.Utils.Utils;

public class Knapsack01_Ks_12a extends Knapsack01{

	double capacity = 2805213.0;
	private List<Integer> profits = Stream.of(1370413, 1281034, 124356, 1296241, 1410881, 993625, 1209949, 573180, 147263, 617431, 1193525, 861742).collect(Collectors.toList());
	private List<Integer> weights = Stream.of(672437, 664905, 66143, 670473, 649045, 513812, 638975, 262871, 79332, 334123, 620541, 437769).collect(Collectors.toList());
	
	public Knapsack01_Ks_12a() {
		super();
		super.setWeights(Utils.convertToDouble(weights));
		super.setProfits(Utils.convertToDouble(profits));
		super.setCapacity(capacity);
		int var = this.weights.size();
		super.setNumVariables(var);
	}
}
