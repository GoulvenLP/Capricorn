package enstabretagne.applications.capricorn.expertise;

import org.apache.commons.geometry.euclidean.twod.Vector2D;

public record Location(String nom,Vector2D position) {
	
	public Location add(Vector2D delta) {
		return new Location(nom,position.add(delta));
	}

	public String getName(){ return this.nom; }

}
