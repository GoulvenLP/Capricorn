package enstabretagne.applications.capricorn.commandcenter;

import enstabretagne.applications.capricorn.expertise.Location;
import enstabretagne.applications.capricorn.missile.Missile;
import enstabretagne.applications.capricorn.mobile.Mobile;
import enstabretagne.base.logger.Logger;
import enstabretagne.base.time.LogicalDuration;
import enstabretagne.engine.EntiteSimulee;
import enstabretagne.engine.InitData;
import enstabretagne.engine.SimEvent;
import enstabretagne.engine.SimuEngine;
import org.apache.commons.geometry.euclidean.twod.Vector2D;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.*;

public class CommandCenter extends EntiteSimulee implements PropertyChangeListener {
    private enum Sensor { COMMAND_CENTER, MISSILE}; // source of the radar used to detect the target
    private List<Location> targetLastCoordinates; // coordinates used to target a detected engine
    private Sensor radarMode;
    private final Map<Missile, Location> activeMissiles;


    public CommandCenter(SimuEngine engine, InitData init){
        super(engine, init);
        this.activeMissiles = new HashMap<>();
        this.targetLastCoordinates = null;
        this.radarMode = Sensor.COMMAND_CENTER;
    }

    /**
     * Typical action of the command center when the radar detects something: it schedules a missile,
     * or updates its trajectory if a missile got already shot.
     */
    public void action() {
        // Récupérer les cibles actuellement suivies
        Set<Location> assignedTargets = new HashSet<>(activeMissiles.values());

        // Identifier les nouvelles cibles (mobiles non encore assignés)
        List<Location> newTargets = targetLastCoordinates.stream()
                .filter(target -> !assignedTargets.contains(target))
                .toList();

        // Déterminer combien de missiles supplémentaires sont nécessaires
        int activeMissilesCount = activeMissiles.size();
        int targetsCount = targetLastCoordinates.size();
        int missilesManquants = targetsCount - activeMissilesCount;

        Logger.Detail(this, "action", "Missiles actifs : " + activeMissilesCount + ", Cibles : " + targetsCount + ", Missiles à tirer : " + missilesManquants);

        // Tirer seulement le nombre nécessaire de missiles
        for (int i = 0; i < missilesManquants && i < newTargets.size(); i++) {
            scheduleFireMissile(newTargets.get(i));
        }

        // Mise à jour des trajectoires des missiles existants
        updateMissileTrajectory();
    }


    /**
     * Programs a missile in the scheduler for two seconds later,
     * sets the class variable firedMissile to true
     */
    public void scheduleFireMissile(Location target) {
        Optional<Missile> availableMissile = this.engine.recherche(e -> e instanceof Missile)
                .stream()
                .map(e -> (Missile) e)
                .filter(m -> !activeMissiles.containsKey(m)) // Ne prendre que les missiles inactifs
                .min(Comparator.comparingDouble(m -> m.getPosition().position().distance(target.position())));

        if (availableMissile.isEmpty()) {
            Logger.Warning(this, "scheduleFireMissile", "Aucun missile disponible pour suivre la cible.");
            return;
        }

        Missile missile = availableMissile.get();
        Logger.Detail(this, "scheduleFireMissile", "Missile " + missile.getId() + " assigné à la cible.");

        // Planifier le tir après 2 secondes
        SimEvent fireMissile = new SimEvent(engine.Now().add(LogicalDuration.ofSeconds(2))) {
            @Override
            public void process() {
                missile.Fire(target);
                activeMissiles.put(missile, target); // Associer le missile à la cible
            }
        };
        super.Post(fireMissile);
    }


    /**
     * Updates the missile's trajectory if and only if a missile got shot and if the leading radar is the
     * command center's one.
     * Bases the update on the coordinates returned by the listener on the radar
     */
    public void updateMissileTrajectory() {
        Logger.Information(this, "updateMissileTrajectory", "Mise à jour des trajectoires des missiles.");

        activeMissiles.forEach((missile, currentTarget) -> {
            Optional<Location> closestTarget = targetLastCoordinates.stream()
                    .min(Comparator.comparingDouble(t -> t.position().distance(missile.getPosition().position())));

            if (closestTarget.isPresent()) {
                Location newTarget = closestTarget.get();
                missile.updateTarget(newTarget);
                activeMissiles.put(missile, newTarget);
            }
        });


    }


    /**
     * When an event is triggered, extracts the new coordinates that are serialized as Json and
     * @param evt A PropertyChangeEvent object describing the event source
     *          and the property that has changed.
     */
    public void propertyChange(PropertyChangeEvent evt){
        if (evt.getPropertyName().equals("mobile")) {
            if(evt.getNewValue() != null) {
                this.targetLastCoordinates = new ArrayList<>((List<Location>) evt.getNewValue());
                System.out.println(this.targetLastCoordinates);
                this.action();
            }else{
                Location explosionLocation = new Location("safeExplosionPlace", Vector2D.of(0, 0));
                this.targetLastCoordinates = List.of(explosionLocation);
                this.action();
            }

        } else if (evt.getPropertyName().equals("switchingRadar")) {
            this.radarMode = Sensor.MISSILE;
        } else if (evt.getPropertyName().equals("missile_exploded")) {
            Missile explodedMissile = (Missile) evt.getNewValue();
            activeMissiles.remove(explodedMissile);

            if (activeMissiles.isEmpty()) {
                resetCommandCenter();
            }
        }
    }

    /**
     * Resets the command center variables
     */
    public void resetCommandCenter() {
        this.activeMissiles.clear();
        this.targetLastCoordinates = null;
        this.radarMode = Sensor.COMMAND_CENTER;
    }


}
