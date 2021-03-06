package alice.buildings.managers;

import alice.AGame;
import alice.AliceConfig;
import alice.units.AUnitType;

public class ASupplyManager {

	private static double supplyTotal;
	private static double supplyFree;

	public static void update() {
		supplyTotal = AGame.getSupplyTotal();

		supplyFree = AGame.getSupplyFree();

		int suppliesBeingBuilt = requestedConstructionOfSupplyNumber();
		boolean noSuppliesBeingBuilt = suppliesBeingBuilt == 0;
		if (supplyTotal <= 11) {
			if (supplyFree <= 2 && noSuppliesBeingBuilt) {
				requestAdditionalSupply();
			}
		} else if (supplyTotal <= 20) {
			if (supplyFree <= 4 && noSuppliesBeingBuilt) {
				requestAdditionalSupply();
			}
		} else if (supplyTotal <= 40) {
			if (supplyFree <= 7 && noSuppliesBeingBuilt) {
				requestAdditionalSupply();
			}
		} else if (supplyTotal <= 100) {
			if (supplyFree <= 10 && noSuppliesBeingBuilt) {
				requestAdditionalSupply();
			}
		} else if (supplyTotal <= 200) {
			if (supplyFree <= 14 && suppliesBeingBuilt <= 1) {
				requestAdditionalSupply();
			}
		}
	}

	private static void requestAdditionalSupply() {

	//	AConstructionManager.requestConstructionOf(AliceConfig.SUPPLY);

	}

	private static int requestedConstructionOfSupplyNumber() {
	//	return AConstructionManager.countNotFinishedConstructionsOfType(AliceConfig.SUPPLY);
		return 0;
	}
}
