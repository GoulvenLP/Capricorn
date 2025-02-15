package enstabretagne.moniteurPlan;

import enstabretagne.base.logger.Logger;
import enstabretagne.base.time.LogicalDateTime;
import enstabretagne.engine.SimuEngine;
import enstabretagne.engine.SimuScenario;
import enstabretagne.engine.SimuScenarioInitData;

public class PlanMonitor {
	Plan plan;
	SimuEngine engine;

	public PlanMonitor(Plan p) {
		//il porte la creation du moteur
		this.engine = new SimuEngine();
		
		plan = p;
		plan.setEngine(engine);
	}

	//methode principale simple
	public void run() {
		//1. on cree les scenarii
		plan.initScenarii();
		Logger.Information(this, "main", "Debut du plan d'experience");

		//on boucle sur les scenarios
		while (plan.hasNextScenario()) {
			//a chaque scenario on demande la creation des entites de simulation correspondantes
			SimuScenario s = plan.nextScenario();
			SimuScenarioInitData sIni = (SimuScenarioInitData) s.getInit();
			//la creation des entites a ete portee a la connaissance du moteur
			//noter que de nouvelles entites pourront etre creees au cours de la simulation
			engine.initSimulation(s,new LogicalDateTime(sIni.start),new LogicalDateTime(sIni.end));
			//declenchement de la boucle de simulation
			engine.simulate();
			//fin du run, nettoyage du moteur en vue de la possible prochaine execution de scenario
			engine.terminate(false);//ce n'est pas la derniere execution
		}
		engine.terminate(true);
		Logger.Information(null, "main", "Fin du plan d'experience");
		Logger.Terminate();

	}
}
