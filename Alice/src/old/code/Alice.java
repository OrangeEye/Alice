package old.code;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashSet;

import bwapi.*;
import bwta.*;

public class Alice extends DefaultBWListener {

	private Mirror mirror = new Mirror();

	static Game game;

	static Player self;
	public static ArrayList<TilePosition> possibleExpandplace = new ArrayList<TilePosition>();
	public static ArrayList<TilePosition> tempExpandPlace = new ArrayList<TilePosition>();
	public static ArrayList<Position> enemyBuildingsPositions = new ArrayList<Position>();
	public static ArrayList<Unit> enemyBuildings = new ArrayList<Unit>();
	public static ArrayList<Position> enemyStartbase = new ArrayList<Position>();
	public static ArrayList<Unit> enemyForce = new ArrayList<Unit>();
	public static ArrayList<Unit> mainForce = new ArrayList<Unit>();
	public static ArrayList<Unit> scoutForce = new ArrayList<Unit>();
	public static ArrayList<Unit> myOverlords = new ArrayList<Unit>();
	public static ArrayList<Unit> myWorkers = new ArrayList<Unit>();
	public static ArrayList<Unit> myBuildings = new ArrayList<Unit>();
	public static ArrayList<Position> toDefendExpandPosition = new ArrayList<Position>();
	public static ArrayList<Position> tempToDefendExpandPosition = new ArrayList<Position>();
	public static int countOverlordsMainForce = 0;
	public static double scoreMyUnits = 0;
	public static double scoreEnemyUnits = 0;
	public static int countEnemyUnits = 0;
	public static int countWorkerAttack = 0;
	public static int countSurrender;
	public static int supplyUsed = 0;
	public static int scoutStartFrame;
	public static boolean deadZergling = false;
	public static Unit closestPsi_Disrupter;
	public static Position closestPsi_DisrupterPosition;

	public static int testCount = 0;
	public static int count = 0;

	public static int mapSpecificationHeartbreakRidgeScxSendDrone = 0;

	public void run() {
		mirror.getModule().setEventListener(this);
		mirror.startGame();
	}

	@Override
	public void onStart() {
		// Deaktiviert wahr. sysout
		OutputStream output = null;
		try {
			output = new FileOutputStream("NUL:");
		} catch (FileNotFoundException e) {
			// e.printStackTrace();
		}
		PrintStream nullOut = new PrintStream(output);
		System.setErr(nullOut);
		System.setOut(nullOut);

		game = mirror.getGame();
		self = game.self();

		game.enableFlag(1); // erlaubt das manuelle kontrollieren im Spiel
		game.setLocalSpeed(0); // max game speed =0
		// game.sendText("power overwhelming"); // Bot kann Textnachrichten senden
		// game.sendText("black sheep wall");
		game.sendText("Good luck and have fun");

		// Use BWTA to analyze map
		// This may take a few minutes if the map is processed first time!
		System.out.println("Analyzing map...");
		BWTA.readMap();
		BWTA.analyze();
		System.out.println("Map data ready");

		int i = 0;
		for (BaseLocation baseLocation : BWTA.getBaseLocations()) {
			System.out.println("Base location #" + (++i) + ". Printing location's region polygon:");
			for (Position position : baseLocation.getRegion().getPolygon().getPoints()) {
				System.out.print(position + ", ");
			}
			System.out.println();
		}

		// reinitalisiert alle Parameter (wird bei Spielneustart gebraucht)
		reInitializeAllParameters();

		// Initialisere das Gebäude Array
		// Building_List.initiateGebäude();
		Building_List.addBauliste(Building_List.Zerg_Drone);

		// initialisiere enemyStartbaseCheck
		for (BaseLocation startBase : BWTA.getStartLocations()) {
			if (!(startBase.getPosition().equals(BWTA.getStartLocation(self).getPosition()))) {
				enemyStartbase.add(startBase.getPosition());

			}
		}

		// Initialisiere possibleExpandPlace
		initializePossibleExpandplace();

		// Initialisiert das Bauplatz Array
		// Building_List.initializeBuildingTilePosition();

		// initializeToDefendPosition();

		mapStartSpecification();

	}

	@SuppressWarnings("unlikely-arg-type")
	@Override
	public void onFrame() {
		// supply wird doppelt gerechnet wegen Zerglinge die nur 0,5 haben

		// bei der Map blockiert ein mineralfeld den Weg. Wenn es noch da ist wird zu
		// dem kürzesten eine Drone zum absammeln geschickt
		mapSpecification();

		// System.out.println("Start refreshExpandPosition");
		refreshExpandPosition();

		// if(game.getFrameCount() % 100==0) {
		// Versucht eine Einheit aus der Bauliste zu bauen

		// System.out.println("Start addBuildingQue");
		// Building_List.vsZergBuildingQue();
		Building_List.addBuildingQue();
		// System.out.println("Start checkProduktion");

		Building_List.checkProduction();
		// System.out.println("Start buildUnit");
		Building_List.buildUnit();
		// System.out.println("Ende");

		// System.out.println("Start findIdleWorker");
		// sucht unbeschäftigte Arbeiter und schickt sie Ressourcen sammeln
		findIdleWorker();

		// sucht feindliche Gebäude und aktualisiert die Liste und setzt enemy Startbase
		// fest
		// System.out.println("Start findEnemybuildings");
		findEnemyBuildings();

		// System.out.println("Start sendForces");
		// schicke Kämpfer gegen Gegnerische Base
		sendForces();
		// System.out.println("ifWorkerAttacked");
		// wroker verteidigen sich
		ifWorkerAttacked();

		// System.out.println("Start cancelAttackedBuildings");
		// bricht Gebäudebau ab, wenn es zu stark angegriffen wird
		cancelAttackedBuildings();

		// wenn keine Einheiten mehr da sind Aufgeben
		surrender();

		// System.out.println("Start drawscreen");
		// zeichnet alles auf der Map
		drawTextScreen();
	}

	// Methoden

