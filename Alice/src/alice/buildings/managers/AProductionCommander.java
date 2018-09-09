package alice.buildings.managers;

import alice.AliceConfig;
import alice.production.orders.ATerranProduction;
import alice.production.orders.AZergProduction;
import bwapi.Race;

/**
 * Steuert das bauen neuer Gebäude
 *
 */
public class AProductionCommander {

	
	public static void update() {
		
		if(AliceConfig.MY_RACE.equals(Race.Zerg))
			AZergProduction.update();
		else if(AliceConfig.MY_RACE.equals(Race.Terran))
		ATerranProduction.update();
	}
}
