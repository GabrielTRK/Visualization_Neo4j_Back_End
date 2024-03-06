package com.VisNeo4j.App.Problemas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.VisNeo4j.App.Constantes.Constantes;
import com.VisNeo4j.App.Modelo.DatosProblemaDias;
import com.VisNeo4j.App.Modelo.DatosRRPS_PAT;
import com.VisNeo4j.App.Modelo.Individuo;
import com.VisNeo4j.App.QDMP.DMPreferences;
import com.VisNeo4j.App.Utils.Utils;

public class RRPS_PAT extends Problema{
	
	private Double resInf = 0.0;
	private Double resSup;
	private DatosRRPS_PAT datos;
	private int numInicializaciones = 0;
	private DMPreferences preferencias;

	public RRPS_PAT(DatosRRPS_PAT datos, Double maxRiesgo, DMPreferences preferencias) {
		super(0, 1);
		int numConexionesTotales = 0;
		for (int i = 0; i < datos.getDatosPorDia().size(); i++) {
			numConexionesTotales += datos.getDatosPorDia().get(i).getConexiones().size()
					- datos.getDatosPorDia().get(i).getDireccionesAMantener().size();
		}
		super.setNumVariables(numConexionesTotales);
		this.datos = datos;
		this.resSup = maxRiesgo;
		this.preferencias = preferencias;
		super.setNombre(Constantes.nombreProblemaRRPS_PAT);
	}
	
	@Override
	public Individuo evaluate(Individuo ind) {
		List<Double> aux = ind.getVariables();

		for (int i = 0; i < this.datos.getDatosPorDia().size(); i++) {
			this.rellenarDireccionesDia(i, aux);
		}
		ind.setVariables(aux);
		
		List<Double> objetivos = this.calcularObjetivos(ind);

		
		List<Double> restricciones = new ArrayList<>(1);

		restricciones.add(0, objetivos.get(0));
		ind.setRestricciones(restricciones);
		this.comprobarRestricciones(ind);
		ind.setObjetivosNorm(objetivos.subList(1, objetivos.size()));
		Double sumaPesos = 0.0;

		for (int i = 1; i < objetivos.size(); i++) {
			sumaPesos += objetivos.get(i) * this.preferencias.getWeightsVector().get(i - 1);
		}

		ind.setObjetivos(List.of(sumaPesos));

		int cont = 0;
		for (int i = 0; i < this.datos.getDatosPorDia().size(); i++) {
			cont = this.quitarDireccionesDia(i, aux, cont);
		}

		ind.setVariables(aux);
		
		
		

		return ind;
	}
	