	// draw on TextScreen
	public static void drawTextScreen() {

		// zeichnet die Stärken der Armee
		game.drawTextScreen(360, 25,
				"scoreMyUnits: " + (int) scoreMyUnits + "\n" + "scoreEnemyUnits: " + (int) scoreEnemyUnits);
		// zeichnet die Position der markierten Unit (zu aufwändig - einfacher!)

		for (Unit selectedUnit : game.getAllUnits()) {
			if (selectedUnit.isSelected()) {
				game.drawTextMap(selectedUnit.getPosition(),
						"\n" + selectedUnit.getPosition().toString() + " Unit ID: " + selectedUnit.getID());
				break;
			}
		}

		// zeichne nextExpanPosition
		if (possibleExpandplace.size() != 0) {
			game.drawCircleMap(nextExpandPosition().toPosition(), 20, bwapi.Color.Orange, false);
		}

		// makiere Chokepoints
		for (Chokepoint thisChokePoint : bwta.BWTA.getChokepoints()) {
			game.drawLineMap(thisChokePoint.getSides().first, thisChokePoint.getSides().second, bwapi.Color.Red);
		}

		if (closestPsi_Disrupter != null) {
			game.drawCircleMap(closestPsi_DisrupterPosition, 3, bwapi.Color.Yellow, true);
		}

		// zeichne alle feindlichen entdeckten Einheiten
		StringBuilder TextEnemyForceList = new StringBuilder("Enemy Units:" + countEnemyUnits + "\n");
		ArrayList<UnitType> drawed = new ArrayList<UnitType>();
		for (Unit enemyUnit : enemyForce) {
			game.drawCircleMap(enemyUnit.getPosition(), 4, bwapi.Color.Red, true);

			if (!(drawed.contains(enemyUnit.getType()))) {
				TextEnemyForceList.append(
						enemyUnit.getType().toString() + ": " + countUnits(enemyBuildings, enemyUnit.getType()) + "\n");
				drawed.add(enemyUnit.getType());
			}
		}
		TextEnemyForceList.append("Enemy Buildings: " + enemyBuildings.size() + "\n");
		ArrayList<UnitType> alreadyPrinted = new ArrayList<UnitType>();
		for (Unit enemyBuilding : enemyBuildings) {
			if (!alreadyPrinted.contains(enemyBuilding.getType())) {
				alreadyPrinted.add(enemyBuilding.getType());
				TextEnemyForceList.append(enemyBuilding.getType().toString() + ": "
						+ countUnits(enemyBuildings, enemyBuilding.getType()) + "\n");
			}
		}
		game.drawTextScreen(490, 25, TextEnemyForceList.toString());

		// zeichne Kreise um Hatchery
		/*
		 * for (Unit myUnit : self.getUnits()) { int SeitenlängeKreuz = 1000; if
		 * (myUnit.getType() == UnitType.Zerg_Hatchery || myUnit.getType() ==
		 * UnitType.Zerg_Hive || myUnit.getType() == UnitType.Zerg_Lair) {
		 * game.drawLineMap(myUnit.getX() - SeitenlängeKreuz, myUnit.getY(),
		 * myUnit.getX() + SeitenlängeKreuz, myUnit.getY(), bwapi.Color.Yellow);
		 * game.drawLineMap(myUnit.getX(), myUnit.getY() - SeitenlängeKreuz,
		 * myUnit.getX(), myUnit.getY() + SeitenlängeKreuz, bwapi.Color.Yellow); } }
		 */

		// markiere die Scouts
		for (Unit scout : scoutForce) {
			game.drawCircleMap(scout.getPosition(), 50, bwapi.Color.Yellow);
			game.drawTextMap(scout.getPosition().getX(), scout.getPosition().getY(), scout.getOrder().toString());
			game.drawLineMap(scout.getPosition(), scout.getTargetPosition(), bwapi.Color.Yellow);
		}

		// zeichne mögliche Expansionsplätze
		for (TilePosition thisPosition : possibleExpandplace) {
			game.drawTextMap(thisPosition.toPosition(),
					possibleExpandplace.indexOf(thisPosition) + ": " + thisPosition.toPosition().toString());
		}

		// markiere feindliche Gebäude
		for (Position enemyBuildingPosition : enemyBuildingsPositions) {
			game.drawBoxMap(enemyBuildingPosition.getX() - 15, enemyBuildingPosition.getY() - 15,
					enemyBuildingPosition.getX() + 15, enemyBuildingPosition.getY() + 15, bwapi.Color.Red);
		}

		// markiere feindliche Startpositionen
		for (Position startPositionEnemy : enemyStartbase) {
			game.drawBoxMap(startPositionEnemy.getX() - 20, startPositionEnemy.getY() - 20,
					startPositionEnemy.getX() + 20, startPositionEnemy.getY() + 20, bwapi.Color.Red);
		}

		for (Unit overlord : myOverlords) {
			game.drawCircleMap(overlord.getPosition(), 2, bwapi.Color.Brown, true);
		}

		game.drawTextScreen(578, 25, "Supply: " + supplyUsed / 2);
		game.drawTextScreen(578, 35, "Workers: " + myWorkers.size());
		game.drawTextScreen(578, 45, "API: " + Alice.self.allUnitCount(UnitType.Zerg_Drone));
		game.drawTextScreen(578, 55, "Buildings: " + myBuildings.size());

		for (Unit myBuilding : myBuildings) {
			game.drawCircleMap(myBuilding.getPosition(), 2, bwapi.Color.Cyan, true);
		}

		// markiere enemyBuidlings
		for (Unit enemyBuilding : enemyBuildings) {
			game.drawCircleMap(enemyBuilding.getPosition(), 2, bwapi.Color.Yellow, true);
		}

		// Zeigt oben links im Fenster die Baureihenfolge an
		StringBuilder TextbuildingList = new StringBuilder("My current production:\n");
		// für alle Gebäude
		int countLines = 0;
		for (Unit myUnit : self.getUnits()) {
			// Gebäude die aktuell Gebaut werden mit Produktionszeit
			if (myUnit.isBeingConstructed())
				if (myUnit.getType() == UnitType.Zerg_Egg) {
					TextbuildingList
							.append(myUnit.getRemainingBuildTime() + "  " + myUnit.getBuildType().toString() + "\n");
					countLines++;
				}
				// Gebäude die noch auf der Bauliste stehen
				else {
					TextbuildingList.append(myUnit.getRemainingBuildTime() + "  " + myUnit.getType().toString() + "\n");
					countLines++;
				}
			if (countLines >= 5) {
				break;
			}
		}
		TextbuildingList.append("\nProduction priority: \n");
		// für alle Einheiten die noch auf der Produktionsliste stehen
		for (int i = 0; i < Building_List.productionlist.size() || i < 6; i++) {

			if (Building_List.productionlist.get(i).getUnitType() != null) {
				TextbuildingList.append(Building_List.productionlist.get(i).getUnitType().toString());

				if (Building_List.productionlist.get(i).getBuildingWorker() != null) {
					TextbuildingList.append(
							" countBuildingWorker: " + Building_List.productionlist.get(i).getcountBuildingWorker());
				}
				TextbuildingList.append("\n");

			} else {
				TextbuildingList.append(Building_List.productionlist.get(i).getUpgradeType().toString() + "\n");
			}

			// for (Building_List thisGebäude : Building_List.Unitlist) {
			// if (Building_List.productionlist.get(i).getUnitType() ==
			// thisGebäude.getUnitType()) {
			// // TextbuildingList.append(thisGebäude.getaddproduction() + "\n");
			// break;
			// }
			// }
			game.drawTextScreen(10, 25, TextbuildingList.toString());
			if (i == 7)
				break;
		}

		// zeige die Arbeiter der Hatcherys an, Parameter kommen aus den Methoden count
		// Gas/Minerals
		for (Unit Hatchery : myBuildings) {
			if ((Hatchery.getType().equals(UnitType.Zerg_Hatchery) || Hatchery.getType().equals(UnitType.Zerg_Lair)
					|| Hatchery.getType().equals(UnitType.Zerg_Hive))) {
				for (BaseLocation Location : BWTA.getBaseLocations()) {
					if (Hatchery.getTilePosition().equals(Location.getTilePosition())) {
						game.drawTextMap(Hatchery.getX() - 40, Hatchery.getY(), "Mineral workers: "
								+ countMineralworkerTotal(Hatchery) + "/" + countshouldMineralworker(Hatchery));
					}

				}

			}
		}

		// zeige die Frames an und den Framecount

		game.drawTextScreen(20, 290, "Time in sec: " + game.getFrameCount() / 42 + " FPS: " + game.getFPS()
				+ " Framecount: " + game.getFrameCount());

		// zeichnet die aktuelle Aufgabe der Unit und den Pfad zur Aufgabe an
		/*
		 * for (Unit myUnit : self.getUnits()) { if
		 * (myUnit.getType().equals(UnitType.Zerg_Drone) ||
		 * myUnit.getType().isBuilding()) { continue; }
		 * game.drawTextMap(myUnit.getPosition().getX(), myUnit.getPosition().getY(),
		 * myUnit.getOrder().toString()); //
		 * game.drawTextMap(myUnit.getPosition().getX(), myUnit.getPosition().getY(), //
		 * myUnit.getTilePosition().toString());
		 * game.drawLineMap(myUnit.getPosition().getX(), myUnit.getPosition().getY(),
		 * myUnit.getOrderTargetPosition().getX(),
		 * myUnit.getOrderTargetPosition().getY(), bwapi.Color.Black);
		 * 
		 * }
		 */
	}

