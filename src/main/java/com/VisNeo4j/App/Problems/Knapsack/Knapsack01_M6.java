package com.VisNeo4j.App.Problems.Knapsack;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.VisNeo4j.App.Utils.Utils;

public class Knapsack01_M6 extends Knapsack01{

	double capacity = 13623.0;
	private List<Integer> weights = Stream.of(470,654,593,783,378,317,546,616,93,643
			,330,334,265,232,519,581,632,650,577,587
			,333,378,342,588,120,764,295,696,539,199
			,276,535,389,119,413,733,799,274,311,505
			,465,644,56,465,336,187,278,756,514,350
			,111,399,773,282,629,66,390,278,783,347
			,328,309,101,596,59,439,166,396,303,443
			,478,172,442,318,730,271,576,87,562,765).collect(Collectors.toList());
	private List<Integer> profits = Stream.of(44,44,33,63,4,53,66,34,10,60
			,55,42,56,74,2,4,48,40,21,7
			,24,7,68,49,76,76,34,60,7,23
			,3,61,30,18,30,59,39,23,12,29
			,29,30,13,36,15,61,51,61,51,21
			,8,33,47,3,38,61,17,24,68,47
			,75,7,76,40,3,80,71,12,44,25
			,54,45,47,14,39,28,35,27,58,4).collect(Collectors.toList());
	
	public Knapsack01_M6() {
		super();
		super.setWeights(Utils.convertToDouble(weights));
		super.setProfits(Utils.convertToDouble(profits));
		super.setCapacity(capacity);
		int var = this.weights.size();
		super.setNumVariables(var);
	}
}
