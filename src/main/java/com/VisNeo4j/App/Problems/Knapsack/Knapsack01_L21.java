package com.VisNeo4j.App.Problems.Knapsack;

import java.util.ArrayList;
import java.util.List;

import com.VisNeo4j.App.Constantes.ConstantesKP01_L21PAA;
import com.VisNeo4j.App.Constantes.ConstantesKP01_L21PAB;
import com.VisNeo4j.App.Constantes.ConstantesKP01_L21PBA;
import com.VisNeo4j.App.Constantes.ConstantesKP01_L21PBB;
import com.VisNeo4j.App.Constantes.ConstantesKP01_L21WAA;
import com.VisNeo4j.App.Constantes.ConstantesKP01_L21WAB;
import com.VisNeo4j.App.Constantes.ConstantesKP01_L21WBA;
import com.VisNeo4j.App.Constantes.ConstantesKP01_L21WBB;
import com.VisNeo4j.App.Utils.Utils;

public class Knapsack01_L21 extends Knapsack01{

	double capacity = 207566020.0;
	private List<Integer> weights = new ArrayList<>();
	private List<Integer> profits = new ArrayList<>();
	
	public Knapsack01_L21() {
		super();
		this.weights.addAll(ConstantesKP01_L21WAA.KP01_L21WAA);
		this.weights.addAll(ConstantesKP01_L21WAB.KP01_L21WAB);
		this.weights.addAll(ConstantesKP01_L21WBA.KP01_L21WBA);
		this.weights.addAll(ConstantesKP01_L21WBB.KP01_L21WBB);
		this.profits.addAll(ConstantesKP01_L21PAA.KP01_L21PAA);
		this.profits.addAll(ConstantesKP01_L21PAB.KP01_L21PAB);
		this.profits.addAll(ConstantesKP01_L21PBA.KP01_L21PBA);
		this.profits.addAll(ConstantesKP01_L21PBB.KP01_L21PBB);
		super.setWeights(Utils.convertToDouble(weights));
		super.setProfits(Utils.convertToDouble(profits));
		super.setCapacity(capacity);
		int var = this.weights.size();
		super.setNumVariables(var);
	}
}
