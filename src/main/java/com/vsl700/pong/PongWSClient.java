package com.vsl700.pong;

import java.io.IOException;
import java.util.LinkedList;

import com.vsl700.pong.messages.GameMessageListener;
import com.vsl700.pong.messages.MessageType;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

@ServerEndpoint("/websocket/pong")
public class PongWSClient implements GameMessageListener {
	
	private PongTable table;
	/* private */ public PongPlate plate;
	private Session session;
	
	/* private */ public static final LinkedList<PongTable> tables = new LinkedList<PongTable>(); 
	
	@OnOpen
	public void onOpen(Session session) {
		this.session = session;
		
		synchronized(tables) {
			setupTable();
			
			plate = table.createNewPongPlate(this);
			table.sendMessageToAll(MessageType.CONNECTED, plate, null);
		}
		
		System.out.println(tables.size() + " tables are already in game");
	}
	
	private void setupTable() {
		for(var table : tables) {
			if(!table.isTableFull() && !table.isGameOver()) {
				this.table = table;
				return;
			}
		}
		
		table = new PongTable();
		tables.add(table);
	}
	
	@Override
	public void sendMessage(MessageType type, PongPlate plate, PongBall ball) {
		try {
			session.getBasicRemote().sendText(type.createTextMessage(plate, ball));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@OnMessage
	public void onMessage(String message) {
		if(message.equals("up")) {
			plate.moveUp();
		}else if(message.equals("down")) {
			plate.moveDown();
		}else if(message.equals("none")) {
			plate.stopMoving();
		}
	}
	
	@OnClose
	public void onClosed(Session session) {
		table.removePlayer(plate);
		table.sendMessageToAll(MessageType.DISCONNECTED, plate, null);
		
		synchronized(tables) {
			if(table.isTableEmpty()) {
				table.disposeTable();
				tables.remove(table);
			}
		}
		
		System.out.println(tables.size() + " tables are already in game");
	}
	
}
