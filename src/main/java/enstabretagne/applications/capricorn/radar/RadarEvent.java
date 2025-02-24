package enstabretagne.applications.capricorn.radar;

import enstabretagne.applications.capricorn.expertise.Location;
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


    private Boolean isMobileInRadar(Mobile m) {
        RadarInit radarInit = (RadarInit) entitePorteuseEvenement.getInit();
        return m.getPosition().position().distance(radarInit.position.position()) < radarInit.portee;
    }

    @Override
    public void process() {
        Logger.Information(entitePorteuseEvenement, "RadarEvent.Process", "RadarEvent à " + getDateOccurence());
        // predicat pour vérifier qu'un mobile est à un certaine position du radar
        List<Mobile> detectedMobiles = entitePorteuseEvenement.recherche(e -> e instanceof Mobile &&
                        isMobileInRadar((Mobile) e) && e.getEtat() != EntiteSimulee.EtatEntite.DEAD)
                .stream()
                .map(e -> (Mobile) e)
                .toList();

        if(!detectedMobiles.isEmpty()){
            Logger.Information(entitePorteuseEvenement, "RadarEvent.Process", "Mobiles trouvés : " + detectedMobiles);
            ((Radar) entitePorteuseEvenement).alertCommandCenter(detectedMobiles);
            ((Radar)entitePorteuseEvenement).setTargetsDetected(true);
            // register the last coordinates
            for (EntiteSimulee e : detectedMobiles){
                ((Radar)entitePorteuseEvenement).add((Mobile)e);
            }
        }else{
            ((Radar)entitePorteuseEvenement).setTargetsDetected(false);
        }
        this.rescheduleAt(getDateOccurence().add(LogicalDuration.ofSeconds(1)));
        entitePorteuseEvenement.Post(this);
    }


}
