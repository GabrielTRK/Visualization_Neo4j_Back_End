package com.VisNeo4j.App.Problems.Knapsack;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.VisNeo4j.App.Utils.Utils;

public class Knapsack01_Ks_16a extends Knapsack01{

	double capacity = 3780355.0;
	private List<Integer> profits = Stream.of(248913, 1051063, 1211866, 716645, 103462, 544575, 860033, 2039565, 1174920, 704133, 1495071, 1907792, 1093059, 345186, 1097141, 487799).collect(Collectors.toList());
	private List<Integer> weights = Stream.of(123074, 517025, 655582, 371336, 51246, 296092, 431720, 933989, 561395, 376612, 722734, 995095, 568523, 168710, 520215, 267362).collect(Collectors.toList());
	
	public Knapsack01_Ks_16a() {
		super();
		super.setWeights(Utils.convertToDouble(weights));
		super.setProfits(Utils.convertToDouble(profits));
		super.setCapacity(capacity);
		int var = this.weights.size();
		super.setNumVariables(var);
	}
}
