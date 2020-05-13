package ui.constraints;

import static ui.UIDimensions.DIMENSION_X;
import static ui.UIDimensions.DIMENSION_Y;
import static ui.UIDimensions.DIMENSION_HEIGHT;
import static ui.UIDimensions.DIMENSION_WIDTH;
import static ui.UIDimensions.DIRECTION_NEGATIVE;

import ui.UIConstraint;
import ui.UIConstraints;
import ui.UIDimensions;

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
		switch(dimension) {
			case DIMENSION_WIDTH:
			case DIMENSION_HEIGHT:
				return size;
				
			case DIMENSION_X:
				return dir == DIRECTION_NEGATIVE ? 
						parent.getLeftX() + size : 
						parent.getRightX() - size - computed.getWidth();
				
			case DIMENSION_Y:
				return dir == DIRECTION_NEGATIVE ? 
					parent.getTopY() + size : 
					parent.getBottomY() - size - computed.getHeight();
				
			default:
				return 0;
		}
	}
	
}
