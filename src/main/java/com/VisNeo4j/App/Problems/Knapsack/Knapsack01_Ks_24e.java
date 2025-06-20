package com.VisNeo4j.App.Problems.Knapsack;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.VisNeo4j.App.Utils.Utils;

public class Knapsack01_Ks_24e extends Knapsack01{
	
double capacity = 6654569.0;
	
	private List<Integer> weights = Stream.of(695093, 430667, 233583, 832832, 422539, 614321, 898576, 371233, 324029, 958551, 381955, 519075, 94236, 835863, 352292, 983000, 286126, 924119, 679730, 229377, 152007, 794048, 566176, 729710).collect(Collectors.toList());
	private List<Integer> profits = Stream.of(1319972, 804470, 487111, 1636416, 915637, 1334476, 1703095, 727928, 588848, 1974348, 818432, 1012735, 181004, 1826340, 736446, 2070580, 597255, 1765740, 1374118, 467036, 310529, 1614819, 1157316, 1370965).collect(Collectors.toList());
	
	public Knapsack01_Ks_24e() {
		super();
		super.setWeights(Utils.convertToDouble(weights));
		super.setProfits(Utils.convertToDouble(profits));
		super.setCapacity(capacity);
		int var = this.weights.size();
		super.setNumVariables(var);
	}

}
