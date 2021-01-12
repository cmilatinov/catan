package test;

import network.events.EventTest;
import network.NetworkModule;
import network.managers.GameClient;

import java.util.Scanner;

public class Client {
	
	public static void main(String[] args) {
		NetworkModule.initialize();
		GameClient client = new GameClient();
		if(!client.isReady())
			return;

		final Runnable exit = () -> {
			client.halt();
			System.exit(0);
		};
		
		client.start();
		client.onDisconnect(exit);
		client.onTimeout(exit);
		client.onConnect(() -> System.out.println("Connected"));

		Scanner scanner = new Scanner(System.in);
		while (client.isRunning()) {
			try {
				int next = scanner.nextInt();
				switch (next) {
					case 0 -> client.connect("localhost", 50000, "rednite");
					case 1 -> {
						System.out.println("SENDING");
						client.sendEvent(new EventTest("Hello from client"));
					}
					case 2 -> client.halt();
					case 3 -> client.disconnect();
				}
			} catch (Exception ignored) {}
		}
	}
}