	public void onUnitDiscover(Unit discoveredUnit) {

		// System.out.println(discoveredUnit.getType().toString() + " discovered
		// distance: "
		// + discoveredUnit.getDistance(self.getStartLocation().toPosition()));

		if (discoveredUnit.getPlayer().getID() == game.enemy().getID()) {

			double standartScore = 0;
			if (!enemyForce.contains(discoveredUnit)) {
				if (discoveredUnit.getType().canAttack() && !discoveredUnit.getType().isWorker()
						&& (double) discoveredUnit.getType().groundWeapon().damageCooldown() != 0) {

					standartScore = discoveredUnit.getType().groundWeapon().damageAmount()
							/ (double) discoveredUnit.getType().groundWeapon().damageCooldown()
							* (discoveredUnit.getType().maxHitPoints() + discoveredUnit.getType().maxShields());
				}
				if (discoveredUnit.getType().isBuilding() && discoveredUnit.getType().canAttack()) {
					standartScore *= 0.4;
					if (standartScore != standartScore) {
						System.out.println("standartScore is NaN Building");
					}
					enemyForce.add(discoveredUnit);
					countEnemyUnits++;
					scoreEnemyUnits += standartScore;

					if (scoreEnemyUnits != scoreEnemyUnits) {
						System.out.println("scoreEnemyUnits is NaN Building");
					}
				} else if (!discoveredUnit.getType().isBuilding()) {
					if (standartScore != standartScore) {
						System.out.println("standartScore is NaN");
					}
					enemyForce.add(discoveredUnit);
					countEnemyUnits++;
					scoreEnemyUnits += standartScore;

					if (scoreEnemyUnits != scoreEnemyUnits) {
						System.out.println("scoreEnemyUnits is NaN");
					}
				}
			}

			if (!enemyBuildings.contains(discoveredUnit) && discoveredUnit.getType().isBuilding()) {

				enemyBuildings.add(discoveredUnit);
				enemyBuildingsPositions.add(discoveredUnit.getPosition());
			}
			/*
			 * if (discoveredUnit.getType().isBuilding()) {
			 * enemyBuildings.add(discoveredUnit);
			 * enemyBuildingsPositions.add(discoveredUnit.getPosition()); if
			 * (discoveredUnit.getType().canAttack()) { enemyForce.add(discoveredUnit);
			 * countEnemyUnits++; standartScore *= 0.4;
			 * 
			 * } } else { enemyForce.add(discoveredUnit); countEnemyUnits++; }
			 */

		}

		if (discoveredUnit.getPlayer().getID() == self.getID())

		{
			if (discoveredUnit.getType().isWorker() && !myWorkers.contains(discoveredUnit)) {
				myWorkers.add(discoveredUnit);
			}
			if (discoveredUnit.getType().isBuilding() && !myBuildings.contains(discoveredUnit)) {
				myBuildings.add(discoveredUnit);
			}
		}
	}

	// wird eigentlich nur für Zerglinge genutzt
	@Override
	public void onUnitCreate(Unit newUnit) {
		// System.out.println(newUnit.getType().toString() + " created");

		// prüft ob die Unit ein Kämpfer ist und kein Overlord und keiner anderen Force
		// angehört
		if (newUnit.getPlayer().getID() == self.getID()) {

			if (newUnit.getType().equals(UnitType.Zerg_Overlord) && !mainForce.contains(newUnit)
					&& !myOverlords.contains(newUnit)) {
				myOverlords.add(newUnit);
			}

			// füge der workerList hinzu
			if (newUnit.getType().isWorker() && !myWorkers.contains(newUnit)) {
				myWorkers.add(newUnit);
			}

			if (!(newUnit.getType().equals(UnitType.Zerg_Zergling))) {
				supplyUsed += newUnit.getType().supplyRequired();
			}

			if (scoutForce.size() == 0 && newUnit.getType().equals(UnitType.Zerg_Zergling)) {
				scoutForce.add(newUnit);
			}

			if (!mainForce.contains(newUnit) && !newUnit.getType().isBuilding() && newUnit.getType().canAttack()
					&& !newUnit.getType().isWorker() && !newUnit.getType().equals(UnitType.Zerg_Overlord)
					&& !scoutForce.contains(newUnit)
					&& (double) newUnit.getType().groundWeapon().damageCooldown() != 0) {
				mainForce.add(newUnit);
				double standartScore = newUnit.getType().groundWeapon().damageAmount()
						/ (double) newUnit.getType().groundWeapon().damageCooldown()
						* (newUnit.getHitPoints() + newUnit.getShields());
				if (standartScore != standartScore) {
					System.out.println("add NaN myScore UnitCreate " + newUnit.getType().toString());
				}
				// System.out.println("add createUnit myScore " + standartScore);
				scoreMyUnits += standartScore;
				if (scoreMyUnits != scoreMyUnits) {
					System.out.println("myScore is UnitCreate " + scoreMyUnits + newUnit.getType().toString()
							+ " standart: " + standartScore);
				}
			}
		}
	}

	public void onUnitMorph(Unit morphedUnit) {
		// System.out.println(morphedUnit.getType().toString()+" morphed");

		if (morphedUnit.getType().equals(UnitType.Resource_Vespene_Geyser)) {
			if (enemyBuildings.contains(morphedUnit)) {
				enemyBuildings.remove(morphedUnit);
			}
		}

		if (morphedUnit.getPlayer().getID() == game.enemy().getID() && !enemyBuildings.contains(morphedUnit)
				&& !enemyForce.contains(morphedUnit)) {
			double standartScore = 0;

			if (morphedUnit.getType().canAttack() && !morphedUnit.getType().isWorker()
					&& (double) morphedUnit.getType().groundWeapon().damageCooldown() != 0) {
				standartScore = morphedUnit.getType().groundWeapon().damageAmount()
						/ (double) morphedUnit.getType().groundWeapon().damageCooldown()
						* (morphedUnit.getType().maxHitPoints() + morphedUnit.getType().maxShields());
			}

			if (morphedUnit.getType().isBuilding()) {
				enemyBuildings.add(morphedUnit);
				enemyBuildingsPositions.add(morphedUnit.getPosition());
				if (morphedUnit.getType().canAttack()) {
					enemyForce.add(morphedUnit);
					countEnemyUnits++;
					standartScore *= 0.4;

				}
			} else {
				enemyForce.add(morphedUnit);
				countEnemyUnits++;
			}
			if (standartScore != standartScore) {
				System.out.println("standartScore is NaN");
			}
			scoreEnemyUnits += standartScore;
			if (scoreEnemyUnits != scoreEnemyUnits) {
				System.out.println("scoreEnemyUnits is NaN");
			}
		}

		if (morphedUnit.getPlayer().getID() == self.getID()) {

			if (morphedUnit.getType().isWorker() && !myWorkers.contains(morphedUnit)) {
				myWorkers.add(morphedUnit);
			}

			if (morphedUnit.getType().isBuilding()) {

				if (!myBuildings.contains(morphedUnit)) {
					myBuildings.add(morphedUnit);
				}
				// verringert den supplyUsed für verbrauchte Drohne
				if (morphedUnit.getType().whatBuilds().first.equals(UnitType.Zerg_Drone)
						&& !(morphedUnit.getType().equals(UnitType.Zerg_Extractor))) {
					supplyUsed -= UnitType.Zerg_Drone.supplyRequired();
					if (myWorkers.contains(morphedUnit)) {
						myWorkers.remove(morphedUnit);
					}
				}
				for (Building_List thisList : Building_List.productionlist) {
					if (morphedUnit.getType().equals(thisList.getUnitType())) {
						if (morphedUnit.getType().whatBuilds().first.equals(UnitType.Zerg_Drone)) {
							thisList.decreaseCountBuildingWorker();
							thisList.removeBuildingWorker();
						}
						Building_List.productionlist.remove(thisList);
						break;
					}
				}
			}

			if (morphedUnit.getType().equals(UnitType.Zerg_Egg)) {
				supplyUsed += morphedUnit.getBuildType().supplyRequired();
				if (morphedUnit.getBuildType().equals(UnitType.Zerg_Zergling)) {
					supplyUsed += morphedUnit.getBuildType().supplyRequired();
				}
				for (Building_List thisList : Building_List.productionlist) {
					if (morphedUnit.getBuildType().equals(thisList.getUnitType())) {
						Building_List.productionlist.remove(thisList);
						break;
					}
				}
			}

			// füge der Force einen Overlord hinzu
			if (!(mainForce.contains(morphedUnit)) && morphedUnit.getType().equals(UnitType.Zerg_Overlord)
					&& countOverlordsMainForce < 2 && mainForce.size() > 10 && !(scoutForce.contains(morphedUnit))) {
				mainForce.add(morphedUnit);
				countOverlordsMainForce++;
			}

			if (morphedUnit.getType().equals(UnitType.Zerg_Overlord) && !mainForce.contains(morphedUnit)
					&& !myOverlords.contains(morphedUnit)) {
				myOverlords.add(morphedUnit);
			}

			if (scoutForce.size() < 2 && morphedUnit.getType().equals(UnitType.Zerg_Zergling)
					|| (morphedUnit.getType().equals(UnitType.Zerg_Overlord) && scoutForce.size() == 2
							&& supplyUsed >= 100 * 2 && !mainForce.contains(morphedUnit))) {
				scoutForce.add(morphedUnit);
			}

			// prüft ob die Unit ein Kämpfer ist und kein Overlord und keiner anderen Force
			// angehört
			if (!(mainForce.contains(morphedUnit)) && !(morphedUnit.getType().isBuilding())
					&& morphedUnit.getType().canAttack() && !(morphedUnit.getType().isWorker())
					&& !(morphedUnit.getType().equals(UnitType.Zerg_Overlord)) && !(scoutForce.contains(morphedUnit))
					&& (double) morphedUnit.getType().groundWeapon().damageCooldown() != 0) {

				mainForce.add(morphedUnit);
				// System.out.println("add mainForce " + morphedUnit.getType().toString());
				double standartScore = morphedUnit.getType().groundWeapon().damageAmount()
						/ (double) morphedUnit.getType().groundWeapon().damageCooldown()
						* (morphedUnit.getType().maxHitPoints() + morphedUnit.getType().maxShields());

				// System.out.println("add myScore " + standartScore);
				scoreMyUnits += standartScore;
				if (scoreMyUnits != scoreMyUnits) {
					System.out.println("myScore is UnitCreate " + morphedUnit.getType().toString());
				}
			}
		}
	}

