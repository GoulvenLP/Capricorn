package enstabretagne.engine;

import enstabretagne.base.time.LogicalDateTime;
import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;

public class SimuScenarioInitData extends InitData{
	@JsonbCreator
	public SimuScenarioInitData(
			 @JsonbProperty(value = "name") String name,
			 @JsonbProperty(value = "replique") int replique,			 
			 @JsonbProperty(value = "graine") double graine,
			 @JsonbProperty(value = "start") String start,
			 @JsonbProperty(value = "end") String end) {
		super(name);
		this.graine = graine;
		this.start = start;
		this.end = end;
		this.replique=replique;
	}
	public final int replique;
	public final double graine;
	public final String start;
	public final String end;
	

}
