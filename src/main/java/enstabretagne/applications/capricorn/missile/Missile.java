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
    private final PropertyChangeSupport pcs;
    private final int range;
    private double probaFail;
    private double speed;
    SimEvent Move;
    private boolean embeddedRadarActivated;

    private boolean isActive;

    private Location target;

    public Missile(SimuEngine engine, InitData ini, CommandCenter commandCenter) {
        super(engine, ini);
        this.isActive = false;
        this.ini = (MissileInit) ini;
        this.range = 20;
        this.probaFail = 0.1;
        this.speed = 1500;
        this.embeddedRadarActivated = false;

        this.pcs = new PropertyChangeSupport(this);
        this.pcs.addPropertyChangeListener(commandCenter);
    }

    @Override
    public void Init() {
        super.Init();
        position=ini.position;
    }

    public void Fire(Location target) {
        this.target = target;
        this.isActive = true;
        this.Move = new SimEvent(engine.Now()) {
            @Override
            public void process() {
                move(Missile.this.target); // très important, on utilise les coords de la cible mises à jour
                Move.rescheduleAt(Now().add(Missile.this.ini.period));
                Post(Move);
            }
        };
        Post(this.Move);
    }

    public void updateTarget(Location newTarget) {
        this.target = newTarget; // Mise à jour de la cible
        Logger.Detail(this, "updateTarget", "Missile " + this.getId() + " redirected to new target");
    }

    private void move(Location target) {
        // Vérification et activation du radar embarqué si nécessaire
        if (!this.embeddedRadarActivated && isEmbeddedRadarRelaying(target)) {
            this.embeddedRadarActivated = true;
            Logger.Information(this, "move", "Alerting Command Center for switching radar mode");
            this.pcs.firePropertyChange("switchingRadar", null, null);
            // TODO: timing goes to ms here
            this.speed = 2000;
        }

        // Calcul du vecteur direction
        Vector2D currentPos = this.position.position();
        Vector2D targetPos = target.position();
        Vector2D direction = targetPos.subtract(currentPos)
                .normalize()
                .multiply(this.speed / 150);

        // Mise à jour de la position du missile
        this.position = this.position.add(direction);
    }


    /**
     * Verifies if the submitted target is close enough so that the missile's radar
     * takes control over the missile.
     * @param target: coordinates
     * @return true if the target is at range for the missile's radar, else returns false
     */
    public boolean isEmbeddedRadarRelaying(Location target){
        Logger.Information(this, "isEmbeddedRadarRelaying", "Checking if target is in radar range");
        return this.range >= target.position().distance(position.position());
    }

    public Integer getId() {
        return ini.id;
    }

    @Override
    public Location getPosition() {
        return position;
    }

    public boolean isActive() {
        return isActive;
    }



}


