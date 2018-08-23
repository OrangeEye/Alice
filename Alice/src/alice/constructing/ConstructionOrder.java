package alice.constructing;

import alice.position.APosition;
import alice.production.ProductionOrder;
import alice.units.AUnit;
import alice.units.AUnitType;
import alice.units.Select;
import atlantis.constructing.position.APositionFinder;
import bwapi.Position;

public class ConstructionOrder {

	private AUnitType buildingType;
	private ConstructionOrderStatus status;
	private ProductionOrder productionOrder;
	private APosition positionToBuild;
	private APosition near;
	private AUnit builder;

	

	public ConstructionOrder(AUnitType buildingType) {
		this.buildingType = buildingType;

		status = ConstructionOrderStatus.CONSTRUCTION_NOT_STARTED;
	}
	
	public AUnit getBuilder() {
		return builder;
	}

	public ConstructionOrderStatus getStatus() {
		return this.status;
	}

	public AUnitType getBuildingType() {
		return buildingType;
	}

	public void setStatus(ConstructionOrderStatus status) {
		this.status = status;
	}
	public ProductionOrder getProductionOrder() {
		return productionOrder;
	}
	public void setProductionOrder(ProductionOrder productionOrder) {
		this.productionOrder = productionOrder;
	}

	public APosition getPositionToBuild() {
		return positionToBuild;
	}

	public void setPositionToBuild(APosition positionToBuild) {
		this.positionToBuild = positionToBuild;
	}

	public APosition getNearTo() {
		return near;
	}

	public void setNear(APosition near) {
		this.near = near;
	}
	
	/**
     * If it's impossible to build in given position (e.g. occupied by units), find new position.
     */
    public APosition findNewBuildPosition() {
        return APositionFinder.getPositionForNew(builder, buildingType, this);
    }
	
	/**
     * In order to find a tile for building, one worker must be assigned as builder. We can assign any worker
     * and we're cool, bro.
     */
    protected void assignNextBuilderForNow(APosition target) {
        builder = Select.our().ourMiningMineralsWorkers().clostestOrInRadius(target, 600); //TODO Radius evtl. anpassen
    }

}
