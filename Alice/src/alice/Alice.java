package alice;

import bwapi.*;

/**
 * Main bridge between the game and your code, ported to BWMirror.
 */
public class Alice implements BWEventListener {

	private boolean isStarted = false;
	private boolean isPaused = false;
	
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

			//Initialisiert den mirror mit dem Listener
			mirror.getModule().setEventListener(this);
			//Initialisiert die API und stellt die Verbindung zum Spiel her
			mirror.startGame();
		}
	}

	public void onEnd(boolean arg0) {
		

	}

	public void onFrame() {
		// TODO Auto-generated method stub

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

	}

	public void onUnitComplete(Unit arg0) {
		// TODO Auto-generated method stub

	}

	public void onUnitCreate(Unit arg0) {
		// TODO Auto-generated method stub

	}

	public void onUnitDestroy(Unit arg0) {
		// TODO Auto-generated method stub

	}

	public void onUnitDiscover(Unit arg0) {
		// TODO Auto-generated method stub

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
     * This method returns bridge connector between Atlantis and Starcraft, which is a BWMirror object. It
     * provides low-level functionality for functions like canBuildHere etc. For more details, see BWMirror
     * project documentation.
     */
    public static Game getBwapi() {
        return getInstance().bwapi;
    }

}
