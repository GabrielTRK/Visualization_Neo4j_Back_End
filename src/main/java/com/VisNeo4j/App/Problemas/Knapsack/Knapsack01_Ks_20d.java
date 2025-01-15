package com.VisNeo4j.App.Problemas.Knapsack;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.VisNeo4j.App.Utils.Utils;

public class Knapsack01_Ks_20d extends Knapsack01{
	
	double capacity = 4286641.0;
	private List<Integer> profits = Stream.of(1252158, 350292, 1479672, 521323, 50313, 715956, 1462192, 314251, 1319970, 978978, 1743412, 748759, 1274136, 1031710, 890331, 1029238, 174683, 1062778, 250178, 431650).collect(Collectors.toList());
	private List<Integer> weights = Stream.of(637662, 193120, 776715, 262749, 23227, 384111, 768320, 168361, 650172, 453970, 873944, 389217, 579192, 490419, 485659, 478971, 79818, 507772, 134957, 234926).collect(Collectors.toList());
	
	public Knapsack01_Ks_20d() {
		super();
		super.setWeights(Utils.convertToDouble(weights));
		super.setProfits(Utils.convertToDouble(profits));
		super.setCapacity(capacity);
		int var = this.weights.size();
		super.setNumVariables(var);
	}

}
