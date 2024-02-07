package com.VisNeo4j.App.Problemas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.VisNeo4j.App.Constantes.Constantes;
import com.VisNeo4j.App.Modelo.DatosProblema;
import com.VisNeo4j.App.Modelo.DatosProblemaDias;
import com.VisNeo4j.App.Modelo.Individuo;
import com.VisNeo4j.App.Utils.Utils;

public class GestionConexionesAeropuertosPorDia extends Problema{
	
	private Double resInf = 0.0;
	private Double resSup;
	private DatosProblemaDias datos;
	private List<Double> pesos;
	private List<Integer> ordenObj;
	private int numInicializaciones = 0;

	public GestionConexionesAeropuertosPorDia(DatosProblemaDias datos, List<Integer> ordenObj, Double maxRiesgo) {
		super(0, 1);
		int numConexionesTotales = 0;
		for(int i = 0; i < datos.getDatosPorDia().size(); i++) {
			numConexionesTotales += datos.getDatosPorDia().get(i).getConexiones().size() - 
					datos.getDatosPorDia().get(i).getDireccionesAMantener().size();
		}
		super.setNumVariables(numConexionesTotales);
		this.datos = datos;
		this.ordenObj = ordenObj;
		this.resSup = maxRiesgo;
		super.setNombre(Constantes.nombreProblemaGestionConexionesAeropuertosPorDia);
	}
	
	@Override
	public Individuo evaluate (Individuo ind) {
		List<Double> aux = ind.getVariables();
		
		for(int i = 0; i < this.datos.getDatosPorDia().size(); i++) {
			this.rellenarDireccionesDia(i, aux);
		}
		ind.setVariables(aux);
		
		List<List<Double>> subObjetivos = new ArrayList<>();
		for(int i = 0; i < this.datos.getDatosPorDia().size(); i++) {
			subObjetivos.add(this.calcularObjetivosDia(i, ind));
		}
		
		List<Double> restricciones = new ArrayList<>(1);
		
		
		List<Double> objetivos = new ArrayList<>();
		for(int i = 0; i < 8; i++) {
			objetivos.add(0.0);
		}
		for(int i = 0; i < subObjetivos.size(); i++) {
			for(int j = 0; j < objetivos.size(); j++) {
				objetivos.set(j, objetivos.get(j) + subObjetivos.get(i).get(j));
			}
		}
		for(int i = 0; i < objetivos.size(); i++) {
			objetivos.set(i, objetivos.get(i) / subObjetivos.size());
		}
		
		/*double sumaObj = 0;
		
		for (int i = 1; i < 7; i++) {
			sumaObj += sumaObj;
		}
		sumaObj = sumaObj / 6;*/
		
		restricciones.add(0, objetivos.get(0));
		ind.setRestricciones(restricciones);
		this.comprobarRestricciones(ind);
		ind.setObjetivosNorm(objetivos.subList(1, objetivos.size()));
		Double sumaPesos = 0.0;
		
		/*sumaPesos += sumaObj * this.pesos.get(0);
		sumaPesos += objetivos.get(7) * this.pesos.get(1);*/
		
		for(int i = 1; i < objetivos.size(); i++) {
			sumaPesos = sumaPesos + objetivos.get(i) * this.pesos.get(i-1); 
		}
		
		ind.setObjetivos(List.of(sumaPesos));
		
		int cont = 0;
		for(int i = 0; i < this.datos.getDatosPorDia().size(); i++) {
			cont = this.quitarDireccionesDia(i, aux, cont);
		}
		
		ind.setVariables(aux);
		
		
		return ind;
	}
	
