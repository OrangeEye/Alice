package alice.units;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AWorkerCommander {

	public static void update() {
		sendWorkerToWork();
	}

	private static void sendWorkerToWork() {
		
		for(AUnit idleWorker : Select.idle(Select.ourWorkers())) {
			
		}
		
		Select mineralFields = Select.ourMineralFields();

		for (AUnit worker : Select.ourWorkers().idle()) {
			worker.gather();
		}
	}

	private static void WorkerCountUpdate() {
		// Fügt neue Gebäude der Liste hinzu
		for (AUnit base : Select.ourBases().listUnits()) {
			if (!Select.getWorkersOnBase().containsKey(base))
				Select.putWorkerOnBase(base);
		}

		// Aktualisiert die Worker die einer Base zugehören
		for (AUnit worker : Select.our().ourMiningMineralsWorkers().listUnits()) {
			for (AUnit base : Select.ourBases().listUnits()) {
				if (worker.isInRangeTo(base, 12)) {
					Select.putWorkerOnBase(worker);
					break;
				}
			}
		}
		
		
	}

}
