package alice;

import alice.buildings.managers.AProductionCommander;
import alice.buildings.managers.AWorkerCommander;
import alice.information.APainter;

public class AGameCommander {
	
	public void update() {
		
		//führt den Code von jedem Commander aus

		APainter.paint();
		AProductionCommander.update();
		AWorkerCommander.update();
	}

}
