package enstabretagne.applications.capricorn.missile;

import enstabretagne.base.logger.Logger;
import enstabretagne.base.time.LogicalDateTime;
import enstabretagne.base.time.LogicalDuration;
import enstabretagne.engine.SimEvent;

public class MissileEvent extends SimEvent {

    public MissileEvent(LogicalDateTime d) {
        super(d);
    }

    @Override
    public void process() {
        Logger.Detail(entitePorteuseEvenement, "MissileEvent.Process", "MissileEvent à " + getDateOccurence());

        // TODO : calculate the path of the missile etc etc

        this.rescheduleAt(getDateOccurence().add(LogicalDuration.ofSeconds(1)));
    }
}
