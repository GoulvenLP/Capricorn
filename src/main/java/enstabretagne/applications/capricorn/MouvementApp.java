package enstabretagne.applications.capricorn;

import java.util.HashMap;
import java.util.Map;

import enstabretagne.applications.capricorn.scenario.ScenarioSimple;
import enstabretagne.applications.capricorn.scenario.ScenarioSimpleInit;
import enstabretagne.base.time.LogicalDateTime;
import enstabretagne.base.time.LogicalDuration;
import enstabretagne.moniteur2D.MoniteurGraphique2D;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;
import javafx.application.Application;



public class MouvementApp {
	public static void main(String[] args) {
		JsonbConfig config = new JsonbConfig().withFormatting(true);
		Jsonb jsonb = JsonbBuilder.create(config);


		double graine = 15;
		var ldStart = LogicalDateTime.Now();
		var ldEnd = ldStart.add(LogicalDuration.ofMinutes(10));

		var sc = new ScenarioSimpleInit("SC1",1,graine,ldStart.toString(),ldEnd.toString(), 200, 5, 5);

		var scSerialized = jsonb.toJson(sc);

		Map<String,String> params = new HashMap<String,String>();

		params.put(MoniteurGraphique2D.RequiredParams.ScenarioType,ScenarioSimple.class.getTypeName());
		params.put(MoniteurGraphique2D.RequiredParams.ScenarioInitType,ScenarioSimpleInit.class.getTypeName());
		params.put(MoniteurGraphique2D.RequiredParams.ScenarioInit,scSerialized);
		params.put(MoniteurGraphique2D.RequiredParams.VisualConverter,MouvementAppVisualConverter.class.getTypeName());

		String[] namedParams = params.entrySet().stream()
				.map(entry -> "--" + entry.getKey() + "=" + entry.getValue())
				.toArray(String[]::new);

		Application.launch(MoniteurGraphique2D.class, namedParams);
	}
}
