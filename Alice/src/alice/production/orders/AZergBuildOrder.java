package alice.production.orders;

import java.util.LinkedList;

import alice.units.AUnitType;

public class AZergBuildOrder extends ABuildOrder {

	private boolean doExtractorTrick;

	public AZergBuildOrder(String order) {
		super(order);
		this.order = order;
		switch (order) {
		case "Zerg_3_Hatch_Opener":
			dynamic = false;
			doExtractorTrick = true;
			buildOrder = Zerg_3_Hatch_Opener_update();
			break;
		}
	}

	private LinkedList<AUnitType> Zerg_3_Hatch_Opener_update() {
		LinkedList<AUnitType> buildOrder = new LinkedList<AUnitType>();
		buildOrder.add(AUnitType.Zerg_Drone);
		buildOrder.add(AUnitType.Zerg_Drone);
		buildOrder.add(AUnitType.Zerg_Drone);
		buildOrder.add(AUnitType.Zerg_Drone);
		buildOrder.add(AUnitType.Zerg_Drone);
		buildOrder.add(AUnitType.Zerg_Overlord); // 9 supply
		buildOrder.add(AUnitType.Zerg_Extractor);
		buildOrder.add(AUnitType.Zerg_Drone);
		buildOrder.add(AUnitType.Zerg_Drone);
		buildOrder.add(AUnitType.Zerg_Drone);
		buildOrder.add(AUnitType.Zerg_Hatchery); // 12 supply
		buildOrder.add(AUnitType.Zerg_Spawning_Pool); // 11 supply
		buildOrder.add(AUnitType.Zerg_Drone);
		buildOrder.add(AUnitType.Zerg_Drone);
		buildOrder.add(AUnitType.Zerg_Zergling); // 13 supply + pool fertig
		buildOrder.add(AUnitType.Zerg_Zergling);
		buildOrder.add(AUnitType.Zerg_Drone);
		buildOrder.add(AUnitType.Zerg_Drone);
		buildOrder.add(AUnitType.Zerg_Drone);
		buildOrder.add(AUnitType.Zerg_Overlord); // 16 supply evtl. anpassen

		return buildOrder;
	}

	public boolean hasExtractorTrick() {
		return doExtractorTrick;
	}

	public void extractorTrickFinished() {
		this.doExtractorTrick = false;
	}

	@Override
	LinkedList<AUnitType> produceRightNow() {
		if (!isDynamic()) {
			return this.buildOrder;
		}
		return null;
	}

}
