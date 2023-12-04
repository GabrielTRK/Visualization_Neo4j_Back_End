package com.nsgaiii.nsgaiiidemo.App.Problemas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nsgaiii.nsgaiiidemo.App.Constantes.Constantes;
import com.nsgaiii.nsgaiiidemo.App.Modelo.DatosProblema;
import com.nsgaiii.nsgaiiidemo.App.Modelo.Individuo;
import com.nsgaiii.nsgaiiidemo.App.Utils.Utils;

public class GestionConexionesAeropuertos extends Problema{
	
	private Double resInf = 0.0;
	private Double resSup = 0.5;
	private DatosProblema datos;
	private List<Double> pesos;
	private int numInicializaciones = 0;

	public GestionConexionesAeropuertos(DatosProblema datos, List<Double> pesos) {
		super(datos.getConexiones().size() - datos.getDireccionesAMantener().size(), 1);
		this.datos = datos;
		this.pesos = pesos;
		super.setNombre(Constantes.nombreProblemaVuelos);
	}
	
	@Override
	public Individuo evaluate (Individuo ind) {
		
		List<Double> aux = ind.getVariables();
		for(int i = 0; i < this.datos.getDireccionesAMantener().size(); i++) {
			aux.add(this.datos.getDireccionesAMantener().get(i), 1.0);
		}
		ind.setVariables(aux);
		
		List<Double> objetivosYRes = calcularRiesgoPasajerosIngresosHPasajerosHIngresos(ind);
		objetivosYRes.add(calculoConectividad(ind));
		
		
		List<Double> restricciones = new ArrayList<>(1);
		restricciones.add(0, objetivosYRes.get(0));
		ind.setRestricciones(restricciones);
		ind.setObjetivosNorm(objetivosYRes.subList(1, objetivosYRes.size()));
		
		this.comprobarRestricciones(ind);
		
		Double sumaPesos = 0.0;
		for(int i = 1; i < objetivosYRes.size(); i++) {
			sumaPesos = sumaPesos + objetivosYRes.get(i) * this.pesos.get(i-1); 
		}
		
		ind.setObjetivos(List.of(sumaPesos));
		
		int cont = 0;
		for(int i = 0; i < this.datos.getDireccionesAMantener().size(); i++) {
			aux.remove((int)this.datos.getDireccionesAMantener().get(i) - cont);
			cont++;
		}
		ind.setVariables(aux);
		
		return ind;
	}
	
	@Override
	public Individuo inicializarValores(Individuo ind) {
		List<Double> valores = new ArrayList<>(super.getNumVariables());
		for(int i = 0; i < super.getNumVariables(); i++) {
			/*if(this.numInicializaciones == 0) {
				valores.add(i, 0.0);
			}else if(this.numInicializaciones == 1) {
				valores.add(i, 1.0);
			}else {
				valores.add(i, Utils.getRandBinNumber());
			}*/
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
			
			
			
			//valores.add(i, Utils.getRandBinNumber());
		}
		this.numInicializaciones++;
		ind.setVariables(valores);
		return ind;
	}
	