	@Override
	public void onUnitDestroy(Unit deadUnit) {

		// System.out.print(deadUnit.getType().toString() + " is dead from ");
		// System.out.println(deadUnit.getPlayer().getName());

		if (myOverlords.contains(deadUnit)) {
			myOverlords.remove(deadUnit);
		}

		if (myBuildings.contains(deadUnit)) {
			myBuildings.remove(deadUnit);

			// baue ein zerstörtes Gebäude wieder auf
			for (Building_List thisType : Building_List.Unitlist) {
				// wenn es ein gemorphtes Gebäude war
				if (thisType.getUnitType() != null && thisType.getUnitType().equals(deadUnit.getType())) {

					if (thisType.getUnitType().equals(UnitType.Zerg_Sunken_Colony)
							|| thisType.getUnitType().equals(UnitType.Zerg_Spore_Colony)) {
						Building_List.addBauliste(Building_List.Zerg_Creep_Colony);
					}

					Building_List.addBauliste(thisType);
					break;
				}
			}
		}

		if (myWorkers.contains(deadUnit)) {
			myWorkers.remove(deadUnit);
		}

		if (enemyForce.contains(deadUnit)) {
			double standartScore = 0;
			countEnemyUnits--;
			enemyForce.remove(deadUnit);

			if (deadUnit.getType().canAttack() && !deadUnit.getType().isWorker()
					&& (double) deadUnit.getType().groundWeapon().damageCooldown() != 0) {

				standartScore = deadUnit.getType().groundWeapon().damageAmount()
						/ (double) deadUnit.getType().groundWeapon().damageCooldown()
						* (deadUnit.getType().maxHitPoints() + deadUnit.getType().maxShields());
			}

			if (deadUnit.getType().isBuilding()) {
				standartScore *= 0.4;
			}
			if (deadUnit.getType().toString().equals("Unknown")) {
				System.out.println("es wird unkown gezaehlt zur enemyForce");
			}
			if (standartScore != standartScore) {
				System.out.println("standartScore is NaN");
			}

			scoreEnemyUnits -= standartScore;
			if (scoreEnemyUnits != scoreEnemyUnits) {
				System.out.println("scoreEnemyUnits is NaN destroyed");
			}
		}

		if (enemyBuildings.contains(deadUnit)) {
			enemyBuildings.remove(deadUnit);
			enemyBuildingsPositions.remove(deadUnit.getPosition());
		}

		if (deadUnit.getPlayer().getID() == self.getID()) {

			if (deadUnit.getType().equals(UnitType.Zerg_Zergling) && deadZergling) {
				supplyUsed -= deadUnit.getType().supplyRequired();
				supplyUsed -= deadUnit.getType().supplyRequired();
				deadZergling = false;
			} else if (deadUnit.getType().equals(UnitType.Zerg_Zergling) && !deadZergling) {
				deadZergling = true;
			} else if (!(deadUnit.getType().equals(UnitType.Zerg_Zergling))) {

				supplyUsed -= deadUnit.getType().supplyRequired();
			}
		}

		if (mainForce.contains(deadUnit)) {
			// System.out.println("start remove");
			// System.out.println("mainForce removed ID" + deadUnit.getID());
			mainForce.remove(deadUnit);
			if (deadUnit.getType().equals(UnitType.Zerg_Overlord)) {
				countOverlordsMainForce--;
			} else {

				double standartScore = deadUnit.getType().groundWeapon().damageAmount()
						/ (double) deadUnit.getType().groundWeapon().damageCooldown()
						* (deadUnit.getType().maxHitPoints() + deadUnit.getType().maxShields());

				scoreMyUnits -= standartScore;
				if (scoreMyUnits != scoreMyUnits) {
					System.out.println("myScore is DeadUnit " + deadUnit.getType().toString());
				}
			}
		}

		if (scoutForce.contains(deadUnit)) {
			scoutForce.remove(deadUnit);
		}
		if (deadUnit == closestPsi_Disrupter) {
			game.sendText("removed closestPsi_Disrupter");
			closestPsi_Disrupter = null;
			closestPsi_DisrupterPosition = null;
		}
	}

