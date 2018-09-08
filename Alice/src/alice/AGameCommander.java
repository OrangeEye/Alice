package alice;

import alice.buildings.managers.AProductionCommander;
import alice.buildings.managers.AWorkerCommander;
import alice.information.APainter;

public class AGameCommander {
	
	public void update() {
		
		//f�hrt den Code von jedem Commander aus

		APainter.paint();
		AStrategyCommander.update();
		AProductionCommander.update();
		AWorkerCommander.update();
	}

}
