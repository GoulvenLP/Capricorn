package enstabretagne.applications.capricorn.factory;

import enstabretagne.applications.capricorn.commandcenter.CommandCenter;
import enstabretagne.applications.capricorn.expertise.ILocatable;
import enstabretagne.applications.capricorn.expertise.Location;
import enstabretagne.applications.capricorn.mobile.Mobile;
import enstabretagne.base.logger.Logger;
import enstabretagne.engine.EntiteSimulee;
import enstabretagne.engine.InitData;
import enstabretagne.engine.SimEvent;
import enstabretagne.engine.SimuEngine;

import java.beans.PropertyChangeSupport;

public class Factory extends EntiteSimulee implements ILocatable {

    Location position;

    SimEvent FactoryEvent;
    private final PropertyChangeSupport pcs;
    private boolean isExploded;

    public final FactoryInit ini;

    public Factory(SimuEngine engine, InitData ini, CommandCenter commandCenter) {
        super(engine, ini);
        this.ini = (FactoryInit) ini;
        this.FactoryEvent = new FactoryEvent(engine.Now());
        this.isExploded = false;
        this.pcs = new PropertyChangeSupport(this);
        this.pcs.addPropertyChangeListener(commandCenter); // add listener
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

    protected void alertCommandCenter(Mobile m) {
        Logger.Information(this, "alertCommandCenter", "Factory hit ! Alerting Command Center");
        this.pcs.firePropertyChange("factory", null, m);
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

