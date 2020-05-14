package ui;

public abstract class UIConstraint {
	
	private final int priority;
	
	public UIConstraint(int priority) {
		this.priority = priority;
	}
	
	public int getPriority() {
		return priority;
	}
	
	public abstract int compute(UIDimensions parent, UIDimensions computed, int dimension);
	
}

