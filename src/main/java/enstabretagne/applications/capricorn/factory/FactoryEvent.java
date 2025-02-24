package enstabretagne.applications.capricorn.factory;

import enstabretagne.applications.capricorn.expertise.Location;
import enstabretagne.applications.capricorn.mobile.Mobile;
import enstabretagne.base.logger.Logger;
import enstabretagne.base.time.LogicalDateTime;
import enstabretagne.base.time.LogicalDuration;
import enstabretagne.engine.EntiteSimulee;
import enstabretagne.engine.SimEvent;

import java.util.Optional;


public class FactoryEvent extends SimEvent {

    public FactoryEvent(LogicalDateTime d) {
        super(d);
    }

    private Boolean isMobileOnFactory(Mobile m) {
        FactoryInit factoryInit = (FactoryInit) entitePorteuseEvenement.getInit();
        return m.isOnFactory();
    }

    private Boolean isMobileCrashed(Mobile m) {
        return m.isObjectiveReached();
    }

    @Override
    public void process() {
        Logger.Information(entitePorteuseEvenement, "FactoryEvent.Process", "FactoryEvent à " + getDateOccurence());

        Optional<Mobile> mobileAbove = entitePorteuseEvenement.recherche(e -> e instanceof Mobile &&
                        isMobileOnFactory((Mobile) e) && e.getEtat() != EntiteSimulee.EtatEntite.DEAD)
                .stream()
                .map(e -> (Mobile) e)
                .findFirst();

        if (mobileAbove.isPresent()) {
            Logger.Information(entitePorteuseEvenement, "FactoryEvent.Process", "Usine touchée par un mobile");
            Factory factory = (Factory) entitePorteuseEvenement;
            factory.explode();
            factory.alertCommandCenter(mobileAbove.get());
        }

        this.rescheduleAt(getDateOccurence().add(LogicalDuration.ofSeconds(1)));
        entitePorteuseEvenement.Post(this);
    }

}
