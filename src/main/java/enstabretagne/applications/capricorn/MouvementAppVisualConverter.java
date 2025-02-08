package enstabretagne.applications.capricorn;


import enstabretagne.applications.capricorn.environnement.Environement;
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
}

	public void convMobile(Mobile m) {
		drawCircle(true,Layers.Objects , m.position().position().getX(),m.position().position().getY(), 5, Color.BLUE, m.ini.name);

	}
		public void convRadar(Radar r) {
		drawCircle(true,Layers.Objects , r.position().position().getX(),r.position().position().getY(), 5, Color.AQUA, r.rIni.name);
		drawCircle(false,Layers.Objects , r.position().position().getX(),r.position().position().getY(), r.rIni.portee, Color.AQUA, "");
	}

	public void convEnv(Environement env) {
		for(var pos : env.ini.positions())
			drawCircle(true,Layers.BackGround, pos.position().getX(), pos.position().getY(), 5, Color.BLACK, pos.nom());
	}

	public void convMissile(Missile m) {
		drawCircle(true,Layers.Objects , m.position().position().getX(),m.position().position().getY(), 8, Color.RED, m.ini.name);
	}

	//
}
