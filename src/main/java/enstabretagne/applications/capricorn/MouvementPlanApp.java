package enstabretagne.applications.capricorn;

import enstabretagne.applications.capricorn.scenario.PlanSimple;
import enstabretagne.base.logger.Logger;
import enstabretagne.base.time.LogicalDateTime;
import enstabretagne.base.time.LogicalDuration;
import enstabretagne.moniteurPlan.PlanMonitor;

public class MouvementPlanApp {
	public static void main(String[] args) {
		Logger.load();
		LogicalDateTime ldtStart = LogicalDateTime.Now();
		LogicalDateTime ldtEnd = ldtStart.add(LogicalDuration.ofMinutes(3));
		PlanSimple ps = new PlanSimple(2,
				ldtStart.toString(),
				ldtEnd.toString(),
				200, 300, 1, 3, 5, 3, 5, 5);
		
		PlanMonitor pm = new PlanMonitor(ps);
		pm.run();
		
		Logger.Terminate();
	}
}
