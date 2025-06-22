package com.VisNeo4j.App.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Particle implements Comparable<Particle> {
	/*Cada individuo está compuesto por: 
		- Los valores de sus variables
		- Los valores de funcion objetivo
		- Los valores de funcion objetivo normalizados, se usan solo para el proceso de reemplazo
		- El número de individuos que dominan al individuo en cuestión
	*/
	
	private List<Double> variables;
	private int domina = 0;
	private List<Double> objectives;
	private List<Double> objectivesNorm;
	private List<Double> constraints;
	private boolean feasible = true;
	private Double constraintViolation = 0.0;
	
	private List<Double> fitnessHist;
	//private Map<Integer, Double> idObjetivos = new HashMap<>();
	private Map<String, List<Double>> extra;
	
	public Particle(int numVariables, int numObjetives) {
		this.variables = new ArrayList<Double>(numVariables);
		this.objectives = new ArrayList<Double>(numObjetives);
		this.initExtra();
	}

	public List<Double> getVariables() {
		return variables;
	}

	public void setVariables(List<Double> variables) {
		this.variables = variables;
	}
	
	public void setIVariable(int posicion, Double variable) {
		this.variables.add(posicion, variable);
	}
	
	public void modIVariable(int posicion, Double variable) {
		this.variables.set(posicion, variable);
	}

	public List<Double> getObjectives() {
		return objectives;
	}

	public void setObjectives(List<Double> objectives) {
		this.objectives = objectives;
	}
	
	public void addIObjetivo(int pos, Double obj) {
		this.objectives.add(pos, obj);
	}
	
	public void setIObjective(int pos, Double obj) {
		this.objectives.set(pos, obj);
	}

	

	@Override
	public String toString() {
		return "Particle [variables=" + variables + ", objectives=" + objectives + ", objectivesNorm=" + objectivesNorm
				+ ", constraints=" + constraints + ", feasible=" + feasible + ", constraintViolation="
				+ constraintViolation + "]";
	}

	public int getdomina() {
		return domina;
	}

	public void setdomina(int domina) {
		this.domina = domina;
	}

	public List<Double> getObjectivesNorm() {
		return objectivesNorm;
	}

	public void setObjectivesNorm(List<Double> objectivesNorm) {
		this.objectivesNorm = objectivesNorm;
	}

	public List<Double> getConstraints() {
		return constraints;
	}

	public void setConstraints(List<Double> constraints) {
		this.constraints = constraints;
	}

	public boolean isFeasible() {
		return feasible;
	}

	public void setFeasible(boolean feasible) {
		this.feasible = feasible;
	}

	public Double getConstraintViolation() {
		return constraintViolation;
	}

	public void setConstraintViolation(Double constraintViolation) {
		this.constraintViolation = constraintViolation;
	}

	public List<Double> getFitnessHist() {
		return fitnessHist;
	}

	public void setFitnessHist(List<Double> fitnessHist) {
		this.fitnessHist = fitnessHist;
	}
	
	public void initExtra() {
		this.extra = new HashMap<>();
	}

	/*public Map<Integer, Double> getIdObjetivos() {
		return idObjetivos;
	}

	public void setIdObjetivos(Map<Integer, Double> idObjetivos) {
		this.idObjetivos = idObjetivos;
	}*/

	public Map<String, List<Double>> getExtra() {
		return extra;
	}

	public void setExtra(Map<String, List<Double>> extra) {
		this.extra = extra;
	}

	//Se comparan el número de individuos que dominan a 2 individuos para asigarles un ranking
	@Override
	public int compareTo(Particle o) {
		int compareDom = o.getdomina();
		    return this.getdomina() - compareDom;
	}

}
