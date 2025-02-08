package enstabretagne.applications.capricorn.radar;

import enstabretagne.applications.capricorn.expertise.Location;
import enstabretagne.base.time.LogicalDuration;
import enstabretagne.engine.InitData;

public class RadarInit extends InitData{
	public final Location position;
	public final double portee;

	public final LogicalDuration period;
	public RadarInit(String name,Location position, double portee, LogicalDuration period) {
		super(name);
		this.position = position;
		this.portee = portee;
		this.period = period;
	}
}
