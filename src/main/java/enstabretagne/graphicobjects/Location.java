package enstabretagne.graphicobjects;

import org.apache.commons.geometry.euclidean.twod.Vector2D;

public class Location implements TacticalObject,Target{
	
	Vector2D v;
	public Vector2D v()
	{
		return v;
	}
	
	public Location(String name, double x, double y) {
		this.name = name;
		v = Vector2D.of(x, y);
	}

	String name;
	@Override
	public String name() {
		return name;
	}

	@Override
	public Vector2D getP() {
		return v();
	}

	@Override
	public Vector2D getOrientation() {
		return Vector2D.ZERO;
	}

	@Override
	public double getVitesse() {
		return 0;
	}

	@Override
	public double getVitesseMax() {
		return 0;
	}
}
