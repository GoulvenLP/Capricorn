package enstabretagne.applications.capricorn.missile;

import enstabretagne.applications.capricorn.expertise.ILocatable;
import enstabretagne.applications.capricorn.expertise.Location;
import enstabretagne.applications.capricorn.mobile.MobileInit;
import enstabretagne.engine.EntiteSimulee;
import enstabretagne.engine.InitData;
import enstabretagne.engine.SimuEngine;

public class Missile extends EntiteSimulee implements ILocatable {

    Location position;

    public final MissileInit ini;

    public Missile(SimuEngine engine, InitData ini) {
        super(engine, ini);
        this.ini = (MissileInit) ini;
    }

    @Override
    public Location position() {
        return null;
    }
}


