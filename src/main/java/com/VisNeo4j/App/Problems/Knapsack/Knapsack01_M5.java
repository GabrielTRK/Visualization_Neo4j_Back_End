package com.VisNeo4j.App.Problems.Knapsack;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.VisNeo4j.App.Utils.Utils;

public class Knapsack01_M5 extends Knapsack01{

	double capacity = 9442.0;
	private List<Integer> weights = Stream.of(641,108,209,521,508,524,383,255,51,87
			,173,359,144,326,126,463,130,104,547,532
			,453,415,52,448,591,393,355,292,328,335
			,496,437,549,130,642,122,126,250,606,310
			,349,381,530,373,286,515,455,54,589,432
			,396,344,93,291,67,568,102,273,198,430
			,136,188,292,589,179,51,524,446,561,392).collect(Collectors.toList());
	private List<Integer> profits = Stream.of(11,20,39,44,48,21,28,57,42,40
			,15,53,9,51,4,67,26,66,63,37
			,44,40,57,50,2,67,37,67,1,45
			,42,29,17,66,52,54,57,12,36,69
			,38,13,13,8,65,12,30,49,52,51
			,42,15,39,62,6,25,61,36,58,14
			,24,6,36,14,65,9,11,25,18,7).collect(Collectors.toList());
	
	public Knapsack01_M5() {
		super();
		super.setWeights(Utils.convertToDouble(weights));
		super.setProfits(Utils.convertToDouble(profits));
		super.setCapacity(capacity);
		int var = this.weights.size();
		super.setNumVariables(var);
	}
}
