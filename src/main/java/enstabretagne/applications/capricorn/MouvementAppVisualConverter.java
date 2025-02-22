package enstabretagne.applications.capricorn;

import enstabretagne.applications.capricorn.environnement.Environement;
import enstabretagne.applications.capricorn.factory.Factory;
import enstabretagne.applications.capricorn.missile.Missile;
import enstabretagne.applications.capricorn.mobile.Mobile;
import enstabretagne.applications.capricorn.radar.Radar;
import enstabretagne.engine.SimuEngine;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;

public class MouvementAppVisualConverter extends enstabretagne.moniteur2D.VisualConverter {

	@Override
	public void init(SimuEngine engine, Canvas background, Canvas objects, double maxX, double maxY) {
		super.init(engine, background, objects, maxX, maxY);
		
		addVisualMapper(Environement.class, this::convEnv);
		addVisualMapper(Radar.class, this::convRadar);
		addVisualMapper(Mobile.class, this::convMobile);
		addVisualMapper(Missile.class, this::convMissile);
		addVisualMapper(Factory.class, this::convFactory);
	}

	public void convFactory(Factory f) {
		if(f.isExplosed()) {
			drawCircle(true, Layers.Objects, f.getPosition().position().getX(), f.getPosition().position().getY(), 5, Color.RED, f.ini.name);
			writeText(Layers.Objects, f.getPosition().position().getX() + 30, f.getPosition().position().getY(), Color.RED, "Factory Damaged !");
		}else{
			drawCircle(true, Layers.Objects, f.getPosition().position().getX(), f.getPosition().position().getY(), 5, Color.GREEN, f.ini.name);
		}
	}

	public void convMobile(Mobile m) {
		drawCircle(true,Layers.Objects , m.getPosition().position().getX(),m.getPosition().position().getY(), 5, Color.BLUE, m.ini.name);
	}

	public void convRadar(Radar r) {
		Color c;
		if(r.isTargetsDetected()){
			c = Color.RED;
		}else{
			c = Color.GREEN;
		}
		drawCircle(true,Layers.Objects , r.getPosition().position().getX(),r.getPosition().position().getY(), 5, c, r.rIni.name);
		drawCircle(false,Layers.Objects , r.getPosition().position().getX(),r.getPosition().position().getY(), r.rIni.portee*2, c, "");
	}

	public void convEnv(Environement env) {
		for(var pos : env.ini.positions())
			drawCircle(true,Layers.BackGround, pos.position().getX(), pos.position().getY(), 5, Color.BLACK, pos.nom());
	}

	public void convMissile(Missile m) {
		drawCircle(true,Layers.Objects , m.getPosition().position().getX(),m.getPosition().position().getY(), 8, Color.RED, m.ini.name);
	}

	//
}
