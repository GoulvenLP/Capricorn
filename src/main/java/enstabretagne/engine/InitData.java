package enstabretagne.engine;

public abstract class InitData {
	public final String name;
	public InitData(String name) {
		this.name=name;
	}

	public String getName(){
		return this.name;
	}

}


