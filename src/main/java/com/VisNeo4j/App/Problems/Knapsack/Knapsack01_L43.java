package com.VisNeo4j.App.Problems.Knapsack;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.VisNeo4j.App.Utils.Utils;

public class Knapsack01_L43 extends Knapsack01{

	double capacity = 3820.0;
	private List<Integer> weights = Stream.of(54, 95, 36, 18, 4, 71, 83, 16, 27, 84, 88,
			45, 94, 64, 14, 80, 4,23, 75, 36, 90, 20, 77, 32, 58,
			6, 14, 86, 84, 59, 71, 21, 30, 22, 96, 49, 81, 48, 37,
			28, 6, 84, 19, 55, 88, 38, 51, 52, 79, 55, 70, 53, 64,
			99, 61, 86, 1, 64, 32, 60, 42, 45, 34, 22,49, 37, 33,
			1, 78, 43, 85, 24, 96, 32, 99, 57, 23, 8, 10, 74, 59,
			89, 95, 40, 46, 65, 6, 89, 84, 83, 6, 19, 45, 59, 26,
			13, 8, 26, 5, 9).collect(Collectors.toList());
	private List<Integer> profits = Stream.of(297, 295, 293, 292, 291, 289, 284, 284, 283,
			283, 281, 280, 279, 277, 276, 275, 273, 264, 260,
			257, 250, 236, 236, 235, 235, 233, 232, 232, 228,
			218, 217, 214, 211, 208, 205, 204, 203, 201, 196,
			194, 193, 193, 192, 191, 190, 187, 187, 184, 184,
			184, 181, 179, 176, 173, 172, 171, 160, 128, 123,
			114, 113, 107, 105, 101, 100, 100, 99, 98, 97, 94,
			94, 93, 91, 80, 74, 73, 72, 63, 63, 62, 61, 60, 56, 53,
			52, 50, 48, 46, 40, 40, 35, 28, 22, 22, 18, 15, 12, 11,
			6, 5).collect(Collectors.toList());
	
	public Knapsack01_L43() {
		super();
		super.setWeights(Utils.convertToDouble(weights));
		super.setProfits(Utils.convertToDouble(profits));
		super.setCapacity(capacity);
		int var = this.weights.size();
		super.setNumVariables(var);
	}
}
