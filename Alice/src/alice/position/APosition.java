package alice.position;

import bwapi.Position;

public class APosition {
	private Position position;

	public APosition(Position position) {
		this.position = position;
	}

	public Position getPosition() {
		return position;
	}
}
