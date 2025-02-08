package enstabretagne.applications.capricorn.scenario;

import org.apache.commons.geometry.euclidean.twod.Vector2D;

import enstabretagne.applications.capricorn.environnement.Environement;
import enstabretagne.applications.capricorn.environnement.EnvironnementInit;
import enstabretagne.applications.capricorn.mobile.Mobile;
import enstabretagne.applications.capricorn.mobile.MobileInit;
import enstabretagne.applications.capricorn.radar.Radar;
import enstabretagne.applications.capricorn.radar.RadarInit;
import enstabretagne.base.time.LogicalDuration;
import enstabretagne.engine.InitData;
import enstabretagne.engine.SimuEngine;
import enstabretagne.engine.SimuScenario;

public class ScenarioSimple extends SimuScenario{

	ScenarioSimpleInit ini;
	public ScenarioSimple(SimuEngine engine, InitData ini) {
		super(engine, ini);
		this.ini = (ScenarioSimpleInit) ini;
	}

	@Override
	public void Init() {
		int scaleX = 3;	//todo: if kept: adjust that factor in the Mobile Class as well	// adjustment parameters for a better view on the map
		int scaleY = 30;		// adjustment parameters for a better view on the map
		super.Init();
		var envIni = new EnvironnementInit("Env");
		var factory = envIni.addPosition("Factory", Vector2D.of(310*scaleX, 5*scaleY));
		var radar = envIni.addPosition("Radar", Vector2D.of(309*scaleX, 5*scaleY));
		var p1 = envIni.addPosition("P1", Vector2D.of(300*scaleX, 1*scaleY));
		var p2 = envIni.addPosition("P2", Vector2D.of(309*scaleX, 4*scaleY));
		var p3 = envIni.addPosition("P3", Vector2D.of(309*scaleX, 6*scaleY));
		var p4 = envIni.addPosition("P4", Vector2D.of(300*scaleX, 10*scaleY));
		var cesna = envIni.addPosition("cesna", Vector2D.of(0*scaleX, 5*scaleY));

		var env = new Environement(engine, envIni);

		// period: period of the "scan" rescheduling
		var iniR = new RadarInit("R",radar,50, LogicalDuration.ofSeconds(1));
		new Radar(engine,iniR);
		
		var iniM = new MobileInit("M1",cesna,LogicalDuration.ofSeconds(1));
		new Mobile(engine,iniM);



		
	}

}
