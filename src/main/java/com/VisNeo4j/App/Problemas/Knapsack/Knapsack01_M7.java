package com.VisNeo4j.App.Problemas.Knapsack;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.VisNeo4j.App.Utils.Utils;

public class Knapsack01_M7 extends Knapsack01{

	double capacity = 16899.0;
	private List<Integer> weights = Stream.of(531,840,497,478,69,464,838,320,642,234
			,343,60,622,671,360,52,404,496,175,653
			,713,420,305,600,143,691,170,824,820,697
			,108,475,268,602,224,665,577,358,820,631
			,796,641,71,588,424,802,375,593,141,189
			,105,631,424,694,158,192,233,505,589,761
			,60,563,152,797,847,759,254,101,392,215
			,808,500,70,441,754,389,682,536,208,641
			,894,359,148,570,221,80,879,624,814,718).collect(Collectors.toList());
	private List<Integer> profits = Stream.of(48,64,33,50,30,55,26,8,9,64
			,6,44,23,48,44,70,47,79,8,3
			,83,86,45,60,27,70,14,41,24,32
			,49,34,14,47,30,67,32,67,66,76
			,71,45,44,3,33,81,69,43,87,27
			,80,67,89,71,13,73,53,52,30,8
			,56,47,26,65,34,9,78,55,8,10
			,67,56,67,28,60,42,80,31,61,2
			,70,12,64,79,40,35,6,77,46,44).collect(Collectors.toList());
	
	public Knapsack01_M7() {
		super();
		super.setWeights(Utils.convertToDouble(weights));
		super.setProfits(Utils.convertToDouble(profits));
		super.setCapacity(capacity);
		int var = this.weights.size();
		super.setNumVariables(var);
	}
}
