package alice.production.orders;

import java.util.LinkedList;

import alice.units.AUnitType;

public abstract class ABuildOrder {
	public static ABuildOrder Zerg_3_Hatch_Opener = new AZergBuildOrder("Zerg_3_Hatch_Opener");

	protected String order;
	protected LinkedList<AUnitType> buildOrder;
	protected boolean dynamic;


	public ABuildOrder(String order) {

	}


	abstract LinkedList<AUnitType> produceRightNow();


	

	protected boolean isDynamic() {
		return dynamic;
	}

	protected String getOrder() {
		return order;
	}
}
