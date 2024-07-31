package com.VisNeo4j.App.Problemas;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.VisNeo4j.App.Constantes.Constantes;
import com.VisNeo4j.App.Modelo.Individuo;
import com.VisNeo4j.App.Problemas.Datos.DatosProblemaDias;
import com.VisNeo4j.App.Problemas.Datos.DatosRRPS_PAT;
import com.VisNeo4j.App.QDMP.DMPreferences;
import com.VisNeo4j.App.Utils.Utils;

public class RRPS_PAT extends Problema {

	private Double resInf = 0.0;
	private Double resSup;
	private DatosRRPS_PAT datos;
	private int numInicializaciones = 0;
	private DMPreferences preferencias;
	private List<String> resPol;
	private List<Integer> direccionesAMantener;

	public RRPS_PAT(DatosRRPS_PAT datos, Double maxRiesgo, List<String> resPol, DMPreferences preferencias) {
		super(0, 1);

		this.datos = datos;
		this.direccionesAMantener = new ArrayList<>();
		this.resPol = resPol;

		this.calcularDireccionesAMantener();
		super.setNumVariables(this.datos.getConexionesTotales().size() - this.direccionesAMantener.size());
		this.resSup = maxRiesgo;
		this.preferencias = preferencias;
		super.setNombre(Constantes.nombreProblemaRRPS_PAT);
	}

	private void calcularDireccionesAMantener() {
		for (int i = 0; i < this.datos.getConexionesTotales().size(); i++) {
			if (this.resPol.contains(this.datos.getContinentesTotales().get(i))
					&& this.datos.getCapitalesTotales().get(i)) {
				this.direccionesAMantener.add(i);
			}
		}
	}

	@Override
	public Individuo evaluate(Individuo ind) {
		List<Double> aux = ind.getVariables();

		this.rellenarDirecciones(aux);

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

		this.quitarDirecciones(aux);

		ind.setVariables(aux);

		return ind;
	}

