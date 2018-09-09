package alice.units;

import java.util.*;

import alice.AGame;
import alice.AliceConfig;
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

	private static HashMap<Integer, AUnit> allUnits = new HashMap<Integer, AUnit>();

	private static HashMap<Integer, AUnit> ourUnits = new HashMap<Integer, AUnit>();
	private static HashMap<Integer, AUnit> ourWorkers = new HashMap<Integer, AUnit>();
	private static HashMap<Integer, AUnit> ourDestroyedUnits = new HashMap<Integer, AUnit>();
	private static HashMap<Integer, AUnit> neutralUnits = new HashMap<Integer, AUnit>();
	private static HashMap<Integer, AUnit> mineralFields = new HashMap<Integer, AUnit>();
	private static HashMap<Integer, AUnit> ourBuildings = new HashMap<Integer, AUnit>();
	private static HashMap<Integer, AUnit> ourLarvas = new HashMap<Integer, AUnit>();
	private static HashMap<Integer, AUnit> ourOverlords = new HashMap<Integer, AUnit>();
	private static HashMap<Integer, AUnit> gasFields = new HashMap<Integer, AUnit>();

	/*
	 * private static List<AUnit> ourUnits = new ArrayList<AUnit>(); // Alle unsere
	 * Einheiten / Gebäude private static List<AUnit> ourWorkers = new
	 * ArrayList<AUnit>(); // Alle unsere Arbeiter private static List<AUnit>
	 * ourBuildings = new ArrayList<AUnit>(); private static List<AUnit> ourLarvas =
	 * new ArrayList<AUnit>(); private static List<AUnit> neutralUnits = new
	 * ArrayList<AUnit>(); private static List<AUnit> ourDestroyedUnits = new
	 * ArrayList<AUnit>(); private static List<AUnit> mineralFields = new
	 * ArrayList<AUnit>(); private static List<AUnit> gasFields = new
	 * ArrayList<AUnit>();
	 */
	/*
	 * private static List<AUnit> ourUnits = new ArrayList<AUnit>(); // Alle unsere
	 * Einheiten / Gebäude private static List<AUnit> ourWorkers = new
	 * ArrayList<AUnit>(); // Alle unsere Arbeiter private static List<AUnit>
	 * ourBuildings = new ArrayList<AUnit>(); private static List<AUnit> ourLarvas =
	 * new ArrayList<AUnit>(); private static List<AUnit> neutralUnits = new
	 * ArrayList<AUnit>(); private static List<AUnit> ourDestroyedUnits = new
	 * ArrayList<AUnit>(); private static List<AUnit> mineralFields = new
	 * ArrayList<AUnit>(); private static List<AUnit> gasFields = new
	 * ArrayList<AUnit>();
	 */


	private HashMap<Integer, AUnit> listSelectedAUnits = new HashMap<Integer, AUnit>();

	// Constructor is private, use our(), enemy() or neutral() methods
	protected Select(HashMap<Integer, AUnit> unitsData) {

		for (AUnit unit : unitsData.values()) {
			listSelectedAUnits.put(unit.getID(), unit);
		}

	}

	public static void addNewUnit(Unit unit) {
		AUnit newUnit = new AUnit(unit);
		/**
		 * Wenn die Einheit noch nicht aufgenommen wurde, wird AUnit erstellt und in den
		 * Listen eingetragen. Sollte sich der AUnitType geändert haben werden die
		 * Einträge geändert
		 */
		if (!allUnits.containsKey(unit.getID())) {
			allUnits.put(newUnit.getID(), newUnit);
		} else if (!allUnits.get(unit.getID()).getType().getUnitType().equals(unit.getType())) {
			validateUnit(newUnit);
		} 
		else initialize(newUnit);


	}

	public static Select our() {
		return new Select(ourUnits);
	}

	private static HashMap<Integer, AUnit> ourUnits() {
		return ourUnits;
	}

	public static HashMap<Integer, AUnit> ourWorkers() {
		return ourWorkers;
	}

	public static HashMap<Integer, AUnit> allUnits() {
		return allUnits;
	}

	public static Select neutral() {
		return new Select(neutralUnits);
	}

	public static HashMap<Integer, AUnit> getOurDestroyedUnits() {
		return ourDestroyedUnits;
	}

	public static HashMap<Integer, AUnit> getOurLarvas() {
		return ourLarvas;
	}

	private static void validateUnit(AUnit changedUnit) {
		
		
		allUnits.replace(changedUnit.getID(), changedUnit);
		ourUnits.replace(changedUnit.getID(), changedUnit); 
		neutralUnits.replace(changedUnit.getID(), changedUnit);
		
		ourWorkers.remove(changedUnit.getID());	
		ourLarvas.remove(changedUnit.getID());
		ourBuildings.remove(changedUnit.getID());
		ourOverlords.remove(changedUnit.getID());
		
		if (changedUnit.getPlayer().equals(AGame.getPlayerUs())) {
			if (changedUnit.isType(AliceConfig.WORKER))
				ourWorkers.put(changedUnit.getID(), changedUnit);
			if (changedUnit.isType(AUnitType.Zerg_Larva))
				ourLarvas.put(changedUnit.getID(), changedUnit);
			if(changedUnit.isBuilding())
				ourBuildings.put(changedUnit.getID(), changedUnit);
			if (changedUnit.isType(AUnitType.Zerg_Overlord))
				ourOverlords.put(changedUnit.getID(), changedUnit);
		}
		
	}
	
	private static void initialize(AUnit newUnit) {
		// Wenn es unsere Unit ist
		if (newUnit.getPlayer().equals(AGame.getPlayerUs())) {
			ourUnits.put(newUnit.getID(), newUnit);

			if (newUnit.isType(AliceConfig.WORKER))
				ourWorkers.put(newUnit.getID(), newUnit);
			if (newUnit.isType(AUnitType.Zerg_Larva))
				ourLarvas.put(newUnit.getID(), newUnit);
			if(newUnit.isBuilding())
				ourBuildings.put(newUnit.getID(), newUnit);
			if (newUnit.isType(AUnitType.Zerg_Overlord))
				ourOverlords.put(newUnit.getID(), newUnit);
		}

		if (newUnit.getPlayer().equals(AGame.getPlayerNeutral())) {
			neutralUnits.put(newUnit.getID(), newUnit);
			if (newUnit.isType(AUnitType.Resource_Mineral_Field, AUnitType.Resource_Mineral_Field_Type_2,
					AUnitType.Resource_Mineral_Field_Type_3))
				mineralFields.put(newUnit.getID(), newUnit);
			else if (newUnit.isType(AUnitType.Resource_Vespene_Geyser))
				gasFields.put(newUnit.getID(), newUnit);
		}
	}

	/**
	 * Selects our unfinished units.
	 */
	public static Select ourNotFinished() {
		// Units units = new AUnits();
		HashMap<Integer, AUnit> data = new HashMap<Integer, AUnit>();

		for (AUnit unit : ourUnits().values()) {

			if (!unit.isCompleted()) {
				data.put(unit.getID(), unit);
			}
		}

		return new Select(data);
	}

	/**
	 * Gibt nur die Einheiten von dem Parameter zurück. Wurde überarbeitet, bei
	 * Fehlern nochmal bei Atlantis reinschauen
	 */
	public Select ofType(AUnitType... types) {
		Iterator<AUnit> it = listSelectedAUnits.values().iterator();
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
		Iterator<AUnit> it = listSelectedAUnits.values().iterator();
		while (it.hasNext()) {
			AUnit nextUnit = it.next();
			if (!nextUnit.isBuilding())
				it.remove();
		}
		return this;
	}

	/**
	 * Selects only those units which are idle. Idle is unit's class flag so be
	 * careful with that.
	 */
	public static List<AUnit> idle(Collection<AUnit> unitList) {
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
	public static HashMap<Integer, AUnit> mineralFields() {
		return mineralFields;
	}
	
	public static HashMap<Integer, AUnit> getOurOverlords() {
		return ourOverlords;
	}

	/**
	 * Gibt alle Mineralienfelder zurück, die an unserer Basis angrenzen
	 * 
	 * @return
	 */
	public static HashMap<Integer, AUnit> ourMineralFields() {
		HashMap<Integer, AUnit> ourMineralFields = new HashMap<Integer, AUnit>();

		for (AUnit mineralField : mineralFields().values()) {
			for (AUnit base : ourBases().listSelectedAUnits.values()) {
				if (mineralField.isInRangeTo(base, 250)) {
					ourMineralFields.put(mineralField.getID(), mineralField);
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
		for (AUnit mineralField : ourMineralFields().values()) {
			if (mineralField.countGatherer() < 2)
				return mineralField;
		}
		return null;
	}

	/**
	 * Gibt alle unsere Arbeiter zurück, die Mineralien sammeln
	 */
	public Select ourMiningMineralsWorkers() {
		Iterator<AUnit> unitsIterator = listSelectedAUnits.values().iterator();
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
		for (AUnit worker : ourWorkers().values()) {
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
		for (AUnit unit : this.listSelectedAUnits.values()) {
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
		Iterator<AUnit> it = this.listSelectedAUnits.values().iterator();
		while (it.hasNext()) {
			AUnit nextUnit = it.next();
			if (nextUnit.getDistance(target.getPosition()) > radius) {
				it.remove();
			}
		}
		return this;
	}

	/**
	 * 
	 */
	public HashMap<Integer, AUnit> listUnits() {
		return this.listSelectedAUnits;
	}

	public static HashMap<Integer, AUnit> getGasFields() {
		return gasFields;
	}

	/**
	 * Returns first unit that matches previous conditions or null if no units match
	 * conditions.
	 */
	public AUnit first() {
		return listSelectedAUnits.isEmpty() ? null : listSelectedAUnits.get(0);
	}

	public static AUnit first(HashMap<Integer, AUnit> list) {
		if (list != null && list.size() > 0)
			for (AUnit next : list.values()) {
				return next;
			}

		return null;
	}

	/**
	 * Returns number of units matching all previous conditions.
	 */
	public int count() {
		return listSelectedAUnits.size();
	}

}
