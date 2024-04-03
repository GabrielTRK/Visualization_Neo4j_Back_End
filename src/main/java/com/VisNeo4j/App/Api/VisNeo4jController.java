package com.VisNeo4j.App.Api;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.VisNeo4j.App.Algoritmo.BPSO;
import com.VisNeo4j.App.Algoritmo.Parametros.BPSOParams;
import com.VisNeo4j.App.Algoritmo.Parametros.CondicionParada.CP;
import com.VisNeo4j.App.Algoritmo.Parametros.InertiaWUpdate.InertiaW;
import com.VisNeo4j.App.Constantes.Constantes;
import com.VisNeo4j.App.Lectura.LecturaDeDatos;
import com.VisNeo4j.App.Modelo.Individuo;
import com.VisNeo4j.App.Modelo.Salida.Aeropuerto;
import com.VisNeo4j.App.Modelo.Salida.DatosConexiones;
import com.VisNeo4j.App.Modelo.Salida.FitnessI;
import com.VisNeo4j.App.Modelo.Salida.Objetivo;
import com.VisNeo4j.App.Modelo.Salida.Persona;
import com.VisNeo4j.App.Modelo.Salida.Proyecto;
import com.VisNeo4j.App.Modelo.Salida.TraducirSalida;
import com.VisNeo4j.App.Modelo.Salida.Vuelos;
import com.VisNeo4j.App.Problemas.Problema;
import com.VisNeo4j.App.Problemas.RRPS_PAT;
import com.VisNeo4j.App.Problemas.SubVuelos;
import com.VisNeo4j.App.Problemas.Datos.DatosRRPS_PAT;
import com.VisNeo4j.App.QDMP.DMPreferences;
import com.VisNeo4j.App.QDMP.ObjectivesOrder;
import com.VisNeo4j.App.Service.VisNeo4jService;
import com.VisNeo4j.App.Utils.Utils;
import com.opencsv.exceptions.CsvException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
class VisNeo4jController {

	private final VisNeo4jService visNeo4jService;

	VisNeo4jController(VisNeo4jService visNeo4jService) {
		this.visNeo4jService = visNeo4jService;
	}
	
	@CrossOrigin
	@PostMapping("/saveP")
	public boolean guardarProyecto(@RequestParam("fecha_inicial") String fecha_I, 
			@RequestParam("fecha_final") String fecha_F,
			
			@RequestParam("iteraciones") int numIteraciones,
			@RequestParam("numIndividuos") int numIndividuos,
			@RequestParam("inertiaW") double inertiaW,
			@RequestParam("c1") double c1,
			@RequestParam("c2") double c2,
			@RequestParam("m") double m,
			@RequestParam("p") double p,
			@RequestParam("res_epi") double resEpi,
			@RequestParam("res_pol") String resPol,
			@RequestParam("nombre") String nombreProyecto,
			@RequestBody ObjectivesOrder order) throws IOException {
		
		DMPreferences preferencias = new DMPreferences(order, Constantes.nombreQDMPSR);
		preferencias.generateWeightsVector(order.getOrder().size());
		
		BPSOParams params = new BPSOParams(numIndividuos, inertiaW, c1, c2, 
				numIteraciones, m, p, Constantes.nombreCPMaxDistQuick, 
				Constantes.nombreIWDyanamicDecreasing);
		
		return visNeo4jService.guardarNuevoproyecto(nombreProyecto, params, preferencias, fecha_I, fecha_F, resEpi, resPol);
	}
	
	@CrossOrigin
	@GetMapping("/loadP")
	public List<Proyecto> cargarProyectos() throws IOException, CsvException {
	    return visNeo4jService.obtenerListaProyectos();
	}
	
	@CrossOrigin
	@GetMapping("/{proyecto}/loadS")
	public void cargarSolucionesProyectoI(@PathVariable String proyecto) {
		//TODO: Devolver lista de soluciones (IDs)
		
	}
	
	@CrossOrigin
	@GetMapping("/{proyecto}/{idS}/{dia}")
	public void cargarProyectoISolucionJDiaK(@PathVariable String proyecto, @PathVariable int idS,
			@PathVariable int dia) {
		//TODO: Devolver solucion
	}
	
