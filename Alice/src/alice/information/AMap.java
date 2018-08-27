package alice.information;

import alice.Alice;

public class AMap {

	
	/**
     * Returns map width in tiles.
     */
    public static int getMapWidthInTiles() {
        return Alice.getBwapi().mapWidth();
    }

    /**
     * Returns map height in tiles.
     */
    public static int getMapHeightInTiles() {
        return Alice.getBwapi().mapHeight();
    }
}
