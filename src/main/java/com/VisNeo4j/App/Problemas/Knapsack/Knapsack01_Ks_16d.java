package com.VisNeo4j.App.Problemas.Knapsack;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.VisNeo4j.App.Utils.Utils;

public class Knapsack01_Ks_16d extends Knapsack01{
	
	double capacity = 4450938.0;
	private List<Integer> profits = Stream.of(319363, 924519, 1016542, 1387079, 1840227, 1346660, 1515021, 1572948, 37076, 1702673, 1623789, 1968433, 1004311, 221324, 956448, 692230).collect(Collectors.toList());
	private List<Integer> weights = Stream.of(169122, 435204, 480076, 638597, 906325, 613953, 713858, 817993, 18612, 819650, 867495, 992711, 509674, 103140, 481247, 334219).collect(Collectors.toList());
	
	public Knapsack01_Ks_16d() {
		super();
		super.setWeights(Utils.convertToDouble(weights));
		super.setProfits(Utils.convertToDouble(profits));
		super.setCapacity(capacity);
		int var = this.weights.size();
		super.setNumVariables(var);
	}

}