	private List<Double> calcularObjetivos(Individuo solucion){
		List<Double> objetivos = new ArrayList<>();
		Double Riesgosumatorio = 0.0;
        Double RiesgosumatorioTotal = 0.0;
        
        Double IngresosTsuma = 0.0;
        Double IngresosTtotalSuma = 0.0;
        
        Double Pasajerossumatorio = 0.0;
        Double Pasajerostotal = 0.0;
        
        Double Tasassumatorio = 0.0;
        Double Tasastotal = 0.0;
        
        String origen = "";
		String destino = "";
		
		Double Conectividadsuma = 0.0;
        Double ConectividadtotalSuma = 0.0;
        Double Conectividadsolucion = 0.0;
        
        Double ConectividadauxSuma = 0.0;
        Double ConectividadauxTotalSuma = 0.0;
        
        Map<String, List<Double>> pasajerosPorCompanyia = new HashMap<>();
        Map<String, List<Double>> ingresosPorAreaInf = new HashMap<>();
        Map<String, List<Double>> ingresosPorAerDest = new HashMap<>();
		
        int pos = 0;
		for(int i = 0; i < this.datos.getConexionesTotales().size(); i++) {
			while(pos < this.datos.getConexionesTotalesSeparadas().size() && 
					this.datos.getConexionesTotalesSeparadas().get(pos).get(0).equals(this.datos.getConexionesTotales().get(i).get(0)) && 
					this.datos.getConexionesTotalesSeparadas().get(pos).get(1).equals(this.datos.getConexionesTotales().get(i).get(1))) {
				Riesgosumatorio += this.datos.getRiesgos().get(pos) * 
	        			solucion.getVariables().get(i);
	            RiesgosumatorioTotal += this.datos.getRiesgos().get(pos);
	            
	            IngresosTsuma += this.datos.getIngresos().get(pos) * 
	        			solucion.getVariables().get(i);
	        	IngresosTtotalSuma += this.datos.getIngresos().get(pos);
	            
	        	Pasajerossumatorio += this.datos.getPasajeros().get(pos) * 
	            		solucion.getVariables().get(i);
	            Pasajerostotal += this.datos.getPasajeros().get(pos);
	            
	            Tasassumatorio += this.datos.getTasas().get(pos) * 
	            		solucion.getVariables().get(i);
	            Tasastotal += this.datos.getTasas().get(pos);
	            
	            if(pasajerosPorCompanyia.get(this.datos.getCompanyiasTotales().get(pos)) != null) {
	            	pasajerosPorCompanyia.put(this.datos.getCompanyiasTotales().get(pos), 
	            			List.of(pasajerosPorCompanyia.get(this.datos.getCompanyiasTotales().get(pos)).get(0) + this.datos.getPasajeros().get(pos) * 
	        	            		solucion.getVariables().get(i), 
	        	            		pasajerosPorCompanyia.get(this.datos.getCompanyiasTotales().get(pos)).get(1) + this.datos.getPasajeros().get(pos)));
	            }else {
	            	pasajerosPorCompanyia.put(this.datos.getCompanyiasTotales().get(pos), 
	            			List.of(this.datos.getPasajeros().get(pos) * 
	        	            		solucion.getVariables().get(i), 
	        	            		this.datos.getPasajeros().get(pos) * 1.0));
	            }
	            if(ingresosPorAreaInf.get(this.datos.getAresInfTotales().get(pos)) != null) {
	            	ingresosPorAreaInf.put(this.datos.getAresInfTotales().get(pos), 
	            			List.of(ingresosPorAreaInf.get(this.datos.getAresInfTotales().get(pos)).get(0) + this.datos.getIngresos().get(pos) * 
	            					solucion.getVariables().get(i), 
	            					ingresosPorAreaInf.get(this.datos.getAresInfTotales().get(pos)).get(1) + this.datos.getIngresos().get(pos)));
	            }else {
	            	ingresosPorAreaInf.put(this.datos.getAresInfTotales().get(pos), 
	            			List.of(this.datos.getIngresos().get(pos) * 
	            					solucion.getVariables().get(i), 
	            					this.datos.getIngresos().get(pos)));
	            }
	            if(ingresosPorAerDest.get(this.datos.getConexionesTotalesSeparadas().get(pos).get(1)) != null) {
	            	ingresosPorAerDest.put(this.datos.getConexionesTotalesSeparadas().get(pos).get(1), 
	            			List.of(ingresosPorAerDest.get(this.datos.getConexionesTotalesSeparadas().get(pos).get(1)).get(0) + this.datos.getTasas().get(pos) * 
	        	            		solucion.getVariables().get(i), 
	        	            		ingresosPorAerDest.get(this.datos.getConexionesTotalesSeparadas().get(pos).get(1)).get(0) + this.datos.getTasas().get(pos)));
	            }else {
	            	ingresosPorAerDest.put(this.datos.getConexionesTotalesSeparadas().get(pos).get(1), 
	            			List.of(this.datos.getTasas().get(pos) * 
	        	            		solucion.getVariables().get(i), 
	        	            		this.datos.getTasas().get(pos)));
	            }
	            
	        	
	            pos++;
			}
			
			if(!this.datos.getConexionesTotales().get(i).get(0).equals(origen)) {
				if(i > 0) {
					Double Conectividadaux = 0.0;
		            if (ConectividadauxTotalSuma != 0) {
		            	Conectividadaux = ConectividadauxSuma / ConectividadauxTotalSuma;
		            }
		            Conectividadsuma += this.datos.getConectividadesTotales().get(i-1) * (1 - Conectividadaux);
		            ConectividadtotalSuma += this.datos.getConectividadesTotales().get(i-1);
				}
				//Sumar las conectividades de los destinos y guardar el origen
				origen = this.datos.getConexionesTotales().get(i).get(0);
				ConectividadauxSuma = 0.0;
				ConectividadauxTotalSuma = 0.0;
			}
			//origen = this.datos.getConexionesTotales().get(i).get(0);
			ConectividadauxSuma += solucion.getVariables().get(i)
            		* this.datos.getVuelosEntrantesConexionOrdenadoTotales().get(i);
			ConectividadauxTotalSuma += this.datos.getVuelosEntrantesConexionOrdenadoTotales().get(i);
		}
		
		
		Double Ingresosaux = 0.0;
        if (IngresosTtotalSuma != 0.0) {
            Ingresosaux = IngresosTsuma / IngresosTtotalSuma;
        }
        Double Pasajerosporcentaje = 1 - Pasajerossumatorio / Pasajerostotal;
        Double Tasasporcentaje = 1 - Tasassumatorio / Tasastotal;
        
        Double Conectividadaux = 0.0;
        if (ConectividadauxTotalSuma != 0) {
        	Conectividadaux = ConectividadauxSuma / ConectividadauxTotalSuma;
        }
        Conectividadsuma += this.datos.getConectividadesTotales().get(this.datos.getConexionesTotales().size()-1) * (1 - Conectividadaux);
        ConectividadtotalSuma += this.datos.getConectividadesTotales().get(this.datos.getConexionesTotales().size()-1);
		
		if (ConectividadtotalSuma != 0) {
			Conectividadsolucion = Conectividadsuma / ConectividadtotalSuma;
        }
		
		double pasajerosPorCompanyiaMediaPerdida = 0.0;
		for(String companyia : pasajerosPorCompanyia.keySet()) {
			pasajerosPorCompanyiaMediaPerdida += 1 - (pasajerosPorCompanyia.get(companyia).get(0) / 
					pasajerosPorCompanyia.get(companyia).get(1));
		}
		pasajerosPorCompanyiaMediaPerdida = pasajerosPorCompanyiaMediaPerdida / pasajerosPorCompanyia.keySet().size();
		double pasajerosPorCompanyiaDesvTipPerdida = 0.0;
		for(String companyia : pasajerosPorCompanyia.keySet()) {
			pasajerosPorCompanyiaDesvTipPerdida += Math.pow((1 - pasajerosPorCompanyia.get(companyia).get(0) / pasajerosPorCompanyia.get(companyia).get(1)) - pasajerosPorCompanyiaMediaPerdida,2);
		}
		pasajerosPorCompanyiaDesvTipPerdida /= pasajerosPorCompanyia.keySet().size();
		pasajerosPorCompanyiaDesvTipPerdida = Math.sqrt(pasajerosPorCompanyiaDesvTipPerdida);
		
		
		double ingresoPerdidoAreasInfMedia = 0.0;
		for(String areaInf : ingresosPorAreaInf.keySet()) {
			ingresoPerdidoAreasInfMedia += 1 - (ingresosPorAreaInf.get(areaInf).get(0) / 
					ingresosPorAreaInf.get(areaInf).get(1));
		}
		ingresoPerdidoAreasInfMedia = ingresoPerdidoAreasInfMedia / ingresosPorAreaInf.keySet().size();
		double ingresoPerdidoAreasInfDesvTip = 0.0;
		for(String areaInf : ingresosPorAreaInf.keySet()) {
			ingresoPerdidoAreasInfDesvTip += Math.pow((1 - ingresosPorAreaInf.get(areaInf).get(0) / ingresosPorAreaInf.get(areaInf).get(1)) - ingresoPerdidoAreasInfMedia,2);
		}
		ingresoPerdidoAreasInfDesvTip /= ingresosPorAreaInf.keySet().size();
		ingresoPerdidoAreasInfDesvTip = Math.sqrt(ingresoPerdidoAreasInfDesvTip);
		
		
		double ingresoPorAerDestMedia = 0.0;
		for(String aeropuerto : ingresosPorAerDest.keySet()) {
			if(ingresosPorAerDest.get(aeropuerto).get(1) == 0.0) {
				ingresoPorAerDestMedia += 0.0;
			}else {
				ingresoPorAerDestMedia += 1 - (ingresosPorAerDest.get(aeropuerto).get(0) /
						ingresosPorAerDest.get(aeropuerto).get(1));
			}
			
		}
		ingresoPorAerDestMedia = ingresoPorAerDestMedia / ingresosPorAerDest.keySet().size();
		double ingresoPorAerDestDesvTip = 0.0;
		for(String aeropuerto : ingresosPorAerDest.keySet()) {
			if(ingresosPorAerDest.get(aeropuerto).get(1) == 0.0) {
				ingresoPorAerDestDesvTip += Math.pow(0.0 - ingresoPorAerDestMedia,2);
			}else {
				ingresoPorAerDestDesvTip += Math.pow((1 - ingresosPorAerDest.get(aeropuerto).get(0) / ingresosPorAerDest.get(aeropuerto).get(1)) - ingresoPorAerDestMedia,2);
			}
			
		}
		ingresoPorAerDestDesvTip /= ingresosPorAerDest.keySet().size();
		ingresoPorAerDestDesvTip = Math.sqrt(ingresoPorAerDestDesvTip);
		
		objetivos.add(Riesgosumatorio / RiesgosumatorioTotal);//Riesgo
		
		objetivos.add(1 - Ingresosaux);//Ingresos
		objetivos.add(ingresoPerdidoAreasInfDesvTip); //Homogen ingresos areas inf
		objetivos.add(pasajerosPorCompanyiaDesvTipPerdida); //Homogen pasajerosCom
		objetivos.add(Tasasporcentaje);//Tasas
		objetivos.add(ingresoPorAerDestDesvTip); //Homogen tasas aeropuerto dest
		
		objetivos.add(Pasajerosporcentaje);//Pasajeros
		objetivos.add(Conectividadsolucion);//Conectividad
		
		
		return objetivos;
	}
	
