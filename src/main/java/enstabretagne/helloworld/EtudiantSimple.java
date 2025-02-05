package enstabretagne.helloworld;

import enstabretagne.base.time.LogicalDuration;
import enstabretagne.engine.EntiteSimulee;
import enstabretagne.engine.SimuEngine;

public class EtudiantSimple extends EntiteSimulee{
	public final String nom;
	
	public EtudiantSimple(SimuEngine engine,String nom, InitEtudiant ini) {
		super(engine,ini);
		this.nom = nom;		
	}
	
	@Override
	public void Init() {
		Post(new Bonjour(Now().add(LogicalDuration.ofMinutes(15)), nom));
	}
	
	@Override
	public String toString() {
		return nom;
	}
}
