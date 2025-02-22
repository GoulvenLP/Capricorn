package enstabretagne.applications.capricorn.factory;

import enstabretagne.applications.capricorn.expertise.ILocatable;
import enstabretagne.applications.capricorn.expertise.Location;
import enstabretagne.base.logger.Logger;
import enstabretagne.engine.EntiteSimulee;
import enstabretagne.engine.InitData;
import enstabretagne.engine.SimEvent;
import enstabretagne.engine.SimuEngine;

public class Factory extends EntiteSimulee implements ILocatable {

    Location position;

    SimEvent FactoryEvent;

    private boolean isExploded;

    public final FactoryInit ini;
    public Factory(SimuEngine engine, InitData ini) {
        super(engine, ini);
        this.ini = (FactoryInit) ini;
        this.FactoryEvent = new FactoryEvent(engine.Now());
        this.isExploded = false;
    }

    @Override
    public void Init() {
        super.Init();
        this.position=ini.position;
        Post(this.FactoryEvent);
    }

    public void explode() {
        // l'usine explose mais peut toujours être frappée par d'autres missiles, on va seulement représenter
        // l'usine en rouge
        Logger.Information(this, "explode", "Explosion de l'usine :");
        this.setExploded(true);
    }

    @Override
    public Location getPosition() {
        return this.position;
    }

    public boolean isExploded() {
        return isExploded;
    }

    public void setExploded(boolean exploded) {
        isExploded = exploded;
    }
}

