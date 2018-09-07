package alice.buildings.managers;

import java.util.Iterator;

import alice.AliceConfig;
import alice.units.AUnit;
import alice.units.Select;
import bwapi.Order;

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

		/**
		 * Setzt die Mineralienfelder die Arbeiter zu, von denen Sie gesammelt werden,
		 * wenn sie noch nicht auf der Liste stehenhe
		 */
		for (AUnit worker : Select.ourWorkers()) {
			AUnit target = worker.getTarget();
			if (target != null && target.isMineralField()) {
				if (!target.getGatherer().contains(worker))
					target.getGatherer().add(worker);
			}
		}

		/**
		 * Prüft ob die Sammler noch an dem selben Mineralienfeld arbeiten und löscht
		 * Sie ggf. aus der Sammlerliste
		 */
		for (AUnit mineralfield : Select.ourMineralFields()) {
			Iterator<AUnit> it = mineralfield.getGatherer().iterator();
			while (it.hasNext()) {
				AUnit gatherer = it.next();
				AUnit target = gatherer.getTarget();
				if (target == null || !gatherer.isGatheringMinerals()
						|| !(target.equals(mineralfield) || target.getType().equals(AliceConfig.BASE))) {
					it.remove();
				}
			}
		}
	}

}
