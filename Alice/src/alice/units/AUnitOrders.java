package alice.units;

import alice.position.APosition;
import alice.production.AOrder;
import bwapi.TilePosition;
import bwapi.Unit;

/**
 * Das Interface nutzt Default Methods, die von AUnit geerbt werden um die
 * Funktionen von einander zu trennen.
 * 
 * @author lganske
 *
 */
public interface AUnitOrders {

	Unit u();

	AUnit unit();

	default boolean buildAddon(AUnitType addon) {
		unit().setUnitOrder(AUnitOrder._BUILDING);
		return u().buildAddon(addon.getUnitType());
	}

	default void morph(AUnitType ut) {
		this.u().morph(ut.getUnitType());
		this.unit().setUnitOrder(AUnitOrder._MORPHING);
	}

	default void build(AUnitType unitType, APosition position,  AOrder order) {
		this.u().build(unitType.getUnitType(), position.toBuildTilePosition(unitType));
		this.unit().setUnitOrder(AUnitOrder._BUILDING);
		order.setStatus(AOrder.STATUS_IN_PROCESS);
	}
	
	

	default void train(AUnitType unitType) {
		this.u().train(unitType.getUnitType());
		this.unit().setUnitOrder(AUnitOrder._TRAIN);
	}

	default void gather(AUnit target) {
		this.u().gather(target.u());
		target.setGatherer(this.unit());

		if (Select.ourMineralFields().containsKey(target.getID()))
			this.unit().setUnitOrder(AUnitOrder._MINING_MINERALS);
		else
			this.unit().setUnitOrder(AUnitOrder._GATHERING_GAS);

	}
	


}
