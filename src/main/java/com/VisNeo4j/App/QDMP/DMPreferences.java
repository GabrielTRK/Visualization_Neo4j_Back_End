package com.VisNeo4j.App.QDMP;

import java.util.ArrayList;
import java.util.List;

import com.VisNeo4j.App.Constantes.Constantes;
import com.VisNeo4j.App.Utils.Utils;

public class DMPreferences {
	
	private ObjectivesOrder order;
	private List<Double> weightsVector;
	
	public DMPreferences(ObjectivesOrder order){
		this.order = order;
	}
	
	public void generateWeightsVector(int numObjetivos){
		List<Double> vector = new ArrayList<>();
		for(int i = 0; i < numObjetivos; i++) {
			vector.add(1.0/numObjetivos);
		}
		this.weightsVector = vector;
		this.adjustWeightsOrder(numObjetivos);
	}
	
	public void adjustWeightsOrder(int numObjetivos) {
		List<Double> newWeights = new ArrayList<>();
		Utils.rellenarListaDeCeros(newWeights, numObjetivos);
		
		for(int i = 0; i < this.order.getOrder().size(); i++) {
			newWeights.set(this.order.getOrder().get(i)-1, this.weightsVector.get(i));
		}
		
		this.weightsVector = newWeights;
	}

	public ObjectivesOrder getOrder() {
		return order;
	}

	public void setOrder(ObjectivesOrder order) {
		this.order = order;
	}

	public List<Double> getWeightsVector() {
		return weightsVector;
	}

	public void setWeightsVector(List<Double> weightsVector) {
		this.weightsVector = weightsVector;
	}
	
	

}
