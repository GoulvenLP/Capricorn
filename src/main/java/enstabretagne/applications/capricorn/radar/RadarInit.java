package enstabretagne.applications.capricorn.radar;

import enstabretagne.applications.capricorn.expertise.Location;
import enstabretagne.engine.InitData;

public class RadarInit extends InitData{
	public final Location position;
	public final double portee;
	
	public RadarInit(String name,Location position, double portee) {
		super(name);
		this.position = position;
		this.portee = portee;
	}
}
