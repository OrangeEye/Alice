package alice.constructing;

import alice.units.AUnitType;
import alice.units.Select;

public class AConstructionManager {

	/**
	 * Zählt die nicht fertiggestellten Einheiten / Gebäude des Types zusammen
	 * @param type
	 * @return
	 */
	public static int countNotFinishedConstructionsOfType(AUnitType type) {
        return Select.ourNotFinished().ofType(type).count()
                + countNotStartedConstructionsOfType(type);
    }
}
