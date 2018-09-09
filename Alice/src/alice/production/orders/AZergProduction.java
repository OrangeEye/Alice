package alice.production.orders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;

import alice.AGame;
import alice.AliceConfig;
import alice.units.AUnit;
import alice.units.AUnitType;
import alice.units.Select;

public class AZergProduction {
	public static ABuildOrder buildOrder = AliceConfig.DEFAULT_BUILD_ORDER;
	private static LinkedList<AUnitType> productionOrder;

	private static int mineralReserved;
	private static int gasReserved;

	public static void update() {
		productionOrder = buildOrder.produceRightNow();
		buildNextUnit();
	}

	private static void buildNextUnit() {
		AUnitType nextUnitType = productionOrder.getFirst();
		if (notEnoughRessourcesAndSupply(nextUnitType))
			return;
		AUnitType builder = nextUnitType.getWhatBuildsIt();
		if (builder.equals(AUnitType.Zerg_Larva)) {
			morphUnit(nextUnitType);
			productionOrder.removeFirst();
		}
		if(nextUnitType.isBuilding()) {
			if(nextUnitType.isSpecialBuilding())
				buildSpecialBuilding(nextUnitType);
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

	private static void morphUnit(AUnitType nextUnitType) {
		AUnit larva = Select.first(Select.getOurLarvas());
		if (larva != null) {
			larva.morph(nextUnitType);
			Select.getOurLarvas().remove(larva.getID());
		}
		addSupply(nextUnitType);
	}
	
	private static void buildSpecialBuilding(AUnitType nextUnitType) {
		if(nextUnitType.isGasBuilding()) {
			todo
		}
	}
	
	private static void addSupply(AUnitType ut) {
		AGame.increaseSupplyUsed(ut);
	}
	
	public static LinkedList<AUnitType> getProductionOrder() {
		return productionOrder;
	}

}
