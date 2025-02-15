package enstabretagne.applications.capricorn.commandcenter;

import com.fasterxml.jackson.databind.ObjectMapper;
import enstabretagne.applications.capricorn.expertise.Location;
import enstabretagne.applications.capricorn.missile.Missile;
import enstabretagne.applications.capricorn.mobile.Mobile;
import enstabretagne.applications.capricorn.radar.Radar;
import enstabretagne.applications.capricorn.scenario.ScenarioSimple;
import enstabretagne.engine.SimEvent;
import enstabretagne.engine.SimuEngine;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

public class CommandCenter implements PropertyChangeListener {
    private enum Sensor { COMMAND_CENTER, MISSILE}; // source of the radar used to detect the target

    private Radar radar;
    private Missile missile; // give the possibility to extend this capability by creating a list instead?
    private List<Mobile> targets; // cesnas come by one in the simulation, this design gives the possibility to obtain more
    private boolean firedMissile;
    private Sensor leadingRadar; //todo: eventually group firedMissile & leadingRadar into a mutual object?
    private ScenarioSimple scenario;
    private Location targetLastCoordinates; // coordinates used to target a detected engine


    public CommandCenter(SimuEngine engine, Radar radar){ //todo: what are the parameters taken into account here?
        this.radar = radar; // todo bind the radar to the command center ?
        this.firedMissile = false;
        this.leadingRadar = Sensor.COMMAND_CENTER;
        this.targetLastCoordinates = null;

    }

    /**
     * Typical action of the command center when the radar detects something: it schedules a missile,
     * or updates its trajectory if a missile got already shot.
     */
    public void action(){
        if (!this.firedMissile){
            scheduleFireMissile();
        } else {
            updateMissileTrajectory();
        }
    }


    /**
     * Programs a missile in the scheduler for two seconds later,
     * sets the class variable firedMissile to true
     */
    public void scheduleFireMissile(){
        // todo : add a missile at t + 2seconds in the scheduler in direction
        // todo: + decide which turret shoots the missile --> the closest one each time & if equi-distance, select the highest ID ?

        this.firedMissile = true;
    }


    /**
     * Updates the missile trajectory if and only if a missile got shot and if the leading radar is the
     * command center's one.
     * Bases the update on the coordinates returned by the listener on the radar
     */
    public void updateMissileTrajectory(){
        if (!this.firedMissile || this.leadingRadar == Sensor.MISSILE) return;
        // todo: implement

    }


    /**
     * When an event is triggered, extracts the new coordinates that are serialized as Json and
     * @param evt A PropertyChangeEvent object describing the event source
     *          and the property that has changed.
     */
    public void propertyChange(PropertyChangeEvent evt){
        JsonObject coordinates = (JsonObject) evt.getNewValue();
        ObjectMapper objectMapper = new ObjectMapper();
        this.targetLastCoordinates =  objectMapper.readValue(evt.getNewValue(), Location.class); // todo fix this
        this.action();
    }




}
