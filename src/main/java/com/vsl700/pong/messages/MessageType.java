package com.vsl700.pong.messages;

import org.json.simple.JSONObject;

import com.vsl700.pong.PongBall;
import com.vsl700.pong.PongPlate;

@SuppressWarnings("unchecked")
public enum MessageType {
	CONNECTED {
		@Override
		public String createTextMessage(PongPlate plate, PongBall ball) {
			JSONObject obj = new JSONObject();
			obj.put("type", "CONNECTED");
			obj.put("name", "player" + plate.getId());
			obj.put("x", plate.getLocation().getX());
			obj.put("y", plate.getLocation().getY());
			obj.put("w", plate.getWidth());
			obj.put("h", plate.getHeight());
			obj.put("score", plate.getScore());
			//obj.put("ballX", ball.getLocation().getX());
			//obj.put("ballY", ball.getLocation().getY());
			obj.put("ballSize", PongBall.SIZE);
			
			return obj.toJSONString();
		}
	},
	DISCONNECTED {
		@Override
		public String createTextMessage(PongPlate plate, PongBall ball) {
			JSONObject obj = new JSONObject();
			obj.put("type", "DISCONNECTED");
			obj.put("name", "player" + plate.getId());
			
			return obj.toJSONString();
		}
	},
	UPDATE {
		@Override
		public String createTextMessage(PongPlate plate, PongBall ball) {
			JSONObject obj = new JSONObject();
			obj.put("type", "UPDATE");
			if(plate == null) {
				obj.put("name", "ball");
				obj.put("x", ball.getLocation().getX());
				obj.put("y", ball.getLocation().getY());
			}else {
				obj.put("name", "player" + plate.getId());
				obj.put("x", plate.getLocation().getX());
				obj.put("y", plate.getLocation().getY());
				obj.put("w", plate.getWidth());
				obj.put("h", plate.getHeight());
				obj.put("score", plate.getScore());
			}
			
			return obj.toJSONString();
		}
	},
	LOSS {
		@Override
		public String createTextMessage(PongPlate plate, PongBall ball) {
			JSONObject obj = new JSONObject();
			obj.put("type", "LOSS");
			obj.put("name", "player" + plate.getId());
			
			return obj.toJSONString();
		}
	},
	WIN {
		@Override
		public String createTextMessage(PongPlate plate, PongBall ball) {
			JSONObject obj = new JSONObject();
			obj.put("type", "WIN");
			obj.put("name", "player" + plate.getId());
			
			return obj.toJSONString();
		}
	};
	
	public abstract String createTextMessage(PongPlate plate, PongBall ball);
}
