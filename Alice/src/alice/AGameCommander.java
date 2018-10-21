package alice;

import alice.buildings.managers.AProductionCommander;
import alice.buildings.managers.AWorkerCommander;
import alice.information.APainter;
import alice.position.AMap;
import alice.units.Select;
import alice.util.CodeProfiler;
import bwapi.Error;

public class AGameCommander {
	
	public void update() {
		
		CodeProfiler.startMeasuring(CodeProfiler.ASPECT_Global);
		
		//führt den Code von jedem Commander aus
		CodeProfiler.startMeasuring(CodeProfiler.ASPECT_PAINTING);
	//	if(AGame.getFrameCount() % 2 ==0)
		APainter.paint();
		
		CodeProfiler.endMeasuring(CodeProfiler.ASPECT_PAINTING);
		
		AMap.update();
		
		CodeProfiler.startMeasuring(CodeProfiler.ASPECT_PRODUCTION);
		
		AProductionCommander.update();
		
		CodeProfiler.endMeasuring(CodeProfiler.ASPECT_PRODUCTION);
		
		
		CodeProfiler.startMeasuring(CodeProfiler.ASPECT_WORKERS);
		
		AWorkerCommander.update();
		
		CodeProfiler.endMeasuring(CodeProfiler.ASPECT_WORKERS);
		
		CodeProfiler.endMeasuring(CodeProfiler.ASPECT_Global);
		
		Select.removeNotExistingUnits();
		Error lastError = AGame.getLastError();
		if(lastError != null && !lastError.equals(Error.None)) System.out.println(lastError);
	}
	
	

}
