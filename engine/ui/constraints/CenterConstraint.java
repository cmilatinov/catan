package ui.constraints;

import ui.UIConstraint;
import ui.UIConstraints;
import ui.UIDimensions;

import static ui.UIDimensions.DIMENSION_X;
import static ui.UIDimensions.DIMENSION_Y;

public class CenterConstraint extends UIConstraint {

	private final int offset;

	public CenterConstraint() {
		super(UIConstraints.PRIORITY_FIRST);
		offset = 0;
	}

	public CenterConstraint(int offset) {
		super(UIConstraints.PRIORITY_FIRST);
		this.offset = offset;
	}

	public int compute(UIDimensions parent, UIDimensions computed, int dimension) {
		int computedValue = 0;
		switch (dimension) {
			case DIMENSION_X -> computedValue = parent.getCenterX() - computed.getWidth() / 2;
			case DIMENSION_Y-> computedValue = parent.getCenterY() - computed.getHeight() / 2;
		}
		computedValue += offset;
		return computedValue;
	}
	
}
