package alice.units;

import java.util.*;

import alice.AGame;
import alice.constructing.AConstructionManager;
import alice.position.APosition;
import alice.scout.AScoutManager;
import alice.units.AUnit;
import bwapi.Position;
import bwapi.Unit;

/**
 * Diese Klasse ermöglicht es einfach Einheiten auszuwählen zB. einen Marine der
 * am nähesten zu einer bestimmten Position steht. Vom Prinzip her ist die
 * Klasse eine Liste von Einheiten
 */
public class Select {
	
	private static List<AUnit> ourUnits; // Alle unsere Einheiten / Gebäude
	private static List<AUnit> ourWorkers; // Alle unsere Arbeiter

	private List<AUnit> listSelectedAUnits;
	

	// Constructor is private, use our(), enemy() or neutral() methods
	protected Select(Collection<AUnit> unitsData) {
		listSelectedAUnits = new ArrayList<AUnit>();
		listSelectedAUnits.addAll(unitsData);
	}

	public static Select our() {
		return new Select(ourUnits);
	}

	private static List<AUnit> ourUnits() {
		return ourUnits;
	}

	public static Select ourWorkers() {
		return new Select(ourWorkers);
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
				listSelectedAUnits.remove(nextUnit);
		}
		return this;
	}
	
	public static Select ourBases() {

		if (AGame.playsAsZerg()) {
			return (Select<AUnit>) our().ofType(AUnitType.Zerg_Hatchery, AUnitType.Zerg_Lair, AUnitType.Zerg_Hive,
					AUnitType.Protoss_Nexus, AUnitType.Terran_Command_Center);
		} else {
			return (Select<AUnit>) our().ofType(AtlantisConfig.BASE);
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
	public Select idle() {
		Iterator<AUnit> unitsIterator = listSelectedAUnits.iterator();
		while (unitsIterator.hasNext()) {
			AUnit unit = unitsIterator.next(); // TODO: will probably not work with enemy units
			if (!unit.isIdle()) {
				unitsIterator.remove();
			}
		}
		return this;
	}

	/**
	 * Gibt alle unsere Arbeiter zurück, die Mineralien sammeln
	 */
	public Select ourMiningMineralsWorkers() {
		Iterator<AUnit> unitsIterator = ourUnits.iterator();
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
	public static Select ourWorkersFreeToBuildOrRepair() {
		Select selectedUnits = ourWorkers();
		Iterator<AUnit> it = selectedUnits.listSelectedAUnits.iterator();
		while(it.hasNext()) {
			AUnit unit = it.next();
			if ( AConstructionManager.isBuilder(unit)|| unit.isRepairing() 
					|| AScoutManager.isScout(unit) || unit.isRepairerOfAnyKind()) {
				it.remove();
			}
		}

		return selectedUnits;
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
