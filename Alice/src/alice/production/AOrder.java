package alice.production;

import java.util.ArrayList;

import alice.position.APosition;
import alice.units.AUnit;
import alice.units.AUnitType;

public class AOrder {
	
	public static String STATUS_FINISHED = "finished";
	public static String STAUS_IN_ORDER = "in_order";
	public static String STATUS_IN_PROCESS = "in_process";
	public static String STATUS_NOT_STARTED = "not_started";
	
	
	private String status = STATUS_NOT_STARTED;
	private AUnitType unitType;
	private APosition buildPosition;
	private AUnit builder;
	


	public AOrder (AUnitType ut) {
		unitType = ut;
	}
	
	public AUnitType getAUnitType() {
		return unitType;
	}
	
	public boolean isUnitType() {
		return (unitType != null);
	}

	public AUnit getBuilder() {
		return builder;
	}

	public void setBuilder(AUnit builder) {
		this.builder = builder;
	}

	
	public AUnitType getWhatBuildsIt() {
		return unitType.getWhatBuildsIt();
	}
	
	



	public APosition getBuildPosition() {
		return buildPosition;
	}


	public void setBuildPosition(APosition buildPosition) {
		this.buildPosition = buildPosition;
	}


	public void setStatus(String status) {
		this.status = status;
	}


    public String getStatus() {
        return this.status;
    }
    
    @Override
    public String toString() {
    	return this.getStatus() + " " + this.unitType.toString();
    }
}
