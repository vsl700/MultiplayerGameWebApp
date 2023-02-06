package com.vsl700.pong;

public enum TableDirections {
	WEST(new Location(0, PongTable.HEIGHT / 2)),
	EAST(new Location(PongTable.WIDTH - PongPlate.SMALL_SIZE, PongTable.HEIGHT / 2)),
	NORTH(new Location(PongTable.WIDTH / 2, PongTable.HEIGHT - PongPlate.SMALL_SIZE)),
	SOUTH(new Location(PongTable.WIDTH / 2, 0));
	
	
	private Location correspondingLoc;
	
	private TableDirections(Location correspondingLoc) {
		this.correspondingLoc = correspondingLoc;
	}
	
	public Location getCorrespondingLocation() {
		return correspondingLoc;
	}
}
