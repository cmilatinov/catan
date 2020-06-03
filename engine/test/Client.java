package test;

import network.GameClient;
import network.NetworkModule;

public class Client {
	
	public static void main(String[] args) {
		NetworkModule.initialize();
		GameClient client = new GameClient();
		if(!client.isReady())
			return;
		
		client.start();
		client.onConnect(() -> System.out.println("Connected"));
		client.connect("localhost", 50000, "rednite");
	}
	
}
