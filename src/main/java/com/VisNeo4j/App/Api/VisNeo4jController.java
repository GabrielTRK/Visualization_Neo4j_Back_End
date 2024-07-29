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
import com.VisNeo4j.App.Modelo.Entrada.ResPolPref;
import com.VisNeo4j.App.Modelo.Entrada.Usuario;
import com.VisNeo4j.App.Modelo.Salida.Aeropuerto;
import com.VisNeo4j.App.Modelo.Salida.DatosConexiones;
import com.VisNeo4j.App.Modelo.Salida.FitnessI;
import com.VisNeo4j.App.Modelo.Salida.Histogramas;
import com.VisNeo4j.App.Modelo.Salida.Objetivo;
import com.VisNeo4j.App.Modelo.Salida.Persona;
import com.VisNeo4j.App.Modelo.Salida.Proyecto;
import com.VisNeo4j.App.Modelo.Salida.Rangos;
import com.VisNeo4j.App.Modelo.Salida.Restricciones;
import com.VisNeo4j.App.Modelo.Salida.Solucion;
import com.VisNeo4j.App.Modelo.Salida.TooltipTexts;
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
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
			@RequestParam("nombre") String nombreProyecto,
			@RequestBody ResPolPref resPolPref) throws IOException {
		
		return visNeo4jService.guardarProyecto(fecha_I, fecha_F, numIteraciones, numIndividuos, 
				inertiaW, c1, c2, m, p, resEpi, nombreProyecto, resPolPref);
	}
	
	@CrossOrigin
	@GetMapping("/loadP")
	public List<Proyecto> cargarProyectos() throws IOException, CsvException {
	    return visNeo4jService.obtenerListaProyectos();
	}
	
	@CrossOrigin
	@GetMapping("/{proyecto}/loadS")
	public List<Solucion> cargarSolucionesProyectoI(@PathVariable String proyecto) throws IOException, CsvException {
		return visNeo4jService.obtenerListaSolucionesProyectoI(proyecto);
	}
	
	@CrossOrigin
	@GetMapping("/{proyecto}/{id}/{dia}")
	public DatosConexiones cargarProyectoISolucionJDiaK(@PathVariable String proyecto, @PathVariable int id,
			@PathVariable int dia) throws FileNotFoundException, IOException, CsvException, ParseException {
		return visNeo4jService.cargarProyectoISolucionJDiaK(proyecto, id, dia);
	}
	
	@CrossOrigin
	@GetMapping("/{proyecto}/{id}/{dia}/{con}")
	public DatosConexiones cargarProyectoISolucionJDiaKFiltro(@PathVariable String proyecto, @PathVariable int id,
			@PathVariable int dia, @PathVariable String con) throws FileNotFoundException, IOException, CsvException, ParseException {
		return visNeo4jService.cargarProyectoISolucionJDiaKFiltro(proyecto, id, dia, con);
	}
	
	@CrossOrigin
	@GetMapping("/{proyecto}/{id}/numDias")
	public int numDiasSolucionI(@PathVariable String proyecto, @PathVariable int id) throws FileNotFoundException, IOException, CsvException, ParseException {
		return visNeo4jService.numDiasSolucionI(proyecto, id);
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
			@RequestParam("nombre") String nombreProyecto,
			@RequestBody ResPolPref resPolPref) throws FileNotFoundException, IOException, CsvException, ParseException {
		//TODO: Devolver id de solucion
		
		return visNeo4jService.guardarYOptimizar(fecha_I, fecha_F, numIteraciones, numIndividuos, 
				inertiaW, c1, c2, m, p, resEpi, nombreProyecto, resPolPref);
	}
	
	@CrossOrigin
	@PostMapping("/{proyecto}/optimize")
	public boolean runOptimization(@PathVariable String proyecto) throws FileNotFoundException, IOException, CsvException, ParseException {
		return visNeo4jService.optimizar(proyecto);
	}
	
	@CrossOrigin
	@PostMapping("/testEva")
	public DatosRRPS_PAT testEvaluacion(@RequestParam("fecha_inicial") String fecha_I, 
			@RequestParam("fecha_final") String fecha_F,
			@RequestBody ObjectivesOrder order) throws FileNotFoundException, IOException, CsvException, ParseException {
		DatosRRPS_PAT datos = visNeo4jService.obtenerDatosRRPS_PAT(fecha_I, fecha_F);
		
		DMPreferences preferencias = new DMPreferences(order, Constantes.nombreQDMPSR);
		preferencias.generateWeightsVector(order.getOrder().size());
		
		Problema problema = new RRPS_PAT(datos, 0.75, List.of(), preferencias);
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
	@GetMapping("/{proyecto}/{id}/hist")
	public List<FitnessI> obtenerHistSolucionI(@PathVariable String proyecto, @PathVariable int id) throws FileNotFoundException, IOException, CsvException, ParseException {
		return visNeo4jService.obtenerHistSolucionI(proyecto, id);
	}
	
	@CrossOrigin
	@GetMapping("/{proyecto}/{id}/objetivos")
	public List<Objetivo> obtenerObjSolucionI(@PathVariable String proyecto, 
			@PathVariable int id) throws FileNotFoundException, IOException, CsvException, ParseException {
		return visNeo4jService.obtenerObjSolucionI(proyecto, id);
	}
	
	@CrossOrigin
	@GetMapping("/{proyecto}/{id}/histogramas")
	public Histogramas obtenerHistogramasSolucionI(@PathVariable String proyecto, 
			@PathVariable int id) throws FileNotFoundException, IOException, CsvException, ParseException {
		return visNeo4jService.obtenerHistogramasSolucionI(proyecto, id);
	}
	
	@CrossOrigin
	@GetMapping("/{proyecto}/{id}/rangos")
	public Rangos obtenerRangosSolucionI(@PathVariable String proyecto, 
			@PathVariable int id) throws FileNotFoundException, IOException, CsvException, ParseException {
		return visNeo4jService.obtenerRangosSolucionI(proyecto, id);
	}
	
	@CrossOrigin
	@GetMapping("/tooltips")
	public TooltipTexts consultarTooltips(@RequestParam("fecha_inicial") String fecha_I, 
			@RequestParam("fecha_final") String fecha_F) throws FileNotFoundException, IOException, CsvException, ParseException {
		return visNeo4jService.obtenerTooltips(fecha_I, fecha_F);
	}
	
	
	@CrossOrigin
	@PostMapping("/login")
	public boolean logIn(@RequestBody Usuario user/*, @RequestBody String pass*/) throws FileNotFoundException, IOException, CsvException, ParseException {
		return visNeo4jService.comprobarUsuario(user);
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
	@GetMapping("/test")
	public void test() throws FileNotFoundException, IOException, CsvException, ParseException {
		// Enter data using BufferReader
        BufferedReader reader = new BufferedReader(
            new InputStreamReader(System.in));
        String name = "";
        while(!name.equals("1")) {
        	
        	
        	
        	
        	name = reader.readLine();
        }
        
        // Reading data using readLine
        

        // Printing the read line
        System.out.println(name);
	}
	
	@CrossOrigin
	@PostMapping("/optimizeALT")
	public boolean runOptimizationALT(@RequestParam("fecha_inicial") String fecha_I, 
			@RequestParam("fecha_final") String fecha_F,
			
			@RequestParam("iteraciones") int numIteraciones,
			@RequestParam("numIndividuos") int numIndividuos,
			@RequestParam("inertiaW") double inertiaW,
			@RequestParam("c1") double c1,
			@RequestParam("c2") double c2,
			@RequestParam("m") double m,
			@RequestParam("p") double p,
			@RequestParam("res_epi") double resEpi,
			@RequestParam("nombre") String nombreProyecto,
			@RequestBody ResPolPref resPolPref) throws FileNotFoundException, IOException, CsvException, ParseException {
		//TODO: Devolver id de solucion
		
		return visNeo4jService.guardarYOptimizarALT(fecha_I, fecha_F, numIteraciones, numIndividuos, 
				inertiaW, c1, c2, m, p, resEpi, nombreProyecto, resPolPref);
	}
	
	@CrossOrigin
	@PostMapping("/{proyecto}/optimizeALT")
	public boolean runOptimizationALT(@PathVariable String proyecto) throws FileNotFoundException, IOException, CsvException, ParseException {
		return visNeo4jService.optimizarALT(proyecto);
	}

}
