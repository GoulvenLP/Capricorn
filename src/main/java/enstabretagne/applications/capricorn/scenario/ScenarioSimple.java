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
		super.Init();
		var envIni = new EnvironnementInit("Env");
		var A = envIni.addPosition("A", Vector2D.of(100, 100));
		var B = envIni.addPosition("B", Vector2D.of(200, 300));
		var C = envIni.addPosition("C", Vector2D.of(-100, 100));
		var D = envIni.addPosition("D", Vector2D.of(-500, 600));

		var env = new Environement(engine, envIni);
		
		var iniR = new RadarInit("R",A,50);
		new Radar(engine,iniR);
		
		var iniM = new MobileInit("M1",D,LogicalDuration.ofSeconds(1));
		new Mobile(engine,iniM);
		
	}

}
