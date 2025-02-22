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
	private boolean reachedObjective;

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
		this.reachedObjective = false;

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
						reachedObjective = true;

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
	 * Specifies if the target got reached by the mobile.
	 * This depends on the fact that the mobile's coordinates correspond
	 * to the target coordinates and that the mobile got a successful random number.
	 * @return true if the mobile reached its objective, else false
	 */
	public boolean isObjectiveReached(){
		return this.reachedObjective;
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
