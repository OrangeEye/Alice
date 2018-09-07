package alice.information;

import alice.AGame;
import alice.Alice;
import alice.units.AUnit;
import alice.units.Select;
import bwapi.Color;
import bwapi.Game;

public class APainter {

	private static Game game = Alice.getBwapi();

	public static void paint() {
		paintBaseRadius();
		paintCountGatherer();
		paintSupply();
		paintOrders();

	}

	private static void paintBaseRadius() {
		for (AUnit base : Select.ourBases().listUnits()) {
			game.drawCircleMap(base.getPosition().getPosition(), 250, Color.Yellow);
		}
	}

	private static void paintCountGatherer() {
		for (AUnit ressource : Select.ourMineralFields()) {
			game.drawTextMap(ressource.getPosition().getPosition(), ressource.countGatherer() + "/2");
		}
	}

	private static void paintOrders() {
		for (AUnit worker : Select.ourWorkers()) {
			game.drawTextMap(worker.getPosition().getPosition(),
					worker.getOrder().toString() + " " + worker.getTarget());
		}
	}

	private static void paintSupply() {
		game.drawTextScreen(585, 20, AGame.getSupplyUsed() + "/" + AGame.getSupplyTotal());
	}

}
