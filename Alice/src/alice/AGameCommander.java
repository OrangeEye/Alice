package alice;

import alice.production.AProductionCommander;
import alice.units.AWorkerCommander;

public class AGameCommander {
	
	public void update() {
		
		//f�hrt den Code von jedem Commander aus
	//	AProductionCommander.update();
		AWorkerCommander.update();
	}

}
