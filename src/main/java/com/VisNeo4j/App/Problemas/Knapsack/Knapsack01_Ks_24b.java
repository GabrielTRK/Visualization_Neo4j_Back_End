package com.VisNeo4j.App.Problemas.Knapsack;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.VisNeo4j.App.Utils.Utils;

public class Knapsack01_Ks_24b extends Knapsack01{
	
	double capacity = 5971071.0;
	
	private List<Integer> weights = Stream.of(763783, 914370, 831207, 100284, 487486, 182921, 369532, 727094, 272162, 227474, 170387, 697344, 266796, 41169, 937876, 883615, 923090, 385246, 839734, 423575, 33377, 975193, 69579, 418849).collect(Collectors.toList());
	private List<Integer> profits = Stream.of(1559844, 1724668, 1548321, 186455, 1012952, 337633, 806295, 1329837, 546869, 446874, 316848, 1399126, 578882, 79486, 1816706, 1665934, 1917601, 770200, 1732704, 806240, 65751, 1870594, 140713, 859568).collect(Collectors.toList());
	
	public Knapsack01_Ks_24b() {
		super();
		super.setWeights(Utils.convertToDouble(weights));
		super.setProfits(Utils.convertToDouble(profits));
		super.setCapacity(capacity);
		int var = this.weights.size();
		super.setNumVariables(var);
	}

}
