package alice.units;

import alice.units.action.UnitActions;
import bwapi.Unit;

/**
 * Das Interface nutzt Default Methods, die von AUnit geerbt werden um die Funktionen von einander zu trennen.
 * @author lganske
 *
 */
public interface AUnitOrders {
	
	Unit u();
	
	AUnit unit();

	
	default boolean buildAddon(AUnitType addon) {
        unit().setUnitAction(UnitActions.BUILD);
        return u().buildAddon(addon.getUnitType());
    }
	
	
}
