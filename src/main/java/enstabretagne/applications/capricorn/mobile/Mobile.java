package enstabretagne.applications.capricorn.mobile;

import org.apache.commons.geometry.euclidean.twod.Vector2D;

import enstabretagne.applications.capricorn.expertise.ILocatable;
import enstabretagne.applications.capricorn.expertise.Location;
import enstabretagne.base.logger.Logger;
import enstabretagne.engine.EntiteSimulee;
import enstabretagne.engine.InitData;
import enstabretagne.engine.SimEvent;
import enstabretagne.engine.SimuEngine;

public class Mobile extends EntiteSimulee implements ILocatable{

	public final MobileInit ini;
	Location p;
	SimEvent Bonjour;
	public Mobile(SimuEngine engine, InitData ini) {
		super(engine, ini);
		this.ini = (MobileInit) ini;
		
		Bonjour = new SimEvent(engine.Now()) {
			@Override
			public void process() {
				bonjour();
				Bonjour.rescheduleAt(Now().add(Mobile.this.ini.period));
				Post(Bonjour);
			}
		};
	}

	@Override
	public void Init() {
		super.Init();
		p=ini.position;
		Post(Bonjour);
	}


	@Override
	public Location position() {
		return p;
	}

	public void bonjour() {
		Logger.Information(this, "bonjour", "Bonjour POsition :" + position());
		p=p.add(Vector2D.of(10,0));
	}
	@Override
	public String toString() {
		return ini.name;
	}

}