	public static void sendForces() {

		// für die mainForce
		for (Unit forceUnit : mainForce) {
			// game.drawCircleMap(forceUnit.getPosition(), 2, bwapi.Color.White, true);
			// System.out.println("Start 0");
		if (forceUnit.getType().equals(UnitType.Zerg_Zergling)) {
				mainForce.remove(forceUnit);
				game.sendText("removed not existing " + forceUnit.getType().toString());
				continue;
			}
			// System.out.println("Start 0.5");
			if (forceUnit.getType().equals(UnitType.Zerg_Overlord)) {
				if (mainForce.get(0).getType().equals(UnitType.Zerg_Overlord)) {
					for (Unit noOverlord : mainForce) {
						if (!noOverlord.getType().equals(UnitType.Zerg_Overlord)) {
							Unit temp = mainForce.get(0);
							int indexOfnoOverlord = mainForce.indexOf(noOverlord);
							mainForce.set(0, noOverlord);
							mainForce.set(indexOfnoOverlord, temp);
							break;
						}
					}
				}
				forceUnit.move(mainForce.get(0).getPosition());
				continue;
			}

			// System.out.println("Start 1.");
			Unit closestEnemyForceUnit = closestEnemyUnitInRange(forceUnit);
			// System.out.println("test zu 1");
			if (closestEnemyForceUnit != null) {

				if (forceUnit.getTarget() != null && forceUnit.getTarget().getID() == closestEnemyForceUnit.getID()) {
					continue;
				}
				game.drawCircleMap(forceUnit.getPosition(), 2, bwapi.Color.Red, true);
				forceUnit.attack(closestEnemyForceUnit);
				// System.out.println("attack unit");
				continue;
			}

			// System.out.println("Start 2");
			Unit toDefendEnemyForceUnit = toDefendBuildingsEnemy(forceUnit);
			// System.out.println((toDefendEnemyForceUnit != null) + "");
			// System.out.println("test zu 2");
			if (toDefendEnemyForceUnit != null) {

				// System.out.println("auftrag schon da ");

				if (forceUnit.getTarget() != null && forceUnit.getTarget().getID() == toDefendEnemyForceUnit.getID()) {
					// System.out.println((forceUnit.getOrderTarget().equals(toDefendEnemyForceUnit))
					// + "");
					continue;
				}
				// System.out.println("greife an");
				game.drawCircleMap(forceUnit.getPosition(), 2, bwapi.Color.Purple, true);
				forceUnit.attack(toDefendEnemyForceUnit);
				continue;
			}

			// System.out.println("Start 3.");
			// System.out.println((scoreMyUnits > scoreEnemyUnits) + " " + (scoreMyUnits >=
			// 800));
			if (scoreMyUnits != scoreMyUnits || scoreEnemyUnits != scoreEnemyUnits) {
				System.out.println(scoreMyUnits + " > " + scoreEnemyUnits);
				System.exit(0);
			}
			if (scoreMyUnits > scoreEnemyUnits && scoreMyUnits >= 800 && readyAttack()) {
				// System.out.println("start attack");
				Position attackPosition = closestEnemyBuildingPosition(forceUnit);
				if (attackPosition != null) {

					// System.out.println((forceUnit.getOrderTargetPosition() != null) + " "
					// + (forceUnit.getOrderTargetPosition().equals(attackPosition)));
					if (forceUnit.getOrderTargetPosition() != null
							&& forceUnit.getOrderTargetPosition().equals(attackPosition)) {
						continue;
					}
					game.drawCircleMap(forceUnit.getPosition(), 2, bwapi.Color.Orange, true);
					forceUnit.attack(attackPosition);
					continue;

				} else {

					if (forceUnit.getOrderTargetPosition() != null
							&& forceUnit.getOrderTargetPosition().equals(enemyStartbase.get(0))
							&& enemyStartbase.size() == 1) {
						continue;
					}
					game.drawCircleMap(forceUnit.getPosition(), 2, bwapi.Color.White, true);
					forceUnit.attack(enemyStartbase.get(0));
					continue;
				}
			} else
			// wenn in Unterzahl Rückzug nach home
			{
				// forceUnit.attack(bwta.BWTA.getNearestChokepoint(forceUnit.getPosition()).getPoint());
				Unit closestBuilding = null;
				for (Unit building : myBuildings) {
					if ((closestBuilding == null
							|| (forceUnit.getDistance(building) < forceUnit.getDistance(closestBuilding)))) {
						closestBuilding = building;
					}

					if (forceUnit.getDistance(closestBuilding) < 100) {
						closestBuilding = null;
						break;
					}
				}
				if (closestBuilding != null && forceUnit.getDistance(closestBuilding) > 100
						&& forceUnit.getOrderTargetPosition() != null
						&& !(forceUnit.getOrderTargetPosition().equals(closestBuilding.getPosition()))) {
					forceUnit.attack(closestBuilding.getPosition());
					game.drawCircleMap(forceUnit.getPosition(), 2, bwapi.Color.Green, true);
				}

			}

		}
		// System.out.println("Start 5");
		for (Unit scout : scoutForce) {

			// wenn der scout sich gerade nicht bewegt oder nicht zum Ziel kommt
			if ((!scout.isMoving() && (scout.getOrder() != null && !scout.getOrder().equals(Order.AttackMove)))
					|| game.getFrameCount() - scoutStartFrame >= 2000) {
				Position targetScout = BWTA.getBaseLocations()
						.get((int) (Math.random() * BWTA.getBaseLocations().size())).getPosition();
				if (!game.isVisible(targetScout.toTilePosition())) {
					scout.move(targetScout);
					scoutStartFrame = game.getFrameCount();
				}
			}
		}

		ArrayList<Position> sendedPosition = new ArrayList<Position>();
		for (Unit overlord : myOverlords) {
			Position target = null;
			// wenn die base noch nicht gefunden wurde, wird danach gesucht
			if (enemyStartbase.size() != 1) {
				if (overlord.getTargetPosition() != null) {
					target = overlord.getTargetPosition();
				}
				if (target != null && enemyStartbase.contains(target) && !sendedPosition.contains(target)) {
					sendedPosition.add(overlord.getTargetPosition());
				}
				enemyPosition: for (Position base : enemyStartbase) {
					if (!sendedPosition.contains(base) && (target == null || !enemyStartbase.contains(target))) {
						overlord.move(base);
						break;
					}

					for (Unit enemyBuilding : enemyBuildings) {
						if (enemyBuilding.getDistance(base) < 1300) {
							Position temp = base;
							enemyStartbase.clear();
							enemyStartbase.add(temp);
							game.sendText("Found enemy base");
							break enemyPosition;
						}
					}
					if (game.isVisible(base.toTilePosition())) {
						enemyStartbase.remove(base);
					}
				}
			} else {
				// wenn bereits die Base gefunden wurde wird der OV zum nächsten Gebäude
				// geschickt
				Unit closestBuilding = null;
				for (Unit building : myBuildings) {
					if ((closestBuilding == null
							|| (overlord.getDistance(building) < overlord.getDistance(closestBuilding)))) {
						closestBuilding = building;
					}

					if (overlord.getDistance(closestBuilding) < 100) {
						closestBuilding = null;
						break;
					}
				}
				if (closestBuilding != null && overlord.getDistance(closestBuilding) > 100
						&& overlord.getOrderTargetPosition() != null
						&& !(overlord.getOrderTargetPosition().equals(closestBuilding.getPosition()))) {
					overlord.move(closestBuilding.getPosition());
				}

			}
		}

	}

	public static Unit toDefendBuildingsEnemy(Unit forceUnit) {
		Unit closestEnemyUnit = null;
		enemys: for (Unit enemyUnit : enemyForce) {
			if (enemyUnit.isVisible() && enemyUnit.isDetected()) {
				for (Unit building : myBuildings) {
					if (building.getDistance(enemyUnit) <= 600 && canAttackTarget(forceUnit, enemyUnit)) {
						closestEnemyUnit = enemyUnit;
						break enemys;
					}
				}
			}
		}
		if (closestEnemyUnit != null)
			game.drawCircleMap(closestEnemyUnit.getPosition(), 4, bwapi.Color.Brown, true);
		return closestEnemyUnit;
	}

	// return closest enemyUnit or building
	public static Unit closestEnemyUnitInRange(Unit attackUnit) {

		Unit closestEnemyUnit = null;
		for (Unit enemyUnit : enemyForce) {
			// sucht die näheste Unit die angreifen kann
			if (enemyUnit.isVisible() && enemyUnit.isDetected()
					&& (closestEnemyUnit == null
							|| attackUnit.getDistance(enemyUnit) < attackUnit.getDistance(closestEnemyUnit))
					&& attackUnit.getDistance(enemyUnit) < 200 && attackUnit.canAttack(enemyUnit)
					&& canAttackTarget(attackUnit, enemyUnit) && !enemyUnit.getType().equals(UnitType.Zerg_Egg)
					&& !enemyUnit.getType().equals(UnitType.Zerg_Larva)) {

				closestEnemyUnit = enemyUnit;
			}
		}
		if (attackUnit.getType().equals(UnitType.Zerg_Zergling) && closestEnemyUnit != null
				&& closestEnemyUnit.isFlying()) {
			System.out.println(attackUnit.getType().toString() + " want attack " + closestEnemyUnit.getType().toString()
					+ attackUnit.canAttack(closestEnemyUnit));
		}

		// game.sendText("attack " + closestEnemyUnit.getType().toString());
		return closestEnemyUnit;
	}

	public static boolean canAttackTarget(Unit attackUnit, Unit target) {
		boolean check = false;
		if (attackUnit != null && target != null) {
			if (target.isFlying()) {

				if (!(attackUnit.getType().airWeapon().equals(WeaponType.None))) {
					check = true;
				}
			} else if (!(attackUnit.getType().groundWeapon().equals(WeaponType.None))) {
				check = true;
			}
		}
		return check;

	}

	// gibt die Position des feindlichen Gebäudes zurück, dass der Einheit am
	// nähesten ist
	public static Position closestEnemyBuildingPosition(Unit myUnit) {
		Position closestEnemyBuilding = null;
		for (Position positionBuilding : enemyBuildingsPositions) {
			if (closestEnemyBuilding == null
					|| myUnit.getDistance(positionBuilding) < myUnit.getDistance(closestEnemyBuilding)) {
				closestEnemyBuilding = positionBuilding;
			}
		}
		return closestEnemyBuilding;
	}

