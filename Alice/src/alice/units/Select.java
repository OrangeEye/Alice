package alice.units;

import java.util.*;

import alice.AGame;
import alice.AliceConfig;
import alice.constructing.AConstructionManager;
import alice.position.APosition;
import alice.scout.AScoutManager;
import alice.units.AUnit;
import bwapi.Position;
import bwapi.Unit;
import bwapi.UnitType;

/**
 * Diese Klasse ermöglicht es einfach Einheiten auszuwählen zB. einen Marine der
 * am nähesten zu einer bestimmten Position steht. Vom Prinzip her ist die
 * Klasse eine Liste von Einheiten
 */
public class Select {

	private static Map<Integer, AUnit> allUnits = new HashMap<Integer, AUnit>();
	/*
	 * private static Map<Integer, AUnit> ourUnits = new HashMap<Integer, AUnit>();
	 * private static Map<Integer, AUnit> ourWorkers = new HashMap<Integer,
	 * AUnit>(); private static Map<Integer, AUnit> ourDestroyedUnits = new
	 * HashMap<Integer, AUnit>(); private static Map<Integer, AUnit> neutralUnits =
	 * new HashMap<Integer, AUnit>(); private static Map<Integer, AUnit>
	 * mineralFields = new HashMap<Integer, AUnit>();
	 */
	private static List<AUnit> ourUnits= new ArrayList<AUnit>(); // Alle unsere Einheiten / Gebäude
	private static List<AUnit> ourWorkers= new ArrayList<AUnit>(); // Alle unsere Arbeiter
	private static List<AUnit> ourBuildings= new ArrayList<AUnit>();
	private static List<AUnit> neutralUnits= new ArrayList<AUnit>();
	private static List<AUnit> ourDestroyedUnits= new ArrayList<AUnit>();
	private static List<AUnit> mineralFields= new ArrayList<AUnit>();

	// CACHED variables
	private static AUnit _cached_mainBase = null;

	private List<AUnit> listSelectedAUnits = new ArrayList<AUnit>();

	// Constructor is private, use our(), enemy() or neutral() methods
	protected Select(Collection<AUnit> unitsData) {
		listSelectedAUnits.addAll(unitsData);
	}

	public static void addNewUnit(Unit unit) {
		AUnit newUnit;
		if (!allUnits.containsKey(unit.getID())) {
			newUnit = new AUnit(unit);
			allUnits.put(newUnit.getID(), newUnit);
		} else
			return;
		
		if (newUnit.getPlayer().equals(AGame.getPlayerUs())) {
			ourUnits.add(newUnit);
			if (newUnit.isType(AliceConfig.WORKER))
				ourWorkers.add(newUnit);
		}
		

		if (newUnit.getPlayer().equals(AGame.getPlayerNeutral())) {
			neutralUnits.add(newUnit);
			if (newUnit.isType(AUnitType.Resource_Mineral_Field, AUnitType.Resource_Mineral_Field_Type_2,
					AUnitType.Resource_Mineral_Field_Type_3, AUnitType.Resource_Vespene_Geyser))
				mineralFields.add(newUnit);
		}
	}

	public static Select our() {
		return new Select(ourUnits);
	}

	private static List<AUnit> ourUnits() {
		return ourUnits;
	}

	public static List<AUnit> ourWorkers() {
		return ourWorkers;
	}
	
	public static Map<Integer, AUnit> allUnits(){
		return allUnits;
	}

	public static Select neutral() {
		return new Select(neutralUnits);
	}

	public static List<AUnit> getOurDestroyedUnits() {
		return ourDestroyedUnits;
	}

	/**
	 * Selects our unfinished units.
	 */
	public static Select ourNotFinished() {
		// Units units = new AUnits();
		List<AUnit> data = new ArrayList<AUnit>();

		for (AUnit unit : ourUnits()) {

			if (!unit.isCompleted()) {
				data.add(unit);
			}
		}

		return new Select(data);
	}

	/**
	 * Gibt nur die Einheiten von dem Parameter zurück. Wurde überarbeitet, bei
	 * Fehlern nochmal bei Atlantis reinschauen
	 */
	public Select ofType(AUnitType... types) {
		Iterator<AUnit> it = listSelectedAUnits.iterator();
		while (it.hasNext()) {
			AUnit nextUnit = it.next();
			boolean missed = true;
			for (AUnitType type : types) {
				if (nextUnit.getType().equals(type)) {
					missed = false;
					break;
				}
			}
			if (missed)
				it.remove();
		}
		return this;
	}

	public static Select ourBases() {

		if (AGame.playsAsZerg()) {
			return our().ofType(AUnitType.Zerg_Hatchery, AUnitType.Zerg_Lair, AUnitType.Zerg_Hive,
					AUnitType.Protoss_Nexus, AUnitType.Terran_Command_Center);
		} else {
			return our().ofType(AliceConfig.BASE);
		}
	}

	/**
	 * Selects all our finished buildings.
	 */
	public static Select ourBuildings() {
		return our().buildings();
	}

	public Select buildings() {
		Iterator<AUnit> it = listSelectedAUnits.iterator();
		while (it.hasNext()) {
			AUnit nextUnit = it.next();
			if (!nextUnit.isBuilding())
				listSelectedAUnits.remove(nextUnit);
		}
		return this;
	}

