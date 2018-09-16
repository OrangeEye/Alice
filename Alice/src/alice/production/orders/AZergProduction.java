package alice.production.orders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;

import alice.AGame;
import alice.Alice;
import alice.AliceConfig;
import alice.position.AMap;
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
		AOrder nextOrder = AZergBuildOrder.getNextOrder();
		if (nextOrder == null) {
			System.out.println("Kein nächste Bau Order");
			return;
		}

		if (nextOrder.isUnitType())
			buildNextUnit(nextOrder);
	}

	/**
	 * baut das nächste Gebäude / nächste Einheit
	 */
	private static void buildNextUnit(AOrder order) {
		AUnitType nextUnitType = order.getAUnitType();
		if (notEnoughRessourcesAndSupply(nextUnitType))
			return;

		AUnitType builder = order.getWhatBuildsIt();
		if (builder.equals(AUnitType.Zerg_Larva)) {
			morphUnit(nextUnitType, order);
		}

		if (nextUnitType.isBuilding()) {
			if (nextUnitType.isSpecialBuilding())
				buildSpecialBuilding(nextUnitType, order);
		}

	}

	/**
	 * Prüft ob genug Ressourcen und Versorgung dafür da ist. Eingerechnet die
	 * bereits geplanten Aufträge
	 * 
	 * @param ut
	 * @return
	 */
	private static boolean notEnoughRessourcesAndSupply(AUnitType ut) {
		if (AGame.getGas() < ut.gasPrice() + gasReserved || AGame.getMinerals() < ut.mineralPrice() + mineralReserved
				|| AGame.getSupplyFree() < ut.supplyRequired())
			return true;

		return false;
	}

	private static void morphUnit(AUnitType nextUnitType, AOrder order) {
		AUnit larva = Select.first(Select.getOurLarvas());
		if (larva != null) {
			order.setBuilder(larva);
			larva.morph(nextUnitType);
			Select.getOurLarvas().remove(larva.getID());

			order.setStatus(AOrder.STATUS_IN_PROCESS);
			addSupply(nextUnitType);
		}
	}

	private static void buildSpecialBuilding(AUnitType nextUnitType, AOrder order) {
		if (nextUnitType.isGasBuilding()) {
			if (extractorTrick)
				extractorTrick( order);
		}
	}

	private static void extractorTrick( AOrder order) {
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

	private static void addSupply(AUnitType ut) {
		AGame.increaseSupplyUsed(ut);
	}
	
	private static void buildExtractor(AUnit gasField,AOrder order) {
		AUnit worker = Select.clostestOrInRadius(Select.ourWorkersFreeToBuildOrRepair(), gasField.getPosition(), 250);
		if(worker != null)
		worker.build(AUnitType.Zerg_Extractor, gasField.getPosition(), order);
	}

	public static LinkedList<AOrder> getBuildOrderList() {
		return AZergBuildOrder.getCurrentBuildOrder().getBuildOrderList();
	}

}
