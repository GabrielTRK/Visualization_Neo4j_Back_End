package com.VisNeo4j.App.Problemas.Knapsack;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.VisNeo4j.App.Utils.Utils;

public class Knapsack01_Ks_20b extends Knapsack01{
	
double capacity = 4681373.0;
	
	private List<Integer> profits = Stream.of(928904, 170787, 1689092, 117966, 632580, 731869, 1185747, 923646, 1617156, 52263, 1501384, 428275, 368772, 1513686, 918075, 903139, 1588130, 220241, 1451995, 1863885).collect(Collectors.toList());
	private List<Integer> weights = Stream.of(426375, 91504, 915455, 60292, 337974, 336679, 566396, 425705, 836379, 27713, 779627, 207304, 174254, 703660, 421993, 450456, 828590, 121896, 719160, 931334).collect(Collectors.toList());
	
	public Knapsack01_Ks_20b() {
		super();
		super.setWeights(Utils.convertToDouble(weights));
		super.setProfits(Utils.convertToDouble(profits));
		super.setCapacity(capacity);
		int var = this.weights.size();
		super.setNumVariables(var);
	}

}
