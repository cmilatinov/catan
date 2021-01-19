package ui.constraints;

import ui.UIConstraint;
import ui.UIConstraints;
import ui.UIDimensions;

import static ui.UIDimensions.*;

public class PixelConstraint extends UIConstraint {
	
	private final int size;
	private final int dir;
	
	public PixelConstraint(int size) {
		super(UIConstraints.PRIORITY_FIRST);
		this.size = size;
		this.dir = DIRECTION_NEGATIVE;
	}
	
	public PixelConstraint(int size, int direction) {
		super(UIConstraints.PRIORITY_FIRST);
		this.size = size;
		this.dir = direction;
	}

	public int compute(UIDimensions parent, UIDimensions computed, int dimension) {
		return switch (dimension) {
			case DIMENSION_WIDTH, DIMENSION_HEIGHT -> size;
			case DIMENSION_X -> dir == DIRECTION_NEGATIVE ?
					parent.getLeftX() + size :
					parent.getRightX() - size - computed.getWidth();
			case DIMENSION_Y -> dir == DIRECTION_NEGATIVE ?
					parent.getTopY() + size :
					parent.getBottomY() - size - computed.getHeight();
			default -> 0;
		};
	}
	
}
