package alice.repair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import alice.units.AUnit;

public class ARepairManager {
	 // Bunker repairers
    protected static Map<AUnit, AUnit> repairersConstantToBunker = new HashMap<>();
    protected static Map<AUnit, ArrayList<AUnit>> bunkersToRepairers = new HashMap<>();
    
    // Unit repairers
    protected static Map<AUnit, AUnit> repairersToUnit = new HashMap<>();
    protected static Map<AUnit, ArrayList<AUnit>> unitsToRepairers = new HashMap<>();
	
	public static boolean isRepairerOfAnyKind(AUnit worker) {
        return repairersConstantToBunker.containsKey(worker) || repairersToUnit.containsKey(worker);
    }
	
}
