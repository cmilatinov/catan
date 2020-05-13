package gameplay;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;

import display.Window;
import gui.GUI;

public class PlayerHand {
	
	private final List<GUI> cards = new ArrayList<GUI>();
	
	private static final float FACTOR_RADIUS = 1.5f; 
	private static final float FACTOR_ELEVATION = 1.0f / 12.0f;
	
	private final Window window;
	
	private final Vector2f center;
	
	public PlayerHand(Window window) {
		this.window = window;
		this.center = new Vector2f(window.getWidth() / 2, window.getHeight() + window.getHeight() * FACTOR_RADIUS - window.getHeight() * FACTOR_ELEVATION);
	}
	
	public void update() {
		float handSpan = Math.min((cards.size() - 1) * 3, 15);
		float angle = handSpan;
		float increment = 2 * handSpan / (cards.size() - 1);
		for(int i = 1; i <= cards.size(); i++ ) {
			GUI card = cards.get(i - 1);
			card.setRotation(-angle);
			card.setPosition(new Vector2f(
					center.x - window.getHeight() * FACTOR_RADIUS * (float)Math.sin(Math.toRadians(angle)), 
					center.y - window.getHeight() * FACTOR_RADIUS * (float)Math.cos(Math.toRadians(angle))));
			card.setElevation(i);
			angle -= increment;
		}
	}
	
	public void addCard(GUI card) {
		cards.add(card);
	}
}