	@CrossOrigin
	@PostMapping("/optimize")
	public boolean runOptimization(@RequestParam("fecha_inicial") String fecha_I, 
			@RequestParam("fecha_final") String fecha_F,
			
			@RequestParam("iteraciones") int numIteraciones,
			@RequestParam("numIndividuos") int numIndividuos,
			@RequestParam("inertiaW") double inertiaW,
			@RequestParam("c1") double c1,
			@RequestParam("c2") double c2,
			@RequestParam("m") double m,
			@RequestParam("p") double p,
			@RequestParam("res_epi") double resEpi,
			@RequestParam("res_pol") String resPol,
			@RequestParam("nombre") String nombreProyecto,
			@RequestBody ObjectivesOrder order) throws FileNotFoundException, IOException, CsvException, ParseException {
		DMPreferences preferencias = new DMPreferences(order, Constantes.nombreQDMPSR);
		preferencias.generateWeightsVector(order.getOrder().size());
		
		BPSOParams params = new BPSOParams(numIndividuos, inertiaW, c1, c2, 
				numIteraciones, m, p, Constantes.nombreCPMaxDistQuick, 
				Constantes.nombreIWDyanamicDecreasing);
		
		DatosRRPS_PAT datos = visNeo4jService.obtenerDatosRRPS_PAT(fecha_I, fecha_F);
		
		Problema problema = new RRPS_PAT(datos, resEpi, resPol, preferencias);
		
		if(visNeo4jService.guardarNuevoproyecto(nombreProyecto, params, preferencias, fecha_I, fecha_F, resEpi, resPol)) {
			BPSO bpso = new BPSO(problema, params);
			Individuo ind = bpso.ejecutarBPSO();
			problema.devolverSolucionCompleta(ind);
			System.out.println(ind);
			
			visNeo4jService.guardarNuevaSolucionRRPS_PAT(ind, datos, nombreProyecto);
			return true;
		}else {
			return false;
		}
		
		/*List<Aeropuerto> lista = Utils.obtenerUltimaSolucionDiaI(0);
		List<Double> bits = Utils.obtenerUltimosBitsDiaI(0);
		Utils.obtenernumDiasUltimaSolucion();
		DatosConexiones datosConexiones = new DatosConexiones(lista, bits);*/
    	//return datosConexiones;
	}
	
	@CrossOrigin
	@PostMapping("/{proyecto}/optimize")
	public boolean runOptimization(@PathVariable String proyecto) throws FileNotFoundException, IOException, CsvException, ParseException {
		DMPreferences preferencias = visNeo4jService.cargarPreferenciasProyecto(proyecto);
		
		BPSOParams params = visNeo4jService.cargarParametrosProyecto(proyecto);
		
		Map<String, String> fechas = visNeo4jService.cargarFechasProyecto(proyecto);
		
		Map<String, String> res = visNeo4jService.cargarRestriccionesProyecto(proyecto);
		
		DatosRRPS_PAT datos = visNeo4jService.obtenerDatosRRPS_PAT(fechas.get(Constantes.nombreFechaInicial), Constantes.nombreFechaFinal);
		
		Problema problema = new RRPS_PAT(datos, Double.valueOf(res.get(Constantes.nombreRestriccionEpidemiologica)), Constantes.nombreRestriccionSocial, preferencias);
		
		BPSO bpso = new BPSO(problema, params);
		Individuo ind = bpso.ejecutarBPSO();
		problema.devolverSolucionCompleta(ind);
		System.out.println(ind);
			
		visNeo4jService.guardarNuevaSolucionRRPS_PAT(ind, datos, proyecto);
		return true;
	}
	
	@CrossOrigin
	@PostMapping("/testEva")
	public DatosRRPS_PAT testEvaluacion(@RequestParam("fecha_inicial") String fecha_I, 
			@RequestParam("fecha_final") String fecha_F,
			@RequestBody ObjectivesOrder order) throws FileNotFoundException, IOException, CsvException, ParseException {
		DatosRRPS_PAT datos = visNeo4jService.obtenerDatosRRPS_PAT(fecha_I, fecha_F);
		
		DMPreferences preferencias = new DMPreferences(order, Constantes.nombreQDMPSR);
		preferencias.generateWeightsVector(order.getOrder().size());
		
		Problema problema = new RRPS_PAT(datos, 0.75, "", preferencias);
		Individuo ind = new Individuo(problema.getNumVariables(), 1);
		problema.inicializarValores(ind);
		//problema.inicializarValores(ind);
		/*problema.inicializarValores(ind);
		problema.inicializarValores(ind);
		problema.inicializarValores(ind);*/
		
		problema.evaluate(ind);
		ind = problema.devolverSolucionCompleta(ind);
		System.out.println(ind);
		System.out.println();
		
		return datos;
	}
	
	@CrossOrigin
	@GetMapping("/data")
	public DatosRRPS_PAT obtenerDatos(@RequestParam("fecha_inicial") String fecha_I, 
			@RequestParam("fecha_final") String fecha_F) throws FileNotFoundException, IOException, CsvException, ParseException {
		DatosRRPS_PAT datos = visNeo4jService.obtenerDatosRRPS_PAT(fecha_I, fecha_F);
		
		return datos;
	}
	
	
	@CrossOrigin
	@GetMapping("/{id}/hist")
	public List<FitnessI> obtenerHistSolucionI(@PathVariable int id) throws FileNotFoundException, IOException, CsvException, ParseException {
		String nombreFichero = String.valueOf(id);
		return TraducirSalida.obtenerHistoricoDeFitness(Utils.leerCSVHistFitness(nombreFichero));
	}
	
