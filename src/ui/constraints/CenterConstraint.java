package ui.constraints;

import static ui.UIDimensions.DIMENSION_X;
import static ui.UIDimensions.DIMENSION_Y;

import ui.UIConstraint;
import ui.UIConstraints;
import ui.UIDimensions;

public class CenterConstraint extends UIConstraint {
	
	public CenterConstraint() {
		super(UIConstraints.PRIORITY_FIRST);
	}

	@Override
	public int compute(UIDimensions parent, UIDimensions computed, int dimension) {
		switch(dimension) {
			case DIMENSION_X:
				return parent.getCenterX() - computed.getWidth() / 2;
				
			case DIMENSION_Y:
				return parent.getCenterY() - computed.getHeight() / 2;
				
			default:
				return 0;
		}
	}
	
}
