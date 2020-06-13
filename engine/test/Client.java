package test;

import network.NetworkModule;
import network.events.EventTest;
import network.managers.GameClient;

import java.util.Scanner;

public class Client {
	
	public static void main(String[] args) {
		NetworkModule.initialize();
		GameClient client = new GameClient();
		if(!client.isReady())
			return;
		
		client.start();
		client.onConnect(() -> System.out.println("Connected"));

		Scanner scanner = new Scanner(System.in);
		while (client.isRunning()) {
			try {
				int next = scanner.nextInt();
				switch (next) {
					case 0 -> client.halt();
					case 1 -> {
						System.out.println("SENDING");
						client.sendEvent(new EventTest());
					}
					case 2 -> client.connect("localhost", 50000, "rednite");
				}
			} catch (Exception ignored) {}
		}
	}
	
}
