package com.VisNeo4j.App.Problems.Knapsack;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.VisNeo4j.App.Utils.Utils;

public class Knapsack01_Ks_12b extends Knapsack01{

	double capacity = 3259036.0;
	
	private List<Integer> profits = Stream.of(800734, 843137, 551965, 1921987, 1429742, 1272555, 552649, 1468914, 645615, 1859603, 89001, 1190478).collect(Collectors.toList());
	private List<Integer> weights = Stream.of(419614, 463634, 305284, 918709, 743181, 652957, 256487, 790046, 310107, 985008, 43471, 629575).collect(Collectors.toList());
	
	public Knapsack01_Ks_12b() {
		super();
		super.setWeights(Utils.convertToDouble(weights));
		super.setProfits(Utils.convertToDouble(profits));
		super.setCapacity(capacity);
		int var = this.weights.size();
		super.setNumVariables(var);
	}
}
