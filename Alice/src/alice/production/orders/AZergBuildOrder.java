package alice.production.orders;

import java.util.LinkedList;

import alice.AliceConfig;
import alice.production.AOrder;
import alice.units.AUnit;
import alice.units.AUnitType;

public class AZergBuildOrder {

	private static AZergBuildOrder Zerg_3_Hatch_Opener = new AZergBuildOrder("Zerg_3_Hatch_Opener");
	public static AZergBuildOrder currentBuildOrder = Zerg_3_Hatch_Opener;

	private String buildName;
	private LinkedList<AOrder> buildOrderList;
	private boolean dynamic;
	private boolean doExtractorTrick;

	protected boolean isDynamic() {
		return dynamic;
	}

	protected String getBuildName() {
		return buildName;
	}

	public AZergBuildOrder(String BuildName) {
		this.buildName = BuildName;
		switch (BuildName) {
		case "Zerg_3_Hatch_Opener":
			dynamic = false;
			doExtractorTrick = true;
			buildOrderList = Zerg_3_Hatch_Opener_initialize();
			break;
		}
	}

	private LinkedList<AOrder> Zerg_3_Hatch_Opener_initialize() {
		LinkedList<AOrder> buildOrder = new LinkedList<AOrder>();
		buildOrder.add(new AOrder(AUnitType.Zerg_Drone));
		buildOrder.add(new AOrder(AUnitType.Zerg_Drone));
		buildOrder.add(new AOrder(AUnitType.Zerg_Drone));
		buildOrder.add(new AOrder(AUnitType.Zerg_Drone));
		buildOrder.add(new AOrder(AUnitType.Zerg_Drone));
		buildOrder.add(new AOrder(AUnitType.Zerg_Overlord)); // 9 supply
		buildOrder.add(new AOrder(AUnitType.Zerg_Extractor));
		buildOrder.add(new AOrder(AUnitType.Zerg_Drone));
		buildOrder.add(new AOrder(AUnitType.Zerg_Drone));
		buildOrder.add(new AOrder(AUnitType.Zerg_Drone));
		buildOrder.add(new AOrder(AUnitType.Zerg_Hatchery)); // 12 supply
		buildOrder.add(new AOrder(AUnitType.Zerg_Spawning_Pool)); // 11 supply
		buildOrder.add(new AOrder(AUnitType.Zerg_Drone));
		buildOrder.add(new AOrder(AUnitType.Zerg_Drone));
		buildOrder.add(new AOrder(AUnitType.Zerg_Zergling)); // 13 supply + pool fertig
		buildOrder.add(new AOrder(AUnitType.Zerg_Zergling));
		buildOrder.add(new AOrder(AUnitType.Zerg_Drone));
		buildOrder.add(new AOrder(AUnitType.Zerg_Drone));
		buildOrder.add(new AOrder(AUnitType.Zerg_Drone));
		buildOrder.add(new AOrder(AUnitType.Zerg_Overlord)); // 16 supply evtl. anpassen
		return buildOrder;
	}

	public boolean hasExtractorTrick() {
		return doExtractorTrick;
	}

	public void extractorTrickFinished() {
		this.doExtractorTrick = false;
	}

	public static AZergBuildOrder getCurrentBuildOrder() {
		return currentBuildOrder;
	}

	public LinkedList<AOrder> getBuildOrderList() {
		return buildOrderList;
	}

	/**
	 * kann null zurückgeben
	 * 
	 * @return
	 */
	public static AOrder getNextOrder() {

		for (AOrder order : AliceConfig.DEFAULT_BUILD_ORDER.buildOrderList) {
			if (order.getStatus().equals(AOrder.STATUS_NOT_STARTED)) {
				return order;
			}
		}
		return null;
	}

	/**
	 * Setzt den Status einer Order auf finish
	 */
	public static void setOrderStatus(AUnitType finished, AUnit builder, String orderStatus) {

		for (AOrder order : Zerg_3_Hatch_Opener.buildOrderList) {
			if (order.getAUnitType().equals(finished)
					&& (builder.equals(order.getBuilder()) || (order.getBuilder() == null && order.getAUnitType().equals(AUnitType.Zerg_Extractor)))) { // beim Extractor
																								// verschwindet der
																								// builder
				order.setStatus(orderStatus);
				return;
			}
		}
	}
	


}
