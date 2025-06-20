package com.VisNeo4j.App.Problems.Knapsack;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.VisNeo4j.App.Utils.Utils;

public class Knapsack01_L2 extends Knapsack01{

	double capacity = 29972.0;
	private List<Integer> weights = Stream.of(751,863,981,975,982,759,829,806,610,546,973,874,768,991,688,546,544,844,712,599,606,549,878,730,746,808,673,879,584,951,905,797,989,703,797,822,917,735,866,722,862,502,593,528,748,997,597,717,752,701,972,579,580,766,651,961,534,952,604,816,720,550,786,746,674,645,569,857,940,613,838,514,962,833,588,618,972,668,658,830,727,651,554,881,939,888,647,635,778,708,732,778,550,998,598,942,740,665,722,506).collect(Collectors.toList());
	private List<Integer> profits = Stream.of(95,27,100,87,40,87,64,90,52,15,94,72,77,30,44,27,83,61,71,32,73,39,22,49,91,39,93,58,51,11,54,56,92,85,86,23,28,54,18,50,41,68,93,85,35,55,62,49,26,67,98,27,56,59,92,43,12,68,95,49,78,71,35,99,71,94,92,83,12,23,42,75,18,69,79,17,89,12,85,54,28,32,23,63,55,79,95,29,80,61,98,51,61,90,36,51,82,88,41,30).collect(Collectors.toList());
	
	public Knapsack01_L2() {
		super();
		super.setWeights(Utils.convertToDouble(weights));
		super.setProfits(Utils.convertToDouble(profits));
		super.setCapacity(capacity);
		int var = this.weights.size();
		super.setNumVariables(var);
	}
}
