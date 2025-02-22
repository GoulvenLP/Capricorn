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
import java.util.List;

public class Radar extends EntiteSimulee implements ILocatable {

	public final RadarInit rIni;

	SimEvent RadarEvent;
	private final PropertyChangeSupport pcs;


	private boolean isTargetsDetected;

	public Radar(SimuEngine engine, InitData ini, CommandCenter commandCenter) {
		super(engine, ini);
		this.rIni = (RadarInit) ini;
		this.RadarEvent = new RadarEvent(engine.Now());
		this.pcs = new PropertyChangeSupport(this);
		this.pcs.addPropertyChangeListener(commandCenter); // add listener
		this.isTargetsDetected = false;
	}

	@Override
	public void Init() {
		super.Init();
		Post(this.RadarEvent);
	}


	/**
	 * Alert command center.
	 *
	 * @param l the list of targets location to alert the command center
	 */
	public void alertCommandCenter(List<Location> l) {
		Logger.Information(this, "alertCommandCenter", "Alerting Command Center");
		this.pcs.firePropertyChange(new PropertyChangeEvent(this, "mobile", null, l));
	}


	@Override
	public Location getPosition() {
		return rIni.position;
	}

	public boolean isTargetsDetected() {
		return isTargetsDetected;
	}

	public void setTargetsDetected(boolean targetsDetected) {
		isTargetsDetected = targetsDetected;
	}




}
