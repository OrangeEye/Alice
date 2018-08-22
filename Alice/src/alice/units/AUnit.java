package alice.units;

import java.util.HashMap;
import java.util.Map;
import bwapi.Unit;

public class AUnit {
	
	// Mapping of native unit IDs to AUnit objects
    private static final Map<Integer, AUnit> unitList = new HashMap<Integer, AUnit>();
    private Unit unit;
    private boolean isRepairableMechanically;
    private boolean isHealable;
    private boolean isMilitaryBuildingAntiGround;
    private boolean isMilitaryBuildingAntiAir;
    private boolean lastCachedType;
    private boolean isWorker;
	
	public static AUnit addUnit(Unit u) {
        if (u == null) {
            throw new RuntimeException("AUnit constructor: unit is null");
        }

        if (unitList.containsKey(u.getID())) {
            return unitList.get(u.getID());
        } else {
            AUnit unit = new AUnit(u);
            unitList.put(u.getID(), unit);
            return unit;
        }
    }
	
	private AUnit(Unit u) {
        if (u == null) {
            throw new RuntimeException("AUnit constructor: unit is null");
        }

        this.unit = u;
//        this.innerID = firstFreeID++;
        
        // Cached type helpers
        refreshType();

        // Repair & Heal
        this.isRepairableMechanically = isBuilding() || isVehicle();
        this.isHealable = isInfantry() || isWorker();

        // Military building
        this.isMilitaryBuildingAntiGround = isType(
                AUnitType.Terran_Bunker, AUnitType.Protoss_Photon_Cannon, AUnitType.Zerg_Sunken_Colony
        );
        this.isMilitaryBuildingAntiAir = isType(
                AUnitType.Terran_Bunker, AUnitType.Terran_Missile_Turret,
                AUnitType.Protoss_Photon_Cannon, AUnitType.Zerg_Spore_Colony
        );
    }
	
	public void refreshType() {
        lastCachedType = AUnitType.createFrom(u.getType());
        isWorker = isType(AUnitType.Terran_SCV, AUnitType.Protoss_Probe, AUnitType.Zerg_Drone);
    }
	
	public boolean isType(AUnitType... types) {
        return getType().isType(types);
    }
	
	/**
     * Returns unit type from BWMirror OR if type is Unknown (behind fog of war) it will return last cached
     * type.
     */
    public AUnitType getType() {
        AUnitType type = AUnitType.createFrom(u.getType());
        if (AUnitType.Unknown.equals(type)) {
            if (this.isOurUnit()) {
                System.err.println("Our unit (" + this + ") returned Unknown type");
            }
            return _lastCachedType;
        } else {
            _lastCachedType = type;
            return type;
        }
    }

}
