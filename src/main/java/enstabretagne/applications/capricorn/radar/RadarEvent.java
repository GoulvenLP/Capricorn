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

    @Override
    public void process() {
        Logger.Detail(entitePorteuseEvenement, "RadarEvent.Process", "RadarEvent à " + getDateOccurence());

        //List<EntiteSimulee> mobiles = entitePorteuseEvenement.recherche(e -> (e instanceof Mobile) && ((Mobile) e).position().position().getX() > TODO:avoir ici la position du radar ? );
        //for (EntiteSimulee e : mobiles) {
        //    Logger.Information(entitePorteuseEvenement, "RadarEvent.Process", "Mobile trouvé : " + e);
        //}
        this.rescheduleAt(getDateOccurence().add(LogicalDuration.ofSeconds(1)));
        entitePorteuseEvenement.Post(this);
    }
}
