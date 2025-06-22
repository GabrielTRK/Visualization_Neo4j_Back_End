package com.VisNeo4j.App.Operators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

import com.VisNeo4j.App.Model.Particle;
import com.VisNeo4j.App.Model.Population;
import com.VisNeo4j.App.Model.ReferencePoint;
import com.VisNeo4j.App.Problems.Problem;
import com.VisNeo4j.App.Utils.Utils;

public class ReplacementOperator {
	
	//Parámetros del Operador de Reemplazo

	private List<List<Particle>> frentesDePareto;
	private int solutionsToSelect;
	private List<ReferencePoint> referencePoints;
	
	public ReplacementOperator(int numberOfObjectives, List<ReferencePoint> referencePoints) {
		this.referencePoints = referencePoints;
	}
	
	//Obtiene el ranking de No Dominancia a partir de los frentes de pareto encontrados
	public List<List<Particle>> obtenerFrentes(Population total, Problem prob){
		
		List<List<Particle>> frentesDePareto = new ArrayList<>();
		
		this.rankingNoDominancia(total, prob);
		while(total.getPopulation().size() != 0) {
			List<Particle> frenteTemp = Utils.obtenerFrenteConIndice(total, 0);
			total = Utils.borrarElementosDeLista(frenteTemp, total);
			frentesDePareto.add(frenteTemp);
			this.rankingNoDominancia(total, prob);
		}
		this.frentesDePareto = frentesDePareto;
		return this.frentesDePareto;
	}
	
	public List<List<Particle>> obtenerFrentes(List<Particle> total, Problem prob){
		
		List<List<Particle>> frentesDePareto = new ArrayList<>();
		
		this.rankingNoDominancia(total, prob);
		while(total.size() != 0) {
			List<Particle> frenteTemp = Utils.obtenerFrenteConIndice(total, 0);
			total = Utils.borrarElementosDeLista(frenteTemp, total);
			frentesDePareto.add(frenteTemp);
			this.rankingNoDominancia(total, prob);
		}
		this.frentesDePareto = frentesDePareto;
		return this.frentesDePareto;
	}
	
	public List<Particle> obtenerPrimerFrente(Population total, Problem prob){
		
		List<Particle> frenteDePareto = new ArrayList<>();
		
		this.rankingNoDominanciaNuevo(total, prob);
		frenteDePareto = Utils.obtenerFrenteConIndice(total, 0);
		
		return frenteDePareto;
	}
	
	public List<Particle> obtenerPrimerFrente(List<Particle> total, Problem prob){
		
		List<Particle> frenteDePareto = new ArrayList<>();
		
		this.rankingNoDominanciaNuevo(total, prob);
		frenteDePareto = Utils.obtenerFrenteConIndice(total, 0);
		
		return frenteDePareto;
	}
	
	//Para cada individuo calcula cuántos individuos lo dominan
	public Population rankingNoDominancia(Population p, Problem prob) {
		for (int i = 0; i < p.getPopulation().size(); i++) {
			int domina = 0;
			Particle a = p.getPopulation().get(i);
			for (int j = 0; j < p.getPopulation().size(); j++) {
				if (i != j) {
					Particle b = p.getPopulation().get(j);
					if(esDominante(b, a, prob)) {
						domina++;
					}
				}
			}
			a.setdomina(domina);
		}
		List<Particle> listaOrden = p.getPopulation();
		Collections.sort(listaOrden);
		p.setPopulation(listaOrden);
		return p;
	}
	
	public List<Particle> rankingNoDominancia(List<Particle> p, Problem prob) {
		for (int i = 0; i < p.size(); i++) {
			int domina = 0;
			Particle a = p.get(i);
			for (int j = 0; j < p.size(); j++) {
				if (i != j) {
					Particle b = p.get(j);
					if(esDominante(b, a, prob)) {
						domina++;
					}
				}
			}
			a.setdomina(domina);
		}
		List<Particle> listaOrden = p;
		Collections.sort(listaOrden);
		p = listaOrden;
		return p;
	}
	
	public Population rankingNoDominanciaNuevo(Population p, Problem prob) {
		for (int i = 0; i < p.getPopulation().size(); i++) {
			int domina = 0;
			Particle a = p.getPopulation().get(i);
			int j = 0;
			while (domina == 0 && j < p.getPopulation().size()) {
				if (i != j) {
					Particle b = p.getPopulation().get(j);
					if(esDominante(b, a, prob)) {
						domina++;
					}
				}
				j++;
			}
			a.setdomina(domina);
		}
		List<Particle> listaOrden = p.getPopulation();
		Collections.sort(listaOrden);
		p.setPopulation(listaOrden);
		return p;
	}
	
