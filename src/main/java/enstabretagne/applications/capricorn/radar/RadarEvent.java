package enstabretagne.applications.capricorn.radar;

import enstabretagne.applications.capricorn.expertise.Location;
import enstabretagne.applications.capricorn.mobile.Mobile;
import enstabretagne.base.logger.Logger;
import enstabretagne.base.time.LogicalDateTime;
import enstabretagne.base.time.LogicalDuration;
import enstabretagne.engine.SimEvent;

import java.util.List;

public class RadarEvent extends SimEvent {

    public RadarEvent(LogicalDateTime d) {
        super(d);
    }


    private Boolean isMobileInRadar(Mobile m) {
        RadarInit radarInit = (RadarInit) entitePorteuseEvenement.getInit();
        return m.getPosition().position().distance(radarInit.position.position()) < radarInit.portee;
    }

    @Override
    public void process() {
        Logger.Detail(entitePorteuseEvenement, "RadarEvent.Process", "RadarEvent à " + getDateOccurence());
        // predicat pour vérifier qu'un mobile est à un certaine position du radar
        List<Location> mobiles_location = entitePorteuseEvenement.recherche(e -> e instanceof Mobile &&
                isMobileInRadar((Mobile) e))
                .stream()
                .map(e -> ((Mobile) e)
                        .getPosition())
                .toList();
        if(!mobiles_location.isEmpty()){
            Logger.Information(entitePorteuseEvenement, "RadarEvent.Process", "Mobiles trouvés aux positions: " + mobiles_location);
            ((Radar) entitePorteuseEvenement).alertCommandCenter(mobiles_location);
            ((Radar)entitePorteuseEvenement).setTargetsDetected(true);
        }else{
            ((Radar)entitePorteuseEvenement).setTargetsDetected(false);
        }
        this.rescheduleAt(getDateOccurence().add(LogicalDuration.ofSeconds(1)));
        entitePorteuseEvenement.Post(this);
    }


}
