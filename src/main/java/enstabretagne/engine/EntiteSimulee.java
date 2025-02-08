package enstabretagne.engine;

import java.util.List;
import java.util.function.Predicate;

import enstabretagne.base.time.LogicalDateTime;
import enstabretagne.engine.EntiteSimulee.EtatEntite;

public abstract class EntiteSimulee {

	protected SimuEngine engine;
	private InitData ini;

	enum EtatEntite {NONE,INITIALIZED,DEAD};
	//�tat de l'entit�
	private EtatEntite etat;
	protected EtatEntite getEtat() {
		return etat;
	}
	
	public EntiteSimulee(SimuEngine engine,InitData ini) {
		this.etat= EtatEntite.NONE;
		this.engine=engine;
		this.ini=ini;
		if(! (this instanceof SimuScenario))
			engine.mesEntitesSimulees.add(this);
	}
	
	public InitData getInit() {
		return ini;
	}
	
	public void Post(SimEvent ev) {
		ev.entitePorteuseEvenement = this;
		engine.Post(ev);
	}
	
	public LogicalDateTime Now() {
		return engine.SimulationDate();
	}
	
	public List<EntiteSimulee> recherche(Predicate<EntiteSimulee> query) {
		return engine.recherche(query);
	}
	public void Init() {
		etat = EtatEntite.INITIALIZED;
	}
	protected void terminate() {
		etat = EtatEntite.DEAD;
	}
}
