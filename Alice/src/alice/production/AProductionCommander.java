package alice.production;

import alice.buildings.managers.ASupplyManager;

/**
 * Steuert das bauen neuer Geb�ude
 *
 */
public class AProductionCommander {

	//produziert Einheiten und Geb�ude nach build order 
	public static void update() {
		
		// Pr�ft ob supply ben�tigt wird und k�mmert sich ggf. darum
        ASupplyManager.update();
	}
}
