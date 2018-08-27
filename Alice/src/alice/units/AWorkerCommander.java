package alice.units;


public class AWorkerCommander {

	public static void update() {
		sendWorkerToWork();
	}

	private static void sendWorkerToWork() {
		
		for(AUnit idleWorker : Select.idle(Select.ourWorkers())) {
			AUnit freeMineralField = Select.freeMineralField();
			if(freeMineralField != null)
				idleWorker.gather(freeMineralField);
		}
	}

}
