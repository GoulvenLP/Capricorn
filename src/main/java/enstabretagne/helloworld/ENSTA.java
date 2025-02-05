package enstabretagne.helloworld;

import enstabretagne.base.logger.Logger;
import enstabretagne.base.time.LogicalDateTime;
import enstabretagne.engine.SimuEngine;

public class ENSTA {
	public static void main(String[] args) {
		SimuEngine engine = new SimuEngine();

		LogicalDateTime start = new LogicalDateTime("04/12/2019 14:00");
		LogicalDateTime end = new LogicalDateTime("04/12/2019 15:00");

		new EtudiantSimple(engine, "Luka", new InitEtudiant("Luka",Genre.Homme,Film.Starwars));
		new EtudiantSimple(engine, "Olivier", new InitEtudiant("Olivier",Genre.Homme,Film.Ghostbuster));
		new EtudiantSimple(engine, "Leia", new InitEtudiant("Leia",Genre.Femme,Film.Sisi));
		new EtudiantSimple(engine, "Beatrice", new InitEtudiant("Beatrice",Genre.Femme,Film.Sisi));

		engine.initSimulation(null,start, end);
		engine.simulate();
		
		Logger.Terminate();

	}
}
