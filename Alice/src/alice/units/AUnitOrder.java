package alice.units;

public class AUnitOrder {
	
	public static AUnitOrder _IDLE = new AUnitOrder("idle");
	public static AUnitOrder _MORPHING = new AUnitOrder("morphing");
	public static AUnitOrder _MOVE = new AUnitOrder("moving");
	public static AUnitOrder _BUILDING = new AUnitOrder("building");
	public static AUnitOrder _MINING_MINERALS = new AUnitOrder("mining");
	public static AUnitOrder _GATHERING_GAS = new AUnitOrder("gather_gas");
	public static AUnitOrder _TRAIN = new AUnitOrder("training");
	public static AUnitOrder _MOVE_TO_BUILD = new AUnitOrder("move_to_build");

	
	private String unitOrder;
	
	public AUnitOrder(String unitOrder) {
		this.unitOrder = unitOrder;
	}

	public String getUnitOrder() {
		return unitOrder;
	}

	public void setUnitOrder(String unitOrder) {
		this.unitOrder = unitOrder;
	}
	
	@Override
	public String toString() {
		return this.unitOrder;
	}
}
