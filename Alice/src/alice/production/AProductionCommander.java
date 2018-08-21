package alice.production;

import alice.buildings.managers.ASupplyManager;

/**
 * Steuert das bauen neuer Gebäude
 *
 */
public class AProductionCommander {

	//produziert Einheiten und Gebäude nach build order 
	public static void update() {
		
		// Prüft ob supply benötigt wird und kümmert sich ggf. darum
        ASupplyManager.update();
	}
}
