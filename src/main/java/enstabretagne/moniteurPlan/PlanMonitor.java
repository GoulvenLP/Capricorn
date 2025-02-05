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
		//il porte la cr�ation du moteur
		this.engine = new SimuEngine();
		
		plan = p;
		plan.setEngine(engine);
	}

	//m�thode principale simple
	public void run() {
		//1. on cr�e les sc�narii
		plan.initScenarii();
		Logger.Information(this, "main", "D�but du plan d'exp�rience");

		//on boucle sur les sc�narios
		while (plan.hasNextScenario()) {
			//� chaque sc�nario on demande la cr�ation des entit�s de simulation correspondantes
			SimuScenario s = plan.nextScenario();
			SimuScenarioInitData sIni = (SimuScenarioInitData) s.getInit();
			//la cr�ation des entit�s a �t� port�e � la connaissance du moteur
			//noter que de nouvelles entit�s pourront �tre cr��es au cours de la simulation
			engine.initSimulation(s,new LogicalDateTime(sIni.start),new LogicalDateTime(sIni.end));
			//d�clenchement de la boucle de simulation
			engine.simulate();
			//fin du run, nettoyage du moteur en vue de la possible prochaine ex�cution de sc�nario
			engine.terminate(false);//ce n'est pas la derni�re ex�cution
		}
		engine.terminate(true);
		Logger.Information(null, "main", "Fin du plan d'exp�rience");
		Logger.Terminate();

	}
}
