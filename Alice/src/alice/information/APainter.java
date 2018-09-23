package alice.information;

import java.text.DecimalFormat;

import alice.AGame;
import alice.Alice;
import alice.position.AMap;
import alice.position.APosition;
import alice.production.AOrder;
import alice.production.orders.AZergProduction;
import alice.units.AUnit;
import alice.units.AUnitType;
import alice.units.Select;
import alice.util.CodeProfiler;
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
		paintBases();
		paintProfiler();
		paintVespeneGas();
		paintOurBuildings();
		paintReservedRessources();
	}

	private static void paintProfiler() {

		DecimalFormat f = new DecimalFormat("#0.00");
		int i = 0;
		game.drawTextScreen(500, 230, "Time: " + AGame.getGameTime());
		game.drawTextScreen(500, 240, "FPS: " + AGame.getFPS() + " d: " + f.format(AGame.getAverageFPS()) );
		for (String profileName : CodeProfiler.getAspectsTimeConsumption().keySet()) {
			if (profileName.equals(CodeProfiler.ASPECT_Global))
				continue;

			game.drawTextScreen(500, 250 + i, profileName + ": " + CodeProfiler.getPercent(profileName) + "%");
			i += 10;
		}

	}
	
	private static void paintReservedRessources() {
		game.drawTextScreen(430, 20,"reserved: M: " +AZergProduction.getMineralReserved() +" G: " + AZergProduction.getGasReserved());
	}

	private static void paintBaseRadius() {
		for (AUnit base : Select.ourBases()) {
			game.drawCircleMap(base.getPosition().getPosition(), 250, Color.Yellow);
		}
	}

	private static void paintWorkers() {
		for (AUnit worker : Select.ourWorkers().values()) {
			game.drawCircleMap(worker.getPosition().getPosition(), 2, Color.Green, true);
		}
	}

	private static void paintLarvas() {
		for (AUnit larva : Select.getOurLarvas().values()) {
			game.drawCircleMap(larva.getPosition().getPosition(), 2, Color.White, true);
		}
	}

	private static void paintOverlords() {
		for (AUnit overlord : Select.getOurOverlords().values()) {
			game.drawCircleMap(overlord.getPosition().getPosition(), 2, Color.Purple, true);
		}
	}

	private static void paintBases() {
	//	game.drawTextMap(AMap.getMainBasePosition().getPosition(), "Main_Base");
		int i = 2;
		for (APosition base : AMap.getBasePositions()) {
			game.drawTextMap(base.getPosition(), i + "");
			i++;
		}
	}
	
	private static void paintOurBuildings() {
		for (AUnit building : Select.ourBuildings().values()) {
			game.drawCircleMap(building.getPosition().getPosition(), 4, Color.Green, false);
		}
	}

	private static void paintBuildOrder() {
		int i = 0;
		if (AZergProduction.getBuildOrderList() != null)
			for (AOrder ut : AZergProduction.getBuildOrderList()) {
				if (!ut.getStatus().equals(AOrder.STATUS_FINISHED)) {
					game.drawTextScreen(20, 20 + i, ut.toString());
					i += 10;
				}
			}
	}

	private static void paintCountGatherer() {
		for (AUnit ressource : Select.ourMineralFields().values()) {
			game.drawTextMap(ressource.getPosition().getPosition(), ressource.countGatherer() + "/2");
		}
	}
	
	private static void paintVespeneGas() {
		for (AUnit ressource : Select.getGasFields().values()) {
			game.drawCircleMap(ressource.getPosition().getPosition(), 4, Color.White, false);
			}
	}

	private static void paintOrders() {
		for (AUnit unit : Select.getOurUnits().values()) {
			game.drawTextMap(unit.getPosition().getPosition(),
					unit.getUnitOrder().toString() + " " + unit.getTarget());
		}
	}

	private static void paintSupply() {
		game.drawTextScreen(585, 20, AGame.getSupplyUsed() + "/" + AGame.getSupplyTotal());
	}

}
