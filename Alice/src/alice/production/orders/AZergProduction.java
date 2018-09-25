package alice.production.orders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;

import alice.AGame;
import alice.Alice;
import alice.AliceConfig;
import alice.position.AMap;
import alice.position.APosition;
import alice.production.AOrder;
import alice.units.AUnit;
import alice.units.AUnitType;
import alice.units.Select;
import bwapi.Color;

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
		if (builder.equals(AUnitType.Zerg_Larva)) {
			morphUnit(nextUnitType, order);
		}

		if (nextUnitType.isBuilding()) {
			if (nextUnitType.isSpecialBuilding())
				buildSpecialBuilding(nextUnitType, order);
			if (order.hasAdditionalInfo())
				handleAdditionalInfo(order, order.getAdditionalInfo());
			else
				handleNormalBuilding(order);
		}

	}

	private static void cancelBuilding() {
		AUnit toCancel = null;
		if (extractorTrick && AGame.getSupplyUsed() == 18 && Select.getOurLarvas().size() == 0)
			toCancel = Select.getUnit(AUnitType.Zerg_Extractor);

		if (toCancel != null)
			extractorTrick = !toCancel.cancelConstruction();
	}

	private static void handleAdditionalInfo(AOrder order, String info) {
		if (info.equals(AOrder.INFO_IS_EXPANSION)) {
			APosition expandPosition = AMap.getNextExpandPosition();
			if(expandPosition != null)
				order.setBuildPosition(expandPosition);
		}

	}

	private static void construct(AOrder order) {
		AUnit worker = order.getBuilder();
		APosition buildPosition = AMap.getBuildPositionCloseTo(order.getAUnitType(), position);
		if (buildPosition != null)
			order.setBuildPosition(buildPosition);
		if (worker!=null) 
			order.setBuilder(worker);
	}

	private static void expand(AOrder order, APosition position) {
		AUnit worker = order.getBuilder();
		order.setBuildPosition();

		// Wenn die Order keinen Builder mehr hat, wird ein neuer zugewiesen
		if (worker == null)
			worker = Select.clostestOrInRadius(Select.ourWorkersFreeToBuildOrRepair(), order.getBuildPosition(), 250);

		if (worker != null)
			worker.build(AUnitType.Zerg_Hatchery, order.getBuildPosition(), order);

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

	private static void buildSpecialBuilding(AUnitType nextUnitType, AOrder order) {
		if (nextUnitType.isGasBuilding()) {
			if (extractorTrick)
				extractorTrick(order);
		}
	}

	private static void extractorTrick(AOrder order) {
		AUnit gasFieldMain = null;
		double gasFieldDistance = Double.MAX_VALUE;
		for (AUnit gasField : Select.getGasFields().values()) {
			if (gasFieldMain == null
					|| AMap.getMainBasePosition().getDistance(gasField.getPosition()) < gasFieldDistance) {
				gasFieldMain = gasField;
				gasFieldDistance = AMap.getMainBasePosition().getDistance(gasField.getPosition());
			}
		}
		buildExtractor(gasFieldMain, order);
	}

	private static void buildExtractor(AUnit gasField, AOrder order) {
		AUnit worker = Select.clostestOrInRadius(Select.ourWorkersFreeToBuildOrRepair(), gasField.getPosition(), 250);
		if (worker != null) {
			worker.build(AUnitType.Zerg_Extractor, gasField.getPosition(), order);
		}
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
