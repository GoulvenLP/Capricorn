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

    private List<Mobile> targetLastMobiles;

    private Sensor radarMode;
    private final Map<Missile, Mobile> activeMissiles;

    public CommandCenter(SimuEngine engine, InitData init){
        super(engine, init);
        this.activeMissiles = new HashMap<>();
        this.targetLastMobiles = new ArrayList<>();
        this.radarMode = Sensor.COMMAND_CENTER;
    }

    /**
     * Typical action of the command center when the radar detects something: it schedules a missile,
     * or updates its trajectory if a missile got already shot.
     */
    public void action() {
        // Récupérer les mobiles actuellement suivis
        Set<Mobile> assignedMobiles = new HashSet<>(activeMissiles.values());

        // Identifier les nouveaux mobiles (ceux non encore assignés)
        List<Mobile> newTargets = targetLastMobiles.stream()
                .filter(mobile -> !assignedMobiles.contains(mobile))
                .toList();

        // Déterminer combien de missiles supplémentaires sont nécessaires
        int activeMissilesCount = activeMissiles.size();
        int targetsCount = targetLastMobiles.size();
        int missilesManquants = targetsCount - activeMissilesCount;

        Logger.Detail(this, "action", "Missiles actifs : " + activeMissilesCount + ", Mobiles détectés : " + targetsCount + ", Missiles à tirer : " + missilesManquants);

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
    public void scheduleFireMissile(Mobile targetMobile) {
        Optional<Missile> availableMissile = this.engine.recherche(e -> e instanceof Missile)
                .stream()
                .map(e -> (Missile) e)
                .filter(m -> !activeMissiles.containsKey(m)) // Ne prendre que les missiles inactifs
                .min(Comparator.comparingDouble(m -> m.getPosition().position().distance(targetMobile.getPosition().position())));

        if (availableMissile.isEmpty()) {
            Logger.Warning(this, "scheduleFireMissile", "Aucun missile disponible pour suivre la cible.");
            return;
        }

        Missile missile = availableMissile.get();
        Logger.Detail(this, "scheduleFireMissile", "Missile " + missile.getId() + " assigné au mobile " + targetMobile);

        SimEvent fireMissile = new SimEvent(engine.Now().add(LogicalDuration.ofSeconds(2))) {
            @Override
            public void process() {
                missile.Fire(targetMobile.getPosition());
                activeMissiles.put(missile, targetMobile); // Associer le missile au mobile
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
        activeMissiles.forEach((missile, mobile) -> {
            missile.updateTarget(mobile.getPosition()); // On récupère directement la position actuelle du mobile
        });
    }


    /**
     * When an event is triggered, extracts the new coordinates that are serialized as Json and
     * @param evt A PropertyChangeEvent object describing the event source
     *          and the property that has changed.
     */
    public void propertyChange(PropertyChangeEvent evt){
        if (evt.getPropertyName().equals("mobile")) {
            this.targetLastMobiles = new ArrayList<>((List<Mobile>) evt.getNewValue());
            System.out.println(this.targetLastMobiles);
            this.action();
        } else if (evt.getPropertyName().equals("factory")) {
            Logger.Information(this, "propertyChange", "Alerte reçue : Usine touchée par un mobile.");
            Mobile mobile = (Mobile) evt.getNewValue();

            // supprimer le missile associé au mobile s'il existe
            Missile missile = activeMissiles.entrySet().stream()
                    .filter(entry -> entry.getValue().equals(mobile))
                    .map(Map.Entry::getKey)
                    .findFirst()
                    .orElse(null);
            if (missile != null) {
                missile.destroyMissile();
                activeMissiles.remove(missile);
            }
            // supprimer l'association missile-mobile ? (à voir)
            activeMissiles.entrySet().removeIf(entry -> entry.getValue().equals(mobile));
        }
        else if (evt.getPropertyName().equals("switchingRadar")) {
            this.radarMode = Sensor.MISSILE;
        } else if (evt.getPropertyName().equals("missile_exploded")) {
            Missile explodedMissile = (Missile) evt.getNewValue();
            Mobile explodedMobile = (Mobile) evt.getOldValue();
            activeMissiles.remove(explodedMissile);
            // todo ?
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
        this.targetLastMobiles.clear();
        this.radarMode = Sensor.COMMAND_CENTER;
    }


}
