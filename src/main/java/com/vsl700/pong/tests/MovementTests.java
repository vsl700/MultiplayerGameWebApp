package com.vsl700.pong.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.vsl700.pong.Location;
import com.vsl700.pong.PongBall;
import com.vsl700.pong.PongPlate;
import com.vsl700.pong.PongTable;
import com.vsl700.pong.TableDirections;
import com.vsl700.pong.messages.GameMessageListener;
import com.vsl700.pong.messages.MessageType;

class MovementTests implements GameMessageListener {

	@Test
	void plateMoveUp() {
		PongTable tempTable = new PongTable();
		PongPlate plate1 = tempTable.createNewPongPlate(this);
		
		Location prevLoc = new Location(plate1.getLocation());
		plate1.moveUp();
		plate1.update();
		Location newLoc = new Location(plate1.getLocation());
		
		assertTrue(prevLoc.getX() != newLoc.getX() || prevLoc.getY() != newLoc.getY());
	}
	
	@Test
	void plateMoveDown() {
		PongTable tempTable = new PongTable();
		PongPlate plate1 = tempTable.createNewPongPlate(this);
		
		Location prevLoc = new Location(plate1.getLocation());
		plate1.moveDown();
		plate1.update();
		Location newLoc = new Location(plate1.getLocation());
		
		assertTrue(prevLoc.getX() != newLoc.getX() || prevLoc.getY() != newLoc.getY());
	}
	
	@Test
	void plateMoveVecUp() {
		PongTable tempTable = new PongTable();
		PongPlate plate1 = tempTable.createNewPongPlate(this);
		PongPlate plate2 = tempTable.createNewPongPlate(this);
		PongPlate plate3 = tempTable.createNewPongPlate(this);
		PongPlate plate4 = tempTable.createNewPongPlate(this);
		
		plate1.moveUp();
		plate2.moveUp();
		plate3.moveUp();
		plate4.moveUp();
		
		Location expectedVec1 = new Location(0, 1).mul(PongPlate.SPEED);
		Location expectedVec2 = new Location(1, 0).mul(PongPlate.SPEED);
		
		assertEquals(expectedVec1.getX(), plate1.moveVec.getX());
		assertEquals(expectedVec1.getY(), plate1.moveVec.getY());
		
		assertEquals(expectedVec1.getX(), plate2.moveVec.getX());
		assertEquals(expectedVec1.getY(), plate2.moveVec.getY());
		
		assertEquals(expectedVec2.getX(), plate3.moveVec.getX());
		assertEquals(expectedVec2.getY(), plate3.moveVec.getY());
		
		assertEquals(expectedVec2.getX(), plate4.moveVec.getX());
		assertEquals(expectedVec2.getY(), plate4.moveVec.getY());
	}
	
	@Test
	void plateMoveVecDown() {
		PongTable tempTable = new PongTable();
		PongPlate plate1 = tempTable.createNewPongPlate(this);
		PongPlate plate2 = tempTable.createNewPongPlate(this);
		PongPlate plate3 = tempTable.createNewPongPlate(this);
		PongPlate plate4 = tempTable.createNewPongPlate(this);
		
		plate1.moveDown();
		plate2.moveDown();
		plate3.moveDown();
		plate4.moveDown();
		
		Location expectedVec1 = new Location(0, -1).mul(PongPlate.SPEED);
		Location expectedVec2 = new Location(-1, 0).mul(PongPlate.SPEED);
		
		assertEquals(expectedVec1.getX(), plate1.moveVec.getX());
		assertEquals(expectedVec1.getY(), plate1.moveVec.getY());
		
		assertEquals(expectedVec1.getX(), plate2.moveVec.getX());
		assertEquals(expectedVec1.getY(), plate2.moveVec.getY());
		
		assertEquals(expectedVec2.getX(), plate3.moveVec.getX());
		assertEquals(expectedVec2.getY(), plate3.moveVec.getY());
		
		assertEquals(expectedVec2.getX(), plate4.moveVec.getX());
		assertEquals(expectedVec2.getY(), plate4.moveVec.getY());
	}
	
	@Test
	void ballMoveVec() {
		var sideDir = TableDirections.EAST;
		var upDownDir = TableDirections.NORTH;
		PongTable table = new PongTable();
		table.ball.setMovementDirection(sideDir, upDownDir);
		
		double expectedX = Math.cos(Math.toRadians(45)) * PongBall.SPEED;
		double actualX = table.ball.moveVec.getX();
		double expectedY = Math.sin(Math.toRadians(45)) * PongBall.SPEED;
		double actualY = table.ball.moveVec.getY();
		
		assertEquals((float) expectedX, (float) actualX);
		assertEquals((float) expectedY, (float) actualY);
	}

	@Override
	public void sendMessage(MessageType type, PongPlate plate, PongBall ball) {
		System.out.println(type.createTextMessage(plate, ball));
	}

}
