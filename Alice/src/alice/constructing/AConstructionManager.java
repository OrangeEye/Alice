package alice.constructing;

import java.util.concurrent.ConcurrentLinkedQueue;

import alice.AGame;
import alice.position.APosition;
import alice.production.ProductionOrder;
import alice.units.AUnit;
import alice.units.AUnitType;
import alice.units.Select;

public class AConstructionManager {

	/**
     * Liste aus allen unfertigen oder anstehenden Produktionen
     */
    private static ConcurrentLinkedQueue<ConstructionOrder> constructionOrders = new ConcurrentLinkedQueue<ConstructionOrder>();
	
	/**
	 * Zählt die nicht fertiggestellten Einheiten / Gebäude des Types zusammen
	 * @param type
	 * @return
	 */
	public static int countNotFinishedConstructionsOfType(AUnitType type) {
        return Select.ourNotFinished().ofType(type).count()
                + countNotStartedConstructionsOfType(type);
    }
	
	/**
     * If we requested to build building A and even assigned worker who's travelling to the building site,
     * it's still doesn't count as unitCreated. We need to manually count number of constructions and only
     * then, we can e.g. "count unstarted barracks constructions".
     */
    public static int countNotStartedConstructionsOfType(AUnitType type) {
        int total = 0;
        for (ConstructionOrder constructionOrder : constructionOrders) {
            if (constructionOrder.getStatus() == ConstructionOrderStatus.CONSTRUCTION_NOT_STARTED
                    && constructionOrder.getBuildingType().equals(type)) {
                total++;
            }
        }

        return total;
    }
    
    /**
     * Issues request of constructing new building. It will automatically find position and builder unit for
     * it.
     */
    public static void requestConstructionOf(AUnitType building) {
        requestConstructionOf(building, null, null);
    }
    
    /**
     * NOTICE/WARNING: passed order parameter can later override nearTo parameter.
     * 
     * Issues request of constructing new building. It will automatically find position and builder unit for
     * it.
     */
    public static void requestConstructionOf(AUnitType building, ProductionOrder order, APosition near) {
//        AGame.sendMessage("Request to build: " + building.getShortName());
        
        // Validate
        if (!building.isBuilding()) {
            throw new RuntimeException("Requested construction of not building!!! Type: " + building);
        }

        //prüft ob das Gebäude eine spezielle Behandlung bedarf und führt diese dann aus (zB ein Addon)
        if (ASpecificConstructionManager.handledAsSpecialBuilding(building, order)) {
            return;
        }

        // =========================================================
        // Create ConstructionOrder object, assign random worker for the time being
        ConstructionOrder newConstructionOrder = new ConstructionOrder(building);
        newConstructionOrder.setProductionOrder(order);
        newConstructionOrder.setNear(near);
        newConstructionOrder.assignNextBuilderForNow(near);

        if (newConstructionOrder.getBuilder() == null) {
                System.err.println("Builder is null, got damn it!");
            return;
        }

        // =========================================================
        // Find place for new building
//        APosition positionToBuild = AtlantisPositionFinder.getPositionForNew(
//                newConstructionOrder.getBuilder(), building, newConstructionOrder, near, 25
//        );
//        newConstructionOrder.setMaxDistance(32);
        newConstructionOrder.setMaxDistance(-1);
        APosition positionToBuild = newConstructionOrder.findNewBuildPosition();
//        AGame.sendMessage("@@ " + building + " at " + positionToBuild + " near " + near);
//        System.err.println("@@ " + building + " at " + positionToBuild + " near " + near);

        // =========================================================
        // Successfully found position for new building
        AUnit optimalBuilder = null;
        if (positionToBuild != null) {

            // Update construction order with found position for building
            newConstructionOrder.setPositionToBuild(positionToBuild);

            // Assign optimal builder for this building
            newConstructionOrder.assignOptimalBuilder();

            // Add to list of pending orders
            constructionOrders.add(newConstructionOrder);

            // Rebuild production queue as new building is about to be built
            ABuildOrderManager.rebuildQueue();
        } 

        // Couldn't find place for building! That's bad, print descriptive explanation.
        else {
            System.err.println("requestConstruction `" + building);
            if (AbstractPositionFinder._CONDITION_THAT_FAILED != null) {
                System.err.println("    (reason: " + AbstractPositionFinder._CONDITION_THAT_FAILED + ")");
            }
            System.err.println("");
        }
    }
}