	/** sucht die näheste unentdeckte StartLocation heraus
	*/
	public static Position scoutenemyStartLocation(Unit myUnit) {
		Position closestPosition = null;
		// wenn bereits ein Gegnerisches Hauptgebäude gefunden wurde schicke den Späher
		// nach Hause
		if (enemyStartbase.size() == 1 && game.enemy().getUnits().size() != 0) {
			return self.getStartLocation().toPosition();
		}

		// sucht die kürzeste Position heraus
		Position: for (Position thisPosition : enemyStartbase) {

			// wenn eine Einheit gefunden wird, berechne die StartLocation
			for (Unit enemyUnit : game.enemy().getUnits()) {
				if (enemyUnit.getDistance(thisPosition) < 1300) {
					Position temp = thisPosition;
					enemyStartbase.clear();
					enemyStartbase.add(temp);
					game.sendText("Found enemy base");
					break Position;
				}
			}

			// wenn bereits ein Overlord zu der Position geschickt wird soll nicht noch
			// einer geschickt werden zu der Position
			for (Unit Overlord : self.getUnits()) {
				if (Overlord.getOrderTargetPosition().equals(thisPosition) && !(Overlord.equals(myUnit))) {
					continue Position;
				}
			}

			if (enemyStartbase.size() > 1 && (closestPosition == null
					|| myUnit.getDistance(thisPosition) < myUnit.getDistance(closestPosition))) {
				closestPosition = thisPosition;
			}
		}

		return closestPosition;
	}

	// sucht und aktualisiert die Positionen der Gebäude der Gegner
	public static void findEnemyBuildings() {

		// loop over all the positions that we remember
		for (Position enemyBuildingPosition : enemyBuildingsPositions) {

			// if that tile is currently visible to us...
			if (game.isVisible(enemyBuildingPosition.toTilePosition())) {

				// loop over all the visible enemy buildings and find out if at least
				// one of them is still at that remembered position
				boolean buildingStillThere = false;
				for (Unit enemyBuilding : enemyBuildings) {
					if (enemyBuilding.getPosition().equals(enemyBuildingPosition)) {
						buildingStillThere = true;
						break;
					}
				}

				// if there is no more any building, remove that position from our memory
				if (!buildingStillThere) {
					enemyBuildingsPositions.remove(enemyBuildingPosition);
					break;
				}
			}
		}
	}

	public static void ifWorkerAttacked() {
		for (Unit worker : myWorkers) {
			if (worker.isUnderAttack()) {
				countWorkerAttack = 1;
				Unit enemy = closestEnemyUnitInRange(worker);
				if (enemy != null) {
					game.drawCircleMap(worker.getPosition(), 2, bwapi.Color.Red, true);
					if (worker.getTarget() != null && !(worker.getTarget().equals(enemy))) {
						worker.attack(enemy);
					}

					for (Unit attackWorker : game.getUnitsInRadius(worker.getPosition(), 150)) {
						if (attackWorker.getType().isWorker()) {
							// enemy = closestEnemyUnitInRange(worker);
							game.drawCircleMap(attackWorker.getPosition(), 2, bwapi.Color.Red, true);
							if (attackWorker.getTarget() != null && !(attackWorker.getTarget().equals(enemy))) {
								attackWorker.attack(enemy);
							}
						}
					}
				}
				break;
			}
		}
		if (countWorkerAttack != 0) {
			countWorkerAttack++;
			// System.out.println("count: " + countWorkerAttack);
		}
		if (countWorkerAttack == 50) {
			// System.out.println("set count to 0");
			countWorkerAttack = 0;
			for (Unit toStopWorker : myWorkers) {
				if (toStopWorker.getOrder() != null && toStopWorker.getOrder().equals(Order.AttackUnit)) {
					// System.out.println(toStopWorker.getType().toString() + " stop attack");
					toStopWorker.stop();
				}

			}
		}
	}

	// sucht unbeschäftigte Arbeiter und schickt sie Ressourcen sammeln
	public static void findIdleWorker() {
		for (Unit myUnit : myWorkers) {
			// if it's a worker and it's idle, send it to the closest Ressource
			if (myUnit.isIdle()) {
				// sucht unbesetzte Extractoren in der Nähe
				Unit closestGas = undermannedExtractor(myUnit);

				// prüfe ob ein unbesetzter Extractor in der Nähe ist und sendet
				if (closestGas != null) {
					myUnit.gather(closestGas);
					continue;
				}

				// find the closest undermanned mineral
				Unit closestMineral = undermannedMineral(myUnit);

				// if a mineral patch was found, send the worker to gather it
				if (closestMineral != null) {
					myUnit.gather(closestMineral);
					continue;
				}
			}
		}
	}

	// prüfe ob ein Extractor in Reichweite und unterbesetzt ist
	public static Unit undermannedExtractor(Unit idleWorker) {
		int countGasWorker = 0;
		int maxDistanz = 350;
		for (Unit Extractor : myBuildings) {
			// wenn der Extractor in der Nähe der Drohne ist
			if (Extractor.getType() == UnitType.Zerg_Extractor && Extractor.isCompleted()
					&& (Math.abs(Extractor.getX() - idleWorker.getX()) < maxDistanz)
					&& (Math.abs(Extractor.getY() - idleWorker.getY()) < maxDistanz)) {
				for (Unit gasWorker : myWorkers) {
					// wenn der Extractor unterbesetzt ist
					if (gasWorker.isGatheringGas() && (Math.abs(Extractor.getX() - gasWorker.getX()) < maxDistanz)
							&& (Math.abs(Extractor.getY() - gasWorker.getY()) < maxDistanz)) {
						countGasWorker++;
					}
				}
				if (countGasWorker < 3) {
					return Extractor;

				}
			}
		}
		return null;
	}

	// sucht das näheste unterbesetzte Mineral
	public static Unit undermannedMineral(Unit idleWorker) {
		count++;
		Unit closestMineral = null;

		// prüft ob die Hatchery die nähste ist
		for (Unit Hatchery : myBuildings) {
			count++;
			if ((Hatchery.getType() == UnitType.Zerg_Hatchery || Hatchery.getType() == UnitType.Zerg_Lair
					|| Hatchery.getType() == UnitType.Zerg_Hive) && Hatchery.getDistance(idleWorker) < 350) {
				count++;
				closestMineral = undermannedHatcheryMinerals(Hatchery);
				if (closestMineral != null) {
					count++;
					return closestMineral;
				}
			}
		}

		// wenn die näheste Hatchery besetzt ist, wird eine andere gesucht
		for (Unit Hatchery : myBuildings) {
			count++;
			if ((Hatchery.getType() == UnitType.Zerg_Hatchery || Hatchery.getType() == UnitType.Zerg_Lair
					|| Hatchery.getType() == UnitType.Zerg_Hive)) {
				count++;
				closestMineral = undermannedHatcheryMinerals(Hatchery);
				if (closestMineral != null) {
					count++;
					return closestMineral;
				}
			}
		}

		// wenn absolut kein Mineralienfeld gefunden wird, schicke die Drone zum
		// nächsten Mineralienfeld
		for (Unit mineral : game.getNeutralUnits()) {

			if (mineral.getType().isMineralField() && (closestMineral == null
					|| idleWorker.getDistance(mineral) < idleWorker.getDistance(closestMineral))) {
				closestMineral = mineral;
			}
		}
		count++;
		return closestMineral;
	}

	// prüfe ob die Hatchery unterbesetzt an MineralWorker ist
	public static Unit undermannedHatcheryMinerals(Unit Hatchery) {
		count++;
		Unit closestMineral = null;
		// prüft ob die Hatchery eine BaseLocation ist
		for (BaseLocation BaseLocation : BWTA.getBaseLocations()) {
			count++;
			if (BaseLocation.getTilePosition().equals(Hatchery.getTilePosition())) {
				count++;
				// wenn bei der Hatchery freie Plätze sind
				if (countMineralworkerTotal(Hatchery) < countshouldMineralworker(Hatchery)) {
					count++;
					// suche ein Mineralfeld von der Hatchery heraus
					for (Unit hatchMineral : game.getNeutralUnits()) {
						count++;
						if (hatchMineral.getType().isMineralField() && hatchMineral.getDistance(Hatchery) < 350) {
							count++;
							closestMineral = hatchMineral;
							break;
						}
					}
					count++;
					return closestMineral;

				}
			}
		}
		count++;
		return closestMineral;
	}

