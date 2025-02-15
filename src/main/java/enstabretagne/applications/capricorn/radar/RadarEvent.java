package enstabretagne.applications.capricorn.radar;

import enstabretagne.applications.capricorn.mobile.Mobile;
import enstabretagne.base.logger.Logger;
import enstabretagne.base.time.LogicalDateTime;
import enstabretagne.base.time.LogicalDuration;
import enstabretagne.engine.EntiteSimulee;
import enstabretagne.engine.SimEvent;

import java.util.List;

public class RadarEvent extends SimEvent {

    public RadarEvent(LogicalDateTime d) {
        super(d);
    }


    public Boolean isMobileInRadar(Mobile m) {
        return m.position().position().getX() > (((RadarInit) entitePorteuseEvenement.getInit()).position.position().getX() - ((RadarInit) entitePorteuseEvenement.getInit()).portee);
    }

    @Override
    public void process() {
        Logger.Detail(entitePorteuseEvenement, "RadarEvent.Process", "RadarEvent à " + getDateOccurence());

        // predicat pour vérifier qu'un mobile est à un certaine position du radar
        List<EntiteSimulee> mobiles = entitePorteuseEvenement.recherche(e -> (
                e instanceof Mobile) && isMobileInRadar((Mobile) e)
        );
        for (EntiteSimulee e : mobiles) {
            Logger.Information(entitePorteuseEvenement, "RadarEvent.Process", "Mobile trouvé : " + e);
        }
        this.rescheduleAt(getDateOccurence().add(LogicalDuration.ofSeconds(1)));
        entitePorteuseEvenement.Post(this);
    }


}
