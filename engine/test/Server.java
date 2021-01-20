package test;

import network.events.EventTest;
import network.managers.GameServer;

import java.util.Scanner;

public class Server {

	public static void main(String[] args) {
		GameServer server = new GameServer(50000);
		if(!server.isReady())
			return;
		
		server.start();

		Scanner scanner = new Scanner(System.in);
		while (server.isRunning()) {
			try {
				int next = scanner.nextInt();
				switch (next) {
					case 0 -> server.broadcastEvent(new EventTest("Hello from server"));
					case 1 -> {
						server.halt();
						System.exit(0);
					}
					case 2 -> {
						System.out.println("Enter your message: ");
						String msg = scanner.nextLine();
						server.broadcastEvent(new EventTest(msg));
					}
				}
			} catch (Exception ignored) {}
		}

	}
	
}