	public static boolean readyAttack() {
		if (Alice.self.getUpgradeLevel(bwapi.UpgradeType.Grooved_Spines) == 1
				&& Alice.self.getUpgradeLevel(bwapi.UpgradeType.Zerg_Missile_Attacks) == 1
				&& Alice.self.getUpgradeLevel(bwapi.UpgradeType.Zerg_Carapace) == 1
				&& Alice.self.getUpgradeLevel(bwapi.UpgradeType.Muscular_Augments) == 1
				&& Alice.self.getUpgradeLevel(bwapi.UpgradeType.Metabolic_Boost) == 1) {
			return true;
		}
		return false;
	}

	// count GasWorkers for this Hatchery
	public static int countshouldMineralworker(Unit Hatchery) {
		int countshouldMineralworker = 0;
		for (Unit BaseMineral : game.neutral().getUnits()) {
			count++;
			if (BaseMineral.getDistance(Hatchery) < 350 && BaseMineral.getType().isMineralField()) {
				count++;
				countshouldMineralworker += 2;
			}
		}
		return countshouldMineralworker;
	}

	public static int countTotalMineralworker() {
		int count = 0;
		for (Unit Hatchery : myBuildings) {
			if ((Hatchery.getType().equals(UnitType.Zerg_Hatchery) || Hatchery.getType().equals(UnitType.Zerg_Lair)
					|| Hatchery.getType().equals(UnitType.Zerg_Hive))) {

				for (BaseLocation Location : BWTA.getBaseLocations()) {
					if (Hatchery.getTilePosition().equals(Location.getTilePosition())) {
						count += countshouldMineralworker(Hatchery) + 5;
					}

				}

			}
		}
		return count;
	}

	// count mineralWorkers for this Hatchery
	public static int countMineralworkerTotal(Unit Hatchery) {
		count++;
		int countMineralworkerTotal = 0;
		for (Unit MineralWorker : myWorkers) {
			count++;
			if (MineralWorker.isGatheringMinerals() && MineralWorker.getDistance(Hatchery) < 350) {
				count++;
				countMineralworkerTotal++;
			}
		}
		count++;
		return countMineralworkerTotal;
	}

	// returns the position of the shortest possible expandPosition
	public static TilePosition nextExpandPosition() {
		TilePosition shortest = null;
		if (possibleExpandplace.size() != 0) {
			shortest = possibleExpandplace.get(0);
		}
		double distance = 0;

		// loop for the closest Position which is contains in possibleExpandplace and is
		// not my own startbase
		/*
		 * for (TilePosition baseLocation : possibleExpandplace) { if ((shortest == null
		 * || self.getStartLocation().getDistance(baseLocation) < distance)) { shortest
		 * = baseLocation; distance = self.getStartLocation().getDistance(baseLocation);
		 * } }
		 */
		if (shortest == null) {
			System.out.println("Es konnte kein Expansionsplatz gefunden werden!");
		}
		return shortest;
	}

	// entfernt einen Expansionsplatz, wenn dort bereits expandiert wird
	public void refreshExpandPosition() {
		// speichert possibleExpandplace in den temp
		thisPosition: for (TilePosition thisPosition : possibleExpandplace) {
			// System.out.println(possibleExpandplace.indexOf(thisPosition) + " von "
			// +possibleExpandplace.size());

			// wenn der Feindort nicht baut || der Feind Startbase in der Nähe ist
			if (enemyBuildingsPositions.contains(thisPosition.toPosition())) {
				// System.out.println("start if 1");
				tempExpandPlace.add(thisPosition);
				initializePossibleExpandplace();
				// System.out.println("end if 1");
				continue;
			}
			// System.out.println("Test 1");
			if (game.isVisible(thisPosition)) {
				game.drawCircleMap(thisPosition.toPosition(), 5, bwapi.Color.Cyan, true);

				for (Unit building : game.getUnitsInRectangle(thisPosition.toPosition(),
						new Position((thisPosition.getX() + 4) * 32, (thisPosition.getY() + 3) * 32))) {
					// System.out.println("test2");
					if (building.getType().isBuilding()) {
						// System.out.println("test3");
						tempExpandPlace.add(thisPosition);
						// System.out.println("test 4");
						initializePossibleExpandplace();
						break thisPosition;
					}
				}
			}
		}
		// System.out.println("Test 2");
		// nimmt entfernte Plätze wieder auf
		for (TilePosition removedPosition : tempExpandPlace) {
			// (wenn dort Hatch gebaut werden kann && der Feind
			// dort nicht baut && Sich der PLatz 1300 der Gegnigerschen Startbase befindet
			if (game.canBuildHere(removedPosition, UnitType.Zerg_Hatchery)
					&& !enemyBuildingsPositions.contains(removedPosition.toPosition())) {
				tempExpandPlace.remove(removedPosition);
				initializePossibleExpandplace();
			}
		}
		// // entferne toDefendExpandPosition von der Liste wenn die Hatch nicht da ist
		/*
		 * for (Position defendPosition : toDefendExpandPosition) { boolean
		 * hatchNotExist = true; for (Unit hatchery : myBuildings) { if
		 * ((hatchery.getType().equals(UnitType.Zerg_Hatchery) ||
		 * hatchery.getType().equals(UnitType.Zerg_Lair) ||
		 * hatchery.getType().equals(UnitType.Zerg_Hive)) &&
		 * hatchery.getDistance(defendPosition) < 600) { hatchNotExist = false; break; }
		 * 
		 * } if (hatchNotExist) { tempToDefendExpandPosition.add(defendPosition);
		 * toDefendExpandPosition.remove(defendPosition); } }
		 * 
		 * // füge sie wieder der Liste hinzu sobald eine Hatch da ist for (Position
		 * tempPosition : tempToDefendExpandPosition) { for (Unit hatchery :
		 * myBuildings) { if ((hatchery.getType().equals(UnitType.Zerg_Hatchery) ||
		 * hatchery.getType().equals(UnitType.Zerg_Lair) ||
		 * hatchery.getType().equals(UnitType.Zerg_Hive)) &&
		 * hatchery.getDistance(tempPosition) < 600) {
		 * toDefendExpandPosition.add(tempPosition);
		 * tempToDefendExpandPosition.remove(tempPosition); break; }
		 * 
		 * } }
		 */
	}

	public static void initializeToDefendPosition() {
		for (BaseLocation thisBaseLocation : bwta.BWTA.getBaseLocations()) {
			Position closestSunken = null;
			Position closestChokepoint = null;
			Position newPosition;

			for (Chokepoint thisChokepoint : bwta.BWTA.getChokepoints()) {
				if (closestChokepoint == null
						|| thisBaseLocation.getDistance(thisChokepoint.getPoint()) < thisBaseLocation
								.getDistance(thisChokepoint.getPoint())) {
					closestChokepoint = thisChokepoint.getPoint();
				}
			}

			// wenn closestChokepoint oben links von der Hatch ist
			if (thisBaseLocation.getX() > closestChokepoint.getX()
					&& thisBaseLocation.getY() > closestChokepoint.getY()) {
				newPosition = new Position(closestSunken.getX() - 150, closestSunken.getY() - 150);
				toDefendExpandPosition.add(newPosition);
			}
			// wenn closestChokepoint oben rechts von der Hatch ist
			else if (thisBaseLocation.getX() < closestChokepoint.getX()
					&& thisBaseLocation.getY() > closestChokepoint.getY()) {
				newPosition = new Position(closestSunken.getX() + 150, closestSunken.getY() - 150);
				toDefendExpandPosition.add(newPosition);
				toDefendExpandPosition.add(newPosition);
			}
			// wenn closestChokepoint unten links von der Hatch ist
			else if (thisBaseLocation.getX() > closestChokepoint.getX()
					&& thisBaseLocation.getY() < closestChokepoint.getY()) {
				newPosition = new Position(closestSunken.getX() - 150, closestSunken.getY() + 150);
				toDefendExpandPosition.add(newPosition);
				toDefendExpandPosition.add(newPosition);
			}
			// wenn closestChokepoint unten rechts von der Hatch ist
			else if (thisBaseLocation.getX() < closestChokepoint.getX()
					&& thisBaseLocation.getY() < closestChokepoint.getY()) {
				newPosition = new Position(closestSunken.getX() + 150, closestSunken.getY() + 150);
				toDefendExpandPosition.add(newPosition);
				toDefendExpandPosition.add(newPosition);
			}
		}
	}

