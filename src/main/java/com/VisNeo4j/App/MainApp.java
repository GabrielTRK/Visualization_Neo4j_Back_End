package com.VisNeo4j.App;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.VisNeo4j.App.Algoritmo.BPSO;
import com.VisNeo4j.App.Algoritmo.Opciones.BPSOOpciones;
import com.VisNeo4j.App.Algoritmo.Parametros.BPSOParams;
import com.VisNeo4j.App.Constantes.Constantes;
import com.VisNeo4j.App.Modelo.Entrada.PreferencesConstraints;
import com.VisNeo4j.App.Problems.Problem;
import com.VisNeo4j.App.Problems.RRPS_PAT;
import com.VisNeo4j.App.Problems.Data.DataRRPS_PAT;
import com.VisNeo4j.App.Problems.Knapsack.Knapsack01_Ks_8a;
import com.VisNeo4j.App.QDMP.DMPreferences;
import com.VisNeo4j.App.QDMP.ObjectivesOrder;
import com.VisNeo4j.App.Service.VisNeo4jService;
import com.VisNeo4j.App.Utils.Utils;
import com.opencsv.exceptions.CsvException;

public class MainApp {
	
	VisNeo4jService visNeo4jService;

	MainApp(VisNeo4jService visNeo4jService) {
		this.visNeo4jService = visNeo4jService;
	}
	
	public static void main( String[] args ) throws FileNotFoundException, IOException, CsvException, ParseException {
		
//--------------------------------------BPSO Params------------------------------------------
		int numParticles = 50;
		double inertiaW = 0.9;
		double c1 = 1.0;
		double c2 = 1.0;
		int maxFunctionEv = 5000;
		double SRate = 0.5;
		
//------------------------------------Knapsack problem declaration---------------------------
		
		//Choose any knapsack problem from the Problems.Knapsack package
		Problem p = new Knapsack01_Ks_8a();
		
		
//--------------------------Air traffic management problem declaration--------------------------------
		
		//Specify the start date(inclusive) and the end date(inclusive) that should be considered.
		//All the flights from that time window will be added to the problem
		//The date format is yyyy-MM-DD
		String startDate = "2020-09-30";
		String endDate = "2020-09-30";
		
		DataRRPS_PAT data = Utils.getDataRRPS_PAT(startDate, endDate);
		
		//Specify continents whose capital cities must stay connected to their destinations(optional)
		//The possible continents are "AF","AS","EU","NA","OC","SA"
		List<String> pol = new ArrayList<>();
		//pol = Stream.of("EU","").collect(Collectors.toList());
		
		//Specify the level of importance of each objective. From most important to least important.
		//There are 7 objectives in total whose IDs go from 1 to 7.
		//All of the objectives must be included in the list bellow.
		List<Integer> order = Stream.of(1,2,3,4,5,6,7).collect(Collectors.toList());
		
		Map<Integer, Double> epiConstraint = new HashMap<>();
		//Specify the maximum amount of imported risk percentage allowed.
		epiConstraint.put(0, 50.0);
		
		PreferencesConstraints preferencesConstraints = new PreferencesConstraints(pol, order, epiConstraint);
		
		DMPreferences preferences = new DMPreferences(new ObjectivesOrder(preferencesConstraints.getOrdenObj(), preferencesConstraints.getRestricciones()), Constantes.nombreQDMPSR);
		preferences.generateWeightsVector(preferencesConstraints.getOrdenObj().size());
		
		//Problem p = new RRPS_PAT(data, preferencesConstraints.getPol(), preferences, SRate);
		
		
		
		BPSOParams params = new BPSOParams(numParticles, inertiaW, c1, c2, 
						maxFunctionEv, Constantes.nombreCPGenerica, 
						Constantes.nombreIWLinearDecreasing);
		
		BPSO bpso = new BPSO(p, params, "ProjectName", new BPSOOpciones(false, 0));
		
		System.out.println(bpso.runBPSO());
	}

}
