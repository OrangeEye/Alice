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

		if (this.u().build(unitType.getUnitType(), position.toBuildTilePosition(unitType))) {
			this.unit().setUnitOrder(AUnitOrder._BUILDING);
			if (this.unit().isConstructing())
				order.setStatus(AOrder.STAUS_IN_ORDER);
		} else if (this.u().move(position.getPosition())) {
			this.unit().setUnitOrder(AUnitOrder._MOVE_TO_BUILD);
			if (order.getBuilder().isConstructing())
				order.setStatus(AOrder.STATUS_IN_PROCESS);
		}
	}

	default void train(AUnitType unitType) {
		this.u().train(unitType.getUnitType());
		this.unit().setUnitOrder(AUnitOrder._TRAIN);
	}

	default boolean gather(AUnit target) {
		if (this.u().gather(target.u())) {
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
