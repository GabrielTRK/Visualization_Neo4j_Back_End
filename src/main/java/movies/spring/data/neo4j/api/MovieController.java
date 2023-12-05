package movies.spring.data.neo4j.api;

import movies.spring.data.neo4j.movies.MovieDetailsDto;
import movies.spring.data.neo4j.movies.MovieResultDto;
import movies.spring.data.neo4j.movies.MovieService;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nsgaiii.nsgaiiidemo.App.TraducirSalida;
import com.nsgaiii.nsgaiiidemo.App.Algoritmo.BPSO;
import com.nsgaiii.nsgaiiidemo.App.Constantes.Constantes;
import com.nsgaiii.nsgaiiidemo.App.Lectura.LecturaDeDatos;
import com.nsgaiii.nsgaiiidemo.App.Modelo.DatosProblema;
import com.nsgaiii.nsgaiiidemo.App.Modelo.DatosProblemaDias;
import com.nsgaiii.nsgaiiidemo.App.Modelo.Individuo;
import com.nsgaiii.nsgaiiidemo.App.Modelo.Salida.Aeropuerto;
import com.nsgaiii.nsgaiiidemo.App.Modelo.Salida.DatosConexiones;
import com.nsgaiii.nsgaiiidemo.App.Modelo.Salida.FitnessI;
import com.nsgaiii.nsgaiiidemo.App.Modelo.Salida.Objetivo;
import com.nsgaiii.nsgaiiidemo.App.Modelo.Salida.Persona;
import com.nsgaiii.nsgaiiidemo.App.Modelo.Salida.Vuelos;
import com.nsgaiii.nsgaiiidemo.App.Problemas.GestionConexionesAeropuertos;
import com.nsgaiii.nsgaiiidemo.App.Problemas.GestionConexionesAeropuertosPorDia;
import com.nsgaiii.nsgaiiidemo.App.Problemas.Problema;
import com.nsgaiii.nsgaiiidemo.App.Problemas.SubVuelos;
import com.nsgaiii.nsgaiiidemo.App.Utils.Utils;
import com.opencsv.exceptions.CsvException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author Michael J. Simons
 */
@RestController
class MovieController {

	private final MovieService movieService;

	MovieController(MovieService movieService) {
		this.movieService = movieService;
	}
	
	@CrossOrigin
	@GetMapping("/algoritmo/dia")
	public DatosConexiones ejecutarAlgoritmoDia(@RequestParam("dia_inicial") String dia_I, 
			@RequestParam("dia_final") String dia_F,
			@RequestParam("mes_inicial") String mes_I,
			@RequestParam("mes_final") String mes_F,
			@RequestParam("año_inicial") String año_I,
			@RequestParam("año_final") String año_F,
			@RequestParam("iteraciones") int num_Iteraciones) throws FileNotFoundException, IOException, CsvException, ParseException {
		DatosProblemaDias datos = movieService.obtenerDatosDias(dia_I, dia_F, mes_I, mes_F, año_I, año_F);
		List<Double> pesos = new ArrayList<>();
		pesos.add(0.9);
		/*pesos.add(0.7);
		pesos.add(0.7);
		pesos.add(0.7);
		pesos.add(0.7);
		pesos.add(0.7);*/
		pesos.add(0.1);
		Problema problema = new GestionConexionesAeropuertosPorDia(datos, pesos); 
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
		int numDias = Utils.obtenernumDiasUltimaSolucion();
		DatosConexiones datosConexiones = new DatosConexiones(lista, bits);
    	return datosConexiones;
	}
	
	@CrossOrigin
	@GetMapping("/datos/dia")
	public DatosProblemaDias obtenerDatosVuelosPorDia(@RequestParam("dia_inicial") String dia_I, 
			@RequestParam("dia_final") String dia_F,
			@RequestParam("mes_inicial") String mes_I,
			@RequestParam("mes_final") String mes_F,
			@RequestParam("año_inicial") String año_I,
			@RequestParam("año_final") String año_F) throws FileNotFoundException, IOException, CsvException, ParseException {
		DatosProblemaDias datos = movieService.obtenerDatosDias(dia_I, dia_F, mes_I, mes_F, año_I, año_F);
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
	@GetMapping("/test")
	public void test(@RequestParam("dia_inicial") String dia_I, 
			@RequestParam("dia_final") String dia_F,
			@RequestParam("mes_inicial") String mes_I,
			@RequestParam("mes_final") String mes_F,
			@RequestParam("año_inicial") String año_I,
			@RequestParam("año_final") String año_F) throws FileNotFoundException, IOException, CsvException, ParseException {
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
		Utils.crearCSVVuelos_Cancelados(List.of(40, 60), "20");
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

	private static String stripWildcards(String title) {
		String result = title;
		if (result.startsWith("*")) {
			result = result.substring(1);
		}
		if (result.endsWith("*")) {
			result = result.substring(0, result.length() - 1);
		}
		return result;
	}
}
