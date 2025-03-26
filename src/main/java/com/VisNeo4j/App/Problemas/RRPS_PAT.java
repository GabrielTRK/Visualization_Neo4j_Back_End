package com.VisNeo4j.App.Problemas;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.VisNeo4j.App.Constantes.Constantes;
import com.VisNeo4j.App.Modelo.Individuo;
import com.VisNeo4j.App.Problemas.Datos.DatosRRPS_PAT;
import com.VisNeo4j.App.QDMP.DMPreferences;
import com.VisNeo4j.App.Utils.Utils;

public class RRPS_PAT extends Problema {
	
	private Double SRate = 0.5;

	private Double resInf = 0.0;
	private Double resSup;
	private DatosRRPS_PAT datos;
	private int numInicializaciones = 0;
	private DMPreferences preferencias;
	private List<String> resPol;
	private List<Integer> direccionesAMantener;
	private List<Integer> objetivosRestriccion = Stream.of(2, 3, 5).collect(Collectors.toList());
	private List<Integer> orderRes = new ArrayList<>();
	
	private Double RiesgosumatorioTotal = 0.0;
	private Double IngresosTtotalSuma = 0.0;

	private Double Pasajerostotal = 0.0;

	private Double Tasastotal = 0.0;
	
	private Double ConectividadtotalSuma = 0.0;
	
	
	private Map<String, List<Double>> pasajerosPorCompanyia = new HashMap<>();
	private Map<String, List<Double>> ingresosPorAreaInf = new HashMap<>();
	private Map<String, List<Double>> ingresosPorAerDest = new HashMap<>();
	

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

		for (int obj : this.preferencias.getOrder().getOrder()) {

			if (this.objetivosRestriccion.contains(obj)) {
				this.orderRes.add(obj);
			}
		}
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
		this.comprobarOrden(ind, objetivos);
		ind.setObjetivosNorm(objetivos.subList(1, objetivos.size()));
		Double sumaPesos = 0.0;

		for (int i = 1; i < objetivos.size(); i++) {
			sumaPesos += objetivos.get(i) * this.preferencias.getWeightsVector().get(i - 1);
		}

