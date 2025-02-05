package enstabretagne.applications.capricorn.scenario;

import enstabretagne.engine.InitData;
import enstabretagne.engine.SimuScenarioInitData;
import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;

public class ScenarioSimpleInit extends SimuScenarioInitData {
	
	public final double vCessna;
	public final int nbCessna;
	public final double periodCessna;

	@JsonbCreator
	public ScenarioSimpleInit( @JsonbProperty(value = "name") String name,
			 @JsonbProperty(value = "replique") int replique,			 

			 @JsonbProperty(value = "graine") double graine,
			 @JsonbProperty(value = "start") String start,
			 @JsonbProperty(value = "end") String end,
			 @JsonbProperty(value = "vCessna") double vCessna,
			 @JsonbProperty(value = "nbCessna") int nbCessna,
			 @JsonbProperty(value = "periodCessna") double periodCessna) {
		super(name,replique,graine,start,end);
		this.vCessna =vCessna;
		this.nbCessna = nbCessna;
		this.periodCessna = periodCessna;
	}

	
}
