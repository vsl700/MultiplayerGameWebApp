package com.vsl700.pong.tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.vsl700.pong.PongBall;
import com.vsl700.pong.PongPlate;
import com.vsl700.pong.PongTable;
import com.vsl700.pong.messages.GameMessageListener;
import com.vsl700.pong.messages.MessageType;

class ScoringTest implements GameMessageListener {

	@Test
	void test() {
		PongTable table = new PongTable();
		table.createNewPongPlate(this);
		PongPlate plate2 = table.createNewPongPlate(this);
		table.ball.moveVec.set(1 * PongBall.SPEED, 0);
		table.ball.getLocation().set(PongTable.WIDTH - plate2.getHeight(), plate2.getLocation().getY());
		
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		Assertions.assertFalse(plate2.getScore() < 11);
	}

	@Override
	public void sendMessage(MessageType type, PongPlate plate, PongBall ball) {
		System.out.println(type.createTextMessage(plate, ball));
	}

}
