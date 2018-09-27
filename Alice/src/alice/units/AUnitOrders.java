package alice.units;

import alice.AGame;
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

	default boolean morph(AUnitType ut) {
		if (this.u().morph(ut.getUnitType())) {
			this.unit().setUnitOrder(AUnitOrder._MORPHING);
			return true;
		}
		return false;
	}

	default void build(AUnitType unitType, APosition position, AOrder order) {

		if (this.u().build(unitType.getUnitType(), position.toBuildTilePosition(unitType)) && AGame.lastOrdersuccess()) {
			this.unit().setUnitOrder(AUnitOrder._BUILDING);
			order.setStatus(AOrder.STAUS_IN_ORDER);
		} else if (this.u().move(position.getPosition()) && AGame.lastOrdersuccess()) {
			this.unit().setUnitOrder(AUnitOrder._MOVE_TO_BUILD);
		}
	}

	default void train(AUnitType unitType) {
		this.u().train(unitType.getUnitType());
		this.unit().setUnitOrder(AUnitOrder._TRAIN);
	}

	default boolean gather(AUnit target) {
		if (this.u().gather(target.u()) && AGame.lastOrdersuccess()) {
			target.setGatherer(this.unit());

			if (Select.ourMineralFields().containsKey(target.getID()))
				this.unit().setUnitOrder(AUnitOrder._MINING_MINERALS);
			else
				this.unit().setUnitOrder(AUnitOrder._GATHERING_GAS);
			return true;
		}
		return false;
	}

}
