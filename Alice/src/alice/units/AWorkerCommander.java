package alice.units;

import java.util.Iterator;

public class AWorkerCommander {

	public static void update() {
		refreshMineralGatherer();
		sendWorkerToWork();
	}

	/**
	 * Sucht unbeschäftigte Arbeiter heraus und schickt Sie an freie
	 * Mineralienfelder, sofern welche vorhanden sind
	 */
	private static void sendWorkerToWork() {

		for (AUnit idleWorker : Select.idle(Select.ourWorkers())) {
			AUnit freeMineralField = Select.freeMineralField();
			if (freeMineralField != null)
				idleWorker.gather(freeMineralField);
		}
	}

	private static void refreshMineralGatherer() {

		for (AUnit worker : Select.ourWorkers()) {
			AUnit target = worker.getTarget();
			if (target != null) {
				if (!target.getGatherer().contains(worker))
					target.getGatherer().add(worker);
			}
		}

		for (AUnit mineralfield : Select.ourMineralFields()) {
			Iterator<AUnit> it = mineralfield.getGatherer().iterator();
			while (it.hasNext()) {
				AUnit gatherer = it.next();
				AUnit target = gatherer.getTarget();
				if(target != null) {
				if (target.equals(mineralfield))
					target.setTimerStart(System.nanoTime());
				else if (target.getTimerStart() < (System.nanoTime() + 3504545022654L))
					it.remove();
			}
			}
		}

		for (AUnit idleWorker : Select.idle(Select.ourWorkers())) {
			AUnit freeMineralField = Select.freeMineralField();
			if (freeMineralField != null)
				idleWorker.gather(freeMineralField);
		}
	}

}
