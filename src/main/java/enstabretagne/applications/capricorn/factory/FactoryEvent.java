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

    private Boolean isMobileCrashed(Mobile m) {
        FactoryInit factoryInit = (FactoryInit) entitePorteuseEvenement.getInit();
        return m.isObjectiveReached();
    }

    @Override
    public void process() {
        Logger.Detail(entitePorteuseEvenement, "FactoryEvent.Process", "FactoryEvent à " + getDateOccurence());
        // predicat pour vérifier qu'un mobile a atteint l'usine et qu'il n'a pas manqué sa cible
        boolean isFactoryImpacted = entitePorteuseEvenement.recherche(e -> e instanceof Mobile &&
                isMobileOnFactory((Mobile) e) && isMobileCrashed((Mobile) e))
                .stream()
                .findFirst()
                .isPresent();

        if(isFactoryImpacted){
            Logger.Information(entitePorteuseEvenement, "FactoryEvent.Process", "Usine touchée par un mobile");
            ((Factory) entitePorteuseEvenement).explode();
        }

        this.rescheduleAt(getDateOccurence().add(LogicalDuration.ofSeconds(1)));
        entitePorteuseEvenement.Post(this);
    }
}
