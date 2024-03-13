package com.VisNeo4j.App.QDMP;

import java.util.ArrayList;
import java.util.List;

import com.VisNeo4j.App.Constantes.Constantes;
import com.VisNeo4j.App.Utils.Utils;

public class DMPreferences {
	
	private ObjectivesOrder order;
	private List<Double> weightsVector;
	private String method;
	
	public DMPreferences(ObjectivesOrder order, String method){
		this.order = order;
		this.method = method;
	}
	
	public void generateWeightsVector(int numObjetivos){
		switch(this.method) {
		case Constantes.nombreQDMPSR:
			this.SRMethod(numObjetivos);
		default:
			this.QDMPgeneric(numObjetivos);
		}
	}
	
	public void QDMPgeneric(int numObjetivos){
		List<Double> vector = new ArrayList<>();
		for(int i = 0; i < numObjetivos; i++) {
			vector.add(1.0/numObjetivos);
		}
		this.weightsVector = vector;
		this.adjustWeightsOrder(numObjetivos);
	}
	
	public void SRMethod(int numObjetivos){
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
		this.setWeightsVector(vector);
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
