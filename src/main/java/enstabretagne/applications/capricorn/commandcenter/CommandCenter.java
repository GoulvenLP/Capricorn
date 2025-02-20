package enstabretagne.applications.capricorn.commandcenter;

import enstabretagne.applications.capricorn.expertise.Location;
import enstabretagne.applications.capricorn.missile.Missile;
import enstabretagne.base.logger.Logger;
import enstabretagne.base.time.LogicalDuration;
import enstabretagne.engine.EntiteSimulee;
import enstabretagne.engine.InitData;
import enstabretagne.engine.SimEvent;
import enstabretagne.engine.SimuEngine;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Comparator;
import java.util.Optional;

public class CommandCenter extends EntiteSimulee implements PropertyChangeListener {
    private enum Sensor { COMMAND_CENTER, MISSILE}; // source of the radar used to detect the target
    private Location targetLastCoordinates; // coordinates used to target a detected engine
    private boolean firedMissile; // true if a missile got already shot
    private Sensor radarMode;

    public CommandCenter(SimuEngine engine, InitData init){
        super(engine, init);
        this.firedMissile = false;
        this.targetLastCoordinates = null;
        this.radarMode = Sensor.COMMAND_CENTER;
    }

    /**
     * Typical action of the command center when the radar detects something: it schedules a missile,
     * or updates its trajectory if a missile got already shot.
     */
    public void action(){
        Logger.Information(this, "action", "Command Center action");
        if (!this.firedMissile){
            Logger.Information(this, "action", "Missile fired");
            scheduleFireMissile();
        } else {
            updateMissileTrajectory();
        }
    }


    /**
     * Programs a missile in the scheduler for two seconds later,
     * sets the class variable firedMissile to true
     */
    public void scheduleFireMissile() {
        this.firedMissile = true;

        // Trouver la tourelle la plus proche
        Optional<Missile> closestTurret = this.engine.recherche(e -> e instanceof Missile)
                .stream()
                .map(e -> (Missile) e)
                .min(Comparator
                        .comparingDouble(m -> ((Missile)m).getPosition().position().distance(this.targetLastCoordinates.position())) // Tri par distance
                        .thenComparingInt(m -> ((Missile)m).getId()).reversed()); // ID le plus élevé en cas d'égalité

        // Si une tourelle est trouvée, planifier le tir
        closestTurret.ifPresent(turret -> {
            Logger.Detail(this, "scheduleFireMissile", "Tourelle la plus proche : " + turret.getId());

            // Planifier le tir après 2 secondes
            SimEvent fireMissile = new SimEvent(engine.Now().add(LogicalDuration.ofSeconds(2))) {
                @Override
                public void process() {
                    turret.Fire(targetLastCoordinates);
                }
            };
            super.Post(fireMissile);
        });
    }



    /**
     * Updates the missile's trajectory if and only if a missile got shot and if the leading radar is the
     * command center's one.
     * Bases the update on the coordinates returned by the listener on the radar
     */
    public void updateMissileTrajectory(){
        Optional<Missile> missile = this.engine.recherche(e -> e instanceof Missile)
                .stream()
                .map(e -> (Missile) e)
                .filter(Missile::isActive) // S'assurer que le missile est en vol
                .findFirst(); // Supposons qu'on ne gère qu'un missile à la fois

        missile.ifPresent(m -> {
            Logger.Information(this, "updateMissileTarget", "Updating trajectory for missile " + m.getId());
            m.updateTarget(targetLastCoordinates);
        });

    }


    /**
     * When an event is triggered, extracts the new coordinates that are serialized as Json and
     * @param evt A PropertyChangeEvent object describing the event source
     *          and the property that has changed.
     */
    public void propertyChange(PropertyChangeEvent evt){
        if (evt.getPropertyName().equals("mobile")) {
            this.targetLastCoordinates = (Location) evt.getNewValue();
            this.action();
        } else if (evt.getPropertyName().equals("switchingRadar")) {
            this.radarMode = Sensor.MISSILE;
        }
    }



}
