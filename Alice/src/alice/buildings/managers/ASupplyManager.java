package alice.buildings.managers;

import alice.AGame;

public class ASupplyManager {
	
	private static int supplyTotal;
    private static int supplyFree;
	
	public static void update() {
		supplyTotal = AGame.getSupplyTotal();
	}
}
