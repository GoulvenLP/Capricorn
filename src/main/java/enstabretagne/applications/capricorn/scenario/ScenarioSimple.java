package enstabretagne.applications.capricorn.scenario;

import enstabretagne.applications.capricorn.commandcenter.CommandCenter;
import enstabretagne.applications.capricorn.commandcenter.CommandCenterInit;
import enstabretagne.applications.capricorn.missile.Missile;
import enstabretagne.applications.capricorn.missile.MissileInit;
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

	private ScenarioSimpleInit ini;

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

		// Missiles (init)
		var iniM1 = new MissileInit("M1",p1,LogicalDuration.ofSeconds(1), 1);
		var iniM2 = new MissileInit("M2",p2,LogicalDuration.ofSeconds(1), 2);
		var iniM3 = new MissileInit("M3",p3,LogicalDuration.ofSeconds(1), 3);
		var iniM4 = new MissileInit("M4",p4,LogicalDuration.ofSeconds(1), 4);

		var iniCC = new CommandCenterInit("CommandCenter");

		var iniCessna = new MobileInit("C1",cesna,LogicalDuration.ofSeconds(1));

		var cc = new CommandCenter(engine, iniCC);

		new Radar(engine,iniR, cc);

		// missiles are charged, but not fired yet
		new Missile(engine,iniM1);
		new Missile(engine,iniM2);
		new Missile(engine,iniM3);
		new Missile(engine,iniM4);
		
		new Mobile(engine,iniCessna);

	}

}
