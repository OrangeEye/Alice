package alice.units;

import java.util.HashMap;
import java.util.Map;

import alice.AGame;
import alice.units.action.UnitAction;
import bwapi.Player;
import bwapi.Unit;

public class AUnit implements AUnitOrders {

	// Mapping of native unit IDs to AUnit objects
	private static final Map<Integer, AUnit> listAUnit = new HashMap<Integer, AUnit>();
	private Unit unit;
	private boolean isRepairableMechanically;
	private boolean isHealable;
	private boolean isMilitaryBuildingAntiGround;
	private boolean isMilitaryBuildingAntiAir;
	private AUnitType lastCachedType; //der zuletzt gespeichertete AUnitType f�r diese Einheit
	private boolean isWorker;
	private UnitAction unitAction;
	
	
	@Override
	public Unit u() { 
		// TODO Auto-generated method stub
		return unit;
	}
	
	/**
     * This method exists only to allow reference in UnitActions class.
     */
	@Override
	public AUnit unit() {
		// TODO Auto-generated method stub
		return this;
	}

	public static AUnit addUnit(Unit u) {
		if (u == null) {
			throw new RuntimeException("AUnit constructor: unit is null");
		}

		if (listAUnit.containsKey(u.getID())) {
			return listAUnit.get(u.getID());
		} else {
			AUnit unit = new AUnit(u);
			listAUnit.put(u.getID(), unit);
			return unit;
		}
	}

	private AUnit(Unit u) {
		if (u == null) {
			throw new RuntimeException("AUnit constructor: unit is null");
		}

		this.unit = u;
		// this.innerID = firstFreeID++;

		// Cached type helpers
		refreshType();

		// Repair & Heal
		this.isRepairableMechanically = isBuilding() || isVehicle();
		this.isHealable = isInfantry() || isWorker();

		// Military building
		this.isMilitaryBuildingAntiGround = isType(AUnitType.Terran_Bunker, AUnitType.Protoss_Photon_Cannon,
				AUnitType.Zerg_Sunken_Colony);
		this.isMilitaryBuildingAntiAir = isType(AUnitType.Terran_Bunker, AUnitType.Terran_Missile_Turret,
				AUnitType.Protoss_Photon_Cannon, AUnitType.Zerg_Spore_Colony);
	}

	public void refreshType() {
		lastCachedType = AUnitType.addUnitType(unit.getType());
		isWorker = isType(AUnitType.Terran_SCV, AUnitType.Protoss_Probe, AUnitType.Zerg_Drone);
	}
	
	

	/**
	 * �berpr�ft ob diese Einheit einer der Einheitentypen entspricht
	 */
	public boolean isType(AUnitType... types) {
		return getType().isType(types);
	}
	
	public boolean isBuilding() {
		if(getType().isBuilding())
			return true;
		else
			return false;
	}
	
	public boolean isInfantry() {
		if(getType().isInfantry())
			return true;
		else
			return false;
	}
	
	public boolean isVehicle() {
		if(getType().isVehicle())
			return true;
		else
			return false;
	}
	
	public boolean isWorker() {
		if(getType().isWorker())
			return true;
		else
			return false;
	}
	
	public boolean isCompleted() {
        return unit.isCompleted();
    }
	
	public boolean isIdle() {
		return unit.isIdle();
	}
	
	public boolean isGatheringMinerals() {
        return unit.isGatheringMinerals();
    }

    public boolean isGatheringGas() {
        return unit.isGatheringGas();
    }

	/**
	 * Gibt den EinheitenTyp der Einheit oder Unkown zur�ck (Durch Kriegsnebel) und 
	 * aktualisiert den zuletzt gespeicherten EinheitenTyp
	 */
	public AUnitType getType() {
		AUnitType type = AUnitType.addUnitType(unit.getType());
		if (AUnitType.Unknown.equals(type)) {
			if (this.isOurUnit()) {
				System.err.println("Our unit (" + this + ") returned Unknown type");
			}
			return lastCachedType;
		} else {
			lastCachedType = type;
			return type;
		}
	}

	/**
	 * Pr�ft ob die Einheit uns geh�rt
	 * @return
	 */
	private boolean isOurUnit() {
		//if(getPlayer().equals(AGame.getPlayerUs()))
		if (getPlayer().getID() == AGame.getPlayerUs().getID())
			return true;
		else
			return false;
	}
	
	public void setUnitAction(UnitAction unitAction) {
        this.unitAction = unitAction;
    }
	
	/**
	 * Gibt den Spieler zur�ck, dem diese Einheit geh�rt
	 * @return
	 */
	public Player getPlayer() {
        return unit.getPlayer();
    }



	

}
