package alice.constructing.position;

import alice.position.APosition;
import alice.units.AUnit;
import alice.units.AUnitType;
import alice.units.Select;

public class ASpecialPositionFinder {

	
	/**
     * Returns build position for next Refinery/Assimilator/Extractor. It will be chosen for the oldest base
     * that doesn't have gas extracting building.
     */
    protected static APosition findPositionForGasBuilding(AUnitType building) {
        AUnit builder = Select.ourWorkers().first();
        for (AUnit base : Select.ourBases().listUnits()) {
            AUnit geyser = (AUnit) Select.neutral().ofType(AUnitType.Resource_Vespene_Geyser).nearestTo(base);

            if (geyser != null && geyser.distanceTo(base) < 12) {
                APosition position = geyser.getPosition().translateByPixels(-64, -32);
                return position;
            }
        }

        System.err.println("Couldn't find geyser for " + building);
        return null;
    }
    
}
