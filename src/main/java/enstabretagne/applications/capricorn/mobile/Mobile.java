package enstabretagne.applications.capricorn.mobile;

import org.apache.commons.geometry.euclidean.twod.Vector2D;

import enstabretagne.applications.capricorn.expertise.ILocatable;
import enstabretagne.applications.capricorn.expertise.Location;
import enstabretagne.base.logger.Logger;
import enstabretagne.engine.EntiteSimulee;
import enstabretagne.engine.InitData;
import enstabretagne.engine.SimEvent;
import enstabretagne.engine.SimuEngine;

public class Mobile extends EntiteSimulee implements ILocatable{

	public final MobileInit ini;
	Location p;
	SimEvent Move;
	private double speed;
	private double probaFail;

	public Mobile(SimuEngine engine, InitData ini, double speed) {
		super(engine, ini);
		this.ini = (MobileInit) ini;
		this.speed = speed;
		this.probaFail = 0.1;

		Move = new SimEvent(engine.Now()) {
			@Override
			public void process() {
				move();
				Move.rescheduleAt(Now().add(Mobile.this.ini.period));
				Post(Move);
			}
		};
	}

	public void explode() {
		Logger.Information(this, "explode", "Explosion Position :" + getPosition());
		unPost(Move);
		terminate();
	}

	@Override
	public void Init() {
		super.Init();
		p=ini.position;
		Post(Move);
	}


	@Override
	public Location getPosition() {
		return p;
	}

	public void move() {
		Logger.Information(this, "bonjour", "Bonjour Position :" + getPosition());
		Vector2D direction = ini.direction.position().subtract(this.getPosition().position())
				.normalize()
				.multiply(10*this.speed / 200);

		p=p.add(direction);
	}
	@Override
	public String toString() {
		return ini.name;
	}

}
