package alice.constructing.position;

import alice.AGame;
import alice.constructing.ConstructionOrder;
import alice.position.APosition;
import alice.units.AUnit;
import alice.units.AUnitType;
import alice.units.Select;

public class APositionFinder {
	
	/**
     * Returns build position for next building of given type.
     */
    public static APosition getPositionForNew(AUnit builder, AUnitType building, ConstructionOrder constructionOrder) {
        APosition near = constructionOrder != null ? constructionOrder.getNearTo() : null;
        double maxDistance = constructionOrder != null ? constructionOrder.getMaxDistance() : 30;
        return getPositionForNew(builder, building, constructionOrder, near, maxDistance);
    }
    
    /**
     * Returns build position for next building of given type. If <b>nearTo</b> is not null, it forces to find
     * position
     * <b>maxDistance</b> build tiles from given position.
     */
    public static APosition getPositionForNew(AUnit builder, AUnitType building, 
            ConstructionOrder constructionOrder, APosition nearTo, double maxDistance) {
        totalRequests++;
        constructionOrder.setMaxDistance(maxDistance);

        // =========================================================
        // Buildings extracting GAS
        if (building.isGasBuilding()) {
            return ASpecialPositionFinder.findPositionForGasBuilding(building);
        } 

        // =========================================================
        // BASE
        else if (building.isBase()) {
            APosition position = ASpecialPositionFinder.findPositionForBase(building, builder, constructionOrder);
            return position;
        } 

        // =========================================================
        // BUNKER
        else if (building.isBunker()) {
            APosition position = TerranBunkerPositionFinder.findPosition(building, builder, constructionOrder);
            return position;
        } 

        // =========================================================
        // Creep colony
        else if (building.equals(AUnitType.Zerg_Creep_Colony)) {
            return ZergCreepColony.findPosition(building, builder, constructionOrder);
        } 

        // =========================================================
        // STANDARD BUILDINGS
        else {

            // If we didn't specify location where to build, build somewhere near the main base
            if (nearTo == null) {
                if (AGame.playsAsZerg()) {
//                    nearTo = Select.secondBaseOrMainIfNoSecond().getPosition();
                    nearTo = Select.mainBase().getPosition();
                }
                else {
//                    nearTo = Select.mainBase().getPosition();
                    nearTo = Select.ourBases().random().getPosition();
                }
            }

            // If all of our bases have been destroyed, build somewhere near our first unit alive
            if (nearTo == null) {
                nearTo = Select.our().first().getPosition();
            }

            // Hopeless case, all units have died, just quit.
            if (nearTo == null) {
                return null;
            }

            if (maxDistance < 0) {
                maxDistance = 50;
            }

            // =========================================================
            // Standard place
            
            return findStandardPosition(builder, building, nearTo, maxDistance);
        }
    }

}
