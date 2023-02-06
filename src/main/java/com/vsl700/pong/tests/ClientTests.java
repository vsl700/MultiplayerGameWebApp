package com.vsl700.pong.tests;

import java.util.LinkedList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.junit.jupiter.api.Test;

import com.vsl700.pong.PongBall;
import com.vsl700.pong.PongPlate;
import com.vsl700.pong.PongWSClient;
import com.vsl700.pong.messages.MessageType;

class ClientTests implements Runnable{
	
	private static final LinkedList<PongWSClient> clients = new LinkedList<PongWSClient>();
	
	@Test
	void test() {
		for(int i = 10; i > 0; i--) {
			Thread[] threads = new Thread[4];
			for(int j = 0; j < threads.length; j++) {
				threads[j] = new Thread(this);
				threads[j].start();
			}
			
			try {
				Thread.sleep(30000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void run() {
		var temp = new PongWSClient() {
			@Override
			public void sendMessage(MessageType type, PongPlate plate, PongBall ball) {
				System.out.println(type.createTextMessage(plate, ball));
			}
		};
		temp.onOpen(null);
		clients.add(temp);
		
		Timer timer = new Timer(ClientTests.class.getSimpleName() + " JUnit timer");
		timer.schedule(new TimerTask() {
			Random r = new Random();
			
			@Override
			public void run() {
				int randNum = r.nextInt(3);
				switch(randNum) {
				case 0: temp.onMessage("up"); break;
				case 1: temp.onMessage("down"); break;
				case 2: temp.onMessage("none"); break;
				}
			}
			
		}, 500, 500);
	}

}
