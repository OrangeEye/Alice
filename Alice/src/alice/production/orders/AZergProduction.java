package alice.production.orders;

import java.util.LinkedList;
import alice.AGame;
import alice.position.AMap;
import alice.production.AOrder;
import alice.units.AUnit;
import alice.units.AUnitType;
import alice.units.Select;

public class AZergProduction {

	private static int mineralReserved;
	private static int gasReserved;

	private static boolean extractorTrick = true;

	public static void update() {
		updateReservedRessources();

		AOrder nextOrder = AZergBuildOrder.getNextOrder();
		if (nextOrder == null) {
			System.out.println("Kein nächste Bau Order");
			return;
		}

		if (nextOrder.isUnitType())
			buildNextUnit(nextOrder);

		cancelBuilding();
	}

	/**
	 * baut das nächste Gebäude / nächste Einheit
	 */
	private static void buildNextUnit(AOrder order) {
		AUnitType nextUnitType = order.getAUnitType();
		if (notEnoughRessourcesAndSupply(nextUnitType, order))
			return;

		AUnitType builder = order.getWhatBuildsIt();
		if (builder.equals(AUnitType.Zerg_Larva))
			morphUnit(nextUnitType, order);

		if (nextUnitType.isBuilding())
			buildBuilding(order);

	}

	private static void cancelBuilding() {
		AUnit toCancel = null;
		if (extractorTrick && AGame.getSupplyUsed() == 18 && Select.getOurLarvas().size() == 0)
			toCancel = Select.getUnit(AUnitType.Zerg_Extractor);

		if (toCancel != null)
			extractorTrick = !toCancel.cancelConstruction();
	}

	private static void buildBuilding(AOrder order) {

		if (order.getAdditionalInfo().equals(AOrder.INFO_IS_EXPANSION))
			expand(order);
		else if (order.getAdditionalInfo().equals(AOrder.INFO_EXTRACTOR_TRICK))
			buildExtractor(order);
		else
			buildInMainBase(order);

	}

	private static void buildInMainBase(AOrder order) {
		if (order.setBuildPosition(AMap.getBuildPositionCloseTo(order.getAUnitType(), AMap.getMainBasePosition()))) {
			if (order.getBuilder() == null)
				order.setBuilder(Select.clostestOrInRadius(Select.ourWorkersFreeToBuildOrRepair(),
						order.getBuildPosition(), 250));

			construct(order);
		}

	}

	private static void construct(AOrder order) {
		if (order.getBuilder() != null && order.getBuildPosition() != null)
			order.getBuilder().build(order.getAUnitType(), order.getBuildPosition(), order);
	}

	private static void expand(AOrder order) {
		order.setBuildPosition(AMap.getNextExpandPosition());

		// Wenn die Order keinen Builder mehr hat, wird ein neuer zugewiesen
		if (order.getBuilder() == null)
			order.setBuilder(
					Select.clostestOrInRadius(Select.ourWorkersFreeToBuildOrRepair(), order.getBuildPosition(), 250));

		construct(order);
	}

	/**
	 * Prüft ob genug Ressourcen und Versorgung dafür da ist. Eingerechnet die
	 * bereits geplanten Aufträge
	 * 
	 * @param ut
	 * @return
	 */
	private static boolean notEnoughRessourcesAndSupply(AUnitType ut, AOrder order) {
		if (AGame.getGas() < ut.gasPrice() + gasReserved
				|| AGame.getMinerals() < ut.mineralPrice() + mineralReserved + order.getMineralDelay()
				|| AGame.getSupplyFree() < ut.supplyRequired())

			return true;

		return false;
	}

	private static void morphUnit(AUnitType nextUnitType, AOrder order) {
		AUnit larva = Select.first(Select.getOurLarvas());
		if (larva != null) {
			order.setBuilder(larva);

			if (larva.morph(nextUnitType))
				order.setStatus(AOrder.STAUS_IN_ORDER);
		}
	}

	private static void buildExtractor(AOrder order) {
		order.setBuildPosition(Select.nextVespeneGeyserPosition());
		if (order.getBuildPosition() == null)
			return;
		order.setBuilder(
				Select.clostestOrInRadius(Select.ourWorkersFreeToBuildOrRepair(), order.getBuildPosition(), 250));

		construct(order);
	}

	public static LinkedList<AOrder> getBuildOrderList() {
		return AZergBuildOrder.getCurrentBuildOrder().getBuildOrderList();
	}

	public static int getMineralReserved() {
		return mineralReserved;
	}

	public static int getGasReserved() {
		return gasReserved;
	}

	public static void updateReservedRessources() {
		mineralReserved = 0;
		gasReserved = 0;
		for (AOrder order : getBuildOrderList()) {
			if (order.getStatus().equals(AOrder.STAUS_IN_ORDER)) {
				mineralReserved += order.getAUnitType().mineralPrice();
				gasReserved += order.getAUnitType().gasPrice();

			}
		}
	}

}
