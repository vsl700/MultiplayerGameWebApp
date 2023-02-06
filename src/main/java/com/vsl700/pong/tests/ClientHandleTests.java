package com.vsl700.pong.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import com.vsl700.pong.Location;
import com.vsl700.pong.PongBall;
import com.vsl700.pong.PongPlate;
import com.vsl700.pong.PongWSClient;
import com.vsl700.pong.messages.MessageType;

class ClientHandleTests {
	
	private static final ArrayList<PongWSClient> clients = new ArrayList<>();

	@Test
	void clientsCount() {
		removeAllClients();
		
		addAndRemoveClients();
		addAndRemoveClients();
		addAndRemoveClients();
		addAndRemoveClients();
		
		assertEquals(4, PongWSClient.tables.size());
	}
	
	@Test
	void clientsSpawnDir() {
		removeAllClients();
		
		addAndRemoveClients();
		
		ArrayList<Location> tempLocs = new ArrayList<Location>();
		for(var client : clients) {
			Location clientLoc = client.plate.getLocation();
			for(var loc : tempLocs) {
				if(loc.copy().sub(clientLoc).len() == 0) {
					fail("Two similar locations detected! (" + "player" + client.plate.getId() + " : " + loc + ")");
				}
			}
			
			tempLocs.add(clientLoc);
		}
	}
	
	private void printTableSizes() {
		System.out.println("\n");
		for(var table : PongWSClient.tables) {
			System.out.println(table.plates.size());
		}
		
		System.out.println("\n");
	}
	
	/**
	 * After this method the (remaining) new clients will be 3
	 */
	private void addAndRemoveClients() {
		for(int i = 0; i < 3; i++) {
			addNewClient();
		}
		
		removeClient(1);
		
		for(int i = 0; i < 2; i++) {
			addNewClient();
		}
		
		removeClient(1);
		removeClient(2);
		
		addNewClient();
	}
	
	private void addNewClient() {
		var temp = new PongWSClient() {
			@Override
			public void sendMessage(MessageType type, PongPlate plate, PongBall ball) {
				//System.out.println(type.createTextMessage(plate, ball));
			}
		};
		
		
		
		temp.onOpen(null);
		clients.add(temp);
		
		printTableSizes();
	}
	
	private void removeClient(int i) {
		clients.get(i).onClosed(null);
		clients.remove(i);
		
		printTableSizes();
	}
	
	private void removeAllClients() {
		for(var client : clients) {
			client.onClosed(null);
		}
		
		clients.clear();
	}

}
