package enstabretagne.applications.capricorn.missile;

import enstabretagne.applications.capricorn.expertise.Location;
import enstabretagne.base.time.LogicalDuration;
import enstabretagne.engine.InitData;

public class MissileInit extends InitData {

    private final LogicalDuration period;
    private Location position;


    public MissileInit(String name, Location position, LogicalDuration period) {
        super(name);
        this.position = position;
        this.period = period;
    }
}
