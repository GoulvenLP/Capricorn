package enstabretagne.applications.capricorn.missile;

import enstabretagne.applications.capricorn.commandcenter.CommandCenter;
import enstabretagne.applications.capricorn.expertise.ILocatable;
import enstabretagne.applications.capricorn.expertise.Location;
import enstabretagne.applications.capricorn.mobile.Mobile;
import enstabretagne.base.logger.Logger;
import enstabretagne.base.time.LogicalDuration;
import enstabretagne.engine.EntiteSimulee;
import enstabretagne.engine.InitData;
import enstabretagne.engine.SimEvent;
import enstabretagne.engine.SimuEngine;
import org.apache.commons.geometry.euclidean.twod.Vector2D;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Random;

import static enstabretagne.applications.capricorn.mobile.Mobile.SPEED_FACTOR;

public class Missile extends EntiteSimulee implements ILocatable {

    Location position;
    private Location target;
    public final MissileInit ini;
    private final PropertyChangeSupport pcs;
    private final int range;
    private double probaFail;
    private double speed;
    SimEvent Move;
    private boolean embeddedRadarActivated;

    private boolean isActive;

    private final CommandCenter commandCenter;


    public Missile(SimuEngine engine, InitData ini, CommandCenter commandCenter) {
        super(engine, ini);
        this.isActive = false;
        this.ini = (MissileInit) ini;
        this.range = 60;
        this.probaFail = 0.1;
        this.speed = 1500;
        this.embeddedRadarActivated = false;
        this.commandCenter = commandCenter;
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
                    if(!isEmbeddedRadarActivated())
                        Move.rescheduleAt(Now().add(Missile.this.ini.period));
                    else
                        Move.rescheduleAt(Now().add(LogicalDuration.ofMillis(10)));
                    Post(Move);
                }
            }
        };
        Post(this.Move);
    }

    public void updateTarget(Location newTarget) {
        this.target = newTarget; // Mise à jour de la cible
        Logger.Information(this, "updateTarget", "Missile " + this.getId() + " redirected to new coordinates");
    }

    private void move(Location target) {
        // Vérification et activation du radar embarqué si nécessaire
        double adaptedSpeed;
        if (!this.embeddedRadarActivated){
            adaptedSpeed = this.speed / 3600;
        } else {
            adaptedSpeed = this.speed / (3600 * 100);
        }
        if (!this.embeddedRadarActivated && isEmbeddedRadarRelaying(target)) {
            this.embeddedRadarActivated = true;
            Logger.Information(this, "move", "Alerting Command Center for switching radar mode");
            this.pcs.firePropertyChange("switchingRadar", null, null);
        }

        // Calcul du vecteur direction
        Vector2D currentPos = this.position.position();
        Vector2D targetPos = target.position();
        Vector2D direction = targetPos.subtract(currentPos)
                .normalize()
                .multiply(adaptedSpeed*SPEED_FACTOR);    // todo right scale?

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
            Logger.Detail(this, "explode", "mobile_failure, 1");
            status = true;
        }
        Logger.Information(this, "isDestroyingTarget", "Missile " + this.getId() + " will explode target : " + status );
        return status;
    }

    public void destroyMissile(){
        if(this.Move.Posted()){
            // event pour recharger le missile après destruction
            this.commandCenter.reloadMissile(this);
            unPost(this.Move);
            terminate();
        }
    }

    private void checkImpact() {
        // get the Mobile
        this.engine.recherche(e -> {
            if (e instanceof Mobile mobile) {
                if (mobile.getPosition().position().distance(this.position.position()) < 5) { // plane's radius is 10m
                    Logger.Information(this, "checkImpact", "Missile " + this.getId() + " a atteint sa cible !");
                    this.isActive = false;
                    if (isDestroyingTarget()) {
                        mobile.explode();
                        double distanceFromFactory = mobile.getPosition().position().distance(mobile.getTargetCoordinates().position());
                        Logger.Detail(this, "checkImpact", "Interception_mobile, 1");
                        Logger.Detail(this, "checkImpact", "distance, " + distanceFromFactory);
                        this.pcs.firePropertyChange("interception", null, true);
                        this.pcs.firePropertyChange("distance", null, distanceFromFactory);
                    }else{
                        Logger.Detail(this, "checkImpact", "missile_failure, 1");
                        this.pcs.firePropertyChange("missile_failed", mobile, this);
                    }
                    destroyMissile();
                    this.pcs.firePropertyChange("missile_exploded", mobile, this);
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





}


