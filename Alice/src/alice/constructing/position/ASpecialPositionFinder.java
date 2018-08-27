package alice.constructing.position;

import alice.constructing.ConstructionOrder;
import alice.position.APosition;
import alice.units.AUnit;
import alice.units.AUnitType;
import alice.units.Select;
import atlantis.constructing.position.APositionFinder;

public class ASpecialPositionFinder {

	/**
     * Constant used as a hint to indicate that building should be placed in the main base region.
     */
    public static final String NEAR_MAIN = "MAIN";
	
	/**
     * Returns build position for next Refinery/Assimilator/Extractor. It will be chosen for the oldest base
     * that doesn't have gas extracting building.
     */
    protected static APosition findPositionForGasBuilding(AUnitType building) {
        AUnit builder = Select.ourWorkers().first();
        for (AUnit base : Select.ourBases().listUnits()) {
            AUnit geyser =  Select.neutral().ofType(AUnitType.Resource_Vespene_Geyser).clostestOrInRadius(base.getPosition(), 0);

            if (geyser != null) {
              //  APosition position = geyser.getPosition().translateByPixels(-64, -32);
                return geyser.getPosition(); //TODO evtl werden Pixel gebraucht
            }
        }

        System.err.println("Couldn't find geyser for " + building);
        return null;
    }
    
    /**
     * Returns build position for next base. It will usually be next free BaseLocation that doesn't have base
     * built.
     */
    public static APosition findPositionForBase(AUnitType building,  ConstructionOrder constructionOrder) {
        String modifier = constructionOrder.getProductionOrder() != null ? 
                constructionOrder.getProductionOrder().getModifier() : null;
        

        if (modifier != null) {
            if (modifier.equals(NEAR_MAIN) || modifier.equals("NEAR_MAIN")) {
                return findPositionForBase_nearMainBase(building,  constructionOrder);
            }
            else if (modifier.equals(AT_NATURAL)) {
                return findPositionForBase_natural(building,  constructionOrder);
            }
        }
        
        return findPositionForBase_nearestFreeBase(building,  constructionOrder);
    }
    
    private static APosition findPositionForBase_nearMainBase(AUnitType building, ConstructionOrder constructionOrder) {
        APosition near = Select.mainBase().getPosition(); //TODO Position in Pixel wird vielleicht gebraucht
        
        constructionOrder.setNearTo(near);
        constructionOrder.setMaxDistance(30);
        
        return APositionFinder.findStandardPosition(building, near, constructionOrder.getMaxDistance());
    }
    
}
