package enstabretagne.applications.capricorn.radar;

import enstabretagne.applications.capricorn.commandcenter.CommandCenter;
import enstabretagne.applications.capricorn.expertise.ILocatable;
import enstabretagne.applications.capricorn.expertise.Location;
import enstabretagne.base.logger.Logger;
import enstabretagne.engine.EntiteSimulee;
import enstabretagne.engine.InitData;
import enstabretagne.engine.SimEvent;
import enstabretagne.engine.SimuEngine;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeSupport;

public class Radar extends EntiteSimulee implements ILocatable {

	public final RadarInit rIni;

	SimEvent RadarEvent;
	private PropertyChangeSupport pcs;

	public Radar(SimuEngine engine, InitData ini, CommandCenter commandCenter) {
		super(engine, ini);
		this.rIni = (RadarInit) ini;

		this.RadarEvent = new RadarEvent(engine.Now());
		this.pcs = new PropertyChangeSupport(this);
		this.pcs.addPropertyChangeListener(commandCenter); // add listener
	}

	@Override
	public void Init() {
		super.Init();
		Post(this.RadarEvent);
	}

	public void alertCommandCenter(Location l) {
		Logger.Information(this, "alertCommandCenter", "Alerting Command Center");
		this.pcs.firePropertyChange(new PropertyChangeEvent(this, "mobile", null, l));
	}

	@Override
	public Location getPosition() {
		return rIni.position;
	}



}
