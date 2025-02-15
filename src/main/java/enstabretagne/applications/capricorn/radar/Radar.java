package enstabretagne.applications.capricorn.radar;

import enstabretagne.applications.capricorn.commandcenter.CommandCenter;
import enstabretagne.applications.capricorn.expertise.ILocatable;
import enstabretagne.applications.capricorn.expertise.Location;
import enstabretagne.base.logger.Logger;
import enstabretagne.base.time.LogicalDateTime;
import enstabretagne.engine.EntiteSimulee;
import enstabretagne.engine.InitData;
import enstabretagne.engine.SimEvent;
import enstabretagne.engine.SimuEngine;

import java.beans.PropertyChangeSupport;

public class Radar extends EntiteSimulee implements ILocatable {

	public final RadarInit rIni;

	SimEvent RadarEvent;
	private PropertyChangeSupport pcs;
	private CommandCenter commandCenter;

	public Radar(SimuEngine engine, InitData ini) {
		super(engine, ini);
		this.rIni = (RadarInit) ini;
		this.commandCenter = null;

		this.RadarEvent = new RadarEvent(engine.Now());
		this.commandCenter = commandCenter;
		this.pcs = new PropertyChangeSupport(this);
		this.pcs.addPropertyChangeListener(commandCenter); // add listener
	}

	@Override
	public void Init() {

		super.Init();
		Post(this.RadarEvent);
		this.pcs.firePropertyChange(this.RadarEvent); // todo : get the coordinates, convert them to JSON and trigger the event. 2 kind of events: update coord OR say that the leading radar has switched to the missile's one
	}

	@Override
	public Location position() {
		return rIni.position;
	}

	public void addCommandCenter(CommandCenter commandCenter){
		this.commandCenter = commandCenter;
	}


}
