package alice.scout;

import java.util.ArrayList;

import alice.units.AUnit;

public class AScoutManager {
	/**
     * Current scout unit.
     */
    private static ArrayList<AUnit> scouts = new ArrayList<>();
	
	/**
     * Returns true if given unit has been assigned to explore the map.
     */
    public static boolean isScout(AUnit unit) {
        return scouts.contains(unit);
    }
}
