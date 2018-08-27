package alice.information;

import alice.position.APosition;
import alice.units.AUnit;
import alice.units.AUnitType;

public class AFoggedUnit {
	private APosition position;
    private final AUnit unit;
    private AUnitType type;
    private AUnitType _lastCachedType;
    private final AUnitType buildType;
    
    public AFoggedUnit(AUnit unit) {
        this.unit = unit;
        position = unit.getPosition();
//        type = unit.getType();
        type = AUnitType.addUnitType(unit.u().getType());
        _lastCachedType = type;
        buildType = unit.getBuildType();
    }

	public APosition getPosition() {
		return position;
	}

	public void setPosition(APosition position) {
		this.position = position;
	}

	public AUnitType getType() {
		return type;
	}

	public void setType(AUnitType type) {
		this.type = type;
	}

	public AUnitType get_lastCachedType() {
		return _lastCachedType;
	}

	public void set_lastCachedType(AUnitType _lastCachedType) {
		this._lastCachedType = _lastCachedType;
	}

	public AUnit getUnit() {
		return unit;
	}

	public AUnitType getBuildType() {
		return buildType;
	}
}
