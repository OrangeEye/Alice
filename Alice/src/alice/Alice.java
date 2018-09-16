package alice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import alice.position.AMap;
import alice.production.orders.AZergBuildOrder;
import alice.units.AUnit;
import alice.units.AUnitType;
import alice.units.Select;
import bwapi.*;
import bwta.BWTA;

/**
 * Main bridge between the game and your code, ported to BWMirror.
 */
public class Alice implements BWEventListener {

	private boolean isStarted = false;
	private boolean isPaused = false;
	//ArrayList<Long> list = new ArrayList<Long>();
	private static Alice instance;
	private Game bwapi;
	private AGameCommander gameCommander;

	public Alice() {
		this.instance = this;
	}

	/**
	 * BWMirror core class.
	 */
	private static Mirror mirror = new Mirror();

	// Start / Pause / Unpause
	/**
	 * Startet den Bot.
	 */
	public void run() {
		try {
			if (!isStarted) {
				isPaused = false;
				isStarted = true;

				// Initialisiert den mirror mit dem Listener
				mirror.getModule().setEventListener(this);
				// Initialisiert die API und stellt die Verbindung zum Spiel her
				mirror.startGame();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void onEnd(boolean arg0) {

	}

	public void onFrame() {
		try {
			gameCommander.update();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void onNukeDetect(Position arg0) {
		// TODO Auto-generated method stub

	}

	public void onPlayerDropped(Player arg0) {
		// TODO Auto-generated method stub

	}

	public void onPlayerLeft(Player arg0) {
		// TODO Auto-generated method stub

	}

	public void onReceiveText(Player arg0, String arg1) {
		// TODO Auto-generated method stub

	}

	public void onSaveGame(String arg0) {
		// TODO Auto-generated method stub

	}

	public void onSendText(String arg0) {
		// TODO Auto-generated method stub

	}

	/**
	 * Wird einmal beim Start ausgeführt
	 */
	public void onStart() {
		try {
		// Initialize bwapi object - BWMirror wrapper of C++ BWAPI.
		bwapi = mirror.getGame();

		// Initialize Game Commander, eine Klasse die das Spiel steuert
		gameCommander = new AGameCommander();

		Race racePlayed = bwapi.self().getRace(); // AGame.getPlayerUs().getRace();
		if (racePlayed.equals(Race.Terran)) {
			AliceConfig.useConfigForTerran();
		} else if (racePlayed.equals(Race.Zerg)) {
			AliceConfig.useConfigForZerg();
		}
		


		System.out.print("Analyzing map... ");
		BWTA.readMap();
		BWTA.analyze();
		System.out.println("Map data ready.");
		//getBwapi().sendText("black sheep wall");
		

		// === Set some BWAPI params ===============================
		AMap.inititalize();
		bwapi.setLocalSpeed(AliceConfig.GAME_SPEED); // Change in-game speed (0 - fastest, 20 - normal)
		// bwapi.setFrameSkip(2); // Number of GUI frames to skip
		// bwapi.setGUI(false); // Turn off GUI - will speed up game considerably
		bwapi.enableFlag(1); // Enable user input - without it you can't control units with mouse
		
		
		
	} catch (Exception e) {
		e.printStackTrace();
	}
	}

	public void onUnitComplete(Unit arg0) {
		System.out.println("onComplete: " + arg0.getType());
		
		Select.addNewUnit(arg0);
		
		AUnit morphedUnit = new AUnit(arg0);
		AUnitType ut = AUnitType.getAUnitType(arg0.getType());
		AZergBuildOrder.finishOrder(ut, morphedUnit);
		

	}

	public void onUnitCreate(Unit arg0) {
		Select.addNewUnit(arg0);
		System.out.println("onCreate: " + arg0.getType() + " ID:" + arg0.getID());
	}

	public void onUnitDestroy(Unit arg0) {
		Select.addNewUnit(arg0);
		System.out.println("onDestroyed: " + arg0.getType());

	}

	public void onUnitDiscover(Unit arg0) {
		Select.addNewUnit(arg0);
		System.out.println("onDiscovered: " + arg0.getType());
	}

	public void onUnitEvade(Unit arg0) {
		// TODO Auto-generated method stub

	}

	public void onUnitHide(Unit arg0) {
		// TODO Auto-generated method stub

	}

	public void onUnitMorph(Unit arg0) {
		System.out.println("onMorph: " + arg0.getType()  + " ID:" + arg0.getID());
		AUnit morphedUnit = new AUnit(arg0);
		AUnitType ut = AUnitType.getAUnitType(arg0.getType());
		AZergBuildOrder.finishOrder(ut, morphedUnit);
		
		if(morphedUnit.isType(AUnitType.Resource_Vespene_Geyser)) {
			Select.removeVespeneGeyser(morphedUnit.getPosition());
		}
	}

	public void onUnitRenegade(Unit arg0) {
		// TODO Auto-generated method stub

	}

	public void onUnitShow(Unit arg0) {
		// TODO Auto-generated method stub

	}

	public static Alice getInstance() {
		if (instance == null) {
			instance = new Alice();
		}
		return instance;
	}

	/**
	 * This method returns bridge connector between Atlantis and Starcraft, which is
	 * a BWMirror object. It provides low-level functionality for functions like
	 * canBuildHere etc. For more details, see BWMirror project documentation.
	 */
	public static Game getBwapi() {
		return getInstance().bwapi;
	}

}
