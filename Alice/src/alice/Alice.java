package alice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import alice.units.AUnit;
import alice.units.Select;
import bwapi.*;
import bwta.BWTA;

/**
 * Main bridge between the game and your code, ported to BWMirror.
 */
public class Alice implements BWEventListener {

	private boolean isStarted = false;
	private boolean isPaused = false;
	ArrayList<Long> list = new ArrayList<Long>();
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
		if (!isStarted) {
			isPaused = false;
			isStarted = true;

			// Initialisiert den mirror mit dem Listener
			mirror.getModule().setEventListener(this);
			// Initialisiert die API und stellt die Verbindung zum Spiel her
			mirror.startGame();
		}
	}

	public void onEnd(boolean arg0) {

	}

	public void onFrame() {
		try {
/*
			for (AUnit mineralfield : Select.ourMineralFields()) {
				Iterator<AUnit> it = mineralfield.getGatherer().iterator();
				while (it.hasNext()) {
					AUnit gatherer = it.next();
					if (gatherer.getTarget() != null) {
						if (gatherer.getTarget().equals(mineralfield)) {
							System.out.println("true");
							gatherer.setTimerStart(System.nanoTime());
						}
						else {
							list.add(System.nanoTime() - gatherer.getTimerStart());
							it.remove();
						}
					}
				}

			}
			long max = 0;
			for (Long value : list) {
				if (value > max)
					max = value;
			}
			System.out.println(max);
			*/
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

		// === Set some BWAPI params ===============================

		bwapi.setLocalSpeed(AliceConfig.GAME_SPEED); // Change in-game speed (0 - fastest, 20 - normal)
		// bwapi.setFrameSkip(2); // Number of GUI frames to skip
		// bwapi.setGUI(false); // Turn off GUI - will speed up game considerably
		bwapi.enableFlag(1); // Enable user input - without it you can't control units with mouse
	}

	public void onUnitComplete(Unit arg0) {
		Select.addNewUnit(arg0);
	}

	public void onUnitCreate(Unit arg0) {
		Select.addNewUnit(arg0);
	}

	public void onUnitDestroy(Unit arg0) {
		Select.addNewUnit(arg0);

	}

	public void onUnitDiscover(Unit arg0) {
		Select.addNewUnit(arg0);
	}

	public void onUnitEvade(Unit arg0) {
		// TODO Auto-generated method stub

	}

	public void onUnitHide(Unit arg0) {
		// TODO Auto-generated method stub

	}

	public void onUnitMorph(Unit arg0) {
		// TODO Auto-generated method stub

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
