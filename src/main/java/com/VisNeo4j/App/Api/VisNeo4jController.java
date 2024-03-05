package com.VisNeo4j.App.Api;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.VisNeo4j.App.Algoritmo.BPSO;
import com.VisNeo4j.App.Constantes.Constantes;
import com.VisNeo4j.App.Lectura.LecturaDeDatos;
import com.VisNeo4j.App.Modelo.DatosProblemaDias;
import com.VisNeo4j.App.Modelo.DatosRRPS_PAT;
import com.VisNeo4j.App.Modelo.Individuo;
import com.VisNeo4j.App.Modelo.ObjectivesOrder;
import com.VisNeo4j.App.Modelo.Salida.Aeropuerto;
import com.VisNeo4j.App.Modelo.Salida.DatosConexiones;
import com.VisNeo4j.App.Modelo.Salida.FitnessI;
import com.VisNeo4j.App.Modelo.Salida.Objetivo;
import com.VisNeo4j.App.Modelo.Salida.Persona;
import com.VisNeo4j.App.Modelo.Salida.TraducirSalida;
import com.VisNeo4j.App.Modelo.Salida.Vuelos;
import com.VisNeo4j.App.Problemas.GestionConexionesAeropuertosPorDia;
import com.VisNeo4j.App.Problemas.Problema;
import com.VisNeo4j.App.Problemas.RRPS_PAT;
import com.VisNeo4j.App.Problemas.SubVuelos;
import com.VisNeo4j.App.Service.VisNeo4jService;
import com.VisNeo4j.App.Utils.Utils;
import com.opencsv.exceptions.CsvException;

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
	
	/*@CrossOrigin
	@PostMapping("/algoritmo")
	public DatosConexiones ejecutarAlgoritmo(@RequestParam("dia_inicial") String dia_I, 
			@RequestParam("dia_final") String dia_F,
			@RequestParam("mes_inicial") String mes_I,
			@RequestParam("mes_final") String mes_F,
			@RequestParam("año_inicial") String año_I,
			@RequestParam("año_final") String año_F,
			@RequestParam("iteraciones") int num_Iteraciones,
			@RequestBody ObjectivesOrder order) throws FileNotFoundException, IOException, CsvException, ParseException {
		DatosProblemaDias datos = visNeo4jService.obtenerDatosDias(dia_I, dia_F, mes_I, mes_F, año_I, año_F);
		
		Problema problema = new GestionConexionesAeropuertosPorDia(datos, order.getOrder(), 0.4); 
		BPSO bpso = new BPSO(4, num_Iteraciones, problema, 0.9, 2, 2);
		Individuo ind = bpso.ejecutarBPSO();
		datos.rellenarConexionesFaltantes(ind);
		System.out.println(ind);
		//TODO: Guardar solucion y conexiones en ficheros
		String fila = Utils.modificarCSVproblemaGestionConexionesAeropuertos(ind, datos);
		Utils.crearCSVConFitnessPorIteracion(ind.getFitnessHist(), fila);
		Utils.crearCSVObjetivos(ind.getObjetivosNorm(), ind.getRestricciones(), fila);
		Utils.crearCSVPersonas_Afectadas(List.of(datos.getNumPasajerosTotales() * ind.getObjetivosNorm().get(0), datos.getNumPasajerosTotales() - datos.getNumPasajerosTotales() * ind.getObjetivosNorm().get(0)), fila);
		Utils.crearCSVVuelos_Cancelados(List.of(Integer.valueOf(problema.calcularValoresAdicionales(ind).get(Constantes.nombreCampoVuelosCancelados)), datos.getNumVuelosTotales() - Integer.valueOf(problema.calcularValoresAdicionales(ind).get(Constantes.nombreCampoVuelosCancelados))), fila);
		
		System.out.println(order);
		List<Aeropuerto> lista = Utils.obtenerUltimaSolucionDiaI(0);
		List<Double> bits = Utils.obtenerUltimosBitsDiaI(0);
		Utils.obtenernumDiasUltimaSolucion();
		DatosConexiones datosConexiones = new DatosConexiones(lista, bits);
    	return datosConexiones;
	}*/
	
	/*@CrossOrigin
	@GetMapping("/algoritmo/dia")
	public DatosConexiones ejecutarAlgoritmoDia(@RequestParam("dia_inicial") String dia_I, 
			@RequestParam("dia_final") String dia_F,
			@RequestParam("mes_inicial") String mes_I,
			@RequestParam("mes_final") String mes_F,
			@RequestParam("año_inicial") String año_I,
			@RequestParam("año_final") String año_F,
			@RequestParam("iteraciones") int num_Iteraciones) throws FileNotFoundException, IOException, CsvException, ParseException {
		DatosProblemaDias datos = visNeo4jService.obtenerDatosDias(dia_I, dia_F, mes_I, mes_F, año_I, año_F);
		List<Integer> ordenObj = new ArrayList<>();
		ordenObj.add(1);
		ordenObj.add(2);
		ordenObj.add(3);
		ordenObj.add(4);
		ordenObj.add(5);
		ordenObj.add(6);
		ordenObj.add(7);
		Problema problema = new GestionConexionesAeropuertosPorDia(datos, ordenObj, 0.4); 
		BPSO bpso = new BPSO(4, num_Iteraciones, problema, 0.9, 2, 2);
		Individuo ind = bpso.ejecutarBPSO();
		datos.rellenarConexionesFaltantes(ind);
		System.out.println(ind);
		//TODO: Guardar solucion y conexiones en ficheros
		String fila = Utils.modificarCSVproblemaGestionConexionesAeropuertos(ind, datos);
		Utils.crearCSVConFitnessPorIteracion(ind.getFitnessHist(), fila);
		Utils.crearCSVObjetivos(ind.getObjetivosNorm(), ind.getRestricciones(), fila);
		Utils.crearCSVPersonas_Afectadas(List.of(datos.getNumPasajerosTotales() * ind.getObjetivosNorm().get(0), datos.getNumPasajerosTotales() - datos.getNumPasajerosTotales() * ind.getObjetivosNorm().get(0)), fila);
		Utils.crearCSVVuelos_Cancelados(List.of(Integer.valueOf(problema.calcularValoresAdicionales(ind).get(Constantes.nombreCampoVuelosCancelados)), datos.getNumVuelosTotales() - Integer.valueOf(problema.calcularValoresAdicionales(ind).get(Constantes.nombreCampoVuelosCancelados))), fila);
		//List<Aeropuerto> lista = TraducirSalida.traducir(ind, datos.getConexionesTotales());
		
		List<Aeropuerto> lista = Utils.obtenerUltimaSolucionDiaI(0);
		List<Double> bits = Utils.obtenerUltimosBitsDiaI(0);
		Utils.obtenernumDiasUltimaSolucion();
		DatosConexiones datosConexiones = new DatosConexiones(lista, bits);
    	return datosConexiones;
	}*/
	
	@CrossOrigin
	@GetMapping("/algoritmo/diaF")
	public DatosConexiones ejecutarAlgoritmoDiaFichero(@RequestParam("dia_inicial") String dia_I, 
			@RequestParam("dia_final") String dia_F,
			@RequestParam("mes_inicial") String mes_I,
			@RequestParam("mes_final") String mes_F,
			@RequestParam("año_inicial") String año_I,
			@RequestParam("año_final") String año_F,
			@RequestParam("iteraciones") int num_Iteraciones) throws FileNotFoundException, IOException, CsvException, ParseException {
		DatosProblemaDias datos = visNeo4jService.obtenerDatosDiasFichero(dia_I, dia_F, mes_I, mes_F, año_I, año_F);
		List<Double> pesos = new ArrayList<>();
		pesos.add(0.06861682918020946189960274467317);//Z6 Pérdida de pasajeros
		pesos.add(0.30335861321776814734561213434453);//Z1 Pérdida de ingresos turismo
		pesos.add(0.15890213073311664860960635608523);//Z3 Homogeneidad Pérdida de pasajeros Aerolineas
		pesos.add(0.2058504875406283856988082340195);//Z2 Homogeneidad Pérdida de ingresos turismo
		pesos.add(0.12459371614301191765980498374865);//Z4 Tasas
		pesos.add(0.09534127843986998916576381365114);//Z5 Homogeneidad tasas
		pesos.add(0.04333694474539544962080173347779);//Z7 Conectividad
		Problema problema = new GestionConexionesAeropuertosPorDia(datos, pesos, 0.75);
		BPSO bpso = new BPSO(4, num_Iteraciones, problema, 0.9, 1.5, 1.5);
		Individuo ind = bpso.ejecutarBPSO();
		datos.rellenarConexionesFaltantes(ind);
		System.out.println(ind);
		//TODO: Guardar solucion y conexiones en ficheros
		String fila = Utils.modificarCSVproblemaGestionConexionesAeropuertos(ind, datos);
		Utils.crearCSVConFitnessPorIteracion(ind.getFitnessHist(), fila);
		Utils.crearCSVObjetivos(ind.getObjetivosNorm(), ind.getRestricciones(), fila);
		Utils.crearCSVPersonas_Afectadas(List.of(datos.getNumPasajerosTotales() * ind.getObjetivosNorm().get(0), datos.getNumPasajerosTotales() - datos.getNumPasajerosTotales() * ind.getObjetivosNorm().get(0)), fila);
		Utils.crearCSVVuelos_Cancelados(List.of(Integer.valueOf(problema.calcularValoresAdicionales(ind).get(Constantes.nombreCampoVuelosCancelados)), datos.getNumVuelosTotales() - Integer.valueOf(problema.calcularValoresAdicionales(ind).get(Constantes.nombreCampoVuelosCancelados))), fila);
		//List<Aeropuerto> lista = TraducirSalida.traducir(ind, datos.getConexionesTotales());
		
		List<Aeropuerto> lista = Utils.obtenerUltimaSolucionDiaI(0);
		List<Double> bits = Utils.obtenerUltimosBitsDiaI(0);
		Utils.obtenernumDiasUltimaSolucion();
		DatosConexiones datosConexiones = new DatosConexiones(lista, bits);
    	return datosConexiones;
	}
	
	@CrossOrigin
	@GetMapping("/datosF")
	public DatosRRPS_PAT obtenerDatosFichero(@RequestParam("dia_inicial") String dia_I, 
			@RequestParam("dia_final") String dia_F,
			@RequestParam("mes_inicial") String mes_I,
			@RequestParam("mes_final") String mes_F,
			@RequestParam("año_inicial") String año_I,
			@RequestParam("año_final") String año_F) throws FileNotFoundException, IOException, CsvException, ParseException {
		DatosRRPS_PAT datos = visNeo4jService.obtenerDatosRRPS_PATFichero(dia_I, dia_F, mes_I, mes_F, año_I, año_F);
		
		List<Double> pesos = new ArrayList<>();
		pesos.add(0.06861682918020946189960274467317);//Z6 Pérdida de pasajeros
		pesos.add(0.30335861321776814734561213434453);//Z1 Pérdida de ingresos turismo
		pesos.add(0.15890213073311664860960635608523);//Z3 Homogeneidad Pérdida de pasajeros Aerolineas
		pesos.add(0.2058504875406283856988082340195);//Z2 Homogeneidad Pérdida de ingresos turismo
		pesos.add(0.12459371614301191765980498374865);//Z4 Tasas
		pesos.add(0.09534127843986998916576381365114);//Z5 Homogeneidad tasas
		pesos.add(0.04333694474539544962080173347779);//Z7 Conectividad
		Problema problema = new RRPS_PAT(datos, pesos, 0.75);
		Individuo ind = new Individuo(problema.getNumVariables(), 1);
		problema.inicializarValores(ind);
		problema.inicializarValores(ind);
		problema.inicializarValores(ind);
		problema.inicializarValores(ind);
		problema.inicializarValores(ind);
		
		problema.evaluate(ind);
		System.out.println(ind);
		
		DatosProblemaDias datos2 = visNeo4jService.obtenerDatosDiasFichero(dia_I, dia_F, mes_I, mes_F, año_I, año_F);
		Problema problema2 = new GestionConexionesAeropuertosPorDia(datos2, pesos, 0.75);
		Individuo ind2 = new Individuo(problema2.getNumVariables(), 1);
		problema2.inicializarValores(ind2);
		problema2.inicializarValores(ind2);
		problema2.inicializarValores(ind2);
		problema2.inicializarValores(ind2);
		problema2.inicializarValores(ind2);
		problema2.evaluate(ind2);
		System.out.println(ind2);
		
		return datos;
	}
	
	@CrossOrigin
	@GetMapping("/datos/dia")
	public DatosProblemaDias obtenerDatosVuelosPorDia(@RequestParam("dia_inicial") String dia_I, 
			@RequestParam("dia_final") String dia_F,
			@RequestParam("mes_inicial") String mes_I,
			@RequestParam("mes_final") String mes_F,
			@RequestParam("año_inicial") String año_I,
			@RequestParam("año_final") String año_F) throws FileNotFoundException, IOException, CsvException, ParseException {
		DatosProblemaDias datos = visNeo4jService.obtenerDatosDias(dia_I, dia_F, mes_I, mes_F, año_I, año_F);
		return datos;
	}
	
	@CrossOrigin
	@GetMapping("/datos/diaF")
	public DatosProblemaDias obtenerDatosVuelosPorDiaFichero(@RequestParam("dia_inicial") String dia_I, 
			@RequestParam("dia_final") String dia_F,
			@RequestParam("mes_inicial") String mes_I,
			@RequestParam("mes_final") String mes_F,
			@RequestParam("año_inicial") String año_I,
			@RequestParam("año_final") String año_F) throws FileNotFoundException, IOException, CsvException, ParseException {
		DatosProblemaDias datos = visNeo4jService.obtenerDatosDiasFichero(dia_I, dia_F, mes_I, mes_F, año_I, año_F);
		System.out.println(datos.getConexionesTotales().size());
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
	@GetMapping("/{id}/personas")
	public List<Persona> obtenerPersonasSolucionI(@PathVariable int id) throws FileNotFoundException, IOException, CsvException, ParseException {
		String nombreFichero = String.valueOf(id);
		return TraducirSalida.obtenerPersonasAfectadas(Utils.leerCSVPersonasAfectadas(nombreFichero));
	}
	
	@CrossOrigin
	@GetMapping("/{id}/vuelos")
	public List<Vuelos> obtenerVuelosSolucionI(@PathVariable int id) throws FileNotFoundException, IOException, CsvException, ParseException {
		String nombreFichero = String.valueOf(id);
		return TraducirSalida.obtenerVuelosCancelados(Utils.leerCSVVuelosCancelados(nombreFichero));
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
	
	@CrossOrigin
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
	}
	
	@CrossOrigin
	@PostMapping("/test")
	public int test(@RequestBody ObjectivesOrder order) throws FileNotFoundException, IOException, CsvException, ParseException {
		System.out.println(order);
		return 0;
		/*DatosProblemaDias datos = movieService.obtenerDatosDias(dia_I, dia_F, mes_I, mes_F, año_I, año_F);
		List<Double> pesos1 = new ArrayList<>();
		pesos1.add(0.7);
		pesos1.add(0.7);
		pesos1.add(0.7);
		pesos1.add(0.7);
		pesos1.add(0.7);
		pesos1.add(0.7);
		pesos1.add(0.3);
		GestionConexionesAeropuertosPorDia problema = new GestionConexionesAeropuertosPorDia(datos, pesos1);
		List<String> bits = Utils.leerCSVproblema(18);
		List<Double> bitsDouble = new ArrayList<>();
		for(int i = 2; i < bits.size(); i++) {
			bitsDouble.add(Double.valueOf(bits.get(i)));
		}
		Individuo ind = new Individuo(problema.getNumVariables(), 1);
		ind.setVariables(bitsDouble);
		//problema.evaluate2(ind);
		System.out.println(problema.calcularValoresAdicionales(ind));*/
		
		/*bits = Utils.leerCSVproblema(16);
		bitsDouble = new ArrayList<>();
		for(int i = 2; i < bits.size(); i++) {
			bitsDouble.add(Double.valueOf(bits.get(i)));
		}
		ind = new Individuo(problema.getNumVariables(), 1);
		ind.setVariables(bitsDouble);
		problema.evaluate2(ind);
		System.out.println(ind);*/
		//Utils.crearCSVVuelos_Cancelados(List.of(40, 60), "20");
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