	public Individuo evaluate2 (Individuo ind) {
		List<Double> aux = ind.getVariables();
		
		/*for(int i = 0; i < this.datos.getDatosPorDia().size(); i++) {
			this.rellenarDireccionesDia(i, aux);
		}*/
		ind.setVariables(aux);
		
		List<List<Double>> subObjetivos = new ArrayList<>();
		for(int i = 0; i < this.datos.getDatosPorDia().size(); i++) {
			subObjetivos.add(this.calcularObjetivosDia(i, ind));
		}
		
		List<Double> restricciones = new ArrayList<>(1);
		
		
		List<Double> objetivos = new ArrayList<>();
		for(int i = 0; i < 8; i++) {
			objetivos.add(0.0);
		}
		for(int i = 0; i < subObjetivos.size(); i++) {
			for(int j = 0; j < objetivos.size(); j++) {
				objetivos.set(j, objetivos.get(j) + subObjetivos.get(i).get(j));
			}
		}
		for(int i = 0; i < objetivos.size(); i++) {
			objetivos.set(i, objetivos.get(i) / subObjetivos.size());
		}
		
		restricciones.add(0, objetivos.get(0));
		ind.setRestricciones(restricciones);
		this.comprobarRestricciones(ind);
		ind.setObjetivosNorm(objetivos.subList(1, objetivos.size()));
		Double sumaPesos = 0.0;
		for(int i = 1; i < objetivos.size(); i++) {
			sumaPesos = sumaPesos + objetivos.get(i) * this.pesos.get(i-1); 
		}
		
		ind.setObjetivos(List.of(sumaPesos));
		
		/*int cont = 0;
		for(int i = 0; i < this.datos.getDatosPorDia().size(); i++) {
			cont = this.quitarDireccionesDia(i, aux, cont);
		}*/
		
		//ind.setVariables(aux);
		
		
		return ind;
	}
	
	@Override
	public Individuo inicializarValores(Individuo ind) {
		List<Double> valores = new ArrayList<>(super.getNumVariables());
		for(int i = 0; i < super.getNumVariables(); i++) {
			
			if(this.numInicializaciones < super.getNumVariables()) {
				if(i == this.numInicializaciones) {
					valores.add(i, 1.0);
				}else {
					valores.add(i, 0.0);
				}
			}else if (this.numInicializaciones == super.getNumVariables()){
				valores.add(i, 0.0);
			}else if (this.numInicializaciones == super.getNumVariables() + 1) {
				valores.add(i, 1.0);
			}
			
		}
		this.numInicializaciones++;
		ind.setVariables(valores);
		return ind;
	}
	
	
	private List<Double> rellenarDireccionesDia(int dia, List<Double> variables) {
		int offset = 0;
		for(int i = 0; i < dia; i++) {
			offset += this.datos.getDatosPorDia().get(i).getConexiones().size();
		}
		
		for(int i = 0; i < this.datos.getDatosPorDia().get(dia).getDireccionesAMantener().size(); i++) {
			variables.add(offset + this.datos.getDatosPorDia().get(dia).getDireccionesAMantener().get(i), 1.0);
		}
		return variables;
	}
	
	private int quitarDireccionesDia(int dia, List<Double> variables, int cont) {
		int offset = 0;
		for(int i = 0; i < dia; i++) {
			offset += this.datos.getDatosPorDia().get(i).getConexiones().size();
		}
		
		for(int i = 0; i < this.datos.getDatosPorDia().get(dia).getDireccionesAMantener().size(); i++) {
			variables.remove(offset + this.datos.getDatosPorDia().get(dia).getDireccionesAMantener().get(i) - cont);
			cont++;
		}
		return cont;
	}
	
	private List<Double> calcularObjetivosDia(int dia, Individuo ind) {
		int offset = 0;
		for(int i = 0; i < dia; i++) {
			offset += this.datos.getDatosPorDia().get(i).getConexiones().size();
		}
		List<Double> bitsDia;
		
		if(this.datos.getDatosPorDia().get(dia).getConexiones().size() + offset > ind.getVariables().size()) {
			bitsDia = ind.getVariables().subList(offset, this.datos.getDatosPorDia().get(dia).getConexiones().size());
		}else {
			bitsDia = ind.getVariables().subList(offset, this.datos.getDatosPorDia().get(dia).getConexiones().size() + offset);
		}
		
		return this.calcularRiesgoPasajerosIngresosHPasajerosHIngresosDia(dia, bitsDia);
		
	}
	
