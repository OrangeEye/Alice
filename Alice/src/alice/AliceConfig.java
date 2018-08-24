package alice;


import alice.production.orders.ABuildOrder;
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
    public static ABuildOrder DEFAULT_BUILD_ORDER = null;

    // =========================================================
    
    /**
     * Helper method for using Terran race.
     */
    public static void useConfigForTerran() {
    	AliceConfig.DEFAULT_BUILD_ORDER = TerranBuildOrder.TERRAN_1_Base_Vultures;
        
        AliceConfig.MY_RACE = Race.Terran;
        AliceConfig.BASE = AUnitType.Terran_Command_Center;
        AliceConfig.WORKER = AUnitType.Terran_SCV;
        AliceConfig.BARRACKS = AUnitType.Terran_Barracks;
        AliceConfig.SUPPLY = AUnitType.Terran_Supply_Depot;
        AliceConfig.GAS_BUILDING = AUnitType.Terran_Refinery;
        
        AliceConfig.DEF_BUILDING_ANTI_LAND = AUnitType.Terran_Bunker;
        AliceConfig.DEFENSIVE_BUILDING_ANTI_AIR = AUnitType.Terran_Missile_Turret;
    }
}
