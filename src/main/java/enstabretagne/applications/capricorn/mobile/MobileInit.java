package enstabretagne.applications.capricorn.mobile;

import enstabretagne.applications.capricorn.expertise.Location;
import enstabretagne.base.time.LogicalDuration;
import enstabretagne.engine.InitData;

public class MobileInit extends InitData{
	
	public final Location position;
	public final LogicalDuration period;

	public final Location direction;

	public final double scaleX;
	public final double scaleY;
	
	public MobileInit(String name,Location position,LogicalDuration period, Location direction, double scaleX, double scaleY) {
		super(name);
		this.position = position;
		this.period = period;
		this.direction = direction;
		this.scaleX = scaleX;
		this.scaleY = scaleY;
	}

}