	private List<Double> calcularRiesgoPasajerosIngresosHPasajerosHIngresos(Individuo solucion) {
		List<Double> objetivos = new ArrayList<Double>(7);
		
		Double Riesgosumatorio = 0.0;
        Double RiesgosumatorioTotal = 0.0;
        
        Double Pasajerossumatorio = 0.0;
        Double Pasajerostotal = 0.0;
        
        Double Ingresossuma = 0.0;
        Double IngresostotalSuma = 0.0;
        
        Double Ingresostasassuma = 0.0;
        Double Ingresostasastotalsuma = 0.0;
        
        boolean calculado = false;
        
        java.util.Map<String, Integer> numPasajerosAeropuerto = new java.util.HashMap<>();
        java.util.Map<String, Integer> numPasajerosAeropuertoConexiones = new java.util.HashMap<>();
        List<Double> IporcentajePerdido = new ArrayList<>();
        double ImediaPorcentajeVuelosPerdidos = 0.0;
        double IporcentajePerdidoDesviacionMedia = 0.0;
        
        int[] totalPasajerosCompanyias = new int[this.datos.getCompanyias().size()];
        int[] totalPasajerosConexiones = new int[this.datos.getCompanyias().size()];    
        List<Double> porcentajePerdido = new ArrayList<>();             
        double porcentajePerdidoMedia = 0.0;                            
        double porcentajePerdidoDesviacionMedia = 0.0;
        
        Map<String, Double> sumaTasasPorAeropuerto = new HashMap<>();
        Map<String, Double> porcentajePerdidoTasasPorAeropuerto = new HashMap<>();
        double porcentajePerdidoTasasMedia = 0.0;
        double porcentajePerdidoTasasDesviacionMedia = 0.0;
        
        
        for (int j = 0; j < this.datos.getCompanyias().size(); j++) {
        	for (int i = 0; i < this.datos.getConexiones().size(); i++) {
        		if(!calculado) {
        			//Riesgo, Pasajeros e Ingresos -----------------------
        			Riesgosumatorio += this.datos.getRiesgos().get(i) * 
            			solucion.getVariables().get(i);
        			RiesgosumatorioTotal += this.datos.getRiesgos().get(i);
            	
        			Pasajerossumatorio += this.datos.getPasajeros().get(i) * 	
                		solucion.getVariables().get(i);
        			Pasajerostotal += this.datos.getPasajeros().get(i);
            	
        			Ingresossuma += this.datos.getDineroMedioT().get(i) * 	
            			solucion.getVariables().get(i);
        			IngresostotalSuma += this.datos.getDineroMedioT().get(i);
        			
        			Ingresostasassuma += this.datos.getTasas().get(i) *
        				solucion.getVariables().get(i);
        			Ingresostasastotalsuma += this.datos.getTasas().get(i);
        			//----------------------------------------------------
            	
        			//calculoHomogeneidadIngresosTurismoAeropuertos
        			if (numPasajerosAeropuerto.get(this.datos.getConexiones().get(i).get(1)) != null) {
        				numPasajerosAeropuerto.put(this.datos.getConexiones().get(i).get(1), numPasajerosAeropuerto.get(
        						this.datos.getConexiones().get(i).get(1)) + this.datos.getPasajeros().get(i)); //Sustituir por i
        			} else {
        				numPasajerosAeropuerto.put(this.datos.getConexiones().get(i).get(1), this.datos.getPasajeros().get(i)); //Sustituir por i
        			}
        			if (solucion.getVariables().get(i) == 1.0) { //Descomentar la linea
        				if (numPasajerosAeropuertoConexiones.get(this.datos.getConexiones().get(i).get(1)) != null) {
        					numPasajerosAeropuertoConexiones.put(this.datos.getConexiones().get(i).get(1), numPasajerosAeropuertoConexiones.get(
        							this.datos.getConexiones().get(i).get(1)) + this.datos.getPasajeros().get(i)); //Sustituir por i
        				} else {
                        numPasajerosAeropuertoConexiones.put(this.datos.getConexiones().get(i).get(1), this.datos.getPasajeros().get(i)); //Sustituir por i
        				}
        			}
        			//Cálculo homogeneidad pérdida de ingresos entre aeropuertos origen mediante las tasas
        			if(!sumaTasasPorAeropuerto.keySet().contains(this.datos.getConexiones().get(i).get(0))) {
        				sumaTasasPorAeropuerto.put(this.datos.getConexiones().get(i).get(0), solucion.getVariables().get(i) * this.datos.getTasas().get(i));
        			}else {
        				sumaTasasPorAeropuerto.put(this.datos.getConexiones().get(i).get(0), sumaTasasPorAeropuerto.get(this.datos.getConexiones().get(i).get(0)) + solucion.getVariables().get(i) * this.datos.getTasas().get(i));
        			}
        			
        		}
        		
        		//calculoHomogeneidadPasajerosAerolineas------------
        		if (this.datos.getPasajerosCompanyia().get(List.of(this.datos.getConexiones().get(i).get(0),this.datos.getConexiones().get(i).get(1),
                		this.datos.getCompanyias().get(j))) != null) {
                    totalPasajerosCompanyias[j] = totalPasajerosCompanyias[j] + this.datos.getPasajerosCompanyia().get(
                            List.of(this.datos.getConexiones().get(i).get(0), this.datos.getConexiones().get(i).get(1), this.datos.getCompanyias().get(j)));
                    if (solucion.getVariables().get(i) == 1.0) { //Descomentar la linea
                        totalPasajerosConexiones[j] = totalPasajerosConexiones[j] + this.datos.getPasajerosCompanyia().
                                get(List.of(this.datos.getConexiones().get(i).get(0), this.datos.getConexiones().get(i).get(1),this.datos.getCompanyias().get(j)));
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
        	
        for (String destino : this.datos.getAeropuertosDestino()) {
        	if(this.datos.getTasasPorAeropuertoDestino().get(destino) == 0.0) {
        		porcentajePerdidoTasasPorAeropuerto.put(destino, 0.0);
        	}else {
        		porcentajePerdidoTasasPorAeropuerto.put(destino, 1 - sumaTasasPorAeropuerto.get(destino) / this.datos.getTasasPorAeropuertoDestino().get(destino));
        		porcentajePerdidoTasasMedia += 1 - sumaTasasPorAeropuerto.get(destino) / this.datos.getTasasPorAeropuertoDestino().get(destino);
        	}
        	
        }
        porcentajePerdidoTasasMedia = porcentajePerdidoTasasMedia / this.datos.getAeropuertosOrigen().size();
        
        for (String origen : this.datos.getAeropuertosOrigen()) {
        	porcentajePerdidoTasasDesviacionMedia += Math.abs(porcentajePerdidoTasasPorAeropuerto.get(origen) - porcentajePerdidoTasasMedia);
        }
        porcentajePerdidoTasasDesviacionMedia = porcentajePerdidoTasasDesviacionMedia / this.datos.getAeropuertosOrigen().size();
        	
        int i = 0;
        
        Double aux = 0.0;
        if (IngresostotalSuma != 0.0) {
            aux = Ingresossuma / IngresostotalSuma;
        }
        
        for (String key : numPasajerosAeropuerto.keySet()) {
            if (numPasajerosAeropuertoConexiones.get(key) != null) {
                IporcentajePerdido.add(1 - (double) numPasajerosAeropuertoConexiones.get(key) / numPasajerosAeropuerto.get(key));
                ImediaPorcentajeVuelosPerdidos = ImediaPorcentajeVuelosPerdidos + IporcentajePerdido.get(i);
            } else {
                IporcentajePerdido.add(1.0);
            }
            i++;
        }
        if(numPasajerosAeropuertoConexiones.size() == 0) {
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
        
        
        
        objetivos.add(0, Riesgosumatorio / RiesgosumatorioTotal);//Riesgo SIR
        objetivos.add(1, 1 - Pasajerossumatorio / Pasajerostotal);//Pérdida de pasajeros
        objetivos.add(2, 1 - aux);//Pérdida de ingresos turismo
        objetivos.add(3, porcentajePerdidoDesviacionMedia);//Homogeneidad Pérdida de pasajeros Aerolineas
        objetivos.add(4, IporcentajePerdidoDesviacionMedia);//Homogeneidad Pérdida de ingresos turismo
        objetivos.add(5, 1 - Ingresostasassuma / Ingresostasastotalsuma);
        objetivos.add(6, porcentajePerdidoTasasDesviacionMedia);
        
        return objetivos;
	}
	
	private Double calculoConectividad(Individuo solution) { // Perfecto, se compara con objetivo conectividad
        Double suma = 0.0;
        Double totalSuma = 0.0;
        Double solucion = 0.0;
        
        for (String origen : this.datos.getAeropuertosOrigen()) {
            Double auxSuma = 0.0;
            Double auxTotalSuma = 0.0;
            
            for (String destino : this.datos.getListaConexionesSalidas().get(origen)) {
                auxSuma += solution.getVariables().get(
                		Utils.encontrarIndiceEnLista(this.datos.getConexiones(), List.of(origen, destino)))
                		* this.datos.getVuelosEntrantesConexion().get(List.of(origen, destino));
                auxTotalSuma += this.datos.getVuelosEntrantesConexion().get(List.of(origen, destino));
            }
            Double aux = 0.0;
            if (auxTotalSuma != 0) {
                aux = auxSuma / auxTotalSuma;
            }
            suma += this.datos.getConectividadesAeropuertosOrigen().get(origen) * (1 - aux);
            totalSuma += this.datos.getConectividadesAeropuertosOrigen().get(origen);
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

}
