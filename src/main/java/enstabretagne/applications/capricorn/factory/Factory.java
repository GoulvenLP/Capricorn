package enstabretagne.applications.capricorn.factory;

import enstabretagne.applications.capricorn.expertise.ILocatable;
import enstabretagne.applications.capricorn.expertise.Location;
import enstabretagne.engine.EntiteSimulee;
import enstabretagne.engine.InitData;
import enstabretagne.engine.SimuEngine;

public class Factory extends EntiteSimulee implements ILocatable {

    Location position;

    public final FactoryInit ini;
    public Factory(SimuEngine engine, InitData ini) {
        super(engine, ini);
        this.ini = (FactoryInit) ini;
    }

    @Override
    public void Init() {
        super.Init();
        this.position=ini.position;
    }
    @Override
    public Location getPosition() {
        return this.position;
    }
}