	public static Position getToDefendPosition(Unit forceUnit) {
		// Position closestPosition = null;
		Unit closestBuilding = null;

		for (Unit building : myBuildings) {
			if ((closestBuilding == null
					|| (forceUnit.getDistance(building) < forceUnit.getDistance(closestBuilding)))) {
				closestBuilding = building;
			}
		}

		return bwta.BWTA.getNearestChokepoint(closestBuilding.getPosition()).getPoint();

		/*
		 * if (enemyStartbase.size() == 1) { for (Position thisPosition :
		 * toDefendExpandPosition) { if (closestPosition == null ||
		 * enemyStartbase.get(0).getDistance(thisPosition) < enemyStartbase.get(0)
		 * .getDistance(closestPosition)) { closestPosition = thisPosition; } } } return
		 * closestPosition;
		 */
	}

	public static void mapSpecification() {
		if (game.mapFileName().equals("(2)Heartbreak Ridge.scx") && mapSpecificationHeartbreakRidgeScxSendDrone < 3
				&& game.getFrameCount() > 5000) {
			if (self.getStartLocation().getDistance(7, 91) < self.getStartLocation().getDistance(120, 3)) {
				if (game.isVisible(7, 91) && game.getUnitsOnTile(7, 91).size() != 0) {
					mineralfields: for (Unit mineralfield : game.getUnitsOnTile(7, 91)) {
						if (mineralfield.getType().equals(UnitType.Resource_Mineral_Field)) {
							for (Unit drone : myWorkers) {

								if (drone.isIdle()) {
									drone.gather(mineralfield);
									mapSpecificationHeartbreakRidgeScxSendDrone++;
									break mineralfields;
								}
							}
						}
					}

				} else {
					for (Unit overlord : self.getUnits()) {
						if (overlord.getType().equals(UnitType.Zerg_Overlord)) {
							overlord.move(new Position(7 * 32, 91 * 32));
							break;
						}
					}
				}
			} else {
				if (game.isVisible(120, 3) && game.getUnitsOnTile(120, 3).size() != 0) {
					mineralfields: for (Unit mineralfield : game.getUnitsOnTile(120, 3)) {
						if (mineralfield.getType().equals(UnitType.Resource_Mineral_Field)) {
							for (Unit drone : myWorkers) {
								if (drone.getType().equals(UnitType.Zerg_Drone) && drone.isIdle()) {
									drone.gather(mineralfield);
									mapSpecificationHeartbreakRidgeScxSendDrone++;
									break mineralfields;
								}
							}
						}
					}

				} else {
					for (Unit overlord : self.getUnits()) {
						if (overlord.getType().equals(UnitType.Zerg_Overlord)) {
							overlord.move(new Position(120 * 32, 3 * 32));
							break;
						}
					}
				}
			}
		}
		// herausgenommen, da 10 Disrupter auf einem Platz sind

		if (game.mapFileName().equals("(4)Electric Circuit.scx") && closestPsi_Disrupter != null) {

			for (Unit scout : scoutForce) {
				if (game.isVisible(closestPsi_DisrupterPosition.toTilePosition())
						&& (scout.getTarget() == null || scout.getTarget().getID() != closestPsi_Disrupter.getID())) {

					scout.attack(closestPsi_Disrupter);
					System.out.println("try attack");
					// System.out.println(scout.getOrder().toString() + " " +
					// scout.getTarget().getType().toString());
				} else {
					scout.move(closestPsi_DisrupterPosition);
				}
			}
		}
	}

	public static void cancelAttackedBuildings() {
		for (Unit thisBuilding : myBuildings) {
			if (!thisBuilding.isCompleted() && thisBuilding.isUnderAttack()
					&& thisBuilding.getHitPoints() <= thisBuilding.getType().maxHitPoints() * 0.1) {
				thisBuilding.cancelConstruction();
				Alice.supplyUsed += UnitType.Zerg_Drone.supplyRequired();
				for (Building_List thisList : Building_List.Unitlist) {
					if (thisList.getUnitType() != null && thisList.getUnitType().equals(thisBuilding.getType())) {
						Building_List.addPriorityBauliste(thisList);
					}
				}

			}
		}
	}

	public static void initializePossibleExpandplace() {
		// Initialisiere possibleExpandPlace
		possibleExpandplace.clear();
		possibleExpandplace.add(self.getStartLocation().getPoint());
		for (int i = 0; i < BWTA.getBaseLocations().size(); i++) {
			TilePosition closestBaseLocation = null;
			TilePosition lastExpandPosition = possibleExpandplace.get(possibleExpandplace.size() - 1);
			TilePosition first = null;
			TilePosition second = null;
			TilePosition third = null;
			for (BaseLocation thisBaseLocation : BWTA.getBaseLocations()) {
				if (!possibleExpandplace.contains(thisBaseLocation.getTilePosition())
						&& !tempExpandPlace.contains(thisBaseLocation.getTilePosition()) && !thisBaseLocation.isIsland()
						&& (closestBaseLocation == null || thisBaseLocation.getPosition()
								.getDistance(lastExpandPosition.toPosition()) < closestBaseLocation.toPosition()
										.getDistance(lastExpandPosition.toPosition()))) {
					closestBaseLocation = thisBaseLocation.getTilePosition();
					third = second;
					second = first;
					first = closestBaseLocation;
				}
			}
			if (closestBaseLocation != null) {
				if (second != null && third != null && bwta.BWTA.getGroundDistance(third,
						lastExpandPosition) < bwta.BWTA.getGroundDistance(second, lastExpandPosition)) {
					second = third;
				}
				if(second!=null&& bwta.BWTA.getGroundDistance(second,
						lastExpandPosition) < bwta.BWTA.getGroundDistance(closestBaseLocation, lastExpandPosition)) {
					closestBaseLocation=second;
				}

				possibleExpandplace.add(closestBaseLocation);
			}
		}
		possibleExpandplace.remove(0);
	}

	public static void surrender() {
		if (supplyUsed == 0 && self.minerals() < 50) {
			if (countSurrender == 0) {
				countSurrender = game.getFrameCount();
				game.sendText("gg wp " + game.enemy().getName());
			}
			if (countSurrender + 150 <= game.getFrameCount()) {
				game.leaveGame();
			}
		}
	}

	public static int countUnits(ArrayList<Unit> list, UnitType unitType) {
		int count = 0;
		for (Unit thisUnit : list) {
			if (thisUnit.getType().equals(unitType)) {
				count++;
			}
		}
		return count;
	}

	public static void mapStartSpecification() {

		if (game.mapFileName().equals("(2)Heartbreak Ridge.scx")) {

			if (self.getStartLocation().toPosition().equals(new Position(288, 1232))) {
				TilePosition temp = possibleExpandplace.get(1);
				possibleExpandplace.set(1, possibleExpandplace.get(3));
				possibleExpandplace.set(3, temp);
				temp = possibleExpandplace.get(8);
				possibleExpandplace.set(8, possibleExpandplace.get(2));
				possibleExpandplace.set(2, temp);
				temp = possibleExpandplace.get(5);
				possibleExpandplace.set(5, possibleExpandplace.get(3));
				possibleExpandplace.set(3, temp);
			} else {
				TilePosition temp = possibleExpandplace.get(1);
				possibleExpandplace.set(1, possibleExpandplace.get(3));
				possibleExpandplace.set(3, temp);
				temp = possibleExpandplace.get(6);
				possibleExpandplace.set(6, possibleExpandplace.get(2));
				possibleExpandplace.set(2, temp);
				temp = possibleExpandplace.get(4);
				possibleExpandplace.set(4, possibleExpandplace.get(3));
				possibleExpandplace.set(3, temp);
			}
		}
	}

	public static void reInitializeAllParameters() {
		countOverlordsMainForce = 0;
		scoreMyUnits = 0;
		scoreEnemyUnits = 0;

		Building_List.productionlist.clear();
		Building_List.productionlist.clear();
		Building_List.productionlist.clear();
		Building_List.reservedGas = 0;
		Building_List.reservedMinerals = 0;
		Building_List.shouldAvaibleCountMakroHatchery = 0;
		Building_List.Unitlist.clear();

	}

	public static int supplyUsed() {
		return supplyUsed;
	}

	public static void main(String[] args) {
		new Alice().run();
	}
}