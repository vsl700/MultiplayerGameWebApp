var gameConsole = document.getElementById("console");
var canvas = document.getElementById("game-canvas");
var ctx = canvas.getContext("2d");

var websocket = new WebSocket("ws://localhost:8080/MultiplayerGameWebApp/websocket/pong");

websocket.onopen = (message) => { log("Connected..."); console.log(message); }
websocket.onmessage = (message) => { console.log(message); processMessage(message.data); }
websocket.onclose = (message) => { log("Disconnected..."); console.log(message); }
websocket.onerror = (message) => { log("Error...\n" + message); }

function log(message){
	let p = document.createElement("p");
	p.className = "console-item";
	p.innerText = message;
	gameConsole.append(p);
}

document.addEventListener("keydown", (event) => {
	if(event.code === "ArrowUp" || event.code === "ArrowRight") 
		moveUp();
	else if(event.code === "ArrowDown" || event.code === "ArrowLeft")
		moveDown();
});

document.addEventListener("keyup", stopMoving);

///////////////////////////////////////////////////////////////////////////////////////////////////

const Player = function(playerName, playerX, playerY, playerWidth, playerHeight, playerScore){
	const name = playerName;
	const x = playerX;
	const y = playerY;
	const width = playerWidth;
	const height = playerHeight;
	const score = playerScore;
	
	return {
		name,
		x,
		y,
		width,
		height,
		score
	}
}

const Ball = function(ballX, ballY, ballSize){
	const x = ballX;
	const y = ballY;
	const size = ballSize;
	
	return {
		x,
		y,
		size
	}
}

var players = [];
//var currentPlayer;
var ball;

function processMessage(message){
	log(message);
	
	let obj = JSON.parse(message);
	
	switch(obj.type){
		case 'CONNECTED':
			players[obj.name] = players[players.length] = Player(obj.name, obj.x, obj.y, obj.w, obj.h, obj.score);
			//if(currentPlayer === undefined)
				//currentPlayer = players[obj.name];
			
			ball = Ball(0, 0, obj.ballSize);
			canvasRender();
			break;
		case 'DISCONNECTED': 
			players[obj.name] = null;
			for(let i = 0; i < players.length; i++){
				if(players[i] == null)
					continue;
				
				if(players[i].name === obj.name){
					players[i] = null;
					break;
				}
			}
			
			canvasRender();
			break;
		case 'UPDATE':
			if(obj.name === "ball"){
				ball.x = obj.x;
				ball.y = obj.y;
				canvasRender();
				return;
			}
			
			if(players[obj.name] === undefined){
				players[obj.name] = players[players.length] = Player(obj.name, obj.x, obj.y, obj.w, obj.h, obj.score);
				canvasRender();
				return;
			}
			
			players[obj.name].x = obj.x;
			players[obj.name].y = obj.y;
			players[obj.name].score = obj.score;
			canvasRender();
			break;
		case 'LOSS': 
			alert('Game Over!');
			break;
		case 'WIN': 
			alert('Congrats! YOU WIN!!!');
			break;
	}
}

function canvasRender(){
	//ctx.clearRect(0, 0, canvas.width, canvas.height);
	
	ctx.fillStyle = 'black';
	ctx.fillRect(0, 0, canvas.width, canvas.height);
	
	ctx.font = "13px Arial";
	
	players.forEach((player) => {
		if(player == null)
			return;
		
		ctx.fillStyle = 'white';
		ctx.fillRect(player.x, canvas.height - player.y - player.height, player.width, player.height);
		ctx.fillStyle = 'gray';
		ctx.fillText(player.score, player.x, canvas.height - (player.y + player.height / 2));
	});
	
	ctx.beginPath();
	ctx.fillStyle = 'white';
	ctx.arc(ball.x + ball.size / 2, canvas.height - (ball.y + ball.size / 2), ball.size / 2, 0, 2 * Math.PI);
	ctx.fill();
	ctx.stroke();
}

function moveUp(){
	websocket.send("up");
	log("send: up");
}

function moveDown(){
	websocket.send("down");
	log("send: down");
}

function stopMoving(){
	websocket.send("none");
	log("send: none");
}