package test;

import network.GameServer;
import network.NetworkModule;

public class Server {

	public static void main(String[] args) {
		NetworkModule.initialize();
		GameServer server = new GameServer(50000);
		if(!server.isReady())
			return;
		
		server.start();
	}
	
}