	public List<Particle> rankingNoDominanciaNuevo(List<Particle> p, Problem prob) {
		for (int i = 0; i < p.size(); i++) {
			int domina = 0;
			Particle a = p.get(i);
			int j = 0;
			while (domina == 0 && j < p.size()) {
				if (i != j) {
					Particle b = p.get(j);
					if(esDominante(b, a, prob)) {
						domina++;
					}
				}
				j++;
			}
			a.setdomina(domina);
		}
		List<Particle> listaOrden = p;
		Collections.sort(listaOrden);
		p = listaOrden;
		return p;
	}
	
	//Definición de dominancia. En la función, "a" domina a "b"
	private boolean esDominante (Particle a, Particle b, Problem prob) {
		int mEstricto = 0;
		int mOIgual = 0;
		if(a.isFeasible() && b.isFeasible()) {
			for (int i = 0; i < prob.getNumObjetivos(); i++) {
				if(prob.getMinOMax().get(i)) {
					if(a.getObjectives().get(i) < b.getObjectives().get(i) && mEstricto == 0) {
						mEstricto = 1;
					}
					if (a.getObjectives().get(i) <= b.getObjectives().get(i)) {
						mOIgual++;
					}
				}else {
					if(a.getObjectives().get(i) > b.getObjectives().get(i) && mEstricto == 0) {
						mEstricto = 1;
					}
					if (a.getObjectives().get(i) >= b.getObjectives().get(i)) {
						mOIgual++;
					}
				}
			}
			if(mEstricto == 1 && mOIgual == prob.getNumObjetivos()) {
				return true;
			} else {
				return false;
			}
		}else if(a.isFeasible() && !b.isFeasible()) {
			return true;
		}else if(!a.isFeasible() && b.isFeasible()) {
			return false;
		}else {
			if(a.getConstraintViolation() < b.getConstraintViolation()) {
				return true;
			}
			else {
				return false;
			}
		}
		
	}
	
	//Inserta los individuos de cada frente según su ranking.
	//En caso de que a un frente le sobren individuos, se aplica el método del hiperplano de Das y Dennis para elegir los individuos que fomenten mayor diversidad
	public Population rellenarPoblacionConFrentes (Population p, 
			Population total, Problem prob) {
		
		List<Particle> nuevaLista = new ArrayList<>(p.getnumParticles());
		for (int i = 0; i < this.frentesDePareto.size(); i++) {
			if(nuevaLista.size() + this.frentesDePareto.get(i).size() <= p.getnumParticles()) {
				Utils.juntarListas(nuevaLista, this.frentesDePareto.get(i));
			}else {
				if(nuevaLista.size() != p.getnumParticles()) {
					this.frentesDePareto = this.frentesDePareto.subList(0, i + 1);
					this.solutionsToSelect = p.getnumParticles() - nuevaLista.size();
					//Metodo del hiperplano
					List<Particle> ultimosMiembros = dasDennis(prob);
					Utils.juntarListas(nuevaLista, ultimosMiembros);
				}
				p.setPopulation(nuevaLista);
				return p;
			}
		}
		p.setPopulation(nuevaLista);
		return p;
	}
	
	private List<Particle> dasDennis(Problem prob){
		//Restarle a cada individuo los valores del punto ideal
		List<Double> punto_ideal = traducirObjetivos(prob);
		//Calcular los puntos extremos con el Achivement Scalarization Function de cada individuo del primer frente
		List<Particle> extreme_points = encontrarPuntosExtremos(prob);
		//Crear hiperplano con lospuntos extremos
	    List<Double> intercepts = construirHiperplano(extreme_points, prob);
	    //Normalizar los valores de funcion objetivo de cada individuo
	    normalizeObjectives(intercepts, punto_ideal, prob);

	    //Asociar cada individuo a un punto de referencia
	    associate();
	    
	    for (ReferencePoint rp : this.referencePoints) {
	    	rp.sort();
	    	this.addToTree(rp);
	    }
	    
	    
	    List<Particle> result = new ArrayList<>();

	    while (result.size() < this.solutionsToSelect) {
	    	final List<ReferencePoint> first = this.referencePointsTree.firstEntry().getValue();
	    	final int min_rp_index = 1 == first.size() ? 0 : Utils.nextInt(0, first.size() - 1);
	    	final ReferencePoint min_rp = first.remove(min_rp_index);
	    	if (first.isEmpty()) this.referencePointsTree.pollFirstEntry();
	    	Particle chosen = SelectClusterMember(min_rp);
	    	if (chosen != null) {
	    		min_rp.AddMember();
	    		this.addToTree(min_rp);
	    		result.add(chosen);
	    	}
	    }
	    
		return result;
	}
	