	private List<Double> calcularRiesgoPasajerosIngresosHPasajerosHIngresosDia(int dia, List<Double> variablesDia) {
		List<Double> objetivos = new ArrayList<Double>(8);
		
		Double Riesgosumatorio = 0.0;
        Double RiesgosumatorioTotal = 0.0;
        
        Double Pasajerossumatorio = 0.0;
        Double Pasajerostotal = 0.0;
        
        Double Ingresossuma = 0.0;
        Double IngresostotalSuma = 0.0;
        
        Double Ingresostasassuma = 0.0;
        Double Ingresostasastotalsuma = 0.0;
        
        //int vuelos_cancelados = 0;
        
        boolean calculado = false;
        
        //java.util.Map<String, Double> ingresosTAeropuertoTotal = new java.util.HashMap<>();
        java.util.Map<String, Double> ingresosTAeropuertosVar = new java.util.HashMap<>();
        List<Double> IporcentajePerdido = new ArrayList<>();
        double ImediaPorcentajeVuelosPerdidos = 0.0;
        double IporcentajePerdidoDesviacionMedia = 0.0;
        
        int[] totalPasajerosCompanyias = new int[this.datos.getDatosPorDia().get(dia).getCompanyias().size()];
        int[] totalPasajerosConexiones = new int[this.datos.getDatosPorDia().get(dia).getCompanyias().size()];
        List<Double> porcentajePerdido = new ArrayList<>();             
        double porcentajePerdidoMedia = 0.0;                            
        double porcentajePerdidoDesviacionMedia = 0.0;
        
        Map<String, Double> sumaTasasPorAeropuerto = new HashMap<>();
        Map<String, Double> porcentajePerdidoTasasPorAeropuerto = new HashMap<>();
        double porcentajePerdidoTasasMedia = 0.0;
        double porcentajePerdidoTasasDesviacionMedia = 0.0;
        
        
        for (int j = 0; j < this.datos.getDatosPorDia().get(dia).getCompanyias().size(); j++) {
        	for (int i = 0; i < this.datos.getDatosPorDia().get(dia).getConexiones().size(); i++) {
        		if(!calculado) {
        			/*if(variablesDia.get(i) == 0.0) {
        				vuelos_cancelados += this.datos.getDatosPorDia().get(dia).getVuelosEntrantesConexion().get(this.datos.getDatosPorDia().get(dia).getConexiones().get(i));
        			}*/
        			
        			
        			//Riesgo, Pasajeros e Ingresos -----------------------
        			Riesgosumatorio += this.datos.getDatosPorDia().get(dia).getRiesgos().get(i) * 
        					variablesDia.get(i);
        			RiesgosumatorioTotal += this.datos.getDatosPorDia().get(dia).getRiesgos().get(i);
            	
        			Pasajerossumatorio += this.datos.getDatosPorDia().get(dia).getPasajeros().get(i) * 	
        					variablesDia.get(i);
        			Pasajerostotal += this.datos.getDatosPorDia().get(dia).getPasajeros().get(i);
            	
        			Ingresossuma += (this.datos.getDatosPorDia().get(dia).getDineroMedioT().get(i) + this.datos.getDatosPorDia().get(dia).getDineroMedioN().get(i)) * 	
        					variablesDia.get(i);
        			IngresostotalSuma += this.datos.getDatosPorDia().get(dia).getDineroMedioT().get(i) + this.datos.getDatosPorDia().get(dia).getDineroMedioN().get(i);
        			
        			Ingresostasassuma += this.datos.getDatosPorDia().get(dia).getTasas().get(i) *
        					variablesDia.get(i);
        			Ingresostasastotalsuma += this.datos.getDatosPorDia().get(dia).getTasas().get(i);
        			//----------------------------------------------------
            	
        			//calculoHomogeneidadIngresosTurismoAeropuertos
        			
        			/*if (ingresosTAeropuertoTotal.get(this.datos.getDatosPorDia().get(dia).getConexiones().get(i).get(1)) != null) {
        				ingresosTAeropuertoTotal.put(this.datos.getDatosPorDia().get(dia).getConexiones().get(i).get(1), ingresosTAeropuertoTotal.get(
        						this.datos.getDatosPorDia().get(dia).getConexiones().get(i).get(1)) + this.datos.getDatosPorDia().get(dia).getDineroMedioT().get(i)); //Sustituir por i
        			} else {
        				ingresosTAeropuertoTotal.put(this.datos.getDatosPorDia().get(dia).getConexiones().get(i).get(1), this.datos.getDatosPorDia().get(dia).getDineroMedioT().get(i)); //Sustituir por i
        			}*/
        			if (variablesDia.get(i) == 1.0) { //Descomentar la linea
        				if (ingresosTAeropuertosVar.get(this.datos.getDatosPorDia().get(dia).getConexiones().get(i).get(1)) != null) {
        					ingresosTAeropuertosVar.put(this.datos.getDatosPorDia().get(dia).getConexiones().get(i).get(1), ingresosTAeropuertosVar.get(
        							this.datos.getDatosPorDia().get(dia).getConexiones().get(i).get(1)) + this.datos.getDatosPorDia().get(dia).getDineroMedioT().get(i)); //Sustituir por i
        				} else {
        					ingresosTAeropuertosVar.put(this.datos.getDatosPorDia().get(dia).getConexiones().get(i).get(1), this.datos.getDatosPorDia().get(dia).getDineroMedioT().get(i)); //Sustituir por i
        				}
        			}
        			//Cálculo homogeneidad pérdida de ingresos entre aeropuertos destino mediante las tasas
        			if(!sumaTasasPorAeropuerto.keySet().contains(this.datos.getDatosPorDia().get(dia).getConexiones().get(i).get(1))) {
        				sumaTasasPorAeropuerto.put(this.datos.getDatosPorDia().get(dia).getConexiones().get(i).get(1), variablesDia.get(i) * this.datos.getDatosPorDia().get(dia).getTasas().get(i));
        			}else {
        				sumaTasasPorAeropuerto.put(this.datos.getDatosPorDia().get(dia).getConexiones().get(i).get(1), sumaTasasPorAeropuerto.get(this.datos.getDatosPorDia().get(dia).getConexiones().get(i).get(1)) + variablesDia.get(i) * this.datos.getDatosPorDia().get(dia).getTasas().get(i));
        			}
        			
        		}
        		
        		//calculoHomogeneidadPasajerosAerolineas------------
        		if (this.datos.getDatosPorDia().get(dia).getPasajerosCompanyia().get(List.of(this.datos.getDatosPorDia().get(dia).getConexiones().get(i).get(0),this.datos.getDatosPorDia().get(dia).getConexiones().get(i).get(1),
                		this.datos.getDatosPorDia().get(dia).getCompanyias().get(j))) != null) {
                    totalPasajerosCompanyias[j] = totalPasajerosCompanyias[j] + this.datos.getDatosPorDia().get(dia).getPasajerosCompanyia().get(
                            List.of(this.datos.getDatosPorDia().get(dia).getConexiones().get(i).get(0), this.datos.getDatosPorDia().get(dia).getConexiones().get(i).get(1), this.datos.getDatosPorDia().get(dia).getCompanyias().get(j)));
                    if (variablesDia.get(i) == 1.0) { //Descomentar la linea
                        totalPasajerosConexiones[j] = totalPasajerosConexiones[j] + this.datos.getDatosPorDia().get(dia).getPasajerosCompanyia().
                                get(List.of(this.datos.getDatosPorDia().get(dia).getConexiones().get(i).get(0), this.datos.getDatosPorDia().get(dia).getConexiones().get(i).get(1),this.datos.getDatosPorDia().get(dia).getCompanyias().get(j)));
                    }
                }
        		
        		
        	}
        	calculado = true;
        	//calculoHomogeneidadPasajerosAerolineas----------------
        	if (totalPasajerosCompanyias[j] != 0) {
                porcentajePerdido.add(1 - (double) totalPasajerosConexiones[j] / totalPasajerosCompanyias[j]);
                porcentajePerdidoMedia = porcentajePerdidoMedia +
                        1 - (double) totalPasajerosConexiones[j] / totalPasajerosCompanyias[j];
            }
        }
        	
        for (String destino : this.datos.getDatosPorDia().get(dia).getAeropuertosDestino()) {
        	if(this.datos.getDatosPorDia().get(dia).getTasasPorAeropuertoDestino().get(destino) == 0.0) {
        		porcentajePerdidoTasasPorAeropuerto.put(destino, 0.0);
        	}else {
        		porcentajePerdidoTasasPorAeropuerto.put(destino, 1 - sumaTasasPorAeropuerto.get(destino) / this.datos.getDatosPorDia().get(dia).getTasasPorAeropuertoDestino().get(destino));
        		porcentajePerdidoTasasMedia += 1 - sumaTasasPorAeropuerto.get(destino) / this.datos.getDatosPorDia().get(dia).getTasasPorAeropuertoDestino().get(destino);
        	}
        	
        }
        porcentajePerdidoTasasMedia = porcentajePerdidoTasasMedia / this.datos.getDatosPorDia().get(dia).getAeropuertosDestino().size();
        
        for (String destino : this.datos.getDatosPorDia().get(dia).getAeropuertosDestino()) {
        	porcentajePerdidoTasasDesviacionMedia += Math.abs(porcentajePerdidoTasasPorAeropuerto.get(destino) - porcentajePerdidoTasasMedia);
        }
        porcentajePerdidoTasasDesviacionMedia = porcentajePerdidoTasasDesviacionMedia / this.datos.getDatosPorDia().get(dia).getAeropuertosDestino().size();
        	
        int i = 0;
        
        Double aux = 0.0;
        if (IngresostotalSuma != 0.0) {
            aux = Ingresossuma / IngresostotalSuma;
        }
        
        for (String key : this.datos.getDatosPorDia().get(dia).getIngresosTAeropuertoTotal().keySet()) {
            if (ingresosTAeropuertosVar.get(key) != null) {
                IporcentajePerdido.add(1 - (double) ingresosTAeropuertosVar.get(key) / this.datos.getDatosPorDia().get(dia).getIngresosTAeropuertoTotal().get(key));
                ImediaPorcentajeVuelosPerdidos = ImediaPorcentajeVuelosPerdidos + IporcentajePerdido.get(i);
            } else {
                IporcentajePerdido.add(1.0);
            }
            i++;
        }
        if(ingresosTAeropuertosVar.size() == 0) {
        	ImediaPorcentajeVuelosPerdidos = IporcentajePerdido.size();
        }
        ImediaPorcentajeVuelosPerdidos = ImediaPorcentajeVuelosPerdidos / IporcentajePerdido.size();
        for (i = 0; i < IporcentajePerdido.size(); i++) {
            IporcentajePerdidoDesviacionMedia = IporcentajePerdidoDesviacionMedia +
                    Math.abs(IporcentajePerdido.get(i) - ImediaPorcentajeVuelosPerdidos);
        }
        IporcentajePerdidoDesviacionMedia = IporcentajePerdidoDesviacionMedia / IporcentajePerdido.size();
        
        porcentajePerdidoMedia = porcentajePerdidoMedia / porcentajePerdido.size();
        for (i = 0; i < porcentajePerdido.size(); i++) {
            porcentajePerdidoDesviacionMedia = porcentajePerdidoDesviacionMedia +
                    Math.abs(porcentajePerdido.get(i) - porcentajePerdidoMedia);
        }
        porcentajePerdidoDesviacionMedia = porcentajePerdidoDesviacionMedia / porcentajePerdido.size();
        
        
        //System.out.println("Vuelos: " + vuelos_cancelados);
        objetivos.add(0, Riesgosumatorio / RiesgosumatorioTotal);//Riesgo SIR
        objetivos.add(1, 1 - Pasajerossumatorio / Pasajerostotal);//Pérdida de pasajeros
        objetivos.add(2, 1 - aux);//Pérdida de ingresos turismo
        objetivos.add(3, porcentajePerdidoDesviacionMedia);//Homogeneidad Pérdida de pasajeros Aerolineas
        objetivos.add(4, IporcentajePerdidoDesviacionMedia);//Homogeneidad Pérdida de ingresos turismo
        objetivos.add(5, 1 - Ingresostasassuma / Ingresostasastotalsuma);
        objetivos.add(6, porcentajePerdidoTasasDesviacionMedia);
        objetivos.add(calculoConectividadDia(dia, variablesDia));
        
       return objetivos; 
	}
	
