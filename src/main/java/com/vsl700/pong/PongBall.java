package com.vsl700.pong;

public class PongBall {
	
	private Location pos;
	/*private*/ public Location moveVec;
	
	/*private*/ public static final int SPEED = 15;
	public static final int SIZE = 15;
	
	public PongBall() {
		pos = new Location(PongTable.WIDTH / 2, PongTable.HEIGHT / 2);
		moveVec = new Location(0, 0);
		setMovementDirection(TableDirections.EAST, TableDirections.NORTH);
	}
	
	public boolean checkCollision(Location loc, int width, int height) {
		float x1 = pos.getX();
		float y1 = pos.getY();
		float x2 = loc.getX();
		float y2 = loc.getY();
		
		boolean b1 = x1 < x2 + width;
		boolean b2 = x1 + SIZE >= x2;
		boolean b3 = y1 < y2 + height;
		boolean b4 = y1 + SIZE >= y2;
		boolean collision = b1 && b2 && b3 && b4;
		
		
		return collision;
	}
	
	private TableDirections sideDir, upDownDir;
	public void setMovementDirection(TableDirections sideDir, TableDirections upDownDir) {
		if(sideDir == null)
			sideDir = this.sideDir;
		else 
			this.sideDir = sideDir;
		
		if(upDownDir == null)
			upDownDir = this.upDownDir;
		else 
			this.upDownDir = upDownDir;
		
		Location center = new Location(PongTable.WIDTH / 2, PongTable.HEIGHT / 2);
		Location tempSideVec = sideDir.getCorrespondingLocation().copy().sub(center);
		Location tempUpDownVec = upDownDir.getCorrespondingLocation().copy().sub(center);
		
		Location tempTotalVec = new Location(tempSideVec.normalize()).add(tempUpDownVec.normalize());
		tempTotalVec.normalize(); // Turn it to a vector with length=1
		
		moveVec.set(tempTotalVec);
		moveVec.mul(SPEED, SPEED / 2);
	}
	
	public void update() {
		pos.add(moveVec);
		
		if(pos.getX() <= 0) {
			setMovementDirection(TableDirections.EAST, upDownDir);
		}else if(pos.getX() + SIZE >= PongTable.WIDTH) {
			setMovementDirection(TableDirections.WEST, upDownDir);
		}
		
		if(pos.getY() <= 0) {
			setMovementDirection(sideDir, TableDirections.NORTH);
		}else if(pos.getY() + SIZE >= PongTable.HEIGHT) {
			setMovementDirection(sideDir, TableDirections.SOUTH);
		}
	}

	public Location getLocation() {
		return pos;
	}
}