	private List<Double> traducirObjetivos(Problem prob){
		List<Double> punto_ideal;
		punto_ideal = new ArrayList<>(prob.getNumObjetivos());
		
		//Obtener punto ideal
		for (int f = 0; f < prob.getNumObjetivos(); f += 1) {
			double minf = Double.MAX_VALUE;
		    for (int i = 0; i < this.frentesDePareto.get(0).size(); i += 1) // min values must appear in the first front
		    {
		    	minf = Math.min(minf, this.frentesDePareto.get(0).get(i).getObjectives().get(f));
		    }
		    punto_ideal.add(minf);
		}
		//Inicializar ObjetivosNorm
		for (List<Particle> list : this.frentesDePareto) {
	    	for (Particle i : list) {
	    		i.setObjectivesNorm(Utils.inicializarLista(prob.getNumObjetivos()));
	        }
	    }
		//Restarle el punto ideal a cada valor de funcion objetivo
		for (List<Particle> list : this.frentesDePareto) {
		    	for (Particle i : list) {
		    		List<Double> o = i.getObjectivesNorm();
		    		for (int f = 0; f < prob.getNumObjetivos(); f += 1) {
		    			o.set(f, i.getObjectives().get(f) - punto_ideal.get(f));
		        }
		    }
		}
		
		return punto_ideal;
	}
	
	//Encontrar puntos extremos con el Achivement Scalarization Function de cada individuo
	private List<Particle> encontrarPuntosExtremos (Problem prob){
		List<Particle> extremePoints = new ArrayList<>();
		Particle min_indv = null;
		for (int f = 0; f < prob.getNumObjetivos(); f += 1) {
			double min_ASF = Double.MAX_VALUE;
			for (Particle s : frentesDePareto.get(0)) {
				double asf = ASF(s, f, prob);
				if (asf < min_ASF) {
					min_ASF = asf;
					min_indv = s;
				}
			}

			extremePoints.add(min_indv);
	    }
		return extremePoints;
	}
	
	//Achivement Scalarization Function
	private double ASF(Particle s, int index, Problem prob) {
		double max_ratio = Double.NEGATIVE_INFINITY;
		for (int i = 0; i < prob.getNumObjetivos(); i++) {
			double weight = (index == i) ? 1.0 : 0.000001;
			max_ratio = Math.max(max_ratio, s.getObjectivesNorm().get(i) / weight);
		}
		return max_ratio;
	}
	
	//Construcción del hiperplano con los puntos extremos
	private List<Double> construirHiperplano(List<Particle> extreme_points, Problem prob){
		
		boolean duplicate = false;
		for (int i = 0; !duplicate && i < extreme_points.size(); i += 1) {
			for (int j = i + 1; !duplicate && j < extreme_points.size(); j += 1) {
				duplicate = extreme_points.get(i).equals(extreme_points.get(j));
			}
		}

		List<Double> intercepts = new ArrayList<>();

	    if (duplicate)
	                   
	    {
	    	System.out.println("Duplicado");
	    	for (int f = 0; f < prob.getNumObjetivos(); f += 1) {
	    		intercepts.add(extreme_points.get(f).getObjectives().get(f));
	    	}
	    } else {
	    	// Encontrar la ecuación del hiperplano
	    	List<Double> b = new ArrayList<>();
	    	for (int i = 0; i < prob.getNumObjetivos(); i++) b.add(1.0);

	    	List<List<Double>> A = new ArrayList<>();
	    	for (Particle s : extreme_points) {
	    		List<Double> aux = new ArrayList<>();
	    		for (int i = 0; i < prob.getNumObjetivos(); i++) aux.add(s.getObjectives().get(i));
	    		A.add(aux);
	    	}
	    	List<Double> x = guassianElimination(A, b);

	    	// Encontrar intersecciones
	    	for (int f = 0; f < prob.getNumObjetivos(); f += 1) {
	    		intercepts.add(1.0 / x.get(f));
	    	}
	    }
	    return intercepts;
	}
	
