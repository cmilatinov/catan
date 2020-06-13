package test;

import network.NetworkModule;
import network.managers.GameServer;

import java.util.Scanner;

public class Server {

	public static void main(String[] args) {
		NetworkModule.initialize();
		GameServer server = new GameServer(50000);
		if(!server.isReady())
			return;
		
		server.start();

		Scanner scanner = new Scanner(System.in);
		while (server.isRunning()) {
			try {
				int next = scanner.nextInt();
			} catch (Exception ignored) {}
		}

	}
	
}
