package alice.enemy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import alice.information.AFoggedUnit;
import alice.units.AUnit;



public class AEnemyUnits {
	protected static Map<AUnit, AFoggedUnit> enemyUnitsDiscovered = new HashMap<>();
    protected static ArrayList<AUnit> enemyUnitsDestroyed = new ArrayList<>();
    
    /**
     * Saves information about enemy unit that we see for the first time.
     */
    public static void discoveredEnemyUnit(AUnit enemyUnit) {
        enemyUnitsDiscovered.put(enemyUnit, new AFoggedUnit(enemyUnit));
    }
    
    /**
     * Saves information about given unit being destroyed, so counting units works properly.
     */
    public static void unitDestroyed(AUnit enemyUnit) {
        enemyUnitsDiscovered.remove(enemyUnit);
        enemyUnitsDestroyed.add(enemyUnit);
    }
    
    /**
     * Returns <b>true</b> if enemy unit has been destroyed and we know it.
     */
    public static boolean isEnemyUnitDestroyed(AUnit enemyUnit) {
        return enemyUnitsDestroyed.contains(enemyUnit);
    }
    

}
