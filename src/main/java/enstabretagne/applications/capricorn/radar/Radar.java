package enstabretagne.applications.capricorn.radar;

import enstabretagne.applications.capricorn.commandcenter.CommandCenter;
import enstabretagne.applications.capricorn.expertise.ILocatable;
import enstabretagne.applications.capricorn.expertise.Location;
import enstabretagne.applications.capricorn.mobile.Mobile;
import enstabretagne.base.logger.Logger;
import enstabretagne.engine.EntiteSimulee;
import enstabretagne.engine.InitData;
import enstabretagne.engine.SimEvent;
import enstabretagne.engine.SimuEngine;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

public class Radar extends EntiteSimulee implements ILocatable {

	public final RadarInit rIni;

	SimEvent RadarEvent;
	private final PropertyChangeSupport pcs;
	private List<RegisteredMobiles> registeredMobiles;


	private boolean isTargetsDetected;

	public Radar(SimuEngine engine, InitData ini, CommandCenter commandCenter) {
		super(engine, ini);
		this.rIni = (RadarInit) ini;
		this.RadarEvent = new RadarEvent(engine.Now());
		this.pcs = new PropertyChangeSupport(this);
		this.pcs.addPropertyChangeListener(commandCenter); // add listener
		this.isTargetsDetected = false;
		this.registeredMobiles = new ArrayList<>(); // stores the last 10 coordinates of any detected object

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
	public void alertCommandCenter(List<Mobile> l) {
		Logger.Information(this, "alertCommandCenter", "Alerting Command Center");
		this.pcs.firePropertyChange(new PropertyChangeEvent(this, "mobile", null, l));
	}


	/**
	 * Verifies if a found mobile is already registered into the RegisteredMobiles list
	 * @param m: the mobile to check
	 * @return true if the mobile is found, else returns false
	 */
	protected boolean isRegisteredMobile(Mobile m){
		for (RegisteredMobiles r : this.registeredMobiles){
			if (m.getName().equals(r.getName())){
				return true;
			}
		}
		return false;
	}

	/**
	 * Adds a specific Location to a mobile. If the mobile is not already registered, an object is created
	 * for the storage and the location is added to the queue. If the mobile is already registered, the location
	 * is simply added to the existing queue of the concerned object.
	 * @param m mobile to register
	 */
	protected void add(Mobile m){
		// mobile not registered --> create it and add to the queue
		if (!this.isRegisteredMobile(m)){
			RegisteredMobiles r = new RegisteredMobiles(m.getName());
			r.add(m.getPosition());
		} else {
			// mobile already registered --> find it and add to the queue
			for (RegisteredMobiles rm : this.registeredMobiles){
				if (rm.getName().equals(m.getName())){
					rm.add(m.getPosition());
					break;
				}
			}
		}
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
