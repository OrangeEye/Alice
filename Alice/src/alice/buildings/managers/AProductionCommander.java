package alice.buildings.managers;

import alice.AGame;
import alice.AliceConfig;
import alice.production.orders.AZergProduction;
import bwapi.Race;

/**
 * Steuert das bauen neuer Gebäude
 *
 */
public class AProductionCommander {

	
	public static void update() {
		
		AGame.updateSupply();
		
		if(AliceConfig.MY_RACE.equals(Race.Zerg))
			AZergProduction.update();
	}
}
