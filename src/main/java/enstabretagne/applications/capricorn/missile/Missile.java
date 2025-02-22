package enstabretagne.applications.capricorn.missile;

import enstabretagne.applications.capricorn.commandcenter.CommandCenter;
import enstabretagne.applications.capricorn.expertise.ILocatable;
import enstabretagne.applications.capricorn.expertise.Location;
import enstabretagne.applications.capricorn.mobile.Mobile;
import enstabretagne.base.logger.Logger;
import enstabretagne.engine.EntiteSimulee;
import enstabretagne.engine.InitData;
import enstabretagne.engine.SimEvent;
import enstabretagne.engine.SimuEngine;
import org.apache.commons.geometry.euclidean.twod.Vector2D;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Random;

public class Missile extends EntiteSimulee implements ILocatable {

    Location position;
    private Location target;
    private Location previousCoordinates;
    public final MissileInit ini;
    private final PropertyChangeSupport pcs;
    private final int range;
    private double probaFail;
    private double speed;
    SimEvent Move;
    private boolean embeddedRadarActivated;

    private boolean isActive;


    public Missile(SimuEngine engine, InitData ini, CommandCenter commandCenter) {
        super(engine, ini);
        this.isActive = false;
        this.ini = (MissileInit) ini;
        this.range = 60;
        this.probaFail = 0.1;
        this.speed = 1500;
        this.embeddedRadarActivated = false;
        this.previousCoordinates = null;

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
                checkImpact();
                if (Move != null){
                    Move.rescheduleAt(Now().add(Missile.this.ini.period));
                    Post(Move);
                }
                if (Missile.this.target.equals(Missile.this.previousCoordinates)){
                    destroyMissile();
                } else {
                    Missile.this.previousCoordinates = Missile.this.target;
                }
            }
        };
        Post(this.Move);
    }

    public void updateTarget(Location newTarget) {
        // target reached its objective --> coordinates remain constant
        if (this.target.equals(newTarget)){
            this.destroyMissile();
        }
        this.target = newTarget; // Mise à jour de la cible
        Logger.Detail(this, "updateTarget", "Missile " + this.getId() + " redirected to new coordinates");
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
     * According to the probability of the missile to destroy a target,
     * generates a random number.
     * @return true if the random number generated is superior to the failure treshold
     * else returns false
     */
    private boolean isDestroyingTarget(){
        boolean status = false;
        Random r = new Random();
        int val = r.nextInt(101);
        if (val > this.probaFail * 10){
            status = true;
        }
        return status;
    }

    public void destroyMissile(){
        unPost(this.Move);
        terminate();
        this.Move = null;
    }

    private void checkImpact() {
        // get the Mobile
        this.engine.recherche(e -> {
            if (e instanceof Mobile) {
                Mobile mobile = (Mobile) e;
                if (mobile.getPosition().position().distance(this.position.position()) <= 5) {
                    Logger.Information(this, "checkImpact", "Missile " + this.getId() + " a atteint sa cible !");
                    this.isActive = false;
                    if (isDestroyingTarget()){
                        mobile.explode();
                    }
                    destroyMissile();
                    this.pcs.firePropertyChange("missile_exploded", null, null);

                }
            }
            return false;
        });

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


    /**
     * Getter on the variable that displays if the embedded radar is currently activated or not
     * @return true if the embedded radar is activated, else false
     */
    public boolean isEmbeddedRadarActivated(){
        return this.embeddedRadarActivated;
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


