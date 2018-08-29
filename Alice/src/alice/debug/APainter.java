package alice.debug;

import alice.Alice;
import alice.units.AUnit;
import alice.units.Select;
import bwapi.Color;

public class APainter {

	public static void paint() {
		paintBaseRadius();
		paintCountGatherer();
		paintOrders();
	}
	
	private static void paintBaseRadius() {
		for(AUnit base : Select.ourBases().listUnits()) {
			Alice.getBwapi().drawCircleMap(base.getPosition().getPosition(), 250, Color.Yellow);
		}
	}
	
	private static void paintCountGatherer() {
		for(AUnit ressource : Select.ourMineralFields()) {
			Alice.getBwapi().drawTextMap(ressource.getPosition().getPosition(), ressource.countGatherer() +"/2");
		}
	}
	private static void paintOrders() {
		for(AUnit worker : Select.ourWorkers()) {
			Alice.getBwapi().drawTextMap(worker.getPosition().getPosition(), worker.getOrder().toString() + " " + worker.getTarget());
		}
	}
	
	
}
