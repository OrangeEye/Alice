package alice;

import alice.buildings.managers.AProductionCommander;
import alice.buildings.managers.AWorkerCommander;
import alice.information.APainter;
import alice.units.Select;
import alice.util.CodeProfiler;

public class AGameCommander {
	
	public void update() {
		
		CodeProfiler.startMeasuring(CodeProfiler.ASPECT_Global);
		
		//führt den Code von jedem Commander aus
		CodeProfiler.startMeasuring(CodeProfiler.ASPECT_PAINTING);
		
		APainter.paint();
		
		CodeProfiler.endMeasuring(CodeProfiler.ASPECT_PAINTING);
		
		
		CodeProfiler.startMeasuring(CodeProfiler.ASPECT_PRODUCTION);
		
		AProductionCommander.update();
		
		CodeProfiler.endMeasuring(CodeProfiler.ASPECT_PRODUCTION);
		
		
		CodeProfiler.startMeasuring(CodeProfiler.ASPECT_WORKERS);
		
		AWorkerCommander.update();
		
		CodeProfiler.endMeasuring(CodeProfiler.ASPECT_WORKERS);
		
		CodeProfiler.endMeasuring(CodeProfiler.ASPECT_Global);
		
		Select.removeNotExistingUnits();
	}

}
