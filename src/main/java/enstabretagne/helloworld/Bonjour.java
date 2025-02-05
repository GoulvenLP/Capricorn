package enstabretagne.helloworld;

import java.util.List;

import enstabretagne.base.logger.Logger;
import enstabretagne.base.time.LogicalDateTime;
import enstabretagne.base.time.LogicalDuration;
import enstabretagne.engine.EntiteSimulee;
import enstabretagne.engine.SimEvent;

public class Bonjour extends SimEvent {

	private String nom;

	public Bonjour(LogicalDateTime d, String nom) {
		super(d);
		this.nom = nom;
	}

	public void process() {
		Logger.Detail(entitePorteuseEvenement, "bonjour.Process",
				"Bonjour de la part de " + nom + " à " + getDateOccurence());
		Logger.Detail(entitePorteuseEvenement, "bonjour.Process",
				"mon film préféré est " + ((InitEtudiant) entitePorteuseEvenement.getInit()).filmPrefere);

		List<EntiteSimulee> amis = entitePorteuseEvenement.recherche(e -> ((e instanceof EtudiantSimple)
				&& (e != entitePorteuseEvenement) && ((InitEtudiant) e.getInit()).filmPrefere.equals(Film.Sisi)));
		if (amis.size() > 0) {
			if (amis.size() == 1) {
				var ie = (InitEtudiant) amis.get(0).getInit();
				String amiString = "";
				switch (ie.genre) {
				case Homme: {
					amiString = "ami";
					break;
				}
				case Femme: {
					amiString = "amie";
					break;
				}
				case Autre: {
					amiString = "ami.e";
					break;
				}
				}

				Logger.Detail(entitePorteuseEvenement, "bonjour.Process",
						"mon " + amiString + " est " + ((EtudiantSimple) amis.get(0)).nom);
			} else {
				String amisString = "";
				for (EntiteSimulee e : amis) {
					amisString = amisString + ((EtudiantSimple) e).nom + " ";
				}
				Logger.Detail(entitePorteuseEvenement, "bonjour.Process", "mes amis sont : " + amisString);

			}
		}

		this.rescheduleAt(getDateOccurence().add(LogicalDuration.ofMinutes(5)));
		entitePorteuseEvenement.Post(this);
	}

}
