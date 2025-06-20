package com.VisNeo4j.App.Problems.Knapsack;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.VisNeo4j.App.Utils.Utils;

public class Knapsack01_Ks_24d extends Knapsack01{
	
	double capacity = 5762284.0;
	private List<Integer> profits = Stream.of(1844400, 75605, 1344522, 1725990, 1029572, 223878, 935619, 725710, 1003949, 953278, 900820, 97657, 215673, 143729, 293251, 187454, 1696715, 1495747, 1589311, 1821594, 495147, 1250695, 970416, 1459955).collect(Collectors.toList());
	private List<Integer> weights = Stream.of(844709, 36928, 649129, 925892, 475939, 108733, 488034, 395701, 550693, 509694, 440355, 49925, 100736, 79142, 152798, 88114, 941561, 820124, 827804, 918561, 261353, 573421, 497161, 788062).collect(Collectors.toList());
	
	public Knapsack01_Ks_24d() {
		super();
		super.setWeights(Utils.convertToDouble(weights));
		super.setProfits(Utils.convertToDouble(profits));
		super.setCapacity(capacity);
		int var = this.weights.size();
		super.setNumVariables(var);
	}

}