	public List<Double> guassianElimination(List<List<Double>> A, List<Double> b) {
		List<Double> x = new ArrayList<>();

		int N = A.size();
		for (int i = 0; i < N; i += 1) {
			A.get(i).add(b.get(i));
		}

		for (int base = 0; base < N - 1; base += 1) {
			for (int target = base + 1; target < N; target += 1) {
				double ratio = A.get(target).get(base) / A.get(base).get(base);
				for (int term = 0; term < A.get(base).size(); term += 1) {
					A.get(target).set(term, A.get(target).get(term) - A.get(base).get(term) * ratio);
				}
			}
		}

		for (int i = 0; i < N; i++) x.add(0.0);

		for (int i = N - 1; i >= 0; i -= 1) {
			for (int known = i + 1; known < N; known += 1) {
				A.get(i).set(N, A.get(i).get(N) - A.get(i).get(known) * x.get(known));
			}
			x.set(i, A.get(i).get(N) / A.get(i).get(i));
		}
		return x;
	 }
	
	//Normalizacion de objetivos mediante la division por los puntos de interseccion traducidos
	public void normalizeObjectives(List<Double> intercepts, List<Double> ideal_point, Problem prob) {
		for (int t = 0; t < frentesDePareto.size(); t += 1) {
			for (Particle s : frentesDePareto.get(t)) {

				for (int f = 0; f < prob.getNumObjetivos(); f++) {
					List<Double> conv_obj = s.getObjectivesNorm();
					if (Math.abs(intercepts.get(f) - ideal_point.get(f)) > 10e-10) {
						conv_obj.set(f, conv_obj.get(f) / (intercepts.get(f) - ideal_point.get(f)));
					} else {
						conv_obj.set(f, conv_obj.get(f) / (10e-10));
					}
		        }
		    }
		}
	}
	
	public void associate() {

		for (int t = 0; t < frentesDePareto.size(); t++) {
			for (Particle s : frentesDePareto.get(t)) {
				int min_rp = -1;
				double min_dist = Double.MAX_VALUE;
				for (int r = 0; r < this.referencePoints.size(); r++) {
					double d = perpendicularDistance(this.referencePoints.get(r).position, s.getObjectivesNorm());
					if (d < min_dist) {
						min_dist = d;
						min_rp = r;
					}
				}
				if (t + 1 != frentesDePareto.size()) {
					this.referencePoints.get(min_rp).AddMember();
				} else {
					this.referencePoints.get(min_rp).AddPotentialMember(s, min_dist);
				}
			}
	    }
	}
	
	public double perpendicularDistance(List<Double> direction, List<Double> point) {
		double numerator = 0, denominator = 0;
		for (int i = 0; i < direction.size(); i += 1) {
			numerator += direction.get(i) * point.get(i);
			denominator += Math.pow(direction.get(i), 2.0);
	    }
		double k = numerator / denominator;

		double d = 0;
		for (int i = 0; i < direction.size(); i += 1) {
			d += Math.pow(k * direction.get(i) - point.get(i), 2.0);
		}
		return Math.sqrt(d);
	}
	
	private TreeMap<Integer, List<ReferencePoint>> referencePointsTree = new TreeMap<>();
	
	private void addToTree(ReferencePoint rp) {
		int key = rp.MemberSize();
		if (!this.referencePointsTree.containsKey(key))
			this.referencePointsTree.put(key, new ArrayList<ReferencePoint>());
		this.referencePointsTree.get(key).add(rp);
	}
	
	Particle SelectClusterMember(ReferencePoint rp) {
		Particle chosen = null;
		if (rp.HasPotentialMember()) {
			if (rp.MemberSize() == 0) // currently has no member
			{
				chosen = rp.FindClosestMember();
			} else {
				chosen = rp.RandomMember();
			}
		}
		return chosen;
	}

	public List<List<Particle>> getFrentesDePareto() {
		return frentesDePareto;
	}

	public void setFrentesDePareto(List<List<Particle>> frentesDePareto) {
		this.frentesDePareto = frentesDePareto;
	}
	
}
