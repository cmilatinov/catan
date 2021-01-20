package ui.constraints;

import ui.UIConstraint;
import ui.UIConstraints;
import ui.UIDimensions;

import static ui.UIDimensions.DIMENSION_HEIGHT;
import static ui.UIDimensions.DIMENSION_WIDTH;

public class AspectConstraint extends UIConstraint {
	
	private final float aspect;
	
	public AspectConstraint(float aspect) {
		super(UIConstraints.PRIORITY_LAST);
		this.aspect = aspect;
	}

	public int compute(UIDimensions parent, UIDimensions computed, int dimension) {
		return switch (dimension) {
			case DIMENSION_WIDTH -> Math.round(computed.getHeight() * aspect);
			case DIMENSION_HEIGHT -> Math.round(computed.getWidth() / aspect);
			default -> 0;
		};
	}
	
}
