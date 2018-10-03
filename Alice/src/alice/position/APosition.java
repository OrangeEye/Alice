package alice.position;

import java.util.HashMap;
import java.util.Map;

import alice.units.AUnitType;
import bwapi.Position;
import bwapi.TilePosition;
import bwta.BaseLocation;

public class APosition {
	public static final String ADDINFO_IS_FREE_BASELOCATION = "free baseLocation";
	public static final String ADDINFO_IS_NO_FREE_BASELOCATION = "no free baseLocation";

	private static final Map<Position, APosition> instances = new HashMap<>();
	private Position position;
	private String addInfo = "";

	public APosition(Position position) {
		this.position = position;
	}


	public APosition(int pixelX, int pixelY) {
		this.position = new Position(pixelX, pixelY);
	}

	public APosition(BaseLocation baseLocation) {
		this.position = new Position(baseLocation.getX(), baseLocation.getY());
	}
	
	public static APosition createFromTile(int tileX, int tileY) {
		return new APosition(tileX * 32, tileY * 32);
	}
	
	public static APosition createFromPosition(Position position) {
		if (instances.containsKey(position)) {
			return instances.get(position);
		} else {
			// APosition position = new APosition(position);
			instances.put(position, new APosition(position));
			return instances.get(position);
		}
	}
	
	public Position getPosition() {
		return position;
	}

	/**
	 * Returns X coordinate in tiles, 1 tile = 32 pixels.
	 */
	public int getTileX() {
		return position.getX() / 32;
	}

	public double getDistance(APosition position) {
		return this.position.getDistance(position.getPosition());
	}

	public TilePosition toTilePosition() {
		return position.toTilePosition();
	}

	/**
	 * Returns Y coordinate in tiles, 1 tile = 32 pixels.
	 */
	public int getTileY() {
		return position.getY() / 32;
	}




	public TilePosition toBuildTilePosition(AUnitType unitType) {
		return new TilePosition(this.getTileX() - unitType.getUnitType().tileWidth() / 2,
				this.getTileY() - unitType.getUnitType().tileHeight() / 2);
	}

	public String getAddInfo() {
		return addInfo;
	}

	public void setAddInfo(String addInfo) {
		this.addInfo = addInfo;
	}
	


	public boolean isCloseTo(APosition position, int distance) {
		if (this.getDistance(position) <= distance)
			return true;
		return false;
	}

	@Override
	public String toString() {
		return this.getPosition().toString();
	}

	@Override
	public boolean equals(Object position) {
		try {
			return this.getPosition().getX() == ((APosition) position).getPosition().getX()
					&& this.getPosition().getY() == ((APosition) position).getPosition().getY();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
