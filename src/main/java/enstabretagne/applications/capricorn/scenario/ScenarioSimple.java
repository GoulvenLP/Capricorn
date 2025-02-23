package enstabretagne.applications.capricorn.scenario;

import enstabretagne.applications.capricorn.commandcenter.CommandCenter;
import enstabretagne.applications.capricorn.commandcenter.CommandCenterInit;
import enstabretagne.applications.capricorn.expertise.Location;
import enstabretagne.applications.capricorn.factory.Factory;
import enstabretagne.applications.capricorn.factory.FactoryInit;
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

import java.util.Random;


public class ScenarioSimple extends SimuScenario{

	private ScenarioSimpleInit ini;

	public ScenarioSimple(SimuEngine engine, InitData ini) {
		super(engine, ini);
		this.ini = (ScenarioSimpleInit) ini;
	}

	@Override
	public void Init() {
		//int scaleX = 1;	//in meters
		//int scaleY = 1; 	// in meters

		int scaleX = 5;	//in meters
		int scaleY = 20; 	// in meters

		int base_distance = 100; // just for better visualization

		super.Init();
		var envIni = new EnvironnementInit("Env");
		//var factory = envIni.addPosition("Factory", Vector2D.of((base_distance+40)*scaleX, 10*scaleY));
		var factory = new Location("Factory", Vector2D.of((base_distance+40)*scaleX, 10*scaleY));
		var radar = envIni.addPosition("Radar", Vector2D.of((base_distance+30)*scaleX, 10*scaleY));
		var p1 = envIni.addPosition("P1", Vector2D.of(base_distance*scaleX, 0*scaleY));
		var p2 = envIni.addPosition("P2", Vector2D.of((base_distance+30)*scaleX, 2*scaleY));
		var p3 = envIni.addPosition("P3", Vector2D.of((base_distance+30)*scaleX, 18*scaleY));
		var p4 = envIni.addPosition("P4", Vector2D.of(base_distance*scaleX, 20*scaleY));

		//var cesna = envIni.addPosition("cesna", Vector2D.of(0*scaleX, 10*scaleY));
		var cesna_1 = envIni.addPosition("cesna1", Vector2D.of(0*scaleX, 10*scaleY));
		var cesna_2 = envIni.addPosition("cesna2", Vector2D.of(10*scaleX, 0*scaleY));
		var cesna_3 = envIni.addPosition("cesna3", Vector2D.of(5*scaleX, 5*scaleY));
		var cesna_4 = envIni.addPosition("cesna4", Vector2D.of(2.5*scaleX, 2.5*scaleY));
		var cesna_5 = envIni.addPosition("cesna5", Vector2D.of(7.5*scaleX, 7.5*scaleY));
		var env = new Environement(engine, envIni);

		// period: period of the "scan" rescheduling

		// Factory
		var iniF = new FactoryInit("Factory",new Location("Factory",factory.position()));

		// Missiles (init)
		var iniM1 = new MissileInit("M1",p1,LogicalDuration.ofSeconds(1), 1, LogicalDuration.ofSeconds(3));
		var iniM2 = new MissileInit("M2",p2,LogicalDuration.ofSeconds(1), 2,LogicalDuration.ofSeconds(3));
		var iniM3 = new MissileInit("M3",p3,LogicalDuration.ofSeconds(1), 3,LogicalDuration.ofSeconds(3));
		var iniM4 = new MissileInit("M4",p4,LogicalDuration.ofSeconds(1), 4,LogicalDuration.ofSeconds(3));

		// Radar
		var iniR = new RadarInit("R",radar,300, LogicalDuration.ofSeconds(1));

		// Command Center
		var iniCC = new CommandCenterInit("CommandCenter");


		// Cessna - the target point is the factory
		var iniCessna1 = new MobileInit("C1",cesna_1,LogicalDuration.ofSeconds(1), factory);
		var iniCessna2 = new MobileInit("C2",cesna_2,LogicalDuration.ofSeconds(1), factory);
		var iniCessna3 = new MobileInit("C3",cesna_3,LogicalDuration.ofSeconds(1), factory);
		var iniCessna4 = new MobileInit("C4",cesna_4,LogicalDuration.ofSeconds(1), factory);
		var iniCessna5 = new MobileInit("C5",cesna_5,LogicalDuration.ofSeconds(1), factory);

		var cc = new CommandCenter(engine, iniCC, ini.getNbCessna(), ini.getSpeed());


		new Factory(engine, iniF, cc);

		new Radar(engine,iniR, cc);

		// missiles are charged, but not fired yet. All rattached to the command center
		new Missile(engine,iniM1, cc);
		new Missile(engine,iniM2, cc);
		new Missile(engine,iniM3, cc);
		new Missile(engine,iniM4, cc);

		// Mobiles: declare always 3 (minimal number for the simulation)
		double speed = this.ini.getSpeed();



		int totalDelay = 0;
		switch (this.ini.getNbCessna()) {
			case 1:
				new Mobile(engine,iniCessna1, speed, LogicalDuration.ofSeconds(0));
				break;
			case 2:
				new Mobile(engine,iniCessna1, speed, LogicalDuration.ofSeconds(0));
				new Mobile(engine,iniCessna2, speed, LogicalDuration.ofSeconds(getStarterDelay()));
				break;
			case 3:
				new Mobile(engine,iniCessna1, speed, LogicalDuration.ofSeconds(0));
				totalDelay = getStarterDelay();
				new Mobile(engine,iniCessna2, speed, LogicalDuration.ofSeconds(totalDelay));
				totalDelay += getStarterDelay();
				new Mobile(engine,iniCessna3, speed, LogicalDuration.ofSeconds(totalDelay));
				break;
			case 4:
				new Mobile(engine,iniCessna1, speed, LogicalDuration.ofSeconds(0));
				totalDelay = getStarterDelay();
				new Mobile(engine,iniCessna2, speed, LogicalDuration.ofSeconds(totalDelay));
				totalDelay += getStarterDelay();
				new Mobile(engine,iniCessna3, speed, LogicalDuration.ofSeconds(totalDelay));
				totalDelay += getStarterDelay();
				new Mobile(engine, iniCessna4, this.ini.getSpeed(), LogicalDuration.ofSeconds(totalDelay));
				break;
			case 5: // maximal number for the simulation
				new Mobile(engine,iniCessna1, speed, LogicalDuration.ofSeconds(0));
				totalDelay = getStarterDelay();
				new Mobile(engine,iniCessna2, speed, LogicalDuration.ofSeconds(totalDelay));
				totalDelay += getStarterDelay();
				new Mobile(engine,iniCessna3, speed, LogicalDuration.ofSeconds(totalDelay));
				totalDelay += getStarterDelay();
				new Mobile(engine, iniCessna4, speed, LogicalDuration.ofSeconds(totalDelay));
				totalDelay += getStarterDelay();
				new Mobile(engine, iniCessna5, speed, LogicalDuration.ofSeconds(totalDelay));
				break;
			default:
				//do nothing
		}
	}

	/**
	 * Generator of a random delay for the arrival of the first mobile, based on
	 * Poisson's law. It takes an average of 5 minutes for a plane to go. This
	 * delay must be inferior to 10 minutes.
	 * @return a delay of time in seconds
	 */
	private static int getStarterDelay(){
		final double lambda = 1./10.;
		final double max_delay = 10 * 60.;
		Random rand = new Random();
		double time = (-Math.log(1.0 - rand.nextDouble()) / lambda);
		while (time >= max_delay){
			time = (-Math.log(1.0 - rand.nextDouble()) / lambda);
		}
		return (int)time;
	}

}