	private List<Double> calcularObjetivos(Individuo solucion) {
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
		for (int i = 0; i < this.datos.getConexionesTotales().size(); i++) {
			while (pos < this.datos.getConexionesTotalesSeparadas().size()
					&& this.datos.getConexionesTotalesSeparadas().get(pos).get(0)
							.equals(this.datos.getConexionesTotales().get(i).get(0))
					&& this.datos.getConexionesTotalesSeparadas().get(pos).get(1)
							.equals(this.datos.getConexionesTotales().get(i).get(1))) {
				Riesgosumatorio += this.datos.getRiesgos().get(pos) * solucion.getVariables().get(i);
				RiesgosumatorioTotal += this.datos.getRiesgos().get(pos);

				IngresosTsuma += this.datos.getIngresos().get(pos) * solucion.getVariables().get(i);
				IngresosTtotalSuma += this.datos.getIngresos().get(pos);

				Pasajerossumatorio += this.datos.getPasajeros().get(pos) * solucion.getVariables().get(i);
				Pasajerostotal += this.datos.getPasajeros().get(pos);

				Tasassumatorio += this.datos.getTasas().get(pos) * solucion.getVariables().get(i);
				Tasastotal += this.datos.getTasas().get(pos);

				if (pasajerosPorCompanyia.get(this.datos.getCompanyiasTotales().get(pos)) != null) {
					pasajerosPorCompanyia.put(this.datos.getCompanyiasTotales().get(pos),
							List.of(pasajerosPorCompanyia.get(this.datos.getCompanyiasTotales().get(pos)).get(0)
									+ this.datos.getPasajeros().get(pos) * solucion.getVariables().get(i),
									pasajerosPorCompanyia.get(this.datos.getCompanyiasTotales().get(pos)).get(1)
											+ this.datos.getPasajeros().get(pos)));
				} else {
					pasajerosPorCompanyia.put(this.datos.getCompanyiasTotales().get(pos),
							List.of(this.datos.getPasajeros().get(pos) * solucion.getVariables().get(i),
									this.datos.getPasajeros().get(pos) * 1.0));
				}
				if (ingresosPorAreaInf.get(this.datos.getAresInfTotales().get(pos)) != null) {
					ingresosPorAreaInf.put(this.datos.getAresInfTotales().get(pos),
							List.of(ingresosPorAreaInf.get(this.datos.getAresInfTotales().get(pos)).get(0)
									+ this.datos.getIngresos().get(pos) * solucion.getVariables().get(i),
									ingresosPorAreaInf.get(this.datos.getAresInfTotales().get(pos)).get(1)
											+ this.datos.getIngresos().get(pos)));
				} else {
					ingresosPorAreaInf.put(this.datos.getAresInfTotales().get(pos),
							List.of(this.datos.getIngresos().get(pos) * solucion.getVariables().get(i),
									this.datos.getIngresos().get(pos)));
				}
				if (ingresosPorAerDest.get(this.datos.getConexionesTotalesSeparadas().get(pos).get(1)) != null) {
					ingresosPorAerDest.put(this.datos.getConexionesTotalesSeparadas().get(pos).get(1), List.of(
							ingresosPorAerDest.get(this.datos.getConexionesTotalesSeparadas().get(pos).get(1)).get(0)
									+ this.datos.getTasas().get(pos) * solucion.getVariables().get(i),
							ingresosPorAerDest.get(this.datos.getConexionesTotalesSeparadas().get(pos).get(1)).get(0)
									+ this.datos.getTasas().get(pos)));
				} else {
					ingresosPorAerDest.put(this.datos.getConexionesTotalesSeparadas().get(pos).get(1),
							List.of(this.datos.getTasas().get(pos) * solucion.getVariables().get(i),
									this.datos.getTasas().get(pos)));
				}

				pos++;
			}

			if (!this.datos.getConexionesTotales().get(i).get(0).equals(origen)) {
				if (i > 0) {
					Double Conectividadaux = 0.0;
					if (ConectividadauxTotalSuma != 0) {
						Conectividadaux = ConectividadauxSuma / ConectividadauxTotalSuma;
					}
					Conectividadsuma += this.datos.getConectividadesTotales().get(i - 1) * (1 - Conectividadaux);
					ConectividadtotalSuma += this.datos.getConectividadesTotales().get(i - 1);
				}
				// Sumar las conectividades de los destinos y guardar el origen
				origen = this.datos.getConexionesTotales().get(i).get(0);
				ConectividadauxSuma = 0.0;
				ConectividadauxTotalSuma = 0.0;
			}
			// origen = this.datos.getConexionesTotales().get(i).get(0);
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
		Conectividadsuma += this.datos.getConectividadesTotales().get(this.datos.getConexionesTotales().size() - 1)
				* (1 - Conectividadaux);
		ConectividadtotalSuma += this.datos.getConectividadesTotales()
				.get(this.datos.getConexionesTotales().size() - 1);

		if (ConectividadtotalSuma != 0) {
			Conectividadsolucion = Conectividadsuma / ConectividadtotalSuma;
		}

		double ingresoPerdidoAreasInfMedia = 0.0;
		for (String areaInf : ingresosPorAreaInf.keySet()) {
			if (ingresosPorAreaInf.get(areaInf).get(1) == 0.0) {
				ingresoPerdidoAreasInfMedia += 0.0;
			} else {
				ingresoPerdidoAreasInfMedia += 1
						- (ingresosPorAreaInf.get(areaInf).get(0) / ingresosPorAreaInf.get(areaInf).get(1));
			}
		}
		ingresoPerdidoAreasInfMedia = ingresoPerdidoAreasInfMedia / ingresosPorAreaInf.keySet().size();
		double ingresoPerdidoAreasInfDesvTip = 0.0;
		for (String areaInf : ingresosPorAreaInf.keySet()) {
			if (ingresosPorAreaInf.get(areaInf).get(1) == 0.0) {
				ingresoPerdidoAreasInfDesvTip += Math.pow(0.0 - ingresoPerdidoAreasInfMedia, 2);
			} else {
				ingresoPerdidoAreasInfDesvTip += Math
						.pow((1 - ingresosPorAreaInf.get(areaInf).get(0) / ingresosPorAreaInf.get(areaInf).get(1))
								- ingresoPerdidoAreasInfMedia, 2);
			}
		}
		ingresoPerdidoAreasInfDesvTip /= ingresosPorAreaInf.keySet().size();
		ingresoPerdidoAreasInfDesvTip = Math.sqrt(ingresoPerdidoAreasInfDesvTip);

		double pasajerosPorCompanyiaMediaPerdida = 0.0;
		for (String companyia : pasajerosPorCompanyia.keySet()) {
			if (pasajerosPorCompanyia.get(companyia).get(1) == 0.0) {
				pasajerosPorCompanyiaMediaPerdida += 0.0;
			} else {
				pasajerosPorCompanyiaMediaPerdida += 1
						- (pasajerosPorCompanyia.get(companyia).get(0) / pasajerosPorCompanyia.get(companyia).get(1));
			}
		}
		pasajerosPorCompanyiaMediaPerdida = pasajerosPorCompanyiaMediaPerdida / pasajerosPorCompanyia.keySet().size();
		double pasajerosPorCompanyiaDesvTipPerdida = 0.0;
		for (String companyia : pasajerosPorCompanyia.keySet()) {
			if (pasajerosPorCompanyia.get(companyia).get(1) == 0.0) {
				pasajerosPorCompanyiaDesvTipPerdida += Math.pow(0.0 - pasajerosPorCompanyiaMediaPerdida, 2);
			} else {
				pasajerosPorCompanyiaDesvTipPerdida += Math.pow(
						(1 - pasajerosPorCompanyia.get(companyia).get(0) / pasajerosPorCompanyia.get(companyia).get(1))
								- pasajerosPorCompanyiaMediaPerdida,
						2);
			}
		}
		pasajerosPorCompanyiaDesvTipPerdida /= pasajerosPorCompanyia.keySet().size();
		pasajerosPorCompanyiaDesvTipPerdida = Math.sqrt(pasajerosPorCompanyiaDesvTipPerdida);

		double ingresoPorAerDestMedia = 0.0;
		for (String aeropuerto : ingresosPorAerDest.keySet()) {
			if (ingresosPorAerDest.get(aeropuerto).get(1) == 0.0) {
				ingresoPorAerDestMedia += 1.0;
			} else {
				ingresoPorAerDestMedia += 1
						- (ingresosPorAerDest.get(aeropuerto).get(0) / ingresosPorAerDest.get(aeropuerto).get(1));
			}

		}
		ingresoPorAerDestMedia = ingresoPorAerDestMedia / ingresosPorAerDest.keySet().size();
		double ingresoPorAerDestDesvTip = 0.0;
		for (String aeropuerto : ingresosPorAerDest.keySet()) {
			if (ingresosPorAerDest.get(aeropuerto).get(1) == 0.0) {
				ingresoPorAerDestDesvTip += Math.pow(1.0 - ingresoPorAerDestMedia, 2);
			} else {
				ingresoPorAerDestDesvTip += Math
						.pow((1 - ingresosPorAerDest.get(aeropuerto).get(0) / ingresosPorAerDest.get(aeropuerto).get(1))
								- ingresoPorAerDestMedia, 2);
			}

		}
		ingresoPorAerDestDesvTip /= ingresosPorAerDest.keySet().size();
		ingresoPorAerDestDesvTip = Math.sqrt(ingresoPorAerDestDesvTip);

		// -------------RESTRICCIÓN--------------

		objetivos.add(Riesgosumatorio / RiesgosumatorioTotal);// Riesgo

		// -------------OBJETIVOS----------------

		objetivos.add(1 - Ingresosaux);// Ingresos
		objetivos.add(ingresoPerdidoAreasInfDesvTip); // Homogen ingresos areas inf
		objetivos.add(pasajerosPorCompanyiaDesvTipPerdida); // Homogen pasajerosCom
		objetivos.add(Tasasporcentaje);// Tasas
		objetivos.add(ingresoPorAerDestDesvTip); // Homogen tasas aeropuerto dest

		objetivos.add(Pasajerosporcentaje);// Pasajeros
		objetivos.add(Conectividadsolucion);// Conectividad

		return objetivos;
	}

	@Override
	public Individuo inicializarValores(Individuo ind) {
		List<Double> valores = new ArrayList<>(super.getNumVariables());
		for (int i = 0; i < super.getNumVariables(); i++) {

			/*
			 * if (this.numInicializaciones == 0) { valores.add(i, 0.0); } else if
			 * (this.numInicializaciones == 1) { valores.add(i, 1.0); } else {
			 */
			if (i == this.numInicializaciones) {
				valores.add(i, 1.0);
			} else {
				valores.add(i, 0.0);
			}
			// }

			/*
			 * if (this.numInicializaciones < super.getNumVariables()) { if (i ==
			 * this.numInicializaciones) { valores.add(i, 1.0); } else { valores.add(i,
			 * 0.0); } } else if (this.numInicializaciones == super.getNumVariables()) {
			 * valores.add(i, 0.0); } else if (this.numInicializaciones ==
			 * super.getNumVariables() + 1) { valores.add(i, 1.0); }
			 */

		}
		this.numInicializaciones++;
		ind.setVariables(valores);
		return ind;
	}

	@Override
	public Individuo extra(Individuo ind) {
		
		List<Double> aux = ind.getVariables();

		this.rellenarDirecciones(aux);

		ind.setVariables(aux);
		
		Map<String, List<Double>> valoresAdicionales = ind.getExtra();

		Map<String, List<Double>> pasajerosPorCompanyia = new HashMap<>();
		Map<String, List<Double>> ingresosPorAreaInf = new HashMap<>();
		Map<String, List<Double>> ingresosPorAerDest = new HashMap<>();

		int pos = 0;
		for (int i = 0; i < this.datos.getConexionesTotales().size(); i++) {
			while (pos < this.datos.getConexionesTotalesSeparadas().size()
					&& this.datos.getConexionesTotalesSeparadas().get(pos).get(0)
							.equals(this.datos.getConexionesTotales().get(i).get(0))
					&& this.datos.getConexionesTotalesSeparadas().get(pos).get(1)
							.equals(this.datos.getConexionesTotales().get(i).get(1))) {
				

				if (pasajerosPorCompanyia.get(this.datos.getCompanyiasTotales().get(pos)) != null) {
					pasajerosPorCompanyia.put(this.datos.getCompanyiasTotales().get(pos),
							List.of(pasajerosPorCompanyia.get(this.datos.getCompanyiasTotales().get(pos)).get(0)
									+ this.datos.getPasajeros().get(pos) * ind.getVariables().get(i),
									pasajerosPorCompanyia.get(this.datos.getCompanyiasTotales().get(pos)).get(1)
											+ this.datos.getPasajeros().get(pos)));
				} else {
					pasajerosPorCompanyia.put(this.datos.getCompanyiasTotales().get(pos),
							List.of(this.datos.getPasajeros().get(pos) * ind.getVariables().get(i),
									this.datos.getPasajeros().get(pos) * 1.0));
				}
				/*
				 if (pasajerosPorCompanyia.get(this.datos.getCompanyiasTotales().get(pos)) != null) {
					pasajerosPorCompanyia.put(this.datos.getCompanyiasTotales().get(pos),
							List.of(pasajerosPorCompanyia.get(this.datos.getCompanyiasTotales().get(pos)).get(0)
									+ this.datos.getPasajeros().get(pos) * solucion.getVariables().get(i),
									pasajerosPorCompanyia.get(this.datos.getCompanyiasTotales().get(pos)).get(1)
											+ this.datos.getPasajeros().get(pos)));
				} else {
					pasajerosPorCompanyia.put(this.datos.getCompanyiasTotales().get(pos),
							List.of(this.datos.getPasajeros().get(pos) * solucion.getVariables().get(i),
									this.datos.getPasajeros().get(pos) * 1.0));
				}
				 */
				if (ingresosPorAreaInf.get(this.datos.getAresInfTotales().get(pos)) != null) {
					ingresosPorAreaInf.put(this.datos.getAresInfTotales().get(pos),
							List.of(ingresosPorAreaInf.get(this.datos.getAresInfTotales().get(pos)).get(0)
									+ this.datos.getIngresos().get(pos) * ind.getVariables().get(i),
									ingresosPorAreaInf.get(this.datos.getAresInfTotales().get(pos)).get(1)
											+ this.datos.getIngresos().get(pos)));
				} else {
					ingresosPorAreaInf.put(this.datos.getAresInfTotales().get(pos),
							List.of(this.datos.getIngresos().get(pos) * ind.getVariables().get(i),
									this.datos.getIngresos().get(pos)));
				}
				if (ingresosPorAerDest.get(this.datos.getConexionesTotalesSeparadas().get(pos).get(1)) != null) {
					ingresosPorAerDest.put(this.datos.getConexionesTotalesSeparadas().get(pos).get(1), List.of(
							ingresosPorAerDest.get(this.datos.getConexionesTotalesSeparadas().get(pos).get(1)).get(0)
									+ this.datos.getTasas().get(pos) * ind.getVariables().get(i),
							ingresosPorAerDest.get(this.datos.getConexionesTotalesSeparadas().get(pos).get(1)).get(0)
									+ this.datos.getTasas().get(pos)));
				} else {
					ingresosPorAerDest.put(this.datos.getConexionesTotalesSeparadas().get(pos).get(1),
							List.of(this.datos.getTasas().get(pos) * ind.getVariables().get(i),
									this.datos.getTasas().get(pos)));
				}

				pos++;
			}
		}

		List<Double> pasajerosPerdidosPorCompanyia = new ArrayList<>();
		List<Double> ingresoPerdidoAreasInf = new ArrayList<>();
		List<Double> ingresoPerdidoAerDest = new ArrayList<>();
		
		for (String companyia : pasajerosPorCompanyia.keySet()) {
			if (pasajerosPorCompanyia.get(companyia).get(1) == 0.0) {
				pasajerosPerdidosPorCompanyia.add(0.0);
			} else {
				pasajerosPerdidosPorCompanyia.add(100 * (1
						- (pasajerosPorCompanyia.get(companyia).get(0) / pasajerosPorCompanyia.get(companyia).get(1))));
			}
		}
		
		for (String areaInf : ingresosPorAreaInf.keySet()) {
			if (ingresosPorAreaInf.get(areaInf).get(1) == 0.0) {
				ingresoPerdidoAreasInf.add(0.0);
			} else {
				ingresoPerdidoAreasInf.add(100 * (1
						- (ingresosPorAreaInf.get(areaInf).get(0) / ingresosPorAreaInf.get(areaInf).get(1))));
			}
		}
		
		for (String aeropuerto : ingresosPorAerDest.keySet()) {
			if (ingresosPorAerDest.get(aeropuerto).get(1) == 0.0) {
				ingresoPerdidoAerDest.add(1.0 * 100); //TODO: Añadir datos restantes sobre aviones y tasas
			} else {
				ingresoPerdidoAerDest.add(100 * (1
						- (ingresosPorAerDest.get(aeropuerto).get(0) / ingresosPorAerDest.get(aeropuerto).get(1))));
			}

		}
		Collections.sort(pasajerosPerdidosPorCompanyia);
		Collections.sort(ingresoPerdidoAreasInf);
		Collections.sort(ingresoPerdidoAerDest);
		
		valoresAdicionales.put(Constantes.nombreCampoPasajerosPerdidosPorCompañía, pasajerosPerdidosPorCompanyia);
		valoresAdicionales.put(Constantes.nombreCampoIngresoPerdidoPorAreaInf, ingresoPerdidoAreasInf);
		valoresAdicionales.put(Constantes.nombreCampoIngresoPerdidoPorAerDest, ingresoPerdidoAerDest);
		
		if(pasajerosPerdidosPorCompanyia.get(0) != 0.0 && pasajerosPerdidosPorCompanyia.get(pasajerosPerdidosPorCompanyia.size()-1) != 1.0) {
			//valoresAdicionales.put(Constantes.nombreCampoPasajerosPerdidosPorCompañía, List.of(pasajerosPerdidosPorCompanyia.get(0) * 100, pasajerosPerdidosPorCompanyia.get(pasajerosPerdidosPorCompanyia.size()-1) * 100));
		}
		else {
			/*if(pasajerosPerdidosPorCompanyia.get(0) == 0.0 && pasajerosPerdidosPorCompanyia.get(pasajerosPerdidosPorCompanyia.size()-1) != 1.0) {
				double valorMin = pasajerosPerdidosPorCompanyia.get(0);
				int index = 1;
				while (index < pasajerosPerdidosPorCompanyia.size() && valorMin == 0.0) {
					valorMin = pasajerosPerdidosPorCompanyia.get(index);
					index++;
				}
				if(valorMin == 1.0) {
					valoresAdicionales.put(Constantes.nombreCampoPasajerosPerdidosPorCompañía, List.of(pasajerosPerdidosPorCompanyia.get(0) * 100, pasajerosPerdidosPorCompanyia.get(pasajerosPerdidosPorCompanyia.size()-1) * 100));
				}else {
					valoresAdicionales.put(Constantes.nombreCampoPasajerosPerdidosPorCompañía, List.of(valorMin * 100, pasajerosPerdidosPorCompanyia.get(pasajerosPerdidosPorCompanyia.size()-1) * 100));
				}
			}else if(pasajerosPerdidosPorCompanyia.get(0) != 0.0 && pasajerosPerdidosPorCompanyia.get(pasajerosPerdidosPorCompanyia.size()-1) == 1.0) {
				double valorMax = pasajerosPerdidosPorCompanyia.get(pasajerosPerdidosPorCompanyia.size()-1);
				int index = 2;
				while (index <= pasajerosPerdidosPorCompanyia.size() && valorMax == 1.0) {
					valorMax = pasajerosPerdidosPorCompanyia.get(pasajerosPerdidosPorCompanyia.size()-index);
					index++;
				}
				if(valorMax == 0.0) {
					valoresAdicionales.put(Constantes.nombreCampoPasajerosPerdidosPorCompañía, List.of(pasajerosPerdidosPorCompanyia.get(0) * 100, pasajerosPerdidosPorCompanyia.get(pasajerosPerdidosPorCompanyia.size()-1) * 100));
				}else {
					valoresAdicionales.put(Constantes.nombreCampoPasajerosPerdidosPorCompañía, List.of(pasajerosPerdidosPorCompanyia.get(0) * 100, valorMax * 100));
				}
			}*/
			
			/*int indMin = 0;
			int indMax = pasajerosPerdidosPorCompanyia.size()-1;
			
			double valorMin = pasajerosPerdidosPorCompanyia.get(indMin);
			double valorMax = pasajerosPerdidosPorCompanyia.get(indMax);
			
			while(indMin < indMax && (valorMin == 0.0 || valorMax == 1.0)) {
				if(valorMin == 0.0) {
					valorMin = pasajerosPerdidosPorCompanyia.get(indMin);
					indMin++;
				}
				if(valorMax == 1.0) {
					valorMax = pasajerosPerdidosPorCompanyia.get(indMax);
					indMax--;
				}
				
			}
			
			valoresAdicionales.put(Constantes.nombreCampoPasajerosPerdidosPorCompañía, List.of(valorMin * 100, valorMax * 100));*/
			
		}
		
		/*if(ingresoPerdidoAreasInf.get(0) != 0.0 && ingresoPerdidoAreasInf.get(ingresoPerdidoAreasInf.size()-1) != 1.0) {
			valoresAdicionales.put(Constantes.nombreCampoIngresoPerdidoPorAreaInf, List.of(ingresoPerdidoAreasInf.get(0) * 100, ingresoPerdidoAreasInf.get(ingresoPerdidoAreasInf.size()-1) * 100));
		}
		else {
			
		}
		
		if(ingresoPerdidoAerDest.get(0) != 0.0 && ingresoPerdidoAerDest.get(ingresoPerdidoAerDest.size()-1) != 1.0) {
			valoresAdicionales.put(Constantes.nombreCampoIngresoPerdidoPorAerDest, List.of(ingresoPerdidoAerDest.get(0) * 100, ingresoPerdidoAerDest.get(ingresoPerdidoAerDest.size()-1) * 100));
		}
		else {
			
		}*/

		ind.setExtra(valoresAdicionales);
		return ind;
	}

	public List<Double> rellenarDirecciones(List<Double> variables) {

		for (int i = 0; i < this.direccionesAMantener.size(); i++) {
			variables.add(this.direccionesAMantener.get(i), 1.0);
		}

		return variables;
	}

	@Override
	public Individuo devolverSolucionCompleta(Individuo ind) {

		List<Double> aux = ind.getVariables();

		for (int i = 0; i < this.direccionesAMantener.size(); i++) {
			aux.add(this.direccionesAMantener.get(i), 1.0);
		}

		ind.setVariables(aux);

		return ind;
	}

	private void quitarDirecciones(List<Double> variables) {
		int cont = 0;

		for (int i = 0; i < this.direccionesAMantener.size(); i++) {
			variables.remove(this.direccionesAMantener.get(i) - cont);
			cont++;
		}

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
