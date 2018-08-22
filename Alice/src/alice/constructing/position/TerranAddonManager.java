package alice.constructing.position;

import alice.production.ProductionOrder;
import alice.units.AUnit;
import alice.units.AUnitType;
import alice.units.Select;

public class TerranAddonManager {
	
	public static void buildNewAddon(AUnitType addon, ProductionOrder order) {
        AUnit parentBuilding = Select.ourBuildings().ofType(addon.getWhatBuildsIt()).idle().first();
        if (parentBuilding != null) {
            parentBuilding.buildAddon(addon);
        }
    }

}
