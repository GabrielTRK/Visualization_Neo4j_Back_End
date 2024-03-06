package com.VisNeo4j.App.QDMP;

import java.util.ArrayList;
import java.util.List;

public class SR extends DMPreferences{
	
	public SR(ObjectivesOrder order) {
		super(order);
	}



	@Override
	public void generateWeightsVector(int numObjetivos){
		List<Double> vector = new ArrayList<>();
		Double objetivoi;
		Double denominador;
		for(double i = 1.0; i <= numObjetivos; i++) {
			objetivoi = 0.0;
			denominador = 0.0;
			
			for(double j = 1.0; j <= numObjetivos; j++) {
				denominador += (1.0/j) + ((numObjetivos+1.0-j) / numObjetivos);
			}
			
			objetivoi = ( (1.0/i) + ( (numObjetivos+1.0-i)/(numObjetivos) ) ) /  denominador ;
			
			vector.add(objetivoi);
		}
		super.setWeightsVector(vector);
		super.adjustWeightsOrder(numObjetivos);
	}
}
