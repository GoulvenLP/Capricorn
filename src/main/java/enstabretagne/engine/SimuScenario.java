package enstabretagne.engine;

import enstabretagne.applications.capricorn.commandcenter.CommandCenter;
import enstabretagne.simulation.basics.ScenarioId;

public abstract class SimuScenario extends EntiteSimulee{
	ScenarioId id;
	public SimuScenario(SimuEngine engine, InitData ini) {
		super(engine, ini);
		var scIni = (SimuScenarioInitData) ini;
		this.id = new ScenarioId(scIni.name, scIni.replique);
	}

	public ScenarioId getID() {
		return id;
	}


}
