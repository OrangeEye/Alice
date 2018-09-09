package alice.information;

import alice.AGame;
import alice.Alice;
import alice.production.orders.AZergProduction;
import alice.units.AUnit;
import alice.units.AUnitType;
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
		paintLarvas();
		paintWorkers();
		paintOverlords();
		paintBuildOrder();

	}

	private static void paintBaseRadius() {
		for (AUnit base : Select.ourBases().listUnits().values()) {
			game.drawCircleMap(base.getPosition().getPosition(), 250, Color.Yellow);
		}
	}

	private static void paintWorkers() {
		for (AUnit worker : Select.ourWorkers().values()) {
			game.drawCircleMap(worker.getPosition().getPosition(), 2, Color.Green,true);
		}
	}

	private static void paintLarvas() {
		for (AUnit larva : Select.getOurLarvas().values()) {
			game.drawCircleMap(larva.getPosition().getPosition(), 2, Color.White,true);
		}
	}
	private static void paintOverlords() {
		for (AUnit overlord : Select.getOurOverlords().values()) {
			game.drawCircleMap(overlord.getPosition().getPosition(), 2, Color.Purple,true);
		}
	}

	private static void paintBuildOrder() {
		int i = 0;
		if (AZergProduction.getProductionOrder() != null)
			for (AUnitType ut : AZergProduction.getProductionOrder()) {
				game.drawTextScreen(20, 20 + i, ut.toString());
				i += 10;
			}
	}

	private static void paintCountGatherer() {
		for (AUnit ressource : Select.ourMineralFields().values()) {
			game.drawTextMap(ressource.getPosition().getPosition(), ressource.countGatherer() + "/2");
		}
	}

	private static void paintOrders() {
		for (AUnit worker : Select.ourWorkers().values()) {
			game.drawTextMap(worker.getPosition().getPosition(),
					worker.getOrder().toString() + " " + worker.getTarget());
		}
	}

	private static void paintSupply() {
		game.drawTextScreen(585, 20, AGame.getSupplyUsed() + "/" + AGame.getSupplyTotal());
	}

}
