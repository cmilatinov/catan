package ui.constraints;

import static ui.UIDimensions.DIMENSION_WIDTH;
import static ui.UIDimensions.DIMENSION_HEIGHT;

import ui.UIConstraint;
import ui.UIConstraints;
import ui.UIDimensions;

public class AspectConstraint extends UIConstraint {
	
	private final float aspect;
	
	public AspectConstraint(float aspect) {
		super(UIConstraints.PRIORITY_LAST);
		this.aspect = aspect;
	}

	public int compute(UIDimensions parent, UIDimensions computed, int dimension) {
		switch(dimension) {
			case DIMENSION_WIDTH:
				return Math.round(computed.getHeight() * aspect);
				
			case DIMENSION_HEIGHT:
				return Math.round(computed.getWidth() / aspect);
				
			default:
				return 0;
		}
	}
	
}
