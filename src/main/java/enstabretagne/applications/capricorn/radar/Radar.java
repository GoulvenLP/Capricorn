package enstabretagne.applications.capricorn.radar;

import enstabretagne.applications.capricorn.expertise.ILocatable;
import enstabretagne.applications.capricorn.expertise.Location;
import enstabretagne.engine.EntiteSimulee;
import enstabretagne.engine.InitData;
import enstabretagne.engine.SimuEngine;

public class Radar extends EntiteSimulee implements ILocatable {

	public final RadarInit rIni;
	public Radar(SimuEngine engine, InitData ini) {
		super(engine, ini);
		rIni = (RadarInit) ini;
	}

	@Override
	public void Init() {
		super.Init();
	}

	@Override
	public Location position() {
		return rIni.position;
	}

}
