package enstabretagne.applications.capricorn.mobile;

import org.apache.commons.geometry.euclidean.twod.Vector2D;

import enstabretagne.applications.capricorn.expertise.ILocatable;
import enstabretagne.applications.capricorn.expertise.Location;
import enstabretagne.base.logger.Logger;
import enstabretagne.engine.EntiteSimulee;
import enstabretagne.engine.InitData;
import enstabretagne.engine.SimEvent;
import enstabretagne.engine.SimuEngine;

import java.util.Objects;
import java.util.Random;

/**
 * The type Mobile.
 */
public class Mobile extends EntiteSimulee implements ILocatable{

	/**
	 * The Ini.
	 */
	public final MobileInit ini;
	/**
	 * The P.
	 */
	Location p;
	/**
	 * The Move.
	 */
	SimEvent Move;
	private double speed;
	private double probaFail;

	/**
	 * Instantiates a new Mobile.
	 *
	 * @param engine the engine
	 * @param ini    the ini
	 * @param speed  the speed
	 */
	public Mobile(SimuEngine engine, InitData ini, double speed) {
		super(engine, ini);
		this.ini = (MobileInit) ini;
		this.speed = speed;
		this.probaFail = 0.1;

		Move = new SimEvent(engine.Now()) {
			@Override
			public void process() {
				if(isOnFactory()) {
					Random rand = new Random();
					int proba = rand.nextInt(101);
					if (proba <= probaFail){
						Logger.Information(this, "explode", "Cessna missied the factory");
					} else {
						Logger.Information(this, "explode", "Factory destroyed");

					}
					explode();
				}else{
					move();
				}
				Move.rescheduleAt(Now().add(Mobile.this.ini.period));
				Post(Move);
			}
		};
	}

	/**
	 * Explode.
	 */
	public void explode() {
		Logger.Information(this, "explode", "Explosion Position :" + getPosition());
		unPost(Move);
		terminate();
	}


	protected boolean isOnFactory() {
		return Objects.equals(ini.direction.position().subtract(getPosition().position()), Vector2D.of(0, 0));
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

	/**
	 * Move.
	 */
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
