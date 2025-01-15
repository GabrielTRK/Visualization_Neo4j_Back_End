package com.VisNeo4j.App.Problemas.Knapsack;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.VisNeo4j.App.Utils.Utils;

public class Knapsack01_L1 extends Knapsack01{

	double capacity = 21963.0;
	private List<Integer> weights = Stream.of(145,462,434,849,921,435,636,753,994,151
			,277,128,396,860,863,957,618,637,644,951
			,941,485,455,290,514,130,791,452,141,917
			,130,148,532,159,792,699,663,526,981,271
			,802,707,334,662,858,173,377,375,507,447
			,303,849,165,914,994,766,146,679,595,533
			,137,697,867,638,146,615,413,134,980,918
			,255,796,372,283,726,778,648,75,816,196
			,904,56,937,168,269,882,203,229,565,88
			,83,1000,810,744,507,987,712,985,585,389).collect(Collectors.toList());
	private List<Integer> profits = Stream.of(16,28,12,74,20,25,86,78,16,29
			,54,99,84,78,29,15,7,100,82,63
			,11,33,17,98,98,36,36,67,31,96
			,71,40,28,44,79,68,48,7,10,44
			,31,65,62,84,56,25,3,92,6,83
			,82,63,15,17,45,45,3,85,19,37
			,32,60,68,41,71,43,81,74,77,64
			,22,33,21,9,92,30,84,29,100,8
			,61,69,44,85,63,65,20,59,5,72
			,9,4,9,48,26,25,59,45,78,86).collect(Collectors.toList());
	
	public Knapsack01_L1() {
		super();
		super.setWeights(Utils.convertToDouble(weights));
		super.setProfits(Utils.convertToDouble(profits));
		super.setCapacity(capacity);
		int var = this.weights.size();
		super.setNumVariables(var);
	}
}
