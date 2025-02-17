package enstabretagne.applications.capricorn.missile;

import enstabretagne.applications.capricorn.commandcenter.CommandCenter;
import enstabretagne.applications.capricorn.expertise.ILocatable;
import enstabretagne.applications.capricorn.expertise.Location;
import enstabretagne.base.logger.Logger;
import enstabretagne.engine.EntiteSimulee;
import enstabretagne.engine.InitData;
import enstabretagne.engine.SimEvent;
import enstabretagne.engine.SimuEngine;
import org.apache.commons.geometry.euclidean.twod.Vector2D;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class Missile extends EntiteSimulee implements ILocatable {

    Location position;

    public final MissileInit ini;
    private PropertyChangeSupport pcs;
    private int range;
    private double probaFail;
    private double speed;
    SimEvent Move;
    private boolean embeddedRadarActivated;

    public Missile(SimuEngine engine, InitData ini, CommandCenter commandCenter) {
        super(engine, ini);
        this.ini = (MissileInit) ini;
        this.range = 10;
        this.pcs = new PropertyChangeSupport(this);
        this.pcs.addPropertyChangeListener(commandCenter);
        this.probaFail = 0.1;
        this.speed = 1500;
        this.embeddedRadarActivated = false;
    }

    @Override
    public void Init() {
        super.Init();
        position=ini.position;
    }

    public void Fire(Location target) {
        Move = new SimEvent(engine.Now()) {
            @Override
            public void process() {
                move(target);
                Move.rescheduleAt(Now().add(Missile.this.ini.period));
                Post(Move);
            }
        };
        Post(Move);
    }

    private void move(Location target) {
        // Calcul du vecteur direction vers la cible
        if (!this.embeddedRadarActivated){
            this.embeddedRadarActivated = isEmbeddedRadarRelaying(target);
            if (this.embeddedRadarActivated){
                Logger.Information(this, "move", "Alerting Command Center for switching radar mode");
                this.pcs.firePropertyChange("switchingRadar", null, null);
                // todo: timing goes to ms here
            }
        }
        Logger.Information(this, "move", "new coordinates are : " + target); //todo delete
        Vector2D direction = target.position().subtract(position.position()).normalize().multiply(this.speed/150); // 10 = vitesse du missile
        // todo: adapt the move to the timer? (n seconds, ms...)

        // Mise à jour de la position du missile
        position = position.add(direction);
    }

    public Integer getId() {
        return ini.id;
    }

    @Override
    public Location position() {
        return position;
    }

    /**
     * Verifies if the submitted target is close enough so that the missile's radar
     * takes control over the missile.
     * @param target: coordinates
     * @return true if the target is at range for the missile's radar, else returns false
     */
    public boolean isEmbeddedRadarRelaying(Location target){
        Logger.Information(this, "checking", target.position().distance(position.position()) + ""); //todo adjust
        return this.range >= target.position().distance(position.position());
    }



}


