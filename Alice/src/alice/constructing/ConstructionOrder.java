package alice.constructing;

import alice.units.AUnitType;

public class ConstructionOrder {
	
	private AUnitType buildingType;
	private ConstructionOrderStatus status;
	
	public ConstructionOrderStatus getStatus() {
		return this.status;
	}
	
	public AUnitType getBuildingType() {
        return buildingType;
    }

}