	@CrossOrigin
	@GetMapping("/{id}/objetivos")
	public List<Objetivo> obtenerObjSolucionI(@PathVariable int id) throws FileNotFoundException, IOException, CsvException, ParseException {
		String nombreFichero = String.valueOf(id);
		return TraducirSalida.obtenerObjetivos(Utils.leerCSVObjetivos(nombreFichero));
	}
	
	
	@CrossOrigin
	@GetMapping("/{id}/{dia}")
	public DatosConexiones obtenerSolucionIDiaJ(@PathVariable int id, @PathVariable int dia) throws FileNotFoundException, IOException, CsvException, ParseException {
		List<Aeropuerto> lista = Utils.obtenerSolucionDiaI(id, dia);
		List<Double> bits = Utils.obtenerBitsSolDiaI(id, dia);
		DatosConexiones datosConexiones = new DatosConexiones(lista, bits);
		return datosConexiones;
	}
	
	@CrossOrigin
	@GetMapping("/numSoluciones")
	public int obtenerNumSoluciones() throws FileNotFoundException, IOException, CsvException, ParseException {
		
		return Utils.leerCSVproblemaNumSoluciones();
	}
	
	@CrossOrigin
	@GetMapping("/{id}/numDias")
	public int numDiasSolucionI(@PathVariable int id) throws FileNotFoundException, IOException, CsvException, ParseException {
		
		return Utils.obtenernumDiasSolucionI(id);
	}
	
	/*@CrossOrigin
	@GetMapping("/eva/{id}")
	public void evaluarSolucion(@PathVariable int id, @RequestParam("dia_inicial") String dia_I, 
			@RequestParam("dia_final") String dia_F,
			@RequestParam("mes_inicial") String mes_I,
			@RequestParam("mes_final") String mes_F,
			@RequestParam("año_inicial") String año_I,
			@RequestParam("año_final") String año_F) throws FileNotFoundException, IOException, CsvException, ParseException {
		List<String> bits = Utils.leerCSVproblema(id);
		
		DatosProblemaDias datos = visNeo4jService.obtenerDatosDiasFichero(dia_I, dia_F, mes_I, mes_F, año_I, año_F);
		List<Double> pesos = new ArrayList<>();
		pesos.add(0.06861682918020946189960274467317);//Z6 Pérdida de pasajeros
		pesos.add(0.30335861321776814734561213434453);//Z1 Pérdida de ingresos turismo
		pesos.add(0.15890213073311664860960635608523);//Z3 Homogeneidad Pérdida de pasajeros Aerolineas
		pesos.add(0.2058504875406283856988082340195);//Z2 Homogeneidad Pérdida de ingresos turismo
		pesos.add(0.12459371614301191765980498374865);//Z4 Tasas
		pesos.add(0.09534127843986998916576381365114);//Z5 Homogeneidad tasas
		pesos.add(0.04333694474539544962080173347779);//Z7 Conectividad
		GestionConexionesAeropuertosPorDia problema = new GestionConexionesAeropuertosPorDia(datos, pesos, 0.75);
		List<Double> bitsDouble = new ArrayList<>();
		for(int i = 2; i < bits.size(); i++) {
			bitsDouble.add(Double.valueOf(bits.get(i)));
		}
		
		Individuo ind = new Individuo(problema.getNumVariables(), problema.getNumObjetivos());
		ind.setVariables(bitsDouble);
		System.out.println(problema.evaluate2(ind));
	}*/
	
	@CrossOrigin
	@PostMapping("/test")
	public void test(@RequestParam("fecha_inicial") String fecha_I, 
			@RequestParam("fecha_final") String fecha_F,
			@RequestBody ObjectivesOrder order) throws FileNotFoundException, IOException, CsvException, ParseException {
		
		File directoryPath = new File(Constantes.rutaFicherosProyectos);
	      //List of all files and directories
	      String contents[] = directoryPath.list();
	      for(int i=0; i<contents.length; i++) {
	         System.out.println(contents[i]);
	      }
	}
	
