package enstabretagne.applications.capricorn.mobile;

import enstabretagne.applications.capricorn.expertise.Location;
import enstabretagne.base.time.LogicalDuration;
import enstabretagne.engine.InitData;

public class MobileInit extends InitData{
	
	public final Location position;
	public final LogicalDuration period;

	public final Location direction;
	
	public MobileInit(String name,Location position,LogicalDuration period, Location direction) {
		super(name);
		this.position = position;
		this.period = period;
		this.direction = direction;
	}

}
