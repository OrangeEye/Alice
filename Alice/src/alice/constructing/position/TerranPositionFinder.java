package alice.constructing.position;

import alice.information.AMap;
import alice.position.APosition;
import alice.units.AUnitType;

public class TerranPositionFinder extends AbstractPositionFinder {
	
	/**
     * Returns best position for given <b>building</b>, maximum <b>maxDistance</b> build tiles from
     * <b>nearTo</b>
     * position.<br />
     * It checks if buildings aren't too close one to another and things like that.
     *
     */
    public static APosition findStandardPositionFor( AUnitType building, APosition nearTo,
            double maxDistance) {
        _CONDITION_THAT_FAILED = null;

        // =========================================================
//        int searchRadius = building.equals(AUnitType.Terran_Supply_Depot) ? 0 : 0;
        int searchRadius = 0;

        while (searchRadius < maxDistance) {
            int xCounter = 0;
            int yCounter = 0;
            int doubleRadius = searchRadius * 2;

            // Search horizontally
            int minTileX = Math.max(0, nearTo.getTileX() - searchRadius);
            int maxTileX = Math.min(nearTo.getTileX() + searchRadius, AMap.getMapWidthInTiles() - 1);
            for (int tileX = minTileX; tileX <= maxTileX; tileX++) {
                
                // Search veritcally
                int minTileY = Math.max(0, nearTo.getTileY() - searchRadius);
                int maxTileY = Math.min(nearTo.getTileY() + searchRadius, AMap.getMapHeightInTiles() - 1);
                for (int tileY = minTileY; tileY <= maxTileY; tileY++) {
                    if (xCounter == 0 || yCounter == 0 || xCounter == doubleRadius || yCounter == doubleRadius) {
//                        System.out.println(tileX + " / " + tileY);
                        APosition constructionPosition = APosition.create(tileX, tileY);
                        
                        // Check if position is buildable etc
                        if (doesPositionFulfillAllConditions(building, constructionPosition)) {
                            return constructionPosition;
                        }
                    }

                    yCounter++;
                }
                xCounter++;
            }

            searchRadius++;
        }
        
        System.err.println("Finished at search radius: " + searchRadius + " from " + nearTo);
        System.err.println("   (Last error: " + AbstractPositionFinder._CONDITION_THAT_FAILED + ")");

        return null;
    }

}
