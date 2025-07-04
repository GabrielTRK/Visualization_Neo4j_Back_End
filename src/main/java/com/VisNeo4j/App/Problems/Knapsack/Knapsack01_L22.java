package com.VisNeo4j.App.Problems.Knapsack;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.VisNeo4j.App.Utils.Utils;

public class Knapsack01_L22 extends Knapsack01{

	double capacity = 6718.0;
	private List<Integer> weights = Stream.of(54,183,106,82,30,58,71,166,117,190,90,191,205,128,110,89,63,6,140,86,30,91,156,31,70,199,142,98,178,16,140,31,24,197,
			101,73,169, 73,92,159,71,102,144,151,27,131,209,164,177,177,129,146,17,53,164,146,43,170,180,171,130,183,5,113,207,
			57,13,163,20,63,12,24,9,42, 6,109,170,108,46,69,43,175,81,5,34,146,148,114,160,174,156,82,47,126,102,83,58,34,21,14).collect(Collectors.toList());
	private List<Integer> profits = Stream.of(597,596,593,586,581,568,567,560,549,548,547,529,529,527,520,491,482,478,475,475,466,462,459,458,454,451,449,443,
			442,421,410, 409,395,394,390,377,375,366,361,347,334,322,315,313,311,309,296,295,294,289,285,279,277,276,272,248,246,
			245,238,237,232,231, 230,225,192,184,183,176,174,171,169,165,165,154,153,150,149,147,143,140,138,134,132,127,124,
			123,114,111,104,89,74,63,62,58,55,48, 27,22,12,6).collect(Collectors.toList());
	
	public Knapsack01_L22() {
		super();
		super.setWeights(Utils.convertToDouble(weights));
		super.setProfits(Utils.convertToDouble(profits));
		super.setCapacity(capacity);
		int var = this.weights.size();
		super.setNumVariables(var);
	}
}
