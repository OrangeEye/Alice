package alice;

import bwapi.Player;
import bwapi.Race;

/**
 * Repräsentiert verschiedene Aspekte aus dem Spiel wie Spielzeit, Supply, 
 * Spielgeschwindigkeit, Gegnerischer Spieler etc.
 * @author Lasse
 *
 */
public class AGame {
	
	private static double supplyTotal; //Maximale Versorgung (200)
	private static double supplyUsed;//Versorgung in Benutzung (200)
	
	/**
     * Number of supply totally available.
     */
    public static double getSupplyTotal() {
        return supplyTotal;
    }
    
    /**
     * Returns true if user plays as Terran.
     */
    public static boolean playsAsTerran() {
        return AliceConfig.MY_RACE.equals(Race.Terran);
    }


    /**
     * Returns true if user plays as Zerg.
     */
    public static boolean playsAsZerg() {
        return AliceConfig.MY_RACE.equals(Race.Zerg);
    }
    
    public static double getSupplyUsed() {
        return supplyUsed;
    }
    
    public static double getSupplyFree() {
        return supplyTotal - supplyUsed;
    }
    
    /**
     * Returns current player.
     */
    public static Player getPlayerUs() {
        return Alice.getBwapi().self();
    }
}
