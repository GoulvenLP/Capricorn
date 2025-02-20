package enstabretagne.applications.capricorn.factory;

import enstabretagne.engine.InitData;
import enstabretagne.applications.capricorn.expertise.Location;

public class FactoryInit extends InitData {

    public final Location position;
    public FactoryInit(String name, Location position) {
        super(name);
        this.position = position;
    }


}
