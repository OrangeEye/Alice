package alice.util;

public class ProcessHelper {


    public static void killStarcraftProcess() {
    	//1x sollte reichen?
        executeInCommandLine("taskkill /f /im Starcraft.exe");
        //executeInCommandLine("taskkill /f /im StarCraft.exe");
    }
    
    public static void killChaosLauncherProcess() {
        executeInCommandLine("taskkill /f /im Chaoslauncher.exe");
    }
    
    /**
     * Startet den Chaoslauncher
     */
    public static void startChaosLauncherProcess() {
        try {
            Thread.sleep(250);
            executeInCommandLine("C:\\Program Files\\Spiele\\BWAPI\\Chaoslauncher\\Chaoslauncher.exe");
        } catch (InterruptedException ex) {
            // Soll nichts machen
        }
    }
	
	
	private static void executeInCommandLine(String command) {
        try {
            Runtime.getRuntime().exec(command);
        } catch (Exception err) {
            err.printStackTrace();
        }
    }
}
