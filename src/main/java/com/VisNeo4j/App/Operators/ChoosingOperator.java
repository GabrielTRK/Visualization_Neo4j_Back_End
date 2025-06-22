package com.VisNeo4j.App.Operators;

import java.util.ArrayList;
import java.util.List;

import com.VisNeo4j.App.Model.Particle;
import com.VisNeo4j.App.Model.Population;
import com.VisNeo4j.App.Utils.Utils;

public class ChoosingOperator {

	public ChoosingOperator() {
		
	}
	
	//Se eligen de forna aleatoria 2 individuos para cruzarlos
	public List<Particle> seleccionAleatoria(Population p){
		int rand1 = Utils.getRandNumber(0, p.getnumParticles());
		int rand2 = Utils.getRandNumber(0, p.getnumParticles());
		while(rand1 == rand2) {
			rand2 = Utils.getRandNumber(0, p.getnumParticles());
		}
		
		List<Particle> padres = new ArrayList<>(2);
		padres.add(p.getPopulation().get(rand1));
		padres.add(p.getPopulation().get(rand2));
		return padres;
	}
	
	public List<Particle> seleccionAleatoriaElitista(List<Particle> frente){
		
		int rand1 = Utils.getRandNumber(0, frente.size());
		int rand2 = Utils.getRandNumber(0, frente.size());
		while(rand1 == rand2) {
			rand2 = Utils.getRandNumber(0, frente.size());
		}
		
		List<Particle> padres = new ArrayList<>(2);
		padres.add(frente.get(rand1));
		padres.add(frente.get(rand2));
		return padres;
	}
	
	
	public List<Particle> seleccionPorTorneoNSGAIII(Population p) {
		List<Particle> padres = new ArrayList<>(2);
		for(int i = 0; i < 2; i++) {
			int rand1 = Utils.getRandNumber(0, p.getnumParticles());
			int rand2 = Utils.getRandNumber(0, p.getnumParticles());
			while(rand1 == rand2) {
				rand2 = Utils.getRandNumber(0, p.getnumParticles());
			}
			if(!p.getPopulation().get(rand1).isFeasible() && !p.getPopulation().get(rand2).isFeasible()) {
				if(p.getPopulation().get(rand1).getConstraintViolation() < p.getPopulation().get(rand2).getConstraintViolation()) {
					padres.add(p.getPopulation().get(rand1));
				} else if(p.getPopulation().get(rand1).getConstraintViolation() > p.getPopulation().get(rand2).getConstraintViolation()) {
					padres.add(p.getPopulation().get(rand2));
				}else {
					if(Utils.getRandBinNumber() == 0.0) {
						padres.add(p.getPopulation().get(rand1));
					}else {
						padres.add(p.getPopulation().get(rand2));
					}
				}
			}else if(p.getPopulation().get(rand1).isFeasible() && !p.getPopulation().get(rand2).isFeasible()) {
				padres.add(p.getPopulation().get(rand1));
			}else if(!p.getPopulation().get(rand1).isFeasible() && p.getPopulation().get(rand2).isFeasible()){
				padres.add(p.getPopulation().get(rand2));
			}else{
				if(Utils.getRandBinNumber() == 0.0) {
					padres.add(p.getPopulation().get(rand1));
				}else {
					padres.add(p.getPopulation().get(rand2));
				}
			}
		}
		
		return padres;
	}
	
}
