package enstabretagne.helloworld;

import enstabretagne.engine.InitData;

public class InitEtudiant extends InitData {
	public final Genre genre;
	public final Film filmPrefere;
	public InitEtudiant(String name,Genre genre, Film filmPrefere) {
		super(name);
		this.genre = genre;
		this.filmPrefere = filmPrefere;
	} 

	
}
