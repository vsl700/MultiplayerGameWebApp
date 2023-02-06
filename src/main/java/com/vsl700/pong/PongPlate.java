package com.vsl700.pong;

import com.vsl700.pong.messages.GameMessageListener;
import com.vsl700.pong.messages.MessageType;

public class PongPlate {
	
	private GameMessageListener messageListener;
	private Location location;
	private TableDirections spawnDir;
	/*private*/ public Location moveVec;
	private int width;
	private int height;
	private int score;
	private int id;
	
	/*private*/public static final int SPEED = 10;
	public static final int BIG_SIZE = 60;
	public static final int SMALL_SIZE = 20;
	
	public PongPlate(int id, TableDirections spawnDir, GameMessageListener messageListener) {
		this.id = id;
		this.spawnDir = spawnDir;
		this.messageListener = messageListener;
		moveVec = new Location(0, 0);
		switch(spawnDir) {
		case EAST: case WEST: 
			width = SMALL_SIZE;
			height = BIG_SIZE;
			location = spawnDir.getCorrespondingLocation().copy().sub(0, height / 2);
			break;
		case NORTH: case SOUTH: 
			width = BIG_SIZE;
			height = SMALL_SIZE;
			location = spawnDir.getCorrespondingLocation().copy().sub(height / 2, 0);
			break;
		}
		
		score = 11;
	}
	
	public void moveUp() {
		if(score <= 0)
			return;
		
		if(spawnDir.equals(TableDirections.WEST) || spawnDir.equals(TableDirections.EAST)) {
			calculateMoveVec(TableDirections.NORTH);
		}else { // If it's sideways
			calculateMoveVec(TableDirections.EAST);
		}
	}
	
	public void moveDown() {
		if(score <= 0)
			return;
		
		if(spawnDir.equals(TableDirections.WEST) || spawnDir.equals(TableDirections.EAST)) {
			calculateMoveVec(TableDirections.SOUTH);
		}else { // If it's sideways
			calculateMoveVec(TableDirections.WEST);
		}
	}
	
	public void stopMoving() {
		moveVec.set(0, 0);
	}
	
	private void calculateMoveVec(TableDirections moveDir) {
		Location center = new Location(PongTable.WIDTH / 2, PongTable.HEIGHT / 2);
		moveVec = moveDir.getCorrespondingLocation().copy().sub(center);
		moveVec.normalize(); // Turn it to a vector with length=1
		moveVec.mul(SPEED);
	}
	
	public void update() {
		if(!isSpawnOnSide() && (location.getX() + moveVec.getX() < 0 || location.getX() + moveVec.getX() + width > PongTable.WIDTH) || 
				isSpawnOnSide() && (location.getY() + moveVec.getY() < 0 || location.getY() + moveVec.getY() + height > PongTable.HEIGHT))
			return;
		
		location.add(moveVec);
	}
	
	public boolean isSpawnOnSide() {
		return spawnDir.equals(TableDirections.EAST) || spawnDir.equals(TableDirections.WEST); 
	}
	
	public void sendMessage(MessageType type, PongPlate plate, PongBall ball) {
		messageListener.sendMessage(type, plate, ball);
	}
	
	public Location getLocation() {
		return location;
	}
	
	public TableDirections getSpawnDir() {
		return spawnDir;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getScore() {
		return score;
	}

	public void decreaseScore() {
		score--;
	}

	public int getId() {
		return id;
	}
}