	private Double calculoConectividadDia(int dia, List<Double> variablesDia/*, Individuo solution*/) {
        Double suma = 0.0;
        Double totalSuma = 0.0;
        Double solucion = 0.0;
        
        for (String origen : this.datos.getDatosPorDia().get(dia).getAeropuertosOrigen()) {
            Double auxSuma = 0.0;
            Double auxTotalSuma = 0.0;
            
            for (String destino : this.datos.getDatosPorDia().get(dia).getListaConexionesSalidas().get(origen)) {
                auxSuma += variablesDia.get(
                		Utils.encontrarIndiceEnLista(this.datos.getDatosPorDia().get(dia).getConexiones(), List.of(origen, destino)))
                		* this.datos.getDatosPorDia().get(dia).getVuelosEntrantesConexion().get(List.of(origen, destino));
                auxTotalSuma += this.datos.getDatosPorDia().get(dia).getVuelosEntrantesConexion().get(List.of(origen, destino));
            }
            Double aux = 0.0;
            if (auxTotalSuma != 0) {
                aux = auxSuma / auxTotalSuma;
            }
            suma += this.datos.getDatosPorDia().get(dia).getConectividadesAeropuertosOrigen().get(origen) * (1 - aux);
            totalSuma += this.datos.getDatosPorDia().get(dia).getConectividadesAeropuertosOrigen().get(origen);
        }
        if (totalSuma != 0) {
            solucion = suma / totalSuma;
        }
        return solucion;
    }
	
