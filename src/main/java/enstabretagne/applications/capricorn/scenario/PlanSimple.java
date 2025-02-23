package enstabretagne.applications.capricorn.scenario;

import java.util.LinkedList;

import enstabretagne.applications.capricorn.commandcenter.CommandCenter;
import enstabretagne.engine.SimuScenario;
import enstabretagne.moniteurPlan.Plan;

public class PlanSimple extends Plan{

	LinkedList<SimuScenario> listeScenarios;
	double vMinCessna;
	double VMaxCessna;
	int nbVCessnaCases;
	
	int nbCessnaMin;
	int nbCessnaMax;
	
	double periodArriveeCessnaMoy;
	double periodArriveeCessnaMax;
	int periodCessnaCases;
	
	String start;
	String end;

	public PlanSimple(
			int nbReplique,
			String start,
			String end,
			double vMinCessna, 
			double VMaxCessna,
			int nbVCessnaCases,
			int nbCessnaMin,
			int nbCessnaMax,
			double periodArriveeCessnaMoy,
			double periodArriveeCessnaMax,
			int periodCessnaCases
			) {
		super(nbReplique);
		assert periodCessnaCases>0 : "Attention periodCessnaCases > 0";
		assert periodCessnaCases>0 : "Attention periodCessnaCases > 0";
		
		this.start = start;
		this.end =end;
		listeScenarios = new LinkedList<>();
		this.vMinCessna=vMinCessna;
		this.VMaxCessna=VMaxCessna;
		this.nbVCessnaCases = nbVCessnaCases;
		
		this.nbCessnaMin=nbCessnaMin;
		this.nbCessnaMax=nbCessnaMax;
				
		this.periodArriveeCessnaMoy=periodArriveeCessnaMoy;
		this.periodArriveeCessnaMax=periodArriveeCessnaMax;
		this.periodCessnaCases=periodCessnaCases;

	}

	@Override
	public void initScenarii() {
		for(int i=0; i< nbVCessnaCases;i++) {
			double vCessna=vMinCessna;
			for(int j=nbCessnaMin;j<=nbCessnaMax;j+=100) { // + 100km/h between two measures
				int nbCessna = j;
				for(int k=0;k<periodCessnaCases;k++) {
					double periodCessnaMinutes = periodArriveeCessnaMoy;
					
					for(int n =0;n<getNbReplique();n++) {
						ScenarioSimpleInit ini = new ScenarioSimpleInit(
								"CAPRICORN v="+i + " n="+j + " d="+k +" r="+n,
								n,
								n,///utilisation de n comme graine
								start,
								end,
								vCessna, 
								nbCessna, 
								periodCessnaMinutes);
						listeScenarios.add(new ScenarioSimple(getEngine(), ini));
					}
				}
			}
		}
	}

	@Override
	public boolean hasNextScenario() {
		return listeScenarios.size()>0;
	}

	@Override
	public SimuScenario nextScenario() {
		if (hasNextScenario()) {
			SimuScenario sc = listeScenarios.pop();
			return sc;
		}
		return null;
	}

}
