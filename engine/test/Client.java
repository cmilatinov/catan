package test;

import network.events.EventTest;
import network.managers.GameClient;

import java.util.Scanner;

public class Client {

	private static String username = "rednite";
	
	public static void main(String[] args) {
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
					case 0 -> client.connect("localhost", 50000, username);
					case 1 -> client.sendEvent(new EventTest("Hello from " + username));
					case 2 -> exit.run();
					case 3 -> client.disconnect();
					case 4 -> {
						System.out.println("Enter your username: ");
						scanner.nextLine();
						username = scanner.nextLine();
					}
					case 5 -> {
						System.out.println("Enter your message: ");
						scanner.nextLine();
						String msg = scanner.nextLine();
						client.sendEvent(new EventTest(msg));
					}
				}
			} catch (Exception ignored) {}
		}
	}
}
