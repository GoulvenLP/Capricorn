package enstabretagne.applications.capricorn.expertise;

import org.apache.commons.geometry.euclidean.twod.Vector2D;

public record Location(String nom,Vector2D position) {
	
	public Location add(Vector2D delta) {
		return new Location(nom,position.add(delta));
	}

	public String getName(){ return this.nom; }


	/**
	 * Compares two Location objects depending on their coordinates
	 * @param o   the reference object with which to compare.
	 * @return true if both coordinates match, else returns false.
	 */
	@Override
	public boolean equals(Object o){
		if (o == null) return false;
		if (!(o instanceof Location)) return false;
		Location l = (Location)o;
		return (l.position().getX() == this.position().getX() && l.position().getY() == this.position().getY());
	}
}
