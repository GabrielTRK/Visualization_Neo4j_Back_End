package com.VisNeo4j.App.Problems.Knapsack;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.VisNeo4j.App.Utils.Utils;

public class Knapsack01_S5 extends Knapsack01{

	double capacity = 10000.0;
	private List<Integer> weights = Stream.of(983, 982, 981, 980, 979, 978, 488,
			976, 972, 486, 486, 972, 972, 485, 485,
			969, 966, 483, 964, 963, 961, 958, 959).collect(Collectors.toList());
	private List<Integer> profits = Stream.of(981, 980, 979, 978, 977,
			976, 487, 974, 970, 485, 485, 970, 970,
			484, 484, 976, 974, 482, 962, 961, 959,
			958, 857).collect(Collectors.toList());
	
	public Knapsack01_S5() {
		super();
		super.setWeights(Utils.convertToDouble(weights));
		super.setProfits(Utils.convertToDouble(profits));
		super.setCapacity(capacity);
		int var = this.weights.size();
		super.setNumVariables(var);
	}
}
