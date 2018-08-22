package alice.constructing;

import alice.AGame;
import alice.production.ProductionOrder;
import alice.units.AUnitType;

public class ASpecificConstructionManager {
	
	/**
     * Some buildings like Zerg SUnken Colony need special treatment.
     */
    protected static boolean handledAsSpecialBuilding(AUnitType building, ProductionOrder order) {
    	if (building.isAddon()) {
            TerranAddonManager.buildNewAddon(building, order);
            return true;
        }
        
        return false;
    }
    

}
