package alice.buildings.managers;

import alice.production.orders.ATerranProduction;

/**
 * Steuert das bauen neuer Gebäude
 *
 */
public class AProductionCommander {

	
	public static void update() {
		
		/**
		 * Steuert die Produktion der Gebäude
		 */
		ATerranProduction.update();
	}
}
