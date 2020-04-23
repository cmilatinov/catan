package gameplay.cards;

public class DevelopmentCard {

	private String name;
	private String description;
	private int quantity;
	private Runnable activation;
	
	public DevelopmentCard(String name, String description, int quantity) {
		this.name = name;
		this.description = description;
		this.quantity = quantity;
	}
	
	public DevelopmentCard(String name, String description, int quantity, Runnable activation) {
		this.name = name;
		this.description = description;
		this.quantity = quantity;
		this.activation = activation;
	}
	
}
