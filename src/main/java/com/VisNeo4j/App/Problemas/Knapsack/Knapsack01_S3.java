package com.VisNeo4j.App.Problemas.Knapsack;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class Knapsack01_S3 extends Knapsack01{
	
	double capacity = 375.0;
	private List<Double> weights = Stream.of (56.358531, 80.874050, 47.987304,
			89.596240, 74.660482, 85.894345,
			51.353496, 1.498459, 36.445204,
			16.589862, 44.569231, 0.466933,
			37.788018, 57.118442, 60.716575).collect(Collectors.toList());
	private List<Double> profits = Stream.of(0.125126, 19.330424,
			58.500931, 35.029145, 82.284005,
			17.410810, 71.050142, 30.399487,
			9.140294, 14.731285, 98.852504,
			11.908322, 0.891140, 53.166295,
			60.176397).collect(Collectors.toList());
	
	public Knapsack01_S3() {
		super();
		super.setWeights(weights);
		super.setProfits(profits);
		super.setCapacity(capacity);
		int var = this.weights.size();
		super.setNumVariables(var);
	}

}
