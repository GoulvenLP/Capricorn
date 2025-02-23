package enstabretagne.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import enstabretagne.base.logger.Logger;
import enstabretagne.base.time.LogicalDateTime;
import enstabretagne.base.time.LogicalDuration;
import enstabretagne.engine.EntiteSimulee.EtatEntite;
import enstabretagne.simulation.basics.IScenarioIdProvider;
import enstabretagne.simulation.basics.ISimulationDateProvider;
import enstabretagne.simulation.basics.ScenarioId;
import enstabretagne.simulation.basics.SortedList;

public class SimuEngine implements ISimulationDateProvider, IScenarioIdProvider{
	
	private SortedList<SimEvent> echeancier;
	private LogicalDateTime start;
	private LogicalDateTime end;
	private LogicalDateTime currentDate;
	public LogicalDateTime Now() {
		return currentDate;
	}
	public List<EntiteSimulee> mesEntitesSimulees; 
	
	
	public SimuEngine() {
		echeancier = new SortedList<>();
		mesEntitesSimulees = new ArrayList<>();
		Logger.setDateProvider(this);
		Logger.setScenarioIdProvider(this);
	}
	
	private SimuScenario currentScenario;
	public void initSimulation(SimuScenario s,LogicalDateTime start, LogicalDateTime end) {
		this.currentScenario = s;
		this.start=start;
		currentDate = this.start;
		this.end = end;
		
		if(s!=null)
			s.Init();
		
		for(EntiteSimulee e:mesEntitesSimulees)
		{
			if(e!=s)
				e.Init();
		}
	}


	protected void Post(SimEvent ev)  {
		if(ev.getDateOccurence().compareTo(currentDate)<0) 
			Logger.Fatal(this, "Post", "tentative de poster un événement antérieur au temps courant");
		ev.Posted(true);
		echeancier.add(ev);
	}
	
	//seule methode donnant acces a l'echeancier
	protected void unPost(SimEvent ev)  {
		ev.Posted(false);
		echeancier.remove(ev);
	}
	
	public void simulate()
	{
		Logger.Detail(this, "simulate", "Début de la simulation");
		simulate(end.soustract(currentDate));
		Logger.Detail(this, "simulate", "Fin de la simulation");
	}
	boolean pauseFlag;
	public void releaseFlag()
	{
		pauseFlag = false;
	}
	void requestPause(EntiteSimulee e)
	{
		pauseFlag = true;
	}

	//boucle de simulation
		public boolean simulate(LogicalDuration d)
		{

			if(endReached) return pauseFlag;
			
			var stepEnd = currentDate.add(d);
			if(stepEnd.compareTo(end)>0) {
				stepEnd = end;
				setEndReached(true);
			}
			
			//simple parcours de l'echeancier
			while(hasANextEvent(stepEnd))
			{
				//on prend le premier evenement suivant de l'echeancier
				SimEvent ev = echeancier.first();
				
				//on l'enleve de l'echeancier
				echeancier.remove(ev);
				
				//si l'entite est DEAD on ne tire pas l'evenement
				if(ev.entitePorteuseEvenement().getEtat().equals(EtatEntite.INITIALIZED)) {
					currentDate = ev.getDateOccurence();
					ev.process();
				}

				if(pauseFlag) {
					stepEnd = currentDate;
					break;
				}
			}
			
			currentDate = stepEnd;

//			Logger.Detail(this, "simulate(dt)", "Date de fin de step =" + currentDate); // TODO Reenable?
			return pauseFlag;
		}

		//permet de savoir s'il reste un evenement encore a traiter
		private boolean hasANextEvent(LogicalDateTime d) {
		
			if(echeancier.size()>0) {
				LogicalDateTime nextDate =echeancier.first().getDateOccurence();
				if(nextDate.compareTo(d)<=0) 
					return true;
			}
			return false;
		}
	
		boolean endReached;
		void setEndReached(boolean endReached) {
			boolean previousEndReached = this.endReached;
			this.endReached = endReached;

			if(endReached!=previousEndReached && endReached) {
				Logger.Information(this, "setEndReached", "Fin end reach");
			}

		}
		
	public List<EntiteSimulee> recherche(Predicate<EntiteSimulee> query) {
		List<EntiteSimulee> resultats = new ArrayList<>();
		for(EntiteSimulee e:mesEntitesSimulees)
		{
			if(query.test(e)) resultats.add(e);
		}
		return resultats;
	}

	@Override
	public LogicalDateTime SimulationDate() {
		return currentDate;
	}
	
	public void terminate(boolean last) {
		Logger.Information(this, "Terminate", "Processus d'arrêt de la simulation");
		//on vide l'echeancier.
		//il peut contenir des evenements restants
		//indispensable pour le prochain run
		echeancier.clear();
		
		//on termine les entit�s.
		//on vide la liste des entit�s
		for(EntiteSimulee e:mesEntitesSimulees)
		{
			//seule l'entit� sc�nario persiste d'un run � l'autre			
			if(!last) {
				if(!(e instanceof SimuScenario)) e.terminate();
			}
			else
				e.terminate();
		}
		mesEntitesSimulees.clear();
		
		//on ne fait plus r�f�rence au sc�nario pr�c�dent
		currentScenario = null;
		
		//on met � zero les �l�ments de temps logique
		currentDate = null;
		start = null;
		end = null;

		//on sollicite le garbage collector pour qu'il cleane la m�moire
		//c'est un bon moment pour le faire
		System.gc();

	}

	@Override
	public ScenarioId getScenarioId() {
		if (currentScenario != null)
			return currentScenario.getID();
		else
			return null;
	}
}
