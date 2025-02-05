package enstabretagne.applications.capricorn.environnement;

import enstabretagne.engine.EntiteSimulee;
import enstabretagne.engine.InitData;
import enstabretagne.engine.SimuEngine;

public class Environement extends EntiteSimulee{

	public final EnvironnementInit ini;
	public Environement(SimuEngine engine, InitData ini) {
		super(engine, ini);
		this.ini = (EnvironnementInit) ini;
	}

	@Override
	public void Init() {
		super.Init();
	}

}
