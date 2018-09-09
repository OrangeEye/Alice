package alice.position;

import java.util.HashMap;
import java.util.Map;

import bwapi.Position;

public class APosition {
	private static final Map<Position, APosition> instances = new HashMap<>();
	private Position position;

	public APosition(Position position) {
		this.position = position;
	}

	public Position getPosition() {
		return position;
	}
	
	public APosition(int pixelX, int pixelY) {
        this.position = new Position(pixelX, pixelY);
    }
	
	/**
     * Returns X coordinate in tiles, 1 tile = 32 pixels.
     */
    public int getTileX() {
        return position.getX() / 32;
    }
    
    /**
     * Returns Y coordinate in tiles, 1 tile = 32 pixels.
     */
    public int getTileY() {
        return position.getY() / 32;
    }
	
	public static APosition createFromPosition(Position position) {
        if (instances.containsKey(position)) {
            return instances.get(position);
        }
        else {
          //  APosition position = new APosition(position);
            instances.put(position, new APosition(position));
            return instances.get(position);
        }
    }
	
	public static APosition createFromTile(int tileX, int tileY) {
        return new APosition(tileX * 32, tileY * 32);
    }
	

	
}
