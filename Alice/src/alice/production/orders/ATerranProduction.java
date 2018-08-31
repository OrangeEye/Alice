package alice.production.orders;

import alice.AGame;
import alice.units.AUnit;
import alice.units.AUnitType;
import alice.units.Select;

public class ATerranProduction {

	public static void update() {
		for (AUnit building : Select.idle(Select.ourBuildings().listUnits())) {
			if (building.isType(AUnitType.Terran_Command_Center))
				CommandCenterUpdate(building);
		}
	}

	private static void CommandCenterUpdate(AUnit building) {
		if (AGame.getSupplyFree() >= 2 && AGame.getMinerals() >= 50)
			building.train(AUnitType.Terran_SCV);
	}

}
