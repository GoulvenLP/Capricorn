package enstabretagne.applications.capricorn.environnement;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.geometry.euclidean.twod.Vector2D;

import enstabretagne.applications.capricorn.expertise.Location;
import enstabretagne.engine.InitData;

public class EnvironnementInit extends InitData{

	private List<Location> positions;
	public List<Location> positions() {return positions;}
	
	public EnvironnementInit(String name) {
		super(name);
		positions = new ArrayList<Location>();
	}
	
	public Location addPosition(String nom,Vector2D position) {
		Location l = new Location(nom,position);
		positions.add(l);
		return l;
	}
}
