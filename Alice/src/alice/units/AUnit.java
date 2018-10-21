package alice.units;

import java.util.ArrayList;
import alice.AGame;
import alice.position.APosition;
import alice.production.AOrder;
import alice.production.orders.AZergBuildOrder;
import alice.repair.ARepairManager;
import bwapi.Order;
import bwapi.Player;
import bwapi.Unit;

public class AUnit implements AUnitOrders {

	// Mapping of native unit IDs to AUnit objects
	
	private Unit unit;
	private int ID=-1;
	private boolean isRepairableMechanically;
	private boolean isHealable;
	private boolean isMilitaryBuildingAntiGround;
	private boolean isMilitaryBuildingAntiAir;
	private AUnitType lastCachedType; //der zuletzt gespeichertete AUnitType für diese Einheit
	private boolean isWorker;
	private long timerStart;
	private ArrayList<AUnit> gatherer = new ArrayList<AUnit>();
	private AUnitOrder unitOrder = AUnitOrder._IDLE; 
	
	
	


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


	public AUnit(Unit u) {
		if (u == null) {
			throw new RuntimeException("AUnit constructor: unit is null");
		}

		this.unit = u;
		// this.innerID = firstFreeID++;

		// Cached type helpers
		refreshType();
		this.ID=u.getID();
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
		lastCachedType = AUnitType.getAUnitType(unit.getType());
		isWorker = isType(AUnitType.Terran_SCV, AUnitType.Protoss_Probe, AUnitType.Zerg_Drone);
	}
	
	public int countGatherer() {
		return this.gatherer.size();
	}
	
	public int getID() {
		return this.ID;
	}

	/**
	 * Überprüft ob diese Einheit einer der Einheitentypen entspricht
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
	
	public boolean isMorphing() {
		return this.unit.isMorphing();
	}
	
	public boolean cancelConstruction() {
		AZergBuildOrder.setOrderStatus(this.getType(), this, AOrder.STATUS_FINISHED); //Anpassen, damit die neu Gebaut werden
		return this.unit.cancelConstruction();
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
		return this.getUnitOrder().equals(AUnitOrder._IDLE);
	}
	
	public boolean isGatheringMinerals() {
        return unit.isGatheringMinerals();
    }

    public boolean isGatheringGas() {
        return unit.isGatheringGas();
    } 
    
    public boolean isMineralField() {
    	if(this.isType(AUnitType.Resource_Mineral_Field, AUnitType.Resource_Mineral_Field_Type_2,
				AUnitType.Resource_Mineral_Field_Type_3, AUnitType.Resource_Vespene_Geyser))
    		return true;
    	else return false;
    }
    
    public int supplyProvided() {
    	return this.getType().getUnitType().supplyProvided();
    }
    
    public int supplyRequired() {
    	return this.getType().getUnitType().supplyRequired();
    }
    
    public int getDistance(APosition target) {
    	return unit.getDistance(target.getPosition());
    }
    
    public APosition getPosition() {
    	return APosition.createFromPosition(unit.getPosition());
    }
    
	public ArrayList<AUnit> getGatherer() {
		return gatherer;
	}
	
	public APosition getOrderTargetPosition() {
		return new APosition(unit.getOrderTargetPosition());
	}

    public boolean isConstructing() {
    	return unit.isConstructing();
    }
    
    public boolean isRepairing() {
    	return unit.isRepairing();
    }
    

    
    public boolean isRepairerOfAnyKind() {
        return ARepairManager.isRepairerOfAnyKind(this);
    }
    
    public AUnitOrder getUnitOrder() {
		return unitOrder;
	}

	public void setUnitOrder(AUnitOrder unitOrder) {
		this.unitOrder = unitOrder;
	}

	public boolean isAlive() { //funktioniert wahrscheinlich nicht auf feindliche Einheiten
//      return getHP() > 0 && !AtlantisEnemyUnits.isEnemyUnitDestroyed(this);
      return isExists() &&  !Select.getOurDestroyedUnits().containsKey(this.getID());
  }
    
    public AUnitType getBuildType() {
        return unit.getBuildType() != null ? AUnitType.getAUnitType(unit.getBuildType()) : null;
    }
    
    public boolean isExists() {
    	return unit.exists();
    }
    


	/**
	 * Gibt den EinheitenTyp der Einheit oder Unkown zurück (Durch Kriegsnebel) und 
	 * aktualisiert den zuletzt gespeicherten EinheitenTyp
	 */
	public AUnitType getType() {
		 /* AUnitType type = AUnitType.getAUnitType(unit.getType());
		if (AUnitType.Unknown.equals(type)) {
			if (this.isOurUnit()) {
				System.err.println("Our unit (" + this + ") returned Unknown type");
			}
			return lastCachedType;
		} else {
			lastCachedType = type;
			return type;
		} */
		return this.lastCachedType;
	}
	

	
	public void setGatherer(AUnit worker) {
		this.gatherer.add(worker);
	}
	
	public long getTimerStart() {
		return timerStart;
	}

	public void setTimerStart(long timerStart) {
		this.timerStart = timerStart;
	}


	/**
	 * Prüft ob die Einheit uns gehört
	 * @return
	 */
	public boolean isOurUnit() {
		//if(getPlayer().equals(AGame.getPlayerUs()))
		if (getPlayer().getID() == AGame.getPlayerUs().getID())
			return true;
		else
			return false;
	}
	
	public boolean isInRangeTo(AUnit target, int radius) {
		if(this.getDistance(target.getPosition()) <= radius)
			return true;
		else 
			return false;
	}
	
	
	public Order getOrder() {
		return unit.getOrder();
	}
	
	public AUnit getTarget() {
		return unit.getTarget() != null  ? Select.allUnits().get(unit.getTarget().getID()) :  null;	
	}
	
	/**
	 * Gibt den Spieler zurück, dem diese Einheit gehört
	 * @return
	 */
	public Player getPlayer() {
        return unit.getPlayer();
    }
	

	@Override
	public String toString() {
		return this.getType().toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
		return this.getID() == ((AUnit) obj).getID();
	}

	

}