	/**
	 * Selects only those units which are idle. Idle is unit's class flag so be
	 * careful with that.
	 */
	public static List<AUnit> idle(List<AUnit> unitList) {
		ArrayList<AUnit> idleUnits = new ArrayList<AUnit>();
		for (AUnit unit : unitList) {
			if (unit.isIdle()) {
				idleUnits.add(unit);
			}
		}
		return idleUnits;
	}

	/**
	 * Gibt alle Mineralien auf der Map zurück
	 * 
	 * @return
	 */
	public static List<AUnit> mineralFields() {
		return mineralFields;

		/*
		 * return Select.neutral().ofType(AUnitType.Resource_Mineral_Field,
		 * AUnitType.Resource_Mineral_Field_Type_2,
		 * AUnitType.Resource_Mineral_Field_Type_3);
		 */
	}

	/**
	 * Gibt alle Mineralienfelder zurück, die an unserer Basis angrenzen
	 * 
	 * @return
	 */
	public static List<AUnit> ourMineralFields() {
		ArrayList<AUnit> ourMineralFields = new ArrayList<AUnit>();

		for (AUnit mineralField : mineralFields()) {
			for (AUnit base : ourBases().listSelectedAUnits) {
				if (mineralField.isInRangeTo(base, 250)) {
					ourMineralFields.add(mineralField);
					break;
				}
			}
		}
		return ourMineralFields;

		/*
		 * Select mineralFields = mineralFields(); Iterator<AUnit> unitsIterator =
		 * mineralFields.listSelectedAUnits.iterator(); while (unitsIterator.hasNext())
		 * { AUnit mineralField = unitsIterator.next(); boolean isOurMineralField =
		 * false; for (AUnit base : ourBases().listSelectedAUnits) { if
		 * (mineralField.isInRangeTo(base, 12)) { isOurMineralField = true; break; } }
		 * if (!isOurMineralField) unitsIterator.remove(); } return mineralFields;
		 */
	}

	/**
	 * Gibt ein Mineralienfeld zurück auf dem weniger als 2 Arbeiter beschäftigt
	 * sind
	 * 
	 * @return
	 */
	public static AUnit freeMineralField() {
		for (AUnit mineralField : ourMineralFields()) {
			if (mineralField.countGatherer() < 2)
				return mineralField;
		}
		return null;
	}

	/**
	 * Gibt alle unsere Arbeiter zurück, die Mineralien sammeln
	 */
	public Select ourMiningMineralsWorkers() {
		Iterator<AUnit> unitsIterator = listSelectedAUnits.iterator();
		while (unitsIterator.hasNext()) {
			AUnit unit = unitsIterator.next();
			if (!unit.isGatheringMinerals()) // TODO Überprüfen ob alle Arbeiter auch den Status Mining Minerals haben
				unitsIterator.remove();
		}
		return this;
	}

	/**
	 * Selects our workers that are free to construct building or repair a unit.
	 * That means they mustn't repait any other unit or construct other building.
	 */
	public static List<AUnit> ourWorkersFreeToBuildOrRepair() {

		ArrayList<AUnit> selectedUnits = new ArrayList<AUnit>();
		for (AUnit worker : ourWorkers()) {
			if (worker.isGatheringMinerals()) {
				selectedUnits.add(worker);
			}
		}

		return selectedUnits;

		/*
		 * Iterator<AUnit> it = selectedUnits.listSelectedAUnits.iterator(); while
		 * (it.hasNext()) { AUnit unit = it.next(); if
		 * (AConstructionManager.isBuilder(unit) || unit.isRepairing() ||
		 * AScoutManager.isScout(unit) || unit.isRepairerOfAnyKind()) { it.remove(); } }
		 * 
		 * return selectedUnits;
		 */
	}

	/**
	 * Gibt die Einheit zurück die am nächsten ist oder die innerhalb des Radius ist
	 */
	public AUnit clostestOrInRadius(APosition target, int radius) {
		AUnit nextUnit = null;
		for (AUnit unit : this.listSelectedAUnits) {
			int distance = unit.getDistance(target);
			if (distance <= radius)
				return unit;
			else if (nextUnit == null || distance < nextUnit.getDistance(target))
				nextUnit = unit;
		}
		return nextUnit;
	}

	/**
	 * Gibt alle Einheiten zurück, die sich im Radius zu der gegebenen Einheit
	 * befinden
	 * 
	 * @param target
	 * @param radius
	 * @return
	 */
	public Select inRangeTo(AUnit target, int radius) {
		Iterator<AUnit> it = this.listSelectedAUnits.listIterator();
		while (it.hasNext()) {
			AUnit nextUnit = it.next();
			if (nextUnit.getDistance(target.getPosition()) > radius) {
				it.remove();
			}
		}
		return this;
	}

	/**
	 * Returns first unit being base. For your units this is most likely your main
	 * base, for enemy it will be first discovered base.
	 */
	public static AUnit mainBase() {
		if (_cached_mainBase == null || !_cached_mainBase.isAlive()) {
			List<AUnit> bases = ourBases().listUnits();
			_cached_mainBase = bases.isEmpty() ? Select.ourBuildings().first() : bases.get(0);
		}
		return _cached_mainBase;
	}

	/**
	 * 
	 */
	public List<AUnit> listUnits() {
		return this.listSelectedAUnits;
	}

	/**
	 * Returns first unit that matches previous conditions or null if no units match
	 * conditions.
	 */
	public AUnit first() {
		return listSelectedAUnits.isEmpty() ? null : listSelectedAUnits.get(0);
	}

	/**
	 * Returns number of units matching all previous conditions.
	 */
	public int count() {
		return listSelectedAUnits.size();
	}

}
