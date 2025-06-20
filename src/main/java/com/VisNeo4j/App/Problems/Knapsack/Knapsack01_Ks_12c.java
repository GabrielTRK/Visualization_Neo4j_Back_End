package com.VisNeo4j.App.Problems.Knapsack;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.VisNeo4j.App.Utils.Utils;

public class Knapsack01_Ks_12c extends Knapsack01{

	double capacity = 2489815.0;
	
	private List<Integer> profits = Stream.of(744436, 446887, 550596, 191341, 142738, 1571133, 868558, 54288, 1425628, 1318834, 2127868, 422621).collect(Collectors.toList());
	private List<Integer> weights = Stream.of(399907, 241106, 271976, 105019, 65202, 864369, 458263, 27528, 667143, 681262, 982460, 215395).collect(Collectors.toList());
	
	public Knapsack01_Ks_12c() {
		super();
		super.setWeights(Utils.convertToDouble(weights));
		super.setProfits(Utils.convertToDouble(profits));
		super.setCapacity(capacity);
		int var = this.weights.size();
		super.setNumVariables(var);
	}
}
