package com.VisNeo4j.App.Problems.Knapsack;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.VisNeo4j.App.Utils.Utils;

public class Knapsack01_KP2_100_1000_1 extends Knapsack01{
	
	double capacity = 995.0;
	private List<Integer> weights = Stream.of(485, 326, 248, 421, 322, 795, 43, 845, 955, 252, 9, 901, 122, 94, 738, 574, 715, 882, 367, 984, 299, 433, 682, 72, 874, 138, 856, 145, 995, 529, 199, 277, 97, 719, 242, 107, 122, 70, 98, 600, 645, 267, 972, 895, 213, 748, 487, 923, 29, 674, 540, 554, 467, 46, 710, 553, 191, 724, 730, 988, 90, 340, 549, 196, 865, 678, 570, 936, 722, 651, 123, 431, 508, 585, 853, 642, 992, 725, 286, 812, 859, 663, 88, 179, 187, 619, 261, 846, 192, 261, 514, 886, 530, 849, 294, 799, 391, 330, 298, 790).collect(Collectors.toList());
	private List<Integer> profits = Stream.of(482, 257, 286, 517, 404, 713, 45, 924, 873, 160, 1, 838, 40, 58, 676, 627, 766, 862, 405, 923, 379, 461, 612, 133, 813, 97, 908, 165, 996, 623, 220, 298, 157, 723, 144, 48, 129, 148, 35, 644, 632, 272, 1040, 977, 312, 778, 567, 965, 1, 616, 569, 628, 493, 76, 733, 575, 288, 775, 723, 912, 64, 354, 565, 210, 922, 775, 566, 934, 626, 742, 194, 485, 483, 617, 876, 653, 896, 652, 220, 727, 900, 563, 56, 157, 280, 537, 284, 920, 124, 239, 459, 931, 504, 910, 382, 795, 485, 351, 289, 865).collect(Collectors.toList());
	
	public Knapsack01_KP2_100_1000_1() {
		super();
		super.setWeights(Utils.convertToDouble(weights));
		super.setProfits(Utils.convertToDouble(profits));
		super.setCapacity(capacity);
		int var = this.weights.size();
		super.setNumVariables(var);
	}

}