	@Override
	public Individuo inicializarValores(Individuo ind) {
		List<Double> valores = new ArrayList<>(super.getNumVariables());
		for (int i = 0; i < super.getNumVariables(); i++) {

			if (this.numInicializaciones < super.getNumVariables()) {
				if (i == this.numInicializaciones) {
					valores.add(i, 1.0);
				} else {
					valores.add(i, 0.0);
				}
			} else if (this.numInicializaciones == super.getNumVariables()) {
				valores.add(i, 0.0);
			} else if (this.numInicializaciones == super.getNumVariables() + 1) {
				valores.add(i, 1.0);
			}

		}
		this.numInicializaciones++;
		ind.setVariables(valores);
		return ind;
	}
	
	private List<Double> rellenarDireccionesDia(int dia, List<Double> variables) {
		int offset = 0;
		for (int i = 0; i < dia; i++) {
			offset += this.datos.getDatosPorDia().get(i).getConexiones().size();
		}

		for (int i = 0; i < this.datos.getDatosPorDia().get(dia).getDireccionesAMantener().size(); i++) {
			variables.add(offset + this.datos.getDatosPorDia().get(dia).getDireccionesAMantener().get(i), 1.0);
		}
		return variables;
	}

	private int quitarDireccionesDia(int dia, List<Double> variables, int cont) {
		int offset = 0;
		for (int i = 0; i < dia; i++) {
			offset += this.datos.getDatosPorDia().get(i).getConexiones().size();
		}

		for (int i = 0; i < this.datos.getDatosPorDia().get(dia).getDireccionesAMantener().size(); i++) {
			variables.remove(offset + this.datos.getDatosPorDia().get(dia).getDireccionesAMantener().get(i) - cont);
			cont++;
		}
		return cont;
	}
	
	@Override
	public Individuo comprobarRestricciones(Individuo ind) {
		if (ind.getRestricciones().get(0) > this.resSup) {
			ind.setFactible(false);
			ind.setConstraintViolation(Math.abs(this.resSup - ind.getRestricciones().get(0)));
		} else if (ind.getRestricciones().get(0) < this.resInf) {
			ind.setFactible(false);
			ind.setConstraintViolation(Math.abs(this.resInf - ind.getRestricciones().get(0)));
		} else {
			ind.setFactible(true);
			ind.setConstraintViolation(0.0);
		}
		return ind;
	}
	
	@Override
	public void sumarNumInicializaciones() {
		this.numInicializaciones++;
	}

}
