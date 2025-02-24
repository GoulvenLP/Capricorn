package enstabretagne.applications.capricorn.mobile;

import enstabretagne.base.time.LogicalDuration;
import org.apache.commons.geometry.euclidean.twod.Vector2D;

import enstabretagne.applications.capricorn.expertise.ILocatable;
import enstabretagne.applications.capricorn.expertise.Location;
import enstabretagne.base.logger.Logger;
import enstabretagne.engine.EntiteSimulee;
import enstabretagne.engine.InitData;
import enstabretagne.engine.SimEvent;
import enstabretagne.engine.SimuEngine;

import java.util.Random;

/**
 * The type Mobile.
 */
public class Mobile extends EntiteSimulee implements ILocatable{
	public static final int SPEED_FACTOR = 10;
	public static final int RADAR_CYCLE = 5;
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
	private final double speed;
	private final double probaFail;
	private boolean reachedObjective;
	private boolean firstTime;
	private final LogicalDuration delayBeforeLaunch;

	/**
	 * Instantiates a new Mobile.
	 *
	 * @param engine the engine
	 * @param ini    the ini
	 * @param speed  the speed
	 * @param delayBeforeLaunch the delay before launch
	 */
	public Mobile(SimuEngine engine, InitData ini, double speed, LogicalDuration delayBeforeLaunch) {
		super(engine, ini);
		this.ini = (MobileInit) ini;
		this.speed = speed;
		this.probaFail = 0.1;
		this.reachedObjective = false;
		this.firstTime = true;
		this.delayBeforeLaunch = delayBeforeLaunch;

		Move = new SimEvent(engine.Now()) {
			@Override
			public void process() {
				if(isOnFactory()) {
					Random rand = new Random();
					int proba = rand.nextInt(101);
					if (proba <= probaFail * 10){
						Logger.Detail(this, "checkImpact", "mobile_failure, 1");
					} else {
						Logger.Information(this, "explode", "Factory destroyed");
						reachedObjective = true;
					}
					explode();
				}else{
					move();
				}
				if (firstTime){
					Move.rescheduleAt(Now().add(Mobile.this.delayBeforeLaunch));
					firstTime = false;
				} else {
					Move.rescheduleAt(Now().add(Mobile.this.ini.period));
				}
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


	public boolean isOnFactory() {
		return this.getPosition().position().distance(ini.direction.position()) < 25; // width of the factory: 250m
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
		//Logger.Information(this, "bonjour", "Bonjour Position :" + getPosition());
		Vector2D direction = ini.direction.position().subtract(this.getPosition().position())
				.normalize()
				.multiply(RADAR_CYCLE * SPEED_FACTOR * this.speed /3600);
		p=p.add(direction);
	}
	@Override
	public String toString() {
		return ini.name;
	}

	public String getName(){
		return this.ini.name;
	}

	/**
	 * Returns the coordinates of the crashing site of the mobile, i.e. the Factory coordinates
	 * @return the coordinates of the target
	 */
	public Location getTargetCoordinates(){
		return ini.direction;
	}
}
