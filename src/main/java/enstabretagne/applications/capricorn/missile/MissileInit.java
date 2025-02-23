package enstabretagne.applications.capricorn.missile;

import enstabretagne.applications.capricorn.expertise.Location;
import enstabretagne.base.time.LogicalDuration;
import enstabretagne.engine.InitData;

public class MissileInit extends InitData {

    final LogicalDuration period;
    Location position;
    final Integer id;
    public final LogicalDuration reloadTime;

    public MissileInit(String name, Location position, LogicalDuration period, Integer id, LogicalDuration reloadTime) {
        super(name);
        this.position = position;
        this.period = period;
        this.id = id;
        this.reloadTime = reloadTime;
    }
}
