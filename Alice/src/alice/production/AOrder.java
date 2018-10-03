package alice.production;

import java.util.ArrayList;

import alice.position.APosition;
import alice.units.AUnit;
import alice.units.AUnitType;

public class AOrder {

	public static String STATUS_FINISHED = "finished";
	public static String STAUS_IN_ORDER = "in_order";
	public static String STATUS_IN_PROCESS = "in_process";
	public static String STATUS_NOT_STARTED = "not_started";

	public static String INFO_NOTHING = "nothing";
	public static String INFO_IS_EXPANSION = "expansion";
	public static String INFO_EXTRACTOR_TRICK = "extractor trick";

	private String status = STATUS_NOT_STARTED; // Welcher Status die Order in der Bauschleife hat
	private String additionalInfo = INFO_NOTHING; // Zusatzinformationen zu der Order
	private int mineralDelay = 0; // Um den Bau zu Verzögern / den Arbeiter schon früher zum Platz zu bewegen
	private AUnitType unitType;
	private APosition buildPosition;
	private AUnit builder;

	public AOrder(AUnitType ut) {
		unitType = ut;
	}

	public AUnitType getAUnitType() {
		return unitType;
	}

	public boolean isUnitType() {
		return (unitType != null);
	}

	public AUnit getBuilder() {
		return builder;
	}

	public void setBuilder(AUnit builder) {
		this.builder = builder;
	}

	public AUnitType getWhatBuildsIt() {
		return unitType.getWhatBuildsIt();
	}

	public String getAdditionalInfo() {
		return additionalInfo;
	}

	public void setAdditionalInfo(String additionalInfo) {
		this.additionalInfo = additionalInfo;
	}

	public APosition getBuildPosition() {
		return buildPosition;
	}

	public boolean hasAdditionalInfo() {
		return !this.additionalInfo.equals(INFO_NOTHING);
	}

	public boolean setBuildPosition(APosition buildPosition) {
		if (buildPosition != null) {
			this.buildPosition = buildPosition;
			return true;
		}
		return false;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return this.status;
	}

	public int getMineralDelay() {
		return mineralDelay;
	}

	public void setMineralDelay(int mineralDelay) {
		this.mineralDelay = mineralDelay;
	}

	@Override
	public String toString() {
		return this.getStatus() + " " + this.unitType.toString();
	}
}
