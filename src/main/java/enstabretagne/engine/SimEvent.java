package enstabretagne.engine;

import enstabretagne.base.time.LogicalDateTime;

public abstract class SimEvent implements Comparable<SimEvent> {
	protected EntiteSimulee entitePorteuseEvenement;
	private LogicalDateTime d;
	protected LogicalDateTime getDateOccurence()
	{
		return d;
	}

	public void rescheduleAt(LogicalDateTime d)
	{
		this.d=d;
	}
	public SimEvent(LogicalDateTime d) {
		this.d=d;
	}
	
	@Override
	public int compareTo(SimEvent ev) {
		return this.d.compareTo(ev.d);
	}
	
	public abstract void process();

	private boolean isPosted;
	public boolean Posted() {
		return isPosted;
	}
	public void Posted(boolean b) {
		isPosted = b;
	}

	public EntiteSimulee entitePorteuseEvenement() {
		return entitePorteuseEvenement;
	}

}