	@Override
	public Individuo comprobarRestricciones(Individuo ind) {
		if(ind.getRestricciones().get(0) > this.resSup) {
			ind.setFactible(false);
			ind.setConstraintViolation(Math.abs(this.resSup - ind.getRestricciones().get(0)));
		}else if(ind.getRestricciones().get(0) < this.resInf){
			ind.setFactible(false);
			ind.setConstraintViolation(Math.abs(this.resInf - ind.getRestricciones().get(0)));
		}
		else {
			ind.setFactible(true);
			ind.setConstraintViolation(0.0);
		}
		return ind;
	}
	
	@Override
	public Map<String, String> calcularValoresAdicionales(Individuo ind){
		Map<String, String> valores = new HashMap<>();
		int vuelos_cancelados = 0;
		
		for(int i = 0; i < this.datos.getDatosPorDia().size(); i++) {
			int offset = 0;
			for(int j = 0; j < i; j++) {
				offset += this.datos.getDatosPorDia().get(j).getConexiones().size();
			}
			
			List<Double> bitsDia;
			
			if(this.datos.getDatosPorDia().get(i).getConexiones().size() + offset > ind.getVariables().size()) {
				bitsDia = ind.getVariables().subList(offset, this.datos.getDatosPorDia().get(i).getConexiones().size());
			}else {
				bitsDia = ind.getVariables().subList(offset, this.datos.getDatosPorDia().get(i).getConexiones().size() + offset);
			}
			
			for (int k = 0; k < this.datos.getDatosPorDia().get(i).getConexiones().size(); k++) {
    			if(bitsDia.get(k) == 0.0) {
    				vuelos_cancelados += this.datos.getDatosPorDia().get(i).getVuelosEntrantesConexion().get(this.datos.getDatosPorDia().get(i).getConexiones().get(k));
    			}
    		
			}
			
		}
		valores.put(Constantes.nombreCampoVuelosCancelados, String.valueOf(vuelos_cancelados));
		
		
		
		return valores;
	}
	
	@Override
	public void sumarNumInicializaciones() {
		this.numInicializaciones++;
	}

}
