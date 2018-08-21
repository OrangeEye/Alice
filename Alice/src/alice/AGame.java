package alice;


/**
 * Repräsentiert verschiedene Aspekte aus dem Spiel wie Spielzeit, Supply, 
 * Spielgeschwindigkeit, Gegnerischer Spieler etc.
 * @author Lasse
 *
 */
public class AGame {

	/**
     * Number of supply totally available.
     */
    public static int getSupplyTotal() {
        return Alice.getBwapi().self().supplyTotal() / 2;
    }
}