		ind.setObjetivos(Stream.of(sumaPesos).collect(Collectors.toList()));

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
							Stream.of(
									pasajerosPorCompanyia.get(this.datos.getCompanyiasTotales().get(pos)).get(0)
											+ this.datos.getPasajeros().get(pos) * solucion.getVariables().get(i),
									pasajerosPorCompanyia.get(this.datos.getCompanyiasTotales().get(pos)).get(1)
											+ this.datos.getPasajeros().get(pos))
									.collect(Collectors.toList()));
				} else {
					pasajerosPorCompanyia.put(this.datos.getCompanyiasTotales().get(pos),
							Stream.of(this.datos.getPasajeros().get(pos) * solucion.getVariables().get(i),
									this.datos.getPasajeros().get(pos) * 1.0).collect(Collectors.toList()));
				}
				if (ingresosPorAreaInf.get(this.datos.getAresInfTotales().get(pos)) != null) {
					ingresosPorAreaInf
							.put(this.datos.getAresInfTotales().get(pos),
									Stream.of(ingresosPorAreaInf.get(this.datos.getAresInfTotales().get(pos)).get(0)
											+ this.datos.getIngresos().get(pos) * solucion.getVariables().get(i),
											ingresosPorAreaInf.get(this.datos.getAresInfTotales().get(pos)).get(1)
													+ this.datos.getIngresos().get(pos))
											.collect(Collectors.toList()));
				} else {
					ingresosPorAreaInf.put(this.datos.getAresInfTotales().get(pos),
							Stream.of(this.datos.getIngresos().get(pos) * solucion.getVariables().get(i),
									this.datos.getIngresos().get(pos)).collect(Collectors.toList()));
				}
				if (ingresosPorAerDest.get(this.datos.getConexionesTotalesSeparadas().get(pos).get(1)) != null) {
					ingresosPorAerDest.put(this.datos.getConexionesTotalesSeparadas().get(pos).get(1), Stream.of(
							ingresosPorAerDest.get(this.datos.getConexionesTotalesSeparadas().get(pos).get(1)).get(0)
									+ this.datos.getTasas().get(pos) * solucion.getVariables().get(i),
							ingresosPorAerDest.get(this.datos.getConexionesTotalesSeparadas().get(pos).get(1)).get(0)
									+ this.datos.getTasas().get(pos))
							.collect(Collectors.toList()));
				} else {
					ingresosPorAerDest.put(this.datos.getConexionesTotalesSeparadas().get(pos).get(1),
							Stream.of(this.datos.getTasas().get(pos) * solucion.getVariables().get(i),
									this.datos.getTasas().get(pos)).collect(Collectors.toList()));
				}

				pos++;
			}
			//TODO: crear una lista para el total de vuelos entrantes para cada origen. Repetidos
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
		solucion.initExtra();
		Map<String, List<Double>> valoresAdicionales = solucion.getExtra();
		
		
		valoresAdicionales.put(Constantes.nombreRiesgoImportado, Stream.of(Riesgosumatorio).collect(Collectors.toList()));
		
		objetivos.add(Riesgosumatorio / RiesgosumatorioTotal);// Riesgo
		this.RiesgosumatorioTotal = RiesgosumatorioTotal;

		// -------------OBJETIVOS----------------

		objetivos.add(1 - Ingresosaux);// Ingresos
		this.IngresosTtotalSuma = IngresosTtotalSuma;
		objetivos.add(ingresoPerdidoAreasInfDesvTip); // Homogen ingresos areas inf
		this.ingresosPorAreaInf = ingresosPorAreaInf;
		objetivos.add(pasajerosPorCompanyiaDesvTipPerdida); // Homogen pasajerosCom
		objetivos.add(Tasasporcentaje);// Tasas
		this.Tasastotal = Tasastotal;
		objetivos.add(ingresoPorAerDestDesvTip); // Homogen tasas aeropuerto dest

		objetivos.add(Pasajerosporcentaje);// Pasajeros
		this.Pasajerostotal = Pasajerostotal;
		objetivos.add(Conectividadsolucion);// Conectividad
		this.ConectividadtotalSuma = ConectividadtotalSuma;
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
							Stream.of(
									pasajerosPorCompanyia.get(this.datos.getCompanyiasTotales().get(pos)).get(0)
											+ this.datos.getPasajeros().get(pos) * ind.getVariables().get(i),
									pasajerosPorCompanyia.get(this.datos.getCompanyiasTotales().get(pos)).get(1)
											+ this.datos.getPasajeros().get(pos))
									.collect(Collectors.toList()));
				} else {
					pasajerosPorCompanyia.put(this.datos.getCompanyiasTotales().get(pos),
							Stream.of(this.datos.getPasajeros().get(pos) * ind.getVariables().get(i),
									this.datos.getPasajeros().get(pos) * 1.0).collect(Collectors.toList()));
				}
				/*
				 * if (pasajerosPorCompanyia.get(this.datos.getCompanyiasTotales().get(pos)) !=
				 * null) { pasajerosPorCompanyia.put(this.datos.getCompanyiasTotales().get(pos),
				 * Stream.of(pasajerosPorCompanyia.get(this.datos.getCompanyiasTotales().get(pos
				 * )).get(0) + this.datos.getPasajeros().get(pos) *
				 * solucion.getVariables().get(i),
				 * pasajerosPorCompanyia.get(this.datos.getCompanyiasTotales().get(pos)).get(1)
				 * + this.datos.getPasajeros().get(pos))); } else {
				 * pasajerosPorCompanyia.put(this.datos.getCompanyiasTotales().get(pos),
				 * Stream.of(this.datos.getPasajeros().get(pos) *
				 * solucion.getVariables().get(i), this.datos.getPasajeros().get(pos) * 1.0)); }
				 */
				if (ingresosPorAreaInf.get(this.datos.getAresInfTotales().get(pos)) != null) {
					ingresosPorAreaInf.put(this.datos.getAresInfTotales().get(pos),
							Stream.of(
									ingresosPorAreaInf.get(this.datos.getAresInfTotales().get(pos)).get(0)
											+ this.datos.getIngresos().get(pos) * ind.getVariables().get(i),
									ingresosPorAreaInf.get(this.datos.getAresInfTotales().get(pos)).get(1)
											+ this.datos.getIngresos().get(pos))
									.collect(Collectors.toList()));
				} else {
					ingresosPorAreaInf.put(this.datos.getAresInfTotales().get(pos),
							Stream.of(this.datos.getIngresos().get(pos) * ind.getVariables().get(i),
									this.datos.getIngresos().get(pos)).collect(Collectors.toList()));
				}
				if (ingresosPorAerDest.get(this.datos.getConexionesTotalesSeparadas().get(pos).get(1)) != null) {
					ingresosPorAerDest.put(this.datos.getConexionesTotalesSeparadas().get(pos).get(1), Stream.of(
							ingresosPorAerDest.get(this.datos.getConexionesTotalesSeparadas().get(pos).get(1)).get(0)
									+ this.datos.getTasas().get(pos) * ind.getVariables().get(i),
							ingresosPorAerDest.get(this.datos.getConexionesTotalesSeparadas().get(pos).get(1)).get(0)
									+ this.datos.getTasas().get(pos))
							.collect(Collectors.toList()));
				} else {
					ingresosPorAerDest.put(this.datos.getConexionesTotalesSeparadas().get(pos).get(1),
							Stream.of(this.datos.getTasas().get(pos) * ind.getVariables().get(i),
									this.datos.getTasas().get(pos)).collect(Collectors.toList()));
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
				ingresoPerdidoAreasInf.add(
						100 * (1 - (ingresosPorAreaInf.get(areaInf).get(0) / ingresosPorAreaInf.get(areaInf).get(1))));
			}
		}

		for (String aeropuerto : ingresosPorAerDest.keySet()) {
			if (ingresosPorAerDest.get(aeropuerto).get(1) == 0.0) {
				ingresoPerdidoAerDest.add(1.0 * 100); // TODO: Añadir datos restantes sobre tasas (tarifa por ruido del
														// avion)
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
	
	private Individuo comprobarOrden(Individuo ind, List<Double> objetivos) {
		if (objetivos.get(this.orderRes.get(0)) <= objetivos.get(this.orderRes.get(1))
				&& objetivos.get(this.orderRes.get(1)) <= objetivos.get(this.orderRes.get(2))) {
			
		} else if (objetivos.get(this.orderRes.get(0)) > objetivos.get(this.orderRes.get(1))
				&& objetivos.get(this.orderRes.get(1)) <= objetivos.get(this.orderRes.get(2))) {
			ind.setFactible(false);
			ind.setConstraintViolation(
					ind.getConstraintViolation() + 
					(objetivos.get(this.orderRes.get(0)) - objetivos.get(this.orderRes.get(1))));
			
		} else if (objetivos.get(this.orderRes.get(0)) <= objetivos.get(this.orderRes.get(1))
				&& objetivos.get(this.orderRes.get(1)) > objetivos.get(this.orderRes.get(2))) {
			ind.setFactible(false);
			ind.setConstraintViolation(
					ind.getConstraintViolation() + 
					(objetivos.get(this.orderRes.get(1)) - objetivos.get(this.orderRes.get(2))));
			
		} else if (objetivos.get(this.orderRes.get(0)) > objetivos.get(this.orderRes.get(1))
				&& objetivos.get(this.orderRes.get(1)) > objetivos.get(this.orderRes.get(2))) {
			ind.setFactible(false);
			ind.setConstraintViolation(
					ind.getConstraintViolation() + 
					(objetivos.get(this.orderRes.get(0)) - objetivos.get(this.orderRes.get(1))));
			ind.setConstraintViolation(
					ind.getConstraintViolation() + 
					(objetivos.get(this.orderRes.get(1)) - objetivos.get(2)));
			
		}
		return ind;
	}
	
	@Override
	public Individuo repararMejorar(Individuo solucion) {
		
		if(solucion.getRestricciones().get(0) > this.resSup) {
			while (solucion.getRestricciones().get(0) > this.resSup) {
				if(Utils.getRandNumber(0.0, 1.0) >= this.SRate) {
					Double maxRatio = Double.MIN_VALUE;
					int maxRatioPos = 0;
					for(int i = 0; i < this.getNumVariables(); i++) {
						if(solucion.getVariables().get(i) == 1.0 && this.evalKP(i)/(this.datos.getRiesgos_KP().get(i)/this.RiesgosumatorioTotal) > maxRatio) {
							maxRatio = this.evalKP(i)/(this.datos.getRiesgos_KP().get(i)/this.RiesgosumatorioTotal);
							maxRatioPos = i;
						}
					}
					solucion.modIVariable(maxRatioPos, 0.0);
				}else {
					int minRatioPos = Utils.getRandNumber(0, getNumVariables());
					solucion.modIVariable(minRatioPos, 0.0);
				}
				this.evaluate(solucion);
			}
		}if(solucion.getRestricciones().get(0) < this.resSup){
			boolean terminate = false;
			while (!terminate) {
				//Double dif = this.resSup - solucion.getRestricciones().get(0);
				
				List<Double> candidatos = new ArrayList<>();
				Map<Double, Integer> candidatosRatio = new HashMap<>();
				
				for(int i = 0; i < this.getNumVariables(); i++) {
					if(solucion.getVariables().get(i) == 0.0 && (this.resSup - (solucion.getExtra().get(Constantes.nombreRiesgoImportado).get(0) + this.datos.getRiesgos_KP().get(i)) / this.RiesgosumatorioTotal) >= 0.0) {
						candidatos.add(this.evalKP(i)/(this.datos.getRiesgos_KP().get(i)/this.RiesgosumatorioTotal));
						candidatosRatio.put(this.evalKP(i)/(this.datos.getRiesgos_KP().get(i)/this.RiesgosumatorioTotal), i);
					}
				}
				if(candidatos.size() != 0) {
					Collections.sort(candidatos);
					
					if(Utils.getRandNumber(0.0, 1.0) >= this.SRate) {
						int pos = candidatosRatio.get(candidatos.get(0));
						solucion.modIVariable(pos, 1.0);
					}else {
						int pos = candidatosRatio.get(candidatos.get(Utils.getRandNumber(0, candidatos.size())));
						solucion.modIVariable(pos, 1.0);
					}
					this.evaluate(solucion);
				}else {
					terminate = true;
				}
				
			}
		}
		
		return solucion;
	}
	
	public double evalKP(int posicion) {
		double eval = 0.0;
		double ingresos = 0.0;
		double pasajeros = 0.0;
		double tasas = 0.0;
		double conectividad = 0.0;
		double HingresosAreaInf = 0.0;
		double mediaPerdidaIngresosPAreaInf = 0.0;
		//double conectividadTotal = 0.0;
		
		ingresos = 1.0 - (this.datos.getIngresos_KP().get(posicion) / this.IngresosTtotalSuma);
		tasas = 1.0 - (this.datos.getTasas_KP().get(posicion) / this.Tasastotal);
		pasajeros = 1.0 - (this.datos.getPasajeros_KP().get(posicion)*1.0 / this.Pasajerostotal*1.0);
		
		if(this.datos.getVuelosEntrantesConexionOrdenadoTotalTotales().get(posicion)*1.0 != 0.0) {
			conectividad = 1.0 - (this.datos.getVuelosEntrantesConexionOrdenadoTotales().get(posicion)*1.0 / this.datos.getVuelosEntrantesConexionOrdenadoTotalTotales().get(posicion)*1.0);
		}
		conectividad *= this.datos.getConectividadesTotales().get(posicion);
		
		if(this.ConectividadtotalSuma != 0.0) {
			conectividad /= this.ConectividadtotalSuma;
		}
		
		mediaPerdidaIngresosPAreaInf = ((this.ingresosPorAreaInf.keySet().size()-1) * 1.0 + (1.0 - (this.datos.getIngresos().get(posicion)/this.datos.getIngresosAreaInfTotalTotales().get(posicion))))/this.ingresosPorAreaInf.keySet().size();
		
		for(int i = 0; i < this.ingresosPorAreaInf.keySet().size(); i++) {
			if(i == 0) {
				HingresosAreaInf += Math.pow((1.0 - (this.datos.getIngresos().get(posicion)/this.datos.getIngresosAreaInfTotalTotales().get(posicion))) - mediaPerdidaIngresosPAreaInf, 2);
			}else {
				HingresosAreaInf += Math.pow(1.0 - mediaPerdidaIngresosPAreaInf, 2);
			}
		}
		
		HingresosAreaInf /= this.ingresosPorAreaInf.keySet().size();
		
		HingresosAreaInf = Math.sqrt(HingresosAreaInf);
		
		eval += ingresos * this.preferencias.getWeightsVector().get(0);
		eval += HingresosAreaInf * this.preferencias.getWeightsVector().get(1);
		eval += tasas * this.preferencias.getWeightsVector().get(3);
		eval += pasajeros * this.preferencias.getWeightsVector().get(5);
		eval += conectividad * this.preferencias.getWeightsVector().get(6);
		
		return eval;
	}

	public DatosRRPS_PAT getDatos() {
		return datos;
	}

	@Override
	public void sumarNumInicializaciones() {
		this.numInicializaciones++;
	}

}
