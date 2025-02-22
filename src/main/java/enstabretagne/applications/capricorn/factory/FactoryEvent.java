package enstabretagne.applications.capricorn.factory;

import enstabretagne.applications.capricorn.expertise.Location;
import enstabretagne.applications.capricorn.mobile.Mobile;
import enstabretagne.applications.capricorn.radar.Radar;
import enstabretagne.base.logger.Logger;
import enstabretagne.base.time.LogicalDateTime;
import enstabretagne.base.time.LogicalDuration;
import enstabretagne.engine.SimEvent;

import java.util.List;

public class FactoryEvent extends SimEvent {

    public FactoryEvent(LogicalDateTime d) {
        super(d);
    }

    private Boolean isMobileOnFactory(Mobile m) {
        FactoryInit factoryInit = (FactoryInit) entitePorteuseEvenement.getInit();
        return m.getPosition().position().distance(factoryInit.position.position()) == 0;
    }

    @Override
    public void process() {
        Logger.Detail(entitePorteuseEvenement, "FactoryEvent.Process", "FactoryEvent à " + getDateOccurence());
        // predicat pour vérifier qu'un mobile est à un certaine position du radar
        boolean mobileOnFactory = entitePorteuseEvenement.recherche(e -> e instanceof Mobile &&
                isMobileOnFactory((Mobile) e) && ((Mobile) e).isObjectiveReached())
                .stream()
                .findFirst()
                .isPresent();
        if(mobileOnFactory){
            Logger.Information(entitePorteuseEvenement, "FactoryEvent.Process", "Usine touchée par un mobile");
            ((Factory) entitePorteuseEvenement).explode();
        }

        this.rescheduleAt(getDateOccurence().add(LogicalDuration.ofSeconds(1)));
        entitePorteuseEvenement.Post(this);
    }
}
