package alice;


import alice.production.orders.AZergBuildOrder;
import alice.units.AUnitType;
import bwapi.Race;

public class AliceConfig {
	public static Race MY_RACE = null;
    public static AUnitType BASE = null;
    public static AUnitType WORKER = null;
    public static AUnitType BARRACKS = null;
    public static AUnitType SUPPLY = null;
    public static AUnitType GAS_BUILDING = null;
    public static AUnitType DEF_BUILDING_ANTI_LAND = null;
    public static AUnitType DEFENSIVE_BUILDING_ANTI_AIR = null;
    public static AZergBuildOrder DEFAULT_BUILD_ORDER = null;
    public static int GAME_SPEED = 20;

    // =========================================================
    
    /**
     * Helper method for using Terran race.
     */
    public static void useConfigForTerran() {
    //	AliceConfig.DEFAULT_BUILD_ORDER = TerranBuildOrder.TERRAN_1_Base_Vultures;
        
        AliceConfig.MY_RACE = Race.Terran;
        AliceConfig.BASE = AUnitType.Terran_Command_Center;
        AliceConfig.WORKER = AUnitType.Terran_SCV;
        AliceConfig.BARRACKS = AUnitType.Terran_Barracks;
        AliceConfig.SUPPLY = AUnitType.Terran_Supply_Depot;
        AliceConfig.GAS_BUILDING = AUnitType.Terran_Refinery;
        
        AliceConfig.DEF_BUILDING_ANTI_LAND = AUnitType.Terran_Bunker;
        AliceConfig.DEFENSIVE_BUILDING_ANTI_AIR = AUnitType.Terran_Missile_Turret;
		
	//	AGame.increaseSupplyTotal(AliceConfig.BASE.getUnitType().supplyProvided()); //Für die Base
	//	AGame.increaseSupplyUsed(AliceConfig.WORKER.getUnitType().supplyRequired() *4); //Für die ersten 4 Arbeiter 
    }
    
    public static void useConfigForZerg() {
    	AliceConfig.MY_RACE = Race.Zerg;
        AliceConfig.BASE = AUnitType.Zerg_Hatchery;
        AliceConfig.WORKER = AUnitType.Zerg_Drone;
        AliceConfig.BARRACKS = AUnitType.Terran_Barracks;
        AliceConfig.SUPPLY = AUnitType.Zerg_Overlord;
        AliceConfig.GAS_BUILDING = AUnitType.Zerg_Extractor;
        AliceConfig.DEFAULT_BUILD_ORDER = AZergBuildOrder.getCurrentBuildOrder();
        
        AliceConfig.DEF_BUILDING_ANTI_LAND = AUnitType.Zerg_Sunken_Colony;
        AliceConfig.DEFENSIVE_BUILDING_ANTI_AIR = AUnitType.Zerg_Spore_Colony;
        
	//	AGame.increaseSupplyUsed(AliceConfig.WORKER.getUnitType().supplyRequired() * 4); //Für die ersten 4 Arbeiter 
	//	AGame.increaseSupplyTotal(AliceConfig.BASE.getUnitType().supplyProvided()); //Für die Base
	//	AGame.increaseSupplyTotal(AUnitType.Zerg_Overlord.getUnitType().supplyProvided()); //Für den Overlord 
    }
}
