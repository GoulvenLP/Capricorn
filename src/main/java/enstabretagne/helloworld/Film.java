package enstabretagne.helloworld;

public enum Film {
	Starwars("Starwars"),
	Ghostbuster("Ghostbuster"),
	Sisi("Sisi");
	
	String nom;
	private Film(String n) {
		nom=n;
	}
	@Override
	public String toString() {
		return nom;
	}
}
