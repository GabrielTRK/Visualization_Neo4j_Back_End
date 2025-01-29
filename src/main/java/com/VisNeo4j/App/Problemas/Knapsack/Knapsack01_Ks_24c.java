package com.VisNeo4j.App.Problemas.Knapsack;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.VisNeo4j.App.Utils.Utils;

public class Knapsack01_Ks_24c extends Knapsack01{
	
double capacity = 5870470.0;
	
	private List<Integer> weights = Stream.of(425855, 348636, 526169, 332447, 441002, 924697, 383512, 696754, 345706, 295567, 606800, 502532, 67352, 991656, 779035, 244102, 623815, 460346, 44551, 774214, 215619, 914117, 568645, 227812).collect(Collectors.toList());
	private List<Integer> profits = Stream.of(911193, 699561, 1049934, 675119, 942922, 1911983, 766504, 1428013, 665388, 601640, 1126415, 1004011, 135369, 2158735, 1516949, 505363, 1160279, 1003942, 81812, 1654345, 455207, 1774312, 1245431, 422075).collect(Collectors.toList());
	
	public Knapsack01_Ks_24c() {
		super();
		super.setWeights(Utils.convertToDouble(weights));
		super.setProfits(Utils.convertToDouble(profits));
		super.setCapacity(capacity);
		int var = this.weights.size();
		super.setNumVariables(var);
	}

}
