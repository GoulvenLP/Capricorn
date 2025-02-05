package enstabretagne.graphicobjects;

import org.apache.commons.geometry.euclidean.twod.Vector2D;

public interface Target {
		public Vector2D getP();
		public Vector2D getOrientation();
		public double getVitesse();
		public double getVitesseMax();
	}