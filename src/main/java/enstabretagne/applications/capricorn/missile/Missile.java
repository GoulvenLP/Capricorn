package enstabretagne.applications.capricorn.missile;

import enstabretagne.applications.capricorn.expertise.ILocatable;
import enstabretagne.applications.capricorn.expertise.Location;
import enstabretagne.applications.capricorn.mobile.MobileInit;
import enstabretagne.engine.EntiteSimulee;
import enstabretagne.engine.InitData;
import enstabretagne.engine.SimEvent;
import enstabretagne.engine.SimuEngine;
import org.apache.commons.geometry.euclidean.twod.Vector2D;

public class Missile extends EntiteSimulee implements ILocatable {

    Location position;

    public final MissileInit ini;

    SimEvent Move;

    public Missile(SimuEngine engine, InitData ini) {
        super(engine, ini);
        this.ini = (MissileInit) ini;
    }

    @Override
    public void Init() {
        super.Init();
        position=ini.position;
    }

    public void Fire() {
        Move = new SimEvent(engine.Now()) {
            @Override
            public void process() {
                move();
                Move.rescheduleAt(Now().add(Missile.this.ini.period));
                Post(Move);
            }
        };
        Post(Move);
    }

    private void move(){
        position=position.add(Vector2D.of(-10,0));
    }

    public Integer getId() {
        return ini.id;
    }

    @Override
    public Location position() {
        return position;
    }
}


