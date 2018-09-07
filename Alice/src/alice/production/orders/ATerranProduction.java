package alice.production.orders;

import alice.AGame;
import alice.units.AUnit;
import alice.units.AUnitType;
import alice.units.Select;

public class ATerranProduction {

	public static void update() {
		 getIdleBuildingsToWork();
	}
	
	private static void getIdleBuildingsToWork() {
		for (AUnit building : Select.idle(Select.ourBuildings().listUnits())) {
			if (building.isType(AUnitType.Terran_Command_Center))
				CommandCenterProduction(building);
		}
	}

	private static void CommandCenterProduction(AUnit building) {
		if (AGame.getSupplyFree() >= 2 && AGame.getMinerals() >= 50) {
			building.train(AUnitType.Terran_SCV);
			addSupply(AUnitType.Terran_SCV);
		}
	}

	private static void addSupply(AUnitType ut) {
		AGame.increaseSupplyUsed(ut);
	}

}
