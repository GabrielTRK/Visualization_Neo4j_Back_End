package com.VisNeo4j.App.Problems;

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
import com.VisNeo4j.App.Problems.Data.DataRRPS_PAT;
import com.VisNeo4j.App.QDMP.DMPreferences;
import com.VisNeo4j.App.Utils.Utils;

public class RRPS_PAT_ALT extends Problem {

	private List<Double> resInf;
	private List<Double> resSup;
	private DataRRPS_PAT datos;
	private int numInicializaciones = 0;
	private DMPreferences preferencias;
	private List<String> resPol;
	private List<Integer> direccionesAMantener;
	private List<Integer> objetivosRestriccion = Stream.of(2, 3, 5).collect(Collectors.toList());
	private List<Integer> orderRes = new ArrayList<>();

	public RRPS_PAT_ALT(DataRRPS_PAT datos, List<Double> maxRiesgos, List<String> resPol, DMPreferences preferencias) {
		super(0, 1);
		this.datos = datos;
		this.direccionesAMantener = new ArrayList<>();
		this.resPol = resPol;

		this.calcularDireccionesAMantener();
		super.setNumVariables(
				(this.datos.getConexionesTotales().size() - this.direccionesAMantener.size()) * datos.getNumDias());
		this.resInf = new ArrayList<>();
		for (int i = 0; i < this.datos.getNumDias(); i++) {
			this.resInf.add(0.0);
		}
		this.resSup = maxRiesgos;
		this.preferencias = preferencias;
		super.setNombre(Constantes.nombreProblemaRRPS_PAT);

		for (int obj : this.preferencias.getOrder().getOrder()) {

			if (this.objetivosRestriccion.contains(obj)) {
				this.orderRes.add((obj - 1) + this.datos.getNumDias());
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

		List<Double> restricciones = new ArrayList<>(this.datos.getNumDias());

		restricciones = objetivos.subList(0, this.datos.getNumDias());

		ind.setRestricciones(restricciones);
		this.comprobarRestricciones(ind);
		this.comprobarOrden(ind, objetivos);

		ind.setObjetivosNorm(objetivos.subList(this.datos.getNumDias(), objetivos.size()));
		Double sumaPesos = 0.0;

		for (int i = this.datos.getNumDias(); i < objetivos.size(); i++) {
			sumaPesos += objetivos.get(i) * this.preferencias.getWeightsVector().get(i - this.datos.getNumDias());
		}

		ind.setObjetivos(Stream.of(sumaPesos).collect(Collectors.toList()));

		this.quitarDirecciones(aux);

		ind.setVariables(aux);

		return ind;
	}

	private List<Double> calcularObjetivos(Individuo solucion) {
		List<Double> Knapsacks = new ArrayList<>();
		List<Double> KnapsacksTotal = new ArrayList<>();

		for (int i = 0; i < this.datos.getNumDias(); i++) {
			Knapsacks.add(0.0);
			KnapsacksTotal.add(0.0);
		}

		List<Double> objetivos = new ArrayList<>();
		// Double Riesgosumatorio = 0.0;
		// Double RiesgosumatorioTotal = 0.0;

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
		int offset = 0;
		for (int i = 0; i < this.datos.getConexionesTotales().size(); i++) {
			Double bit = this.comprobarUno(solucion.getVariables().subList(offset, offset + this.datos.getNumDias()));
			// Calcular dia
			int dia = this.calcularDia(i);
			while (pos < this.datos.getConexionesTotalesSeparadas().size()
					&& this.datos.getConexionesTotalesSeparadas().get(pos).get(0)
							.equals(this.datos.getConexionesTotales().get(i).get(0))
					&& this.datos.getConexionesTotalesSeparadas().get(pos).get(1)
							.equals(this.datos.getConexionesTotales().get(i).get(1))) {
				double knapsackI = KnapsacksTotal.get(dia);
				// Riesgosumatorio += this.datos.getRiesgos().get(pos) * bit;
				// RiesgosumatorioTotal += this.datos.getRiesgos().get(pos);
				this.calcularKnapsacks(pos, solucion.getVariables().subList(offset, offset + this.datos.getNumDias()),
						Knapsacks);
				KnapsacksTotal.set(dia, knapsackI + this.datos.getRiesgos().get(pos));

				IngresosTsuma += this.datos.getIngresos().get(pos) * bit;
				IngresosTtotalSuma += this.datos.getIngresos().get(pos);

				Pasajerossumatorio += this.datos.getPasajeros().get(pos) * bit;
				Pasajerostotal += this.datos.getPasajeros().get(pos);

				Tasassumatorio += this.datos.getTasas().get(pos) * bit;
				Tasastotal += this.datos.getTasas().get(pos);

				if (pasajerosPorCompanyia.get(this.datos.getCompanyiasTotales().get(pos)) != null) {
					pasajerosPorCompanyia.put(this.datos.getCompanyiasTotales().get(pos),
							Stream.of(
									pasajerosPorCompanyia.get(this.datos.getCompanyiasTotales().get(pos)).get(0)
											+ this.datos.getPasajeros().get(pos) * bit,
									pasajerosPorCompanyia.get(this.datos.getCompanyiasTotales().get(pos)).get(1)
											+ this.datos.getPasajeros().get(pos))
									.collect(Collectors.toList()));
				} else {
					pasajerosPorCompanyia.put(this.datos.getCompanyiasTotales().get(pos), Stream
							.of(this.datos.getPasajeros().get(pos) * bit, this.datos.getPasajeros().get(pos) * 1.0)
							.collect(Collectors.toList()));
				}
				if (ingresosPorAreaInf.get(this.datos.getAresInfTotales().get(pos)) != null) {
					ingresosPorAreaInf.put(this.datos.getAresInfTotales().get(pos),
							Stream.of(
									ingresosPorAreaInf.get(this.datos.getAresInfTotales().get(pos)).get(0)
											+ this.datos.getIngresos().get(pos) * bit,
									ingresosPorAreaInf.get(this.datos.getAresInfTotales().get(pos)).get(1)
											+ this.datos.getIngresos().get(pos))
									.collect(Collectors.toList()));
				} else {
					ingresosPorAreaInf.put(this.datos.getAresInfTotales().get(pos),
							Stream.of(this.datos.getIngresos().get(pos) * bit, this.datos.getIngresos().get(pos))
									.collect(Collectors.toList()));
				}
				if (ingresosPorAerDest.get(this.datos.getConexionesTotalesSeparadas().get(pos).get(1)) != null) {
					ingresosPorAerDest.put(this.datos.getConexionesTotalesSeparadas().get(pos).get(1), Stream.of(
							ingresosPorAerDest.get(this.datos.getConexionesTotalesSeparadas().get(pos).get(1)).get(0)
									+ this.datos.getTasas().get(pos) * bit,
							ingresosPorAerDest.get(this.datos.getConexionesTotalesSeparadas().get(pos).get(1)).get(0)
									+ this.datos.getTasas().get(pos))
							.collect(Collectors.toList()));
				} else {
					ingresosPorAerDest.put(this.datos.getConexionesTotalesSeparadas().get(pos).get(1),
							Stream.of(this.datos.getTasas().get(pos) * bit, this.datos.getTasas().get(pos))
									.collect(Collectors.toList()));
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
			ConectividadauxSuma += bit * this.datos.getVuelosEntrantesConexionOrdenadoTotales().get(i);
			ConectividadauxTotalSuma += this.datos.getVuelosEntrantesConexionOrdenadoTotales().get(i);
			offset += this.datos.getNumDias();
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

		// objetivos.add(Riesgosumatorio / RiesgosumatorioTotal);// Riesgo
		for (int i = 0; i < Knapsacks.size(); i++) {
			Knapsacks.set(i, Knapsacks.get(i) / KnapsacksTotal.get(i));
		}

		objetivos.addAll(Knapsacks);

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
			
			//valores.add(i, Utils.getRandBinNumber());

			if (this.numInicializaciones == 0) {
				valores.add(i, 0.0);
			} else if (this.numInicializaciones == 1) {
				valores.add(i, 1.0);
			} else {
				if (i == this.numInicializaciones - 2) {
					valores.add(i, 1.0);
				} else {
					valores.add(i, 0.0);
				}
			}

			/*if (this.numInicializaciones < super.getNumVariables()) {
				if (i == this.numInicializaciones) {
					valores.add(i, 1.0);
				} else {
					valores.add(i, 0.0);
				}
			} else if (this.numInicializaciones == super.getNumVariables()) {
				valores.add(i, 0.0);
			} else if (this.numInicializaciones == super.getNumVariables() + 1) {
				valores.add(i, 1.0);
			}*/

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
			List<Double> bitsReemplazo = new ArrayList<>();
			int dia = this.calcularDia(this.direccionesAMantener.get(i));
			for (int j = 0; j < this.datos.getNumDias(); j++) {
				bitsReemplazo.add(0.0);
			}
			bitsReemplazo.set(dia, 1.0);

			variables.addAll(this.datos.getNumDias() * this.direccionesAMantener.get(i), bitsReemplazo);
		}

		return variables;
	}

	@Override
	public Individuo devolverSolucionCompleta(Individuo ind) {

		List<Double> aux = ind.getVariables();

		for (int i = 0; i < this.direccionesAMantener.size(); i++) {
			List<Double> bitsReemplazo = new ArrayList<>();
			int dia = this.calcularDia(this.direccionesAMantener.get(i));
			for (int j = 0; j < this.datos.getNumDias(); j++) {
				bitsReemplazo.add(0.0);
			}
			bitsReemplazo.set(dia, 1.0);

			aux.addAll(this.datos.getNumDias() * this.direccionesAMantener.get(i), bitsReemplazo);
		}

		ind.setVariables(aux);

		return ind;
	}

	private void quitarDirecciones(List<Double> variables) {
		int cont = 0;

		for (int i = 0; i < this.direccionesAMantener.size(); i++) {

			for (int j = 0; j < this.datos.getNumDias(); j++) {
				variables.remove((this.direccionesAMantener.get(i) - cont) * this.datos.getNumDias());
			}

			cont++;
		}

	}

	@Override
	public Individuo comprobarRestricciones(Individuo ind) {
		double ConsV = 0.0;
		// Bucle que recorre los bits de cada conexion
		int offset = 0;
		for (int i = 0; i < this.datos.getConexionesTotales().size(); i++) {
			List<Double> bits = ind.getVariables().subList(offset, offset + this.datos.getNumDias());
			int count = 0;
			for (int j = 0; j < bits.size(); j++) {
				if (bits.get(j) == 1.0) {
					count++;
				}
			}
			if (count > 0) {
				ConsV += 500.0 * (count - 1);
			}
			offset += this.datos.getNumDias();
		}

		for (int i = 0; i < this.resSup.size(); i++) {
			if (ind.getRestricciones().get(i) > this.resSup.get(i)) {
				ConsV += Math.abs(this.resSup.get(i) - ind.getRestricciones().get(i));
			} else if (ind.getRestricciones().get(i) < this.resInf.get(i)) {
				ConsV += Math.abs(this.resInf.get(i) - ind.getRestricciones().get(i));
			}
		}

		if (ConsV == 0.0) {
			ind.setFactible(true);
		} else {
			ind.setFactible(false);
		}
		ind.setConstraintViolation(ConsV);

		return ind;
	}

	private Individuo comprobarOrden(Individuo ind, List<Double> objetivos) {
		if (objetivos.get(this.orderRes.get(0)) <= objetivos.get(this.orderRes.get(1))
				&& objetivos.get(this.orderRes.get(1)) <= objetivos.get(this.orderRes.get(2))) {

		} else if (objetivos.get(this.orderRes.get(0)) > objetivos.get(this.orderRes.get(1))
				&& objetivos.get(this.orderRes.get(1)) <= objetivos.get(this.orderRes.get(2))) {
			ind.setFactible(false);
			ind.setConstraintViolation(ind.getConstraintViolation()
					+ (objetivos.get(this.orderRes.get(0)) - objetivos.get(this.orderRes.get(1))));

		} else if (objetivos.get(this.orderRes.get(0)) <= objetivos.get(this.orderRes.get(1))
				&& objetivos.get(this.orderRes.get(1)) > objetivos.get(this.orderRes.get(2))) {
			ind.setFactible(false);
			ind.setConstraintViolation(ind.getConstraintViolation()
					+ (objetivos.get(this.orderRes.get(1)) - objetivos.get(this.orderRes.get(2))));

		} else if (objetivos.get(this.orderRes.get(0)) > objetivos.get(this.orderRes.get(1))
				&& objetivos.get(this.orderRes.get(1)) > objetivos.get(this.orderRes.get(2))) {
			ind.setFactible(false);
			ind.setConstraintViolation(ind.getConstraintViolation()
					+ (objetivos.get(this.orderRes.get(0)) - objetivos.get(this.orderRes.get(1))));
			ind.setConstraintViolation(
					ind.getConstraintViolation() + (objetivos.get(this.orderRes.get(1)) - objetivos.get(2)));

		}
		return ind;
	}

	private Double comprobarUno(List<Double> bitsKnapsack) {
		for (int i = 0; i < bitsKnapsack.size(); i++) {
			if (bitsKnapsack.get(i) == 1.0) {
				return 1.0;
			}
		}
		return 0.0;
	}

	private int calcularDia(int i) {
		int offset = 0;
		int dia;
		for (dia = 0; dia < this.datos.getDatosPorDia().size(); dia++) {
			if (i < offset + this.datos.getDatosPorDia().get(dia).getConexiones().size()) {
				return dia;
			} else {
				offset += this.datos.getDatosPorDia().get(dia).getConexiones().size();
			}
		}
		return dia;
	}

	private void calcularKnapsacks(int pos, List<Double> bits, List<Double> Knapsacks) {
		for (int i = 0; i < bits.size(); i++) {
			if (bits.get(i) == 1.0) {
				Knapsacks.set(i, Knapsacks.get(i) + this.datos.getRiesgos().get(pos));
			}
		}
	}

	public DataRRPS_PAT getDatos() {
		return datos;
	}

	@Override
	public void sumarNumInicializaciones() {
		this.numInicializaciones++;
	}

}
