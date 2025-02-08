package enstabretagne.applications.capricorn.radar;

import enstabretagne.applications.capricorn.expertise.ILocatable;
import enstabretagne.applications.capricorn.expertise.Location;
import enstabretagne.base.logger.Logger;
import enstabretagne.base.time.LogicalDateTime;
import enstabretagne.engine.EntiteSimulee;
import enstabretagne.engine.InitData;
import enstabretagne.engine.SimEvent;
import enstabretagne.engine.SimuEngine;

public class Radar extends EntiteSimulee implements ILocatable {

	public final RadarInit rIni;

	SimEvent RadarEvent;

	public Radar(SimuEngine engine, InitData ini) {
		super(engine, ini);
		rIni = (RadarInit) ini;

		RadarEvent = new RadarEvent(engine.Now());
	}

	@Override
	public void Init() {

		super.Init();
		Post(RadarEvent);
	}

	@Override
	public Location position() {
		return rIni.position;
	}

}
