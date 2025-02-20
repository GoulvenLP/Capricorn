package enstabretagne.applications.capricorn.radar;

import enstabretagne.applications.capricorn.mobile.Mobile;
import enstabretagne.applications.capricorn.mobile.MobileInit;
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
        RadarInit radarInit = (RadarInit) entitePorteuseEvenement.getInit();
        return m.getPosition().position().distance(radarInit.position.position()) < radarInit.portee;
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
            // alert the radar that a mobile is in its range and send the mobile position to the command center
            ((Radar) entitePorteuseEvenement).alertCommandCenter(((Mobile)e).getPosition());
        }
        this.rescheduleAt(getDateOccurence().add(LogicalDuration.ofSeconds(1)));
        entitePorteuseEvenement.Post(this);
    }


}