	@CrossOrigin
	@GetMapping("/airports")
	public DatosConexiones getAirports() throws FileNotFoundException, IOException, CsvException {
		List<String> AeropuertosEntrada = new ArrayList<>();
		List<String> AeropuertosEspanyoles = new ArrayList<>();
        List<String> AeropuertosOrigen = new ArrayList<>();
        List<String> companyias = new ArrayList<>();
        Map<List<String>, Integer> pasajerosCompanyia = new HashMap<>();
        Map<List<String>, Integer> vuelosEntrantesConexion = new HashMap<>();
        Map<String, Integer> vuelosSalientesAEspanya = new HashMap<>();
        Map<String, Integer> vuelosSalientes = new HashMap<>();
        Map<String, Double> conectividadesAeropuertosOrigen = new HashMap<>();
        Map<String, Set<String>> listaConexionesPorAeropuertoEspanyol = new HashMap<>();
        Map<String, Set<String>> listaConexionesSalidas = new HashMap<>();
        List<Integer> indPorAeropuerto = new ArrayList<>();
        Map<String, List<List<String>>> conexionesPorAeropuerto = new HashMap<>();
        List<List<String>> conexionesAMantener = new ArrayList<>();
        
        List<List<String>> conexiones = new ArrayList<>();
        List<Double> riesgos = new ArrayList<>();
        List<Integer> vuelos = new ArrayList<>();
        List<Double> dineroMedio = new ArrayList<>();
        List<Integer> pasajeros = new ArrayList<>();
    	
        LecturaDeDatos.leerDatosNuevo(conexiones, riesgos, vuelos);
    	LecturaDeDatos.leerDatosDineroMedioNuevo(conexiones, dineroMedio);
    	LecturaDeDatos.leerDatosPasajerosNuevo(conexiones, pasajeros);
    	LecturaDeDatos.leerDatosAeropuertosEntradas(AeropuertosEntrada);
    	LecturaDeDatos.leerDatosAeropuertosEspanyoles(AeropuertosEspanyoles);
    	LecturaDeDatos.leerDatosAeropuertosOrigen(AeropuertosOrigen);
    	LecturaDeDatos.leerDatosCompanyias(companyias);
    	LecturaDeDatos.leerDatosPasajerosCompanyia(pasajerosCompanyia);
    	LecturaDeDatos.leerDatosConectividad(vuelosEntrantesConexion, vuelosSalientesAEspanya,
    			vuelosSalientes, conectividadesAeropuertosOrigen, conexiones, AeropuertosOrigen);
    	LecturaDeDatos.leerDatosListaConexiones(listaConexionesPorAeropuertoEspanyol, AeropuertosEntrada, conexiones);
    	LecturaDeDatos.leerDatosListaConexionesSalidas(listaConexionesSalidas, AeropuertosOrigen, conexiones);
    	LecturaDeDatos.leerFicherosAeropuertos(AeropuertosEntrada, indPorAeropuerto, conexionesPorAeropuerto);
    	LecturaDeDatos.leerConexionesAMantener(conexionesAMantener);
		
    	/*VuelosExt problemaext = new VuelosExt(conexiones.size(), 
    			AeropuertosEntrada, AeropuertosEspanyoles, AeropuertosOrigen,
    			companyias, pasajerosCompanyia,
    			vuelosEntrantesConexion, vuelosSalientesAEspanya, 
    			vuelosSalientes, conectividadesAeropuertosOrigen,
    			listaConexionesPorAeropuertoEspanyol, listaConexionesSalidas, conexionesAMantener, 
    			conexiones, riesgos, vuelos, dineroMedio, pasajeros);*/
    	
    	SubVuelos subproblema = new SubVuelos(AeropuertosEntrada.size(), AeropuertosEntrada, AeropuertosOrigen,
    			companyias, pasajerosCompanyia,
    			vuelosEntrantesConexion, vuelosSalientesAEspanya, 
    			vuelosSalientes, conectividadesAeropuertosOrigen,
    			listaConexionesPorAeropuertoEspanyol, listaConexionesSalidas, indPorAeropuerto, 
    			conexionesPorAeropuerto, conexiones, riesgos, dineroMedio, pasajeros);
    	
    	List<Individuo> solucionesOptimas = Utils.leerCSV("solucionesCompromiso.csv");
    	Individuo ind = subproblema.traducirIndividuo(solucionesOptimas.get(0));
    	
    	
    	/*ind = problemaext.inicializarValores(ind);
    	ind = problemaext.inicializarValores(ind);
    	ind = problemaext.inicializarValores(ind);*/
    	
    	/*List<Double> aux = ind.getVariables();
		for(int i = 0; i < problemaext.getDireccionesAMantener().size(); i++) {
			aux.add(problemaext.getDireccionesAMantener().get(i), 1.0);
		}*/
		
		//ind.setVariables(aux);
    	
    	List<Aeropuerto> lista = TraducirSalida.traducir(ind, conexiones);
		
    	DatosConexiones datosConexiones = new DatosConexiones(lista, ind);
    	
		return datosConexiones;
	}

}
