package com.vsl700.pong;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

import com.vsl700.pong.messages.GameMessageListener;
import com.vsl700.pong.messages.MessageType;

public class PongTable {

	private Timer timer;
	/* private */ public ArrayList<PongPlate> plates;
	private AtomicInteger plateIds;
	/*private*/ public PongBall ball;
	private TableDirections spawnDir;
	private boolean gameOver;
	
	private static final int TICK_DELAY = 70;
	public static final int WIDTH = 640;
	public static final int HEIGHT = 480;
	private static final boolean CHEAT = true;
	
	public PongTable() {
		timer = new Timer(PongTable.class.getSimpleName() + " Timer");
		timer.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				update();
			}
			
		}, TICK_DELAY, TICK_DELAY);
		
		plates = new ArrayList<PongPlate>(TableDirections.values().length);
		plateIds = new AtomicInteger();
		ball = new PongBall();
		
		gameOver = false;
	}
	
	public PongPlate createNewPongPlate(GameMessageListener messageListener) {
		if(isTableFull()) 
			return null;
		
		spawnDir = nextSpawnDirection();
		PongPlate temp = new PongPlate(plateIds.getAndIncrement(), spawnDir, messageListener);
		synchronized(plates) {
			plates.add(temp);
		}
		
		return temp;
	}
	
	private void cheat(PongPlate plate) {
		if(plate.isSpawnOnSide()) {
			plate.getLocation().set(plate.getLocation().getX(), ball.getLocation().getY() - plate.getHeight() / 2);
		}else {
			plate.getLocation().set(ball.getLocation().getX() - plate.getWidth() / 2, plate.getLocation().getY());
		}
	}
	
	private void update() {
		if(plates.size() < 2)
			return;
		
		ball.update();
		
		synchronized(plates) {
			for(var plate : plates) {
				if(CHEAT)
					cheat(plate);
				
				plate.update();
				sendMessageToAll(MessageType.UPDATE, plate, null);
				plate.sendMessage(MessageType.UPDATE, null, ball);
				
				switch(plate.getSpawnDir()) {
				case WEST: 
					if(ball.getLocation().getX() <= plate.getLocation().getX() + plate.getWidth()) {
						if(ball.checkCollision(plate.getLocation(), plate.getWidth(), plate.getHeight())) {
							ball.setMovementDirection(TableDirections.EAST, null);
						}else if(plate.getScore() > 0 && ball.getLocation().getX() <= 0){
							platePenalty(plate);
						}
					}
					
					break;
				case EAST: 
					if(ball.getLocation().getX() + PongBall.SIZE >= plate.getLocation().getX()) {
						if(ball.checkCollision(plate.getLocation(), plate.getWidth(), plate.getHeight())) {
							ball.setMovementDirection(TableDirections.WEST, null);
						}else if(plate.getScore() > 0 && ball.getLocation().getX() + PongBall.SIZE >= WIDTH){
							platePenalty(plate);
						}
					}
					
					break;
				case SOUTH:
					if(ball.getLocation().getY() <= plate.getLocation().getY() + plate.getHeight()) {
						if(ball.checkCollision(plate.getLocation(), plate.getWidth(), plate.getHeight())) {
							ball.setMovementDirection(null, TableDirections.NORTH);
						}else if(plate.getScore() > 0 && ball.getLocation().getY() <= 0){
							platePenalty(plate);
						}
					}
					
					break;
				case NORTH: 
					if(ball.getLocation().getY() + PongBall.SIZE >= plate.getLocation().getY()) {
						if(ball.checkCollision(plate.getLocation(), plate.getWidth(), plate.getHeight())) {
							ball.setMovementDirection(null, TableDirections.SOUTH);
						}else if(plate.getScore() > 0 && ball.getLocation().getY() + PongBall.SIZE >= HEIGHT){
							platePenalty(plate);
						}
					}
					
					break;
				}
			}
		}
	}
	
	private void platePenalty(PongPlate plate) {
		plate.decreaseScore();
		if(plate.getScore() <= 0) {
			plate.sendMessage(MessageType.LOSS, plate, null);
			
			PongPlate tempPlate = getWinner();
			if(tempPlate != null) {
				tempPlate.sendMessage(MessageType.WIN, tempPlate, null);
				timer.cancel();
				gameOver = true;
			}
		}else 
			plate.sendMessage(MessageType.UPDATE, plate, null);
		
		ball.getLocation().set(WIDTH / 2, HEIGHT / 2);
	}
	
	private PongPlate getWinner() {
		PongPlate temp = null;
		for(var plate : plates) {
			if(plate.getScore() > 0) {
				if(temp == null)
					temp = plate;
				else 
					return null;
			}
		}
		
		return temp;
	}
	
	public void sendMessageToAll(MessageType type, PongPlate plate, PongBall ball) {
		System.out.println("Sending message to all (" + type.toString() + ")");
		synchronized(plates) {
			for(var targetPlate : plates) {
				targetPlate.sendMessage(type, plate, ball);
			}
		}
	}
	
	public void removePlayer(PongPlate plate) {
		synchronized(plates) {
			plates.remove(plate);
		}
	}
	
	public void disposeTable() {
		timer.cancel();
	}
	
	private TableDirections nextSpawnDirection() {
		for(var tempDir : TableDirections.values()) {
			boolean flag = false;
			for(var plate : plates) {
				if(plate.getSpawnDir().equals(tempDir)) {
					flag = true;
					break;
				}
			}
			
			if(!flag)
				return tempDir;
		}
		
		return null;
	}
	
	/**
	 * If this is true then this table will be unavailable for new players and can only be removed 
	 * from the game once all players leave
	 * @return
	 */
	public boolean isGameOver() {
		return gameOver;
	}
	
	public boolean isTableFull() {
		return plates.size() >= TableDirections.values().length;
	}
	
	public boolean isTableEmpty() {
		return plates.size() == 0;
	}
	
}
