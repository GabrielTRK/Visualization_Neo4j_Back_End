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
import com.VisNeo4j.App.Model.Particle;
import com.VisNeo4j.App.Problems.Data.DataRRPS_PAT;
import com.VisNeo4j.App.QDMP.DMPreferences;
import com.VisNeo4j.App.Utils.Utils;

public class RRPS_PAT extends Problem {
	
	private Double SRate = 0.5;

	private Double resInf = 0.0;
	private Double resSup;
	private DataRRPS_PAT datos;
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
	
	private Map<String, List<Double>> vuelosEntrantesOrigen = new HashMap<>();
	
	private List<Double> evalKP = new ArrayList<>();
	

	public RRPS_PAT(DataRRPS_PAT datos, Double maxRiesgo, List<String> resPol, DMPreferences preferencias) {
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
	
	public RRPS_PAT(DataRRPS_PAT datos, List<String> resPol, DMPreferences preferencias) {
		super(0, 1);
		
		this.datos = datos;
		this.direccionesAMantener = new ArrayList<>();
		this.resPol = resPol;

		this.calcularDireccionesAMantener();
		super.setNumVariables(this.datos.getConexionesTotales().size() - this.direccionesAMantener.size());
		this.preferencias = preferencias;
		super.setNombre(Constantes.nombreProblemaRRPS_PAT);

		for (int obj : this.preferencias.getOrder().getOrder()) {

			if (this.objetivosRestriccion.contains(obj)) {
				this.orderRes.add(obj);
			}
		}
	}
	
	public RRPS_PAT(DataRRPS_PAT datos, List<String> resPol, DMPreferences preferencias, double SRate) {
		super(0, 1);
		
		this.SRate = SRate;
		this.datos = datos;
		this.direccionesAMantener = new ArrayList<>();
		this.resPol = resPol;

		this.calcularDireccionesAMantener();
		super.setNumVariables(this.datos.getConexionesTotales().size() - this.direccionesAMantener.size());
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
	public Particle evaluate(Particle ind) {
		List<Double> aux = ind.getVariables();

		this.rellenarDirecciones(aux);

		ind.setVariables(aux);

		List<Double> objetivos = this.calcularObjetivos(ind);

		List<Double> restricciones = new ArrayList<>(1);

		restricciones.add(0, objetivos.get(0));
		ind.setConstraints(restricciones);
		ind.setObjectivesNorm(objetivos.subList(0, objetivos.size()));
		this.comprobarRestricciones(ind);
		this.comprobarOrden(ind, objetivos);
		
		Double sumaPesos = 0.0;
		
		for(int i = 0; i < this.preferencias.getOrder().getOrder().size(); i++) {
			sumaPesos += objetivos.get(this.preferencias.getOrder().getOrder().get(i)) 
					* this.preferencias.getIdWeights().get(this.preferencias.getOrder().getOrder().get(i));
		}

		/*Double res = 0.0;
		
		for(int id : this.preferencias.getOrder().getRestricciones().keySet()) {
			res += (1.0 - objetivos.get(id)) * (1.0 / this.preferencias.getOrder().getRestricciones().keySet().size());
		}*/

		ind.setObjectives(Stream.of(sumaPesos).collect(Collectors.toList()));

		return ind;
	}
	
	public Particle evaluate2(Particle ind) {

		List<Double> objetivos = this.calcularObjetivos(ind);

		List<Double> restricciones = new ArrayList<>(1);

		restricciones.add(0, objetivos.get(0));
		ind.setConstraints(restricciones);
		ind.setObjectivesNorm(objetivos.subList(0, objetivos.size()));
		this.comprobarRestricciones(ind);
		this.comprobarOrden(ind, objetivos);
		
		Double sumaPesos = 0.0;

		for(int i = 0; i < this.preferencias.getOrder().getOrder().size(); i++) {
			sumaPesos += objetivos.get(this.preferencias.getOrder().getOrder().get(i)) 
					* this.preferencias.getIdWeights().get(this.preferencias.getOrder().getOrder().get(i));
		}
		
		/*Double res = 0.0;
		
		for(int id : this.preferencias.getOrder().getRestricciones().keySet()) {
			res += (1.0 - objetivos.get(id)) * (1.0 / this.preferencias.getOrder().getRestricciones().keySet().size());
		}*/

		ind.setObjectives(Stream.of(sumaPesos).collect(Collectors.toList()));

		return ind;
	}

	private List<Double> calcularObjetivos(Particle solucion) {
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
		int dia = 0;
		int numConexiones = this.datos.getDatosPorDia().get(dia).getConexiones().size();
		int offset = 0;
		for (int i = 0; i < this.datos.getConexionesTotales().size(); i++) {
			//Calcular num dia
			if(i >= numConexiones + offset) {
				offset += numConexiones;
				dia = this.calcularDia(i);
				numConexiones = this.datos.getDatosPorDia().get(dia).getConexiones().size();
			}
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
				if (ingresosPorAerDest.get(this.datos.getConexionesNombresTotalesSeparadas().get(pos).get(1)) != null) {
					ingresosPorAerDest.put(this.datos.getConexionesNombresTotalesSeparadas().get(pos).get(1), Stream.of(
							ingresosPorAerDest.get(this.datos.getConexionesNombresTotalesSeparadas().get(pos).get(1)).get(0)
									+ this.datos.getTasas().get(pos) * solucion.getVariables().get(i),
							ingresosPorAerDest.get(this.datos.getConexionesNombresTotalesSeparadas().get(pos).get(1)).get(1)
									+ this.datos.getTasas().get(pos))
							.collect(Collectors.toList()));
				} else {
					ingresosPorAerDest.put(this.datos.getConexionesNombresTotalesSeparadas().get(pos).get(1),
							Stream.of(this.datos.getTasas().get(pos) * solucion.getVariables().get(i),
									this.datos.getTasas().get(pos)).collect(Collectors.toList()));
				}

				pos++;
			}
			
			if (!this.datos.getConexionesTotales().get(i).get(0).equals(origen)) {
				if (i > 0) {
					Double Conectividadaux = 0.0;
					if (ConectividadauxTotalSuma != 0) {
						this.vuelosEntrantesOrigen.put(origen + String.valueOf(dia), Stream.of(ConectividadauxSuma).collect(Collectors.toList()));
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
		this.vuelosEntrantesOrigen.put(origen + String.valueOf(dia), Stream.of(ConectividadauxSuma).collect(Collectors.toList()));
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
		pasajerosPorCompanyiaMediaPerdida /= pasajerosPorCompanyia.keySet().size();
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
		//solucion.initExtra();
		Map<String, List<Double>> valoresAdicionales = solucion.getExtra();
		
		
		valoresAdicionales.put(Constantes.nombreRiesgoImportado, Stream.of(Riesgosumatorio).collect(Collectors.toList()));
		valoresAdicionales.putAll(ingresosPorAerDest);
		valoresAdicionales.putAll(ingresosPorAreaInf);
		valoresAdicionales.putAll(pasajerosPorCompanyia);
		valoresAdicionales.putAll(this.vuelosEntrantesOrigen);
		objetivos.add(Riesgosumatorio / RiesgosumatorioTotal);// Riesgo
		this.RiesgosumatorioTotal = RiesgosumatorioTotal;
		//solucion.getIdObjetivos().put(0, Riesgosumatorio / RiesgosumatorioTotal);

		// -------------OBJETIVOS----------------

		objetivos.add(1 - Ingresosaux);// Ingresos
		this.IngresosTtotalSuma = IngresosTtotalSuma;
		//solucion.getIdObjetivos().put(1, 1 - Ingresosaux);
		
		
		objetivos.add(ingresoPerdidoAreasInfDesvTip); // Homogen ingresos areas inf
		this.ingresosPorAreaInf = ingresosPorAreaInf;
		//solucion.getIdObjetivos().put(2, ingresoPerdidoAreasInfDesvTip);
		
		objetivos.add(pasajerosPorCompanyiaDesvTipPerdida); // Homogen pasajerosCom
		this.pasajerosPorCompanyia = pasajerosPorCompanyia;
		//solucion.getIdObjetivos().put(3, pasajerosPorCompanyiaDesvTipPerdida);
		
		objetivos.add(Tasasporcentaje);// Tasas
		this.Tasastotal = Tasastotal;
		//solucion.getIdObjetivos().put(4, Tasasporcentaje);
		
		objetivos.add(ingresoPorAerDestDesvTip); // Homogen tasas aeropuerto dest
		this.ingresosPorAerDest = ingresosPorAerDest;
		//solucion.getIdObjetivos().put(5, ingresoPorAerDestDesvTip);

		objetivos.add(Pasajerosporcentaje);// Pasajeros
		this.Pasajerostotal = Pasajerostotal;
		//solucion.getIdObjetivos().put(6, Pasajerosporcentaje);
		
		objetivos.add(Conectividadsolucion);// Conectividad
		this.ConectividadtotalSuma = ConectividadtotalSuma;
		//solucion.getIdObjetivos().put(7, Conectividadsolucion);
		
		return objetivos;
	}

	@Override
	public Particle initializeValues(Particle ind) {
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
	public Particle extra(Particle ind) {

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
	public Particle devolverSolucionCompleta(Particle ind) {

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
	public Particle comprobarRestricciones(Particle ind) {
		double constraintV = 0.0;
		ind.setConstraintViolation(constraintV);
		ind.setFeasible(true);
		
		for(int res : this.preferencias.getOrder().getRestricciones().keySet()) {
			if((res == 1 || res == 4 || res == 6 || res == 7) && 1 - ind.getObjectivesNorm().get(res) > this.preferencias.getOrder().getRestricciones().get(res) / 100) {
				ind.setFeasible(false);
				constraintV += Math.abs(this.preferencias.getOrder().getRestricciones().get(res)/100 - (1 - ind.getObjectivesNorm().get(res)));
			}else if((res == 0 || res == 2 || res == 3 || res == 5) && ind.getObjectivesNorm().get(res) > this.preferencias.getOrder().getRestricciones().get(res) / 100) {
				ind.setFeasible(false);
				constraintV += Math.abs(this.preferencias.getOrder().getRestricciones().get(res)/100 - ind.getObjectivesNorm().get(res));
			}
		}
		ind.setConstraintViolation(constraintV);
		
		/*if (ind.getRestricciones().get(0) > this.resSup) {
			ind.setFeasible(false);
			ind.setConstraintViolation(Math.abs(this.resSup - ind.getRestricciones().get(0)));
		} else if (ind.getRestricciones().get(0) < this.resInf) {
			ind.setFeasible(false);
			ind.setConstraintViolation(Math.abs(this.resInf - ind.getRestricciones().get(0)));
		} else {
			ind.setFeasible(true);
			ind.setConstraintViolation(0.0);
		}*/

		return ind;
	}
	
	private Particle comprobarOrden(Particle ind, List<Double> objetivos) {
		
		if(this.orderRes.size() == 3) {
			if (objetivos.get(this.orderRes.get(0)) <= objetivos.get(this.orderRes.get(1))
					&& objetivos.get(this.orderRes.get(1)) <= objetivos.get(this.orderRes.get(2))) {
				
			} else if (objetivos.get(this.orderRes.get(0)) > objetivos.get(this.orderRes.get(1))
					&& objetivos.get(this.orderRes.get(1)) <= objetivos.get(this.orderRes.get(2))) {
				ind.setFeasible(false);
				ind.setConstraintViolation(
						ind.getConstraintViolation() + 
						(objetivos.get(this.orderRes.get(0)) - objetivos.get(this.orderRes.get(1))));
				
			} else if (objetivos.get(this.orderRes.get(0)) <= objetivos.get(this.orderRes.get(1))
					&& objetivos.get(this.orderRes.get(1)) > objetivos.get(this.orderRes.get(2))) {
				ind.setFeasible(false);
				ind.setConstraintViolation(
						ind.getConstraintViolation() + 
						(objetivos.get(this.orderRes.get(1)) - objetivos.get(this.orderRes.get(2))));
				
			} else if (objetivos.get(this.orderRes.get(0)) > objetivos.get(this.orderRes.get(1))
					&& objetivos.get(this.orderRes.get(1)) > objetivos.get(this.orderRes.get(2))) {
				ind.setFeasible(false);
				ind.setConstraintViolation(
						ind.getConstraintViolation() + 
						(objetivos.get(this.orderRes.get(0)) - objetivos.get(this.orderRes.get(1))));
				ind.setConstraintViolation(
						ind.getConstraintViolation() + 
						(objetivos.get(this.orderRes.get(1)) - objetivos.get(2)));
				
			}
		}else if(this.orderRes.size() == 2) {
			if(objetivos.get(this.orderRes.get(0)) > objetivos.get(this.orderRes.get(1))){
				ind.setFeasible(false);
				ind.setConstraintViolation(
						ind.getConstraintViolation() + 
						(objetivos.get(this.orderRes.get(0)) - objetivos.get(this.orderRes.get(1))));
			}
		}
		
		
		return ind;
	}
	
	@Override
	public Particle repairImprove(Particle solucion) {
		this.calcularEvalKP();
		//Se debe intentar reparar la solución quitando conexiones y añadiendo.
		//Se debe intentar mejorar la solución quitando conexiones y añadiendo.
		//Al final del método se comparan las soluciones obtenidas por ambas vías y descartamos la peor
		Particle solucion2 = Utils.copiarIndividuo(solucion);
		
		if(this.preferencias.getOrder().getRestricciones().keySet().size() >= 1) {
			List<Integer> idsRes = this.restricionesInfactibles(solucion.getObjectivesNorm());
			List<Integer> idsRes2 = this.restricionesInfactibles(solucion2.getObjectivesNorm());
			
			if(idsRes.size() != 0 || idsRes2.size() != 0) {
				boolean reparar1 = (idsRes.size() != 0 && this.comprobarUnos(solucion));
				boolean reparar2 = (idsRes2.size() != 0 && this.comprobarCeros(solucion2));
				
				while (reparar1 || reparar2) {
					if(Utils.getRandNumber(0.0, 1.0) >= this.SRate) {
						Double maxRatio = Double.MIN_VALUE;
						int maxRatioPos = 0;
							
						Double minRatio = Double.MAX_VALUE;
						int minRatioPos = 0;
							
						for(int i = 0; i < this.datos.getConexionesTotales().size(); i++) {
							if(reparar1) {
								if(this.evalKP.get(i) != -1.0 && solucion.getVariables().get(i) == 1.0) {
									double ratio = this.evalKP.get(i);
									if(ratio > maxRatio) {
										maxRatio = ratio;
										maxRatioPos = i;
									}
								}
							}
							if(reparar2) {
								if(solucion2.getVariables().get(i) == 0.0) {
									double ratio2 = this.evalKP.get(i);
									if(ratio2 < minRatio) {
										minRatio = ratio2;
										minRatioPos = i;
									}
								}
							}
								
						}
						if(reparar1) {
							solucion.modIVariable(maxRatioPos, 0.0);
						}
						if(reparar2) {
							solucion2.modIVariable(minRatioPos, 1.0);
						}
					}else {
						if(reparar1) {
							int maxRatioPos = Utils.getRandNumber(0, this.datos.getConexionesTotales().size());
							while(solucion.getVariables().get(maxRatioPos) == 0.0) {
								maxRatioPos = Utils.getRandNumber(0, this.datos.getConexionesTotales().size());
							}
							solucion.modIVariable(maxRatioPos, 0.0);
						}
						if(reparar2) {
							int minRatioPos = Utils.getRandNumber(0, this.datos.getConexionesTotales().size());
							while(solucion2.getVariables().get(minRatioPos) == 1.0) {
								minRatioPos = Utils.getRandNumber(0, this.datos.getConexionesTotales().size());
							}
							solucion2.modIVariable(minRatioPos, 1.0);
						}
					}
					if(reparar1) {
						this.evaluate2(solucion);
						idsRes = this.restricionesInfactibles(solucion.getObjectivesNorm());
						reparar1 = (idsRes.size() != 0 && this.comprobarUnos(solucion));
					}
					if(reparar2) {
						this.evaluate2(solucion2);
						idsRes2 = this.restricionesInfactibles(solucion2.getObjectivesNorm());
						reparar2 = (idsRes2.size() != 0 && this.comprobarCeros(solucion2));
					}
				}
			}
			
			if(idsRes.size() == 0 || idsRes2.size() == 0){
				boolean mejorar1 = (idsRes.size() == 0);
				boolean mejorar2 = (idsRes2.size() == 0);
				
				boolean terminate = false;
				while (!terminate) {
					List<Double> candidatos = new ArrayList<>();
					Map<Double, Integer> candidatosRatio = new HashMap<>();
					
					List<Double> candidatos2 = new ArrayList<>();
					Map<Double, Integer> candidatosRatio2 = new HashMap<>();
					for(int i = 0; i < this.datos.getConexionesTotales().size(); i++) {
						if(mejorar1 && solucion.getVariables().get(i) == 0.0) {
							List<Double> objTemp = this.calcularObjTemp(solucion, i);
							List<Integer> idsResTemp = this.restricionesInfactibles(objTemp);
							if(idsResTemp.size() == 0 && this.calcularFitness(objTemp) < solucion.getObjectives().get(0)) {
								Double eval = this.evalKP.get(i);
								candidatos.add(eval);
								candidatosRatio.put(eval, i);
							}
						}
						if(mejorar2 && solucion2.getVariables().get(i) == 1.0) {
							List<Double> objTemp2 = this.calcularObjTemp2(solucion2, i);
							List<Integer> idsResTemp2 = this.restricionesInfactibles(objTemp2);
							if(idsResTemp2.size() == 0 && this.calcularFitness(objTemp2) < solucion2.getObjectives().get(0)) {
								Double eval = this.evalKP.get(i);
								candidatos2.add(eval);
								candidatosRatio2.put(eval, i);
							}
						}
					}
					if(mejorar1 && candidatos.size() != 0) {
						Collections.sort(candidatos);
						if(Utils.getRandNumber(0.0, 1.0) >= this.SRate) {
							int pos = candidatosRatio.get(candidatos.get(0));
							solucion.modIVariable(pos, 1.0);
						}else {
							int pos = candidatosRatio.get(candidatos.get(Utils.getRandNumber(0, candidatos.size())));
							solucion.modIVariable(pos, 1.0);
						}
						this.evaluate2(solucion);
					}if(mejorar2 && candidatos2.size() != 0) {
						Collections.sort(candidatos2, Collections.reverseOrder());
						if(Utils.getRandNumber(0.0, 1.0) >= this.SRate) {
							int pos = candidatosRatio2.get(candidatos2.get(0));
							solucion2.modIVariable(pos, 0.0);
						}else {
							int pos = candidatosRatio2.get(candidatos2.get(Utils.getRandNumber(0, candidatos2.size())));
							solucion2.modIVariable(pos, 0.0);
						}
						this.evaluate2(solucion2);
					}if(candidatos.size() == 0 && candidatos2.size() == 0) {
						terminate = true;
					}
					
				}
			}
		}
		
		Particle Ifinal = null;
		
		if(solucion.isFeasible() && solucion2.isFeasible()) {
			if(solucion.getObjectives().get(0) < solucion2.getObjectives().get(0)) {
				Ifinal = solucion;
			}else {
				Ifinal = solucion2;
			}
		}else if(solucion.isFeasible() && !solucion2.isFeasible()) {
			Ifinal = solucion;
		}else if(!solucion.isFeasible() && solucion2.isFeasible()) {
			Ifinal = solucion2;
		}else if(!solucion.isFeasible() && !solucion2.isFeasible()) {
			if(solucion.getConstraintViolation() < solucion2.getConstraintViolation()) {
				Ifinal = solucion;
			}else {
				Ifinal = solucion2;
			}
		}
		
		this.quitarDirecciones(Ifinal.getVariables());
		
		return Ifinal;
	}
	
	private List<Integer> restricionesInfactibles(List<Double> obj) {
		List<Integer> ids = new ArrayList<>();
		
		/*TODO: Restricciones de cada objetivo:
		 * 0	Riesgo importado: Valor máximo de riesgo perimitido
		 * 1	Ingresos por turismo: Limitar ganancia
		 * 			(100% de reducción = abrir todas las conexiones. 0% de reducción = cerrar todas las conexiones)
		 * 2	H areas de influencia: Limitar la desviacion estandar
		 * 3	H pasajeros compañia: Limitar la desv estandar
		 * 4	Tasas aero: Limitar ganancia
		 * 5	H ingresos aer dest: Limitar la desv estandar
		 * 6	Pasajeros: Limitar pasajeros
		 * 7	Conectividad: limitar ganancia de con
		 */
		
		for(int res : this.preferencias.getOrder().getRestricciones().keySet()) {
			if((res == 1 || res == 4 || res == 6 || res == 7) && 1 - obj.get(res) > this.preferencias.getOrder().getRestricciones().get(res)/100) {
				ids.add(res);
			}else if((res == 0 || res == 2 || res == 3 || res == 5) && obj.get(res) > this.preferencias.getOrder().getRestricciones().get(res)/100) {
				ids.add(res);
			}
		}
		
		return ids;
	}
	
	private List<Double> calcularObjTemp(Particle solucion, int posicion) {
		List<Double> objTemp = new ArrayList<>();
		/*objTemp.add(-1.0);
		if(this.preferencias.getOrder().getRestricciones().keySet().contains(0)) {*/
			Double riesgo = solucion.getObjectivesNorm().get(0)*this.RiesgosumatorioTotal;
			//objTemp.set(0, (riesgo + this.datos.getRiesgos_KP().get(posicion))/ this.RiesgosumatorioTotal);
			objTemp.add((riesgo + this.datos.getRiesgos_KP().get(posicion))/ this.RiesgosumatorioTotal);
		//}
		/*objTemp.add(-1.0);
		if(this.preferencias.getOrder().getRestricciones().keySet().contains(1)) {*/
			Double ingresos = (1 - solucion.getObjectivesNorm().get(1))*this.IngresosTtotalSuma;
			//objTemp.set(1, 1 - (ingresos + this.datos.getIngresos_KP().get(posicion)) / this.IngresosTtotalSuma);
			objTemp.add(1 - (ingresos + this.datos.getIngresos_KP().get(posicion)) / this.IngresosTtotalSuma);
		//}
		//objTemp.add(-1.0);
		List<Double> valorActual;
		Double valorAntes;
		//if(this.preferencias.getOrder().getRestricciones().keySet().contains(2)) {
			Double HingresosAreaInf = 0.0;
			Double mediaingresosAreaInf = 0.0;
			valorActual = solucion.getExtra().get(this.datos.getAreasInf_KP().get(posicion));
			valorAntes = valorActual.get(0);
			valorActual.set(0, valorAntes + this.datos.getIngresos_KP().get(posicion));
			solucion.getExtra().put(this.datos.getAreasInf_KP().get(posicion), valorActual);
			for(String areaInf : this.ingresosPorAreaInf.keySet()) {
				mediaingresosAreaInf += 1.0 - (solucion.getExtra().get(areaInf).get(0) / solucion.getExtra().get(areaInf).get(1));
			}
			mediaingresosAreaInf /= this.ingresosPorAreaInf.keySet().size();
			for(String areaInf : this.ingresosPorAreaInf.keySet()) {
				HingresosAreaInf += Math.pow((1.0 - (solucion.getExtra().get(areaInf).get(0) / solucion.getExtra().get(areaInf).get(1))) - mediaingresosAreaInf, 2);
			}
			HingresosAreaInf /= this.ingresosPorAreaInf.keySet().size();
			HingresosAreaInf = Math.sqrt(HingresosAreaInf);
			//objTemp.set(2, HingresosAreaInf);
			objTemp.add(HingresosAreaInf);
			valorActual.set(0, valorAntes);
		//}
		/*objTemp.add(-1.0);
		if(this.preferencias.getOrder().getRestricciones().keySet().contains(3)) {*/
			Double HpasajerosCompanyia = 0.0;
			Double mediaingresosPasajerosCompanyia = 0.0;
			List<List<Double>> valoresAntes = new ArrayList<>();
			for(int i = 0; i < this.datos.getCompanyias_KP().get(posicion).size(); i++) {
				valorActual = solucion.getExtra().get(this.datos.getCompanyias_KP().get(posicion).get(i));
				valoresAntes.add(Utils.copiarLista(valorActual));
				valorActual.set(0, valoresAntes.get(0).get(0) + this.datos.getPasajerosCompanyias_KP().get(posicion).get(i));
				solucion.getExtra().put(this.datos.getCompanyias_KP().get(posicion).get(i), valorActual);
			}
			for(String companyia : this.pasajerosPorCompanyia.keySet()) {
				mediaingresosPasajerosCompanyia += 1.0 - (solucion.getExtra().get(companyia).get(0) / solucion.getExtra().get(companyia).get(1));
			}
			mediaingresosPasajerosCompanyia /= this.pasajerosPorCompanyia.keySet().size();
			for(String companyia : this.pasajerosPorCompanyia.keySet()) {
				HpasajerosCompanyia += Math.pow((1.0 - (solucion.getExtra().get(companyia).get(0) / solucion.getExtra().get(companyia).get(1))) - mediaingresosPasajerosCompanyia , 2);
			}
			HpasajerosCompanyia /= this.pasajerosPorCompanyia.keySet().size();
			HpasajerosCompanyia = Math.sqrt(HpasajerosCompanyia);
			
			for(int i = 0; i < this.datos.getCompanyias_KP().get(posicion).size(); i++) {
				solucion.getExtra().put(this.datos.getCompanyias_KP().get(posicion).get(i), valoresAntes.get(i));
			}
			
			//objTemp.set(3, HpasajerosCompanyia);
			objTemp.add(HpasajerosCompanyia);
		//}
		/*objTemp.add(-1.0);
		if(this.preferencias.getOrder().getRestricciones().keySet().contains(4)) {*/
			Double tasas = (1 - solucion.getObjectivesNorm().get(4))*this.Tasastotal;
			//objTemp.set(4, 1 - (tasas + this.datos.getTasas_KP().get(posicion)) / this.Tasastotal);
			objTemp.add(1 - (tasas + this.datos.getTasas_KP().get(posicion)) / this.Tasastotal);
		//}
		/*objTemp.add(-1.0);
		if(this.preferencias.getOrder().getRestricciones().keySet().contains(5)) {*/
			Double HingresosAerDest = 0.0;
			Double mediaingresosAerDest = 0.0;
			valorActual = solucion.getExtra().get(this.datos.getConexionesNombresTotales().get(posicion).get(1));
			valorAntes = valorActual.get(0);
			valorActual.set(0, valorAntes + this.datos.getTasas_KP().get(posicion));
			solucion.getExtra().put(this.datos.getConexionesNombresTotales().get(posicion).get(1), valorActual);
			for(String aer : this.ingresosPorAerDest.keySet()) {
				mediaingresosAerDest += 1.0 - (solucion.getExtra().get(aer).get(0) / solucion.getExtra().get(aer).get(1));
			}
			mediaingresosAerDest /= this.ingresosPorAerDest.keySet().size();
			for(String aer : this.ingresosPorAerDest.keySet()) {
				HingresosAerDest += Math.pow((1.0 - (solucion.getExtra().get(aer).get(0)/solucion.getExtra().get(aer).get(1))) - mediaingresosAerDest, 2);
			}
			HingresosAerDest /= this.ingresosPorAerDest.keySet().size();
			HingresosAerDest = Math.sqrt(HingresosAerDest);
			
			//objTemp.set(5, HingresosAerDest);
			objTemp.add(HingresosAerDest);
			valorActual.set(0, valorAntes);
		//}
		/*objTemp.add(-1.0);
		if(this.preferencias.getOrder().getRestricciones().keySet().contains(6)) {*/
			Double pasajeros = (1 - solucion.getObjectivesNorm().get(6))*this.Pasajerostotal;
			//objTemp.set(6, 1 - (pasajeros + this.datos.getPasajeros_KP().get(posicion)) / this.Pasajerostotal);
			objTemp.add(1 - (pasajeros + this.datos.getPasajeros_KP().get(posicion)) / this.Pasajerostotal);
		//}
		/*objTemp.add(-1.0);
		if(this.preferencias.getOrder().getRestricciones().keySet().contains(7)) {*/
			int dia = this.calcularDia(posicion);
			Double conectividadAntes = solucion.getObjectivesNorm().get(7);
			
			conectividadAntes *= this.ConectividadtotalSuma;
			conectividadAntes -= this.datos.getConectividadesTotales().get(posicion) * (1 - (solucion.getExtra().get(this.datos.getConexionesTotales().get(posicion).get(0) + String.valueOf(dia)).get(0) / (1.0*this.datos.getVuelosEntrantesConexionOrdenadoTotalTotales().get(posicion))));
			conectividadAntes += this.datos.getConectividadesTotales().get(posicion) * (1 - (( (this.datos.getVuelosEntrantesConexionOrdenadoTotales().get(posicion)*1.0) + solucion.getExtra().get(this.datos.getConexionesTotales().get(posicion).get(0) + String.valueOf(dia)).get(0) ) / (1.0*this.datos.getVuelosEntrantesConexionOrdenadoTotalTotales().get(posicion))));
			
			conectividadAntes /= this.ConectividadtotalSuma;
			
			//objTemp.set(7, conectividadAntes);
			objTemp.add(conectividadAntes);
		//}
		
		return objTemp;
	}
	
	private List<Double> calcularObjTemp2(Particle solucion, int posicion) {
		List<Double> objTemp = new ArrayList<>();
		/*objTemp.add(-1.0);
		if(this.preferencias.getOrder().getRestricciones().keySet().contains(0)) {*/
			Double riesgo = solucion.getObjectivesNorm().get(0)*this.RiesgosumatorioTotal;
			//objTemp.set(0, (riesgo - this.datos.getRiesgos_KP().get(posicion))/ this.RiesgosumatorioTotal);
			objTemp.add((riesgo - this.datos.getRiesgos_KP().get(posicion))/ this.RiesgosumatorioTotal);
		//}
		/*objTemp.add(-1.0);
		if(this.preferencias.getOrder().getRestricciones().keySet().contains(1)) {*/
			Double ingresos = (1 - solucion.getObjectivesNorm().get(1))*this.IngresosTtotalSuma;
			//objTemp.set(1, 1 - (ingresos - this.datos.getIngresos_KP().get(posicion)) / this.IngresosTtotalSuma);
			objTemp.add(1 - (ingresos - this.datos.getIngresos_KP().get(posicion)) / this.IngresosTtotalSuma);
		//}
		//objTemp.add(-1.0);
		List<Double> valorActual;
		Double valorAntes;
		//if(this.preferencias.getOrder().getRestricciones().keySet().contains(2)) {
			Double HingresosAreaInf = 0.0;
			Double mediaingresosAreaInf = 0.0;
			valorActual = solucion.getExtra().get(this.datos.getAreasInf_KP().get(posicion));
			valorAntes = valorActual.get(0);
			valorActual.set(0, valorAntes - this.datos.getIngresos_KP().get(posicion));
			solucion.getExtra().put(this.datos.getAreasInf_KP().get(posicion), valorActual);
			for(String areaInf : this.ingresosPorAreaInf.keySet()) {
				mediaingresosAreaInf += 1.0 - (solucion.getExtra().get(areaInf).get(0) / solucion.getExtra().get(areaInf).get(1));
			}
			mediaingresosAreaInf /= this.ingresosPorAreaInf.keySet().size();
			for(String areaInf : this.ingresosPorAreaInf.keySet()) {
				HingresosAreaInf += Math.pow((1.0 - (solucion.getExtra().get(areaInf).get(0) / solucion.getExtra().get(areaInf).get(1))) - mediaingresosAreaInf, 2);
			}
			HingresosAreaInf /= this.ingresosPorAreaInf.keySet().size();
			HingresosAreaInf = Math.sqrt(HingresosAreaInf);
			//objTemp.set(2, HingresosAreaInf);
			objTemp.add(HingresosAreaInf);
			valorActual.set(0, valorAntes);
		//}
		/*objTemp.add(-1.0);
		if(this.preferencias.getOrder().getRestricciones().keySet().contains(3)) {*/
			Double HpasajerosCompanyia = 0.0;
			Double mediaingresosPasajerosCompanyia = 0.0;
			List<List<Double>> valoresAntes = new ArrayList<>();
			for(int i = 0; i < this.datos.getCompanyias_KP().get(posicion).size(); i++) {
				valorActual = solucion.getExtra().get(this.datos.getCompanyias_KP().get(posicion).get(i));
				valoresAntes.add(Utils.copiarLista(valorActual));
				valorActual.set(0, valoresAntes.get(0).get(0) - this.datos.getPasajerosCompanyias_KP().get(posicion).get(i));
				solucion.getExtra().put(this.datos.getCompanyias_KP().get(posicion).get(i), valorActual);
			}
			for(String companyia : this.pasajerosPorCompanyia.keySet()) {
				mediaingresosPasajerosCompanyia += 1.0 - (solucion.getExtra().get(companyia).get(0) / solucion.getExtra().get(companyia).get(1));
			}
			mediaingresosPasajerosCompanyia /= this.pasajerosPorCompanyia.keySet().size();
			for(String companyia : this.pasajerosPorCompanyia.keySet()) {
				HpasajerosCompanyia += Math.pow((1.0 - (solucion.getExtra().get(companyia).get(0) / solucion.getExtra().get(companyia).get(1))) - mediaingresosPasajerosCompanyia , 2);
			}
			HpasajerosCompanyia /= this.pasajerosPorCompanyia.keySet().size();
			HpasajerosCompanyia = Math.sqrt(HpasajerosCompanyia);
			
			for(int i = 0; i < this.datos.getCompanyias_KP().get(posicion).size(); i++) {
				solucion.getExtra().put(this.datos.getCompanyias_KP().get(posicion).get(i), valoresAntes.get(i));
			}
			
			//objTemp.set(3, HpasajerosCompanyia);
			objTemp.add(HpasajerosCompanyia);
		//}
		/*objTemp.add(-1.0);
		if(this.preferencias.getOrder().getRestricciones().keySet().contains(4)) {*/
			Double tasas = (1 - solucion.getObjectivesNorm().get(4))*this.Tasastotal;
			//objTemp.set(4, 1 - (tasas - this.datos.getTasas_KP().get(posicion)) / this.Tasastotal);
			objTemp.add(1 - (tasas - this.datos.getTasas_KP().get(posicion)) / this.Tasastotal);
		//}
		/*objTemp.add(-1.0);
		if(this.preferencias.getOrder().getRestricciones().keySet().contains(5)) {*/
			Double HingresosAerDest = 0.0;
			Double mediaingresosAerDest = 0.0;
			valorActual = solucion.getExtra().get(this.datos.getConexionesNombresTotales().get(posicion).get(1));
			valorAntes = valorActual.get(0);
			valorActual.set(0, valorAntes - this.datos.getTasas_KP().get(posicion));
			solucion.getExtra().put(this.datos.getConexionesNombresTotales().get(posicion).get(1), valorActual);
			for(String aer : this.ingresosPorAerDest.keySet()) {
				mediaingresosAerDest += 1.0 - (solucion.getExtra().get(aer).get(0) / solucion.getExtra().get(aer).get(1));
			}
			mediaingresosAerDest /= this.ingresosPorAerDest.keySet().size();
			for(String aer : this.ingresosPorAerDest.keySet()) {
				HingresosAerDest += Math.pow((1.0 - (solucion.getExtra().get(aer).get(0)/solucion.getExtra().get(aer).get(1))) - mediaingresosAerDest, 2);
			}
			HingresosAerDest /= this.ingresosPorAerDest.keySet().size();
			HingresosAerDest = Math.sqrt(HingresosAerDest);
			
			//objTemp.set(5, HingresosAerDest);
			objTemp.add(HingresosAerDest);
			valorActual.set(0, valorAntes);
		//}
		/*objTemp.add(-1.0);
		if(this.preferencias.getOrder().getRestricciones().keySet().contains(6)) {*/
			Double pasajeros = (1 - solucion.getObjectivesNorm().get(6))*this.Pasajerostotal;
			//objTemp.set(6, 1 - (pasajeros - this.datos.getPasajeros_KP().get(posicion)) / this.Pasajerostotal);
			objTemp.add(1 - (pasajeros - this.datos.getPasajeros_KP().get(posicion)) / this.Pasajerostotal);
		//}
		/*objTemp.add(-1.0);
		if(this.preferencias.getOrder().getRestricciones().keySet().contains(7)) {*/
			int dia = this.calcularDia(posicion);
			Double conectividadAntes = solucion.getObjectivesNorm().get(7);
			
			conectividadAntes *= this.ConectividadtotalSuma;
			conectividadAntes -= this.datos.getConectividadesTotales().get(posicion) * (1 - (solucion.getExtra().get(this.datos.getConexionesTotales().get(posicion).get(0) + String.valueOf(dia)).get(0) / (1.0*this.datos.getVuelosEntrantesConexionOrdenadoTotalTotales().get(posicion))));
			conectividadAntes += this.datos.getConectividadesTotales().get(posicion) * (1 - (( (solucion.getExtra().get(this.datos.getConexionesTotales().get(posicion).get(0) + String.valueOf(dia)).get(0) - this.datos.getVuelosEntrantesConexionOrdenadoTotales().get(posicion)*1.0) ) / (1.0*this.datos.getVuelosEntrantesConexionOrdenadoTotalTotales().get(posicion))));
			
			conectividadAntes /= this.ConectividadtotalSuma;
			
			//objTemp.set(7, conectividadAntes);
			objTemp.add(conectividadAntes);
		//}
		
		return objTemp;
	}
	
	private boolean comprobarUnos(Particle solucion) {
		boolean hayUnos = false;
		int pos = 0;
		while(!hayUnos && pos < solucion.getVariables().size()) {
			if(solucion.getVariables().get(pos) == 1.0 && !this.direccionesAMantener.contains(pos)) {
				hayUnos = true;
			}
			pos++;
		}
		return hayUnos;
	}
	
	private boolean comprobarCeros(Particle solucion) {
		return solucion.getVariables().contains(0.0);
	}
	
	public double evalKP(int posicion) {
		double eval = 0.0;
		double res = 0.0;
		double riesgo = 0.0;
		double ingresos = 0.0;
		double pasajeros = 0.0;
		double tasas = 0.0;
		double conectividad = 0.0;
		double HingresosAreaInf = 0.0;
		double mediaPerdidaIngresosPAreaInf = 0.0;
		double mediaPerdidaIngresosPAerDest = 0.0;
		double HingresosAerDest = 0.0;
		double sumaPerdidaPasajerosPCompanyia = 0.0;
		double mediaPerdidaPasajerosPCompanyia = 0.0;
		double HpasajerosCompanyia = 0.0;
		List<Double> objetivos = new ArrayList<>();
		
		riesgo = this.datos.getRiesgos_KP().get(posicion)/this.RiesgosumatorioTotal;
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
		
		
		mediaPerdidaIngresosPAreaInf = ((this.ingresosPorAreaInf.keySet().size()-1) * 1.0 + (1.0 - (this.datos.getIngresos_KP().get(posicion)/this.datos.getIngresosAreaInfTotalTotales().get(posicion))))/this.ingresosPorAreaInf.keySet().size();
		
		HingresosAreaInf += (this.ingresosPorAreaInf.keySet().size() - 1) * Math.pow(1.0 - mediaPerdidaIngresosPAreaInf, 2);
		HingresosAreaInf += Math.pow((1.0 - (this.datos.getIngresos_KP().get(posicion)/this.datos.getIngresosAreaInfTotalTotales().get(posicion))) - mediaPerdidaIngresosPAreaInf, 2);
		
		HingresosAreaInf /= this.ingresosPorAreaInf.keySet().size();
		
		HingresosAreaInf = Math.sqrt(HingresosAreaInf);
		
		
		mediaPerdidaIngresosPAerDest = ((this.ingresosPorAerDest.keySet().size()-1) * 1.0 + (1.0 - (this.datos.getTasas_KP().get(posicion)/this.datos.getIngresosAerDestTotalTotales().get(posicion))))/this.ingresosPorAerDest.keySet().size();
		
		HingresosAerDest += (this.ingresosPorAerDest.keySet().size() - 1) * Math.pow(1.0 - mediaPerdidaIngresosPAerDest, 2);
		HingresosAerDest += Math.pow((1.0 - (this.datos.getTasas_KP().get(posicion)/this.datos.getIngresosAerDestTotalTotales().get(posicion))) - mediaPerdidaIngresosPAerDest, 2);
		
		HingresosAerDest /= this.ingresosPorAerDest.keySet().size();
		
		HingresosAerDest = Math.sqrt(HingresosAerDest);
		
		
		for(int i = 0; i < this.datos.getCompanyias_KP().get(posicion).size(); i++) {
			Double num = this.datos.getPasajerosCompanyias_KP().get(posicion).get(i) * 1.0;
			Double den = this.datos.getTotalPasajerosCompanyias().get(posicion).get(i) * 1.0;
			sumaPerdidaPasajerosPCompanyia += 1.0 - (num/den);
		}
		
		mediaPerdidaPasajerosPCompanyia = ((this.pasajerosPorCompanyia.keySet().size() - this.datos.getCompanyias_KP().get(posicion).size()) * 1.0 + sumaPerdidaPasajerosPCompanyia)/this.pasajerosPorCompanyia.keySet().size()*1.0;
		
		HpasajerosCompanyia += (this.pasajerosPorCompanyia.keySet().size() - this.datos.getCompanyias_KP().get(posicion).size()) * Math.pow(1.0 - mediaPerdidaPasajerosPCompanyia, 2);
		
		for(int i = 0; i < this.datos.getCompanyias_KP().get(posicion).size(); i++) {
			Double num = this.datos.getPasajerosCompanyias_KP().get(posicion).get(i) * 1.0;
			Double den = this.datos.getTotalPasajerosCompanyias().get(posicion).get(i) * 1.0;
			HpasajerosCompanyia += Math.pow((1.0 - (num/den)), 2);
		}
		
		HpasajerosCompanyia /= this.pasajerosPorCompanyia.keySet().size()*1.0;
		
		HpasajerosCompanyia = Math.sqrt(HpasajerosCompanyia);
		
		objetivos.add(riesgo);
		objetivos.add(ingresos);
		objetivos.add(HingresosAreaInf);
		objetivos.add(HpasajerosCompanyia);
		objetivos.add(tasas);
		objetivos.add(HingresosAerDest);
		objetivos.add(pasajeros);
		objetivos.add(conectividad);
		
		for(int i = 0; i < this.preferencias.getOrder().getOrder().size(); i++) {
			eval += objetivos.get(this.preferencias.getOrder().getOrder().get(i)) * this.preferencias.getIdWeights().get(this.preferencias.getOrder().getOrder().get(i));
		}
		
		for(int id : this.preferencias.getOrder().getRestricciones().keySet()) {
			res += (1.0 - objetivos.get(id)) * (1.0 / this.preferencias.getOrder().getRestricciones().keySet().size());
		}
		
		return eval/res;
	}
	
	private int calcularDia(int pos) {
		int i = 0;
		int offset = 0;
		while(i < this.datos.getDatosPorDia().size()) {
			if(pos < this.datos.getDatosPorDia().get(i).getConexiones().size() + offset) {
				break;
			}
			offset += this.datos.getDatosPorDia().get(i).getConexiones().size();
			i++;
		}
		return i;
	}
	
	private void calcularEvalKP() {
		for(int i = 0; i < this.datos.getConexionesTotales().size(); i++) {
			if(!this.direccionesAMantener.contains(i)){
				this.evalKP.add(this.evalKP(i));
			}else {
				this.evalKP.add(-1.0);
			}
		}
	}
	
	private double calcularFitness(List<Double> objTemp) {
		double sumaPesos = 0.0;
		for(int i = 0; i < this.preferencias.getOrder().getOrder().size(); i++) {
			sumaPesos += objTemp.get(this.preferencias.getOrder().getOrder().get(i)) 
					* this.preferencias.getIdWeights().get(this.preferencias.getOrder().getOrder().get(i));
		}
		return sumaPesos;
	}

	public DataRRPS_PAT getDatos() {
		return datos;
	}

	@Override
	public void sumarNumInicializaciones() {
		this.numInicializaciones++;
	}

}
