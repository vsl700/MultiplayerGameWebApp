package com.vsl700.pong.messages;

import com.vsl700.pong.PongBall;
import com.vsl700.pong.PongPlate;

public interface GameMessageListener {

	void sendMessage(MessageType type, PongPlate plate, PongBall ball);
	
}
