package alice.buildings.managers;

import alice.production.orders.ATerranProduction;

/**
 * Steuert das bauen neuer Geb�ude
 *
 */
public class AProductionCommander {

	
	public static void update() {
		
		/**
		 * Steuert die Produktion der Geb�ude
		 */
		ATerranProduction.update();
	}
}
